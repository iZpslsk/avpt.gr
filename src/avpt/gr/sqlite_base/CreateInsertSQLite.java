package avpt.gr.sqlite_base;

import avpt.gr.common.Roads;
import avpt.gr.common.UtilsArmG;
import avpt.gr.common.Version;
import avpt.gr.maps.Stations;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateInsertSQLite {

    private static HashMap<String, Long> mapFilenames = new HashMap<String, Long>();

    // Foreign Key On
    private static String sql_foreign_keys_on = "PRAGMA foreign_keys = ON";

    // справочная таблица дорог
    private static String sql_drop_road =
            "DROP TABLE IF EXISTS road";
    private static String sql_road =
            "CREATE TABLE IF NOT EXISTS road (" +
                    "code_road INTEGER NOT NULL UNIQUE, " +
                    "name_road TEXT " +
                    ");";

    /**
     * @param codeRoad - код дороги
     * @return одна строка вставки
     */
    private static String strInsRoad(int codeRoad) {
        return "(" + codeRoad + "," + "\"" + Roads.getName(codeRoad) + "\"" + ")";
    }

    private static String sql_insert_road =
            "INSERT INTO road (code_road, name_road)" +
                    "VALUES " +
                    strInsRoad(Roads._ALL_0) + ", " +
                    strInsRoad(Roads._OCT_1) + ", " +
                    strInsRoad(Roads._KAL_10) + ", " +
                    strInsRoad(Roads._MOS_17) + ", " +
                    strInsRoad(Roads._GOR_24) + ", " +
                    strInsRoad(Roads._SEV_28) + ", " +
                    strInsRoad(Roads._SEV_KAV_51) + ", " +
                    strInsRoad(Roads._UG_VOS_58) + ", " +
                    strInsRoad(Roads._PRI_61) + ", " +
                    strInsRoad(Roads._KUY_63) + ", " +
                    strInsRoad(Roads._SVE_76) + ", " +
                    strInsRoad(Roads._UG_URAL_80) + ", " +
                    strInsRoad(Roads._ZAP_SIB_83) + ", " +
                    strInsRoad(Roads._KRA_88) + ", " +
                    strInsRoad(Roads._VO_SIB_92) + ", " +
                    strInsRoad(Roads._ZAB_94) + ", " +
                    strInsRoad(Roads._DAL_96) + ", " +
                    strInsRoad(Roads._SAH_99) + ", " +
                    strInsRoad(Roads._BEL_13) + "; ";

    // справочная таблица типов локомотивов
    private static String sql_drop_locomotive_type =
            "DROP TABLE IF EXISTS locomotive_type";
    private static String sql_locomotive_type =
            "CREATE TABLE IF NOT EXISTS locomotive_type (" +
                    "code_type_loc INTEGER NOT NULL," +
                    "code_asoup INTEGER NOT NULL," +
                    "name_type_loc text NOT NULL" +
                    ");";
    private static String sql_locomotive_type_index =
            "CREATE INDEX locomotive_type_index ON locomotive_type (code_type_loc, code_asoup)";

    /**
     * @param typeLoc - код типа локомотива
     * @return одна строка для вставки
     */
    private static String strInsType(int codeAsoup, int typeLoc) {
        return "(" + typeLoc + "," + "\"" + codeAsoup + "\"," + "\"" + Train.getNameTypeLoc(typeLoc, codeAsoup) + "\"" + ")";
    }

    private static String sql_insert_locomotive_type =
            "INSERT INTO locomotive_type (code_type_loc, code_asoup, name_type_loc) " +
                    "VALUES " +
                    strInsType(134, Train.VL11) + ", " +
                    strInsType(123, Train.VL10) + ", " +
                    strInsType(240, Train.VL80) + ", " +
                    strInsType(244, Train.VL85) + ", " +
                    strInsType(0, Train.S5K) + ", " +
                    strInsType(222, Train.S5K) + ", " + // 2ЭС5К
                    strInsType(253, Train.S5K) + ", " + // 3ЭС5К
                    strInsType(220, Train.S5K) + ", " + // 4ЭС5К
                    strInsType(0, Train.KZ8A) + ", " +
                    strInsType(222, Train.S5) + ", " +
                    strInsType(145, Train.S6) + ", " +
                    strInsType(144, Train.S4K) + ", " +
                    strInsType(0, Train.S5K_2) + ", " +     // ЭС5К
                    strInsType(222, Train.S5K_2) + ", " +   // 2ЭС5К
                    strInsType(253, Train.S5K_2) + ", " +   // 3ЭС5К
                    strInsType(220, Train.S5K_2) + ", " +   // 4ЭС5К
                    strInsType(134, Train.VL11_ASIM) + ", " +
                    strInsType(142, Train.VL15_ASIM) + ", " +
                    strInsType(244, Train.VL85_ASIM) + ", " +
                    strInsType(123, Train.VL10_ASIM) + ", " +
                    strInsType(135, Train.VL11K_ASIM) + ", " +
                    strInsType(240, Train.VL80C_ASIM) + ", " +
                    strInsType(226, Train.VL80T_ASIM) + ", " +
                    strInsType(230, Train.VL80TK_ASIM) + ", " +
                    strInsType(233, Train.VL80P_ASIM) + ", " +
                    strInsType(239, Train.VL80SK_ASIM) + ", " +
                    strInsType(139, Train.VL10UK_ASIM) + ", " +
                    strInsType(125, Train.VL10K_ASIM) + ", " +
                    strInsType(138, Train.VL10U_ASIM) + ", " +
                    strInsType(153, Train.VL11M_ASIM) + "; ";

    // таблица - информация о версии
    private static String sql_drop_version =
            "DROP TABLE IF EXISTS version";
    private static String sql_version =
            "CREATE TABLE IF NOT EXISTS version (" +
                    "major INTEGER NOT NULL, " +
                    "minor INTEGER NOT NULL" +
                    ");";

    // таблица файлов с поездками
    private static String sql_drop_file =
            "DROP TABLE IF EXISTS file";
    private static String sql_file =
            "CREATE TABLE IF NOT EXISTS file ( " +
                    "file_id INTEGER PRIMARY KEY, " +
                    "file_name TEXT NOT NULL, " +
                    "date_save DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    ");";

    // таблица локомотивов
    private static String sql_drop_locomotive =
            "DROP TABLE IF EXISTS locomotive";
    private static String sql_locomotive =
            "CREATE TABLE IF NOT EXISTS locomotive (" +
                    "locomotive_id INTEGER PRIMARY KEY, " +
                    "code_type_loc INTEGER NOT NULL, " +
                    "code_asoup INTEGER NOT NULL, " +
                    "num_loc INTEGER NOT NULL, " +
                    "UNIQUE (code_type_loc, code_asoup, num_loc), " +
                    "FOREIGN KEY (code_type_loc, code_asoup) REFERENCES locomotive_type (code_type_loc, code_asoup) " +
                    ");";

    // таблица машинистов
    private static String sql_drop_driver =
            "DROP TABLE IF EXISTS driver";
    private static String sql_driver =
            "CREATE TABLE IF NOT EXISTS driver (" +
                    "driver_id INTEGER PRIMARY KEY, " +
                    "tab_num INTEGER NOT NULL, " +
