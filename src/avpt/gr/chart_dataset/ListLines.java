package avpt.gr.chart_dataset;

import java.util.ArrayList;

/**
 * Содержит поля всех линий:
 * напряжения, токи, расх энергии, сила, расстояние до хвоста,
 * давление, скорость, профиль, позиция контроллера;
 * массивы формируются в ChartArrays
 *
 * где class ItemLine - точки линий
 * second - секунда
 * value - значение
 * typeLoc - тип локомотива
 * участок добавляется если value предшесвующий неравен value текущему
 *
 * для завершения линии необходимо отработать complete() для продолжения линии до конца поездки
 */
public class ListLines {
    // поезд
    private final ArrayList<ItemLine> listTrains = new ArrayList<ItemLine>();
    // напряжение контактной сети
    private final ArrayList<ItemLine> listVoltage = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltage_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltage_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltage_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltage_s4 = new ArrayList<ItemLine>();
    // напряжение двигателя
    private final ArrayList<ItemLine> listVoltageEngine_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine1_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine2_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine3_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine4_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine1_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine2_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine3_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine4_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine1_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine2_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine3_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine4_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine1_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine2_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine3_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageEngine4_s4 = new ArrayList<ItemLine>();
    // напряжение бхв акб
    private final ArrayList<ItemLine> listVoltageBhvAkbGet = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listVoltageBhvAkbSend = new ArrayList<ItemLine>();
    // общий ток
    private final ArrayList<ItemLine> listAmperage = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperage_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperage_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperage_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperage_s4 = new ArrayList<ItemLine>();
    // ток якоря
    private final ArrayList<ItemLine> listAmperageAnchor     = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor1_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor1_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor1_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor1_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor2_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor2_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor2_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor2_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor3_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor3_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor3_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor3_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor4_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor4_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor4_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageAnchor4_s4 = new ArrayList<ItemLine>();
    // ток возбуждения
    private final ArrayList<ItemLine> listAmperageExcitation    = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation1_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation1_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation1_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation1_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation2_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation2_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation2_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation2_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation3_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation3_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation3_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation3_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation4_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation4_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation4_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listAmperageExcitation4_s4 = new ArrayList<ItemLine>();
    // расход энергии
    private final ArrayList<ItemLine> listConsumptionEn = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_r = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_r_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_r_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_r_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listConsumptionEn_r_s4 = new ArrayList<ItemLine>();
    // показания счетчика энергии
    private final ArrayList<ItemLine> listCntEn = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_s4 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_r = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_r_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_r_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_r_s3 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listCntEn_r_s4 = new ArrayList<ItemLine>();
    // сила реализованная: 0x53 сила тяги положительная и 0x5C сила торможения отрицательная
    private final ArrayList<ItemLine> s5k_listPowerImplementation = new ArrayList<ItemLine>();
    // сила задание контроллера
    private final ArrayList<ItemLine> listPowerTaskControl = new ArrayList<ItemLine>();
    // сила макс тяга
    private final ArrayList<ItemLine> listPowerMax = new ArrayList<ItemLine>();
    // сила макс рекуп
    private final ArrayList<ItemLine> listPowerMaxRec = new ArrayList<ItemLine>();
    // сила задание автоведения
    private final ArrayList<ItemLine> listPowerTaskAuto = new ArrayList<ItemLine>();
    // давление ПМ
    private final ArrayList<ItemLine> listPressPM = new ArrayList<ItemLine>();            // 0x5E_S5K
    // скорость мсуд (ЭС5К)
    private final ArrayList<ItemLine> s5k_listSpeed_MSUD = new ArrayList<ItemLine>();         // 0x5E_S5K

