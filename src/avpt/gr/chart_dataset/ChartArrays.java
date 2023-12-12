package avpt.gr.chart_dataset;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.asim.*;
import avpt.gr.blocks32.overall.*;
import avpt.gr.common.UtilsArmG;
import avpt.gr.maps.Profiles;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import java.util.ArrayList;

import static avpt.gr.blocks32.SubIdGr.getSubId;
import static avpt.gr.train.Train.*;

/**
 * заполняем все поля линий, сигналов и задач;
 * fill() - итерация по ArrBlock32, на каждый шаг вызываем doStep();
 * в завершении вызыаем finalStep() - заполняем финальные значения;
 */
public class ChartArrays {
    private ArrBlock32 arrBlock32;
    private ArrTrains arrTrains;
    private ListLines listLines;
    private ListSignals listSignals;
    private ListTasks listTasks;
    private boolean isTime; // если true - развертка по времени, иначе по координите
    private static String NOT_DEF = "не определен";

    public static class RailCoordinate {
        private int km;
        private int pk;

        public RailCoordinate(int km, int pk) {
            this.km = km;
            this.pk = pk;
        }

        public int getKm() {
            return km;
        }

        public int getPk() {
            return pk;
        }
    }

    public static class GpsCoordinate {
        private double lat;
        private double lon;

        public GpsCoordinate(double lon, double lat) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

    }

    private final ArrayList<GpsCoordinate> listGpsCoordinate = new ArrayList<GpsCoordinate>();
    private final ArrayList<RailCoordinate> listRailCoordinate = new ArrayList<RailCoordinate>();
    private final ArrayList<LocalDateTime> listDateTimes = new ArrayList<LocalDateTime>();
    private final ArrayList<Integer> listBlockBySeconds = new ArrayList<Integer>();
//    private final ArrayList<Double> listSpeedLimTmp = new ArrayList<Double>();

//    посекундный список объектов и станций
    private int[] arrObjects;
    private int[] arrProfiles;
    private int[] arrLimits;
    private int indexStation;
    private int indexTrafficLight;
    private int indexThermometer;
    private int indexNeutralInsert;
    private int indexCrossing;
    private int indexCheckBrake;
    private int indexLandslide;
    private int indexLimitMap;
    private int indexProfile;

    private ArrayList<int[]> listObjects = new ArrayList<int[]>();
    private ArrayList<int[]> listProfiles = new ArrayList<int[]>();
    private ArrayList<int[]> listLimits = new ArrayList<int[]>();

    private int curTypeLoc;
    private int curTypeLoc_SAVP;
    private int curTypeMove;
    private int second_coordinate = 0;     // счетчик секунд
    private LocalDate initDate_26; // дата инициализации из посылки 0x26
    private LocalDateTime curDateTime_21;  // дата время из посылки 0x21-1
    private LocalTime curTime;  // время: для каждого типа локомотива своя посылка
    private LocalTime curTime_21; // время из посылки 0x21-4

    public static double LINE_MAP = 1.0;    // координата y для линии под станцией

    // 1D_4 ------------------------------------------------------------------------------------------------------------
    private int km = -1;
    private int pk = -1;
    // -----------------------------------------------------------------------------------------------------------------
    private double lon;// широта
    private double lat;// долгота
    // 21_4 ------------------------------------------------------------------------------------------------------------
    private int coordinate = -1;
    private int cur_coordinate = 0; // текущая координата, отсчет начинается с 0 и до конца файлов поездок
    private int cur_mode_auto = -1;
    // 21_1 ------------------------------------------------------------------------------------------------------------
    private int numConditionWags = -1;
    private int numSections = -1;
    // 21_7 ------------------------------------------------------------------------------------------------------------
    private int curNumAcceptSchedule = -1;  // №  последнего принятого расписания
    private int curNumAcceptValidSchedule = -1; // №  последнего принятого и верного расписания
    private int curNumLoadSchedule = -1;        // № загруженного расписания
    // 3ЭС5К -----------------------------------------------------------------------------------------------------------
    private static double voltageK_s5k = 0.001;                // множитель для напряжения контактной сети 3ЭС5К, 2ЭС5К посылка 0x57
    private static double amperageK_s5k = 0.00001;             // множитель для общего тока 3ЭС5К, 2ЭС5К посылка 0x57
    private int power_brake_s5k = 0;                    // сила торможения 3ЭС5К, 2ЭС5К посылка 0x5C
    //------------------------------------------------------------------------------------------------------------------
    private double voltageK_s4k = 0.0001;                // множитель для напряжения контактной сети 3ЭС5К, 2ЭС5К посылка 0x57

    private double tempLimit;
    private Profiles profiles;
    private int len_prof = 0;
    private int slope_prof = 0;
    // предшествующяя посылка энергии
    private avpt.gr.blocks32.s5k.Block32_57 block32_57_s5k_prev = null;
    private avpt.gr.blocks32.s5k.Block32_58 block32_58_s5k_prev = null;
    private avpt.gr.blocks32.s5k.Block32_59 block32_59_s5k_prev = null;
    private avpt.gr.blocks32.s5k.Block32_5A block32_5A_s5k_prev = null;

    private avpt.gr.blocks32.s4k.Block32_57 block32_57_s4k_prev = null;
    private avpt.gr.blocks32.s4k.Block32_58 block32_58_s4k_prev = null;
    private avpt.gr.blocks32.s4k.Block32_59 block32_59_s4k_prev = null;
    private avpt.gr.blocks32.s4k.Block32_5A block32_5A_s4k_prev = null;

    private avpt.gr.blocks32.s5k_2.Block32_57 block32_57_s5k_2_prev = null;
    private avpt.gr.blocks32.s5k_2.Block32_58 block32_58_s5k_2_prev = null;
    private avpt.gr.blocks32.s5k_2.Block32_59 block32_59_s5k_2_prev = null;
    private avpt.gr.blocks32.s5k_2.Block32_5A block32_5A_s5k_2_prev = null;

    private avpt.gr.blocks32.vl10.Block32_13 block32_13_prev = null;
    private avpt.gr.blocks32.vl80.Block32_59 block32_59_vl80_prev = null;
    private avpt.gr.blocks32.vl80.Block32_5A block32_5A_vl80_prev = null;

    private avpt.gr.blocks32.s5.Block32_57 block32_57_s5_prev = null;
    private avpt.gr.blocks32.s5.Block32_58 block32_58_s5_prev = null;
    private avpt.gr.blocks32.s5.Block32_59 block32_59_s5_prev = null;
    private avpt.gr.blocks32.s5.Block32_5A block32_5A_s5_prev = null;

    private avpt.gr.blocks32.asim.Block32_C3_0 block32_C3_0_prev = null;
    private avpt.gr.blocks32.asim.Block32_C3_1 block32_C3_1_prev = null;
    private avpt.gr.blocks32.asim.Block32_C3_2 block32_C3_2_prev = null;
    private avpt.gr.blocks32.asim.Block32_C3_3 block32_C3_3_prev = null;

    private int acdc_ui = -1; // постоянка переменка для асим и эп20

    private final int second_21_9_prev = -1;                          // предыдущая секунда
    private double profile = 0;
    private double min_prof = 2;
//    private final double slope_prof = 0;
//    private final long len_prof = 0;

    public ChartArrays(ArrBlock32 arrBlock32, ArrTrains arrTrains, boolean isTime) {
        profiles = new Profiles(arrBlock32);
        this.arrBlock32 = arrBlock32;
        this.arrTrains = arrTrains;
        this.isTime = isTime;
        listLines = new ListLines();
        listTasks = new ListTasks();
        listSignals = new ListSignals();
    }

    public void fill(int nBlStart, int nBlFinish) {
        curTypeMove = arrBlock32.getFirstTypeMove(nBlStart);
        curTypeLoc = arrBlock32.getFirstTypeTrain(nBlStart);
        if (curTypeLoc == -1) {
            curTypeLoc = S5K_2;
        }

        for (int i = nBlStart; i <= nBlFinish; i++) {
            doStep(i);
//            if (arrTrains.size() != 0)
//                indexTrainPrev = arrTrains.size();
        }
        finalStep();
    }

