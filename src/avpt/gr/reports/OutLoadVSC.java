package avpt.gr.reports;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32_gp;
import avpt.gr.blocks32.overall.*;
import avpt.gr.chart_dataset.ChartArrays;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartPanelArm;
import avpt.gr.maps.Limits;
import avpt.gr.maps.Objects;
import avpt.gr.maps.Stations;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import avpt.gr.train.Train;

import java.util.ArrayList;

import static avpt.gr.blocks32.SubIdGr.getSubId;
import static avpt.gr.chart_dataset.TaskAlsn.*;
import static avpt.gr.chart_dataset.TaskAutoDrive.*;
import static avpt.gr.common.HelperExl_POI.getStyle;
import static avpt.gr.common.UtilsArmG.formatDate;
import static avpt.gr.common.UtilsArmG.formatTime;
import static avpt.gr.train.Train.S5K;
import static avpt.gr.train.Train.S5K_2;

public class OutLoadVSC {

    private final XSSFSheet sheet;
    private final Train train;
    private final ArrBlock32 arrBlock32;
    private final ArrayList<Stations.Station> stations_train;
    private final CellStyle styleRight;
    private final CellStyle styleCenter;
    private boolean isBrake = false;
    private boolean isTm = false;

    /**
     * режим работы
     * 1 - стоянка
     * 2 - тяга
     * 3 - выбег
     * 4 - рекуперация
     * 5 - превматическое торможение
     * 6 - комбинированное торможение
     */
    public static class ModeWork {
        private final int code;
        private final String name;

