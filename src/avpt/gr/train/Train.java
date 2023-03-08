package avpt.gr.train;

import avpt.gr.blocks32.overall.Block32_21_4;
import avpt.gr.chart_dataset.ItemWag;
import avpt.gr.chart_dataset.TaskAlsn;
import avpt.gr.common.UtilsArmG;
import avpt.gr.maps.Limits;
import avpt.gr.maps.Stations;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class Train {

    public final static int VL11 = 1;   // ВЛ11
    public final static int VL10 = 2;   // ВЛ10
    public final static int VL80 = 3;   // ВЛ80
    public final static int VL85 = 4;   // ВЛ85
    public final static int S5K = 5;    // 2ЭС5К, 3ЭС5К
    public final static int KZ8A = 6;   // KZ8A
    public final static int S5 = 7;     // 2ЭС5
    public final static int S6 = 8;     // 2ЭС6
    public final static int S4K = 9;    // 2ЭС4К, 3ЭС4К
    public final static int S5K_2 = 11; // 2ЭС5К, 3ЭС5К
    // asim
    public final static int VL11_ASIM = 16;   // ВЛ11
    public final static int VL15_ASIM = 17;   // ВЛ15
    public final static int VL85_ASIM = 20;   // ВЛ85
    public final static int VL10_ASIM = 32;   // ВЛ10
    public final static int VL11K_ASIM = 46; // ВЛ11К
    public final static int VL80C_ASIM = 51;  // ВЛ80С
    public final static int VL80T_ASIM = 52;  // ВЛ80Т
    public final static int VL80TK_ASIM = 53;   // ВЛ80ТК
    public final static int VL80P_ASIM = 54;  // ВЛ80Р
    public final static int VL80SK_ASIM = 988;   // ВЛ80СКs
    public final static int VL10UK_ASIM = 989; // ВЛ10УК
    public final static int VL10K_ASIM = 995; // ВЛ10К
    public final static int VL10U_ASIM = 996; // ВЛ10У
    public final static int VL11M_ASIM = 997; // ВЛ11М
    // pass
    public final static int CHS7 = 101;         // ЧС-7
    public final static int CHS7_EKS = 102;     // ЧС-7 ЕКС
    public final static int CHS7_KAUD_F = 103;  // ЧС-7 КАУД-Ф
    public final static int CHS2_RED_FLAG = 104;// ЧС-2 Красн.Знамя
    public final static int CHS2_KAUD = 105;    // ЧС-2 КАУД
    public final static int CHS2_EKS_2 = 106;   // ЧС-2 ЕКС-2
    public final static int CHS2K_USAVPP = 107; // ЧС-2К УСАВПП
    public final static int CHS2K_KAUD = 108;   // ЧС-2К КАУД
    public final static int CHS2K_ESAUP = 109;  // ЧС-2К ЕСАУП
    public final static int CHS2T_KAUD = 110;   // ЧС-2Т КАУД
    public final static int CHS6_KAUD = 111;    // ЧС-6/200 КАУД
    public final static int CHS4T_KAUD = 112;   // ЧС-4Т КАУД
    public final static int EP1_KAUD = 113;     // ЭП1 КАУД
    public final static int CHS8_KAUD = 114;    // ЧС-8 КАУД
    public final static int EP1_U = 115;        // ЭП1У КАУД
    public final static int CHS7_EKS2 = 116;    // ЧС-7 ЕКС2
    public final static int EP2K_KAUD = 117;    // ЭП2К КАУД
    public final static int EP20_KAUD = 118;    // ЭП20 КАУД
    public final static int CHS7_KAUD_L = 119;  // ЧС-7 КАУД-Л
    public final static int KZ4AT = 120;        // KZ4AT
    public final static int TEP33A = 121;       // ТЭП33А
    public final static int EP1_P = 153;         // ЭП1П
    public final static int TEP70 = 171;       // ТЭП70
    public final static int TEP70BS = 172;       // ТЭП70БС

    public static final String WRONG_TYPE_NAME = "-";

    private int blStart = -1;			// номер блока картраджа - начало поезда
    private int blEnd = -1;				// номер блока картраджа - конец поезда
    private int secondsStart = 0;
    private int secondsEnd = 0;

    // initBr
    private long numFactoryBr;      // заводской номер
    private String verBr;           // версия бр
    private long numLoc;            // Номер  электровоза (номер локототива)
    private int numSection;         // номер секции
    private int locTypeAsoup;// Тип локомотива по таблице АСОУП
    private LocalDate dateInit;
    private LocalDate dateMap;

    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;

    private int numTrain = -1;			// номер поезда
    private int typeLoc = -1;			// серия (тип) локомотива
    private long numTab = -1;				// табельный номер

    private int dBandage = -1;			// диаметр бандажа
    private int cntWags = -1;			// количество вагонов
    private int cntConditionWags = - 1; // количество условных вагонов
    private int weightTrain = -1;		// вес поезда
    private int stAmperage = -1;        // уставка тока
    private long coordinate = -1;        // координата
    private int stIsavprt = -1;         // ИСАВПРТ
    private int cntSection = -1;        // количество секций
    private int typeS = -1;             // тип состава

    private int routeId = -1;           // идентификатор маршрута
    private String routName;            // имя мяршрута

    private long distance = 0;           // пройдено м
    private long distance_prompt = 0;   // пройдено в подсказке м
    private long distance_auto = 0;     // пройдено в автоведении м

    private long seconds = 0;           // пройдено сек
    private long seconds_prompt = 0;   // пройдено в подсказке сек
    private long seconds_auto = 0;     // пройдено в автоведении сек
    private long seconds_stop = 0;      // секунд в стоянке

    private String duration_time = "";   // затраченое время
    private String duration_time_auto = "";   // затраченое время в автоведении
    private String duration_time_prompt = "";   // затраченое время в подсказке

    private int codePositionTrain = -1;     // код позиции поезда
    private String positionTrain = "";      // позиция поезда ведущий, ведомый 1..n

    // показываем если statusIsavprt = 1 или 4
    private int numSlave;
    private String nameSlave = "";
    private int netAddress;
    private String additionalChannel = "";

    private double avgSpeed = -1;          // средняя скорость
    private double avgSpeedMove = -1;      // средняя техническая скорость

    private int typeMove = -1;             // тип движения 1 - пасс 2 - пассаж Т 3 - грузовой 7 - асим
    private boolean isAsim = false;

    private boolean isChangeLoadSchedule = false; // смена загруженного расписания
    private int cntVLim = 0;    // количество временных ограничений
    private int lenVlim = 0;    // длина временных ограничений
    private int cntCLim = 0;    // количество постоянных ограничений

    private long act = -1;
    private long act1 = -1;
    private long act2 = -1;
    private long act3 = -1;
    private long act4 = -1;

    private long rec = -1;
    private long rec1 = -1;
    private long rec2 = -1;
    private long rec3 = -1;
    private long rec4 = -1;

    // показания счетчиков на начало-конец поездки
    private long cnt_start_act = 0;
    private long cnt_start_act1 = 0;
    private long cnt_start_act2 = 0;
    private long cnt_start_act3 = 0;
    private long cnt_start_act4 = 0;

    private long cnt_end_act = 0;
    private long cnt_end_act1 = 0;
    private long cnt_end_act2 = 0;
    private long cnt_end_act3 = 0;
    private long cnt_end_act4 = 0;

    private long cnt_start_rec = 0;
    private long cnt_start_rec1 = 0;
    private long cnt_start_rec2 = 0;
    private long cnt_start_rec3 = 0;
    private long cnt_start_rec4 = 0;

    private long cnt_end_rec = 0;
    private long cnt_end_rec1 = 0;
    private long cnt_end_rec2 = 0;
    private long cnt_end_rec3 = 0;
    private long cnt_end_rec4 = 0;

    // проезд алсн
    private final CntAlsn cnt_yellow = new CntAlsn();
    private final CntAlsn cnt_white = new CntAlsn();
    private final CntAlsn cnt_green = new CntAlsn();
    private final CntAlsn cnt_red_yellow = new CntAlsn();
    private final CntAlsn cnt_red = new CntAlsn();

    // диагностика всц
    private final VscDiagnostic mainLinkModem = new VscDiagnostic();    // Связь по основному каналу с модемом (%)
    private final VscDiagnostic mainLinkVSC = new VscDiagnostic();      // Связь по основному каналу между локомотивами (%)
    private final VscDiagnostic slaveLinkModem = new VscDiagnostic();   // Связь по дополнительному каналу с модемом (%)
    private final VscDiagnostic slaveLinkVSC = new VscDiagnostic();     // Связь по дополнительному каналу между локомотивами (%)
    private final VscDiagnostic slaveIsOn = new VscDiagnostic();        // Включение доп канала (%)
    private boolean isTestThrust;                                       // тест тяги не пройден
    private boolean isTestBrake;                                        // тест тормозов не пройден
    private long uMax;                                                  // максимальное напряжение контактной сети
    private boolean isAlsn;                                             // наличие сигнала алсн
    private int bhvCnt;                                                 // наличие посылок бхв

    public static class VscDiagnostic {
        private final UtilsArmG.PairInt pair = new UtilsArmG.PairInt();

        public void incCntFirst() {
            pair.incFirst();
        }

        public void incCntSecond() {
            pair.incSecond();
        }

        public int getPercent() {
            int result = 0;
            int amount = pair.getFirst() + pair.getSecond();
            if (amount > 0) {
                result = pair.getFirst() * 100 / amount;
                result = result == 0 && pair.getFirst() > 0 ? 1 : result;
            }
            return result;
            //return result == 0 && statusIsavprt == 0 ? -1 : result;
        }
    }

    public static class CntAlsn {
        private int cnt;
        private int distance;
        private int second;

        public void incCnt() {
            ++cnt;
        }

        public void incDistance(int distance) {
            this.distance += distance;
        }

        public void incSecond(int second) {
            this.second += second;
        }

        public int getCnt() {
            return cnt;
        }

        public int getDistance() {
            return distance;
        }

        public int getSecond() {
            return second;
        }
    }

    public static class Limit_V {
        int second_start;
        int second_end;
        int coordinate_start;
        int coordinate_end;
        double speed;

        public Limit_V(int second_start, int coordinate_start, double speed) {
            this.second_start = second_start;
            this.coordinate_start = coordinate_start;
            this.speed = speed;
        }

        public void setSecond_end(int second_end) {
            this.second_end = second_end;
        }

        public void setCoordinate_end(int coordinate_end) {
            this.coordinate_end = coordinate_end;
        }

        public int getLen() {
            return coordinate_end - coordinate_start;
        }
    }

    private final List<ItemWag> wags = new ArrayList<ItemWag>();
    private final List<Limits.Limit> limits_c = new ArrayList<Limits.Limit>();
    private final List<Limit_V> limits_v = new ArrayList<Limit_V>();
    private final List<Stations.Station> stations = new ArrayList<Stations.Station>();

    public Train(int blStart) {
        this.blStart = blStart;
    }

//    public void setBlockEnd(int blEnd) {
//        this.blEnd = blEnd;
//    }

    /**
     * @param typeLoc - код типа локомотива
     * @return название типа локомотива
     */
    public static String getNameTypeLoc(int typeLoc, int code_asoup) {
        String result = WRONG_TYPE_NAME;
        switch (typeLoc) {
            case VL11:
                result = "ВЛ11";
                break;
            case VL10:
                if (code_asoup == 138)
                    result = "ВЛ10У";
                else
                    result = "ВЛ10";
                break;
            case VL80:
                result = "ВЛ80";
                break;
            case VL85:
                result = "ВЛ85";
                break;
            case S5K:
                if (code_asoup == 253)
                    result = "3ЭС5К(МСУД-Н)";
                else if (code_asoup == 222)
                    result = "2ЭС5К(МСУД-Н)";
                else if (code_asoup == 220)
                    result = "4ЭС5К(МСУД-Н)";
                else
                    result = "ЭС5К(МСУД-Н)";
                break;
            case KZ8A:
                result = "KZ8A";
                break;
            case S5:
                if (code_asoup == 253)
                    result = "3ЭС5";
                else if (code_asoup == 222)
                    result = "2ЭС5";
                else if (code_asoup == 220)
                    result = "4ЭС5";
                else
                    result = "ЭС5";
                break;
            case S6:
                result = "2ЭС6";
                break;
            case S4K:
                if (code_asoup == 115)
                    result = "3ЭС4К";
                else if (code_asoup == 144)
                    result = "2ЭС4К";
                else
                    result = "ЭС4К";
                break;
            case S5K_2:
                if (code_asoup == 253)
                    result = "3ЭС5К(МСУД-015)";
                else if (code_asoup == 222)
                    result = "2ЭС5К(МСУД-015)";
                else if (code_asoup == 220)
                    result = "4ЭС5К(МСУД-015)";
                else
                    result = "ЭС5К(МСУД-015)";
                break;

            case VL11_ASIM:
                result = "ВЛ11 АСИМ";
                break;
            case VL15_ASIM:
                result = "ВЛ15 АСИМ";
                break;
            case VL85_ASIM:
                result = "ВЛ85 АСИМ";
                break;
            case VL10_ASIM:
                result = "ВЛ10 АСИМ";
                break;
            case VL11K_ASIM:
                result = "ВЛ11К АСИМ";
                break;
            case VL80C_ASIM:
                result = "ВЛ80С АСИМ";
                break;
            case VL80T_ASIM:
                result = "ВЛ80Т АСИМ";
                break;
            case VL80TK_ASIM:
                result = "ВЛ80ТК АСИМ";
                break;
            case VL80P_ASIM:
                result = "ВЛ80Р АСИМ";
                break;
            case VL80SK_ASIM:
                result = "ВЛ80СК АСИМ";
                break;
            case VL10UK_ASIM:
                result = "ВЛ10УК АСИМ";
                break;
            case VL10K_ASIM:
                result = "ВЛ10К АСИМ";
                break;
            case VL10U_ASIM:
                result = "ВЛ10У АСИМ";
                break;
            case VL11M_ASIM:
                result = "ВЛ11М АСИМ";
                break;
        }
        return result;
    }

//    public static int getSectionsByTypeLoc(int codeTypeLoc) {
//        int result = 0;
//        switch (codeTypeLoc) {
//            case VL11:
//            case VL10:
//            case VL80:
//            case VL85:
//            case S6:
//                result = 2;
//                break;
//            case S5:
//            case S5K:
//                if (locTypeAsoup == 253)
//                    result = 3;
//                else if (locTypeAsoup == 222)
//                    result = 2;
//                else if (locTypeAsoup == 220)
//                    result = 4;
//                else
//                    result = 2;
//                break;
//            case S4K:
//                if (locTypeAsoup == 115)
//                    result = 3;
//                else if (locTypeAsoup == 144)
//                    result = 2;
//                else
//                    result = 2;
//                break;
//            case S5K_2:
//                if (locTypeAsoup == 253)
//                    result = 3;
//                else if (locTypeAsoup == 222)
//                    result = 2;
//                else if (locTypeAsoup == 220)
//                    result = 4;
//                else
//                    result = 3;
//                break;
////
////            case VL11_ASIM:
////                result = "ВЛ-11 АСИМ";
////                break;
////            case VL11M_ASIM:
////                result = "ВЛ-11М АСИМ";
////                break;
////            case VL15_ASIM:
////                result = "ВЛ-15 АСИМ";
////                break;
////            case VL10_ASIM:
////                result = "ВЛ-10 АСИМ";
////                break;
////            case VL10U_ASIM:
////                result = "ВЛ-10У АСИМ";
////                break;
////            case VL80C_ASIM:
////                result = "ВЛ-10С АСИМ";
////                break;
////            case VL80T_ASIM:
////                result = "ВЛ-10Т АСИМ";
////                break;
////            case VL80P_ASIM:
////                result = "ВЛ-10Р АСИМ";
////                break;
////            case VL85_ASIM:
////                result = "ВЛ-85 АСИМ";
////                break;
//        }
//        return result;
//    }

    public String getNumTrainStr() {
        if (numTrain != -1)
            return numTrain + "";
        else
            return "__";
    }

    public int getBlStart() {
        return blStart;
    }

    public int getBlEnd() {
        return blEnd;
    }

    public int getSecondStart() {
        return secondsStart;
    }

    public int getSecondsEnd() {
        return secondsEnd;
    }

    public int getNumTrain() {
        return numTrain;
    }

    public int getTypeLoc() {
        return typeLoc;
    }

    public long getNumTab() {
        return numTab;
    }

    public int getdBandage() {
        return dBandage;
    }

    public int getCntWags() {
        return cntWags;
    }

    public int getWeightTrain() {
        return weightTrain;
    }

//    public int getSeconds() {
//        return seconds;
//    }

//    public int getIndexTrain() {
//        return indexTrain;
//    }

    public void setBlEnd(int blEnd) {
        this.blEnd = blEnd;
    }

    public void setSecondStart(int secondsStart) {
        this.secondsStart = secondsStart;
    }

    public void setSecondEnd(int secondsEnd) {
        this.secondsEnd = secondsEnd;
    }

    public void setNumTrain(int numTrain) {
        this.numTrain = numTrain;
    }

    public void setTypeLoc(int typeLoc) {
        this.typeLoc = typeLoc;
    }

    public void setNumTab(long numTab) {
        this.numTab = numTab;
    }

    public void setdBandage(int dBandage) {
        this.dBandage = dBandage;
    }

    public void setCntWags(int cntWags) {
        this.cntWags = cntWags;
    }

    public void setWeightTrain(int weightTrain) {
        this.weightTrain = weightTrain;
    }

    public void setNumFactoryBr(long numFactoryBr) {
        this.numFactoryBr = numFactoryBr;
    }

    public void setVerBr(String verBr) {
        this.verBr = verBr;
    }

    public void setNumLoc(long numLoc) {
        this.numLoc = numLoc;
    }

    public void setNumSection(int numSection) {
        this.numSection = numSection;
    }

    public void setLocTypeAsoup(int locTypeAsoup) {
        this.locTypeAsoup = locTypeAsoup;
    }

    public void setDateInit(LocalDate dateInit) {
        this.dateInit = dateInit;
    }

    public void setDateMap(LocalDate dateMap) {
        this.dateMap = dateMap;
    }

    public int getStAmperage() {
        return stAmperage;
    }

    public void setStAmperage(int stAmperage) {
        this.stAmperage = stAmperage;
    }

    public int getCntConditionWags() {
        return cntConditionWags;
    }

    public void setCntConditionWags(int cntConditionWags) {
        this.cntConditionWags = cntConditionWags;
    }

    public long getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(long coordinate) {
        this.coordinate = coordinate;
    }

    public String getStatusIsavprt(int statusIsavprt) {
        switch (statusIsavprt) {
            case 0: {
                if (bhvCnt > 3) return "бхв";
                else return "один";
            }
            case 1: return "ведущий РТ";
            case 2: return "ведомый РТ";
            case 4: return "виртуальная сцепка";
        }
        return "";
    }

    public int getStIsavprt() {
        return stIsavprt;
    }

    public void setStIsavprt(int stIsavprt) {
        this.stIsavprt = stIsavprt;
    }

    public int getCntSection() {
        return cntSection;
    }

    public void setCntSection(int cntSection) {
        this.cntSection = cntSection;
    }

    public long getNumFactoryBr() {
        return numFactoryBr;
    }

    public String getVerBr() {
        return verBr;
    }

    public long getNumLoc() {
        return numLoc;
    }

    public int getNumSection() {
        return numSection;
    }

    public String getNumSectionText() {
        String result = "";
        if (numSection > -1)
            result = "секция " + numSection;
        return result;
    }

    public int getLocTypeAsoup() {
        return locTypeAsoup;
    }

    public LocalDate getDateInit() {
        return dateInit;
    }

    public LocalDate getDateMap() {
        return dateMap;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(LocalDateTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public int getTypeS() {
        return typeS;
    }

    /**
     * @param typeS - идентификатор типа состава
     * @return
     */
    public static String getTypeS(int typeS) {
        switch (typeS) {
            case 1: return "с порожними вагонами";
            case 2: return "c порожними цистернами";
        }
        return "общий";
    }

    public void setTypeS(int typeS) {
        this.typeS = typeS;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRoutName() {
        return routName;
    }

    public void setRouteName(String routName) {
        this.routName = routName;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getDistance_prompt() {
        return distance_prompt;
    }

    public void setDistance_prompt(long distance_prompt) {
        this.distance_prompt = distance_prompt;
    }

    public long getDistance_auto() {
        return distance_auto;
    }

    public void setDistance_auto(long distance_auto) {
        this.distance_auto = distance_auto;
    }


    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds_prompt() {
        return seconds_prompt;
    }

    public void setSeconds_prompt(long seconds_prompt) {
        this.seconds_prompt = seconds_prompt;
    }

    public long getSeconds_auto() {
        return seconds_auto;
    }

    public long getSeconds_stop() {
        return seconds_stop;
    }

    public void setSeconds_auto(long seconds_auto) {
        this.seconds_auto = seconds_auto;
    }

    public void setSeconds_stop(long seconds_stop) {
        this.seconds_stop = seconds_stop;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }

    public String getDuration_time_auto() {
        return duration_time_auto;
    }

    public void setDuration_time_auto(String duration_time_auto) {
        this.duration_time_auto = duration_time_auto;
    }

    public String getDuration_time_prompt() {
        return duration_time_prompt;
    }

    public void setDuration_time_prompt(String duration_time_prompt) {
        this.duration_time_prompt = duration_time_prompt;
    }

    public int getNumSlave() {
        return numSlave;
    }

    public void setNumSlave(int numSlave) {
        this.numSlave = numSlave;
    }

    public String getNameSlave() {
        return nameSlave;
    }

    public void setNameSlave(String nameSlave) {
        this.nameSlave = nameSlave;
    }

    public int getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(int netAddress) {
        this.netAddress = netAddress;
    }

    public String getAdditionalChannel() {
        return additionalChannel;
    }

    public void setAdditionalChannel(String additionalChannel) {
        this.additionalChannel = additionalChannel;
    }

    public int getLengthTrain() {
        return cntConditionWags * 14 + cntSection * 17;
    }

    public String getPositionTrain() {
        return positionTrain;
    }

    public void setPositionTrain(String positionTrain) {
        this.positionTrain = positionTrain;
    }

    public int getCodePositionTrain() {
        return codePositionTrain;
    }

    public void setCodePositionTrain(int codePositionTrain) {
        this.codePositionTrain = codePositionTrain;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgSpeedMove() {
        return avgSpeedMove;
    }

    public void setAvgSpeedMove(double avgSpeedMove) {
        this.avgSpeedMove = avgSpeedMove;
    }

    public int getTypeMove() {
        return typeMove;
    }

    public void setTypeMove(int typeMove) {
        this.typeMove = typeMove;
    }

    public boolean isAsim() {
        return isAsim;
    }

    public void setAsim(boolean asim) {
        isAsim = asim;
    }

    public boolean isChangeLoadSchedule() {
        return isChangeLoadSchedule;
    }

    public void setChangeLoadSchedule(boolean changeLoadSchedule) {
        isChangeLoadSchedule = changeLoadSchedule;
    }

    public int getCntCLim() {
        return cntCLim;
    }

    public void incCLim() {
        cntCLim++;
    }

    public long getAct1() {
        return act1;
    }

    public void setAct1(long act1) {
        this.act1 = act1;
    }

    public long getAct2() {
        return act2;
    }

    public void setAct2(long act2) {
        this.act2 = act2;
    }

    public long getAct3() {
        return act3;
    }

    public void setAct3(long act3) {
        this.act3 = act3;
    }

    public long getRec1() {
        return rec1;
    }

    public void setRec1(long rec1) {
        this.rec1 = rec1;
    }

    public long getRec2() {
        return rec2;
    }

    public void setRec2(long rec2) {
        this.rec2 = rec2;
    }

    public long getRec3() {
        return rec3;
    }

    public void setRec3(long rec3) {
        this.rec3 = rec3;
    }

    public long getAct() {
        return act;
    }

    public void setAct(long act) {
        this.act = act;
    }

    public long getAct4() {
        return act4;
    }

    public void setAct4(long act4) {
        this.act4 = act4;
    }

    public long getRec() {
        return rec;
    }

    public void setRec(long rec) {
        this.rec = rec;
    }

    public long getRec4() {
        return rec4;
    }

    public void setRec4(long rec4) {
        this.rec4 = rec4;
    }

    public long getCnt_start_act() {
        return cnt_start_act;
    }

    public void setCnt_start_act(long cnt_start_act) {
        this.cnt_start_act = cnt_start_act;
    }

    public long getCnt_start_act1() {
        return cnt_start_act1;
    }

    public void setCnt_start_act1(long cnt_start_act1) {
        this.cnt_start_act1 = cnt_start_act1;
    }

    public long getCnt_start_act2() {
        return cnt_start_act2;
    }

    public void setCnt_start_act2(long cnt_start_act2) {
        this.cnt_start_act2 = cnt_start_act2;
    }

    public long getCnt_start_act3() {
        return cnt_start_act3;
    }

    public void setCnt_start_act3(long cnt_start_act3) {
        this.cnt_start_act3 = cnt_start_act3;
    }

    public long getCnt_start_act4() {
        return cnt_start_act4;
    }

    public void setCnt_start_act4(long cnt_start_act4) {
        this.cnt_start_act4 = cnt_start_act4;
    }

    public long getCnt_end_act() {
        return cnt_end_act;
    }

    public void setCnt_end_act(long cnt_end_act) {
        this.cnt_end_act = cnt_end_act;
    }

    public long getCnt_end_act1() {
        return cnt_end_act1;
    }

    public void setCnt_end_act1(long cnt_end_act1) {
        this.cnt_end_act1 = cnt_end_act1;
    }

    public long getCnt_end_act2() {
        return cnt_end_act2;
    }

    public void setCnt_end_act2(long cnt_end_act2) {
        this.cnt_end_act2 = cnt_end_act2;
    }

    public long getCnt_end_act3() {
        return cnt_end_act3;
    }

    public void setCnt_end_act3(long cnt_end_act3) {
        this.cnt_end_act3 = cnt_end_act3;
    }

    public long getCnt_end_act4() {
        return cnt_end_act4;
    }

    public void setCnt_end_act4(long cnt_end_act4) {
        this.cnt_end_act4 = cnt_end_act4;
    }

    public long getCnt_start_rec() {
        return cnt_start_rec;
    }

    public void setCnt_start_rec(long cnt_start_rec) {
        this.cnt_start_rec = cnt_start_rec;
    }

    public long getCnt_start_rec1() {
        return cnt_start_rec1;
    }

    public void setCnt_start_rec1(long cnt_start_rec1) {
        this.cnt_start_rec1 = cnt_start_rec1;
    }

    public long getCnt_start_rec2() {
        return cnt_start_rec2;
    }

    public void setCnt_start_rec2(long cnt_start_rec2) {
        this.cnt_start_rec2 = cnt_start_rec2;
    }

    public long getCnt_start_rec3() {
        return cnt_start_rec3;
    }

    public void setCnt_start_rec3(long cnt_start_rec3) {
        this.cnt_start_rec3 = cnt_start_rec3;
    }

    public long getCnt_start_rec4() {
        return cnt_start_rec4;
    }

    public void setCnt_start_rec4(long cnt_start_rec4) {
        this.cnt_start_rec4 = cnt_start_rec4;
    }

    public long getCnt_end_rec() {
        return cnt_end_rec;
    }

    public void setCnt_end_rec(long cnt_end_rec) {
        this.cnt_end_rec = cnt_end_rec;
    }

    public long getCnt_end_rec1() {
        return cnt_end_rec1;
    }

    public void setCnt_end_rec1(long cnt_end_rec1) {
        this.cnt_end_rec1 = cnt_end_rec1;
    }

    public long getCnt_end_rec2() {
        return cnt_end_rec2;
    }

    public void setCnt_end_rec2(long cnt_end_rec2) {
        this.cnt_end_rec2 = cnt_end_rec2;
    }

    public long getCnt_end_rec3() {
        return cnt_end_rec3;
    }

    public void setCnt_end_rec3(long cnt_end_rec3) {
        this.cnt_end_rec3 = cnt_end_rec3;
    }

    public long getCnt_end_rec4() {
        return cnt_end_rec4;
    }

    public void setCnt_end_rec4(long cnt_end_rec4) {
        this.cnt_end_rec4 = cnt_end_rec4;
    }

    public List<ItemWag> getWags() {
        return wags;
    }

    public void setWags(List<ItemWag> wags) {
        this.wags.addAll(wags);
    }

    public List<Limit_V> getLimits_v() {
        return limits_v;
    }

//    public void setLimits_v(List<Limits.Limit> limits_v) {
//        this.limits_v = limits_v;
//    }

    public List<Limits.Limit> getLimits_c() {
        return limits_c;
    }

    /**
     * @param without_same - без учета повторяющихся ограничений
     * @return -
     */
    public int getCntCLim(boolean without_same) {
        if (!without_same) return limits_c.size();
        int cnt = 0;
        for (int i = 0; i < limits_c.size(); i++) {
            if (i == 0) {
                cnt++;
            }
            else { // одинаковые ограничения считаем как одно
                if (limits_c.get(i).getSpeed() != limits_c.get(i - 1).getSpeed()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public int getCntVLim() {
        return limits_v.size();
    }

    public int getLenVLim() {
        int sum = 0;
        for (int i = 0; i < limits_v.size(); i++) {
            int len = limits_v.get(i).getLen();
            if (len > 0)
                sum += limits_v.get(i).getLen();
        }
        return sum;
    }

//    public void setLimits_c(List<Limits.Limit> limits_c) {
//        this.limits_c = limits_c;
//    }


    public List<Stations.Station> getStations() {
        return stations;
    }

    public VscDiagnostic getMainLinkModem() {
        return mainLinkModem;
    }

    public VscDiagnostic getMainLinkVSC() {
        return mainLinkVSC;
    }

    public VscDiagnostic getSlaveLinkModem() {
        return slaveLinkModem;
    }

    public VscDiagnostic getSlaveLinkVSC() {
        return slaveLinkVSC;
    }

    public VscDiagnostic getSlaveIsOn() {
        return slaveIsOn;
    }

    public boolean isTestThrust() {
        return isTestThrust;
    }

    public void setTestThrust(boolean testThrust) {
        isTestThrust = testThrust;
    }

    public boolean isTestBrake() {
        return isTestBrake;
    }

    public void setTestBrake(boolean testBrake) {
        isTestBrake = testBrake;
    }

    public long getUMax() {
        return uMax;
    }

    public void setUMax(long uMax) {
        this.uMax = uMax;
    }

    public boolean isAlsn() {
        return isAlsn;
    }

    public void setAlsn(boolean alsn) {
        isAlsn = alsn;
    }

    public int getBhvCnt() {
        return bhvCnt;
    }

    public void incBhvCnt() {
        bhvCnt++;
    }

    private void incSecond(CntAlsn cntAlsn, Block32_21_4 block32_21_4_prev, Block32_21_4 block32_21_4) {
        if (block32_21_4_prev != null) {
            LocalTime prevTime = UtilsArmG.getTime(block32_21_4_prev.getSecBeginDay());
            LocalTime curTime =  UtilsArmG.getTime(block32_21_4.getSecBeginDay());
            cntAlsn.incSecond((int)Duration.between(prevTime, curTime).getSeconds());
        }
    }

    // проезд на сигнал светофора
    public void incCntAlsn(Block32_21_4 block32_21_4_prev, Block32_21_4 block32_21_4, int d) {
        if (d < 0 || d > 100) d = 0;
        switch (block32_21_4.getALSN()) {
            case TaskAlsn.ALSN_YELLOW:
                if (block32_21_4_prev == null || block32_21_4_prev.getALSN() != TaskAlsn.ALSN_YELLOW) cnt_yellow.incCnt();
                incSecond(cnt_yellow, block32_21_4_prev, block32_21_4);
                cnt_yellow.incDistance(d);
                break;
            case TaskAlsn.ALSN_WHITE:
                if (block32_21_4_prev == null || block32_21_4_prev.getALSN() != TaskAlsn.ALSN_WHITE) cnt_white.incCnt();
                incSecond(cnt_white, block32_21_4_prev, block32_21_4);
                cnt_white.incDistance(d);
                break;
            case TaskAlsn.ALSN_GREEN:
                if (block32_21_4_prev == null || block32_21_4_prev.getALSN() != TaskAlsn.ALSN_GREEN) cnt_green.incCnt();
                incSecond(cnt_green, block32_21_4_prev, block32_21_4);
                cnt_green.incDistance(d);
                break;
            case TaskAlsn.ALSN_REDYELLOW:
                if (block32_21_4_prev == null || block32_21_4_prev.getALSN() != TaskAlsn.ALSN_REDYELLOW) cnt_red_yellow.incCnt();
                incSecond(cnt_red_yellow, block32_21_4_prev, block32_21_4);
                cnt_red_yellow.incDistance(d);
                break;
            case TaskAlsn.ALSN_RED:
                if (block32_21_4_prev == null || block32_21_4_prev.getALSN() != TaskAlsn.ALSN_RED) cnt_red.incCnt();
                incSecond(cnt_red, block32_21_4_prev, block32_21_4);
                cnt_red.incDistance(d);
                break;
        }
    }

    public CntAlsn getCnt_yellow() {
        return cnt_yellow;
    }

    public CntAlsn getCnt_white() {
        return cnt_white;
    }

    public CntAlsn getCnt_green() {
        return cnt_green;
    }

    public CntAlsn getCnt_red_yellow() {
        return cnt_red_yellow;
    }

    public CntAlsn getCnt_red() {
        return cnt_red;
    }

    /**
     * @param iSection - индекс секции
     * @return - показание счетчика расхода энергии на начало поездки
     */
    public long getCnt_start_act(int iSection) {
        long result;
        switch (iSection) {
            case 0:
                result = cnt_start_act1;
                break;
            case 1:
                result = cnt_start_act2;
                break;
            case 2:
                result = cnt_start_act3;
                break;
            case 3:
                result = cnt_start_act4;
                break;
            default:
                result = 0;
        }
        if (result == 0 && iSection == 0)
            result = cnt_start_act;

        return result;
    }

    /**
     * @param iSection - индекс секции
     * @return - показание счетчика расхода энергии на конец поездки
     */
    public long getCnt_end_act(int iSection) {
        long result;
        switch (iSection) {
            case 0:
                result = cnt_end_act1;
                break;
            case 1:
                result = cnt_end_act2;
                break;
            case 2:
                result = cnt_end_act3;
                break;
            case 3:
                result = cnt_end_act4;
                break;
            default:
                result = -1;
        }
        if (result == -1 && iSection == 0)
            result = cnt_end_act;

        return result;
    }

    /**
     * @param iSection - индекс секции
     * @return - расход активной энергии
     */
    public long getAct(int iSection) {
        switch (iSection) {
            case 0: return act1;
            case 1: return act2;
            case 2: return act3;
            case 3: return act4;
            default: return -1;
        }
    }

    /**
     * @param iSection - индекс секции
     * @return - рекуперация
     */
    public long getRec(int iSection) {
        switch (iSection) {
            case 0: return rec1;
            case 1: return rec2;
            case 2: return rec3;
            case 3: return rec4;
            default: return -1;
        }
    }

    public String getStationBegin() {
        return stations.isEmpty() ? "" : stations.get(0).getNameStation();
    }

    public String getStationEnd() {
        return stations.isEmpty() ? "" : stations.get(stations.size() - 1).getNameStation();
    }

    public String getStationsBeginEnd() {
        return stations.isEmpty() ? "" : stations.get(0).getNameStation() + " - " + stations.get(stations.size() - 1).getNameStation();
    }

    public int getEmptyCnt() {
        int emptyCnt = 0;
        for (ItemWag item : wags) if (item.getWeight() == 0) emptyCnt++;
        return emptyCnt;
    }

    public static boolean isCHS7(int typeLoc) {
        return  typeLoc == CHS7 ||
                typeLoc == CHS7_EKS ||
                typeLoc == CHS7_EKS2 ||
                typeLoc == CHS7_KAUD_F ||
                typeLoc == CHS7_KAUD_L;
    }

    /**
     * данные для ИСАВПРТ(М)
     * @return режим исапрт(м) включен
     */
    public boolean isIsavprt() {
        return getStIsavprt() == 1 || getStIsavprt() == 2 || getStIsavprt() == 4;
    }

    /**
     * данные для ИСАВПРТ(М)
     *  -1 - исавпрт отсутствует
     *  1 - исавпрт исправен
     *  0 - исавпрт неисправен
     * @return исправность исавпрт
     */
    public int getIavprtIsOk() {
        if (isIsavprt()) {
            if(getMainLinkModem().getPercent() > 97)
                return 1;
            else
                return 0;
        }
        return -1;
    }

    /**
     * данные для ИСАВПРТ(М)
     * 0 - РТ ведущий
     * 1 - РТ ведомый
     * 40 - вирт сцепка ведущий
     * 41... - вирт сцепка ведомый
     * -1 - один
     * @return ведомый или ведущий
     */
    public int getIsMainOrSlave() {
        // всц
        if (getStIsavprt() == 4) {
            if (getCodePositionTrain() < 1) return -1;
            return 40 + (getCodePositionTrain() - 1);
        }
        // рт
        else if (getStIsavprt() == 1)
            return 0;
        else if (getStIsavprt() == 2)
            return 1;
        return -1;
    }

    /**
     * @return % от общего пробега в режиме автоведения
     */
    public double getPercentAuto() {
        if (distance > 0) {
            return distance_auto * 100.0 / distance;
        }
        return 0;
    }

    /**
     * @return % от общего пробега в режиме советчик
     */
    public double getPercentPrompt() {
        if (distance > 0) {
            return distance_prompt * 100.0 / distance;
        }
        return 0;
    }

}