    public void doStep(int index) {
        avpt.gr.blocks32.overall.Block32_16_56 block32_16_56;
        switch (arrBlock32.get(index).getId()) {
            case 0x61:
            case 0x21: {
                    int curSubId_21 = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                    switch (curSubId_21) {
                        case 0x01:
                            Block32_21_1 block32_21_1 = new Block32_21_1(arrBlock32.get(index).getValues());
                            arrTrains.addInitUsavp(block32_21_1);
                            curTypeLoc = block32_21_1.getTypeLoc();
                            curDateTime_21 = block32_21_1.getDateTime();
                            numConditionWags = block32_21_1.getNumConditionalWags();
                            numSections = block32_21_1.getCntSections();
                            break;
//                    case 0x02:
//                        Block32_21_2 block32_21_2 = new Block32_21_2(arrBlock32.get(index).getValues());
//                        ItemWag.add(block32_21_2); // добавляем информацию о вагоне
//                        break;
                        case 0x03:
                            Block32_21_3 block32_21_3 = new Block32_21_3(arrBlock32.get(index).getValues());
                            ItemLimit.add(block32_21_3); // добавляем вр огр в актуальный на данный момент список
                            break;
                        case 0x04:
                            int max_second_21_4 = isTime ? 2 : -1;
                            Block32_21_4 block32_21_4 = new Block32_21_4(arrBlock32.get(index).getValues());
                            arrTrains.addMainUsavp(block32_21_4);

                            int d = coordinate > 0 ? block32_21_4.getCoordinate() - coordinate : 0;
                            if (coordinate > -1 && d > 0 && d < 100)
                                cur_coordinate += d;//block32_21_4.getCoordinate() - coordinate;
                            coordinate = block32_21_4.getCoordinate();

                            tempLimit = ItemLimit.getTempLimit(coordinate, listLines.getListSpeedLimitTemp());
                            listLines.add(listLines.getListSpeedLimitTemp(), second_coordinate, tempLimit, max_second_21_4);
                            listLines.add(listLines.getListCurSpeedLimit(), second_coordinate, block32_21_4.getCurSpeedLimit(), -1);
                            listLines.add(listLines.getListSpeed_USAVP(), second_coordinate, block32_21_4.getSpeed(), max_second_21_4);
                            listLines.add(listLines.getListSpeed_ERG(), second_coordinate, block32_21_4.getSpeedERG(), max_second_21_4);
                         //   listLines.add_lim_map(listLines.getListSpeedMax(), second_coordinate, Block32_21_4.getMaxLimit());
                            listLines.add(listLines.getListPressTC_USAVP(), second_coordinate, block32_21_4.getPressTC(), max_second_21_4);
                            listLines.add(listLines.getListPressTM_USAVP(), second_coordinate, block32_21_4.getPressTM(), max_second_21_4);
                            listLines.add(listLines.getListPressUR_USAVP(), second_coordinate, block32_21_4.getPressUR(), max_second_21_4);

                            if (curTypeLoc != S5 && curTypeLoc != S6 && curTypeLoc != KZ8A) {
                                int pos = curTypeLoc == S5K ? block32_21_4.getPosition() * 25 : block32_21_4.getPosition();
                                if (curTypeLoc == S5K)
                                    listLines.add(listLines.getListPositionS5k(), second_coordinate, pos, max_second_21_4);
                                else
                                    listLines.add(listLines.getListPosition(), second_coordinate, pos, max_second_21_4);

                                listLines.add(listLines.getListWeakField(), second_coordinate, block32_21_4.getWeakField(), max_second_21_4);
                            }
                            listSignals.add(listSignals.getListBanThrust(), second_coordinate, block32_21_4.getBanTract() == 1);
                            listSignals.add(listSignals.getListBanBrake(), second_coordinate, block32_21_4.getBanBrake() == 1);
                            listSignals.add(listSignals.getListEmulate(), second_coordinate, block32_21_4.getEmulate() == 1);
                            listSignals.add(listSignals.getListEPK(), second_coordinate, block32_21_4.getEPK() == 1);
                            listSignals.add(listSignals.getListChainOff(), second_coordinate, block32_21_4.getChainOff() == 1);
                            listSignals.add(listSignals.getListTestBrakeCorrupt(), second_coordinate, block32_21_4.getTestBrakeCorrupt() == 1);
                            listSignals.add(listSignals.getListTestTractCorrupt(), second_coordinate, block32_21_4.getTestTractCorrupt() == 1);
                            listSignals.add(listSignals.getListManeuver(), second_coordinate, block32_21_4.getManeuver() == 1);
                            listSignals.add(listSignals.getListMainChannel(), second_coordinate, block32_21_4.getFlagTract() == 1);
                            listSignals.add(listSignals.getListAdditionChannel(), second_coordinate, block32_21_4.getAdditionChannel() == 1);

                            listTasks.add(listTasks.getListALSN_USAVP(), second_coordinate, block32_21_4.getALSN(), curTypeLoc, max_second_21_4);
                            listTasks.add(listTasks.getListPneumaticUsavp(), second_coordinate, block32_21_4.getPneumaticState(), curTypeLoc, max_second_21_4);
                            cur_mode_auto = block32_21_4.getModeAuto();
                            listTasks.add(listTasks.getListAuto(), second_coordinate, cur_mode_auto, curTypeLoc, max_second_21_4);
                            if (curTypeLoc == S5 | curTypeLoc == KZ8A)
                                listLines.add(listLines.getListPowerTaskAuto(), second_coordinate, block32_21_4.getPowerTaskAuto(), -1); //
                            curTime_21 = UtilsArmG.getTime(block32_21_4.getSecBeginDay());
                            break;
                        case 0x07:
                            final int MAX_SECOND_21_7 = 1;
                            Block32_21_7 block32_21_7 = new Block32_21_7(arrBlock32.get(index).getValues());
                            listLines.add(listLines.getListLevelGSM(), second_coordinate, block32_21_7.getLevelGSM(), MAX_SECOND_21_7);

                            listSignals.add(listSignals.getListReady(), second_coordinate, block32_21_7.getReady() == 1);
                            listSignals.add(listSignals.getListCan(), second_coordinate, block32_21_7.getCAN2() == 1);
                            listSignals.add(listSignals.getListError(), second_coordinate, block32_21_7.getErrModem() > 0);

                            // смена №  последнего принятого расписания
                            if (curNumAcceptSchedule > -1)
                                listSignals.add(listSignals.getListChangeAcceptedSchedule(), second_coordinate,
                                        block32_21_7.getNumLastAcceptSchedule() != curNumAcceptSchedule);
                            curNumAcceptSchedule = block32_21_7.getNumLastAcceptSchedule();
                            // смена №  последнего принятого и верного расписания
                            if (curNumAcceptValidSchedule > -1)
                                listSignals.add(listSignals.getListChangeAcceptedValidSchedule(), second_coordinate,
                                        block32_21_7.getNumLastAcceptValidSchedule() != curNumAcceptValidSchedule);
                            curNumAcceptValidSchedule = block32_21_7.getNumLastAcceptValidSchedule();
                            // смена № загруженного расписания
                            if (curNumLoadSchedule > -1)
                                listSignals.add(listSignals.getListChangeLoadSchedule(), second_coordinate,
                                        block32_21_7.getNumLoadSchedule() != curNumLoadSchedule);
                            curNumLoadSchedule = block32_21_7.getNumLoadSchedule();
                            // источник расписания - база данных
                            listSignals.add(listSignals.getListSourceSchedule(), second_coordinate, block32_21_7.getSourceSchedule() == 1);
                            //  режим работы бмс
                            listSignals.add(listSignals.getListModeWork(), second_coordinate, block32_21_7.getModeWork() == 1);
                            // уровень GPRS
                            listSignals.add(listSignals.getListLevelGPRS(), second_coordinate, block32_21_7.getLevelGSM() > 20);
                            // Обновление ПО КАУД
                            int last_com = block32_21_7.getLastCommandBMS();
                            listSignals.add(listSignals.getListUpdate(), second_coordinate, last_com == 6 || last_com == 9);
                            // Скачивание д. с ЕСМ БС
                            listSignals.add(listSignals.getListEsmBs(), second_coordinate, last_com == 10);
                            // связь с сервером
                            int err_modem = block32_21_7.getErrModem();
                            listSignals.add(listSignals.getListLinkServer(), second_coordinate, err_modem != 3);
                            // связь со шлюзом РОРС
                            listSignals.add(listSignals.getListLinkGateway(), second_coordinate, err_modem != 7);
                            // есть GPRS услуга
                            listSignals.add(listSignals.getListGPRS(), second_coordinate, err_modem != 13);
                            // Автовед. по расп. СИМ
                            listSignals.add(listSignals.getListAutoSchedule(), second_coordinate,
                                    block32_21_7.getNumLoadSchedule() > 0 &&
                                            (cur_mode_auto != TaskAutoDrive.MANUAL && cur_mode_auto != TaskAutoDrive.CHAIN_OFF));
                            break;
                        case 0x08:
                            Block32_21_8 block32_21_8 = new Block32_21_8(arrBlock32.get(index).getValues());
                            listTasks.add(listTasks.getListBHV_valve(), second_coordinate, block32_21_8.getStateValve(), curTypeLoc, -1);
                            listTasks.add(listTasks.getListBHV_command(), second_coordinate, block32_21_8.getCommandBrake(), curTypeLoc, -1);
                            listTasks.add(listTasks.getListBHV_voltage(), second_coordinate, block32_21_8.getVoltageBHV(), curTypeLoc, -1);

                            listLines.add(listLines.getListVoltageBhvAkbGet(), second_coordinate, block32_21_8.getVoltageGet(), -1);
                            listLines.add(listLines.getListVoltageBhvAkbSend(), second_coordinate, block32_21_8.getVoltageSend(), -1);

                            listLines.add(listLines.getListPressTmTail(), second_coordinate, block32_21_8.getPressTmTail(), -1);
                            listLines.add(listLines.getListPressWorkCameraBhv(), second_coordinate, block32_21_8.getPressWorkCam(), -1);

                            listSignals.add(listSignals.getListBHV_BrakeTail(), second_coordinate, block32_21_8.getBrakeTail() == 1);
                            listSignals.add(listSignals.getListBHV_IsSlaveChan(), second_coordinate, block32_21_8.getIsSaveChan() == 1);
                            listSignals.add(listSignals.getListBHV_LinkSlaveChan(), second_coordinate, block32_21_8.getLinkSlaveCh() == 1);
                            listSignals.add(listSignals.getListBHV_LinkMainChan(), second_coordinate, block32_21_8.getLinkMainCh() == 1);
                            listSignals.add(listSignals.getListBHV_IsCommand(), second_coordinate, block32_21_8.getIsCommand() == 1);
                            listSignals.add(listSignals.getListBHV_FanEmergencyBrake(), second_coordinate, block32_21_8.getFanEmergencyBrake() == 1);
                            listSignals.add(listSignals.getListBHV_FanBrake(), second_coordinate, block32_21_8.getFanBrake() == 1);
                            listSignals.add(listSignals.getListBHV_SensorPressRK(), second_coordinate, block32_21_8.getSensorPressRK() == 1);
                            listSignals.add(listSignals.getListBHV_SensorPressTM(), second_coordinate, block32_21_8.getSensorPressTM() == 1);
                            listSignals.add(listSignals.getListBHV_UpdateParam(), second_coordinate, block32_21_8.getUpdateParam() == 1);
                            listSignals.add(listSignals.getListBHV_DebugOtp(), second_coordinate, block32_21_8.getDebugOtp() == 1);
                            listSignals.add(listSignals.getListBHV_Ready(), second_coordinate, block32_21_8.getBhvReady() == 1);
                            listSignals.add(listSignals.getListBHV_Other(), second_coordinate, block32_21_8.getBhvOther() == 1);
                            listSignals.add(listSignals.getListBHV_InterventionRadio(), second_coordinate, block32_21_8.getInterventionRadio() == 1);
                            listSignals.add(listSignals.getListBHV_CrashAfuMost(), second_coordinate, block32_21_8.getCrashAfuMost() == 1);
                            listSignals.add(listSignals.getListBHV_IsBhv(), second_coordinate, block32_21_8.getIsBhv() == 1);
                            listSignals.add(listSignals.getListBHV_AllowAnswer(), second_coordinate, block32_21_8.getAllowAnswer() == 1);
                            break;
                        case 0x09:
 //                           final int MAX_SECOND_21_9 = 1;
                            Block32_21_9 block32_21_9 = new Block32_21_9(arrBlock32.get(index).getValues());
                            int mapLimit = block32_21_9.getLimSpeed();
                            if (mapLimit > 0)
                                listLines.add_lim_map(listLines.getListSpeedLimitConst(), second_coordinate, mapLimit);

                            // профиль
//                        if (!Double.isNaN(block32_21_9.getSlope())) {

//                            if (block32_21_9_prev != null) {
//                                len_prof = block32_21_9.getCoordinate() - block32_21_9_prev.getCoordinate();
//                                if (len_prof > 0 && len_prof < 20000)
//                                    profile = profile + (len_prof) * block32_21_9_prev.getSlope() / 1000.0;
//                            }
//                            second_21_9_prev = second;
//                            block32_21_9_prev = block32_21_9;
//                            min_prof = Math.min(profile, min_prof);
//                        }
                            listLines.add(listLines.getListProfile(), second_coordinate, profile, -1);
                            int[] arrId = block32_21_9.getArrId();
                            /*if (arrProfiles == null)*/
                            arrProfiles = new int[2];
                            /*if (arrLimits == null)*/
                            arrLimits = new int[2];
                            for (int j : arrId) {

//                            if (arrProfiles == null) {
                                if (j == 1 && arrProfiles[0] == 0) {
                                    arrProfiles[0] = j;
                                    arrProfiles[1] = indexProfile++;
                                }
                                //                           }

//                            if (arrLimits == null) {
                                if (j == 3 && arrLimits[0] == 0) {
                                    arrLimits[0] = j;
                                    arrLimits[1] = indexLimitMap++;
                                    //                               }
                                }

                                if (arrObjects == null) {
                                    switch (j) {
                                        case 7: // светофор
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexTrafficLight++;
                                            break;
                                        case 10: // ПОНАБ
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexThermometer++;
                                            break;
                                        case 4: // нейтральная вставка
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexNeutralInsert++;
                                            break;
                                        case 9: // переезд
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexCrossing++;
                                            break;
                                        case 6: // пробы тормозов
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexCheckBrake++;
                                            break;
                                        case 19: // обрывоопасное место
                                            arrObjects = new int[2];
                                            arrObjects[0] = j;
                                            arrObjects[1] = indexLandslide++;
                                            break;
                                    }
                                }
                            }
                            break;
                        case 0x0B:
                            Block32_21_B block32_21_B = new Block32_21_B(arrBlock32.get(index).getValues());
                            if (curTypeLoc == S5K || curTypeLoc == S5K_2) // сила второго пока только для ЭС5К
                                listLines.add(listLines.getListPowerLocomotiveSlave(), second_coordinate, block32_21_B.getPowerSlave(), -1);
                            if (curTypeLoc != S6)
                                listTasks.add(listTasks.getListPneumatic2(), second_coordinate, block32_21_B.getStatePTSlave(), curTypeLoc, -1);
                            break;
                        case 0x0E:
                            Block32_21_E block32_21_E = new Block32_21_E(arrBlock32.get(index).getValues());
                            listLines.add(listLines.getListSpeed_CLUB(), second_coordinate, block32_21_E.getSpeed(), -1);
                            listLines.add(listLines.getListPermissibleSpeed_CLUB(), second_coordinate, block32_21_E.getPermissibleSpeed(), -1);
                            listTasks.add(listTasks.getListALSN_CLUB(), second_coordinate, block32_21_E.getTrafficLight(), curTypeLoc, -1);
                            lat = block32_21_E.getGpsLat(); // широта
                            lon = block32_21_E.getGpsLon(); // долгота
                            break;
                    }
            }
                break;

            case 0x1D: {
                curTypeMove = ArrTrains.CODE_MOVE_G;
                int curSubId_1D = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                switch (curSubId_1D) {
                    case 0x04:
                        Block32_1D_4 block32_1D_4 = new Block32_1D_4(arrBlock32.get(index).getValues());
                        km = block32_1D_4.getKm();
                        pk = block32_1D_4.getPicket();
//                        listLines.add(listLines.getListCurSpeedLimit(), second_coordinate, block32_1D_4.getCurSpeedLimit(), -1);
                        listSignals.add(listSignals.getListBanPT(), second_coordinate, block32_1D_4.getBanPT() == 1);
                        listSignals.add(listSignals.getListBanET(), second_coordinate, block32_1D_4.getBanET() == 1);
                        listTasks.add(listTasks.getListVsc(), second_coordinate, block32_1D_4.getMoveVSC(), curTypeLoc, -1);
                        break;
                    case 0x05:
                        Block32_1D_5 block32_1D_5 = new Block32_1D_5(arrBlock32.get(index).getValues());
                        km = block32_1D_5.getKm();
                        pk = block32_1D_5.getPicket();
                        break;
                    case 0x09:
                        //Block32_1D_9 block32_1D_9 = new Block32_1D_9(arrBlock32.get(index).getValues());
                        arrObjects = new int[2]; // станция
                        arrObjects[0] = 8;
                        arrObjects[1] = indexStation++;
                        break;
                    case 0x0B:
                        Block32_1D_B block32_1D_B = new Block32_1D_B(arrBlock32.get(index).getValues());
                        String nameRoute = block32_1D_B.getNameRoute();
                        arrTrains.addRoute(block32_1D_B.getRouteId(), nameRoute.isEmpty() ? NOT_DEF : nameRoute);
                        break;
                    case 0x0C: {
                        Block32_1D_C block32_1D_C = new Block32_1D_C(arrBlock32.get(index).getValues());
                        if (curTypeLoc == S6) {// для ЭС6
                            listLines.add(listLines.getListPowerLocomotiveSlave(), second_coordinate, block32_1D_C.getPowerSlaveS6(listLines.getListPowerLocomotiveSlave()), -1);
                            listTasks.add(listTasks.getListPneumatic2(), second_coordinate, block32_1D_C.getPneumaticSlaveS6(listTasks.getListPneumatic2()), curTypeLoc, -1);
                        }
                    }   break;
                    case 0x0E:
                        final int MAX_SECOND_1D_E = 2;
                        Block32_1D_E block32_1D_E = new Block32_1D_E(arrBlock32.get(index).getValues());
                        // расстояние до хвоста
                        if (block32_1D_E.getPlaceSlave() == 1) {
                            if (coordinate > -1) {
                                double val = (block32_1D_E.getCoordinateSlave() - block32_1D_E.getLenSlave() - coordinate) * 0.001;
                                if (val > 0 && val < 15)
                                    listLines.add(listLines.getListDistanceTail(), second_coordinate, val, -1);
                            }
                        }
                        else {
                            if (numConditionWags > -1 && numSections > -1 && coordinate > -1) {
                                double val = (coordinate - numConditionWags * 14 + numSections * 17 - block32_1D_E.getCoordinateSlave()) * 0.001;
                                if (val > 0 && val < 15)
                                    listLines.add(listLines.getListDistanceTail(), second_coordinate, val, -1);
                            }
                        }
                        // скорость второго
                        listLines.add(listLines.getListSpeedSlave(), second_coordinate, block32_1D_E.getSpeedSlave(), -1);
                        break;
                }
            }
                break;

            case 0xC0: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
                int curSubId = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                if (curSubId == 0) {
                    Block32_C0_0 block32_C0_0 = new Block32_C0_0(arrBlock32.get(index).getValues());
                    curDateTime_21 = block32_C0_0.getDateTime();
                    arrTrains.addInitASIM(block32_C0_0, coordinate);
                    acdc_ui = block32_C0_0.getTypeACDC();
                    curTypeLoc = block32_C0_0.getTypeLoc();
                }
                if (curSubId == 1) {
                    Block32_C0_1 block32_C0_1 = new Block32_C0_1(arrBlock32.get(index).getValues());
                    String nameRoute = block32_C0_1.getNameRoute();
                    arrTrains.addRoute(block32_C0_1.getRouteId(), nameRoute.isEmpty() ? NOT_DEF : nameRoute);
//                    String txt = block32_C0_1.getNameRoute();
//                    System.out.println(txt);
                }
                if (curSubId == 2) {
                    Block32_C0_2 block32_C0_2 = new Block32_C0_2(arrBlock32.get(index).getValues());
//                    String txt = block32_C0_2.getNameDriver();
//                    System.out.println(txt);
                }
            }
            break;

            case 0xC2: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
                int curSubId = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                if (curSubId == 0) {
                    Block32_C2_0 block32_C2_0 = new Block32_C2_0(arrBlock32.get(index).getValues());
                    arrTrains.addMainAsim(block32_C2_0);
                    int d = (int)block32_C2_0.getCoordinate() - coordinate;
                    if (coordinate > -1 && d > 0 && d < 100)
                        cur_coordinate += block32_C2_0.getCoordinate() - coordinate;
                    coordinate = (int)block32_C2_0.getCoordinate();

                    curTime_21 = block32_C2_0.getTime();
                    addSecond(isTime);
                    boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_C2_0.getTime());
                    // сброс счетчиков энергии на начало поезда
                    if (isNewTrain) resetEnergy();
                    listBlockBySeconds.add(index);

                    listLines.add(listLines.getListSpeedFact(), second_coordinate, block32_C2_0.getSpeed(), -1);
                    listLines.add(listLines.getListSpeedCalc(), second_coordinate, block32_C2_0.getSpeedCalc(), -1);
                    listLines.add(listLines.getListSpeedGPS(), second_coordinate, block32_C2_0.getSpeedGPS(), -1);
                    listTasks.add(listTasks.getListALSN_USAVP(), second_coordinate, block32_C2_0.getALSN(), curTypeLoc, -1);

//                    listLines.add(listLines.getListPressTC(), second_coordinate, block32_C2_0.getPressTC(), -1);
//                    listLines.add(listLines.getListPressTM(), second_coordinate, block32_C2_0.getPressTM(), -1);
//                    listLines.add(listLines.getListPressUR(), second_coordinate, block32_C2_0.getPressUR(), -1);
                }
                if (curSubId == 1) {
                    Block32_C2_1 block32_C2_1 = new Block32_C2_1(arrBlock32.get(index).getValues());
                    listLines.add(listLines.getListVoltage(), second_coordinate, block32_C2_1.getVoltage(), -1);
                    listLines.add(listLines.getListAmperage(), second_coordinate, block32_C2_1.getAmperage(), -1);
                }
            }
                break;

            case 0xC3: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
                int curSubId = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                if (curSubId == 1) {
                    Block32_C3_0 block32_c3_0 = new Block32_C3_0(arrBlock32.get(index).getValues());
                    // расход энергии 1 секция
                    if (block32_C3_0_prev != null) {
                        if (acdc_ui == 1) { // постоянка
                            double val = getConsumptionEnergy(block32_C3_0_prev.getEnergyAct_s1_dc(), block32_c3_0.getEnergyAct_s1_dc(), listLines.getListConsumptionEn_s1());
                            listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_c3_0.getEnergyAct_s1_dc(), -1);
                        }
                        else if (acdc_ui == 2) {  // переменка
                            double val = getConsumptionEnergy(block32_C3_0_prev.getEnergyAct_s1_ac(), block32_c3_0.getEnergyAct_s1_ac(), listLines.getListConsumptionEn_s1());
                            listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_c3_0.getEnergyAct_s1_ac(), -1);
                            val = getConsumptionEnergy(block32_C3_0_prev.getEnergyActRec_ac_s1(), block32_c3_0.getEnergyActRec_ac_s1(), listLines.getListConsumptionEn_r_s1());
                            listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_c3_0.getEnergyActRec_ac_s1(), -1);
                        }
                    }
                    block32_C3_0_prev = block32_c3_0;
                }
                else if (curSubId == 2) {
                    Block32_C3_1 block32_c3_1 = new Block32_C3_1(arrBlock32.get(index).getValues());
                    // расход энергии 2 секция
                    if (block32_C3_1_prev != null) {
                        if (acdc_ui == 1) { // постоянка
                            double val = getConsumptionEnergy(block32_C3_1_prev.getEnergyAct_s2_dc(), block32_c3_1.getEnergyAct_s2_dc(), listLines.getListConsumptionEn_s2());
                            listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_c3_1.getEnergyAct_s2_dc(), -1);
                        }
                        else if (acdc_ui == 2) {  // переменка
                            double val = getConsumptionEnergy(block32_C3_1_prev.getEnergyAct_s2_ac(), block32_c3_1.getEnergyAct_s2_ac(), listLines.getListConsumptionEn_s2());
                            listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_c3_1.getEnergyAct_s2_ac(), -1);
                            val = getConsumptionEnergy(block32_C3_1_prev.getEnergyActRec_ac_s2(), block32_c3_1.getEnergyActRec_ac_s2(), listLines.getListConsumptionEn_r_s2());
                            listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_c3_1.getEnergyActRec_ac_s2(), -1);
                        }
                    }
                    block32_C3_1_prev = block32_c3_1;
                }
                else if (curSubId == 3) {
                    Block32_C3_2 block32_c3_2 = new Block32_C3_2(arrBlock32.get(index).getValues());
                    // расход энергии 3 секция
                    if (block32_C3_2_prev != null) {
                        if (acdc_ui == 1) { // постоянка
                            double val = getConsumptionEnergy(block32_C3_2_prev.getEnergyAct_s3_dc(), block32_c3_2.getEnergyAct_s3_dc(), listLines.getListConsumptionEn_s3());
                            listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_c3_2.getEnergyAct_s3_dc(), -1);
                        }
                        else if (acdc_ui == 2) {  // переменка
                            double val = getConsumptionEnergy(block32_C3_2_prev.getEnergyAct_s3_ac(), block32_c3_2.getEnergyAct_s3_ac(), listLines.getListConsumptionEn_s3());
                            listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_c3_2.getEnergyAct_s3_ac(), -1);
                            val = getConsumptionEnergy(block32_C3_2_prev.getEnergyActRec_ac_s3(), block32_c3_2.getEnergyActRec_ac_s3(), listLines.getListConsumptionEn_r_s3());
                            listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_r_s3(), second_coordinate, block32_c3_2.getEnergyActRec_ac_s3(), -1);
                        }
                    }
                    block32_C3_2_prev = block32_c3_2;
                }
                else if (curSubId == 4) {
                    Block32_C3_3 block32_c3_3 = new Block32_C3_3(arrBlock32.get(index).getValues());
                    // расход энергии 4 секция
                    if (block32_C3_3_prev != null) {
                        if (acdc_ui == 1) { // постоянка
                            double val = getConsumptionEnergy(block32_C3_3_prev.getEnergyAct_s4_dc(), block32_c3_3.getEnergyAct_s4_dc(), listLines.getListConsumptionEn_s4());
                            listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_c3_3.getEnergyAct_s4_dc(), -1);
                        }
                        else if (acdc_ui == 2) {  // переменка
                            double val = getConsumptionEnergy(block32_C3_3_prev.getEnergyAct_s4_ac(), block32_c3_3.getEnergyAct_s4_ac(), listLines.getListConsumptionEn_s4());
                            listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_c3_3.getEnergyAct_s4_ac(), -1);
                            val = getConsumptionEnergy(block32_C3_3_prev.getEnergyActRec_ac_s4(), block32_c3_3.getEnergyActRec_ac_s4(), listLines.getListConsumptionEn_r_s4());
                            listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, val, -1);
                            listLines.add(listLines.getListCntEn_r_s4(), second_coordinate, block32_c3_3.getEnergyActRec_ac_s4(), -1);
                        }
                    }
                    block32_C3_3_prev = block32_c3_3;
                }
            }
                break;

            case 0xC4: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
                int curSubId = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                if (curSubId == 0) {
                    Block32_C4_0 block32_c4_0 = new Block32_C4_0(arrBlock32.get(index).getValues());
                    listLines.add(listLines.getListProfile(), second_coordinate, profile, -1);
                    int [] arrId = block32_c4_0.getArrId();
//                    /*if (arrProfiles == null)*/ arrProfiles = new int[2];
//                   // /*if (arrLimits == null)*/ arrLimits = new int[2];
                    for (int j : arrId) {

                        arrProfiles = new int[2];
                        if (j == 1 && arrProfiles[0] == 0) {
                            arrProfiles[0] = j;
                            arrProfiles[1] = indexProfile++;
                        }

                        if (arrObjects == null) {
                            switch (j) {
                                case 7: // светофор
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexTrafficLight++;
                                    break;
                                case 10: // ПОНАБ
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexThermometer++;
                                    break;
                                case 4: // нейтральная вставка
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexNeutralInsert++;
                                    break;
                                case 9: // переезд
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexCrossing++;
                                    break;
                                case 6: // пробы тормозов
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexCheckBrake++;
                                    break;
                                case 19: // обрывоопасное место
                                    arrObjects = new int[2];
                                    arrObjects[0] = j;
                                    arrObjects[1] = indexLandslide++;
                                    break;
                            }
                        }
                    }
                }
            }
                break;

            case 0xC5: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
                int curSubId = getSubId(arrBlock32.get(index).getId(), arrBlock32.get(index).getValues());
                if (curSubId == 0) {    // информация о станциях
                    arrObjects = new int[2]; // станция
                    arrObjects[0] = 8;
                    arrObjects[1] = indexStation++;
                    break;
                }
                if (curSubId == 1) {        // имя станции
                    Block32_C5_1 block32_C5_1 = new Block32_C5_1(arrBlock32.get(index).getValues());
                }
            }
                break;

            case 0xC7: {
                curTypeMove = ArrTrains.CODE_MOVE_ASIM;
//                Block32_C7 block32_C7 = new Block32_C7(arrBlock32.get(index).getValues());
//                String txt = block32_C7.getTextMessage();
//                    System.out.println(txt);
            }
                break;
            case 0x10:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x10(index);
                break;
            case 0x13:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x13(index);
                break;
            case 0x14:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x14(index);
                break;
            case 0x50:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x50(index);
                break;
            case 0x51:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x51(index);
                break;
            case 0x52:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x52(index);
                break;
            case 0x53:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x53(index);
                break;
            case 0x54:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x54(index);
                break;
            case 0x55:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x55(index);
                break;
            case 0x56:
            case 0x16:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                block32_16_56 = new avpt.gr.blocks32.overall.Block32_16_56(arrBlock32.get(index).getValues());
                arrTrains.addInitBr(block32_16_56);
                initDate_26 = block32_16_56.getDateInit();
                break;
            case 0x57:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x57(index);
                break;
            case 0x58:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x58(index);
                break;
            case 0x59:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x59(index);
                break;
            case 0x5A:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5A(index);
                break;
            case 0x5B:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5B(index);
                break;
            case 0x5C:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5C(index);
                break;
            case 0x5D:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5D(index);
                break;
            case 0x5E:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5E(index);
                break;
            case 0x5F:
                curTypeMove = ArrTrains.CODE_MOVE_G;
                handling0x5F(index);
                break;