    // сила тяги второго
    private final ArrayList<ItemLine> listPowerLocomotiveSlave = new ArrayList<ItemLine>();   // 0x21-B
    // расстояние до хвоста
    private final ArrayList<ItemLine> listDistanceTail = new ArrayList<ItemLine>();           // 0x1D-E, 0x21-4
    // давление УСАВП
    private final ArrayList<ItemLine> listPressUR_USAVP = new ArrayList<ItemLine>();          // 0x21-4 давление УР
    private final ArrayList<ItemLine> listPressTM_USAVP = new ArrayList<ItemLine>();          // 0x21-4 давление ТМ
    private final ArrayList<ItemLine> listPressTC_USAVP = new ArrayList<ItemLine>();          // 0x21-4 давление ТЦ
    private final ArrayList<ItemLine> listPressZTS_USAVP = new ArrayList<ItemLine>();          // ЗТС длч пассажирского
    // давление
    private final ArrayList<ItemLine> listPressUR = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTM = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTC = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressNM = new ArrayList<ItemLine>();

    private final ArrayList<ItemLine> listPressUR_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTM_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTC_s1 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressUR_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTM_s2 = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressTC_s2 = new ArrayList<ItemLine>();

    private final ArrayList<ItemLine> listPressTmTail = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPressWorkCameraBhv = new ArrayList<ItemLine>();
    // скорость
    private final ArrayList<ItemLine> listSpeedLimitTemp = new ArrayList<ItemLine>();         // временные огранчения
    private final ArrayList<ItemLine> listSpeedLimitConst = new ArrayList<ItemLine>();        // постоянные ограничения
    private final ArrayList<ItemLine> listSpeed_USAVP = new ArrayList<ItemLine>();     // 0x21-4 скорость
    private final ArrayList<ItemLine> listSpeed_CLUB = new ArrayList<ItemLine>();             // 0x21-E скорость клуб
    private final ArrayList<ItemLine> listSpeed_BS_DPS = new ArrayList<ItemLine>();         // скорость БС ДПС
    private final ArrayList<ItemLine> listSpeed_BUT = new ArrayList<ItemLine>();             //
    private final ArrayList<ItemLine> listSpeed_BLOCK = new ArrayList<ItemLine>();             //
    private final ArrayList<ItemLine> listSpeed_Task = new ArrayList<ItemLine>();             //
    private final ArrayList<ItemLine> listSpeed_ERG = new ArrayList<ItemLine>();             // 0x21-E скорость расчетная по эргу
    private final ArrayList<ItemLine> listPermissibleSpeed_CLUB = new ArrayList<ItemLine>();  // 0x21-E допустимая скорость
    private final ArrayList<ItemLine> listSpeedSlave = new ArrayList<ItemLine>();             // 0x1D-E скорость второго
    private final ArrayList<ItemLine> listSpeedMax = new ArrayList<ItemLine>();               // максимальная скорость
    private final ArrayList<ItemLine> listCurSpeedLimit = new ArrayList<ItemLine>();          //  тек ограничение
    // асим
    private final ArrayList<ItemLine> listSpeedCalc = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listSpeedFact = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listSpeedGPS = new ArrayList<ItemLine>();

    private final ArrayList<ItemLine> listLevelGSM = new ArrayList<ItemLine>();               // 0x21-7
    //
    private final ArrayList<ItemLine> listWeakField = new ArrayList<ItemLine>();         // 0x21-4 ослабление поля
    private final ArrayList<ItemLine> listPositionS5k = new ArrayList<ItemLine>();               // 0x21-4 позиция контроллера
    private final ArrayList<ItemLine> listPosition = new ArrayList<ItemLine>();
    private final ArrayList<ItemLine> listPositionTaskAuto = new ArrayList<ItemLine>();
    // профиль
    private final ArrayList<ItemLine> listProfile = new ArrayList<ItemLine>();                // 0x21-9
    private final  ArrayList<ItemLine> listProfileDirect = new ArrayList<ItemLine>();           // опорная линия для профиля
    // ограничения
    private final ArrayList<ItemLine> listTempLimit = new ArrayList<ItemLine>();        // 0x21-3 - временные ограничения
    // карта (вспомогательная невидимая линия состоит из двух точек секунда начала и секунда конца поездки)
    private final ArrayList<ItemLine> listMapDirect = new ArrayList<ItemLine>();
    // линия под иконками станций
    private final ArrayList<ItemLine> listMapLIne = new ArrayList<ItemLine>();