//                    "code_position INTEGER NOT NULL, " +
                    "code_road INTEGER NOT NULL, " +
                    "n_tch INTEGER NOT NULL, " +
                    "n_col INTEGER NOT NULL, " +
                    "UNIQUE (tab_num, code_road) " +
                    "FOREIGN KEY (code_road) REFERENCES road (code_road) " +
                    ");";

    // таблица секций
    private static String sql_drop_section =
            "DROP TABLE IF EXISTS section";
    private static String sql_section =
            "CREATE TABLE IF NOT EXISTS section (" +
                    "section_id INTEGER PRIMARY KEY, " +
                    "train_id INTEGER NOT NULL, " +
                    "num INTEGER NOT NULL, " +
                    "act INTEGER, " +
                    "rec INTEGER, " +
                    "cnt_start_act INTEGER, " +
                    "cnt_end_act INTEGER, " +
                    "cnt_start_rec INTEGER, " +
                    "cnt_end_rec INTEGER, " +
                    "FOREIGN KEY (train_id) REFERENCES train (train_id) " +
                    ");";

    // таблица поездок
    private static String sql_drop_train =
            "DROP TABLE IF EXISTS train";
    private static String sql_train =
            "CREATE TABLE IF NOT EXISTS train (" +
                    "train_id INTEGER PRIMARY KEY, " +
                    "file_id INTEGER NOT NULL, " +
                    "driver_id INTEGER NOT NULL, " +
                    "locomotive_id INTEGER NOT NULL, " +

                    "train_num INTEGER NOT NULL, " +
                    "date_begin TIMESTAMP NOT NULL, " +
                    "date_map TIMESTAMP NOT NULL, " +
                    "status_isavprt INTEGER NOT NULL, " +
                    "addr_isavprt INTEGER NOT NULL, " +
                    "code_road INTEGER NOT NULL, " +
                    "num_tch INTEGER NOT NULL, " +
                    "ver_po TEXT , " +
                    "is_shed_load INTEGER NOT NULL, " +
                    "num_section INTEGER NOT NULL, " +

                    "weight INTEGER NOT NULL, " +
                    "length INTEGER NOT NULL, " +
                    "wags_cnt INTEGER NOT NULL, " +
                    "wags_empty_cnt INTEGER NOT NULL, " +
                    "bandage INTEGER NOT NULL, " +
                    "work REAL NOT NULL, " +
                    "distance INTEGER NOT NULL, " +
                    "distance_prompt INTEGER NOT NULL, " +
                    "distance_auto INTEGER NOT NULL, " +
                    "seconds INTEGER NOT NULL, " +
                    "seconds_prompt INTEGER NOT NULL, " +
                    "seconds_auto INTEGER NOT NULL, " +
                    "speed_move REAL NOT NULL, " +
                    "speed REAL NOT NULL, " +
                    "seconds_stop INTEGER NOT NULL, " +
                    "v_lim_cnt INTEGER NOT NULL, " +
                    "c_lim_cnt INTEGER NOT NULL, " +
                    "v_lim_len INTEGER NOT NULL, " +
                    "route_name TEXT, " +
                    "stations_start_end_name TEXT, " +

                    "FOREIGN KEY (file_id) REFERENCES file (file_id), " +
                    "FOREIGN KEY (driver_id) REFERENCES driver (driver_id), " +
                    "FOREIGN KEY (code_road) REFERENCES road (code_road), " +
                    "FOREIGN KEY (locomotive_id) REFERENCES locomotive (locomotive_id) " +
                    ");";

    // таблица станций
    private static String sql_drop_station =
            "DROP TABLE IF EXISTS station";
    private static String sql_station =
            "CREATE TABLE IF NOT EXISTS station (" +
                    "station_id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "esr_code INTEGER " +
                    ");";
    private static String sql_station_esr_index =
            "CREATE INDEX station_esr_index ON station (esr_code)";


    // таблица расписания
    private static String sql_drop_schedule =
            "DROP TABLE IF EXISTS schedule";
    private static String sql_schedule =
            "CREATE TABLE IF NOT EXISTS schedule (" +
                    "schedule_id INTEGER PRIMARY KEY, " +
                    "train_id INTEGER NOT NULL, " +
                    "station_id INTEGER NOT NULL, " +
                    "arrival_sch INTEGER NOT NULL, " +
                    "arrival_real INTEGER NOT NULL, " +
                    "departure_sch INTEGER NOT NULL, " +
                    "departure_real INTEGER NOT NULL, " +
                    "auto_percent INTEGER NOT NULL, " +
                    "FOREIGN KEY (train_id) REFERENCES train (train_id), " +
                    "FOREIGN KEY (station_id) REFERENCES station (station_id) " +
                    ");";

    // диагностика всц
    private static String sql_drop_vsc_diagnostic =
            "DROP TABLE IF EXISTS vsc_diagnostic";
    private static String sql_vsc_diagnostic =
            "CREATE TABLE IF NOT EXISTS vsc_diagnostic (" +
                    "sql_diagnostic_id INTEGER PRIMARY KEY, " +
                    "train_id INTEGER NOT NULL, " +
                    "type_vsc TEXT, " +
                    "main_link_modem_perc INTEGER NOT NULL, " +
                    "main_link_vsc_perc INTEGER NOT NULL, " +
                    "slave_link_modem_perc INTEGER NOT NULL, " +
                    "slave_link_vsc_perc INTEGER NOT NULL, " +
                    "slave_is_on_perc INTEGER NOT NULL, " +
                    "num_link_loc INTEGER NOT NULL, " +
                    "test_thrust INTEGER NOT NULL, " +
                    "test_brake INTEGER NOT NULL, " +
                    "u_max INTEGER NOT NULL, " +
                    "is_alsn INTEGER NOT NULL, " +