//            // passenger -----------------------------------------------------------------------------------------------
//            // UI
//            case 0x20:
//            case 0x60:
//            case 0x90: {
//                curTypeMove = CODE_MOVE_P;
//                Block32_20_60_90 block32_20_60_90 = new Block32_20_60_90(curTypeLoc, arrBlock32.get(index).getValues());
//                // voltage ----------
//                listLines.add(listLines.getListVoltage(), second_coordinate, block32_20_60_90.getVoltage(), -1);
//                listLines.add(listLines.getListVoltage_s1(), second_coordinate, block32_20_60_90.getVoltage_s1(), -1);
//                listLines.add(listLines.getListVoltage_s2(), second_coordinate, block32_20_60_90.getVoltage_s2(), -1);
//
////                if (acdc_ui == 1)
////                    listLines.add(listLines.getListVoltageDC(), second_coordinate, block32_20_60_90.getVoltageDC(), -1);
////                else
////                    listLines.add(listLines.getListVoltageDC(), second_coordinate, Double.NaN, -1);
////                if (acdc_ui == 2)
////                    listLines.add(listLines.getListVoltageAC(), second_coordinate, block32_20_60_90.getVoltageAC(), -1);
////                else
////                    listLines.add(listLines.getListVoltageAC(), second_coordinate, Double.NaN, -1);
//
//                listLines.add(listLines.getListVoltageEngine1(), second_coordinate, block32_20_60_90.getVoltageEngine1(), -1);
//                listLines.add(listLines.getListVoltageEngine2(), second_coordinate, block32_20_60_90.getVoltageEngine2(), -1);
//                listLines.add(listLines.getListVoltageEngine3(), second_coordinate, block32_20_60_90.getVoltageEngine3(), -1);
//                listLines.add(listLines.getListVoltageEngine4(), second_coordinate, block32_20_60_90.getVoltageEngine4(), -1);
//                listLines.add(listLines.getListVoltageEngine5(), second_coordinate, block32_20_60_90.getVoltageEngine5(), -1);
//                listLines.add(listLines.getListVoltageEngine6(), second_coordinate, block32_20_60_90.getVoltageEngine6(), -1);
//                listLines.add(listLines.getListVoltageEngine1_4(), second_coordinate, block32_20_60_90.getVoltageEngine1_4(), -1);
//                listLines.add(listLines.getListVoltageEngine5_8(), second_coordinate, block32_20_60_90.getVoltageEngine5_8(), -1);
//                // amperage -----------
//                listLines.add(listLines.getListAmperage_s1(), second_coordinate, block32_20_60_90.getAmperage_s1(), -1);
//                listLines.add(listLines.getListAmperage_s2(), second_coordinate, block32_20_60_90.getAmperage_s2(), -1);
//                listLines.add(listLines.getListAmperage(), second_coordinate, block32_20_60_90.getAmperage(), -1);
//
//                if (acdc_ui == 1)
//                    listLines.add(listLines.getListAmperageCommonDc(), second_coordinate, block32_20_60_90.getAmperageDC(), -1);
//                else
//                    listLines.add(listLines.getListAmperageCommonDc(), second_coordinate, Double.NaN, -1);
//                if (acdc_ui == 2)
//                    listLines.add(listLines.getListAmperageCommonAc(), second_coordinate, block32_20_60_90.getAmperageAC(), -1);
//                else
//                    listLines.add(listLines.getListAmperageCommonAc(), second_coordinate, Double.NaN, -1);
//
//                listLines.add(listLines.getListAmperageTed1_2_s1(), second_coordinate, block32_20_60_90.getAmperageTed1_2_s1(), -1);
//                listLines.add(listLines.getListAmperageTed3_4_s1(), second_coordinate, block32_20_60_90.getAmperageTed3_4_s1(), -1);
//                listLines.add(listLines.getListAmperageTed5_6_s2(), second_coordinate, block32_20_60_90.getAmperageTed5_6_s2(), -1);
//                listLines.add(listLines.getListAmperageTed7_8_s2(), second_coordinate, block32_20_60_90.getAmperageTed7_8_s2(), -1);
//
//                listLines.add(listLines.getListAmperageEngine_sr_s_spr_sp_pr_p(), second_coordinate, block32_20_60_90.getAmperageTed_sr_s_spr_sp_pr_p(), -1);
//                listLines.add(listLines.getListAmperageEngine_spr_sp_pr_p(), second_coordinate, block32_20_60_90.getAmperageTed_spr_sp_pr_p(), -1);
//                listLines.add(listLines.getListAmperageEngine_pr_p(), second_coordinate, block32_20_60_90.getAmperageTed_pr_p(), -1);
//                listLines.add(listLines.getListAmperageEngine_sr_spr_sp_pr_p(), second_coordinate, block32_20_60_90.getAmperageTed_sr_spr_sp_pr_p(), -1);
//                listLines.add(listLines.getListAmperageEngine_s_spr_sp_pr_p(), second_coordinate, block32_20_60_90.getAmperageTed_s_spr_sp_pr_p(), -1);
//
//                listLines.add(listLines.getListAmperageEngine1(), second_coordinate, block32_20_60_90.getAmperageEngine1(), -1);
//                listLines.add(listLines.getListAmperageEngine2(), second_coordinate, block32_20_60_90.getAmperageEngine2(), -1);
//                listLines.add(listLines.getListAmperageEngine3(), second_coordinate, block32_20_60_90.getAmperageEngine3(), -1);
//                listLines.add(listLines.getListAmperageEngine4(), second_coordinate, block32_20_60_90.getAmperageEngine4(), -1);
//                listLines.add(listLines.getListAmperageEngine5(), second_coordinate, block32_20_60_90.getAmperageEngine5(), -1);
//                listLines.add(listLines.getListAmperageEngine6(), second_coordinate, block32_20_60_90.getAmperageEngine6(), -1);
//                listLines.add(listLines.getListAmperageEngine1_2(), second_coordinate, block32_20_60_90.getAmperageEngine1_2(), -1);
//                listLines.add(listLines.getListAmperageEngine3_4(), second_coordinate, block32_20_60_90.getAmperageEngine3_4(), -1);
//                listLines.add(listLines.getListAmperageEngine5_6(), second_coordinate, block32_20_60_90.getAmperageEngine5_6(), -1);
//
//                listLines.add(listLines.getListAmperageAnchor2(), second_coordinate, block32_20_60_90.getAmperageAnchor2(), -1);
//                listLines.add(listLines.getListAmperageAnchor3(), second_coordinate, block32_20_60_90.getAmperageAnchor3(), -1);
//                listLines.add(listLines.getListAmperageAnchor4(), second_coordinate, block32_20_60_90.getAmperageAnchor4(), -1);
//                listLines.add(listLines.getListAmperageAnchor5(), second_coordinate, block32_20_60_90.getAmperageAnchor5(), -1);
//                listLines.add(listLines.getListAmperageAnchor6(), second_coordinate, block32_20_60_90.getAmperageAnchor6(), -1);
//                listLines.add(listLines.getListAmperageAnchor8(), second_coordinate, block32_20_60_90.getAmperageAnchor8(), -1);
//
//                listLines.add(listLines.getListAmperageHeat_s1(), second_coordinate, block32_20_60_90.getAmperageHeat_s1(), -1);
//                listLines.add(listLines.getListAmperageHeat_s2(), second_coordinate, block32_20_60_90.getAmperageHeat_s2(), -1);
//                listLines.add(listLines.getListAmperageHeat(), second_coordinate, block32_20_60_90.getAmperageHeat(), -1);
//
//                if (acdc_ui == 1)
//                    listLines.add(listLines.getListAmperageHeatDc(), second_coordinate, block32_20_60_90.getAmperageHeatDC(), -1);
//                else
//                    listLines.add(listLines.getListAmperageHeatDc(), second_coordinate, Double.NaN, -1);
//
//                if (acdc_ui == 2)
//                    listLines.add(listLines.getListAmperageHeatAc(), second_coordinate, block32_20_60_90.getAmperageHeatAC(), -1);
//                else
//                    listLines.add(listLines.getListAmperageHeatAc(), second_coordinate, Double.NaN, -1);
//
//                listLines.add(listLines.getListAmperageExcitation1(), second_coordinate, block32_20_60_90.getAmperageExcitation1(), -1);
//                listLines.add(listLines.getListAmperageExcitation5(), second_coordinate, block32_20_60_90.getAmperageExcitation5(), -1);
//                listLines.add(listLines.getListAmperageHelp(), second_coordinate, block32_20_60_90.getAmperageHelp(), -1);
//                listLines.add(listLines.getListAmperageEpt(), second_coordinate, block32_20_60_90.getAmperageEpt(), -1);
//
//                listLines.add(listLines.getListPowerThrustBrake_1(), second_coordinate, block32_20_60_90.getPowerThrustBrake_1(), -1);
//                listLines.add(listLines.getListPowerThrustBrake_2(), second_coordinate, block32_20_60_90.getPowerThrustBrake_2(), -1);
//                listLines.add(listLines.getListPowerThrustBrake_3(), second_coordinate, block32_20_60_90.getPowerThrustBrake_3(), -1);
//                listLines.add(listLines.getListPowerThrustBrake_4(), second_coordinate, block32_20_60_90.getPowerThrustBrake_4(), -1);
//                listLines.add(listLines.getListPowerThrustBrake_5(), second_coordinate, block32_20_60_90.getPowerThrustBrake_5(), -1);
//                listLines.add(listLines.getListPowerThrustBrake_6(), second_coordinate, block32_20_60_90.getPowerThrustBrake_6(), -1);
//                listLines.add(listLines.getListPowerThrust(), second_coordinate, block32_20_60_90.getPowerThrust(), -1);
//                listLines.add(listLines.getListPowerBrake(), second_coordinate, block32_20_60_90.getPowerBrake(), -1);
//            }
//            break;
//            case 0x23:
//            case 0x63:
//            case 0x93: {
//                curTypeMove = CODE_MOVE_P;
//                Block32_23_63_93 block32_23_63_93 = new Block32_23_63_93(curTypeLoc, arrBlock32.get(index).getValues(), acdc_ui);
//                if (block32_23_63_93_prev != null) {
//                    if (curTypeLoc != EP20_KAUD) {
//                        // общая
//                        double val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionSum(),
//                                (long) block32_23_63_93.getEnConsumptionSum(), listLines.getListConsumptionEnSum());
//                        listLines.add(listLines.getListConsumptionEnSum(), second_coordinate, val, -1);
//                        listLines.add(listLines.getListCntEnSum(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                        // рекуп
//                        val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionRec(),
//                                (long) block32_23_63_93.getEnConsumptionRec(), listLines.getListConsumptionEnRec());
//                        listLines.add(listLines.getListConsumptionEnRec(), second_coordinate, val, -1);
//                        listLines.add(listLines.getListCntEnRec(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                        // вспомогательная
//                        val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHelp(),
//                                (long) block32_23_63_93.getEnConsumptionHelp(), listLines.getListConsumptionEnHelp());
//                        listLines.add(listLines.getListConsumptionEnHelp(), second_coordinate, val, -1);
//                        listLines.add(listLines.getListCntEnHelp(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                        // отопление
//                        val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHeat(),
//                                (long) block32_23_63_93.getEnConsumptionHeat(), listLines.getListConsumptionEnHeat());
//                        listLines.add(listLines.getListConsumptionEnHeat(), second_coordinate, val, -1);
//                        listLines.add(listLines.getListCntEnHeat(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                    }
//                    else { // ЭП20
//                        if (acdc_ui == 1) {// постоянка
//                            // общая
//                            double val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionSum(),
//                                    (long) block32_23_63_93.getEnConsumptionSum(), listLines.getListConsumptionEnSumDc());
//                            listLines.add(listLines.getListConsumptionEnSumDc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnSumDc(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                            // рекуп
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionRec(),
//                                    (long) block32_23_63_93.getEnConsumptionRec(), listLines.getListConsumptionEnRecDc());
//                            listLines.add(listLines.getListConsumptionEnRecDc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnRecDc(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                            // вспомогательная
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHelp(),
//                                    (long) block32_23_63_93.getEnConsumptionHelp(), listLines.getListConsumptionEnHelpDc());
//                            listLines.add(listLines.getListConsumptionEnHelpDc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnHelpAc(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                            // отопление
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHeat(),
//                                    (long) block32_23_63_93.getEnConsumptionHeat(), listLines.getListConsumptionEnHeatDc());
//                            listLines.add(listLines.getListConsumptionEnHeatDc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnHeatDc(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                        }
//                        else if (acdc_ui == 2) {// переменка
//                            // общая
//                            double val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionSum(),
//                                    (long) block32_23_63_93.getEnConsumptionSum(), listLines.getListConsumptionEnSumAc());
//                            listLines.add(listLines.getListConsumptionEnSumAc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnSumAc(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                            // рекуп
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionRec(),
//                                    (long) block32_23_63_93.getEnConsumptionRec(), listLines.getListConsumptionEnRecAc());
//                            listLines.add(listLines.getListConsumptionEnRecAc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnRecAc(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                            // вспомогательная
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHelp(),
//                                    (long) block32_23_63_93.getEnConsumptionHelp(), listLines.getListConsumptionEnHelpAc());
//                            listLines.add(listLines.getListConsumptionEnHelpAc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnHelpAc(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                            // отопление
//                            val = getConsumptionEnergy((long) block32_23_63_93_prev.getEnConsumptionHeat(),
//                                    (long) block32_23_63_93.getEnConsumptionHeat(), listLines.getListConsumptionEnHeatAc());
//                            listLines.add(listLines.getListConsumptionEnHeatAc(), second_coordinate, val, -1);
//                            listLines.add(listLines.getListCntEnHeatAc(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                        }
//                    }
//                }
//                block32_23_63_93_prev = block32_23_63_93;
//                if (curTypeLoc != EP20_KAUD) {
//                    listLines.add(listLines.getListConsumptionEnAll(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                    listLines.add(listLines.getListConsumptionEnRec(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                    listLines.add(listLines.getListConsumptionEnHelp(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                    listLines.add(listLines.getListConsumptionEnHeat(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                }
//                else {
//                    if (acdc_ui == 1) {// постоянка
//                        listLines.add(listLines.getListConsumptionEnAllDc(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                        listLines.add(listLines.getListConsumptionEnRecDc(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                        listLines.add(listLines.getListConsumptionEnHelpDc(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                        listLines.add(listLines.getListConsumptionEnHeatDc(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                    }
//                    if (acdc_ui == 2) {// переменка
//                        listLines.add(listLines.getListConsumptionEnAllAc(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//                        listLines.add(listLines.getListConsumptionEnRecAc(), second_coordinate, block32_23_63_93.getEnConsumptionRec(), -1);
//                        listLines.add(listLines.getListConsumptionEnHelpAc(), second_coordinate, block32_23_63_93.getEnConsumptionHelp(), -1);
//                        listLines.add(listLines.getListConsumptionEnHeatAc(), second_coordinate, block32_23_63_93.getEnConsumptionHeat(), -1);
//                    }
//                }
//                listLines.add(listLines.getListConsumptionEnAll(), second_coordinate, block32_23_63_93.getEnConsumptionSum(), -1);
//            }
//            break;
//            case 0x24:
//            case 0x64:
//            case 0x94: {
//                curTypeMove = CODE_MOVE_P;
//                Block32_24_64_94 block32_24_64_94 = new Block32_24_64_94(curTypeLoc, arrBlock32.get(index).getValues());
//                if (curTime_21 != null) {
//                    addSecond(isTime);
//                    boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, curTime_21);
//                    // сброс счетчиков энергии на начало поезда
//                    if (isNewTrain) resetEnergy();
//                    listBlockBySeconds.add(index);
//                }
//                acdc_ui = block32_24_64_94.getAcDcUI();
//            }
//            break;
//            case 0x26:
//            case 0x66:
//            case 0x96: {
//                curTypeMove = CODE_MOVE_P;
//                Block32_26_66_96 block32_26_66_96 = new Block32_26_66_96(arrBlock32.get(index).getValues());
//                curTypeLoc = block32_26_66_96.getTypeLoc();
//            }
//            break;
//            case 0x76: {
//                Block32_76 block32_76 = new Block32_76(arrBlock32.get(index).getValues());
//                curTypeLoc = block32_76.getTypeLoc();
//            }
//            break;
//            case 0x78: {
//                Block32_78 block32_78 = new Block32_78(arrBlock32.get(index).getValues());
//                curTypeLoc = block32_78.getTypeLoc();
//                break;
//            }

        }
        arrBlock32.get(index).setSecond(second_coordinate);
      //  arrBlock32.get(index).setDateTime(curDateTime_21);
        setDateTimeToArrBlock32(index);
        arrBlock32.get(index).setCoordinate(cur_coordinate);
        arrBlock32.get(index).setKm(km);
        arrBlock32.get(index).setPk(pk);
        arrBlock32.get(index).setValLimTmp(tempLimit);
        arrBlock32.get(index).setLen_prof(len_prof);
        arrBlock32.get(index).setSlope_prof(slope_prof);
    }

    /**
     * добавляем текущий обект в соответствующий массив
     */
    private void addCurObject() {
//        if (arrObjects != null)
//            System.out.println(second + "_" + arrObjects[0] + "_" + arrObjects[1]);
        listObjects.add(arrObjects);
        arrObjects = null;
    }

    private void addCurProfile() {
//        Profiles profiles = new Profiles(arrBlock32);
//        if (arrObjects != null)
//            System.out.println(second + "_" + arrObjects[0] + "_" + arrObjects[1]);
        listProfiles.add(arrProfiles);
        if (arrProfiles != null && arrProfiles[0] == 1) {
            //   System.out.println(second_coordinate + "_" + profiles.getProfile(arrProfiles[1]).getLen());
            if (profiles.size() > 0) {
                len_prof = (int) profiles.getProfile(arrProfiles[1]).getLen();
                slope_prof = (int) profiles.getProfile(arrProfiles[1]).getSlope();
            }
        }
        arrProfiles = null;
    }

    private void addCurMapLimit() {
        listLimits.add(arrLimits);
        arrLimits = null;
    }

    private void addRailCoordinate() {
        listRailCoordinate.add(new RailCoordinate(km, pk));
    }