    /**
     * начало участка с определенным значением
     * second - секунда начала участка
     * second_last - последняя секунда необходима для завершения промежутка с одинаковыми значениями
     * val - значение
     * typeLoc - тип локомотива
     */
    public static class ItemLine {
        private int second;
        private int second_last;
        private double value;

        public ItemLine(int second, double value) {
            this.second = second;
            this.value = value;
        }

        public int getSecond() {
            return second;
        }

        public double getValue() {
            return value;
        }

        public int getSecond_last() {
            return second_last;
        }

        public void setSecond_last(int second_last) {
            this.second_last = second_last;
        }
    }

    /**
     * используем только для постоянных ограничений
     * добавляем item только если текущее значение отличается от предыдущего (первые несколько секунд добавляем все подряд)
     * @param arrDots - список точек для одной линии
     * @param second - текущая секунда
     * @param val - текущее  значение
     */
    public void add_lim_map(ArrayList<ItemLine> arrDots, int second, double val) {
        double curVal = Double.NaN;
        if (arrDots.size() > 0) {
            ListLines.ItemLine item = arrDots.get(arrDots.size() - 1);
            curVal = item.value;
            item.second_last = second;
        }
        if (Double.compare(curVal, val) != 0) {
            arrDots.add(new ItemLine(second, val));
        }
    }

    /**
     * добавляем item только если текущее значение отличается от предыдущего
     * приостанавливаем прорисовку линии при достижении продолжительности max_second (для усавп 2 секунды)
     * @param arrDots - список точек для одной линии
     * @param second - текущая секунда
     * @param value - текущее  значение
     * @param max_second - максимальное количество секунд для продолжения прорисовки (-1 выключене проверки на max_second)
     */
    public void add(ArrayList<ItemLine> arrDots, int second, double value, int max_second) {
        if (arrDots.size() == 0) {
            ItemLine item = new ItemLine(second, value);
            item.second_last = second;
            arrDots.add(item);
        }
        else {
            ItemLine item = arrDots.get(arrDots.size() - 1);
            double prev_val = item.value;
            int second_last = item.second_last;
            int d = second - second_last;
            if (max_second > -1 && d > max_second) {
                item = new ItemLine(second_last, Double.NaN);
                arrDots.add(item);
            }
            if (prev_val != value) {
                item = new ItemLine(second, value);
                item.second_last = second;
                arrDots.add(item);
            }
            else
                arrDots.get(arrDots.size() - 1).second_last = second;
        }
    }

    /**
     * прямая опорная линия
     * @param arrDots -
     * @param arrDotsDirect - список точек для одной линии
     * @param secondStart - начальная секунда
     * @param secondEnd - конечная секунда
     * @param val - значение
     * @param isClose - замкнуть
     */
    public static void addDirect(ArrayList<ItemLine> arrDots, ArrayList<ItemLine> arrDotsDirect,
                                 int secondStart, int secondEnd, double val, boolean isClose) {
        if (isClose) {
            double val_top = arrDots.get(0).getValue();
            int second_last = arrDots.get(0).getSecond_last();
            arrDotsDirect.add(new ItemLine(secondStart, val_top));
            arrDotsDirect.add(new ItemLine(second_last, val));
        }
        else
            arrDotsDirect.add(new ItemLine(secondStart, val));
        arrDotsDirect.add(new ItemLine(secondEnd, val));
    }

    /**
     * устанавливаем значение для последней секунды
     * @param arrDots - список точек для одной линии
     * @param lastSecond - последняя секунда в списке
     */
    public void complete(ArrayList<ItemLine> arrDots, int lastSecond) {
        if (arrDots.size() == 0) return;
        ItemLine item = arrDots.get(arrDots.size() - 1);
        if (item.second_last > 0 && item.second_last <= lastSecond) {
            arrDots.add(new ItemLine(item.second_last, item.getValue()));
        }
    }