        public ModeWork(int code, String name) {
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

    /**
     * выгрузка для виртуальной сцепки
     * @param sheet - лист Excel
     * @param chartPanelArm -
     * @param train -
     */
    public OutLoadVSC(XSSFSheet sheet, ChartPanelArm chartPanelArm, Train train) {
        this.sheet = sheet;
        this.train = train;
        arrBlock32 = chartPanelArm.getChartDataset().getArrBlock32();
        Stations stations = chartPanelArm.getChartArm().getStations();
        int secondStart = train != null ? train.getSecondStart() : arrBlock32.get(0).getSecond();
        int secondEnd = train != null ? train.getSecondsEnd() : arrBlock32.get(arrBlock32.size() - 1).getSecond();
        stations_train = stations.getStations(secondStart, secondEnd);
        styleRight = getStyle(sheet, CellStyle.ALIGN_RIGHT, true);
        styleCenter = getStyle(sheet, CellStyle.ALIGN_CENTER, true);
    }

    /**
     * выгрузка заголовка
     * @return - row
     */
    private int outHead() {
        int row = 0;
        ExcelReports.toCell(sheet, ++row, 3, Train.getNameTypeLoc(train.getTypeLoc(), train.getLocTypeAsoup()), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getNumLoc() == -1 ? "" : "" + train.getNumLoc(), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getNumTrain(), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getPositionTrain(), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getWeightTrain(), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getCntWags(), styleRight);
        ExcelReports.toCell(sheet, ++row, 3, train.getNumSlave(), styleRight);
        if (!stations_train.isEmpty()) {
            ExcelReports.toCell(sheet, ++row, 3, stations_train.get(0).getNameStation(), styleRight);
            ExcelReports.toCell(sheet, ++row, 3, stations_train.get(stations_train.size() - 1).getNameStation(), styleRight);
        } else
            row += 2;
        ExcelReports.toCell(sheet, ++row, 3, train.getRoutName(), styleRight);
        return row;
    }

    /**
     * выгрузка данных
     */
    public void outLoad() {
        int row = train != null ? outHead() + 5 : 4;

        LocalTime localTime = null;
        LocalTime localTimePrev = null;
        LocalDate localDate = null;
        long start_coordinate = -1;;
        long cur_coordinate = 0;
        long prev_coordinate = start_coordinate;
        long cur_second = 0;
        long cur_lim = 0;
        int cnt = 0;
        double voltageK_s5k = 0.001;                // множитель для напряжения контактной
        double ks1 = 0;                             // напряжение контактной сети А

        // предшествующяя посылка энергии
        avpt.gr.blocks32.s5k.Block32_57 block32_57_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_58 block32_58_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_59 block32_59_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_5A block32_5A_s5k_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_57 block32_57_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_58 block32_58_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_59 block32_59_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_5A block32_5A_s5k_2_prev = null;
        // энергия
        int act1 = 0;
        int act2 = 0;
        int act3 = 0;
        int act4 = 0;
        int rec1 = 0;
        int rec2 = 0;
        int rec3 = 0;
        int rec4 = 0;
        StringBuilder travel_obj = new StringBuilder();
        int power_brake = 0;
        int power = 0;
        int speed = 0;
        double tc = 0;
        double ur = 0;
        double task = 0;
        int cntSilent = -1;

        int is_main_link = 0;
        int is_add_link = 0;
        int is_modem_on = 0;
        int num_slave = 0;

        double amperageAnchor1_s1 = 0;
        double amperageExcitation1_s1 = 0;

        long lat = 0;
        long lon = 0;

        int blStart = train != null ? train.getBlStart() : 0;
        int blEnd = train != null ? train.getBlEnd() : arrBlock32.size() - 1;
        int curTypeLoc = 0;
        long curNumLoc = 0;
        int curNumTrain = 0;
        int curOrdNum = 0;
        int curWeightTrain = 0;
        int curCntWags = 0;


        for (int i = blStart; i <= blEnd; i++) {
            Block32_gp block32_gp = arrBlock32.get(i);

            if (block32_gp.getId() == 0x21) {
                int curSubId_21 = getSubId(block32_gp.getId(), block32_gp.getValues());
                switch (curSubId_21) {
                    case 0x01:
                        Block32_21_1 block32_21_1 = new Block32_21_1(block32_gp.getValues());
                        localDate = block32_21_1.getDate();
                        curTypeLoc = block32_21_1.getTypeLoc();
                        curNumTrain = block32_21_1.getNumTrain();
                        curWeightTrain = block32_21_1.getNumTrain();
                        curCntWags = block32_21_1.getNumWags();
                        break;
                    case 0x04:
                        Block32_21_4 block32_21_4 = new Block32_21_4(block32_gp.getValues());
                        localTime = UtilsArmG.getTime(block32_21_4.getSecBeginDay());
                        tc = block32_21_4.getPressTC();
                        ur = block32_21_4.getPressUR();
                        task = block32_21_4.getTask() / 10.0;
                        if (task == ur) cntSilent++;
                        else cntSilent = -1;
                        if (isTm && tc < 0.1) {
                            isBrake = true;
                            cntSilent = -1;
                        }
                        else if (cntSilent > 3)
                            isBrake = false;

                        speed = block32_21_4.getSpeed();
                        int modeAuto = block32_21_4.getModeAuto();
                        int isAuto = modeAuto > 1 ? 1 : 0;
//                        if (localTimePrev != null && localTime != null)
//                            cur_second += Duration.between(localTimePrev, localTime).getSeconds();
                        cur_second += localTimePrev != null && localTime != null ? Duration.between(localTimePrev, localTime).getSeconds() : 0;
                        if (!localTime.equals(localTimePrev)) {
                            ExcelReports.toCell(sheet, ++row, 1, ++cnt, styleCenter);
                            if (localDate != null)
                                ExcelReports.toCell(sheet, row, 2, localDate.format(formatDate), styleRight);
                            else
                                ExcelReports.toCell(sheet, row, 2, "", styleRight);
                            ExcelReports.toCell(sheet, row, 3, localTime.format(formatTime), styleRight);
                            ExcelReports.toCell(sheet, row, 4, train != null ? Train.getNameTypeLoc(train.getTypeLoc(), train.getLocTypeAsoup()) : Train.getNameTypeLoc(curTypeLoc, 0), styleRight);
                            ExcelReports.toCell(sheet, row, 5, train != null ? train.getNumLoc() : curNumLoc, styleRight);
                            ExcelReports.toCell(sheet, row, 6, train != null ? train.getNumTrain() : curNumTrain, styleRight);
                            String curPositionTrain = "";
                            if (curOrdNum == 1) curPositionTrain = "ведущий";
                            else if (curOrdNum > 1) curPositionTrain = String.format("ведомый %d", curOrdNum - 1);
                            ExcelReports.toCell(sheet, row, 7, train != null ? train.getPositionTrain() : curPositionTrain, styleRight);
                            ExcelReports.toCell(sheet, row, 8, train != null ? train.getWeightTrain() : curWeightTrain, styleRight);
                            ExcelReports.toCell(sheet, row, 9, train != null ? train.getCntWags() : curCntWags, styleRight);
                            if (!stations_train.isEmpty()) {
                                ExcelReports.toCell(sheet, row, 10, stations_train.get(0).getEcp(), styleRight);
                                ExcelReports.toCell(sheet, row, 11, stations_train.get(stations_train.size() - 1).getEcp(), styleRight);
                            }

                            long d = block32_21_4.getCoordinate() - prev_coordinate;
                            if (d > 0 && d < 100)
                                cur_coordinate += block32_21_4.getCoordinate() - prev_coordinate;
                            prev_coordinate = block32_21_4.getCoordinate();
                            ExcelReports.toCell(sheet, row, 12, (int)cur_coordinate, styleRight);
                            ExcelReports.toCell(sheet, row, 13, cur_second, styleRight);
                            ExcelReports.toCell(sheet, row, 14, speed, styleRight);
                            ExcelReports.toCell(sheet, row, 15, cur_lim, styleRight);
                            ExcelReports.toCell(sheet, row, 16, block32_gp.getValLimTmp(), styleRight);
                            ExcelReports.toCell(sheet, row, 17, Math.round(ks1), styleRight);
                            ExcelReports.toCell(sheet, row, 18, act1, styleRight);
                            ExcelReports.toCell(sheet, row, 19, act2, styleRight);
                            ExcelReports.toCell(sheet, row, 20, act3, styleRight);
                            ExcelReports.toCell(sheet, row, 21, act4, styleRight);
                            ExcelReports.toCell(sheet, row, 22, rec1, styleRight);
                            ExcelReports.toCell(sheet, row, 23, rec2, styleRight);
                            ExcelReports.toCell(sheet, row, 24, rec3, styleRight);
                            ExcelReports.toCell(sheet, row, 25, rec4, styleRight);
                            ExcelReports.toCell(sheet, row, 26, (long)arrBlock32.get(i).getLen_prof(), styleRight);
                            ExcelReports.toCell(sheet, row, 27, arrBlock32.get(i).getSlope_prof(), styleRight);
                            ExcelReports.toCell(sheet, row, 28, (long)arrBlock32.get(i).getKm(), styleRight);
                            ExcelReports.toCell(sheet, row, 29, (long)arrBlock32.get(i).getPk(), styleRight);
                            ExcelReports.toCell(sheet, row, 30, travel_obj.toString(), styleRight);
                            travel_obj = new StringBuilder();
                            ExcelReports.toCell(sheet, row, 31, getModeWork(speed, power, tc).getName(), styleRight);
                            ExcelReports.toCell(sheet, row, 32, getModeWork(speed, power, tc).getCode(), styleRight);
                            ExcelReports.toCell(sheet, row, 33, block32_21_4.getPosition(), styleRight);
                            ExcelReports.toCell(sheet, row, 34, getAlsnStr(block32_21_4.getALSN()), styleRight);
                            ExcelReports.toCell(sheet, row, 35, isAuto, styleRight);
                            ExcelReports.toCell(sheet, row, 36, modeAuto, styleRight);
                            ExcelReports.toCell(sheet, row, 37, getModeAutoStr(modeAuto), styleRight);
                            ExcelReports.toCell(sheet, row, 38, block32_21_4.getManeuver(), styleRight);
                            ExcelReports.toCell(sheet, row, 39, block32_21_4.getFlagTract(), styleRight);
                            ExcelReports.toCell(sheet, row, 40, block32_21_4.getAdditionChannel(), styleRight);
                            ExcelReports.toCell(sheet, row, 41, is_main_link, styleRight);
                            ExcelReports.toCell(sheet, row, 42, is_add_link, styleRight);
                            ExcelReports.toCell(sheet, row, 43, is_modem_on, styleRight);
                            ExcelReports.toCell(sheet, row, 44, (long)block32_21_4.getBanAuto(), styleRight);
                            ExcelReports.toCell(sheet, row, 45, (long)block32_21_4.getTestTractCorrupt(), styleRight);
                            ExcelReports.toCell(sheet, row, 46, (long)block32_21_4.getTestBrakeCorrupt(), styleRight);
                            ExcelReports.toCell(sheet, row, 47, (long)block32_21_4.getTestPT(), styleRight);
                            ExcelReports.toCell(sheet, row, 48, (long)num_slave, styleRight);
                            int can1 = block32_21_4.getCAN1SingleErr() == 1 ||
                                    block32_21_4.getCAN1ErrWarn() == 1 || block32_21_4.getCAN1ErrPassive() == 1 ? 1 : 0;
                            int can2 = block32_21_4.getCAN2SingleErr() == 1 || block32_21_4.getCAN2ErrWarn() == 1 ? 1 : 0;
                            ExcelReports.toCell(sheet, row, 49, can1, styleRight);
                            ExcelReports.toCell(sheet, row, 50, can2, styleRight);

                            ExcelReports.toCell(sheet, row, 52, amperageAnchor1_s1, styleRight);
                            ExcelReports.toCell(sheet, row, 53, amperageExcitation1_s1, styleRight);
                            ExcelReports.toCell(sheet, row, 54, block32_21_4.getPressTC(), styleRight);
                            ExcelReports.toCell(sheet, row, 55, block32_21_4.getPressTM(), styleRight);
                            ExcelReports.toCell(sheet, row, 56, block32_21_4.getPressUR(), styleRight);
                            ExcelReports.toCell(sheet, row, 57, UtilsArmG.getGps(lat), styleRight);
                            ExcelReports.toCell(sheet, row, 58, UtilsArmG.getGps(lon), styleRight);
                        }
                        localTimePrev = localTime;
                        break;
                    case 0x09:
                        Block32_21_9 block32_21_9 = new Block32_21_9(block32_gp.getValues());
                        int [] arrId = block32_21_9.getArrId();
                        if (Limits.Limit.isLimit(arrId)) {
                            cur_lim = block32_21_9.getLimSpeed();
                        }
                        if (Objects.CheckBrake.isCheckBrake(arrId)) {
                            travel_obj.append("проверка тормозов ");
                        }
                        if (Objects.Crossing.isCrossing(arrId)) {
                            travel_obj.append("переезд ");
                        }
                        if (Objects.NeutralInsert.isNeutralInsert(arrId)) {
                            travel_obj.append("нейтральная вставка ");
                        }
                        if (Objects.Thermometer.isThermometer(arrId)) {
                            travel_obj.append("понаб ");
                        }
                        if (Objects.Traffic_light.isTrafficLight(arrId)) {
                            travel_obj.append("светофор ");
                        }
                        if (Objects.Landslide.isLandslide(arrId)) {
                            travel_obj.append("обрывоопасный участок ");
                        }
                        break;
                    case 0x0E:
                        Block32_21_E block32_21_E = new Block32_21_E(block32_gp.getValues());
                        lat = block32_21_E.getLatitude();
                        lon = block32_21_E.getLongitude();
                        break;
                }
            }

            if (block32_gp.getId() == 0x1D) {
                int subId = getSubId(block32_gp.getId(), block32_gp.getValues());
                //if (subId == 0x09) {
                switch (subId) {
                    case 0x09:
                        Block32_1D_9 block32_1D_9 = new Block32_1D_9(block32_gp.getValues());
                        int idStation =block32_1D_9.getStationId();
                        final int CNT = 20;
                        for (int j = i + 1; j < i + CNT; j++) {
                            if (j >= arrBlock32.size()) break;
                            byte[] values = arrBlock32.get(j).getValues();
                            subId = getSubId(arrBlock32.get(j).getId(), values);
                            if (subId == 0x0A) {
                                Block32_1D_A block32_1D_A = new Block32_1D_A(values);
                                if (block32_1D_A.getStationId() == idStation) {
                                    travel_obj.append(block32_1D_A.getNameStation());
                                    break;
                                }
                            }
                        }
                        break;
                    case 0x0D:
                        Block32_1D_D block32_1D_D = new Block32_1D_D(block32_gp.getValues());
                        is_main_link = block32_1D_D.getLinkVEBR(); //
                        is_add_link = block32_1D_D.getLinkAdditional();
                        is_modem_on = block32_1D_D.getAdditionalChenOn();
                        num_slave = block32_1D_D.getNumSlave();
                        curOrdNum = block32_1D_D.getOrderNum();
                        break;
                }
            }

            int typeLoc = train != null ? train.getTypeLoc() : curTypeLoc;
            if (typeLoc == S5K) {
                switch (block32_gp.getId()){
                    case 0x52:
                        avpt.gr.blocks32.s5k.Block32_52 block32_52 = new avpt.gr.blocks32.s5k.Block32_52(arrBlock32.get(i).getValues());
                        amperageAnchor1_s1 = block32_52.getAmperageAnchor1_s1();
                        amperageExcitation1_s1 = block32_52.getAmperageExcitation1_s1();
                        break;
                    case 0x53:
                        avpt.gr.blocks32.s5k.Block32_53 block32_53 = new avpt.gr.blocks32.s5k.Block32_53(arrBlock32.get(i).getValues());
                        if (power_brake < 0)
                            power = power_brake;
                        else
                            power = block32_53.getPowerLocomotive();
                        break;
                    case 0x57: // расход А
                        avpt.gr.blocks32.s5k.Block32_57 block32_57 = new avpt.gr.blocks32.s5k.Block32_57(arrBlock32.get(i).getValues());
                        voltageK_s5k = block32_57.getVoltageK_s5k();
                        act1 += ChartArrays.getDiffEnCnt(block32_57_s5k_prev != null ? block32_57_s5k_prev.getEnergyAct_s1() : 0, block32_57.getEnergyAct_s1());
                        rec1 += ChartArrays.getDiffEnCnt(block32_57_s5k_prev != null ? block32_57_s5k_prev.getEnergyActRec_s1() : 0, block32_57.getEnergyActRec_s1());;
                        block32_57_s5k_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s5k.Block32_58 block32_58 = new avpt.gr.blocks32.s5k.Block32_58(arrBlock32.get(i).getValues());
                        act2 += ChartArrays.getDiffEnCnt(block32_58_s5k_prev != null ? block32_58_s5k_prev.getEnergyAct_s2() : 0, block32_58.getEnergyAct_s2());
                        rec2 += ChartArrays.getDiffEnCnt(block32_58_s5k_prev != null ? block32_58_s5k_prev.getEnergyActRec_s2() : 0, block32_58.getEnergyActRec_s2());;
                        block32_58_s5k_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s5k.Block32_59 block32_59 = new avpt.gr.blocks32.s5k.Block32_59(arrBlock32.get(i).getValues());
                        act3 += ChartArrays.getDiffEnCnt(block32_59_s5k_prev != null ? block32_59_s5k_prev.getEnergyAct_s3() : 0, block32_59.getEnergyAct_s3());
                        rec3 += ChartArrays.getDiffEnCnt(block32_59_s5k_prev != null ? block32_59_s5k_prev.getEnergyActRec_s3() : 0, block32_59.getEnergyActRec_s3());
                        block32_59_s5k_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s5k.Block32_5A block32_5A = new avpt.gr.blocks32.s5k.Block32_5A(arrBlock32.get(i).getValues());
                        act4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev != null ? block32_5A_s5k_prev.getEnergyAct_s4() : 0, block32_5A.getEnergyAct_s4());
                        rec4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev != null ? block32_5A_s5k_prev.getEnergyActRec_s4() : 0, block32_5A.getEnergyActRec_s4());;
                        block32_5A_s5k_prev = block32_5A;
                        break;
                    case 0x5B: // нарпяжение контактной сети
                        avpt.gr.blocks32.s5k.Block32_5B block32_5B = new avpt.gr.blocks32.s5k.Block32_5B(arrBlock32.get(i).getValues());
                        ks1 = block32_5B.getRmsVoltage_s1() * voltageK_s5k;
                        break;
                    case 0x5C:
                        avpt.gr.blocks32.s5k.Block32_5C block32_5C = new avpt.gr.blocks32.s5k.Block32_5C(arrBlock32.get(i).getValues());
                        power_brake = block32_5C.getPowerBrake();
                        break;
                    case 0x5E:
                        avpt.gr.blocks32.s5k.Block32_5E block32_5E = new avpt.gr.blocks32.s5k.Block32_5E(arrBlock32.get(i).getValues());
                        isTm = block32_5E.getTM() == 1;
                        break;
                }
            }
            else if (typeLoc == S5K_2) {
                switch (block32_gp.getId()){
                    case 0x53:
                        avpt.gr.blocks32.s5k_2.Block32_53 block32_53_2 = new avpt.gr.blocks32.s5k_2.Block32_53(arrBlock32.get(i).getValues());
                        power = (short)block32_53_2.getCommonPower();
                        isTm = block32_53_2.getTM() == 1;
                        break;
                    case 0x54:
                        avpt.gr.blocks32.s5k_2.Block32_54 block32_54 = new avpt.gr.blocks32.s5k_2.Block32_54(arrBlock32.get(i).getValues());
                        amperageAnchor1_s1 = block32_54.getAmperageAnchor1_main();
                        amperageExcitation1_s1 = block32_54.getAmperageExcitation_main();
                        break;
                    case 0x57: // расход А
                        avpt.gr.blocks32.s5k_2.Block32_57 block32_57 = new avpt.gr.blocks32.s5k_2.Block32_57(arrBlock32.get(i).getValues());
                        act1 += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev != null ? block32_57_s5k_2_prev.getEnergyAct_s1() : 0, block32_57.getEnergyAct_s1());
                        rec1 += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev != null ? block32_57_s5k_2_prev.getEnergyActRec_s1() : 0, block32_57.getEnergyActRec_s1());;
                        block32_57_s5k_2_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s5k_2.Block32_58 block32_58 = new avpt.gr.blocks32.s5k_2.Block32_58(arrBlock32.get(i).getValues());
                        act2 += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev != null ? block32_58_s5k_2_prev.getEnergyAct_s2() : 0, block32_58.getEnergyAct_s2());
                        rec2 += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev != null ? block32_58_s5k_2_prev.getEnergyActRec_s2() : 0, block32_58.getEnergyActRec_s2());;
                        block32_58_s5k_2_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s5k_2.Block32_59 block32_59 = new avpt.gr.blocks32.s5k_2.Block32_59(arrBlock32.get(i).getValues());
                        act3 += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev != null ? block32_59_s5k_2_prev.getEnergyAct_s3() : 0, block32_59.getEnergyAct_s3());
                        rec3 += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev != null ? block32_59_s5k_2_prev.getEnergyActRec_s3() : 0, block32_59.getEnergyActRec_s3());
                        block32_59_s5k_2_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s5k_2.Block32_5A block32_5A = new avpt.gr.blocks32.s5k_2.Block32_5A(arrBlock32.get(i).getValues());
                        act4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev != null ? block32_5A_s5k_2_prev.getEnergyAct_s4() : 0, block32_5A.getEnergyAct_s4());
                        rec4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev != null ? block32_5A_s5k_2_prev.getEnergyActRec_s4() : 0, block32_5A.getEnergyActRec_s4());;
                        block32_5A_s5k_2_prev = block32_5A;
                        break;
                    case 0x5B: // нарпяжение контактной сети
                        avpt.gr.blocks32.s5k_2.Block32_5B block32_5B = new avpt.gr.blocks32.s5k_2.Block32_5B(arrBlock32.get(i).getValues());
                        ks1 = block32_5B.getRmsVoltage_s1() * voltageK_s5k;
                        break;
                }
            }

            if (block32_gp.getId() == 0x16 || block32_gp.getId() == 0x56) {
                avpt.gr.blocks32.overall.Block32_16_56 block32_16_56 = new avpt.gr.blocks32.overall.Block32_16_56(arrBlock32.get(i).getValues());
                curNumLoc = block32_16_56.getLocNum();
            }
        }
    }

    /**
     * @param speed - текущая скорость
     * @param power - сила
     * @param tc - давление в цилинд
     * @return рпжем работы локомотива
     */
    private ModeWork getModeWork(int speed, int power, double tc) {
        int code = -1;
        String name = "";
        if (speed == 0) {
            name = "Стоянка";
            code = 1;
            isBrake = false;
        }
        else if (power > 5 && tc < 0.1) {
            name = "Тяга";
            code = 2;
        }
        else if (power > -5 && power <= 5 && tc < 0.1) {
            name = "Выбег";
            code = 3;
            if (isBrake) {
                name = "Пневматическое торможение";
                code = 5;
            }
        }
        else if (power <= -5 && tc < 0.1) {
            name = "Рекуперация";
            code = 4;
            if (isBrake) {
                name = "Комбинированное торможение";
                code = 6;
            }
        }
        else if (power <= -5 && tc >= 0.1) {
            name = "Комбинированное торможение";
            code = 6;
            isBrake = false;
        }
        else if (tc >= 0.1) {
            name = "Пневматическое торможение";
            code = 5;
            isBrake = false;
        }
        return new ModeWork(code, name);
    }

    private String getAlsnStr(int code) {
        switch (code) {
            case ALSN_YELLOW: return "Ж";
            case ALSN_WHITE: return "Б";
            case ALSN_GREEN: return "З";
            case ALSN_REDYELLOW: return "КЖ";
            case ALSN_RED: return "К";
            default: return "";
        }
    }

    private String getModeAutoStr(int code) {
        switch (code) {
            case MANUAL: return "советчик";
            case AUTO_SCHEDULE: return "по расписанию";
            case AUTO_AVG_SPEED: return "по средней скорости";
            case CHAIN_OFF: return "отключены цепи";
            case BAN_TRACT: return "запрет тяги";
            case BAN_BRAKE_TRACT: return "запрет торм";
            case EMULATION: return "эмуляция";
            case BAN_AUTO: return "запрет автоведения";
            default: return "";
        }
    }

}