//    private void addSpeedLimTmp() {
//        listSpeedLimTmp.add(tempLimit);
//    }

    private void addGpsCoordinate() {
        listGpsCoordinate.add(new GpsCoordinate(lon, lat));
    }

    private LocalDateTime getCurDateTime() {

        LocalDate date;
        if (curDateTime_21 != null) {
            date = curDateTime_21.toLocalDate();
        }
        else date = initDate_26;

        LocalTime time = null;
        if (curTime_21 != null)
            time = curTime_21;
        else if (curTime != null)
            time = curTime;

        if (date != null && time != null)
            return LocalDateTime.of(date, time);
        else
            return null;
    }

    private void setDateTimeToArrBlock32(int index) {
        arrBlock32.get(index).setDateTime(getCurDateTime());
    }

    /**
     * добавляем текущие дату и время в соответствующие массивы
     */
    private void addCurDateTime() {
       listDateTimes.add(getCurDateTime());
    }

    public void finalStep() {
        arrTrains.complete(arrBlock32);
        arrTrains.fillEnergy(listLines);
        arrTrains.fillArrList(listLines.getListTrains());
//        for (int i = 0; i < arrTrains.size() -1; i++) {
//            System.out.println(arrTrains.get(i).getStIsavprt());
//        }
        if (arrTrains.size() == 0) {
            Train train = new Train(0);
            train.setBlEnd(arrBlock32.size() - 1);
            train.setSecondStart(0);
            train.setSecondEnd(arrBlock32.get(arrBlock32.size() - 1).getSecond());
            train.setTypeLoc(curTypeLoc);
            train.setNumTab(0);
            arrTrains.add(train);
        }
        listLines.complete(listLines.getListVoltageEngine1_s1(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine1_s2(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine1_s3(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine1_s4(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine2_s1(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine2_s2(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine2_s3(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine2_s4(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine3_s1(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine3_s2(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine3_s3(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine3_s4(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine4_s1(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine4_s2(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine4_s3(), second_coordinate);
        listLines.complete(listLines.getListVoltageEngine4_s4(), second_coordinate);

        listLines.complete(listLines.getListVoltage(), second_coordinate);
        listLines.complete(listLines.getListVoltage_s1(), second_coordinate);
        listLines.complete(listLines.getListVoltage_s2(), second_coordinate);
        listLines.complete(listLines.getListVoltage_s3(), second_coordinate);
        listLines.complete(listLines.getListVoltage_s4(), second_coordinate);
//        listLines.complete(listLines.getListVoltageAC(), second_coordinate);
//        listLines.complete(listLines.getListVoltageDC(), second_coordinate);
        listLines.complete(listLines.getListVoltageBhvAkbGet(), second_coordinate);
        listLines.complete(listLines.getListVoltageBhvAkbSend(), second_coordinate);

        listLines.complete(listLines.getListAmperageAnchor(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor1_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation1_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor1_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation1_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor1_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation1_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor1_s4(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation1_s4(), second_coordinate);

        listLines.complete(listLines.getListAmperageAnchor2_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation2_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor2_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation2_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor2_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation2_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor2_s4(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation2_s4(), second_coordinate);

        listLines.complete(listLines.getListAmperageAnchor3_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation3_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor3_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation3_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor3_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation3_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor3_s4(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation3_s4(), second_coordinate);

        listLines.complete(listLines.getListAmperageAnchor4_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation4_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor4_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation4_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor4_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation4_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperageAnchor4_s4(), second_coordinate);
        listLines.complete(listLines.getListAmperageExcitation4_s4(), second_coordinate);

        listLines.complete(listLines.getListAmperage_s1(), second_coordinate);
        listLines.complete(listLines.getListAmperage_s2(), second_coordinate);
        listLines.complete(listLines.getListAmperage_s3(), second_coordinate);
        listLines.complete(listLines.getListAmperage_s4(), second_coordinate);

        listLines.complete(listLines.getListConsumptionEn(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_s1(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_s2(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_s3(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_s4(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_r(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_r_s1(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_r_s2(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_r_s3(), second_coordinate);
        listLines.complete(listLines.getListConsumptionEn_r_s4(), second_coordinate);

        listLines.complete(listLines.getListCntEn(), second_coordinate);
        listLines.complete(listLines.getListCntEn_s1(), second_coordinate);
        listLines.complete(listLines.getListCntEn_s2(), second_coordinate);
        listLines.complete(listLines.getListCntEn_s3(), second_coordinate);
        listLines.complete(listLines.getListCntEn_s4(), second_coordinate);
        listLines.complete(listLines.getListCntEn_r(), second_coordinate);
        listLines.complete(listLines.getListCntEn_r_s1(), second_coordinate);
        listLines.complete(listLines.getListCntEn_r_s2(), second_coordinate);
        listLines.complete(listLines.getListCntEn_r_s3(), second_coordinate);
        listLines.complete(listLines.getListCntEn_r_s4(), second_coordinate);

//        listLines.complete(listLines.getListCntEnSum(), second_coordinate);
//        listLines.complete(listLines.getListCntEnRec(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHelp(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHeat(), second_coordinate);
//        listLines.complete(listLines.getListCntEnSumAc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnRecAc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHelpAc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHeatAc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnSumDc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnRecDc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHelpDc(), second_coordinate);
//        listLines.complete(listLines.getListCntEnHeatDc(), second_coordinate);

        listLines.complete(listLines.getS5k_listPowerImplementation(), second_coordinate);
        listLines.complete(listLines.getListPressPM(), second_coordinate);
        listLines.complete(listLines.getListPressUR(), second_coordinate);
        listLines.complete(listLines.getListPressTM(), second_coordinate);
        listLines.complete(listLines.getListPressTC(), second_coordinate);
        listLines.complete(listLines.getListPressTmTail(), second_coordinate);
        listLines.complete(listLines.getListPressNM(), second_coordinate);
        listLines.complete(listLines.getS5k_listSpeed_MSUD(), second_coordinate);
        listLines.complete(listLines.getListPowerLocomotiveSlave(), second_coordinate);
        listLines.complete(listLines.getListDistanceTail(), second_coordinate);
        listLines.complete(listLines.getListPressTC_USAVP(), second_coordinate);
        listLines.complete(listLines.getListPressTM_USAVP(), second_coordinate);
        listLines.complete(listLines.getListPressUR_USAVP(), second_coordinate);
        listLines.complete(listLines.getListPressTC_s1(), second_coordinate);
        listLines.complete(listLines.getListPressTM_s1(), second_coordinate);
        listLines.complete(listLines.getListPressUR_s1(), second_coordinate);
        listLines.complete(listLines.getListPressTC_s2(), second_coordinate);
        listLines.complete(listLines.getListPressTM_s2(), second_coordinate);
        listLines.complete(listLines.getListPressUR_s2(), second_coordinate);
        listLines.complete(listLines.getListSpeedLimitTemp(), second_coordinate);
        listLines.complete(listLines.getListSpeedLimitConst(), second_coordinate);
        listLines.complete(listLines.getListSpeed_USAVP(), second_coordinate);
        listLines.complete(listLines.getListSpeedFact(), second_coordinate);
        listLines.complete(listLines.getListSpeedCalc(), second_coordinate);
        listLines.complete(listLines.getListSpeedGPS(), second_coordinate);

        listLines.complete(listLines.getListSpeed_BUT(), second_coordinate);
        listLines.complete(listLines.getListSpeed_BLOCK(), second_coordinate);
        listLines.complete(listLines.getListSpeed_Task(), second_coordinate);
        listLines.complete(listLines.getListSpeed_ERG(), second_coordinate);
     //   listLines.complete(listLines.getListSpeedMax(), second_coordinate);
        listLines.complete(listLines.getListSpeed_CLUB(), second_coordinate);
        listLines.complete(listLines.getListSpeed_BS_DPS(), second_coordinate);
        listLines.complete(listLines.getListPermissibleSpeed_CLUB(), second_coordinate);
        listLines.complete(listLines.getListSpeedSlave(), second_coordinate);
        listLines.complete(listLines.getListCurSpeedLimit(), second_coordinate);
        listLines.complete(listLines.getListLevelGSM(), second_coordinate);
        listLines.complete(listLines.getListWeakField(), second_coordinate);
        listLines.complete(listLines.getListPositionS5k(), second_coordinate);
        listLines.complete(listLines.getListPosition(), second_coordinate);
        listLines.complete(listLines.getListPositionTaskAuto(), second_coordinate);
        listLines.complete(listLines.getListProfile(), second_coordinate);
        listLines.complete(listLines.getListTempLimit(), second_coordinate);

        ArrayList<ListLines.ItemLine> listProf = listLines.getListProfile();
        if (listProf.size() > 0) {
            // опорная линия профиля
            ListLines.addDirect(listProf, listLines.getListProfileDirect(), listProf.get(0).getSecond(),
                    listProf.get(listProf.size() - 1).getSecond(), min_prof, true);
            // опорная линия карты
            // координита y для опорной линии карты
            double DIRECT_MAP = 10;
            ListLines.addDirect(listProf, listLines.getListMapDirect(), listProf.get(0).getSecond(),
                    listProf.get(listProf.size() - 1).getSecond(), DIRECT_MAP, true);
            // линия под иконками станций
            ListLines.addDirect(listProf, listLines.getListMapLine(), listProf.get(0).getSecond(),
                    listProf.get(listProf.size() - 1).getSecond(), LINE_MAP, false);
        }
//        for (int i = 0; i < listLines.getListSpeed_USAVP().size(); i++) {
//            ListLines.ItemLine item = listLines.getListSpeed_USAVP().get(i);
//            System.out.println(item.getSecond() + "_" + item.getSecond_last() + " val=" + item.getValue());
//        }
    }

    public ListLines getListLines() {
        return listLines;
    }

    public ListSignals getListSignals() {
        return listSignals;
    }

    public ListTasks getListTasks() {
        return listTasks;
    }

    private void handling0x10(int index) {
        switch (curTypeLoc) {
            case Train.VL10:// ВЛ10
                avpt.gr.blocks32.vl10.Block32_10 block32_10 = new avpt.gr.blocks32.vl10.Block32_10(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage(), second_coordinate, block32_10.getVoltage(), -1);
                listLines.add(listLines.getListVoltageEngine_s1(), second_coordinate, block32_10.getVoltageEngine_s1(), -1);
                listLines.add(listLines.getListVoltageEngine_s2(), second_coordinate, block32_10.getVoltageEngine_s2(), -1);
                listLines.add(listLines.getListAmperage(), second_coordinate, block32_10.getAmperage(), -1);
                listLines.add(listLines.getListAmperageAnchor1_s1(), second_coordinate, block32_10.getAmperageAnchor1_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor1_s2(), second_coordinate, block32_10.getAmperageAnchor1_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s1(), second_coordinate, block32_10.getAmperageAnchor2_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s2(), second_coordinate, block32_10.getAmperageAnchor2_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation_s1(), second_coordinate, block32_10.getAmperageExcitation_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation_s2(), second_coordinate, block32_10.getAmperageExcitation_s2(), -1);
                break;
        }
    }

    private void handling0x13(int index) {
        switch (curTypeLoc) {
            case Train.VL10:// ВЛ10
                avpt.gr.blocks32.vl10.Block32_13 block32_13 = new avpt.gr.blocks32.vl10.Block32_13(arrBlock32.get(index).getValues());

                if (block32_13_prev != null) {
                    double val = getConsumptionEnergy(block32_13_prev.getEnergy(), block32_13.getEnergy(), listLines.getListConsumptionEn());
                    listLines.add(listLines.getListConsumptionEn(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn(), second_coordinate, block32_13.getEnergy(), -1);
                    val = getConsumptionEnergy(block32_13_prev.getEnergyRec(), block32_13.getEnergyRec(), listLines.getListConsumptionEn_r());
                    listLines.add(listLines.getListConsumptionEn_r(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r(), second_coordinate, block32_13.getEnergyRec(), -1);
                }
                block32_13_prev = block32_13;
                break;
        }
    }

    private void handling0x14(int index) {
        switch (curTypeLoc) {
            case Train.VL10: {// ВЛ10
                avpt.gr.blocks32.vl10.Block32_14 block32_14_vl10 = new avpt.gr.blocks32.vl10.Block32_14(arrBlock32.get(index).getValues());
                curTime = block32_14_vl10.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_14_vl10.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listLines.add(listLines.getListPressUR_s1(), second_coordinate, block32_14_vl10.getPressUR_s1(), -1);
                listLines.add(listLines.getListPressTM_s1(), second_coordinate, block32_14_vl10.getPressTM_s1(), -1);
                listLines.add(listLines.getListPressTC_s1(), second_coordinate, block32_14_vl10.getPressTC_s1(), -1);
                listLines.add(listLines.getListPressUR_s2(), second_coordinate, block32_14_vl10.getPressUR_s2(), -1);
                listLines.add(listLines.getListPressTM_s2(), second_coordinate, block32_14_vl10.getPressTM_s2(), -1);
                listLines.add(listLines.getListPressTC_s2(), second_coordinate, block32_14_vl10.getPressTC_s2(), -1);

                listTasks.add(listTasks.getListALSN_vl10_1(), second_coordinate, block32_14_vl10.getALSN1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListALSN_vl10_2(), second_coordinate, block32_14_vl10.getALSN2(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkm_1(), second_coordinate, block32_14_vl10.getKKM1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkm_2(), second_coordinate, block32_14_vl10.getKKM2(), curTypeLoc, -1);
                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_14_vl10.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_14_vl10.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_14_vl10.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_14_vl10.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_14_vl10.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_14_vl10.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_14_vl10.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_14_vl10.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_14_vl10.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_14_vl10.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_14_vl10.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_14_vl10.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_14_vl10.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_14_vl10.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_14_vl10.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_14_vl10.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_14_vl10.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_14_vl10.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_14_vl10.getKeyNine(), curTypeLoc, -1);
                //
                listSignals.add(listSignals.getListBox(), second_coordinate, block32_14_vl10.getBox() == 1);
                listSignals.add(listSignals.getListPst1(), second_coordinate, block32_14_vl10.getPST1() == 1);
                listSignals.add(listSignals.getListPst2(), second_coordinate, block32_14_vl10.getPST2() == 1);
                listSignals.add(listSignals.getListEpk1(), second_coordinate, block32_14_vl10.getEPK1() == 1);
                listSignals.add(listSignals.getListEpk2(), second_coordinate, block32_14_vl10.getEPK2() == 1);
                listSignals.add(listSignals.getListBv(), second_coordinate, block32_14_vl10.getBV() == 1);
                listSignals.add(listSignals.getListPt_rec(), second_coordinate, block32_14_vl10.getPT_rec() == 1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_14_vl10.getTM() == 1);
                listSignals.add(listSignals.getListRevers(), second_coordinate, block32_14_vl10.getRevers() == 1);
                listSignals.add(listSignals.getListOverload(), second_coordinate, block32_14_vl10.getOverload() == 1);
                listSignals.add(listSignals.getListStop1(), second_coordinate, block32_14_vl10.getStop1() == 1);
                listSignals.add(listSignals.getListStop2(), second_coordinate, block32_14_vl10.getStop2() == 1);
            }
                break;
            case Train.VL11: {
                avpt.gr.blocks32.vl11.Block32_14 block32_14_vl11 = new avpt.gr.blocks32.vl11.Block32_14(arrBlock32.get(index).getValues());
                curTime = block32_14_vl11.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_14_vl11.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listSignals.add(listSignals.getListTM1(), second_coordinate, block32_14_vl11.getTM1() == 1);
                listSignals.add(listSignals.getListTM2(), second_coordinate, block32_14_vl11.getTM2() == 1);
                listSignals.add(listSignals.getListBV1(), second_coordinate, block32_14_vl11.getBV1() == 1);
                listSignals.add(listSignals.getListBV2(), second_coordinate, block32_14_vl11.getBV2() == 1);
                listSignals.add(listSignals.getListPSR1(), second_coordinate, block32_14_vl11.getPSR1() == 1);
                listSignals.add(listSignals.getListPSR2(), second_coordinate, block32_14_vl11.getPSR2() == 1);
                listSignals.add(listSignals.getListKP1(), second_coordinate, block32_14_vl11.getKP1() == 1);
                listSignals.add(listSignals.getListKP2(), second_coordinate, block32_14_vl11.getKP2() == 1);
                listSignals.add(listSignals.getListUKK1(), second_coordinate, block32_14_vl11.getUKK1() == 1);
                listSignals.add(listSignals.getListUKK2(), second_coordinate, block32_14_vl11.getUKK2() == 1);
                listSignals.add(listSignals.getListSER_BRAKE1(), second_coordinate, block32_14_vl11.getSerBrake1() == 1);
                listSignals.add(listSignals.getListSER_BRAKE2(), second_coordinate, block32_14_vl11.getSerBrake2() == 1);
                listSignals.add(listSignals.getListInterDriver1(), second_coordinate, block32_14_vl11.getInterDriver1() == 1);
                listSignals.add(listSignals.getListInterDriver2(), second_coordinate, block32_14_vl11.getInterDriver2() == 1);
                listSignals.add(listSignals.getListMV1(), second_coordinate, block32_14_vl11.getMV1() == 1);
                listSignals.add(listSignals.getListMV2(), second_coordinate, block32_14_vl11.getMV2() == 1);
                listSignals.add(listSignals.getListSSP1(), second_coordinate, block32_14_vl11.getSSP1() == 1);
                listSignals.add(listSignals.getListSSP2(), second_coordinate, block32_14_vl11.getSSP2() == 1);
                listSignals.add(listSignals.getListDB1(), second_coordinate, block32_14_vl11.getDB1() == 1);
                listSignals.add(listSignals.getListDB2(), second_coordinate, block32_14_vl11.getDB2() == 1);
            }
                break;
        }
    }

    private void handling0x50(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                avpt.gr.blocks32.vl80.Block32_50 block32_50 = new avpt.gr.blocks32.vl80.Block32_50(arrBlock32.get(index).getValues());
                listSignals.add(listSignals.getListEpk1(), second_coordinate, block32_50.getEPK1() == 1);
                listSignals.add(listSignals.getListEpk2(), second_coordinate, block32_50.getEPK2() == 1);
                listSignals.add(listSignals.getListPst1(), second_coordinate, block32_50.getPST1() == 1);
                listSignals.add(listSignals.getListPst2(), second_coordinate, block32_50.getPST2() == 1);

                listLines.add(listLines.getListVoltageEngine_s1(), second_coordinate, block32_50.getVoltageEngine_s1(), -1);
                listLines.add(listLines.getListVoltageEngine_s2(), second_coordinate, block32_50.getVoltageEngine_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor1_s1(), second_coordinate, block32_50.getAmperageAnchor1_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor1_s2(), second_coordinate, block32_50.getAmperageAnchor1_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s1(), second_coordinate, block32_50.getAmperageAnchor2_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s2(), second_coordinate, block32_50.getAmperageAnchor2_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation_s1(), second_coordinate, block32_50.getAmperageExcitation_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation_s2(), second_coordinate, block32_50.getAmperageExcitation_s2(), -1);

                listTasks.add(listTasks.getListKkm_1(), second_coordinate, block32_50.getKKM1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkm_2(), second_coordinate, block32_50.getKKM2(), curTypeLoc, -1);
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: // 2ЭС4К, 3ЭС4К
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                break;
        }
    }

    private void handling0x51(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: // 2ЭС4К, 3ЭС4К
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_51 block32_51 = new avpt.gr.blocks32.s5k_2.Block32_51(curTypeLoc, arrBlock32.get(index).getValues());
                listTasks.add(listTasks.getListUatlCommand(), second_coordinate, block32_51.getCommand(), curTypeLoc, -1);
                listTasks.add(listTasks.getListUatlMode(), second_coordinate, block32_51.getMode(), curTypeLoc, -1);
                listTasks.add(listTasks.getListUatlWork(), second_coordinate, block32_51.getWork(), curTypeLoc, -1);
                break;
        }
    }

    private void handling0x52(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
            {
                avpt.gr.blocks32.s5k.Block32_52 block32_52 = new avpt.gr.blocks32.s5k.Block32_52(arrBlock32.get(index).getValues());
                // напряжение секция 1
                listLines.add(listLines.getListVoltageEngine1_s1(), second_coordinate, block32_52.getVoltageEngine1_s1(), -1);
                listLines.add(listLines.getListVoltageEngine2_s1(), second_coordinate, block32_52.getVoltageEngine2_s1(), -1);
                listLines.add(listLines.getListVoltageEngine3_s1(), second_coordinate, block32_52.getVoltageEngine3_s1(), -1);
                listLines.add(listLines.getListVoltageEngine4_s1(), second_coordinate, block32_52.getVoltageEngine4_s1(), -1);
                // ток секция 1
                listLines.add(listLines.getListAmperageAnchor1_s1(), second_coordinate, block32_52.getAmperageAnchor1_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s1(), second_coordinate, block32_52.getAmperageAnchor2_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s1(), second_coordinate, block32_52.getAmperageAnchor3_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s1(), second_coordinate, block32_52.getAmperageAnchor4_s1(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s1(), second_coordinate, block32_52.getAmperageExcitation1_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s1(), second_coordinate, block32_52.getAmperageExcitation2_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s1(), second_coordinate, block32_52.getAmperageExcitation3_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s1(), second_coordinate, block32_52.getAmperageExcitation4_s1(), -1);

                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_52.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_52.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_52.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_52.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_52.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_52.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_52.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_52.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_52.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_52.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_52.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_52.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_52.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_52.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_52.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_52.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_52.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_52.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_52.getKeyNine(), curTypeLoc, -1);
                //
            }
                break;
            case Train.KZ8A: {// KZ8A
                avpt.gr.blocks32.kz8.Block32_52 block32_52 = new avpt.gr.blocks32.kz8.Block32_52(arrBlock32.get(index).getValues());
                curTime = block32_52.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_52.getTime());
                listBlockBySeconds.add(index);

                listLines.add(listLines.getListPowerMax(), second_coordinate, block32_52.getPowerMax(), -1); //
                listLines.add(listLines.getListPowerMaxRec(), second_coordinate, block32_52.getPowerMaxRec(), -1); //

                listLines.add(listLines.getListPressUR(), second_coordinate, block32_52.getPressUR(), -1);
                listLines.add(listLines.getListPressTM(), second_coordinate, block32_52.getPressTM(), -1);
                listLines.add(listLines.getListPressTC(), second_coordinate, block32_52.getPressTC(), -1);
                listLines.add(listLines.getListPressNM(), second_coordinate, block32_52.getPressNM(), -1);
                listTasks.add(listTasks.getListKkm_kz8(), second_coordinate, block32_52.getKKM(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkbt_kz8(), second_coordinate, block32_52.getKKBT(), curTypeLoc, -1);
                listLines.add(listLines.getListPosition(), second_coordinate, block32_52.getPosition(), -1);
            }
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_52 block32_52 = new avpt.gr.blocks32.s5.Block32_52(arrBlock32.get(index).getValues());
                curTime = block32_52.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_52.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listLines.add(listLines.getListSpeed_BUT(), second_coordinate, block32_52.getSpeedBoot(), -1);
                listLines.add(listLines.getListSpeed_Task(), second_coordinate, block32_52.getTaskControl(), -1);
                listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, block32_52.getPowerImplement(), -1); // тяги
                listLines.add(listLines.getListPowerTaskControl(), second_coordinate, block32_52.getPowerTaskControl(), -1); //
                listLines.add(listLines.getListPowerMax(), second_coordinate, block32_52.getPowerMax(), -1); //
                listLines.add(listLines.getListPowerMaxRec(), second_coordinate, block32_52.getPowerMaxRec(), -1); //

                listSignals.add(listSignals.getListReadyAuto(), second_coordinate, block32_52.getReadyAuto() == 1);
                listSignals.add(listSignals.getListProtect(), second_coordinate, block32_52.getProtect() == 1);
                listSignals.add(listSignals.getListInterDriver(), second_coordinate, block32_52.getInterDriver() == 1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_52.getTM() == 1);
                listSignals.add(listSignals.getListGV(), second_coordinate, block32_52.getGV() == 1);
                listSignals.add(listSignals.getListPst(), second_coordinate, block32_52.getPST() == 1);
                listSignals.add(listSignals.getListBox(), second_coordinate, block32_52.getBox() == 1);
                listSignals.add(listSignals.getListBSK(), second_coordinate, block32_52.getAutoBSK() == 1);
                listSignals.add(listSignals.getListRT(), second_coordinate, block32_52.getRT() == 1);

                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_52.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_52.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_52.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_52.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_52.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_52.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_52.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_52.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_52.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_52.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_52.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_52.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_52.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_52.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_52.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_52.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_52.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_52.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_52.getKeyNine(), curTypeLoc, -1);
            }
                break;
            case Train.S6: {// 2ЭС6
                avpt.gr.blocks32.s6.Block32_52 block32_52 = new avpt.gr.blocks32.s6.Block32_52(arrBlock32.get(index).getValues());
                curTime = block32_52.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_52.getTime());
//                // сброс счетчиков энергии на начало поезда
//                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listLines.add(listLines.getListPowerTaskAuto(), second_coordinate, block32_52.getPowerTaskAuto(), -1); // т
                listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, block32_52.getPowerImplement(), -1); //
                listLines.add(listLines.getListVoltage(), second_coordinate, block32_52.getVoltage(), -1);
                listLines.add(listLines.getListAmperageAnchor(), second_coordinate, block32_52.getAmperageAnchor(), -1);
                listLines.add(listLines.getListAmperageExcitation(), second_coordinate, block32_52.getAmperageExcitation(), -1);
                listLines.add(listLines.getListPosition(), second_coordinate, block32_52.getPosition(), -1);
                listLines.add(listLines.getListPositionTaskAuto(), second_coordinate, block32_52.getPositionTaskAuto(), -1);

                listSignals.add(listSignals.getListControlAuto(), second_coordinate, block32_52.getControlAuto() == 1);
                listSignals.add(listSignals.getListReadyAuto(), second_coordinate, block32_52.getReadyAuto() == 1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_52.getTM() == 1);
                listSignals.add(listSignals.getListEPK(), second_coordinate, block32_52.getEPK() == 1);
                listSignals.add(listSignals.getListWhistleEPK(), second_coordinate, block32_52.getWhistleEPK() == 1);

                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_52.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_52.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_52.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_52.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_52.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_52.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_52.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_52.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_52.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_52.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_52.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_52.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_52.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_52.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_52.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_52.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_52.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_52.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_52.getKeyNine(), curTypeLoc, -1);
            }
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_52 block32_52 = new avpt.gr.blocks32.s4k.Block32_52(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltageEngine1_s1(), second_coordinate, block32_52.getVoltageEngine1_s1(), -1);
                listLines.add(listLines.getListVoltageEngine2_s1(), second_coordinate, block32_52.getVoltageEngine2_s1(), -1);
                listLines.add(listLines.getListVoltageEngine3_s1(), second_coordinate, block32_52.getVoltageEngine3_s1(), -1);
                listLines.add(listLines.getListVoltageEngine4_s1(), second_coordinate, block32_52.getVoltageEngine4_s1(), -1);

                listLines.add(listLines.getListAmperageAnchor1_s1(), second_coordinate, block32_52.getAmperageAnchor1_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s1(), second_coordinate, block32_52.getAmperageAnchor2_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s1(), second_coordinate, block32_52.getAmperageAnchor3_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s1(), second_coordinate, block32_52.getAmperageAnchor4_s1(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s1(), second_coordinate, block32_52.getAmperageExcitation1_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s1(), second_coordinate, block32_52.getAmperageExcitation2_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s1(), second_coordinate, block32_52.getAmperageExcitation3_s1(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s1(), second_coordinate, block32_52.getAmperageExcitation4_s1(), -1);

                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_52.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_52.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_52.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_52.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_52.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_52.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_52.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_52.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_52.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_52.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_52.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_52.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_52.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_52.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_52.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_52.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_52.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_52.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_52.getKeyNine(), curTypeLoc, -1);
            }
                break;
            case Train.S5K_2: {// 2ЭС5К, 3ЭС5К мсуд-15
                avpt.gr.blocks32.s5k_2.Block32_52 block32_52 = new avpt.gr.blocks32.s5k_2.Block32_52(arrBlock32.get(index).getValues());
                curTime = block32_52.getTime();
                addSecond(isTime);
                boolean isNewTrain;
                if (curTime_21 != null)
                    isNewTrain = arrTrains.addCurBr(index, second_coordinate, curTime_21);
                else
                    isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_52.getTime());
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listTasks.add(listTasks.getListKkm2(), second_coordinate, block32_52.getKKM(), curTypeLoc, -1);
            }
                break;
        }
    }

    private void handling0x53(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
            {
                avpt.gr.blocks32.s5k.Block32_53 block32_53 = new avpt.gr.blocks32.s5k.Block32_53(arrBlock32.get(index).getValues());
                // напряжение секция 2
                listLines.add(listLines.getListVoltageEngine1_s2(), second_coordinate, block32_53.getVoltageEngine1_s2(), -1);
                listLines.add(listLines.getListVoltageEngine2_s2(), second_coordinate, block32_53.getVoltageEngine2_s2(), -1);
                listLines.add(listLines.getListVoltageEngine3_s2(), second_coordinate, block32_53.getVoltageEngine3_s2(), -1);
                listLines.add(listLines.getListVoltageEngine4_s2(), second_coordinate, block32_53.getVoltageEngine4_s2(), -1);
                // ток секция 2
                listLines.add(listLines.getListAmperageAnchor1_s2(), second_coordinate, block32_53.getAmperageAnchor1_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s2(), second_coordinate, block32_53.getAmperageAnchor2_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s2(), second_coordinate, block32_53.getAmperageAnchor3_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s2(), second_coordinate, block32_53.getAmperageAnchor4_s2(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s2(), second_coordinate, block32_53.getAmperageExcitation1_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s2(), second_coordinate, block32_53.getAmperageExcitation2_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s2(), second_coordinate, block32_53.getAmperageExcitation3_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s2(), second_coordinate, block32_53.getAmperageExcitation4_s2(), -1);
                // сила
                if (power_brake_s5k < 0)
                    listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, power_brake_s5k, -1);                 // торможения
                else
                    listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, block32_53.getPowerLocomotive(), -1); // тяги
            }
                break;
            case Train.KZ8A: {// KZ8A
                avpt.gr.blocks32.kz8.Block32_53 block32_53 = new avpt.gr.blocks32.kz8.Block32_53(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage(), second_coordinate, block32_53.getVoltage(), -1);
                listLines.add(listLines.getListAmperageAnchor(), second_coordinate, block32_53.getAmperageAnchor(), -1);
                listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, block32_53.getPowerImplement(), -1); //
            }
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_53 block32_53 = new avpt.gr.blocks32.s4k.Block32_53(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltageEngine1_s2(), second_coordinate, block32_53.getVoltageEngine1_s2(), -1);
                listLines.add(listLines.getListVoltageEngine2_s2(), second_coordinate, block32_53.getVoltageEngine2_s2(), -1);
                listLines.add(listLines.getListVoltageEngine3_s2(), second_coordinate, block32_53.getVoltageEngine3_s2(), -1);
                listLines.add(listLines.getListVoltageEngine4_s2(), second_coordinate, block32_53.getVoltageEngine4_s2(), -1);

                listLines.add(listLines.getListAmperageAnchor1_s2(), second_coordinate, block32_53.getAmperageAnchor1_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s2(), second_coordinate, block32_53.getAmperageAnchor2_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s2(), second_coordinate, block32_53.getAmperageAnchor3_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s2(), second_coordinate, block32_53.getAmperageAnchor4_s2(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s2(), second_coordinate, block32_53.getAmperageExcitation1_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s2(), second_coordinate, block32_53.getAmperageExcitation2_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s2(), second_coordinate, block32_53.getAmperageExcitation3_s2(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s2(), second_coordinate, block32_53.getAmperageExcitation4_s2(), -1);
            }
                break;
            case Train.S5K_2: {// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_53 block32_53_2 = new avpt.gr.blocks32.s5k_2.Block32_53(arrBlock32.get(index).getValues());
                listLines.add(listLines.getS5k_listPowerImplementation(), second_coordinate, block32_53_2.getCommonPower(), -1); // тяги / торможения

                listSignals.add(listSignals.getListGV(), second_coordinate, block32_53_2.getGV() == 1);
                listSignals.add(listSignals.getListBox(), second_coordinate, block32_53_2.getBox() == 1);
                listSignals.add(listSignals.getListPressOver(), second_coordinate, block32_53_2.getPressOver() == 1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_53_2.getTM() == 1);
                listSignals.add(listSignals.getListEmergencyBrake(), second_coordinate, block32_53_2.getEmergencyBrake() == 1);
                listSignals.add(listSignals.getListAcceleratedBrake(), second_coordinate, block32_53_2.getAcceleratedBrake() == 1);
                listSignals.add(listSignals.getListReleaseBrake(), second_coordinate, block32_53_2.getReleaseBrake() == 1);
                listSignals.add(listSignals.getListSandAuto(), second_coordinate, block32_53_2.getSandAuto() == 1);
                //listSignals.add(listSignals.getListHandSpeedZero(), second_coordinate, block32_53_2.getHandleSpeedZero() == 1);

                listTasks.add(listTasks.getListCabin(), second_coordinate, block32_53_2.getNumCabin(), curTypeLoc, -1);
                listTasks.add(listTasks.getListMainControl(), second_coordinate, block32_53_2.getMainControl(), curTypeLoc, -1);
                listTasks.add(listTasks.getListRevControl(), second_coordinate, block32_53_2.getRevControl(), curTypeLoc, -1);
            }
                break;
        }
    }

    private void handling0x54(int index) {
        switch (curTypeLoc) {
            case VL80:// ВЛ80
            {
                avpt.gr.blocks32.vl80.Block32_54 block32_54 = new avpt.gr.blocks32.vl80.Block32_54(arrBlock32.get(index).getValues());
                curTime = block32_54.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_54.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listTasks.add(listTasks.getListALSN_vl80_1(), second_coordinate, block32_54.getALSN1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListALSN_vl80_2(), second_coordinate, block32_54.getALSN2(), curTypeLoc, -1);

                listLines.add(listLines.getListPressUR_s1(), second_coordinate, block32_54.getPressUR_s1(), -1);
                listLines.add(listLines.getListPressTM_s1(), second_coordinate, block32_54.getPressTM_s1(), -1);
                listLines.add(listLines.getListPressTC_s1(), second_coordinate, block32_54.getPressTC_s1(), -1);
                listLines.add(listLines.getListPressUR_s2(), second_coordinate, block32_54.getPressUR_s2(), -1);
                listLines.add(listLines.getListPressTM_s2(), second_coordinate, block32_54.getPressTM_s2(), -1);
                listLines.add(listLines.getListPressTC_s2(), second_coordinate, block32_54.getPressTC_s2(), -1);

                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_54.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_54.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_54.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_54.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_54.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_54.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_54.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_54.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_54.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_54.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_54.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_54.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_54.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_54.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_54.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_54.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_54.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_54.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_54.getKeyNine(), curTypeLoc, -1);
                //

                listSignals.add(listSignals.getListEmergencyBrake1(), second_coordinate, block32_54.getEmergencyBrake1() == 1);
                listSignals.add(listSignals.getListEmergencyBrake2(), second_coordinate, block32_54.getEmergencyBrake2() == 1);
                listSignals.add(listSignals.getListBox1(), second_coordinate, block32_54.getBox1() == 1);
                listSignals.add(listSignals.getListBox2(), second_coordinate, block32_54.getBox2() == 1);
                listSignals.add(listSignals.getListRP1(), second_coordinate, block32_54.getRP1() == 1);
                listSignals.add(listSignals.getListRP2(), second_coordinate, block32_54.getRP2() == 1);
                listSignals.add(listSignals.getListTD1(), second_coordinate, block32_54.getTD1() == 1);
                listSignals.add(listSignals.getListTD2(), second_coordinate, block32_54.getTD2() == 1);
                listSignals.add(listSignals.getListRZ1(), second_coordinate, block32_54.getRZ1() == 1);
                listSignals.add(listSignals.getListRZ2(), second_coordinate, block32_54.getRZ2() == 1);
                listSignals.add(listSignals.getListInterDriver1(), second_coordinate, block32_54.getInterDriver1() == 1);
                listSignals.add(listSignals.getListInterDriver2(), second_coordinate, block32_54.getInterDriver2() == 1);
                listSignals.add(listSignals.getListTM1(), second_coordinate, block32_54.getTM1() == 1);
                listSignals.add(listSignals.getListTM2(), second_coordinate, block32_54.getTM2() == 1);
                listSignals.add(listSignals.getListUKKNP1(), second_coordinate, block32_54.getUKKNP1() == 1);
                listSignals.add(listSignals.getListUKKNP2(), second_coordinate, block32_54.getUKKNP2() == 1);
                listSignals.add(listSignals.getListGV1(), second_coordinate, block32_54.getGV1() == 1);
                listSignals.add(listSignals.getListGV2(), second_coordinate, block32_54.getGV2() == 1);
                listSignals.add(listSignals.getListPPV1(), second_coordinate, block32_54.getPPV1() == 1);
                listSignals.add(listSignals.getListPPV2(), second_coordinate, block32_54.getPPV2() == 1);
            }
            break;
            case VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_54 block32_54 = new avpt.gr.blocks32.s5k.Block32_54(arrBlock32.get(index).getValues());
                // напряжение секция 4
                listLines.add(listLines.getListVoltageEngine1_s4(), second_coordinate, block32_54.getVoltageEngine1_s4(), -1);
                listLines.add(listLines.getListVoltageEngine2_s4(), second_coordinate, block32_54.getVoltageEngine2_s4(), -1);
                listLines.add(listLines.getListVoltageEngine3_s4(), second_coordinate, block32_54.getVoltageEngine3_s4(), -1);
                listLines.add(listLines.getListVoltageEngine4_s4(), second_coordinate, block32_54.getVoltageEngine4_s4(), -1);
                // ток секция 4
                listLines.add(listLines.getListAmperageAnchor1_s4(), second_coordinate, block32_54.getAmperageAnchor1_s4(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s4(), second_coordinate, block32_54.getAmperageAnchor2_s4(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s4(), second_coordinate, block32_54.getAmperageAnchor3_s4(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s4(), second_coordinate, block32_54.getAmperageAnchor4_s4(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s4(), second_coordinate, block32_54.getAmperageExcitation1_s4(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s4(), second_coordinate, block32_54.getAmperageExcitation2_s4(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s4(), second_coordinate, block32_54.getAmperageExcitation3_s4(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s4(), second_coordinate, block32_54.getAmperageExcitation4_s4(), -1);
            }
                break;
            case KZ8A: {// KZ8A
                avpt.gr.blocks32.kz8.Block32_54 block32_54 = new avpt.gr.blocks32.kz8.Block32_54(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListSpeed_BUT(), second_coordinate, block32_54.getSpeedBoot(), -1);
                listLines.add(listLines.getListSpeed_CLUB(), second_coordinate, block32_54.getSpeedClub(), -1);
            }
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_54 block32_54 = new avpt.gr.blocks32.s5.Block32_54(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListPressUR(), second_coordinate, block32_54.getPressUR(), -1);
                listLines.add(listLines.getListPressTM(), second_coordinate, block32_54.getPressTM(), -1);
                listLines.add(listLines.getListPressTC(), second_coordinate, block32_54.getPressTC(), -1);
                listLines.add(listLines.getListPressNM(), second_coordinate, block32_54.getPressNM(), -1);

                listSignals.add(listSignals.getListKAET(), second_coordinate, block32_54.getKAET() == 1);
                listTasks.add(listTasks.getListPneumatic1(), second_coordinate, block32_54.getPneumaticState_s1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListPneumaticStatusBlk(), second_coordinate, block32_54.getStatusBlock(), curTypeLoc, -1);

                listTasks.add(listTasks.getListKkm_s5(), second_coordinate, block32_54.getKKM(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkbt_s5(), second_coordinate, block32_54.getKKBT(), curTypeLoc, -1);
            }
                break;
            case S6: {// ЭС6
                avpt.gr.blocks32.s6.Block32_54 block32_54 = new avpt.gr.blocks32.s6.Block32_54(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListPressUR(), second_coordinate, block32_54.getPressUR(), -1);
                listLines.add(listLines.getListPressTM(), second_coordinate, block32_54.getPressTM(), -1);
                listLines.add(listLines.getListPressTC(), second_coordinate, block32_54.getPressTC(), -1);
                listLines.add(listLines.getListPressNM(), second_coordinate, block32_54.getPressNM(), -1);
                listLines.add(listLines.getListSpeed_BS_DPS(), second_coordinate, block32_54.getSpeed_BS_DPS(), -1);
                listTasks.add(listTasks.getListPneumatic1(), second_coordinate, block32_54.getPneumaticState_s1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListPneumaticStatusBlk(), second_coordinate, block32_54.getPneumaticStatusBlock(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKkm_s5(), second_coordinate, block32_54.getKKM(), curTypeLoc, -1);
            }
                break;
            case S4K: // 2ЭС4К, 3ЭС4К
                break;
            case S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_54 block32_54_2 = new avpt.gr.blocks32.s5k_2.Block32_54(arrBlock32.get(index).getValues());
                // напряжение секция main
                listLines.add(listLines.getListVoltageEngine1_s1(), second_coordinate, block32_54_2.getVoltage1_main(), -1);
                listLines.add(listLines.getListVoltageEngine2_s1(), second_coordinate, block32_54_2.getVoltage2_main(), -1);
                listLines.add(listLines.getListVoltageEngine3_s1(), second_coordinate, block32_54_2.getVoltage2_main(), -1);
                listLines.add(listLines.getListVoltageEngine4_s1(), second_coordinate, block32_54_2.getVoltage2_main(), -1);
                // ток секция main
                listLines.add(listLines.getListAmperageAnchor1_s1(), second_coordinate, block32_54_2.getAmperageAnchor1_main(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s1(), second_coordinate, block32_54_2.getAmperageAnchor2_main(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s1(), second_coordinate, block32_54_2.getAmperageAnchor3_main(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s1(), second_coordinate, block32_54_2.getAmperageAnchor4_main(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s1(), second_coordinate, block32_54_2.getAmperageExcitation_main(), -1);

                listTasks.add(listTasks.getListSchema(), second_coordinate, block32_54_2.getSchema(), curTypeLoc, -1);

                listSignals.add(listSignals.getListTed1_1s(), second_coordinate, block32_54_2.getShutdownTED1() == 1);
                listSignals.add(listSignals.getListTed2_1s(), second_coordinate, block32_54_2.getShutdownTED2() == 1);
                listSignals.add(listSignals.getListTed3_1s(), second_coordinate, block32_54_2.getShutdownTED3() == 1);
                listSignals.add(listSignals.getListTed4_1s(), second_coordinate, block32_54_2.getShutdownTED4() == 1);
                break;
        }
    }

    private void handling0x55(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_55 block32_55 = new avpt.gr.blocks32.s5.Block32_55(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListSpeed_BLOCK(), second_coordinate, block32_55.getBlock(), -1);
                listTasks.add(listTasks.getListPneumaticSrcTask(), second_coordinate, block32_55.getPneumaticSrcTask(), curTypeLoc, -1);
            }
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: // 2ЭС4К, 3ЭС4К
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_55 block32_55_2 = new avpt.gr.blocks32.s5k_2.Block32_55(arrBlock32.get(index).getValues());
                // напряжение секция 2
                listLines.add(listLines.getListVoltageEngine1_s2(), second_coordinate, block32_55_2.getVoltage1_s1(), -1);
                listLines.add(listLines.getListVoltageEngine2_s2(), second_coordinate, block32_55_2.getVoltage2_s1(), -1);
                listLines.add(listLines.getListVoltageEngine3_s2(), second_coordinate, block32_55_2.getVoltage2_s1(), -1);
                listLines.add(listLines.getListVoltageEngine4_s2(), second_coordinate, block32_55_2.getVoltage2_s1(), -1);
                // ток секция main
                listLines.add(listLines.getListAmperageAnchor1_s2(), second_coordinate, block32_55_2.getAmperageAnchor1_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s2(), second_coordinate, block32_55_2.getAmperageAnchor2_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s2(), second_coordinate, block32_55_2.getAmperageAnchor3_s1(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s2(), second_coordinate, block32_55_2.getAmperageAnchor4_s1(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s2(), second_coordinate, block32_55_2.getAmperageExcitation_s1(), -1);

                listSignals.add(listSignals.getListTed1_2s(), second_coordinate, block32_55_2.getShutdownTED1_s1() == 1);
                listSignals.add(listSignals.getListTed2_2s(), second_coordinate, block32_55_2.getShutdownTED2_s1() == 1);
                listSignals.add(listSignals.getListTed3_2s(), second_coordinate, block32_55_2.getShutdownTED3_s1() == 1);
                listSignals.add(listSignals.getListTed4_2s(), second_coordinate, block32_55_2.getShutdownTED4_s1() == 1);
                break;
        }
    }

    private void handling0x57(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_57 block32_57 = new avpt.gr.blocks32.s5k.Block32_57(arrBlock32.get(index).getValues());
                voltageK_s5k = block32_57.getVoltageK_s5k();
                amperageK_s5k = block32_57.getAmperageK_s5k();
                // добавляем расход энергии 1 секция
                if (block32_57_s5k_prev != null) {
                    double val = getConsumptionEnergy(block32_57_s5k_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1(), listLines.getListConsumptionEn_s1());
                    listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_57.getEnergyAct_s1(), -1);
                    val = getConsumptionEnergy(block32_57_s5k_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1(), listLines.getListConsumptionEn_r_s1());
                    listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_57.getEnergyActRec_s1(), -1);
                }
                block32_57_s5k_prev = block32_57;
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_57 block32_57 = new avpt.gr.blocks32.s5.Block32_57(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage(), second_coordinate, block32_57.getVoltage(), -1);
                listLines.add(listLines.getListAmperage(), second_coordinate, block32_57.getAmperage(), -1);

                // добавляем расход энергии 1 секция
                if (block32_57_s5_prev != null) {
                    double val = getConsumptionEnergy(block32_57_s5_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1(), listLines.getListConsumptionEn_s1());
                    listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_57.getEnergyAct_s1(), -1);
                    val = getConsumptionEnergy(block32_57_s5_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1(), listLines.getListConsumptionEn_r_s1());
                    listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_57.getEnergyActRec_s1(), -1);
                }
                block32_57_s5_prev = block32_57;
            }
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_57 block32_57 = new avpt.gr.blocks32.s4k.Block32_57(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage_s1(), second_coordinate, block32_57.getRmsVoltage_s1() * voltageK_s4k, -1);
                listLines.add(listLines.getListAmperage_s1(), second_coordinate, block32_57.getRmsAmperage_s1() * amperageK_s5k, -1);

                // расход энергии 1 секция
                if (block32_57_s4k_prev != null) {
                    double val = getConsumptionEnergy(block32_57_s4k_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1(), listLines.getListConsumptionEn_s1());
                    listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_57.getEnergyAct_s1(), -1);
                    val = getConsumptionEnergy(block32_57_s4k_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1(), listLines.getListConsumptionEn_r_s1());
                    listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_57.getEnergyActRec_s1(), -1);
                }
                block32_57_s4k_prev = block32_57;
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_57 block32_57_2 = new avpt.gr.blocks32.s5k_2.Block32_57(arrBlock32.get(index).getValues());
                // добавляем расход энергии 1 секция
                if (block32_57_s5k_2_prev != null) {
                    double val = getConsumptionEnergy(block32_57_s5k_2_prev.getEnergyAct_s1(), block32_57_2.getEnergyAct_s1(), listLines.getListConsumptionEn_s1());
                    listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_57_2.getEnergyAct_s1(), -1);
                    val = getConsumptionEnergy(block32_57_s5k_2_prev.getEnergyActRec_s1(), block32_57_2.getEnergyActRec_s1(), listLines.getListConsumptionEn_r_s1());
                    listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_57_2.getEnergyActRec_s1(), -1);
                }
                block32_57_s5k_2_prev = block32_57_2;
                break;
        }
    }

    private void handling0x58(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_58 block32_58 = new avpt.gr.blocks32.s5k.Block32_58(arrBlock32.get(index).getValues());
                // добавляем расход энергии 2 секция
                if (block32_58_s5k_prev != null) {
                    double val = getConsumptionEnergy(block32_58_s5k_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2(), listLines.getListConsumptionEn_s2());
                    listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_58.getEnergyAct_s2(), -1);
                    val = getConsumptionEnergy(block32_58_s5k_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2(), listLines.getListConsumptionEn_r_s2());
                    listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_58.getEnergyActRec_s2(), -1);
                }
                block32_58_s5k_prev = block32_58;
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_58 block32_58 = new avpt.gr.blocks32.s5.Block32_58(arrBlock32.get(index).getValues());
                // расход энергии 2 секция
                if (block32_58_s5_prev != null) {
                    double val = getConsumptionEnergy(block32_58_s5_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2(), listLines.getListConsumptionEn_s2());
                    listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_58.getEnergyAct_s2(), -1);
                    val = getConsumptionEnergy(block32_58_s5_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2(), listLines.getListConsumptionEn_r_s2());
                    listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_58.getEnergyActRec_s2(), -1);
                }
                block32_58_s5_prev = block32_58;
            }
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_58 block32_58 = new avpt.gr.blocks32.s4k.Block32_58(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage_s2(), second_coordinate, block32_58.getRmsVoltage_s2() * voltageK_s4k, -1);
                listLines.add(listLines.getListAmperage_s2(), second_coordinate, block32_58.getRmsAmperage_s2() * amperageK_s5k, -1);

                // расход энергии 2 секция
                if (block32_58_s4k_prev != null) {
                    double val = getConsumptionEnergy(block32_58_s4k_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2(), listLines.getListConsumptionEn_s2());
                    listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_58.getEnergyAct_s2(), -1);
                    val = getConsumptionEnergy(block32_58_s4k_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2(), listLines.getListConsumptionEn_r_s2());
                    listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_58.getEnergyActRec_s2(), -1);
                }
                block32_58_s4k_prev = block32_58;
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_58 block32_58_2 = new avpt.gr.blocks32.s5k_2.Block32_58(arrBlock32.get(index).getValues());
                // добавляем расход энергии 2 секция
                if (block32_58_s5k_2_prev != null) {
                    double val = getConsumptionEnergy(block32_58_s5k_2_prev.getEnergyAct_s2(), block32_58_2.getEnergyAct_s2(), listLines.getListConsumptionEn_s2());
                    listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_58_2.getEnergyAct_s2(), -1);
                    val = getConsumptionEnergy(block32_58_s5k_2_prev.getEnergyActRec_s2(), block32_58_2.getEnergyActRec_s2(), listLines.getListConsumptionEn_r_s2());
                    listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_58_2.getEnergyActRec_s2(), -1);
                }
                block32_58_s5k_2_prev = block32_58_2;
                break;
        }
    }

    private void handling0x59(int index) {
        switch (curTypeLoc) {
            case Train.VL80: {// ВЛ80
                avpt.gr.blocks32.vl80.Block32_59  block32_59 = new avpt.gr.blocks32.vl80.Block32_59(arrBlock32.get(index).getValues());
                if (block32_59_vl80_prev != null) {
                    double val = getConsumptionEnergy(block32_59_vl80_prev.getEnergyAct_s1(), block32_59.getEnergyAct_s1(), listLines.getListConsumptionEn_s1());
                    listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s1(), second_coordinate, block32_59.getEnergyAct_s1(), -1);
                    val = getConsumptionEnergy(block32_59_vl80_prev.getEnergyAct_s2(), block32_59.getEnergyAct_s2(), listLines.getListConsumptionEn_s2());
                    listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s2(), second_coordinate, block32_59.getEnergyAct_s2(), -1);
                    val = getConsumptionEnergy(block32_59_vl80_prev.getEnergyActRec_s1(), block32_59.getEnergyActRec_s1(), listLines.getListConsumptionEn_r_s1());
                    listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s1(), second_coordinate, block32_59.getEnergyActRec_s1(), -1);
                    val = getConsumptionEnergy(block32_59_vl80_prev.getEnergyActRec_s2(), block32_59.getEnergyActRec_s2(), listLines.getListConsumptionEn_r_s2());
                    listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s2(), second_coordinate, block32_59.getEnergyActRec_s2(), -1);
                }
                block32_59_vl80_prev = block32_59;
                // напряжение контактной сети 1 секции
                listLines.add(listLines.getListVoltage_s1(), second_coordinate, block32_59.getVoltage_s1(), -1);
                // общий ток 1 секции
                listLines.add(listLines.getListAmperage_s1(), second_coordinate, block32_59.getAmperage_s1(), -1);
            }
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_59 block32_59 = new avpt.gr.blocks32.s5k.Block32_59(arrBlock32.get(index).getValues());
                // добавляем расход энергии 3 секция
                if (block32_59_s5k_prev != null) {
                    double val = getConsumptionEnergy(block32_59_s5k_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3(), listLines.getListConsumptionEn_s3());
                    listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_59.getEnergyAct_s3(), -1);
                    val = getConsumptionEnergy(block32_59_s5k_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3(), listLines.getListConsumptionEn_r_s3());
                    listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s3(), second_coordinate, block32_59.getEnergyActRec_s3(), -1);
                }
                block32_59_s5k_prev = block32_59;
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_59 block32_59 = new avpt.gr.blocks32.s5.Block32_59(arrBlock32.get(index).getValues());
                // добавляем расход энергии 3 секция
                if (block32_59_s5_prev != null) {
                    double val = getConsumptionEnergy(block32_59_s5_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3(), listLines.getListConsumptionEn_s3());
                    listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_59.getEnergyAct_s3(), -1);
                    val = getConsumptionEnergy(block32_59_s5_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3(), listLines.getListConsumptionEn_r_s3());
                    listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s3(), second_coordinate, block32_59.getEnergyActRec_s3(), -1);
                }
                block32_59_s5_prev = block32_59;
            }
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_59 block32_59 = new avpt.gr.blocks32.s4k.Block32_59(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage_s3(), second_coordinate, block32_59.getRmsVoltage_s3() * voltageK_s4k, -1);
                listLines.add(listLines.getListAmperage_s3(), second_coordinate, block32_59.getRmsAmperage_s3() * amperageK_s5k, -1);

                // добавляем расход энергии 3 секция
                if (block32_59_s4k_prev != null) {
                    double val = getConsumptionEnergy(block32_59_s4k_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3(), listLines.getListConsumptionEn_s3());
                    listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_59.getEnergyAct_s3(), -1);
                    val = getConsumptionEnergy(block32_59_s4k_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3(), listLines.getListConsumptionEn_r_s3());
                    listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s3(), second_coordinate, block32_59.getEnergyActRec_s3(), -1);
                }
                block32_59_s4k_prev = block32_59;
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k_2.Block32_59 block32_59_2 = new avpt.gr.blocks32.s5k_2.Block32_59(arrBlock32.get(index).getValues());
                // добавляем расход энергии 3 секция
                if (block32_59_s5k_2_prev != null) {
                    double val = getConsumptionEnergy(block32_59_s5k_2_prev.getEnergyAct_s3(), block32_59_2.getEnergyAct_s3(), listLines.getListConsumptionEn_s3());
                    listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s3(), second_coordinate, block32_59_2.getEnergyAct_s3(), -1);
                    val = getConsumptionEnergy(block32_59_s5k_2_prev.getEnergyActRec_s3(), block32_59_2.getEnergyActRec_s3(), listLines.getListConsumptionEn_r_s3());
                    listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s3(), second_coordinate, block32_59_2.getEnergyActRec_s3(), -1);
                }
                block32_59_s5k_2_prev = block32_59_2;
                break;
        }
    }

    private void handling0x5A(int index) {
        switch (curTypeLoc) {
            case Train.VL80: {// ВЛ80
                avpt.gr.blocks32.vl80.Block32_5A  block32_5A = new avpt.gr.blocks32.vl80.Block32_5A(arrBlock32.get(index).getValues());
                // напряжение контактной сети 2 секции
                listLines.add(listLines.getListVoltage_s2(), second_coordinate, block32_5A.getVoltage_s2(), -1);
                // общий ток 2 секции
                listLines.add(listLines.getListAmperage_s2(), second_coordinate, block32_5A.getAmperage_s2(), -1);
            }
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_5A block32_5A = new avpt.gr.blocks32.s5k.Block32_5A(arrBlock32.get(index).getValues());
                // добавляем расход энергии 4 секция
                if (block32_5A_s5k_prev != null) {
                    double val = getConsumptionEnergy(block32_5A_s5k_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4(), listLines.getListConsumptionEn_s4());
                    listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_5A.getEnergyAct_s4(), -1);
                    val = getConsumptionEnergy(block32_5A_s5k_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4(), listLines.getListConsumptionEn_r_s4());
                    listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s4(), second_coordinate, block32_5A.getEnergyActRec_s4(), -1);
                }
                block32_5A_s5k_prev = block32_5A;
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5: {// 2ЭС5
                avpt.gr.blocks32.s5.Block32_5A block32_5A = new avpt.gr.blocks32.s5.Block32_5A(arrBlock32.get(index).getValues());
                // добавляем расход энергии 4 секция
                if (block32_5A_s5_prev != null) {
                    double val = getConsumptionEnergy(block32_5A_s5_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4(), listLines.getListConsumptionEn_s4());
                    listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_5A.getEnergyAct_s4(), -1);
                    val = getConsumptionEnergy(block32_5A_s5_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4(), listLines.getListConsumptionEn_r_s4());
                    listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s4(), second_coordinate, block32_5A.getEnergyActRec_s4(), -1);
                }
                block32_5A_s5_prev = block32_5A;
            }
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_5A block32_5A = new avpt.gr.blocks32.s4k.Block32_5A(arrBlock32.get(index).getValues());
                // добавляем расход энергии 4 секция
                if (block32_5A_s4k_prev != null) {
                    double val = getConsumptionEnergy(block32_5A_s4k_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4(), listLines.getListConsumptionEn_s4());
                    listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_5A.getEnergyAct_s4(), -1);
                    val = getConsumptionEnergy(block32_5A_s4k_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4(), listLines.getListConsumptionEn_r_s4());
                    listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s4(), second_coordinate, block32_5A.getEnergyActRec_s4(), -1);
                }
                block32_5A_s4k_prev = block32_5A;
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_5A block32_5A_2 = new avpt.gr.blocks32.s5k_2.Block32_5A(arrBlock32.get(index).getValues());
                // добавляем расход энергии 3 секция
                if (block32_5A_s5k_2_prev != null) {
                    double val = getConsumptionEnergy(block32_5A_s5k_2_prev.getEnergyAct_s4(), block32_5A_2.getEnergyAct_s4(), listLines.getListConsumptionEn_s4());
                    listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_s4(), second_coordinate, block32_5A_2.getEnergyAct_s4(), -1);
                    val = getConsumptionEnergy(block32_5A_s5k_2_prev.getEnergyActRec_s4(), block32_5A_2.getEnergyActRec_s4(), listLines.getListConsumptionEn_r_s4());
                    listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, val, -1);
                    listLines.add(listLines.getListCntEn_r_s4(), second_coordinate, block32_5A_2.getEnergyActRec_s4(), -1);
                }
                block32_5A_s5k_2_prev = block32_5A_2;
                break;
        }
    }

    private void handling0x5B(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_5B block32_5B = new avpt.gr.blocks32.s5k.Block32_5B(arrBlock32.get(index).getValues());
                // напряжение контактной сети 1, 2 секции
                listLines.add(listLines.getListVoltage_s1(), second_coordinate, block32_5B.getRmsVoltage_s1() * voltageK_s5k, -1);
                listLines.add(listLines.getListVoltage_s2(), second_coordinate, block32_5B.getRmsVoltage_s2() * voltageK_s5k, -1);
                // общий ток 1, 2 секции
                listLines.add(listLines.getListAmperage_s1(), second_coordinate, block32_5B.getRmsAmperage_s1() * amperageK_s5k, -1);
                listLines.add(listLines.getListAmperage_s2(), second_coordinate, block32_5B.getRmsAmperage_s2() * amperageK_s5k, -1);
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: // 2ЭС4К, 3ЭС4К
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_5B block32_5B_2 = new avpt.gr.blocks32.s5k_2.Block32_5B(arrBlock32.get(index).getValues());
                // напряжение контактной сети 1, 2 секции
                listLines.add(listLines.getListVoltage_s1(), second_coordinate, block32_5B_2.getRmsVoltage_s1() * voltageK_s5k, -1);
                listLines.add(listLines.getListVoltage_s2(), second_coordinate, block32_5B_2.getRmsVoltage_s2() * voltageK_s5k, -1);
                // общий ток 1, 2 секции
                listLines.add(listLines.getListAmperage_s1(), second_coordinate, block32_5B_2.getRmsAmperage_s1() * amperageK_s5k, -1);
                listLines.add(listLines.getListAmperage_s2(), second_coordinate, block32_5B_2.getRmsAmperage_s2() * amperageK_s5k, -1);
                break;
        }
    }

    private void handling0x5C(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_5C block32_5C = new avpt.gr.blocks32.s5k.Block32_5C(arrBlock32.get(index).getValues());
                // напряжение секция 3
                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C.getVoltageEngine1_s3(), -1);
                listLines.add(listLines.getListVoltageEngine2_s3(), second_coordinate, block32_5C.getVoltageEngine2_s3(), -1);
                listLines.add(listLines.getListVoltageEngine3_s3(), second_coordinate, block32_5C.getVoltageEngine3_s3(), -1);
                listLines.add(listLines.getListVoltageEngine4_s3(), second_coordinate, block32_5C.getVoltageEngine4_s3(), -1);
                // ток секция 3
                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C.getAmperageAnchor1_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s3(), second_coordinate, block32_5C.getAmperageAnchor2_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s3(), second_coordinate, block32_5C.getAmperageAnchor3_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s3(), second_coordinate, block32_5C.getAmperageAnchor4_s3(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C.getAmperageExcitation1_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s3(), second_coordinate, block32_5C.getAmperageExcitation2_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s3(), second_coordinate, block32_5C.getAmperageExcitation3_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s3(), second_coordinate, block32_5C.getAmperageExcitation4_s3(), -1);