    // поезд
    public ArrayList<ItemLine> getListTrains() {
        return listTrains;
    }

    // напряжение
    public ArrayList<ItemLine> getListVoltageEngine1_s1() {
        return listVoltageEngine1_s1;
    }

    public ArrayList<ItemLine> getListVoltageEngine1_s2() {
        return listVoltageEngine1_s2;
    }

    public ArrayList<ItemLine> getListVoltageEngine1_s3() {
        return listVoltageEngine1_s3;
    }

    public ArrayList<ItemLine> getListVoltageEngine2_s1() {
        return listVoltageEngine2_s1;
    }

    public ArrayList<ItemLine> getListVoltageEngine2_s2() {
        return listVoltageEngine2_s2;
    }

    public ArrayList<ItemLine> getListVoltageEngine2_s3() {
        return listVoltageEngine2_s3;
    }

    public ArrayList<ItemLine> getListVoltageEngine3_s1() {
        return listVoltageEngine3_s1;
    }

    public ArrayList<ItemLine> getListVoltageEngine3_s2() {
        return listVoltageEngine3_s2;
    }

    public ArrayList<ItemLine> getListVoltageEngine3_s3() {
        return listVoltageEngine3_s3;
    }

    public ArrayList<ItemLine> getListVoltageEngine4_s1() {
        return listVoltageEngine4_s1;
    }

    public ArrayList<ItemLine> getListVoltageEngine4_s2() {
        return listVoltageEngine4_s2;
    }

    public ArrayList<ItemLine> getListVoltageEngine4_s3() {
        return listVoltageEngine4_s3;
    }

    // токи
    public ArrayList<ItemLine> getListAmperageAnchor() {
        return listAmperageAnchor;
    }

    public ArrayList<ItemLine> getListAmperageExcitation() {
        return listAmperageExcitation;
    }

    public ArrayList<ItemLine> getListAmperageAnchor1_s1() {
        return listAmperageAnchor1_s1;
    }

    public ArrayList<ItemLine> getListAmperageExcitation1_s1() {
        return listAmperageExcitation1_s1;
    }

    public ArrayList<ItemLine> getListAmperageExcitation_s1() {
        return listAmperageExcitation_s1;
    }

    public ArrayList<ItemLine> getListAmperageAnchor1_s2() {
        return listAmperageAnchor1_s2;
    }

    public ArrayList<ItemLine> getListAmperageExcitation1_s2() {
        return listAmperageExcitation1_s2;
    }

    public ArrayList<ItemLine> getListAmperageExcitation_s2() {
        return listAmperageExcitation_s2;
    }

    public ArrayList<ItemLine> getListAmperageAnchor1_s3() {
        return listAmperageAnchor1_s3;
    }

    public ArrayList<ItemLine> getListAmperageExcitation1_s3() {
        return listAmperageExcitation1_s3;
    }

    public ArrayList<ItemLine> getListAmperageAnchor2_s1() {
        return listAmperageAnchor2_s1;
    }

    public ArrayList<ItemLine> getListAmperageExcitation2_s1() {
        return listAmperageExcitation2_s1;
    }

    public ArrayList<ItemLine> getListAmperageAnchor2_s2() {
        return listAmperageAnchor2_s2;
    }

    public ArrayList<ItemLine> getListAmperageExcitation2_s2() {
        return listAmperageExcitation2_s2;
    }

    public ArrayList<ItemLine> getListAmperageAnchor2_s3() {
        return listAmperageAnchor2_s3;
    }

    public ArrayList<ItemLine> getListAmperageExcitation2_s3() {
        return listAmperageExcitation2_s3;
    }

    public ArrayList<ItemLine> getListAmperageAnchor3_s1() {
        return listAmperageAnchor3_s1;
    }

    public ArrayList<ItemLine> getListAmperageExcitation3_s1() {
        return listAmperageExcitation3_s1;
    }

    public ArrayList<ItemLine> getListAmperageAnchor3_s2() {
        return listAmperageAnchor3_s2;
    }

