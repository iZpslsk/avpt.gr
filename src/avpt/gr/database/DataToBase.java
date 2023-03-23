package avpt.gr.database;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import avpt.gr.maps.Stations;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DataToBase {

    private Connection conn;
    private ArrTrains trains;
    private final String fileName;
    private Stations stations;
    private final String user;

    public DataToBase(
            String fileName,
            String host,
            String nameDb,
            String user,
            String pass,
            int codeRoad,
            int codeDepo,
            long guidFile) throws SQLException, ClassNotFoundException {

        ArrBlock32 arrBlock32;
        this.fileName = fileName;
        this.user = user;
        try {
            arrBlock32 = new ArrBlock32(fileName, true);
        } catch (IOException e) {
            UtilsArmG.outWriteAndExit(301, e.getMessage(), fileName, false);
            return;
        }
        ChartDataset chartDataset = new ChartDataset(arrBlock32, true, true);
        trains = chartDataset.getArrTrains();
        stations = new Stations(arrBlock32);
        stations.addStationsToTrains(trains);
        if (connect(host, nameDb, user, pass))
            insertToBase(codeRoad, codeDepo, guidFile);
        else {
            UtilsArmG.outWriteAndExit(302, "нет соединения с сервером", fileName, false);
        }
    }

    private boolean connect(String host, String nameDB, String user, String pass) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = String.format("jdbc:postgresql://%s/%s", host, nameDB);
        try {
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException e) {
            throw new SQLException(e.getMessage(), "302");
        }
        return conn != null;
    }

    private void insertToBase(int codeRoad, int codeDepo, long guidFile) throws SQLException {
        if (conn == null) throw new SQLException("Нет соединения с сервером!") ;
        try {
            conn.setAutoCommit(false);
            // дорога
            int idRoad = (int) getIdRoad(codeRoad);
            if (idRoad == -1) throw new SQLException("Код дороги не найден в классификаторе", "304");
            // депо
            int idDepot = (int) insertDepot(idRoad, codeDepo);
            if (idDepot == -1) throw new SQLException("Вставка депо неудачна", "305");
            // file
            long idImage = insertFileImage(idRoad, idDepot, guidFile);
            if (idImage == -1) throw new SQLException("Ошибка вставки в таблицу \"FILELIST_CARTRIDGES\"", "306");
            // user
            long idUser = getIdUser(user);
            if (idUser == -1) throw new SQLException("Пользователь не найден", "307");

            boolean isCommit = false;
            if (trains.size() == 1 && trains.get(0).getDistance() == 0)
                throw new SQLException("Поездки не определены", "317");

            String errMess = "Файл некорректный";
            String errCode = "308";
            for (int i = 0; i < trains.size(); i++) {
                Train train = trains.get(i);
                int idTypeMove = train.getTypeMove();
                if (train.getDistance() < 1000) {
                    errCode = "309";
                    errMess = "Недостаточно информации для записи";
                    continue;
                }
                int typeSeries = convertTypeSeries(train.getTypeLoc(), train.getLocTypeAsoup());
                int idTypeSeries = (int) getIdTypeSeries(typeSeries, idTypeMove);
                if (idTypeSeries == -1) {
                    errCode = "310";
                    errMess = "Неизвестный тип серии локомотива";
                    continue;
                }
                if (train.getSeconds() < 120) {
                    errCode = "309";
                    errMess = "Недостаточно информации для записи";
                    continue;
                }

                long idDriver = insertDriver(train, codeRoad, codeDepo);
                if (idDriver == -1) {
                    errCode = "311";
                    errMess = "Ошибка добавления кода машиниста";
                    continue;
                }

                long idRoute = insertRoute(train, idRoad, idDepot);

                long idUnit = insertUnit(train, codeRoad, codeDepo, idRoute, idUser, idDriver, idTypeMove, idImage, guidFile);
                if (idUnit == -1) {
                    errCode = "312";
                    errMess = "Файл является дубликатом уже имеющихся данных";
//                    errMess = "Ошибка вставки в таблицу \"Insert_SPECIFICATION_ROUTE_UNITS\" или запись дублируется";
                    continue;
                }

                long idUnitInfo = insertUnitInfo(train, idUnit);
                if (idUnitInfo == -1) {
                    errCode = "313";
                    errMess = "Ошибка вставки в таблицу \"Insert_SPECIFICATION_ROUTE_UNITS_GEN_INFO\"";
                    continue;
                }

                long idDiagnMoving = insertDiagnMoving(train, idUnit);
                if (idDiagnMoving == -1) {
                    errCode = "314";
                    errMess = "Ошибка вставки в таблицу \"Insert_SPECIFICATION_DIAGNOSTICS_MOVING\"";
                    continue;
                }

                long idSeriesTrain = insertSeriesTrain(train, idRoad, idTypeSeries, idDepot);
                if (idSeriesTrain == -1) {
                    errCode = "315";
                    errMess = "Ошибка вставки в таблицу \"Insert_DIRECTORY_SERIES_TRAINS\"";
                    continue;
                }

                long idSection;
                for (int iSect = 0; iSect < 4; iSect++) {
                    idSection = insetrtSection(train, iSect, idUnit, idUser, idSeriesTrain);
                    if (idSection == -1) {
                        errCode = "316";
                        errMess = "Ошибка вставки в таблицу \"Insert_SPECIFICATION_SECTIONS\"";
                    }
                }

                insetrtStationsSched(train, idUnit, idRoute);
                isCommit = true;
            }
            if (isCommit)
                conn.commit();
            else {
                conn.rollback();
                throw new SQLException(errMess, errCode);
            }
        }
        finally {
            conn.close();
        }
    }


    /**
     * @param codeRoad - код дороги
     * @return - idRoad - идентификатор дороги
     * @throws SQLException -
     */
    private long getIdRoad(int codeRoad) throws SQLException {
        final int nCodeRoad = 1;
        final String sql =  "SELECT \"RAILROAD_ID\" " +
                "FROM \"DIRECTORY_RAILROADS\" " +
                "WHERE \"CODE_RAILROAD\" = ?; ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(nCodeRoad, codeRoad);
        }
        catch (Exception e) {
            throwSQLException("getIdRoad", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param idRoad -
     * @param codeDepo  -
     * @return idDepo - идентификатор депо
     * @throws SQLException -
     */
    private long insertDepot(int idRoad, int codeDepo) throws SQLException {
        final int nIdRoad = 1;
        final int nDepoName = 2;
        final int nDescript = 3;
        final int nCodeDepo = 4;
        final String depoName = "ТЧ-" + codeDepo;
        final String sql = "SELECT \"S_WU_DEPO\"(?, -1, ?, ?, ?); ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(nIdRoad, idRoad);
            prepStat.setString(nDepoName, depoName);
            prepStat.setString(nDescript, "");
            prepStat.setInt(nCodeDepo, codeDepo);
        }
        catch (Exception e) {
            throwSQLException("insertDepo", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param idRoad -
     * @param idDepo -
     * @param guidFile - крайний параметр командной строки (id файла поездок)
     * @return - FILE_CARTRIDGE_ID
     * @throws SQLException -
     */
    private long insertFileImage(int idRoad, int idDepo, long guidFile) throws SQLException {
        final int nIdRoad = 1;
        final int nIdDepo = 2;
        final int nFlag  = 3;
        final int nNumCar = 4;
        final int nDateRead  = 5;
        final int nFileName  = 6;
        final int nEnMan = 7;
        final String sql  = "SELECT \"Insert_FILELIST_CARTRIDGES\"(?, ?, ?, ?, cast(? as timestamp with time zone), ?, ?); ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(nIdRoad, idRoad);
            prepStat.setInt(nIdDepo, idDepo);
            prepStat.setInt(nFlag, -1);
            prepStat.setInt(nNumCar, -1);

            String timeStampStr = UtilsArmG.getTimestampStr(LocalDateTime.now());
            prepStat.setString(nDateRead, timeStampStr);

            if (guidFile == -1)   // если крайний параметр командной строки не введен
                prepStat.setString(nFileName, fileName);
            else
                prepStat.setString(nFileName, Long.toString(guidFile));
            prepStat.setInt(nEnMan, -1);
        }
        catch (Exception e) {
            throwSQLException("insertFileImage", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param user - имя пользователя
     * @return - USER_ID
     * @throws SQLException
     */
    private long getIdUser(String user) throws SQLException {
        final int nUserName = 1;
        final String sql =  "SELECT \"USER_ID\" " +
                "FROM \"DIRECTORY_USERS\" " +
                "WHERE \"USER_LOGIN\" = ?; ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setString(nUserName, user);
        }
        catch (Exception e) {
            throwSQLException("getIdUser", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param typeLoc -
     * @param typeMove  -
     * @return TYPE_SERIES_TRAIN_ID
     * @throws SQLException -
     */
    private long getIdTypeSeries(int typeLoc, int typeMove) throws  SQLException {
        final int TYPE_LOC = 1;
        final int TYPE_MOVE  = 2;
        final String sql =
                "SELECT \"TYPE_SERIES_TRAIN_ID\" " +
                        "FROM \"DIRECTORY_TYPE_SERIES_TRAINS\" " +
                        "WHERE \"type_code\" = ? AND \"ID_TYPE_MOVING\" = ? ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(TYPE_LOC, typeLoc);
            prepStat.setInt(TYPE_MOVE, typeMove);
        }
        catch (Exception e) {
            throwSQLException("getIdTypeSeries", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param codeRoad -
     * @param codeDepo -
     * @return - DRIVER_ID
     * @throws SQLException
     */
    private long insertDriver(Train train, int codeRoad, int codeDepo) throws SQLException {
        final int nTabNum = 1;
        final int nCodeRoad  = 2;
        final int nCodeDepo = 3;
        final String sql = "SELECT \"sp_GetDriverId\" ( ?, ?, ?)";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(nTabNum, (int)train.getNumTab());
        prepStat.setInt(nCodeRoad, codeRoad);
        prepStat.setInt(nCodeDepo, codeDepo);
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param idRoad -
     * @param idDepo -
     * @return RAILROAD_DIRECTION_ID
     */
    private long insertRoute(Train train, int idRoad, int idDepo) throws SQLException {
        final int nIdRoad = 1;
        final int nIdDepo = 2;
        final int nFlagActive = 3;
        final int nIsRelateDir = 4;
        final int nDirFlagUnit = 5;
        final int nDateLastShed = 6;
        final int nCntMap = 7;
        final int nDateLastMap = 8;
        final int nNameRoad = 9;

        final String sql = "SELECT \"Insert_DIRECTORY_RAILROAD_DIRECTIONS\" (" +
                "?, ?, ?, ?, ?, cast(? as timestamp with time zone), ?, cast(? as timestamp with time zone), ?); ";

        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(nIdRoad, idRoad);
            prepStat.setInt(nIdDepo, idDepo);
            prepStat.setBoolean(nFlagActive, false);
            prepStat.setInt(nIsRelateDir, -1);
            prepStat.setInt(nDirFlagUnit, -1);
            String timeStampStr = UtilsArmG.getTimestampStr(
                    LocalDateTime.of(0, 1, 1, 1, 0));
            prepStat.setString(nDateLastShed, timeStampStr);
            prepStat.setInt(nCntMap, -1);
            prepStat.setString(nDateLastMap, timeStampStr);
            String routeName = train.getRoutName();
            if (routeName == null) routeName = "";
            prepStat.setString(nNameRoad, routeName);
        }
        catch (Exception e) {
            throwSQLException("insertRoute", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param codeRoad -
     * @param codeDepot -
     * @param idUser -
     * @param idDrv -
     * @param typeMove -
     * @param idFile -
     * @return UNIT_ROUTE_ID
     * @throws SQLException
     */
    private long insertUnit(Train train, int codeRoad, int codeDepot, long idRoute, long idUser, long idDrv,
                            int typeMove, long idFile, long guidFile) throws SQLException {

        final int num_road = 1;                                     // integer
        final int num_tch = 2;                                      // integer
        final int RAILROAD_DIRECTION_ID = 3;                        // integer
        final int image_id = 4;                                     // bigint
        final int USER_ID = 5;                                      // integer
        final int PERSON_NUMBER_DRIVER = 6;                         // integer
        final int NUMBER_ROUTE = 7;                                 // integer
        final int dateTr = 8;                                       // timestamp with time zone
        final int drv_id = 9;                                       // integer
        final int COUNT_ITEMS_SPECIFICATION_TRAIN_SCHEDULES = 10;   // integer
        final int FULL_NUMBER_CARTRIDGE = 11;                       // bigint
        final int DATE_READ_FILE_CARTRIDGE = 12;                    // timestamp with time zone
        final int DATE_SCHEDULE_MAP = 13;                           // timestamp with time zone,
        final int DATE_TIME_RECORD_DATA_ROUTE = 14;                 // timestamp with time zone
        final int verpo = 15;                                       // character varying
        final int INFO_RPDA_NUMBER_BLOCK_BR = 16;                   // integer
        final int isavprt_status = 17;                              // integer
        final int isavprt_addr = 18;                                // integer
        final int TYPE_MOVEMENT = 19;                               // integer
        final int ID_TYPE_MOVING = 20;                              // integer
        final int TYPE_CODE = 21;                                   // integer
        final int PODTYPE_CODE = 22;                                // integer
        final int CODE_CLASSIFIER = 23;                             // bigint
        final int INFO_RPDA_TYPE_BLOCK_BR = 24;                     // character varying DEFAULT ' '::character varying)

        final String sql =
                "SELECT \"Insert_SPECIFICATION_ROUTE_UNITS\" " +
                        "(?, " +                                    //1
                        "?, " +                                     //2
                        "?, " +                                     //3
                        "?, " +                                     //4
                        "?, " +                                     //5
                        "?, " +                                     //6
                        "?, " +                                     //7
                        "cast(? as timestamp with time zone), " +   //8
                        "?, " +                                     //9
                        "?, " +                                     //10
                        "?, " +                                     //11
                        "cast(? as timestamp with time zone), " +   //12
                        "cast(? as timestamp with time zone), " +   //13
                        "cast(? as timestamp with time zone), " +   //14
                        "?, " +                                     //15
                        "?, " +                                     //16
                        "?, " +                                     //17
                        "?, " +                                     //18
                        "?, " +                                     //19
                        "?, " +                                     //30
                        "?, " +                                     //21
                        "?, " +                                     //22
                        "?, " +                                     //23
                        "?); ";                                     //24
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(num_road, codeRoad);
            prepStat.setInt(num_tch, codeDepot);
            prepStat.setInt(RAILROAD_DIRECTION_ID, (int)idRoute); // Индекс направления
            prepStat.setLong(image_id, idFile);
            prepStat.setInt(USER_ID, (int)idUser);
            prepStat.setInt(PERSON_NUMBER_DRIVER, (int)train.getNumTab()); // табельный номер
            prepStat.setInt(NUMBER_ROUTE, train.getNumTrain()); // Номер поезда
            String timeStampStr = UtilsArmG.getTimestampStr(train.getDateTimeStart());
            prepStat.setString(dateTr, timeStampStr );  // дата поезда
            prepStat.setInt(drv_id, (int)idDrv);
            prepStat.setInt(COUNT_ITEMS_SPECIFICATION_TRAIN_SCHEDULES, -1); // Количество пунктов в таблице с данными о выполнении расписания (количество станций, пройденных по расписанию)
            if (guidFile == -1)
                prepStat.setLong(FULL_NUMBER_CARTRIDGE, 0);
            else
                prepStat.setLong(FULL_NUMBER_CARTRIDGE, guidFile);
            timeStampStr = UtilsArmG.getTimestampStr(LocalDateTime.now());
            prepStat.setString(DATE_READ_FILE_CARTRIDGE, timeStampStr); // Дата чтения для поиска исходного файла (настоящее время)
            timeStampStr = UtilsArmG.getTimestampStr(LocalDateTime.of(train.getDateMap(), LocalTime.of(0, 0, 0)));
            prepStat.setString(DATE_SCHEDULE_MAP, timeStampStr);    // Дата используемых расписания и карты
            prepStat.setString(DATE_TIME_RECORD_DATA_ROUTE, timeStampStr);  // Дата и время записи данных о маршруте в базу данных РПДА
            prepStat.setString(verpo, train.getVerBr());
            prepStat.setInt(INFO_RPDA_NUMBER_BLOCK_BR, -1);      // № блока управления системы регистрации РПДА

            prepStat.setInt(isavprt_status, train.getIsMainOrSlave()); // 0 - РТ ведомый, 1 - РТ ведущий, 40 - вирт сцепка ведущий, > 41... - вирт сцепка ведомый
            prepStat.setInt(isavprt_addr, train.getNetAddress());
            prepStat.setInt(TYPE_MOVEMENT, train.getNumSlave());  // адрес парного локомотива
            prepStat.setInt(ID_TYPE_MOVING, typeMove);              // Идентификатор типа движения
            // в таблице нет этих полей (для проверки)
            int typeSeries = convertTypeSeries(train.getTypeLoc(), train.getLocTypeAsoup());
            prepStat.setInt(TYPE_CODE, typeSeries);
//            System.out.println(avpt.gr.train.getTypeLoc());
            prepStat.setInt(PODTYPE_CODE, 1);
            prepStat.setLong(CODE_CLASSIFIER, train.getNumLoc());
//            System.out.println(avpt.gr.train.getNumLoc());
            prepStat.setString(INFO_RPDA_TYPE_BLOCK_BR, " ");
        }
        catch (Exception e) {
            throwSQLException("insertUnit", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param idUnit -
     * @return UNIT_ROUTE_GEN_INFO_ID
     * @throws SQLException -
     */
    private long insertUnitInfo(Train train, long idUnit) throws SQLException {
        final int UNIT_ROUTE_ID = 1;                            // bigint
        final int TYPE_SYSTEM_USAVP = 2;                        // smallint
        final int NET_WEIGHT_TRAIN_Kg = 3;                      // integer
        final int SPECIFIC_CONSUMPTION_TRAIN_Wh_10000tnKm = 4;  // integer
        final int WORK_TRAIN_tn10000Km = 5;                     // integer
        final int ID_ASUT1 = 6;                                 // bigint
        final int ID_ASUT2 = 7;                                 // bigint
        final int POINTER_SYSTEM_CONFIG_RPDA = 8;               // bigint
        final int DATE_TIME_END_SHIFT = 9;                      // timestamp with time zone
        final int E_Invalidate = 10;                            // bit
        final int x_Common = 11;                                // real
        final int x_SavpeAuto = 12;                             // real
        final int x_SavpePrompt = 13;                           // real
        final int DrawMeter = 14;                               // real
        final int RecupMeter = 15;                              // real
        final int Norma = 16;                                   // real
        final int DifNorma = 17;                                // real
        final int NormaVt = 18;                                 // real
        final int DifNormaVt = 19;                              // real
        final int av_speed = 20;                                // real
        final int av_speed_move = 21;                           // real
        final int countTLim = 22;                               // integer
        final int train_time = 23;                              // integer
        final int nKar = 24;                                    // integer
        final int SUM_ENERGY = 25;                              // integer
        final int SUM_ENERGY_REACTIVE = 26;                     // integer
        final int TEMP_OUTSIDE = 27;                            // smallint DEFAULT '-2000'::integer

        final String sql =
                "SELECT \"Insert_SPECIFICATION_ROUTE_UNITS_GEN_INFO\" " +
                        "(cast(? as bigint), " +    //1
                        "cast(? as smallint), " +   //2
                        "?, " +                     //3
                        "?, " +                     //4
                        "?, " +                     //5
                        "cast(? as bigint), " +     //6
                        "cast(? as bigint), " +     //7
                        "cast(? as bigint), " +     //8
                        "cast(? as timestamp), " +  //9
                        "cast(? as bit(16)), " +        //10
                        "cast(? as real), " +       //11
                        "cast(? as real), " +       //12
                        "cast(? as real), " +       //13
                        "cast(? as real), " +       //14
                        "cast(? as real), " +       //15
                        "cast(? as real), " +       //16
                        "cast(? as real), " +       //17
                        "cast(? as real), " +       //18
                        "cast(? as real), " +       //19
                        "cast(? as real), " +       //20
                        "cast(? as real), " +       //21
                        "?, " +                     //22
                        "?, " +                     //23
                        "?, " +                     //24
                        "?, " +                     //25
                        "?, " +                     //26
                        "cast(? as smallint)); ";   //27
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setLong(UNIT_ROUTE_ID, idUnit);
            prepStat.setLong(TYPE_SYSTEM_USAVP, -1);        // Тип системы УСАВП
            prepStat.setInt(NET_WEIGHT_TRAIN_Kg, train.getWeightTrain() * 1000);
            prepStat.setInt(SPECIFIC_CONSUMPTION_TRAIN_Wh_10000tnKm, 0); // Удельный расход поезда в Вт*Час на 10000 ТнКм
            prepStat.setInt(WORK_TRAIN_tn10000Km, (int)(train.getDistance() * train.getWeightTrain() / 10000)); // Работа выполненная Поездом, т*10000 км
            prepStat.setLong(ID_ASUT1 , -1);
            prepStat.setLong(ID_ASUT2 , -1);
            prepStat.setLong(POINTER_SYSTEM_CONFIG_RPDA , -1); // Указатель комплектации системы РПДА (РПДА-T, РПДА-П, РПДА-Г), в зависимости от типа тягового состава (берется с картриджа)
            String timeStampStr = UtilsArmG.getTimestampStr(train.getDateTimeEnd());
            prepStat.setString(DATE_TIME_END_SHIFT, timeStampStr);  // Дата и время окончания смены

            int testThrust = train.isTestThrust() ? 0 : 0x0001;                         // тест тяги не пройден
            int testBrake = train.isTestBrake() ? 0 :   0x0002;                         // тест торможения не пройден
            int mainLinkVSC = train.getMainLinkVSC().getPercent() > 50 ? 0 : 0x0004;    // связь по основному каналу между локомотивами отсутствует
            int slaveLinkVSC = train.getSlaveLinkVSC().getPercent() > 50 ? 0 : 0x0008;  // связь по дополнительному каналу между локомотивами отсутствует
            int autoDrive = train.getAutoDrive().getPercent() > 50 ? 0 : 0x0010;        // автоведение отсутствует
            int modeManeuver = train.getModeManeuver().getPercent() > 50 ? 0x0020 : 0;  // сигнал маневровые работы не отключен
            int chainOff = train.getChainOff().getPercent() > 50 ? 0x0040 : 0;          // отключены выходные цепи
            int banPT = train.getBanPT().getPercent() > 50 ? 0x0080 : 0;                // запрет пневматического торможения

            prepStat.setLong(E_Invalidate, train.getIavprtIsOk()
                | testThrust | testBrake | mainLinkVSC | slaveLinkVSC | autoDrive |modeManeuver | chainOff | banPT);
            prepStat.setDouble(x_Common, train.getDistance());
            prepStat.setDouble(x_SavpeAuto, train.getDistance_auto());
            prepStat.setDouble(x_SavpePrompt, train.getDistance_prompt());
            prepStat.setDouble(DrawMeter, -1);
            prepStat.setDouble(RecupMeter, -1);
            prepStat.setDouble(Norma, -1);
            prepStat.setDouble(DifNorma, -1);
            prepStat.setDouble(NormaVt, -1);
            prepStat.setDouble(DifNormaVt, -1);
            prepStat.setDouble(av_speed, train.getAvgSpeed() * 1000);
            prepStat.setDouble(av_speed_move, train.getAvgSpeedMove() * 1000);
            prepStat.setInt(countTLim, 0);
            prepStat.setInt(train_time, (int)train.getSeconds());
            prepStat.setInt(nKar, -1);
            prepStat.setInt(SUM_ENERGY, -1);
            prepStat.setInt(SUM_ENERGY_REACTIVE, -1);
            prepStat.setInt(TEMP_OUTSIDE, -1);
        }
        catch (Exception e) {
            throwSQLException("insertUnitInfo", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param idUnit -
     * @return DIAGNOSTICS_MOVING_ID
     * @throws SQLException -
     */
    private long insertDiagnMoving(Train train, long idUnit) throws SQLException {
        final int UNIT_ROUTE_ID = 1;                            // bigint,
        final int COUNT_STARTS_MOVING_LOCOMOTIVE = 2;           // integer
        final int COUNT_SWITCH_KM = 3;                          // integer
        final int COUNT_SWITCH_FORWARD = 4;                     // integer
        final int TIME_PARKING_s = 5;                           // integer
        final int TIME_MOVING_s = 6;                            // integer
        final int TIME_BRAKING_s = 7;                           // integer
        final int COUNT_EDT_ON = 8;                             // integer
        final int NUMBER_ROUTE = 9;                             // smallint
        final int TIME_SURGE_s = 10;                            // smallint
        final int TIME_REGISTRATION_TRIP_s = 11;                // integer
        final int TIME_DRIVING_ROUTE_s = 12;                    // integer
        final int TIME_DEVIATION_ARRIVAL_LAST_STATION_s = 13;   // integer
        final int TIME_WORK_USAVP_s = 14;                       // integer
        final int TIME_WORK_USAVP_REGIME_AUTODRIVING_s = 15;    // integer
        final int TIME_WORK_USAVP_REGIME_HELP_s = 16;           // integer
        final int TIME_WORK_HEATING_SALONS_s = 17;              // integer
        final int RUNNING_DPS_m = 18;                           // integer
        final int RUNNING_MAP_m = 19;                           // integer
        final int RUNNING_MAP_REGIME_AUTODRIVING_m = 20;        // integer
        final int AVG_SPEED_100Km_h = 21;                       // smallint
        final int AVG_TECHNICAL_SPEED_100Km_h = 22;             // smallint
        final int COUNTER_EXCEEDANCES_TEMPORARY_SPEED_LIMITS = 23;// smallint
        final int COUNTER_EXCEEDANCES_CONSTANT_SPEED_LIMITS = 24;// smallint
        final int COUNTER_UNFORESEEN_TRAIN_STOP = 25;            // smallint,
        final int COUNTER_EXCEEDANCES_SPEED_ALSN_YELLOW = 26;    // smallint
        final int COUNTER_EXCEEDANCES_SPEED_ALSN_RED_YELLOW = 27;// smallint
        final int COUNTER_ABSENCES_STOP_ALSN_RED_YELLOW = 28;    // smallint
        final int ESR_CODE_DEPARTURE_STATION = 29;              // integer
        final int ESR_CODE_ARRIVAL_STATION = 30;                // integer
        final int LENGTH_TRAIN_m = 31;                          // integer
        final int COUNT_WAGONS = 32;                            // integer
        final int DIAMETER_BANDAGES_mm = 33;                    // integer
        final int SUM_LENGTH_TIME_LIMIT_m = 34;                 // integer
        final int COUNT_AXLES_TRAIN = 35;                       // integer
        final int ROUTE_NAME = 36;                              // character varying
        final int RUNNING_ALSN_GREEN_m = 37;                    // integer
        final int RUNNING_ALSN_YELLOW_m = 38;                   // integer
        final int RUNNING_ALSN_RED_YELLOW_m = 39;               // integer
        final int RUNNING_ALSN_RED_m = 40;                      // integer
        final int RUNNING_ALSN_WHITE_m = 41;                    // integer
        final int RUNNING_ALSN_GRAY_m = 42;                     // integer
        final int RUNNING_SURGE_ALSN_m = 43;                    // integer
        final int TIME_RUNNING_ALSN_GREEN_s = 44;               // integer
        final int TIME_RUNNING_ALSN_YELLOW_s = 45;              // integer
        final int TIME_RUNNING_ALSN_RED_YELLOW_s = 46;          // integer
        final int TIME_RUNNING_ALSN_RED_s = 47;                 // integer
        final int TIME_RUNNING_ALSN_WHITE_s = 48;               // integer
        final int TIME_RUNNING_ALSN_GRAY_s = 49;                // integer
        final int TIME_RUNNING_SURGE_ALSN_s = 50;               // integer
        final int COUNT_PASSED_ALSN_GREEN = 51;                 // integer
        final int COUNT_PASSED_ALSN_YELLOW = 52;                // integer
        final int COUNT_PASSED_ALSN_RED_YELLOW = 53;            // integer
        final int COUNT_PASSED_ALSN_RED = 54;                   // integer
        final int COUNT_PASSED_ALSN_WHITE = 55;                 // integer
        final int COUNT_PASSED_ALSN_GRAY = 56;                  // integer
        final int SPEED_MAP_100Km_h = 57;                       // integer
        final int RUNNING_LINEAR_m_MM = 58;                     // integer
        final int MAX_SPEED_100Km_h = 59;                      // integer
        final int SUM_TIME_WORK_EDT_s = 60;                     // integer
        final int MAX_TIME_WORK_EDT_s = 61;                     // integer
        final int MAX_SPEED_EDT_ON_100Km_h = 62;                // integer
        final int MIN_SPEED_EDT_OFF_100Km_h = 63;               // integer
        final int CountEmpty = 64;                              // integer
        final int speedNagon = 65;                                // integer
        final int Sh_n = 66;                                    // integer
        final int Sh_k = 67;                                    // integer
        final int Dg_n = 68;                                    // integer
        final int Dg_k = 69;                                    // integer
        final int t_ek = 70;                                    // integer
        final int t_pr = 71;                                    // integer

        final String sql =
                "SELECT \"Insert_SPECIFICATION_DIAGNOSTICS_MOVING\" " +
                        "(cast(? as bigint), " +
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //  0..3
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 4..7
                        "cast(? as smallint), " +   //
                        "cast(? as smallint), " +   // 8..9
                        " ?, " +                    //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 10..16
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 17..19
                        "cast(? as smallint), " +   //
                        "cast(? as smallint), " +   // 20..21
                        "cast(? as smallint), " +   //
                        "cast(? as smallint), " +   // 22..23
                        "cast(? as smallint), " +   //
                        "cast(? as smallint), " +   // 24..25
                        "cast(? as smallint), " +   //
                        "cast(? as smallint), " +   // 26..27
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 28..35
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 36..42
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 43..49
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 50..55
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     // 56..62
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?, " +                     //
                        "?); ";                     // 63..70

        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setLong(UNIT_ROUTE_ID, idUnit);
            prepStat.setInt(COUNT_STARTS_MOVING_LOCOMOTIVE, -1);        //Количество троганий локомотива
            prepStat.setInt(COUNT_SWITCH_KM, -1);                       // Количество переключений КМ (контроллер машиниста)
            prepStat.setInt(COUNT_SWITCH_FORWARD, -1);                  // Количество переключений ВПР (вперед)
            prepStat.setInt(TIME_PARKING_s, (int)train.getSeconds_stop()); // Время в стоянке, сек
            long time_stop = (train.getSeconds() - train.getSeconds_stop());
            if (time_stop > 0)
                prepStat.setInt(TIME_MOVING_s, (int)time_stop);   // Время в движении, сек
            else
                prepStat.setInt(TIME_MOVING_s, 0);
            prepStat.setInt(TIME_BRAKING_s, -1);                           // Время в торможении,  сек
            prepStat.setInt(COUNT_EDT_ON, -1);                              // Количество включений ЭДТ (Электродинамическое торможение)
            prepStat.setShort(NUMBER_ROUTE, (short)-1);                     // № маршрута
            prepStat.setShort(TIME_SURGE_s, (short)-1);                     // Время нагона, сек
            prepStat.setInt(TIME_REGISTRATION_TRIP_s, -1);              // Время регистрации поездки. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(TIME_DRIVING_ROUTE_s, -1);                  // Время движения по маршруту. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(TIME_DEVIATION_ARRIVAL_LAST_STATION_s, -1); // Отклонение от прибытия на конечный пункт, сек
            prepStat.setInt(TIME_WORK_USAVP_s, -1);                     // Время работы УСАВП. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(TIME_WORK_USAVP_REGIME_AUTODRIVING_s, -1);  // Время работы УСАВП в режиме автоведения. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(TIME_WORK_USAVP_REGIME_HELP_s, -1);         // Время работы УСАВП в режиме подсказки. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(TIME_WORK_HEATING_SALONS_s, -1);            // Время работы отопления в салонах. Время отсчитывается в секундах, начиная с 00:00:00. Если значение < 0, данные не определены
            prepStat.setInt(RUNNING_DPS_m, -1);                         // Пробег по данным ДПС (Датчик угловых перемещений), в метрах
            prepStat.setInt(RUNNING_MAP_m, -1);                         // Пробег по карте в метрах
            prepStat.setInt(RUNNING_MAP_REGIME_AUTODRIVING_m, -1);      // Пробег по карте в режиме автоведения в метрах
            prepStat.setInt(AVG_SPEED_100Km_h, (int)Math.round(train.getAvgSpeed() * 100));// Средняя скорость движения, км/час * 100
            prepStat.setInt(AVG_TECHNICAL_SPEED_100Km_h, (int)Math.round(train.getAvgSpeedMove() * 100));// Средняя техническая скорость, км/час * 100
            prepStat.setInt(COUNTER_EXCEEDANCES_TEMPORARY_SPEED_LIMITS, -1);    // Счетчик Превышений временных ограничений скорости
            prepStat.setInt(COUNTER_EXCEEDANCES_CONSTANT_SPEED_LIMITS, -1);     // Счетчик Превышений постоянных ограничений скорости
            prepStat.setInt(COUNTER_UNFORESEEN_TRAIN_STOP, -1);                 //
            prepStat.setInt(COUNTER_EXCEEDANCES_SPEED_ALSN_YELLOW, -1);
            prepStat.setInt(COUNTER_EXCEEDANCES_SPEED_ALSN_RED_YELLOW, -1);
            prepStat.setInt(COUNTER_ABSENCES_STOP_ALSN_RED_YELLOW, -1);
            prepStat.setInt(ESR_CODE_DEPARTURE_STATION, -1);
            prepStat.setInt(ESR_CODE_ARRIVAL_STATION, -1);
            prepStat.setInt(LENGTH_TRAIN_m, -1);                        // Длина состава, м
            prepStat.setInt(COUNT_WAGONS, train.getCntWags());                          // Количество прицепных вагонов (пассажир, грузовик или общее количество вагонов в составе электрички)
            prepStat.setInt(DIAMETER_BANDAGES_mm, train.getdBandage());                  // Диаметр бандажа, мм
            prepStat.setInt(SUM_LENGTH_TIME_LIMIT_m, -1);
            prepStat.setInt(COUNT_AXLES_TRAIN, -1);        // Количество осей в поезде
            prepStat.setString(ROUTE_NAME, "");                         // Название маршрута (станция отправления-станция назначения)
            prepStat.setInt(RUNNING_ALSN_GREEN_m, train.getCnt_green().getDistance());                  // Пробег под сфетофор, зеленый, в м
            prepStat.setInt(RUNNING_ALSN_YELLOW_m, train.getCnt_yellow().getDistance());                // Пробег под сфетофор, желтый, в м
            prepStat.setInt(RUNNING_ALSN_RED_YELLOW_m, train.getCnt_red_yellow().getDistance());        // Пробег под сфетофор, красно-желтый, в м
            prepStat.setInt(RUNNING_ALSN_RED_m, train.getCnt_red().getDistance());                      // Пробег под сфетофор, красный, в м
            prepStat.setInt(RUNNING_ALSN_WHITE_m, train.getCnt_white().getDistance());                  // Пробег под сфетофор, белый, в м
            prepStat.setInt(RUNNING_ALSN_GRAY_m, -1);                   // Пробег под сфетофор, серый, в м
            prepStat.setInt(RUNNING_SURGE_ALSN_m, -1);                  // Пробег при нагоне под светофорами, в м
            prepStat.setInt(TIME_RUNNING_ALSN_GREEN_s, train.getCnt_green().getSecond());             // Время прохождения под светофор, зеленый , в сек.
            prepStat.setInt(TIME_RUNNING_ALSN_YELLOW_s, train.getCnt_yellow().getSecond());
            prepStat.setInt(TIME_RUNNING_ALSN_RED_YELLOW_s, train.getCnt_red_yellow().getSecond());
            prepStat.setInt(TIME_RUNNING_ALSN_RED_s, train.getCnt_red().getSecond());
            prepStat.setInt(TIME_RUNNING_ALSN_WHITE_s, train.getCnt_white().getSecond());
            prepStat.setInt(TIME_RUNNING_ALSN_GRAY_s, -1);
            prepStat.setInt(TIME_RUNNING_SURGE_ALSN_s, -1);             // Время при нагоне под светофорами, в сек.
            prepStat.setInt(COUNT_PASSED_ALSN_GREEN, train.getCnt_green().getCnt());               // Количество пройденных за маршрут зеленых светофоров
            prepStat.setInt(COUNT_PASSED_ALSN_YELLOW, train.getCnt_yellow().getCnt());
            prepStat.setInt(COUNT_PASSED_ALSN_RED_YELLOW, train.getCnt_red_yellow().getCnt());
            prepStat.setInt(COUNT_PASSED_ALSN_RED, train.getCnt_red().getCnt());
            prepStat.setInt(COUNT_PASSED_ALSN_WHITE, train.getCnt_white().getCnt());
            prepStat.setInt(COUNT_PASSED_ALSN_GRAY, -1);
            prepStat.setInt(SPEED_MAP_100Km_h, -1);
            prepStat.setInt(RUNNING_LINEAR_m_MM, -1);                   // Пробег линейный, метров по ММ (мастер модуль)
            prepStat.setInt(MAX_SPEED_100Km_h, -1);                     // Максимальная скорость, км/час * 100
            prepStat.setInt(SUM_TIME_WORK_EDT_s, -1);                   // Суммарное время работы ЭДТ (Электродинамическое торможение), сек
            prepStat.setInt(MAX_TIME_WORK_EDT_s, -1);                   // Максимальная продолжительность работы ЭДТ (Электродинамическое торможение, относится к диагностике), сек
            prepStat.setInt(MAX_SPEED_EDT_ON_100Km_h, -1);              // Максимальная скорость при включении ЭДТ (Электродинамическое торможение, относится к диагностике) , км/час * 100
            prepStat.setInt(MIN_SPEED_EDT_OFF_100Km_h, -1);             // Минимальная скорость при выключении ЭДТ (Электродинамическое торможение, относится к диагностике) , км/час * 100
            prepStat.setInt(CountEmpty, -1);                            // Кол-во пустых вагонов
            prepStat.setInt(speedNagon, -1);
            prepStat.setInt(Sh_n, -1);                     // Широта начало
            prepStat.setInt(Sh_k, -1);                       // Широта конец
            prepStat.setInt(Dg_n, -1);                     // Долгота начало
            prepStat.setInt(Dg_k, -1);                       // Долгота конец
            prepStat.setInt(t_ek, -1);                                      //Время экипировки, сек 'public int getRefuel_second' должно быть по секциям
            prepStat.setInt(t_pr, -1);                                      // Время пропуска регистрации, сек
        }
        catch (Exception e) {
            throwSQLException("insertDiagnMoving", e.getMessage());
        }
        return execQuery(prepStat);
    }

    private long insertSeriesTrain(Train train, int idRoad, int idTypeLoc, long idDepo) throws SQLException {
        final int RAILROAD_ID = 1;                                          // integer
        final int DEPOT_ID = 2;                                             // integer
        final int MIN_NUMBER_SECTION = 3;                                   // integer
        final int MAX_NUMBER_SECTION = 4;                                   // integer
        final int MASS_LOAD_SECTION_Kg = 5;                                 // integer
        final int LENGTH_SECTION_m = 6;                                     // integer
        final int COEFFICIENT_NORM_CONSUMPTION_SECTION_ON_SERIES10000 = 7;  // real
        final int PARAMETER1_SECTION = 8;                                   // integer
        final int PARAMETER2_SECTION = 9;                                   // integer
        final int PARAMETER3_SECTION = 10;                                  // integer
        final int COUNT_SECTIONS = 11;                                      // integer
        final int NAME_SERIES_TRAIN = 12;                                   // character varying
        final int NAME_TYPE_TRAIN = 13;                                     // character varying
        final int CODE_CLASSIFIER = 14;                                     // bigint
        final int LOCOMOTIVE_IS_RESIDENT_DEPOT = 15;                        // boolean
        final int LOCOMOTIVE_USE_AVP = 16;                                  // boolean
        final int DATE_TIME_SAVE_IN_AWS = 17;                               // timestamp with time zone
        final int TYPE_SERIES_TRAIN_ID = 18;                                // integer
        final int SERIA_ASUOP = 19;                                         // integer
        final int SERIAL_CNSI = 20;                                         // integer

        final String sql =
                "SELECT \"Insert_DIRECTORY_SERIES_TRAINS\" " +
                        "( " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "cast(? as real), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "cast(? as timestamp), " +
                        "?, " +
                        "?, " +
                        "?" +
                        "); ";

        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(RAILROAD_ID, idRoad);
            prepStat.setInt(DEPOT_ID, (int)idDepo);
            prepStat.setInt(MIN_NUMBER_SECTION, -1);
            prepStat.setInt(MAX_NUMBER_SECTION, -1);
            prepStat.setInt(MASS_LOAD_SECTION_Kg, -1);    // avpt.gr.train.getWeightTain() * 1000
            prepStat.setInt(LENGTH_SECTION_m, -1);
            prepStat.setDouble(COEFFICIENT_NORM_CONSUMPTION_SECTION_ON_SERIES10000 , -1.0);
            prepStat.setInt(PARAMETER1_SECTION, -1);
            prepStat.setInt(PARAMETER2_SECTION, -1);
            prepStat.setInt(PARAMETER3_SECTION, -1);
            prepStat.setInt(COUNT_SECTIONS, -1);
            int typeSeries = convertTypeSeries(train.getTypeLoc(), train.getLocTypeAsoup());
            prepStat.setString(NAME_SERIES_TRAIN, Train.getNameTypeLoc(typeSeries, train.getLocTypeAsoup()));
            prepStat.setString(NAME_TYPE_TRAIN, "Грузовой электровоз");
            prepStat.setLong(CODE_CLASSIFIER, train.getNumLoc());
            prepStat.setBoolean(LOCOMOTIVE_IS_RESIDENT_DEPOT, true);
            prepStat.setBoolean(LOCOMOTIVE_USE_AVP, false);
            prepStat.setString(DATE_TIME_SAVE_IN_AWS, UtilsArmG.getTimestampStr(LocalDateTime.now())); // настоящее время
            prepStat.setInt(TYPE_SERIES_TRAIN_ID, idTypeLoc);
            prepStat.setInt(SERIA_ASUOP, -1);
            prepStat.setInt(SERIAL_CNSI, -1);
        }
        catch (Exception e) {
            throwSQLException("insertSeriesTrain", e.getMessage());
        }
        return execQuery(prepStat);
    }

    /**
     * @param train -
     * @param indSection -
     * @param idUnit -
     * @param idUser -
     * @param idSeriesTrain -
     * @return SECTION_ID
     * @throws SQLException -
     */
    private long insetrtSection(Train train, int indSection, long idUnit, long idUser, long idSeriesTrain) throws SQLException {
        final int UNIT_ROUTE_ID = 1;                            // bigint
        final int USER_ID = 2;                                  // integer
        final int SUBTYPE_SPEC_TRAIN = 3;                       // integer
        final int NUMBER_SERIES_WAGON = 4;                      // smallint
        final int NUMBER_WAGON = 5;                             // smallint
        final int SERIES_WAGON = 6;                             // smallint
        final int RUNNING_SECTION_m = 7;                        // integer
        final int MASK_DIAGNOSTICS_DATA_TRAIN = 8;              // integer
        final int MASK_DIAGNOSTICS_DATA_BOARD_DEVICES_RPDA = 9; // integer
        final int EBack = 10;                                   // real рекуп ~
        final int ERBack = 11;                                  // real
        final int ERecup = 12;                                  // real рекуп =
        final int ENERGY_HEATING_kWh_4 = 13;                    // real
        final int ENERGY_ACTIVE = 14;                           // real активная ~
        final int ENERGY_REACTIVE = 15;                         // real

        final int T_NNA = 16;                                   // integer
        final int T_XXO = 17;                                   // integer
        final int T_XXD = 18;                                   // integer
        final int T_ODA = 19;                                   // integer
        final int T_NO = 20;                                    // integer
        final int T_NA = 21;                                    // integer
        final int f_rsc2 = 22;                                  // integer
        final int f_rsc3 = 23;                                  // integer

        final int T_CCCR = 24;                                  // integer
        final int T_CCCD = 25;                                  // integer
        final int K_CCCD = 26;                                  // integer
        final int tto = 27;                                     // integer
        final int SERIES_TRAIN_ID = 28;                         // integer

        final int EDrawAC = 29;                                 // real DEFAULT '-1'::integer
        final int EHeatAC = 30;                                 // real DEFAULT '-1'::integer
        final int ESum = 31;                                    // real DEFAULT '-1'::integer  суммарн =
        final int EHelp = 32;                                   // real DEFAULT '-1'::integer
        final int EDraw = 33;                                   // real DEFAULT '-1'::integer

        final int EnCountStart = 34;                            // integer DEFAULT '-1'::integer
        final int EnCountFinish = 35;                           // integer DEFAULT '-1'::integer
        final int K_CCCA = 36;                                  // smallint DEFAULT ('-1'::integer)::smallint
        final int COUNTER_SHUTDOWN_FIRE_ALARMS = 37;            // smallint DEFAULT ('-1'::integer)::smallint
        final int COUNTER_SHUTDOWN_RELAY_BOXING = 38;           // smallint DEFAULT ('-1'::integer)::smallint
        final int Kom_s = 39;                                   // integer DEFAULT '-1'::integer
        final int Kom_so = 40;                                  // integer DEFAULT '-1'::integer
        final int AVG_TIME_WORK_COMPRESSOR_s = 41;              // smallint DEFAULT ('-1'::integer)::smallint
        final int COUNT_SHUTDOWN_PROTECTION_AIR = 42;           // integer DEFAULT '-1'::integer
        final int AVG_PRESSURE_COMPRESSOR_ON_100atm = 43;       // smallint DEFAULT ('-1'::integer)::smallint
        final int AVG_PRESSURE_COMPRESSOR_OFF_100atm = 44;      // smallint DEFAULT ('-1'::integer)::smallint
        final int MAX_TIME_WORK_COMPRESSOR_s = 45;              // integer DEFAULT '-1'::integer
        final int TIME_WORK_COMPRESSOR_s = 46;                  // integer DEFAULT '-1'::integer
        final int kwET = 47;                                    // smallint DEFAULT ('-1'::integer)::smallint

        final String sql =
                "SELECT \"Insert_SPECIFICATION_SECTIONS\" " +
                        "(cast(? as bigint), " +
                        "?, " +
                        "?, " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?,  " +
                        "?, " +
                        "?, " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "cast(? as real), " +
                        "?, " +
                        "?, " +
                        "cast(? as smallint), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?" +
                        "); ";

        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setLong(UNIT_ROUTE_ID, idUnit);
            prepStat.setInt(USER_ID, (int)idUser);
            prepStat.setInt(SUBTYPE_SPEC_TRAIN, -1);                            // Подтип СПС (специальный подвижной состав)
            prepStat.setShort(NUMBER_SERIES_WAGON, (short)-1);
            prepStat.setShort(NUMBER_WAGON, (short)-1);
            prepStat.setShort(SERIES_WAGON, (short)-1);
            prepStat.setInt(RUNNING_SECTION_m, indSection + 1);                 // индекс секции
            prepStat.setInt(MASK_DIAGNOSTICS_DATA_TRAIN, -1);
            prepStat.setInt(MASK_DIAGNOSTICS_DATA_BOARD_DEVICES_RPDA, -1);
            prepStat.setDouble(EBack, (int)train.getRec(indSection));
            prepStat.setDouble(ERBack, 0);
            prepStat.setDouble(ERecup, train.getRec());
            prepStat.setDouble(ENERGY_HEATING_kWh_4, 0);
            long act = train.getAct(indSection);
            prepStat.setDouble(ENERGY_ACTIVE, (int)act);
            prepStat.setDouble(ENERGY_REACTIVE, 0);
            prepStat.setInt(T_NNA, -1);                                 // Время без нагрузки, сек
            prepStat.setInt(T_XXO, -1);   // Время работы на ХХ одного дизеля, сек
            prepStat.setInt(T_XXD, -1);                                 // Время работы на ХХ на 2-х дизелях, сек
            prepStat.setInt(T_ODA, -1); // Оба дизеля заглушены, сек
            prepStat.setInt(T_NO, -1);  // Есть нагрузка хотя бы одного дизеля, сек
            prepStat.setInt(T_NA, -1);                                  // Под нагрузкой оба дизеля, сек
            prepStat.setInt(f_rsc2, -1);                                // Расчетная норма 2
            prepStat.setInt(f_rsc3, -1);                                // Расчетная норма 3 диагностики кг
            prepStat.setInt(T_CCCR, -1);       // Время работы системы САЗДТ, сек
            prepStat.setInt(T_CCCD, -1);       // Время работы дизеля при включенной системе САЗДТ, сек
            prepStat.setInt(K_CCCD, -1);      // Кол-во запусков дизеля при системе САЗДТ.
            prepStat.setInt(tto, -1);                    // Код текущего техобслуживания
            prepStat.setInt(SERIES_TRAIN_ID, (int)idSeriesTrain);           // Идентификатор серии поезда
            prepStat.setDouble(EDrawAC, 0);                                 // Энергия, потраченная на тяговые двигатели, перем.ток (кВт*ч)
            prepStat.setDouble(EHeatAC, 0);                                 // Энергия на отопление, перем.ток (кВт*ч)
            prepStat.setDouble(ESum, (int)train.getAct());                    // Суммарная потребленная энергия за поездку, кВт*ч
            prepStat.setDouble(EHelp, 0);                                   // Энергия на отопление, пост.ток (кВт*ч)
            prepStat.setDouble(EDraw, 0);                                   // Энергия, потраченная на тяговые двигатели, пост.ток (кВт*ч)
            long start = train.getCnt_start_act(indSection);
            if (start < 0) return -1;
            prepStat.setInt(EnCountStart, (int)start); // Показание общего счетчика энергии на начало поездки, в 1/4 кВт*ч
            prepStat.setInt(EnCountFinish, (int)train.getCnt_end_act(indSection));  // Показание общего счетчика энергии на конец поездки, в 1/4 кВт*ч

            //  System.out.println((int)avpt.gr.train.getCnt_start_act(indSection) + "  " + (int)avpt.gr.train.getCnt_end_act(indSection));
            prepStat.setInt(K_CCCA, -1);                                    // Кол-во запусков дизеля с помощью конденсаторов при отключеной системе САЗДТ
            prepStat.setShort(COUNTER_SHUTDOWN_FIRE_ALARMS, (short)-1);         // Счетчик Срабатываний пожарной сигнализации
            prepStat.setShort(COUNTER_SHUTDOWN_RELAY_BOXING, (short)-1);        // Счетчик Срабатываний реле боксования
            prepStat.setInt(Kom_s, -1);                                      // Комплектация всей системы (параметры комплектации оборудования РПДА)
            prepStat.setInt(Kom_so, -1);                  // Комплектация системы обогрева
            prepStat.setShort(AVG_TIME_WORK_COMPRESSOR_s, (short)-1);           // Время работы компрессора
            prepStat.setInt(COUNT_SHUTDOWN_PROTECTION_AIR, -1);              // Количество срабатываний защиты воздуха
            prepStat.setShort(AVG_PRESSURE_COMPRESSOR_ON_100atm, (short)-1);    //Среднее давление включения компрессора Атм*100
            prepStat.setShort(AVG_PRESSURE_COMPRESSOR_OFF_100atm, (short)-1);   // Среднее давление выключения компрессора Атм*100
            prepStat.setInt(MAX_TIME_WORK_COMPRESSOR_s, -1);                 // Средняя время работы компрессора (производительность), сек
            prepStat.setInt(TIME_WORK_COMPRESSOR_s, -1);                     // Максимальная продолжительность работы компрессора, сек
            prepStat.setShort(kwET, (short)-1);                                 // Кол-во включений компрессора
        }
        catch (Exception e) {
            throwSQLException("insetrtSection", e.getMessage());
        }
        return execQuery(prepStat);
    }

    private long insertStation(int iStatioin) throws SQLException {
        final int ESR_CODE_STATION = 1;                             // int
        final int CODE_STATION_INTERNAL_DATABASE = 2;               // int
        final int STATION_NAME = 3;                                 // character varying

        final String sql = "SELECT \"Insert_DIRECTORY_STATIONS\"(?, ?, ?); ";

        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setInt(ESR_CODE_STATION, (int)stations.getStation(iStatioin).getEcp());
            prepStat.setInt(CODE_STATION_INTERNAL_DATABASE, -1);
            prepStat.setString(STATION_NAME, stations.getStation(iStatioin).getNameStation());
        }
        catch (Exception e) {
            throwSQLException("insertStation", e.getMessage());
        }
        return execQuery(prepStat);
    }

    private void insertSchedule(int indxStation, long idUnit, long idStation, long idRoute)  throws SQLException {
        final int UNIT_ROUTE_ID = 1;                                        // long
        final int ESR_CODE_STATION = 2;                                     // int
        final int CODE_STATION_TRAIN_SCHEDULE = 3;                          // smallint
        final int CAUSE_DEVIATION_TRAIN_SCHEDULE = 4;                       // smallint,
        final int TIME_ARRIVAL_TRAIN_SCHEDULE_s = 5;                        // double precision,
        final int TIME_ARRIVAL_REAL_TRAIN_SCHEDULE_s = 6;                   // double precision,
        final int TIME_DEPARTURE_TRAIN_SCHEDULE_s = 7;                      // double precision,
        final int TIME_DEPARTURE_REAL_TRAIN_SCHEDULE_s = 8;                 // double precision,
        final int TIME_OPEN_DOORS_TRAIN_SCHEDULE_s = 9;                     // integer,
        final int TIME_CLOSE_DOORS_TRAIN_SCHEDULE_s = 10;                   // integer,
        final int BRUTTO_WEIGHT_TRAIN_SCHEDULE_Kg = 11;                     // integer,
        final int CONSUMPTION_ENERGY_RAILWAY_HAUL_TRAIN_SCHEDULE_kWh_4 = 12;// integer,
        final int DEVIATION_LAST_STATION_TRAIN_UNSCHEDULE_s = 13;           // smallint,
        final int TRAIN_STOP_FLAG_TRAIN_SCHEDULE = 14;                      // smallint,
        final int SPEED_SITE_WORK_TRAIN_SCHEDULE_100Km_h = 15;              // smallint,
        final int TIME_RUNNING_ALSN_YELLOW_TRAIN_SCHEDULE_s = 16;           // smallint,
        final int TIME_RUNNING_ALSN_RED_YELLOW_TRAIN_SCHEDULE_s = 17;       // smallint,
        final int TIME_SURGE_RAILWAY_HAUL_TRAIN_SCHEDULE_s = 18;            // smallint,

        final int PERCENT_WORK_USAVP_REGIME_AUTODRIVING_TRAIN_SCHEDULE = 19;// smallint,
        final int STATION_ID = 20;                                          // integer,
        final int RAILROAD_DIRECTION_ID = 21;                               // integer

        final String sql =
                "SELECT \"Insert_SPECIFICATION_TRAIN_SCHEDULES\" " +
                        "(cast(? as bigint), " +
                        "?, " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as double precision), " +
                        "cast(? as double precision), " +
                        "cast(? as double precision), " +
                        "cast(? as double precision), " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "cast(? as smallint), " +
                        "?, " +
                        "? " +
                        "); ";

        Stations.Station station = stations.getStation(indxStation);
        PreparedStatement prepStat = conn.prepareStatement(sql);
        try {
            prepStat.setLong(UNIT_ROUTE_ID, idUnit);
            prepStat.setInt(ESR_CODE_STATION, (int)station.getEcp());
            prepStat.setShort(CODE_STATION_TRAIN_SCHEDULE, (short)-1);
            prepStat.setShort(CAUSE_DEVIATION_TRAIN_SCHEDULE, (short)-1);
            prepStat.setDouble(TIME_ARRIVAL_TRAIN_SCHEDULE_s, station.getTimeArrivalSchedule());
            prepStat.setDouble(TIME_ARRIVAL_REAL_TRAIN_SCHEDULE_s, station.getTimeArrivalFact());
            prepStat.setDouble(TIME_DEPARTURE_TRAIN_SCHEDULE_s, station.getTimeDepartureSchedule());
            prepStat.setDouble(TIME_DEPARTURE_REAL_TRAIN_SCHEDULE_s, station.getTimeDepartureFact());
            prepStat.setInt(TIME_OPEN_DOORS_TRAIN_SCHEDULE_s, -1);
            prepStat.setInt(TIME_CLOSE_DOORS_TRAIN_SCHEDULE_s, -1);
            prepStat.setInt(BRUTTO_WEIGHT_TRAIN_SCHEDULE_Kg, -1);
            prepStat.setInt(CONSUMPTION_ENERGY_RAILWAY_HAUL_TRAIN_SCHEDULE_kWh_4, -1);
            prepStat.setShort(DEVIATION_LAST_STATION_TRAIN_UNSCHEDULE_s, (short)-1);
            prepStat.setShort(TRAIN_STOP_FLAG_TRAIN_SCHEDULE, (short)-1);
            prepStat.setShort(SPEED_SITE_WORK_TRAIN_SCHEDULE_100Km_h, (short)-1);
            prepStat.setShort(TIME_RUNNING_ALSN_YELLOW_TRAIN_SCHEDULE_s, (short)-1);
            prepStat.setShort(TIME_RUNNING_ALSN_RED_YELLOW_TRAIN_SCHEDULE_s, (short)-1);
            prepStat.setShort(TIME_SURGE_RAILWAY_HAUL_TRAIN_SCHEDULE_s, (short)-1);
            prepStat.setShort(PERCENT_WORK_USAVP_REGIME_AUTODRIVING_TRAIN_SCHEDULE, (short)station.getPercentAuto());
            prepStat.setInt(STATION_ID, (int)idStation);
            prepStat.setInt(RAILROAD_DIRECTION_ID, (int)idRoute);
        }
        catch (Exception e) {
            throwSQLException("insertSchedule", e.getMessage());
        }
        execQuery(prepStat);
    }

    /**
     *
     * @param train -
     * @param idUnit -
     * @param idRoute -
     */
    private void insetrtStationsSched(Train train, long idUnit, long idRoute) throws SQLException {

        List<Stations.Station> stations = train.getStations();
        for (Stations.Station station : stations) {
            long idStation = insertStation(station.getIndex());
            if (idStation > -1)
                insertSchedule(station.getIndex(), idUnit, idStation, idRoute);
        }

//        int start = train.getSecondStart();
//        int end = train.getSecondsEnd();
//        for (int j = 0; j < stations.size(); j++) {
//            Stations.Station station = stations.getStation(j);
//            int cur_second = station.getSecond();
//            if (cur_second > start && cur_second < end) {
//                long idStation = insertStation(j);
//                if (idStation > -1)
//                    insertSchedule(j, idUnit, idStation, idRoute);
//            }
//        }
    }
//----------------------------------------------------------------------------------------------------------------------

    private void throwSQLException(String nameMeth, String mess) throws SQLException {
        throw new SQLException(this.getClass().getName() + "." + nameMeth + " - " + mess);
    }

    /**
     * @param prepStat - PreparedStatement
     * @return результат запроса long
     */
    private long execQuery(PreparedStatement prepStat) throws SQLException {
        long result = -1;
        ResultSet rs;
        try {
            rs = prepStat.executeQuery();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        if (rs.next())
            result = rs.getLong(1);

        rs.close();
        return result;
    }

    /**
     * @param typeLoc -
     * @param typeAsoup -
     * @return - корректировка кода серии локомотива с учетом колдичества секций
     */
    private static int convertTypeSeries(int typeLoc, int typeAsoup) {
        switch (typeLoc) {
            // ЭС5К
            case 5:
                if (typeAsoup == 253) return 35;
                else if (typeAsoup == 222)return 25;
                else if (typeAsoup == 220)return 45;
                else return 5;
                // ЭС5К МСУД 15
            case 11:
                if (typeAsoup == 253) return 30;
                else if (typeAsoup == 222)return 20;
                else return 111;
                // ЭС4К
            case 9:
                if (typeAsoup == 115) return 39;
                else if (typeAsoup == 144)return 29;
                else return 9;
                // ВЛ10
            case 2:
                if (typeAsoup == 138) return 52;
                else return 2;
            default: return typeLoc;
        }
    }
}