//                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C.getVoltageEngine1_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine2_s3(), second, block32_5C.getVoltageEngine2_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine3_s3(), second, block32_5C.getVoltageEngine3_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine4_s3(), second, block32_5C.getVoltageEngine4_s3(), -1);
//                // ток секция 3
//                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C.getAmperageAnchor1_s3(), -1);
//                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C.getAmperageExcitation1_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor2_s3(), second, block32_5C.getAmperageAnchor2_s3(), -1);
////                listLines.add(listLines.getListAmperageExcitation2_s3(), second, block32_5C.getAmperageExcitation2_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor3_s3(), second, block32_5C.getAmperageAnchor3_s3(), -1);
////                listLines.add(listLines.getListAmperageExcitation3_s3(), second, block32_5C.getAmperageExcitation3_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor4_s3(), second, block32_5C.getAmperageAnchor4_s3(), -1);
////                listLines.add(listLines.getListAmperageExcitation4_s3(), second, block32_5C.getAmperageExcitation4_s3(), -1);
                // сила торможения
                power_brake_s5k = block32_5C.getPowerBrake();
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_5C block32_5C = new avpt.gr.blocks32.s4k.Block32_5C(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C.getVoltageEngine1_s3(), -1);
                listLines.add(listLines.getListVoltageEngine2_s3(), second_coordinate, block32_5C.getVoltageEngine2_s3(), -1);
                listLines.add(listLines.getListVoltageEngine3_s3(), second_coordinate, block32_5C.getVoltageEngine3_s3(), -1);
                listLines.add(listLines.getListVoltageEngine4_s3(), second_coordinate, block32_5C.getVoltageEngine4_s3(), -1);

                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C.getAmperageAnchor1_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s3(), second_coordinate, block32_5C.getAmperageAnchor2_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s3(), second_coordinate, block32_5C.getAmperageAnchor3_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s3(), second_coordinate, block32_5C.getAmperageAnchor4_s3(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C.getAmperageExcitation1_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation2_s3(), second_coordinate, block32_5C.getAmperageExcitation2_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation3_s3(), second_coordinate, block32_5C.getAmperageExcitation3_s3(), -1);
                listLines.add(listLines.getListAmperageExcitation4_s3(), second_coordinate, block32_5C.getAmperageExcitation4_s3(), -1);