    public ArrayList<ItemLine> getListAmperageExcitation3_s2() {
        return listAmperageExcitation3_s2;
    }

    public ArrayList<ItemLine> getListAmperageAnchor3_s3() {
        return listAmperageAnchor3_s3;
    }

    public ArrayList<ItemLine> getListAmperageExcitation3_s3() {
        return listAmperageExcitation3_s3;
    }

    public ArrayList<ItemLine> getListAmperageAnchor4_s1() {
        return listAmperageAnchor4_s1;
    }

    public ArrayList<ItemLine> getListAmperageExcitation4_s1() {
        return listAmperageExcitation4_s1;
    }

    public ArrayList<ItemLine> getListAmperageAnchor4_s2() {
        return listAmperageAnchor4_s2;
    }

    public ArrayList<ItemLine> getListAmperageExcitation4_s2() {
        return listAmperageExcitation4_s2;
    }

    public ArrayList<ItemLine> getListAmperageAnchor4_s3() {
        return listAmperageAnchor4_s3;
    }

    public ArrayList<ItemLine> getListAmperageExcitation4_s3() {
        return listAmperageExcitation4_s3;
    }

    // сила
    public ArrayList<ItemLine> getS5k_listPowerImplementation() {
        return s5k_listPowerImplementation;
    }

    public ArrayList<ItemLine> getListPowerLocomotiveSlave() {
        return listPowerLocomotiveSlave;
    }

    public ArrayList<ItemLine> getListPowerTaskControl() {
        return listPowerTaskControl;
    }

    public ArrayList<ItemLine> getListPowerMax() {
        return listPowerMax;
    }

    public ArrayList<ItemLine> getListPowerMaxRec() {
        return listPowerMaxRec;
    }

    public ArrayList<ItemLine> getListPowerTaskAuto() {
        return listPowerTaskAuto;
    }

    // давление
    public ArrayList<ItemLine> getListPressUR_USAVP() {
        return listPressUR_USAVP;
    }

    public ArrayList<ItemLine> getListPressTM_USAVP() {
        return listPressTM_USAVP;
    }

    public ArrayList<ItemLine> getListPressTC_USAVP() {
        return listPressTC_USAVP;
    }

    public ArrayList<ItemLine> getListPressZTS_USAVP() {
        return listPressZTS_USAVP;
    }

    public ArrayList<ItemLine> getListPressPM() {
        return listPressPM;
    }

    // скорость
    public ArrayList<ItemLine> getListSpeedLimitTemp() {
        return listSpeedLimitTemp;
    }

    public ArrayList<ItemLine> getListSpeedLimitConst() {
        return listSpeedLimitConst;
    }

    public ArrayList<ItemLine> getListSpeed_USAVP() {
        return listSpeed_USAVP;
    }

    public ArrayList<ItemLine> getListSpeed_ERG() {
        return listSpeed_ERG;
    }

    public ArrayList<ItemLine> getListSpeed_CLUB() {
        return listSpeed_CLUB;
    }

    public ArrayList<ItemLine> getListSpeed_BS_DPS() {
        return listSpeed_BS_DPS;
    }

    public ArrayList<ItemLine> getListPermissibleSpeed_CLUB() {
        return listPermissibleSpeed_CLUB;
    }

    public ArrayList<ItemLine> getListSpeed_BUT() {
        return listSpeed_BUT;
    }

    public ArrayList<ItemLine> getListSpeed_BLOCK() {
        return listSpeed_BLOCK;
    }

    public ArrayList<ItemLine> getListSpeed_Task() {
        return listSpeed_Task;
    }

    public ArrayList<ItemLine> getListSpeedSlave() {
        return listSpeedSlave;
    }

    public ArrayList<ItemLine> getListSpeedMax() {
        return listSpeedMax;
    }

    public ArrayList<ItemLine> getListCurSpeedLimit() {
        return listCurSpeedLimit;
    }

    public ArrayList<ItemLine> getS5k_listSpeed_MSUD() {
        return s5k_listSpeed_MSUD;
    }