//                    "sec_num INTEGER NOT NULL, " +
                    "FOREIGN KEY (train_id) REFERENCES train (train_id) " +
                    ");";

    private static Connection conn = null;

    public static Connection getConnection() {
        return  conn;
    }

    private static Statement getStatement() throws SQLException {
        return conn != null ? conn.createStatement() : null;
    }

    private static void insertVersion(int major, int minor) throws SQLException {
        final int nMajor = 1;
        final int nMinor = 2;
        final String sql = "INSERT INTO version (major, minor) " +
                "VALUES (?, ?)";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(nMajor, major);
        prepStat.setInt(nMinor, minor);
        prepStat.executeUpdate();
    }

    /**
     * @throws SQLException -
     */
    public static void makeNewTables() throws SQLException {

        Statement statement = getStatement();
        if (statement != null) {
//            statement.execute(sql_drop_version);
//            statement.execute(sql_drop_road);
//            statement.execute(sql_drop_locomotive_type);
//            statement.execute(sql_drop_file);
//            statement.execute(sql_drop_locomotive);
//            statement.execute(sql_drop_driver);
//            statement.execute(sql_drop_train);
//            statement.execute(sql_drop_section);
//            statement.execute(sql_drop_vsc_diagnostic);
//            statement.execute(sql_drop_schedule);
//            statement.execute(sql_drop_station);

            statement.execute(sql_road);
            statement.execute(sql_insert_road);

            statement.execute(sql_locomotive_type);
            statement.execute(sql_insert_locomotive_type);

            statement.execute(sql_version);

            statement.execute(sql_file);
            statement.execute(sql_locomotive);
            statement.execute(sql_driver);
            statement.execute(sql_train);
            statement.execute(sql_section);
            statement.execute(sql_vsc_diagnostic);

            statement.execute(sql_station);
            statement.execute(sql_station_esr_index);
            statement.execute(sql_schedule);

            statement.execute(sql_foreign_keys_on);

            insertVersion(Version.getVersionJar().getMajor(), Version.getVersionJar().getMinor());

            conn.commit();
        }
    }

    /**
     * @param name - короткое имя файла базы с расширением .db
     * @return - длинное имя базы данных в каталоге текущего пользователя /db/
     * @throws FileNotFoundException -
     */
    private static String getFullBaseName(String name) throws FileNotFoundException {
        File fullName = UtilsArmG.makeUserPath("db", false);
        fullName = new File(fullName, name);
        return fullName.getPath();
    }

    /**
     * соединение с базой, если базы нет создается новая
     * @param name - имя файла базы данных
     * @return connection
     * @throws SQLException -
     */
    public static boolean doConnection(String name) throws SQLException {
        try {
            int major = Version.getVersionBase().getMajor();
            int minor = Version.getVersionBase().getMinor();
            name = name + "_" + major + "_" + minor + ".db";
            String baseName = getFullBaseName(name);
            String url = "jdbc:sqlite:" + baseName;
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void doDisconnect() throws SQLException {
        if (conn != null) conn.close();
        conn = null;
    }

    private static boolean isTableVersionExists() throws SQLException {
        final String sql =
                "SELECT COUNT(*) AS cnt FROM sqlite_master " +
                        "WHERE type='table' AND name='version'";
        Statement statement = getStatement();
        if (statement == null) return false;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next())
            return rs.getInt("cnt") > 0;
        return false;
    }

    public static boolean isVersionExists() throws SQLException {
        final String sql =
                "SELECT major, minor FROM version";
        int major = Version.getVersionJar().getMajor();
        int minor = Version.getVersionJar().getMinor();
        if (isTableVersionExists()) {
            Statement statement = getStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next())
                return major == rs.getInt("major") && (minor == rs.getInt("minor"));
        }
        return false;
    }

    public static long insertFile(String nameFileImage) throws SQLException {
        final int nFileName = 1;
        final String sql =  "INSERT INTO file (file_name) VALUES (?)";
        PreparedStatement prepStat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        prepStat.setString(nFileName, nameFileImage);
        int rowAffected = prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    private static long insertTrain(long file_id, long driver_id, long loc_id,
                                    int code_road, int n_tch, Train train) throws SQLException {

        if (train == null || train.getSeconds() < 10 * 60) return -1;
        final String sql =
                "INSERT INTO train (file_id, driver_id, locomotive_id, train_num, date_begin, date_map, " +
                        "status_isavprt, addr_isavprt, code_road, num_tch, ver_po, is_shed_load, num_section, " +
                        "weight, length, wags_cnt, wags_empty_cnt, bandage, work, distance, distance_prompt, distance_auto, " +
                        "seconds, seconds_prompt, seconds_auto, speed_move, speed, seconds_stop, " +
                        "v_lim_cnt, c_lim_cnt, v_lim_len, route_name, stations_start_end_name)" +
                        "VALUES (?, ?, ?, ?, ?, ?, " +
                                "?, ?, ?, ?, ?, ?, ?, " +
                                "?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                                "?, ?, ?, ?, ?, ?, " +
                                "?, ?, ?, ?, ?)";

        final int nFileId = 1;
        final int nDriver = 2;
        final int nLocomotive = 3;
        final int nTrainNum = 4;
        final int nDateBegin = 5;
        final int nDateMap = 6;
        final int nStatusIsavprt = 7;
        final int nAddrIsavprt = 8;
        final int nCodeRoad = 9;
        final int nNumTch = 10;
        final int nVerPo = 11;
        final int nIsShedLoad = 12;
        final int nNumSection = 13;
        final int nWeight = 14;
        final int nLength = 15;
        final int nWagsCnt = 16;
        final int nWagsEmptyCnt = 17;
        final int nBandage = 18;
        final int nWork = 19;
        final int nDistance = 20;
        final int nDistancePrompt = 21;
        final int nDistanceAuto = 22;
        final int nSeconds = 23;
        final int nSecondsPrompt = 24;
        final int nSecondsAuto = 25;
        final int nSpeedMove = 26;
        final int nSpeed = 27;
        final int nSecondsStop = 28;
        final int nVLimCnt = 29;
        final int nCLimCnt = 30;
        final int nVLimLen = 31;
        final int nRouteName = 32;
        final int nStationStartEndName = 33;

        PreparedStatement prepStat = conn.prepareStatement(sql);

        prepStat.setLong(nFileId, file_id);
        prepStat.setLong(nDriver, driver_id);
        prepStat.setLong(nLocomotive, loc_id);
        prepStat.setInt(nTrainNum, train.getNumTrain());
        prepStat.setString(nDateBegin, train.getDateTimeStart().format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setString(nDateMap, train.getDateMap().format(UtilsArmG.formatDateSQLite));
        prepStat.setInt(nStatusIsavprt, train.getStIsavprt());
        prepStat.setInt(nAddrIsavprt, train.getNetAddress());
        prepStat.setInt(nCodeRoad, code_road);
        prepStat.setInt(nNumTch, n_tch);
        prepStat.setString(nVerPo, train.getVerBr());
        prepStat.setBoolean(nIsShedLoad, train.isChangeLoadSchedule());// ----------------
        prepStat.setInt(nNumSection, train.getNumSection());
        prepStat.setInt(nWeight, train.getWeightTrain());
        prepStat.setInt(nLength, train.getLengthTrain());
        prepStat.setInt(nWagsCnt, train.getCntWags());
        prepStat.setInt(nWagsEmptyCnt, train.getEmptyCnt());// ----------------------
        prepStat.setInt(nBandage, train.getdBandage());
        prepStat.setDouble(nWork, (train.getDistance() * train.getWeightTrain() / 10000.0)); // --------------------
        prepStat.setLong(nDistance, train.getDistance());
        prepStat.setLong(nDistancePrompt, train.getDistance_prompt());
        prepStat.setLong(nDistanceAuto, train.getDistance_auto());
        prepStat.setLong(nSeconds, train.getSeconds());
        prepStat.setLong(nSecondsPrompt, train.getSeconds_prompt());
        prepStat.setLong(nSecondsAuto, train.getSeconds_auto());
        prepStat.setDouble(nSpeedMove, train.getAvgSpeedMove());
        prepStat.setDouble(nSpeed, train.getAvgSpeed());
        prepStat.setLong(nSecondsStop, train.getSeconds_stop());
        prepStat.setInt(nVLimCnt, train.getCntVLim());
        prepStat.setInt(nCLimCnt, train.getCntCLim(true));
        prepStat.setInt(nVLimLen, train.getLenVLim());
        prepStat.setString(nRouteName, train.getRoutName());
        prepStat.setString(nStationStartEndName, train.getStationsBeginEnd());

        prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    private static long isLocExists(int code_type, int num) throws  SQLException {
        final String sql = "SELECT * FROM locomotive WHERE code_type_loc = ? AND num_loc = ?";
        final int nIdType = 1;
        final int nNum = 2;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(nIdType, code_type);
        prepStat.setInt(nNum, num);
        ResultSet rs = prepStat.executeQuery();
        if (rs.next()) {
            return rs.getLong("locomotive_id");
        }
        return -1;
    }

    /**
     *
     * @param tab -
     * @param code_road -
     * @return driver_id если существует или -1 если не существует
     * @throws SQLException
     */
    private static long isDriverExists(int tab, int code_road) throws SQLException {
        final String sql = "SELECT * FROM driver WHERE tab_num = ? AND code_road = ?";
        final int nTab = 1;
        final int nRoad = 2;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(nTab, tab);
        prepStat.setInt(nRoad, code_road);
        ResultSet rs = prepStat.executeQuery();
        if (rs.next()) {
            return rs.getLong("driver_id");
        }
        return -1;
    }

    /**
     *
     * @param code_type -
     * @param num -
     * @return locomotive_id
     * @throws SQLException
     */
    private static long insertLocomotive(int code_type, int code_asoup, int num) throws SQLException {
        long id = isLocExists(code_type, num);
        if (id > -1) return id;
        final String sql =  "INSERT INTO locomotive (code_type_loc, code_asoup, num_loc) VALUES (?, ?, ?)";
        final int nCodeType = 1;
        final int nCodeAsoup = 2;
        final int nNum = 3;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(nCodeType, code_type);
        prepStat.setInt(nCodeAsoup, code_asoup);
        prepStat.setInt(nNum, num);
        prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    /**
     *
     * @param tab -
     * @param code_road -
     * @param n_tch -
     * @param n_col -
     * @return driver_id
     * @throws SQLException
     */
    private static long insertDriver(int tab, int code_road, int n_tch, int n_col) throws SQLException {
        long id = isDriverExists(tab, code_road);
        if (id > -1) return id;
        final String sql =
                "INSERT INTO driver (tab_num, code_road, n_tch, n_col) " +
                        "VALUES (?, ?, ?, ?)";
        final int nTab = 1;
        final int nRoad = 2;
        final int nTch = 3;
        final int nCol = 4;
        PreparedStatement prepStat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        prepStat.setInt(nTab, tab);
        prepStat.setInt(nRoad, code_road);
        prepStat.setInt(nTch, n_tch);
        prepStat.setInt(nCol, n_col);
        prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    private static long insertDiagnosticVSC(long train_id, Train train) throws SQLException {
        if (train == null) return -1;
        final String sql =
                "INSERT INTO vsc_diagnostic (train_id, type_vsc, main_link_modem_perc, main_link_vsc_perc," +
                        "slave_link_modem_perc, slave_link_vsc_perc, slave_is_on_perc, num_link_loc," +
                        "test_thrust, test_brake, u_max, is_alsn) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        final int nTrainId = 1;
        final int nTypeVsc = 2;
        final int nMainLinkModemPerc = 3;
        final int nMainLinkVscPerc = 4;
        final int nSlaveLinkModemPerc = 5;
        final int nSlaveLinkVscPerc = 6;
        final int nSlaveIsOnPerc = 7;
        final int nNumLinkLoc = 8;
        final int nTestThrust = 9;
        final int nTestBrake = 10;
        final int nUMax = 11;
        final int nIsAlsn = 12;

        String typeVsc = train.getStatusIsavprt(train.getStIsavprt());
        int mainLinkModemPerc = train.getStIsavprt() == 0 ? -1 : train.getMainLinkModem().getPercent();
        int mainLinkVscPerc = train.getStIsavprt() == 0 ? -1 : train.getMainLinkVSC().getPercent();
        int slaveLinkModemPerc = train.getStIsavprt() == 0 ? -1 : train.getSlaveLinkModem().getPercent();
        int slaveLinkVscPerc = train.getStIsavprt() == 0 ? -1 : train.getSlaveLinkVSC().getPercent();
        int slaveIsOnPerc = train.getStIsavprt() == 0 ? -1 : train.getSlaveIsOn().getPercent();
        int numLinkLoc = train.getNumSlave();
        boolean isTestThrust = train.isTestThrust();
        boolean isTestBrake = train.isTestBrake();
        long uMax = train.getUMax();
        boolean isAlsn = train.isAlsn();
        int secNum = train.getNumSection();

        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setString(nTypeVsc, typeVsc);
        prepStat.setInt(nMainLinkModemPerc, mainLinkModemPerc);
        prepStat.setInt(nMainLinkVscPerc, mainLinkVscPerc);
        prepStat.setInt(nSlaveLinkModemPerc, slaveLinkModemPerc);
        prepStat.setInt(nSlaveLinkVscPerc, slaveLinkVscPerc);
        prepStat.setInt(nSlaveIsOnPerc, slaveIsOnPerc);
        prepStat.setInt(nNumLinkLoc, numLinkLoc);
        prepStat.setBoolean(nTestThrust, isTestThrust);
        prepStat.setBoolean(nTestBrake, isTestBrake);
        prepStat.setLong(nUMax, uMax);
        prepStat.setBoolean(nIsAlsn, isAlsn);
        prepStat.executeUpdate();

        ResultSet rs = prepStat.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    private static void insertSections(long train_id, Train train) throws SQLException {
        if (train == null) return;
        final String sql =
                "INSERT INTO section (train_id, num, act, rec, cnt_start_act, cnt_end_act, cnt_start_rec, cnt_end_rec) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        final int nTrainId = 1;
        final int nNum = 2;
        final int nAct = 3;
        final int nRec = 4;
        final int nCnt_start_act = 5;
        final int nCnt_end_act = 6;
        final int nCnt_start_rec = 7;
        final int nCnt_end_rec = 8;

        int num = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setInt(nNum, num);
        prepStat.setLong(nAct, train.getAct1() == 0 ? train.getAct() : train.getAct1());
        prepStat.setLong(nRec, train.getRec1() == 0 ? train.getRec() : train.getRec1());
        prepStat.setLong(nCnt_start_act, train.getCnt_start_act1() == 0 ? train.getCnt_start_act() : train.getCnt_start_act1());
        prepStat.setLong(nCnt_end_act, train.getCnt_end_act1() == 0 ? train.getCnt_end_act() : train.getCnt_end_act1());
        prepStat.setLong(nCnt_start_rec, train.getCnt_start_rec1() == 0 ? train.getCnt_start_rec() : train.getCnt_start_rec1());
        prepStat.setLong(nCnt_end_rec, train.getCnt_end_rec1() == 0 ? train.getCnt_end_rec() : train.getCnt_end_rec1());
        prepStat.executeUpdate();

        num = 2;
        prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setInt(nNum, num);
        prepStat.setLong(nAct, train.getAct2());
        prepStat.setLong(nRec, train.getRec2());
        prepStat.setLong(nCnt_start_act, train.getCnt_start_act2());
        prepStat.setLong(nCnt_end_act, train.getCnt_end_act2());
        prepStat.setLong(nCnt_start_rec, train.getCnt_start_rec2());
        prepStat.setLong(nCnt_end_rec, train.getCnt_end_rec2());
        prepStat.executeUpdate();

        num = 3;
        prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setInt(nNum, num);
        prepStat.setLong(nAct, train.getAct3());
        prepStat.setLong(nRec, train.getRec3());
        prepStat.setLong(nCnt_start_act, train.getCnt_start_act3());
        prepStat.setLong(nCnt_end_act, train.getCnt_end_act3());
        prepStat.setLong(nCnt_start_rec, train.getCnt_start_rec3());
        prepStat.setLong(nCnt_end_rec, train.getCnt_end_rec3());
        prepStat.executeUpdate();

        num = 4;
        prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setInt(nNum, num);
        prepStat.setLong(nAct, train.getAct4());
        prepStat.setLong(nRec, train.getRec4());
        prepStat.setLong(nCnt_start_act, train.getCnt_start_act4());
        prepStat.setLong(nCnt_end_act, train.getCnt_end_act4());
        prepStat.setLong(nCnt_start_rec, train.getCnt_start_rec4());
        prepStat.setLong(nCnt_end_rec, train.getCnt_end_rec4());
        prepStat.executeUpdate();
    }

    private static int insertStation(Stations.Station station) throws SQLException {
        if (station == null) return -1;
        final String sql =
                "INSERT INTO station (name, esr_code) " +
                        "SELECT ?, ? " +
                        "WHERE NOT EXISTS (SELECT * FROM station WHERE esr_code = ?)";
        final int nName = 1;
        final int nESR_Code = 2;
        final int nESR_Code_in = 3;

        PreparedStatement prepStat = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        String name = station.getNameStation();
        prepStat.setString(nName, name);
        long esr = station.getEcp();
        prepStat.setLong(nESR_Code, esr);
        prepStat.setLong(nESR_Code_in, esr);
        prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        if (id == -1) id = getIdStation(station.getEcp());
        return id;
    }

    private static int getIdStation(long esr) throws SQLException {
        final String sql =
                "SELECT station_id " +
                "FROM station " +
                "WHERE esr_code = ?";

        Statement statement = getStatement();
        if (statement == null) return -1;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next())
            return rs.getInt("station_id");
        else
            return -1;
    }

    private static void insertSchedule(long train_id, long station_id, Stations.Station station, Train train) throws SQLException {
        final String sql =
                "INSERT INTO schedule (train_id, station_id, arrival_sch, arrival_real, departure_sch, departure_real, auto_percent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        final int nTrainId = 1;
        final int nStationId = 2;
        final int nArrivalSch = 3;
        final int nArrivalReal = 4;
        final int nDepartureSch = 5;
        final int nDepartureReal = 6;
        final int nAutoPercent = 7;

        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nTrainId, train_id);
        prepStat.setLong(nStationId, station_id);
        prepStat.setLong(nArrivalSch, station.getTimeArrivalSchedule());
        prepStat.setLong(nArrivalReal, station.getTimeArrivalFact());
        prepStat.setLong(nDepartureSch, station.getTimeDepartureSchedule());
        prepStat.setLong(nDepartureReal, station.getTimeDepartureFact());
        prepStat.setLong(nAutoPercent, station.getPercentAuto());
        prepStat.executeUpdate();

    }

    private static void delFromSection(long id_file) throws SQLException {
        final String sql = "DELETE FROM section " +
                           "WHERE train_id = (SELECT train_id " +
                                             "FROM train t " +
                                             "WHERE t.file_id = ? " +
                                             "AND section.train_id = t.train_id)";
        final int nIdFile = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nIdFile, id_file);
        prepStat.executeUpdate();
    }

    /**
     * удаляем из train
     * @param id_file -
     * @throws SQLException -
     */
    private static void delFromTrain(long id_file) throws SQLException {
        final String sql =  "DELETE FROM train " +
                "WHERE file_id = ? ";
        final int nIdFile = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nIdFile, id_file);
        prepStat.executeUpdate();
    }

    /**
     * удаляем из file
     * @param id_file -
     * @throws SQLException -
     */
    private static void delFromFile(long id_file) throws SQLException {
        final String sql =  "DELETE FROM file " +
                "WHERE file_id = ? ";
        final int nIdFile = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nIdFile, id_file);
        prepStat.executeUpdate();
    }

    private static void delFromDiagnosticVsc(long id_file) throws SQLException {
        final String sql = "DELETE FROM vsc_diagnostic " +
                            "WHERE train_id = (SELECT train_id " +
                                              "FROM train t " +
                                              "WHERE t.file_id = ? " +
                                              "AND vsc_diagnostic.train_id = t.train_id)";
        final int nIdFile = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nIdFile, id_file);
        prepStat.executeUpdate();
    }

    private static void delFromSchedule(long id_file) throws SQLException {
        final String sql = "DELETE FROM schedule " +
                "WHERE train_id = (SELECT train_id " +
                "FROM train t " +
                "WHERE t.file_id = ? " +
                "AND schedule.train_id = t.train_id)";
        final int nIdFile = 1;
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setLong(nIdFile, id_file);
        prepStat.executeUpdate();
    }

    /**
     * заполнить hash mapFileNames
     * @return - hash map ключи всех имен файлов и их значения id из бызы
     * @throws SQLException
     */
    public static void fillMapFilenames() throws SQLException {
//        mapFileNames = new HashMap<String, Long>();
        final String sql =
                "SELECT file_id, file_name " +
                        "FROM file";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            Long id = rs.getLong("file_id");
            String name = rs.getString("file_name");
            name = UtilsArmG.getShortNameFile(name);
            mapFilenames.put(name, id);
        }
    }

    /**
     * @return - hash map ключи всех имен файлов и их значения id из бызы
     */
    public static Map<String, Long> getMapFilenames() {
        return mapFilenames;
    }

    /**
     * insert всех поездок из файла
     * @param arrTrains -
     * @param code_road -
     * @param n_tch -
     * @throws SQLException -
     */
    public static void insertTrains(ArrTrains arrTrains, String fileName, int code_road, int n_tch) throws SQLException {
        Long file_id = -1L;
        if (arrTrains == null) return;
        try {
            file_id = mapFilenames.get(UtilsArmG.getShortNameFile(fileName));
            if (file_id != null)
                deleteFile(file_id);
            file_id = insertFile(fileName);
            mapFilenames.put(UtilsArmG.getShortNameFile(fileName), file_id);
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
        if (file_id == null || file_id < 0) return;

        for (int i = 0; i < arrTrains.size(); i++) {
            Train train = arrTrains.get(i);
            if (train.getSeconds() < 120) continue;

            long driver_id = insertDriver((int)train.getNumTab(), code_road, n_tch, 0);
            long loc_id = insertLocomotive(train.getTypeLoc(), train.getLocTypeAsoup (), (int)train.getNumLoc());
            long train_id = insertTrain(file_id, driver_id, loc_id, code_road, n_tch, train);
            if (train_id > 0) {
                insertSections(train_id, train);
                insertDiagnosticVSC(train_id, train);
            }

            List<Stations.Station> stations = train.getStations();
            for (Stations.Station station : stations) {
                int station_id = insertStation(station);
                insertSchedule(train_id, station_id, station, train);
            }
        }
        conn.commit();
    }

    /**
     * открываем или создаем новую если базы не существует
     * @param name - имя базы данных
     * @throws SQLException -
     */
    public static void createBase(String name) throws SQLException {
        if (doConnection(name)) {
            // если новая - пустая база, создаем новые таблицы
            if (!isVersionExists())
                makeNewTables();
        }
    }

    public static class ItemLocomotive {
        private int codeType;
        private int codeAsoup;
        private int num;

        public  ItemLocomotive(int codeType, int codeAsoup, int num) {
            this.codeType = codeType;
            this.codeAsoup = codeAsoup;
            this.num = num;
        }

        public int getCodeType() {
            return codeType;
        }

        public int getCodeAsoup() {
            return codeAsoup;
        }

        public int getNum() {
            return num;
        }
    }

    public static class ItemRoad {
        private int code;
        private String name;

        public ItemRoad(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static ArrayList<ItemRoad> getArrRoads() throws SQLException {
        final String sql =
                "SELECT code_road, name_road " +
                        "FROM road";
        ArrayList<ItemRoad> list = new ArrayList<ItemRoad>();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            int code = rs.getInt("code_road");
            String name = rs.getString("name_road");
            list.add(new ItemRoad(code, name));
        }
        return list;
    }

    public static ArrayList<ItemLocomotive> getArrLocomotives(int codeRoad, int num_tch) throws SQLException {
        final String sql =
                "SELECT l.code_type_loc, l.code_asoup, num_loc " +
                        "FROM locomotive l " +
                        "INNER JOIN locomotive_type lt ON l.code_type_loc = lt.code_type_loc " +
                        "INNER JOIN train t ON t.locomotive_id = l.locomotive_id " +
                        "WHERE (code_road = ? AND num_tch = ?) OR (0 = ? AND 0 = ?) " +
                        "GROUP BY l.code_type_loc, num_loc ";

        final int ordCodeRoad1 = 1;
        final int ordTchNum1 = 2;
        final int ordCodeRoad2 = 3;
        final int ordTchNum2 = 4;

        ArrayList<ItemLocomotive> list = new ArrayList<ItemLocomotive>();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setInt(ordCodeRoad1, codeRoad);
        stat.setInt(ordTchNum1, num_tch);
        stat.setInt(ordCodeRoad2, codeRoad);
        stat.setInt(ordTchNum2, num_tch);
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            int code_type = rs.getInt("code_type_loc");
            int code_asoup = rs.getInt("code_asoup");
            int num = rs.getInt("num_loc");
            list.add(new ItemLocomotive(code_type, code_asoup, num));
        }
        return list;
    }

    /**
     * удаляем из таблицы file и всех связаных таблиц
     * @param id_file -
     * @throws SQLException -
     */
    public static void deleteFile(long id_file) throws SQLException {
        delFromSchedule(id_file);
        delFromDiagnosticVsc(id_file);
        delFromSection(id_file);
        delFromTrain(id_file);
        delFromFile(id_file);
    }

}