//                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C.getVoltageEngine1_s3(), -1);
//                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C.getAmperageAnchor1_s3(), -1);
//                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C.getAmperageExcitation1_s3(), -1);
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_5C block32_5C_2 = new avpt.gr.blocks32.s5k_2.Block32_5C(arrBlock32.get(index).getValues());
                // напряжение секция 3
                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C_2.getVoltage1_s2(), -1);
                listLines.add(listLines.getListVoltageEngine2_s3(), second_coordinate, block32_5C_2.getVoltage2_s2(), -1);
                listLines.add(listLines.getListVoltageEngine3_s3(), second_coordinate, block32_5C_2.getVoltage2_s2(), -1);
                listLines.add(listLines.getListVoltageEngine4_s3(), second_coordinate, block32_5C_2.getVoltage2_s2(), -1);
                // ток секция 3
                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C_2.getAmperageAnchor1_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s3(), second_coordinate, block32_5C_2.getAmperageAnchor2_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s3(), second_coordinate, block32_5C_2.getAmperageAnchor3_s2(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s3(), second_coordinate, block32_5C_2.getAmperageAnchor4_s2(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C_2.getAmperageExcitation_s2(), -1);

//                listLines.add(listLines.getListVoltageEngine1_s3(), second_coordinate, block32_5C_2.getVoltage1_s2(), -1);
////                listLines.add(listLines.getListVoltageEngine2_s3(), second, block32_5C_2.getVoltage2_s2(), -1);
////                listLines.add(listLines.getListVoltageEngine3_s3(), second, block32_5C_2.getVoltage3_s2(), -1);
////                listLines.add(listLines.getListVoltageEngine4_s3(), second, block32_5C_2.getVoltage4_s2(), -1);
//                // ток секция 3
//                listLines.add(listLines.getListAmperageAnchor1_s3(), second_coordinate, block32_5C_2.getAmperageAnchor1_s2(), -1);
//                listLines.add(listLines.getListAmperageExcitation1_s3(), second_coordinate, block32_5C_2.getAmperageExcitation_s2(), -1);
//                listLines.add(listLines.getListAmperageAnchor2_s3(), second, block32_5C_2.getAmperageAnchor2_s2(), -1);
//                listLines.add(listLines.getListAmperageAnchor3_s3(), second, block32_5C_2.getAmperageAnchor3_s2(), -1);
//                listLines.add(listLines.getListAmperageAnchor4_s3(), second, block32_5C_2.getAmperageAnchor4_s2(), -1);
                listSignals.add(listSignals.getListTed1_3s(), second_coordinate, block32_5C_2.getShutdownTED1_s2() == 1);
                listSignals.add(listSignals.getListTed2_3s(), second_coordinate, block32_5C_2.getShutdownTED2_s2() == 1);
                listSignals.add(listSignals.getListTed3_3s(), second_coordinate, block32_5C_2.getShutdownTED3_s2() == 1);
                listSignals.add(listSignals.getListTed4_3s(), second_coordinate, block32_5C_2.getShutdownTED4_s2() == 1);
                break;
        }
    }

    private void handling0x5D(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K:// 2ЭС5К, 3ЭС5К
                // напряжене контактной сети 3, 4 секции
                avpt.gr.blocks32.s5k.Block32_5D block32_5D = new avpt.gr.blocks32.s5k.Block32_5D(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage_s3(), second_coordinate, block32_5D.getRmsVoltage_s3() * voltageK_s5k, -1);
                listLines.add(listLines.getListVoltage_s4(), second_coordinate, block32_5D.getRmsVoltage_s4() * voltageK_s5k, -1);
                // общий ток 3, 4 секции
                listLines.add(listLines.getListAmperage_s3(), second_coordinate, block32_5D.getRmsAmperage_s3() * amperageK_s5k, -1);
                listLines.add(listLines.getListAmperage_s4(), second_coordinate, block32_5D.getRmsAmperage_s4() * amperageK_s5k, -1);
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: // 2ЭС4К, 3ЭС4К
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                // напряжене контактной сети 3, 4 секции
                avpt.gr.blocks32.s5k_2.Block32_5D block32_5D_2 = new avpt.gr.blocks32.s5k_2.Block32_5D(arrBlock32.get(index).getValues());
                listLines.add(listLines.getListVoltage_s3(), second_coordinate, block32_5D_2.getRmsVoltage_s3() * voltageK_s5k, -1);
                listLines.add(listLines.getListVoltage_s4(), second_coordinate, block32_5D_2.getRmsVoltage_s4() * voltageK_s5k, -1);
                // общий ток 3, 4 секции
                listLines.add(listLines.getListAmperage_s3(), second_coordinate, block32_5D_2.getRmsAmperage_s3() * amperageK_s5k, -1);
                listLines.add(listLines.getListAmperage_s4(), second_coordinate, block32_5D_2.getRmsAmperage_s4() * amperageK_s5k, -1);
                break;
        }
    }

    private void handling0x5E(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_5E block32_5E = new avpt.gr.blocks32.s5k.Block32_5E(arrBlock32.get(index).getValues());
                addSecond(isTime);
                curTime = block32_5E.getTime();
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_5E.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);


                listLines.add(listLines.getListPressPM(), second_coordinate, block32_5E.getPressPM(), -1);
                listLines.add(listLines.getS5k_listSpeed_MSUD(), second_coordinate, block32_5E.getSpeedMSUD(), -1);
                listTasks.add(listTasks.getListKkm(), second_coordinate, block32_5E.getKKM(), curTypeLoc, -1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_5E.getTM() == 1);
                listSignals.add(listSignals.getListPst(), second_coordinate, block32_5E.getKeyPst() == 1);
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_5E block32_5E = new avpt.gr.blocks32.s4k.Block32_5E(arrBlock32.get(index).getValues());
                curTime = block32_5E.getTime();
                addSecond(isTime);
                boolean isNewTrain = arrTrains.addCurBr(index, second_coordinate, block32_5E.getTime());
                // сброс счетчиков энергии на начало поезда
                if (isNewTrain) resetEnergy();
                listBlockBySeconds.add(index);

                listLines.add(listLines.getListPressPM(), second_coordinate, block32_5E.getPressPM(), -1);
                listLines.add(listLines.getS5k_listSpeed_MSUD(), second_coordinate, block32_5E.getSpeedMSUD(), -1);
                listTasks.add(listTasks.getListKkm2(), second_coordinate, block32_5E.getKKM(), curTypeLoc, -1);
            }
                break;
            case Train.S5K_2: {// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_5E block32_5E_2 = new avpt.gr.blocks32.s5k_2.Block32_5E(arrBlock32.get(index).getValues());
                // напряжение секция 4
                listLines.add(listLines.getListVoltageEngine1_s4(), second_coordinate, block32_5E_2.getVoltage1_s3(), -1);
                listLines.add(listLines.getListVoltageEngine2_s4(), second_coordinate, block32_5E_2.getVoltage2_s3(), -1);
                listLines.add(listLines.getListVoltageEngine3_s4(), second_coordinate, block32_5E_2.getVoltage2_s3(), -1);
                listLines.add(listLines.getListVoltageEngine4_s4(), second_coordinate, block32_5E_2.getVoltage2_s3(), -1);
                // ток секция 4
                listLines.add(listLines.getListAmperageAnchor1_s4(), second_coordinate, block32_5E_2.getAmperageAnchor1_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor2_s4(), second_coordinate, block32_5E_2.getAmperageAnchor2_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor3_s4(), second_coordinate, block32_5E_2.getAmperageAnchor3_s3(), -1);
                listLines.add(listLines.getListAmperageAnchor4_s4(), second_coordinate, block32_5E_2.getAmperageAnchor4_s3(), -1);

                listLines.add(listLines.getListAmperageExcitation1_s4(), second_coordinate, block32_5E_2.getAmperageExcitation_s3(), -1);

//                // напряжение секция 3
//                listLines.add(listLines.getListVoltageEngine1_s4(), second_coordinate, block32_5E_2.getVoltage1_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine2_s4(), second, block32_5E_2.getVoltage2_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine3_s4(), second, block32_5E_2.getVoltage3_s3(), -1);
////                listLines.add(listLines.getListVoltageEngine4_s4(), second, block32_5E_2.getVoltage4_s3(), -1);
//                // ток секция 3
//                listLines.add(listLines.getListAmperageAnchor1_s4(), second_coordinate, block32_5E_2.getAmperageAnchor1_s3(), -1);
//                listLines.add(listLines.getListAmperageExcitation1_s4(), second_coordinate, block32_5E_2.getAmperageExcitation_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor2_s4(), second, block32_5E_2.getAmperageAnchor2_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor3_s4(), second, block32_5E_2.getAmperageAnchor3_s3(), -1);
////                listLines.add(listLines.getListAmperageAnchor4_s4(), second, block32_5E_2.getAmperageAnchor4_s3(), -1);
                listSignals.add(listSignals.getListTed1_4s(), second_coordinate, block32_5E_2.getShutdownTED1_s3() == 1);
                listSignals.add(listSignals.getListTed2_4s(), second_coordinate, block32_5E_2.getShutdownTED2_s3() == 1);
                listSignals.add(listSignals.getListTed3_4s(), second_coordinate, block32_5E_2.getShutdownTED3_s3() == 1);
                listSignals.add(listSignals.getListTed4_4s(), second_coordinate, block32_5E_2.getShutdownTED4_s3() == 1);

//                listBlockBySeconds.add(index);
            }
                break;
        }
    }

    private void handling0x5F(int index) {
        switch (curTypeLoc) {
            case Train.VL80:// ВЛ80
                break;
            case Train.VL85:// ВЛ85
                break;
            case S5K: {// 2ЭС5К, 3ЭС5К
                avpt.gr.blocks32.s5k.Block32_5F block32_5F = new avpt.gr.blocks32.s5k.Block32_5F(arrBlock32.get(index).getValues());
                listSignals.add(listSignals.getListGV(), second_coordinate, block32_5F.getGV() == 1);
                listSignals.add(listSignals.getListProtect(), second_coordinate, block32_5F.getProtect() == 1);
                listSignals.add(listSignals.getListBox(), second_coordinate, block32_5F.getBox() == 1);
                listSignals.add(listSignals.getListPressOver(), second_coordinate, block32_5F.getPressOver() == 1);
                listSignals.add(listSignals.getListMKCompressOff(), second_coordinate, block32_5F.getMKCompressOff() == 1);
                listSignals.add(listSignals.getListDMPressOil(), second_coordinate, block32_5F.getDMPressOil() == 1);
                //listSignals.add(listSignals.getListHandSpeedZero(), second_coordinate, block32_5F.getHandleSpeedZero() == 1);
                listTasks.add(listTasks.getListCabin(), second_coordinate, block32_5F.getNumCabin(), curTypeLoc, -1);
                listTasks.add(listTasks.getListMainControl(), second_coordinate, block32_5F.getMainControl(), curTypeLoc, -1);
                listTasks.add(listTasks.getListRevControl(), second_coordinate, block32_5F.getRevControl(), curTypeLoc, -1);
                listTasks.add(listTasks.getListSchema(), second_coordinate, block32_5F.getSchema(), curTypeLoc, -1);
                // выключение тэд
                listSignals.add(listSignals.getListTed_1_2(), second_coordinate, block32_5F.getTed1Off() == 1);
                listSignals.add(listSignals.getListTed_3_4(), second_coordinate, block32_5F.getTed3Off() == 1);
                listSignals.add(listSignals.getListTed_5_6(), second_coordinate, block32_5F.getTed5Off() == 1);
                listSignals.add(listSignals.getListTed_7_8(), second_coordinate, block32_5F.getTed7Off() == 1);
                listSignals.add(listSignals.getListTed_9_10(), second_coordinate, block32_5F.getTed9Off() == 1);
                listSignals.add(listSignals.getListTed_11_12(), second_coordinate, block32_5F.getTed11Off() == 1);
                listSignals.add(listSignals.getListTed_13_14(), second_coordinate, block32_5F.getTed13Off() == 1);
                listSignals.add(listSignals.getListTed_15_16(), second_coordinate, block32_5F.getTed15Off() == 1);
            }
                break;
            case Train.KZ8A:// KZ8A
                break;
            case S5:// 2ЭС5
                break;
            case Train.S6:// 2ЭС6
                break;
            case Train.S4K: {// 2ЭС4К, 3ЭС4К
                avpt.gr.blocks32.s4k.Block32_5F block32_5F = new avpt.gr.blocks32.s4k.Block32_5F(arrBlock32.get(index).getValues());

                listSignals.add(listSignals.getListProtect(), second_coordinate, block32_5F.getProtect() == 1);
                listSignals.add(listSignals.getListBox(), second_coordinate, block32_5F.getBox() == 1);
                listSignals.add(listSignals.getListPressOver(), second_coordinate, block32_5F.getPressOver() == 1);
                listSignals.add(listSignals.getListBv(), second_coordinate, block32_5F.getBV() == 1);
                listSignals.add(listSignals.getListEmergencyBrake(), second_coordinate, block32_5F.getEmergencyBrake() == 1);
                listSignals.add(listSignals.getListEPK(), second_coordinate, block32_5F.getEPK() == 1);
                listSignals.add(listSignals.getListTm(), second_coordinate, block32_5F.getTM() == 1);
                listSignals.add(listSignals.getListSandAuto(), second_coordinate, block32_5F.getSandAuto() == 1);
                listSignals.add(listSignals.getListReleaseBrake(), second_coordinate, block32_5F.getReleaseBrake() == 1);
                listSignals.add(listSignals.getListMKCompressOff(), second_coordinate, block32_5F.getCompressorOn() == 0);
                listSignals.add(listSignals.getListDischargeAB(), second_coordinate, block32_5F.getDischargeAB() == 1);

                listTasks.add(listTasks.getListCabin(), second_coordinate, block32_5F.getNumCabin(), curTypeLoc, -1);
                listTasks.add(listTasks.getListMainControl(), second_coordinate, block32_5F.getMainControl(), curTypeLoc, -1);
                listTasks.add(listTasks.getListRevControl(), second_coordinate, block32_5F.getRevControl(), curTypeLoc, -1);
                listTasks.add(listTasks.getListSchema(), second_coordinate, block32_5F.getSchema(), curTypeLoc, -1);
            }
                break;
            case Train.S5K_2:// 2ЭС5К, 3ЭС5К Ермак
                avpt.gr.blocks32.s5k_2.Block32_5F block32_5F_2 = new avpt.gr.blocks32.s5k_2.Block32_5F(arrBlock32.get(index).getValues());
                // клавиши
                listTasks.add(listTasks.getListKeyStart(), second_coordinate, block32_5F_2.getKeyStart(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyF1(), second_coordinate, block32_5F_2.getKeyF1(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEnter(), second_coordinate, block32_5F_2.getKeyEnter(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEsc(), second_coordinate, block32_5F_2.getKeyEsc(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDel(), second_coordinate, block32_5F_2.getKeyDel(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyUp(), second_coordinate, block32_5F_2.getKeyUp(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyDown(), second_coordinate, block32_5F_2.getKeyDown(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyLeft(), second_coordinate, block32_5F_2.getKeyLeft(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyRight(), second_coordinate, block32_5F_2.getKeyRight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyZero(), second_coordinate, block32_5F_2.getKeyZero(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyOne(), second_coordinate, block32_5F_2.getKeyOne(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyTwo(), second_coordinate, block32_5F_2.getKeyTwo(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyThree(), second_coordinate, block32_5F_2.getKeyThree(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFour(), second_coordinate, block32_5F_2.getKeyFour(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyFive(), second_coordinate, block32_5F_2.getKeyFive(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySix(), second_coordinate, block32_5F_2.getKeySix(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeySeven(), second_coordinate, block32_5F_2.getKeySeven(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyEight(), second_coordinate, block32_5F_2.getKeyEight(), curTypeLoc, -1);
                listTasks.add(listTasks.getListKeyNine(), second_coordinate, block32_5F_2.getKeyNine(), curTypeLoc, -1);
                //
                break;
        }
    }

//    private int typeLocFromTypeLocUsavp(int typeLocUsavp) {
//        switch (typeLocUsavp) {
//            case 0: return CHS7;
//            case 1: return CHS2_KAUD;
//            case 3: return CHS2K_KAUD;
//            case 4: return CHS2T_KAUD;
//            case 5: return CHS6_KAUD;
//            case 6: return CHS4T_KAUD;
//            case 7: return EP1_KAUD;
//            case 8: return CHS8_KAUD;
//            case 9: return EP2K_KAUD;
//            case 10: return EP1_P;
//            case 11: return TEP70;
//            case 13:
//            case 14: return EP20_KAUD;
//
//            default: return -1;
//        }
//    }

//    /**
//     * 0x21 - пассажирское движение
//     * @param index -
//     */
//    private void handling0x21_passenger(int index) {
//        Block32_21_61_91 block32_21_61_91 = new Block32_21_61_91(arrBlock32.get(index).getValues());
//        curTime_21 = block32_21_61_91.getTime();
//        LocalDate localDate = block32_21_61_91.getDate();
//        if (localDate != null && curTime_21 != null)
//            curDateTime_21 = LocalDateTime.of(localDate, curTime_21);
//        curTypeLoc_SAVP = block32_21_61_91.getTypeLoc();
//        if (curTypeLoc == -1)
//            curTypeLoc = typeLocFromTypeLocUsavp(curTypeLoc_SAVP);
//        if (curTypeLoc == 113 && typeLocFromTypeLocUsavp(curTypeLoc_SAVP) == EP1_P)
//            curTypeLoc = EP1_P;
//
//        listLines.add(listLines.getListSpeed_USAVP(), second_coordinate, block32_21_61_91.getSpeed(), -1);
//        listLines.add(listLines.getListPressUR_USAVP(), second_coordinate, block32_21_61_91.getPressUR(), -1);
//        listLines.add(listLines.getListPressTC_USAVP(), second_coordinate, block32_21_61_91.getPressTC(), -1);
//        listLines.add(listLines.getListPressZTS_USAVP(), second_coordinate, block32_21_61_91.getPressZTC(), -1);
//    }

    public ArrayList<GpsCoordinate> getListGpsCoordinate() {
        return listGpsCoordinate;
    }

    public ArrayList<RailCoordinate> getListRailCoordinate() {
        return listRailCoordinate;
    }

//    public ArrayList<Double> getListSpeedLimTmp() {
//        return listSpeedLimTmp;
//    }

    public ArrayList<LocalDateTime> getListDateTimes() {
        return listDateTimes;
    }

    public ArrayList<int[]> getListObjects() {
        return listObjects;
    }

    public ArrayList<int[]> getListProfiles() {
        return listProfiles;
    }

    public ArrayList<int[]> getListLimits() {
        return listLimits;
    }

    /**
     * @param cnt_en_prev - предыдущее покозание
     * @param cnt_en_cur - текущее показание
     * @return - разница между текущим и предыдущим покозаниями счетчика энергии
     */
    public static long getDiffEnCnt(long cnt_en_prev, long cnt_en_cur) {
        long d = cnt_en_cur - cnt_en_prev;
        if (d < 0 || d > 10000) d = 0;
        return d;
    }

    /**
     * @param cnt_en_prev - показания счетчика предыдущей посылки
     * @param cnt_en_cur - показания счетчика текущей посылки
     * @param list -
     * @return - расход энергии
     */
    private double getConsumptionEnergy(long cnt_en_prev, long cnt_en_cur, ArrayList<ListLines.ItemLine> list) {
        long d = getDiffEnCnt(cnt_en_prev, cnt_en_cur);
        double curConsumption = 0;
        if (list != null && !list.isEmpty()) {
            if (arrTrains.size() > 0 && list.get(list.size() - 1).getSecond() < arrTrains.get(arrTrains.size() - 1).getSecondStart())
                // сброс в 0 на начало поездки
                curConsumption = 0;
            else
                curConsumption = list.get(list.size() - 1).getValue();
        }
        return  curConsumption + d;
    }

    /**
     * @param second - секунда
     * @return - номер блока по секунде
     */
    public int getNBlockBySecond(int second) {
        if (second < 1 || second > listBlockBySeconds.size()) return -1;
        return listBlockBySeconds.get(second - 1);
    }

    public int getSecondCoordinate() {
        if (isTime)
            return second_coordinate;
        else
            return cur_coordinate;
    }

    /**
     * увеличиваем время или координату на одну позицию
     * @param isTime - true рзвертка по времени, false развертка по координате
     */
    private void addSecond(boolean isTime) {
        if (isTime) {
            second_coordinate++;
            addGpsCoordinate();
            addRailCoordinate();
            addCurDateTime();
            addCurObject();
            addCurProfile();
            addCurMapLimit();
        }
        else {
            int n = cur_coordinate - second_coordinate;
            second_coordinate = cur_coordinate;
            for (int i = 0; i < n; i++) {
                addGpsCoordinate();
                addRailCoordinate();
                addCurDateTime();
                addCurObject();
                addCurProfile();
                addCurMapLimit();
            }
        }
    }

    /**
     * сброс энернии на начало поезда
     */
    private void resetEnergy() {
        listLines.add(listLines.getListConsumptionEn(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_s1(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_s2(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_s3(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_s4(), second_coordinate, 0, -1);

        listLines.add(listLines.getListConsumptionEn_r(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_r_s1(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_r_s2(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_r_s3(), second_coordinate, 0, -1);
        listLines.add(listLines.getListConsumptionEn_r_s4(), second_coordinate, 0, -1);
    }

    public void setCurTypeLoc(int curTypeLoc) {
        this.curTypeLoc = curTypeLoc;
    }

    public void setCurTypeMove(int curTypeMove) {
        this.curTypeMove = curTypeMove;
    }

    public static double getVoltageK_s5k() {
        return voltageK_s5k;
    }

    public static double getAmperageK_s5k() {
        return amperageK_s5k;
    }
}