    public ArrayList<ItemLine> getListLevelGSM() {
        return listLevelGSM;
    }

    public ArrayList<ItemLine> getListSpeedCalc() {
        return listSpeedCalc;
    }

    public ArrayList<ItemLine> getListSpeedFact() {
        return listSpeedFact;
    }

    public ArrayList<ItemLine> getListSpeedGPS() {
        return listSpeedGPS;
    }

    // ослаблене поля
    public ArrayList<ItemLine> getListWeakField() {
        return listWeakField;
    }

    // позиция
    public ArrayList<ItemLine> getListPositionS5k() {
        return listPositionS5k;
    }

    public ArrayList<ItemLine> getListPosition() {
        return listPosition;
    }

    // задание автоведения
    public ArrayList<ItemLine> getListPositionTaskAuto() {
        return listPositionTaskAuto;
    }

    public ArrayList<ItemLine> getListConsumptionEn_s1() {
        return listConsumptionEn_s1;
    }

    public ArrayList<ItemLine> getListConsumptionEn_s2() {
        return listConsumptionEn_s2;
    }

    public ArrayList<ItemLine> getListConsumptionEn_s3() {
        return listConsumptionEn_s3;
    }

    public ArrayList<ItemLine> getListConsumptionEn_s4() {
        return listConsumptionEn_s4;
    }

    public ArrayList<ItemLine> getListDistanceTail() {
        return listDistanceTail;
    }

    public ArrayList<ItemLine> getListProfile() {
        return listProfile;
    }

    public ArrayList<ItemLine> getListProfileDirect() {
        return listProfileDirect;
    }

    public ArrayList<ItemLine> getListVoltage() {
        return listVoltage;
    }

    public ArrayList<ItemLine> getListVoltage_s1() {
        return listVoltage_s1;
    }

    public ArrayList<ItemLine> getListVoltage_s2() {
        return listVoltage_s2;
    }

    public ArrayList<ItemLine> getListVoltage_s3() {
        return listVoltage_s3;
    }

    public ArrayList<ItemLine> getListVoltage_s4() {
        return listVoltage_s4;
    }

    public ArrayList<ItemLine> getListAmperage() {
        return listAmperage;
    }

    public ArrayList<ItemLine> getListAmperage_s1() {
        return listAmperage_s1;
    }

    public ArrayList<ItemLine> getListAmperage_s2() {
        return listAmperage_s2;
    }

    public ArrayList<ItemLine> getListAmperage_s3() {
        return listAmperage_s3;
    }

    public ArrayList<ItemLine> getListAmperage_s4() {
        return listAmperage_s4;
    }

    public ArrayList<ItemLine> getListTempLimit() {
        return listTempLimit;
    }

    public ArrayList<ItemLine> getListMapDirect() {
        return listMapDirect;
    }

    public ArrayList<ItemLine> getListMapLine() {
        return listMapLIne;
    }

    public ArrayList<ItemLine> getListVoltageEngine1_s4() {
        return listVoltageEngine1_s4;
    }

    public ArrayList<ItemLine> getListVoltageEngine2_s4() {
        return listVoltageEngine2_s4;
    }

    public ArrayList<ItemLine> getListVoltageEngine3_s4() {
        return listVoltageEngine3_s4;
    }

    public ArrayList<ItemLine> getListVoltageEngine4_s4() {
        return listVoltageEngine4_s4;
    }

    public ArrayList<ItemLine> getListVoltageBhvAkbGet() {
        return listVoltageBhvAkbGet;
    }

    public ArrayList<ItemLine> getListVoltageBhvAkbSend() {
        return listVoltageBhvAkbSend;
    }

    public ArrayList<ItemLine> getListAmperageAnchor1_s4() {
        return listAmperageAnchor1_s4;
    }

    public ArrayList<ItemLine> getListAmperageAnchor2_s4() {
        return listAmperageAnchor2_s4;
    }

    public ArrayList<ItemLine> getListAmperageAnchor3_s4() {
        return listAmperageAnchor3_s4;
    }

