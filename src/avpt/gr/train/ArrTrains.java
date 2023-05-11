package avpt.gr.train;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;
import avpt.gr.blocks32.asim.Block32_C0_0;
import avpt.gr.blocks32.asim.Block32_C2_0;
import avpt.gr.blocks32.overall.*;
import avpt.gr.chart_dataset.ChartArrays;
import avpt.gr.chart_dataset.ItemWag;
import avpt.gr.chart_dataset.ListLines;
import avpt.gr.common.UtilsArmG;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrTrains {

    private final ArrayList<Train> listTrains = new ArrayList<Train>();
    // массив счетчиков для типов движения
    private final int[] cntTypesMove = {0, 0, 0, 0};
    // индексы в массиве
    private static final int index_g = 0;
    private static final int index_p = 1;
    private static final int index_pt = 2;
    private static final int index_asim = 3;
    // коды идентификаторов типов движений
    public static final int CODE_MOVE_P = 1;
    public static final int CODE_MOVE_PT = 2;
    public static final int CODE_MOVE_G = 3;
    public static final int CODE_MOVE_ASIM = 7;

    // 0x21_1
    private int numTrain = -1;
    private int locType = -1;
    private int weight = -1;
    private int bandage = 1200;
    private int stAmperage = -1;    // уставка тока
    private long tabNum = -1;
    private int cntWags = -1;           // число вагонов
    private int cntConditionWags = -1;  // число условных вагонов
    private LocalDate dateInitUsavp = null;
    private LocalDate dateMapUsavp = LocalDate.of(2000, 1, 1);
    private long coordinate_init = -1;
    private int statIsavprt = -1;
    private int cntSections = -1;        // число секций
    private int typeS = -1;             // тип состава

    // 0x56
    private long numFactoryBr = -1; // заводской номер
    private String verBr = null;
    private long numLoc = -1;       // Номер  электровоза (номер локототива)
    private int numSection = -1;    // номер секции
    private int locTypeAsoup = -1;  // Тип локомотива по таблице АСОУП
    private LocalDate dateInit = null;
    //
    private int curBlock = -1;
    private int curSecond = -1;
    private LocalTime curTimeUsavp = null;
    private LocalTime curTimeBr = null;
    private int curSpeed = -1;
//    private boolean isFirst = true; // вступительная часть поездки (скорость отсутствует)
//    private int curSpeedClub = -1;
    private boolean isManeuver = true;
    private boolean isNewTrain = false;     // можно создавать новый поезд
    // 1D_B
    private int routeId = -1;               // идентификатор маршрута
    private String routeName;               // название маршрута

    private int coordinate = -1;

    /**
     * @return текущая дата-время
     */
    private LocalDateTime getCurDateTime() {
        LocalDate date = null;
        if (dateInitUsavp != null) date = dateInitUsavp;
        else if (dateInit != null) date = dateInit;
        LocalTime time = null;
        if (curTimeBr != null) time = curTimeBr;
        else if (curTimeUsavp != null) time = curTimeUsavp;
        LocalDateTime dateTime = null;
        if (date != null && time != null)
            dateTime = LocalDateTime.of(date, time);
        return dateTime;
    }

    /**
     * @param time - текущее время
     * @return текущие дата-время
     */
    private LocalDateTime getCurDateTime(LocalTime time) {
        LocalDate date = null;
        if (dateInitUsavp != null) date = dateInitUsavp;
        else if (dateInit != null) date = dateInit;
        LocalDateTime dateTime = null;
        if (date != null && time != null)
            dateTime = LocalDateTime.of(date, time);
        return dateTime;
    }

    /**
     * присвоение данных из посылки инициализации бр
     * @param train - поезд
     */
    private void setInitBr(Train train) {
        train.setNumFactoryBr(numFactoryBr);
        train.setVerBr(verBr);
        train.setNumLoc(numLoc);
        train.setNumSection(numSection);
        train.setLocTypeAsoup(locTypeAsoup);
        train.setDateInit(dateInit);
    }

    /**
     * присвоение данных из посылки инициализации усавп
     * @param train - поезд
     */
    private void setInitUsavp(Train train) {
        train.setNumTrain(numTrain);
        train.setTypeLoc(locType);
        train.setWeightTrain(weight);
        train.setdBandage(bandage);
        train.setStAmperage(stAmperage);
        train.setNumTab(tabNum);
        train.setCntWags(cntWags);
        train.setCntConditionWags(cntConditionWags);
        train.setDateInit(dateInitUsavp);
        train.setDateMap(dateMapUsavp);
        train.setCoordinate(coordinate_init);
        train.setStIsavprt(statIsavprt);
        train.setCntSection(cntSections);
        train.setTypeS(typeS);
    }

    /**
     * присвоение маршрута
     * @param train - поезд
     */
    private void setRoute(Train train) {
        train.setRouteId(routeId);
        train.setRouteName(routeName);
    }

//    public void addClubU(Block32_21_E block32_21_E) {
//        this.curSpeedClub = block32_21_E.getSpeed();
//    }

    /**
     * посекундная усавп
     * @param block32_21_4 -
     */
    public void addMainUsavp(Block32_21_4 block32_21_4) {
       // this.isManeuver = false;//block32_21_4.getManeuver() == 1;
        this.curSpeed = block32_21_4.getSpeed();
        this.curTimeUsavp = UtilsArmG.getTime(block32_21_4.getSecBeginDay());
    }

    /**
     * посекундная АСИМ
     * @param block32_c2_0 -
     */
    public void addMainAsim(Block32_C2_0 block32_c2_0) {
        this.curSpeed = block32_c2_0.getSpeed();
        this.curTimeUsavp = block32_c2_0.getTime();
    }

    /**
     * посекундная посылка бр (id посылки зависит от формата на конкретный тип локомотива)
     * @param curBlock - текущий блок
     * @param curSecond - текущая секунда
     * @param curTimeBr - время бр
     */
    public boolean addCurBr(int curBlock, int curSecond, LocalTime curTimeBr) {
        this.curBlock = curBlock;
        this.curSecond = curSecond;
        this.curTimeBr = curTimeBr;
        boolean result = false;

        if (isNewTrain /*&& (curSpeed > 0 || isManeuver)*/ /*|| curSpeedClub > 1)*/ /*&& !isManeuver*/) {

            long second_prev = 0; // секунда начала предыдущего поезда (последнего на данный момент в listTrains)
            if (!listTrains.isEmpty()) {
                second_prev = listTrains.get(listTrains.size() - 1).getSecondStart();
            }
            long seconds = curSecond - second_prev;
            // если последняя поездка меньше 120 секунд, удаляем
            if (!listTrains.isEmpty() && seconds < 120 ) {
                listTrains.remove(listTrains.size() - 1);
            }

            Train train = new Train(curBlock);
            train.setSecondStart(curSecond);
            train.setDateTimeStart(getCurDateTime());
            setInitBr(train);
            setInitUsavp(train);
            setRoute(train);

            long num_prev = -1;
            long num_route = -1;
            if (!listTrains.isEmpty()) {
                num_prev = listTrains.get(listTrains.size() - 1).getNumTrain();
                num_route  = listTrains.get(listTrains.size() - 1).getRouteId();
            }
            // добавляем поезд если предыдущий и текущий отличаются по номеру или по маршруту
            if (num_prev != train.getNumTrain() || num_route != train.getRouteId()) {
                result = listTrains.add(train);
            }
            isManeuver = false;
            isNewTrain = false;
        }
        return result;
    }

    public void addInitASIM(Block32_C0_0 block32_c0_0, int coordinate) {
        int numTrain_cur = block32_c0_0.getNumTrain();
        int locType_cur = block32_c0_0.getTypeLoc();
        long tabNum_cur = block32_c0_0.getTabNum();
        coordinate_init = coordinate;
        if (numTrain_cur != numTrain || locType_cur != locType || tabNum_cur != tabNum) isNewTrain = true;

        numTrain = block32_c0_0.getNumTrain();
        locType = block32_c0_0.getTypeLoc();
        numLoc = block32_c0_0.getLocNum();
        weight = block32_c0_0.getWeight();
//        bandage = block32_c0_0.getBandage();
//        stAmperage = block32_21_1.getStAmperage();
        tabNum = block32_c0_0.getTabNum();
        cntWags = block32_c0_0.getNumWags();
//        cntConditionWags = block32_c0_0.getNumConditionalWags();
        if (block32_c0_0.getDate().getYear() > 2005)
            dateInitUsavp = block32_c0_0.getDate();
        else
            dateInitUsavp = null;

//        if (block32_c0_0.getDateMap().getYear() > 2000)
//            dateMapUsavp = block32_21_1.getDateMap();
//        else
//            dateMapUsavp = null;

//        coordinate_init = block32_c0_0.getCoordinate();
//        statIsavprt = block32_21_1.getStatusIsavprt();
//        cntSections = block32_21_1.getCntSections();
//        typeS = block32_21_1.getTypeS();
    }

    /**
     * инициализация усавп
     * @param block32_21_1 -
     */
    public void addInitUsavp(Block32_21_1 block32_21_1) {
        int numTrain_cur = block32_21_1.getNumTrain();
        int locType_cur = block32_21_1.getTypeLoc();
        long tabNum_cur = block32_21_1.getTabNum();

        if (numTrain_cur != numTrain || locType_cur != locType || tabNum_cur != tabNum) isNewTrain = true;
        numTrain = block32_21_1.getNumTrain();
        locType = block32_21_1.getTypeLoc();
        weight = block32_21_1.getWeight();
        bandage = block32_21_1.getBandage();
        stAmperage = block32_21_1.getStAmperage();
        tabNum = block32_21_1.getTabNum();
        cntWags = block32_21_1.getNumWags();
        cntConditionWags = block32_21_1.getNumConditionalWags();

        LocalDate date = block32_21_1.getDate();
        if (date != null && date.getYear() > 2005)
            dateInitUsavp = date;
//        else
//            dateInitUsavp = null;

        date = block32_21_1.getDateMap();
        if (date != null && date.getYear() > 2000)
            dateMapUsavp = date;
//        else
//            dateMapUsavp = null;

        coordinate_init = block32_21_1.getCoordinate();
        statIsavprt = block32_21_1.getStatusIsavprt();
        cntSections = block32_21_1.getCntSections();
        typeS = block32_21_1.getTypeS();
    }

    /**
     * инициализация бр
     * @param block32_16_56 -
     */
    public void addInitBr(Block32_16_56 block32_16_56) {
        long numLoc_cur = block32_16_56.getLocNum();
        int numSection_cur = block32_16_56.getSectNum();
        if ((numLoc_cur != numLoc && numLoc != -1) || (numSection_cur != numSection && numSection != -1))
            isNewTrain = true;
        numFactoryBr = block32_16_56.getNumFactoryBR();
        verBr = block32_16_56.getVerBR();
        numLoc = block32_16_56.getLocNum();
        numSection = block32_16_56.getSectNum();
        locTypeAsoup = block32_16_56.getLocTypeASOUP();
        dateInit = block32_16_56.getDateInit();
    }

    /**
     * 1D_B
     * id и имя маршрута
     * @param routeId - id маршрута
     * @param routeName - имя маршрута
     */
    public void addRoute(int routeId, String routeName) {
        if (this.routeId != routeId /*&& this.routeId != -1*/) isNewTrain = true;
        this.routeId = routeId;
        this.routeName = routeName;
    }

    /**
     * @param index - индекс
     * @return - поезд
     */
    public Train get(int index) {
        if (index >= 0 && index < listTrains.size())
            return listTrains.get(index);
        else
            return null;
    }

    /**
     * @return размер списка поездов
     */
    public int size() {
        return listTrains.size();
    }

    public void complete(ArrBlock32 arrBlock32) {
        for (int i = 1; i < listTrains.size(); i++) {
            Train train_prev = listTrains.get(i - 1);
            Train train_cur = listTrains.get(i);

            int curSpeed = 0;
            int n = train_cur.getBlStart();
            //if (train_prev.getBlEnd() == -1) {
            while (n > train_prev.getBlStart() && curSpeed < 2) {
                if (arrBlock32.get(n).getId() == 0x21) {
                    int curSubId_21 = Block32.getSubId(arrBlock32.get(n).getId(), arrBlock32.get(n).getValues());
                    if (curSubId_21 == 0x04) {
                        Block32_21_4 block32_21_4 = new Block32_21_4(arrBlock32.get(n).getValues());
                        curSpeed = block32_21_4.getSpeed();
                    }
                }
                if (arrBlock32.get(n).getId() == 0xC2) {
                    int curSubId = Block32.getSubId(arrBlock32.get(n).getId(), arrBlock32.get(n).getValues());
                    if (curSubId == 0x00) {
                        Block32_C2_0 block32_c2_0 = new Block32_C2_0(arrBlock32.get(n).getValues());
                        curSpeed = block32_c2_0.getSpeed();
                    }
                }
                n--;
            }
            if (n == train_prev.getBlStart())
                train_prev.setBlEnd(train_cur.getBlStart());
            else
                train_prev.setBlEnd(n);
            //}
            train_prev.setSecondEnd(arrBlock32.get(train_prev.getBlEnd()).getSecond());
            train_prev.setDateTimeEnd(arrBlock32.get(train_prev.getBlEnd()).getDateTime());
        }
        int n = arrBlock32.size() - 1;
   //     if (listTrains.get(listTrains.size() - 1).getBlEnd() == -1) {
        if (listTrains.size() == 0) return;
        Train lastTrain = listTrains.get(listTrains.size() - 1);
        if (lastTrain.getBlEnd() == -1) {
            while (n > lastTrain.getBlStart() && (curSpeed == 0)) {
                if (arrBlock32.get(n).getId() == 0x21) {
                    int curSubId_21 = Block32.getSubId(arrBlock32.get(n).getId(), arrBlock32.get(n).getValues());
                    if (curSubId_21 == 0x04) {
                        Block32_21_4 block32_21_4 = new Block32_21_4(arrBlock32.get(n).getValues());
                        curSpeed = block32_21_4.getSpeed();
                    }
                }
                if (arrBlock32.get(n).getId() == 0xC2) {
                    int curSubId = Block32.getSubId(arrBlock32.get(n).getId(), arrBlock32.get(n).getValues());
                    if (curSubId == 0x00) {
                        Block32_C2_0 block32_c2_0 = new Block32_C2_0(arrBlock32.get(n).getValues());
                        curSpeed = block32_c2_0.getSpeed();
                    }
                }
                n--;
            }
            if (n == lastTrain.getBlStart())
                lastTrain.setBlEnd(arrBlock32.size() - 1);
            else
                lastTrain.setBlEnd(n);
        }
        lastTrain.setSecondEnd(arrBlock32.get(lastTrain.getBlEnd()).getSecond());
        lastTrain.setDateTimeEnd(arrBlock32.get(lastTrain.getBlEnd()).getDateTime());

        fillArrTrainValues(arrBlock32);
        delShort();
    }

    /**
     * удалить короткие поезда
     */
    private void delShort() {
        for (int i = listTrains.size() - 1; i >= 0; i--) {
            Train train = listTrains.get(i);
            long d = train.getDistance();
            if (d < 1000)
                train.setNumTrain(9999);
//            if (avpt.gr.train.getSecondsEnd() - avpt.gr.train.getSecondStart() < 60) {
            if (train.getSeconds() < 60 /*|| avpt.gr.train.getDistance() < 5*/) {
                listTrains.remove(train);
            }
        }
    }

    /**
     * заполняем линию поездов
     * @param lines -
     */
    public void fillArrList(ArrayList<ListLines.ItemLine> lines) {
        for (int i = 0; i < listTrains.size(); i++) {
            Train train = listTrains.get(i);
            lines.add(new ListLines.ItemLine(train.getSecondStart(), 10));
            lines.add(new ListLines.ItemLine(train.getSecondsEnd(), Double.NaN));
        }
    }

    /**
     * @param arrBlock32 - массив посылок
     * @param x - секунда
     * @return - индекс поезда
     */
    public int getIndexFromX(ArrBlock32 arrBlock32, double x) {
        int i;
        for (i = 0; i < listTrains.size(); i++) {
            Train train = listTrains.get(i);
            int start = train.getBlStart();
            int end =  train.getBlEnd();

            double x_start;
            if (start > -1 && start < arrBlock32.size())
                x_start = arrBlock32.get(start).getSecond();
            else
                continue;

            double x_end;
            if (end > -1 && end < arrBlock32.size())
                x_end = arrBlock32.get(end).getSecond();
            else
                continue;
            if (x >= x_start && x <= x_end) break;
        }
        if (i == listTrains.size()) i = -1;
        return i;
    }

    /**
     * @param x - координата
     * @return поездка Train
     */
    public Train getTrain(ArrBlock32 arrBlock32, double x) {
        int index = getIndexFromX(arrBlock32, x);
        return get(index);
    }

    /**
     * заполняем некоторые поля поездов
     * @param arrBlock32 -
     */
    private void fillArrTrainValues(ArrBlock32 arrBlock32) {
        for (int i = 0; i < size(); i++) {
            Train train = get(i);
            fillTrainValues(arrBlock32, train);
        }
    }

    /**
     * заполняем поля расхода энергии и рекуперации
     * @param lines - массив полей всех линий
     */
    public void fillEnergy(ListLines lines) {
        for (int i = 0; i < size(); i++) {
            Train train = get(i);
            train.setAct(getTrainEnergy(train, lines.getListConsumptionEn()));
            train.setAct1(getTrainEnergy(train, lines.getListConsumptionEn_s1()));
            train.setAct2(getTrainEnergy(train, lines.getListConsumptionEn_s2()));
            train.setAct3(getTrainEnergy(train, lines.getListConsumptionEn_s3()));
            train.setAct4(getTrainEnergy(train, lines.getListConsumptionEn_s4()));
            train.setRec(getTrainEnergy(train, lines.getListConsumptionEn_r()));
            train.setRec1(getTrainEnergy(train, lines.getListConsumptionEn_r_s1()));
            train.setRec2(getTrainEnergy(train, lines.getListConsumptionEn_r_s2()));
            train.setRec3(getTrainEnergy(train, lines.getListConsumptionEn_r_s3()));
            train.setRec4(getTrainEnergy(train, lines.getListConsumptionEn_r_s4()));

            long[] arrCnt = getTrainCntEnergy(train, lines.getListCntEn());
            train.setCnt_start_act(arrCnt[0]);
            //avpt.gr.train.setCnt_end_act(arrCnt[1]);
            train.setCnt_end_act(arrCnt[0] + train.getAct());

            arrCnt = getTrainCntEnergy(train, lines.getListCntEn_s1());
            train.setCnt_start_act1(arrCnt[0]);
            //avpt.gr.train.setCnt_end_act1(arrCnt[1]);
            train.setCnt_end_act1(arrCnt[0] + train.getAct1());

            arrCnt = getTrainCntEnergy(train, lines.getListCntEn_s2());
            train.setCnt_start_act2(arrCnt[0]);
            //avpt.gr.train.setCnt_end_act2(arrCnt[1]);
            train.setCnt_end_act2(arrCnt[0] + train.getAct2());

            arrCnt = getTrainCntEnergy(train, lines.getListCntEn_s3());
            train.setCnt_start_act3(arrCnt[0]);
            //avpt.gr.train.setCnt_end_act3(arrCnt[1]);
            train.setCnt_end_act3(arrCnt[0] + train.getAct3());

            arrCnt = getTrainCntEnergy(train, lines.getListCntEn_s4());
            train.setCnt_start_act4(arrCnt[0]);
            //avpt.gr.train.setCnt_end_act4(arrCnt[1]);
            train.setCnt_end_act4(arrCnt[0] + train.getAct4());
        }
    }

    /**
     *
     * @param train - поезд
     * @param list_cnt_en -
     * @return - показания счетчика энергии [0]-avpt.gr.start , [1]-end
     */
    private long[] getTrainCntEnergy(Train train, ArrayList<ListLines.ItemLine> list_cnt_en) {
        long[] result = {0, 0};
        if (list_cnt_en.size() == 1 && list_cnt_en.get(0).getSecond() < train.getSecondStart() && list_cnt_en.get(0).getSecond_last() > train.getSecondsEnd()) {
            result[0] = (long)list_cnt_en.get(0).getValue();
            result[1] = (long)list_cnt_en.get(0).getValue();
            return result;
        }
        for (int i = 0; i < list_cnt_en.size(); i++) {
            if (i > 0 && list_cnt_en.get(i).getSecond() > train.getSecondStart() && result[0] == 0)
                result[0] = (long)list_cnt_en.get(i - 1).getValue();
            if (list_cnt_en.get(i).getSecond() > train.getSecondsEnd()) {
                if (i > 0) result[1] = (long)list_cnt_en.get(i - 1).getValue();
                break;
            }
            else
                result[1] = (long)list_cnt_en.get(list_cnt_en.size() - 1).getValue();
        }
        return result;
    }

    /**
     * @param train - поезд
     * @param list_en - массив значений  энергии или рекуперации
     * @return - расход или рекуперация энергии для поезда
     */
    private long getTrainEnergy(Train train, ArrayList<ListLines.ItemLine> list_en) {
        long val_start = -1;
        long val_end = -1;
        boolean lock = false;
        long result = 0;
        for (int i = 0; i < list_en.size(); i++) {
            if (list_en.get(i).getSecond() > train.getSecondsEnd()) {
                if (i > 0) result = (long)list_en.get(i - 1).getValue();
                break;
            }
            else
                result = (long)list_en.get(list_en.size() - 1).getValue();
//            if (cur_second > avpt.gr.train.getSecondsEnd() && val_start > -1 && val_end > -1) {
//                result = val_end - val_start;
//                break;
//            }
//            if (cur_second >= avpt.gr.train.getSecondStart() && !lock) {
//                val_start = (long)item.getValue();
//                lock = true;
//            }
//            val_end = (long)item.getValue();
        }
        return result;
    }

    /**
     * заполняем некоторые поля для поезда
     * @param arrBlock32 - массив посылок
     * @param train - поезд
     */
    private void fillTrainValues(ArrBlock32 arrBlock32, Train train) {
        int distance_auto = 0;
        int distance_prompt = 0;
        long second_auto = 0;
        long second_stop = 0;
        long second_prompt = 0;
        boolean isAuto = false;
        boolean isStop = false;
        int numSlave = 0;
        final String UNKNOWN_TYPE = "Неизвестный тип";
        String nameSlave = UNKNOWN_TYPE;
        int netAddress = 0;
        String additionalChannel = "";
        int orderNum = 0;
        String positionTrain = "";

        double cnt_move = 0.0;
        double sum_speed_move = 0;
        int cnt = 0;
        double sum_speed = 0;
        double avg_speed = 0;
        double avg_speed_move = 0;
        Block32_21_4 block32_21_4_prev = null;
        Arrays.fill(cntTypesMove, 0);
        int curNumLoadSchedule = -1;        // № загруженного расписания
        boolean lockBeginTmpLim = false;
        for (int i = train.getBlStart() + 1; i <= train.getBlEnd(); i++) {

            if (arrBlock32.get(i).getId() == 0x21) {
                int curSubId_21 = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());

                if (curSubId_21 == 0x02) {
                    Block32_21_2 block32_21_2 = new Block32_21_2(arrBlock32.get(i).getValues());
                    ItemWag.add(block32_21_2);
                }

                if (curSubId_21 == 0x04) {
                    Block32_21_4 block32_21_4 = new Block32_21_4(arrBlock32.get(i).getValues());

                    isAuto = block32_21_4.getAuto() == 1;
                    cnt++;
                    int speed = block32_21_4.getSpeed();
                    isStop = speed == 0;
                    sum_speed += speed;
                    if (speed > 0) {
                        cnt_move++;
                        sum_speed_move += speed;
                    }
                    // проезд на сигнал алсн
                    int d = coordinate > 0 ? block32_21_4.getCoordinate() - coordinate : 0;
                    coordinate = block32_21_4.getCoordinate();

                    train.incCntAlsn(block32_21_4_prev, block32_21_4, d);
                    if (block32_21_4.getALSN() > 0 && block32_21_4.getALSN() <= 5) {
                        train.setAlsn(true);
                    }
                    // связь по основному каналу
                    if (block32_21_4.getFlagTract() == 1 ) train.getMainLinkVSC().incCntFirst();
                    else train.getMainLinkVSC().incCntSecond();
                    // связь по дополнительному каналу
                    if (block32_21_4.getAdditionChannel() == 1) train.getSlaveLinkVSC().incCntFirst();
                    else train.getSlaveLinkVSC().incCntSecond();
                    // маневровые работы
                    if (block32_21_4.getManeuver() == 1) train.getModeManeuver().incCntFirst();
                    else train.getModeManeuver().incCntSecond();
                    // автоведение
                    if (block32_21_4.getAuto() == 1) train.getAutoDrive().incCntFirst();
                    else train.getAutoDrive().incCntSecond();
                    // отключены выходные цепи
                    if (block32_21_4.getChainOff() == 1) train.getChainOff().incCntFirst();
                    else train.getChainOff().incCntSecond();

                    if (block32_21_4.getTestTractCorrupt() == 0) train.setTestThrust(true);   // тест тяги не пройден
                    if (block32_21_4.getTestBrakeCorrupt() == 0) train.setTestBrake(true);    // тест тормозов не пройден
                    block32_21_4_prev = block32_21_4;
                }
                if (curSubId_21 == 0x07) {
                    Block32_21_7 block32_21_7 = new Block32_21_7(arrBlock32.get(i).getValues());
                    if (curNumLoadSchedule > -1) // смена загруженного расписания
                        if (block32_21_7.getNumLoadSchedule() != curNumLoadSchedule) train.setChangeLoadSchedule(true);
                    curNumLoadSchedule = block32_21_7.getNumLoadSchedule();
                }
                // бхв
                if (curSubId_21 == 0x08) {
                    Block32_21_8 block32_21_8 = new Block32_21_8(arrBlock32.get(i).getValues());
                    train.incBhvCnt();
                }
            }
            // asim
            if (arrBlock32.get(i).getId() == 0xC2) {
                int curSubId = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (curSubId == 0x00) {
                    Block32_C2_0 block32_c2_0 = new Block32_C2_0(arrBlock32.get(i).getValues());
                    cnt++;
                    int speed = block32_c2_0.getSpeed();
                    sum_speed += speed;
                    if (speed > 0) {
                        cnt_move++;
                        sum_speed_move += speed;
                    }
                }
            }

            if (arrBlock32.get(i).getId() == 0x1D) {
                int curSubId_1D = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (curSubId_1D == 0x0D) {
                    Block32_1D_D block32_1D_D = new Block32_1D_D(arrBlock32.get(i).getValues());
                    if (numSlave == 0) numSlave = block32_1D_D.getNumSlave();
                    if (nameSlave.equals(UNKNOWN_TYPE)) nameSlave = Train.getNameTypeLoc(block32_1D_D.getTypeSlave(), train.getLocTypeAsoup());
                    if (netAddress == 0) netAddress = block32_1D_D.getAddress();
                    if (additionalChannel.isEmpty()) {
                        boolean isChanelOn = block32_1D_D.getAdditionalChenOn() == 1;
                        boolean isLinkModem = block32_1D_D.getLinkAdditional() == 1;
                        if (isChanelOn && isLinkModem)
                            additionalChannel = "включен";
                        else if (isChanelOn)
                            additionalChannel = "отсутствует";
                        else
                            additionalChannel = "отключен";
                    }
                    if (orderNum == 0) orderNum = block32_1D_D.getOrderNum();
                    if (orderNum == 1) positionTrain = "ведущий";
                    else if (orderNum > 1) positionTrain = String.format("ведомый %d", orderNum - 1);

                    // Связь по основному каналу с модемом
                    if (block32_1D_D.getLinkVEBR() == 1) train.getMainLinkModem().incCntFirst();
                    else train.getMainLinkModem().incCntSecond();
                    // Связь по дополнительному каналу с модемом
                    if (block32_1D_D.getLinkAdditional() == 1) train.getSlaveLinkModem().incCntFirst();
                    else train.getSlaveLinkModem().incCntSecond();
                    // Включение доп канала
                    if (block32_1D_D.getAdditionalChenOn() == 1) train.getSlaveIsOn().incCntFirst();
                    else train.getSlaveIsOn().incCntSecond();
                }
                if (curSubId_1D == 0x04) {
                    Block32_1D_4 block32_1D_4 = new Block32_1D_4(arrBlock32.get(i).getValues());
                    // запрет превматического торможения
                    if (block32_1D_4.getBanPT() == 1) train.getBanPT().incCntFirst();
                    else train.getBanPT().incCntSecond();
                }
            }

            if (train.getTypeLoc() == Train.S5K || train.getTypeLoc() == Train.S5K_2) {
                if (arrBlock32.get(i).getId() == 0x5B) {
                    avpt.gr.blocks32.s5k.Block32_5B block32_5B = new avpt.gr.blocks32.s5k.Block32_5B(arrBlock32.get(i).getValues());
                    long u1 = (long) (block32_5B.getRmsVoltage_s1() * ChartArrays.getVoltageK_s5k());
                    if (u1 > train.getUMax()) train.setUMax(u1);
                    long u2 = (long) (block32_5B.getRmsVoltage_s2() * ChartArrays.getVoltageK_s5k());
                    if (u2 > train.getUMax()) train.setUMax(u2);
                }

                if (arrBlock32.get(i).getId() == 0x5D) {
                    avpt.gr.blocks32.s5k.Block32_5D block32_5D = new avpt.gr.blocks32.s5k.Block32_5D(arrBlock32.get(i).getValues());
                    long u3 = (long) (block32_5D.getRmsVoltage_s3() * ChartArrays.getVoltageK_s5k());
                    if (u3 > train.getUMax()) train.setUMax(u3);
                    long u4 = (long) (block32_5D.getRmsVoltage_s4() * ChartArrays.getVoltageK_s5k());
                    if (u4 > train.getUMax()) train.setUMax(u4);
                }
            }

            int prevCoordinate = arrBlock32.get(i - 1).getCoordinate();
            int curCoordinate = arrBlock32.get(i).getCoordinate();
            int d = curCoordinate - prevCoordinate;
            if (d > 0 && d < 100) {
                if (isAuto) distance_auto += d;
                else distance_prompt +=d;
            }
            int prevSecond = arrBlock32.get(i - 1).getSecond();
            int curSecond = arrBlock32.get(i).getSecond();
            int ss = curSecond - prevSecond;
            if (isAuto) second_auto += ss;
            if (isStop) second_stop += ss;
            double valLimTmp = arrBlock32.get(i).getValLimTmp();
            if (!Double.isNaN(valLimTmp)) {
                if (!lockBeginTmpLim) {
                    lockBeginTmpLim = true;
                    train.getLimits_v().add(new Train.Limit_V(curSecond, curCoordinate, valLimTmp));
                }
            }
            else {
                if (lockBeginTmpLim) {
                    train.getLimits_v().get(train.getLimits_v().size() - 1).setSecond_end(curSecond);
                    train.getLimits_v().get(train.getLimits_v().size() - 1).setCoordinate_end(curCoordinate);
                }
                lockBeginTmpLim = false;
            }

            int id = arrBlock32.get(i).getId();
            if ((id >= 0x10 && id <= 0x1F) || (id >= 0x50 && id <= 0x5F)) cntTypesMove[index_g]++;
            else if ((id >= 0x22 && id <= 0x2F) || (id >= 0x60 && id <= 0x6F) || (id >= 0x90 && id <= 0x9F)) cntTypesMove[index_p]++;
            else if (id >= 0xF0 && id <= 0xFF) cntTypesMove[index_pt]++;
            else if (id >= 0xC0 && id <= 0xC7) cntTypesMove[index_asim]++;
        }
        if (lockBeginTmpLim) {
            train.getLimits_v().get(train.getLimits_v().size() - 1).setSecond_end(train.getSecondsEnd());
            train.getLimits_v().get(train.getLimits_v().size() - 1).setCoordinate_end(coordinate);
        }
        train.setTypeMove(getTypeMove());
        train.setAsim(train.getTypeMove() == 7);

        long seconds = Duration.between(train.getDateTimeStart(), train.getDateTimeEnd()).getSeconds();
        second_prompt = seconds - second_auto;
      //  if (second_prompt < 0) second_prompt = 0;
        int distance = distance_auto + distance_prompt;
        if (cnt > 0) avg_speed = sum_speed / cnt;
        if (cnt_move > 0) avg_speed_move = sum_speed_move / cnt_move;
    //    if (avpt.gr.train.getCntWags() == ItemWag.getWags().size())
        train.setWags(ItemWag.getWags());
        train.setDistance(distance);
        train.setDistance_auto(distance_auto);
        train.setDistance_prompt(distance_prompt);
        train.setSeconds(seconds);
        train.setSeconds_auto(second_auto);
        train.setSeconds_stop(second_stop);
        train.setSeconds_prompt(second_prompt);
        train.setDuration_time(UtilsArmG.getDurationTime(seconds));
        train.setDuration_time_auto(UtilsArmG.getDurationTime(second_auto));
        train.setDuration_time_prompt(UtilsArmG.getDurationTime(second_prompt));
        // показываем если statusIsavprt = 1 или 4
        train.setNumSlave(numSlave);
        train.setNameSlave(nameSlave);
        train.setNetAddress(netAddress);
        train.setAdditionalChannel(additionalChannel);

        train.setPositionTrain(positionTrain);
        train.setCodePositionTrain(orderNum);

        train.setAvgSpeed(avg_speed);
        train.setAvgSpeedMove(avg_speed_move);

//        List<ItemWag> wags = avpt.gr.train.getWags();
//        System.out.println(avpt.gr.train.getNumTrain() + " " + avpt.gr.train.getDateTimeStart());
//        for (ItemWag item : wags) {
//            System.out.println(item.getNum() + " " + item.getType() + " " + item.getWeight());
//        }
    }

    /**
     * TypeMove_G = 0;          4
     * TypeMove_P = 1;          1
     * TypeMove_PT = 2;         2
     * TypeMove_ASIM = 3;       7
     * @return тип движения
     */
    private int getTypeMove() {
        int index = UtilsArmG.getIndexOfLargest(cntTypesMove);
        switch (index) {
            case index_g: return CODE_MOVE_G;
            case index_p: return CODE_MOVE_P;
            case index_pt: return CODE_MOVE_PT;
            case index_asim: return CODE_MOVE_ASIM;
        }
        return -1;
    }

    public void add(Train train) {
        listTrains.add(train);
    }

    /**
     * Запись в файл бинарной информации о поездках, согласно протоколу для нбд2
     * @param outFilename - имя выходного файла
     */
    public void writeInfo(String outFilename) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outFilename);
        outputStream.write(UtilsArmG.getBytes(2, 2));                  // версия всегда 2
        outputStream.write(UtilsArmG.getBytes(2, size()));                 // количество поездок в файле
        int index = 0;
        for (Train train : listTrains) {
            outputStream.write(UtilsArmG.getBytes(2, train.getNumTrain()));// номер поезда
            outputStream.write(UtilsArmG.getBytes(4, train.getNumTab()));  // табельный номер
            outputStream.write(UtilsArmG.getBytes(2, index));              // индекс

            int cntSect = 0;

            outputStream.write(UtilsArmG.getBytes(1, cntSect));            // всего секций
            for (int i = 0; i < 4; i++) {
//                if (avpt.gr.train.getAreaFuel(i) != null) {
                    outputStream.write(UtilsArmG.getBytes(4, train.getLocTypeAsoup()));   // серия локомотива
                    outputStream.write(UtilsArmG.getBytes(4, train.getNumLoc()));   // номер локомотива
                    outputStream.write(UtilsArmG.getBytes(1, i + 1));              // номер секции
                    outputStream.write(UtilsArmG.getBytes(1, 0));              //
 //               }
            }
            // date avpt.gr.start
            LocalDateTime dateTime = train.getDateTimeStart();
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getSecond()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getMinute()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getHour()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getDayOfMonth()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getMonthValue()));
            outputStream.write(UtilsArmG.getBytes(2, dateTime.getYear()));
            // date end
            dateTime = train.getDateTimeEnd();
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getSecond()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getMinute()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getHour()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getDayOfMonth()));
            outputStream.write(UtilsArmG.getBytes(1, dateTime.getMonthValue()));
            outputStream.write(UtilsArmG.getBytes(2, dateTime.getYear()));
            // широта долгота
            outputStream.write(UtilsArmG.getBytes(4, 0 /*avpt.gr.train.getLatStart()*/));    // широта начало
            outputStream.write(UtilsArmG.getBytes(4, 0 /*avpt.gr.train.getLonStart()*/));    // долгота начало
            outputStream.write(UtilsArmG.getBytes(4, 0/*avpt.gr.train.getLatEnd()*/));    // широта конец
            outputStream.write(UtilsArmG.getBytes(4, 0 /*avpt.gr.train.getLonEnd()*/));    // долгота конец
            // карты нет
            outputStream.write(UtilsArmG.getBytes(4, 0));
            outputStream.write(UtilsArmG.getBytes(4, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));  // наличие карты
            // дата карты
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(1, 0));
            outputStream.write(UtilsArmG.getBytes(2, 0));

            index++;
        }
    }
}