    public ArrayList<ItemLine> getListAmperageAnchor4_s4() {
        return listAmperageAnchor4_s4;
    }

    public ArrayList<ItemLine> getListAmperageExcitation1_s4() {
        return listAmperageExcitation1_s4;
    }

    public ArrayList<ItemLine> getListAmperageExcitation2_s4() {
        return listAmperageExcitation2_s4;
    }

    public ArrayList<ItemLine> getListAmperageExcitation3_s4() {
        return listAmperageExcitation3_s4;
    }

    public ArrayList<ItemLine> getListAmperageExcitation4_s4() {
        return listAmperageExcitation4_s4;
    }

    public ArrayList<ItemLine> getListMapLIne() {
        return listMapLIne;
    }

    public ArrayList<ItemLine> getListVoltageEngine_s1() {
        return listVoltageEngine_s1;
    }

    public ArrayList<ItemLine> getListVoltageEngine_s2() {
        return listVoltageEngine_s2;
    }

    public ArrayList<ItemLine> getListConsumptionEn() {
        return listConsumptionEn;
    }

    public ArrayList<ItemLine> getListConsumptionEn_r() {
        return listConsumptionEn_r;
    }

    public ArrayList<ItemLine> getListConsumptionEn_r_s1() {
        return listConsumptionEn_r_s1;
    }

    public ArrayList<ItemLine> getListConsumptionEn_r_s2() {
        return listConsumptionEn_r_s2;
    }

    public ArrayList<ItemLine> getListConsumptionEn_r_s3() {
        return listConsumptionEn_r_s3;
    }

    public ArrayList<ItemLine> getListConsumptionEn_r_s4() {
        return listConsumptionEn_r_s4;
    }

    public ArrayList<ItemLine> getListCntEn() {
        return listCntEn;
    }

    public ArrayList<ItemLine> getListCntEn_s1() {
        return listCntEn_s1;
    }

    public ArrayList<ItemLine> getListCntEn_s2() {
        return listCntEn_s2;
    }

    public ArrayList<ItemLine> getListCntEn_s3() {
        return listCntEn_s3;
    }

    public ArrayList<ItemLine> getListCntEn_s4() {
        return listCntEn_s4;
    }

    public ArrayList<ItemLine> getListCntEn_r() {
        return listCntEn_r;
    }

    public ArrayList<ItemLine> getListCntEn_r_s1() {
        return listCntEn_r_s1;
    }

    public ArrayList<ItemLine> getListCntEn_r_s2() {
        return listCntEn_r_s2;
    }

    public ArrayList<ItemLine> getListCntEn_r_s3() {
        return listCntEn_r_s3;
    }

    public ArrayList<ItemLine> getListCntEn_r_s4() {
        return listCntEn_r_s4;
    }

    public ArrayList<ItemLine> getListPressUR_s1() {
        return listPressUR_s1;
    }

    public ArrayList<ItemLine> getListPressUR() {
        return listPressUR;
    }

    public ArrayList<ItemLine> getListPressTM() {
        return listPressTM;
    }

    public ArrayList<ItemLine> getListPressTC() {
        return listPressTC;
    }

    public ArrayList<ItemLine> getListPressNM() {
        return listPressNM;
    }

    public ArrayList<ItemLine> getListPressTM_s1() {
        return listPressTM_s1;
    }

    public ArrayList<ItemLine> getListPressTC_s1() {
        return listPressTC_s1;
    }

    public ArrayList<ItemLine> getListPressUR_s2() {
        return listPressUR_s2;
    }

    public ArrayList<ItemLine> getListPressTM_s2() {
        return listPressTM_s2;
    }

    public ArrayList<ItemLine> getListPressTC_s2() {
        return listPressTC_s2;
    }

    public ArrayList<ItemLine> getListPressTmTail() {
        return listPressTmTail;
    }

    public ArrayList<ItemLine> getListPressWorkCameraBhv() {
        return listPressWorkCameraBhv;
    }
}
