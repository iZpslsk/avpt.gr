package avpt.gr.reports;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32_gp;
import avpt.gr.blocks32.asim.*;
import avpt.gr.blocks32.overall.Block32_21_1;
import avpt.gr.blocks32.overall.Block32_21_4;
import avpt.gr.chart_dataset.ChartArrays;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartPanelArm;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;
import avpt.gr.train.Train;

import static avpt.gr.blocks32.SubIdGr.getSubId;
import static avpt.gr.common.HelperExl_POI.getStyle;
import static avpt.gr.common.UtilsArmG.formatDateTime;
import static avpt.gr.common.UtilsArmG.formatTime;
import static avpt.gr.train.Train.*;

public class OutConsumptionEn {

    private final XSSFSheet sheet;
    private final Train train;
    private final ArrBlock32 arrBlock32;
    private Block32_gp block32_gp;
    private final CellStyle styleRight;
    private final CellStyle styleCenter;

    private LocalTime localTime = null;
    private int cur_coordinate = 0;
    private int cur_second = 0;

    private int act1 = 0;
    private int act2 = 0;
    private int act3 = 0;
    private int act4 = 0;
    private int react1 = 0;
    private int react2 = 0;
    private int react3 = 0;
    private int react4 = 0;

    private int act1_rec = 0;
    private int act2_rec = 0;
    private int act3_rec = 0;
    private int act4_rec = 0;
    private int react1_rec = 0;
    private int react2_rec = 0;
    private int react3_rec = 0;
    private int react4_rec = 0;

    private int act = 0;
    private int rec = 0;

    private int curTypeLoc;

    public OutConsumptionEn(XSSFSheet sheet, ChartPanelArm chartPanelArm, Train train) {
        this.sheet = sheet;
        this.train = train;
        arrBlock32 = chartPanelArm.getChartDataset().getArrBlock32();
        styleRight = getStyle(sheet, CellStyle.ALIGN_RIGHT, true);
        styleCenter = getStyle(sheet, CellStyle.ALIGN_CENTER, true);
    }

    public void outConsumptionEn() {
        int row = 0;

        ExcelReports.toCell(sheet, row, 0,
                "Отчет о расходе энергии " +
                        (train != null ? "поезда №" +
                                train.getNumTrain() + "  " + train.getDateTimeStart().format(formatDateTime) : ""), styleCenter);
        row += 4;

        int blStart = train != null ? train.getBlStart() : 0;
        int blEnd = train != null ? train.getBlEnd() : arrBlock32.size() - 1;

        LocalTime localTimePrev = null;
       // int curTypeLoc = 0;
        int coordinate_start = -1;
        int acdc_asim = -1;

        // предшествующяя посылка энергии
        avpt.gr.blocks32.s5k.Block32_57 block32_57_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_58 block32_58_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_59 block32_59_s5k_prev = null;
        avpt.gr.blocks32.s5k.Block32_5A block32_5A_s5k_prev = null;

        avpt.gr.blocks32.s5k_2.Block32_57 block32_57_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_58 block32_58_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_59 block32_59_s5k_2_prev = null;
        avpt.gr.blocks32.s5k_2.Block32_5A block32_5A_s5k_2_prev = null;

        avpt.gr.blocks32.s5.Block32_57 block32_57_s5_prev = null;
        avpt.gr.blocks32.s5.Block32_58 block32_58_s5_prev = null;
        avpt.gr.blocks32.s5.Block32_59 block32_59_s5_prev = null;
        avpt.gr.blocks32.s5.Block32_5A block32_5A_s5_prev = null;

        avpt.gr.blocks32.s4k.Block32_57 block32_57_s4k_prev = null;
        avpt.gr.blocks32.s4k.Block32_58 block32_58_s4k_prev = null;
        avpt.gr.blocks32.s4k.Block32_59 block32_59_s4k_prev = null;
        avpt.gr.blocks32.s4k.Block32_5A block32_5A_s4k_prev = null;

        avpt.gr.blocks32.vl10.Block32_13 block32_13_prev = null;
        avpt.gr.blocks32.vl80.Block32_59 block32_59_vl80_prev = null;

        avpt.gr.blocks32.asim.Block32_C3_0 block32_c3_0_prev = null;
        avpt.gr.blocks32.asim.Block32_C3_1 block32_c3_1_prev = null;
        avpt.gr.blocks32.asim.Block32_C3_2 block32_c3_2_prev = null;
        avpt.gr.blocks32.asim.Block32_C3_3 block32_c3_3_prev = null;

        for (int i = blStart; i <= blEnd; i++) {
            block32_gp = arrBlock32.get(i);

            if (block32_gp.getId() == 0xC0) {
                int curSubId = getSubId(block32_gp.getId(), block32_gp.getValues());
                if (curSubId == 0) { // init
                    Block32_C0_0 block32_c0_0 = new Block32_C0_0(block32_gp.getValues());
                    curTypeLoc = block32_c0_0.getTypeLoc();
                }
            }
            if (block32_gp.getId() == 0xC2) {
                int curSubId = getSubId(block32_gp.getId(), block32_gp.getValues());
                if (curSubId == 0) {
                    Block32_C2_0 block32_c2_0 = new Block32_C2_0(block32_gp.getValues());
                    localTime = block32_c2_0.getTime();
                    if (localTimePrev != null && localTime != null) {
                        cur_second += Duration.between(localTimePrev, localTime).getSeconds();
                        if (localTimePrev.getMinute() != localTime.getMinute()) addToSheet(row++);
                    }
                    if (coordinate_start == -1) coordinate_start = (int)block32_c2_0.getCoordinate();
                    if (coordinate_start > -1) cur_coordinate = (int)block32_c2_0.getCoordinate() - coordinate_start;
                    localTimePrev = localTime;

                }
            }

            if (block32_gp.getId() == 0x21) {
                int curSubId_21 = getSubId(block32_gp.getId(), block32_gp.getValues());
                switch (curSubId_21) {
                    case 0x01:
                        Block32_21_1 block32_21_1 = new Block32_21_1(block32_gp.getValues());
                        curTypeLoc = block32_21_1.getTypeLoc();
                        break;
                    case 0x04:
                        Block32_21_4 block32_21_4 = new Block32_21_4(block32_gp.getValues());
                        localTime = UtilsArmG.getTime(block32_21_4.getSecBeginDay());
                        if (localTimePrev != null && localTime != null) {
                            cur_second += Duration.between(localTimePrev, localTime).getSeconds();
                            if (localTimePrev.getMinute() != localTime.getMinute()) addToSheet(row++);
                        }
                        if (coordinate_start == -1) coordinate_start = block32_21_4.getCoordinate();
                        if (coordinate_start > -1) cur_coordinate = block32_21_4.getCoordinate() - coordinate_start;
                        localTimePrev = localTime;
                        break;
                }
            }
            int typeLoc = train != null ? train.getTypeLoc() : curTypeLoc;
            if (typeLoc == S5K) {
                switch (block32_gp.getId()){
                    case 0x57: // расход А
                        avpt.gr.blocks32.s5k.Block32_57 block32_57 = new avpt.gr.blocks32.s5k.Block32_57(arrBlock32.get(i).getValues());
                        if (block32_57_s5k_prev != null) {
                            act1 += ChartArrays.getDiffEnCnt(block32_57_s5k_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1());
                            act1_rec += ChartArrays.getDiffEnCnt(block32_57_s5k_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1());
                            react1 += ChartArrays.getDiffEnCnt(block32_57_s5k_prev.getEnergyReact_s1(), block32_57.getEnergyReact_s1());
                            react1_rec += ChartArrays.getDiffEnCnt(block32_57_s5k_prev.getEnergyReactRec_s1(), block32_57.getEnergyReactRec_s1());
                        }
                        block32_57_s5k_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s5k.Block32_58 block32_58 = new avpt.gr.blocks32.s5k.Block32_58(arrBlock32.get(i).getValues());
                        if (block32_58_s5k_prev != null) {
                            act2 += ChartArrays.getDiffEnCnt(block32_58_s5k_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2());
                            act2_rec += ChartArrays.getDiffEnCnt(block32_58_s5k_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2());
                            react2 += ChartArrays.getDiffEnCnt(block32_58_s5k_prev.getEnergyReact_s2(), block32_58.getEnergyReact_s2());
                            react2_rec += ChartArrays.getDiffEnCnt(block32_58_s5k_prev.getEnergyReactRec_s2(), block32_58.getEnergyReactRec_s2());
                        }
                        block32_58_s5k_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s5k.Block32_59 block32_59 = new avpt.gr.blocks32.s5k.Block32_59(arrBlock32.get(i).getValues());
                        if (block32_59_s5k_prev != null) {
                            act3 += ChartArrays.getDiffEnCnt(block32_59_s5k_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3());
                            act3_rec += ChartArrays.getDiffEnCnt(block32_59_s5k_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3());
                            react3 += ChartArrays.getDiffEnCnt(block32_59_s5k_prev.getEnergyReact_s3(), block32_59.getEnergyReact_s3());
                            react3_rec += ChartArrays.getDiffEnCnt(block32_59_s5k_prev.getEnergyReactRec_s3(), block32_59.getEnergyReactRec_s3());
                        }
                        block32_59_s5k_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s5k.Block32_5A block32_5A = new avpt.gr.blocks32.s5k.Block32_5A(arrBlock32.get(i).getValues());
                        if (block32_5A_s5k_prev != null) {
                            act4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4());
                            act4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4());
                            react4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev.getEnergyReact_s4(), block32_5A.getEnergyReact_s4());
                            react4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5k_prev.getEnergyReactRec_s4(), block32_5A.getEnergyReactRec_s4());
                        }
                        block32_5A_s5k_prev = block32_5A;
                        break;
                }
            }
            if (typeLoc == S5K_2) {
                switch (block32_gp.getId()){
                    case 0x57: // расход А
                        avpt.gr.blocks32.s5k_2.Block32_57 block32_57 = new avpt.gr.blocks32.s5k_2.Block32_57(arrBlock32.get(i).getValues());
                        if (block32_57_s5k_2_prev != null) {
                            act1 += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1());
                            act1_rec += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1());
                            react1 += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev.getEnergyReact_s1(), block32_57.getEnergyReact_s1());
                            react1_rec += ChartArrays.getDiffEnCnt(block32_57_s5k_2_prev.getEnergyReactRec_s1(), block32_57.getEnergyReactRec_s1());
                        }
                        block32_57_s5k_2_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s5k_2.Block32_58 block32_58 = new avpt.gr.blocks32.s5k_2.Block32_58(arrBlock32.get(i).getValues());
                        if (block32_58_s5k_2_prev != null) {
                            act2 += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2());
                            act2_rec += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2());
                            react2 += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev.getEnergyReact_s2(), block32_58.getEnergyReact_s2());
                            react2_rec += ChartArrays.getDiffEnCnt(block32_58_s5k_2_prev.getEnergyReactRec_s2(), block32_58.getEnergyReactRec_s2());
                        }
                        block32_58_s5k_2_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s5k_2.Block32_59 block32_59 = new avpt.gr.blocks32.s5k_2.Block32_59(arrBlock32.get(i).getValues());
                        if (block32_59_s5k_2_prev != null) {
                            act3 += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3());
                            act3_rec += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3());
                            react3 += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev.getEnergyReact_s3(), block32_59.getEnergyReact_s3());
                            react3_rec += ChartArrays.getDiffEnCnt(block32_59_s5k_2_prev.getEnergyReactRec_s3(), block32_59.getEnergyReactRec_s3());
                        }
                        block32_59_s5k_2_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s5k_2.Block32_5A block32_5A = new avpt.gr.blocks32.s5k_2.Block32_5A(arrBlock32.get(i).getValues());
                        if (block32_5A_s5k_2_prev != null) {
                            act4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4());
                            act4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4());
                            react4 += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev.getEnergyReact_s4(), block32_5A.getEnergyReact_s4());
                            react4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5k_2_prev.getEnergyReactRec_s4(), block32_5A.getEnergyReactRec_s4());
                        }
                        block32_5A_s5k_2_prev = block32_5A;
                        break;
                }
            }
            if (typeLoc == S5) {
                switch (block32_gp.getId()){
                    case 0x57: // расход А
                        avpt.gr.blocks32.s5.Block32_57 block32_57 = new avpt.gr.blocks32.s5.Block32_57(arrBlock32.get(i).getValues());
                        if (block32_57_s5_prev != null) {
                            act1 += ChartArrays.getDiffEnCnt(block32_57_s5_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1());
                            act1_rec += ChartArrays.getDiffEnCnt(block32_57_s5_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1());
                            react1 += ChartArrays.getDiffEnCnt(block32_57_s5_prev.getEnergyReact_s1(), block32_57.getEnergyReact_s1());
                            react1_rec += ChartArrays.getDiffEnCnt(block32_57_s5_prev.getEnergyReactRec_s1(), block32_57.getEnergyReactRec_s1());
                        }
                        block32_57_s5_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s5.Block32_58 block32_58 = new avpt.gr.blocks32.s5.Block32_58(arrBlock32.get(i).getValues());
                        if (block32_58_s5_prev != null) {
                            act2 += ChartArrays.getDiffEnCnt(block32_58_s5_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2());
                            act2_rec += ChartArrays.getDiffEnCnt(block32_58_s5_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2());
                            react2 += ChartArrays.getDiffEnCnt(block32_58_s5_prev.getEnergyReact_s2(), block32_58.getEnergyReact_s2());
                            react2_rec += ChartArrays.getDiffEnCnt(block32_58_s5_prev.getEnergyReactRec_s2(), block32_58.getEnergyReactRec_s2());
                        }
                        block32_58_s5_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s5.Block32_59 block32_59 = new avpt.gr.blocks32.s5.Block32_59(arrBlock32.get(i).getValues());
                        if (block32_59_s5_prev != null) {
                            act3 += ChartArrays.getDiffEnCnt(block32_59_s5_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3());
                            act3_rec += ChartArrays.getDiffEnCnt(block32_59_s5_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3());
                            react3 += ChartArrays.getDiffEnCnt(block32_59_s5_prev.getEnergyReact_s3(), block32_59.getEnergyReact_s3());
                            react3_rec += ChartArrays.getDiffEnCnt(block32_59_s5_prev.getEnergyReactRec_s3(), block32_59.getEnergyReactRec_s3());
                        }
                        block32_59_s5_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s5.Block32_5A block32_5A = new avpt.gr.blocks32.s5.Block32_5A(arrBlock32.get(i).getValues());
                        if (block32_5A_s5_prev != null) {
                            act4 += ChartArrays.getDiffEnCnt(block32_5A_s5_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4());
                            act4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4());
                            react4 += ChartArrays.getDiffEnCnt(block32_5A_s5_prev.getEnergyReact_s4(), block32_5A.getEnergyReact_s4());
                            react4_rec += ChartArrays.getDiffEnCnt(block32_5A_s5_prev.getEnergyReactRec_s4(), block32_5A.getEnergyReactRec_s4());
                        }
                        block32_5A_s5_prev = block32_5A;
                        break;
                }
            }

            if (typeLoc == S4K) {
                switch (block32_gp.getId()){
                    case 0x57: // расход А
                        avpt.gr.blocks32.s4k.Block32_57 block32_57 = new avpt.gr.blocks32.s4k.Block32_57(arrBlock32.get(i).getValues());
                        if (block32_57_s4k_prev != null) {
                            act1 += ChartArrays.getDiffEnCnt(block32_57_s4k_prev.getEnergyAct_s1(), block32_57.getEnergyAct_s1());
                            act1_rec += ChartArrays.getDiffEnCnt(block32_57_s4k_prev.getEnergyActRec_s1(), block32_57.getEnergyActRec_s1());
                        }
                        block32_57_s4k_prev = block32_57;
                        break;
                    case 0x58: // расход Б
                        avpt.gr.blocks32.s4k.Block32_58 block32_58 = new avpt.gr.blocks32.s4k.Block32_58(arrBlock32.get(i).getValues());
                        if (block32_58_s4k_prev != null) {
                            act2 += ChartArrays.getDiffEnCnt(block32_58_s4k_prev.getEnergyAct_s2(), block32_58.getEnergyAct_s2());
                            act2_rec += ChartArrays.getDiffEnCnt(block32_58_s4k_prev.getEnergyActRec_s2(), block32_58.getEnergyActRec_s2());
                        }
                        block32_58_s4k_prev = block32_58;
                        break;
                    case 0x59: // расход В
                        avpt.gr.blocks32.s4k.Block32_59 block32_59 = new avpt.gr.blocks32.s4k.Block32_59(arrBlock32.get(i).getValues());
                        if (block32_59_s4k_prev != null) {
                            act3 += ChartArrays.getDiffEnCnt(block32_59_s4k_prev.getEnergyAct_s3(), block32_59.getEnergyAct_s3());
                            act3_rec += ChartArrays.getDiffEnCnt(block32_59_s4k_prev.getEnergyActRec_s3(), block32_59.getEnergyActRec_s3());
                        }
                        block32_59_s4k_prev = block32_59;
                        break;
                    case 0x5A: // расход Г
                        avpt.gr.blocks32.s4k.Block32_5A block32_5A = new avpt.gr.blocks32.s4k.Block32_5A(arrBlock32.get(i).getValues());
                        if (block32_5A_s4k_prev != null) {
                            act4 += ChartArrays.getDiffEnCnt(block32_5A_s4k_prev.getEnergyAct_s4(), block32_5A.getEnergyAct_s4());
                            act4_rec += ChartArrays.getDiffEnCnt(block32_5A_s4k_prev.getEnergyActRec_s4(), block32_5A.getEnergyActRec_s4());
                        }
                        block32_5A_s4k_prev = block32_5A;
                        break;
                }
            }

            if (typeLoc == VL10 && block32_gp.getId() == 0x13) {
                avpt.gr.blocks32.vl10.Block32_13 block32_13 = new avpt.gr.blocks32.vl10.Block32_13(arrBlock32.get(i).getValues());
                if (block32_13_prev != null) {
                    act += ChartArrays.getDiffEnCnt(block32_13_prev.getEnergy(), block32_13.getEnergy());
                    rec += ChartArrays.getDiffEnCnt(block32_13_prev.getEnergyRec(), block32_13.getEnergyRec());
                }
                block32_13_prev = block32_13;
            }
            if (typeLoc == VL80 && block32_gp.getId() == 0x59) {
                avpt.gr.blocks32.vl80.Block32_59 block32_59 = new avpt.gr.blocks32.vl80.Block32_59(arrBlock32.get(i).getValues());
                if (block32_59_vl80_prev != null) {
                    act1 += ChartArrays.getDiffEnCnt(block32_59_vl80_prev.getEnergyAct_s1(), block32_59.getEnergyAct_s1());
                    act2 += ChartArrays.getDiffEnCnt(block32_59_vl80_prev.getEnergyAct_s2(), block32_59.getEnergyAct_s2());
                    act1_rec += ChartArrays.getDiffEnCnt(block32_59_vl80_prev.getEnergyActRec_s1(), block32_59.getEnergyActRec_s1());
                    act2_rec += ChartArrays.getDiffEnCnt(block32_59_vl80_prev.getEnergyActRec_s2(), block32_59.getEnergyActRec_s2());
                }
                block32_59_vl80_prev = block32_59;
            }


            if (block32_gp.getId() == 0xC0) {
                Block32_C0_0 block32_c0_0 = new Block32_C0_0(arrBlock32.get(i).getValues());
                acdc_asim = block32_c0_0.getTypeACDC();
            }
            if (block32_gp.getId() == 0xC3) {
                int curSubId = getSubId(block32_gp.getId(), block32_gp.getValues());
                switch (curSubId){
                    case 1:
                        Block32_C3_0 block32_c3_0 = new Block32_C3_0(arrBlock32.get(i).getValues());
                        if (block32_c3_0_prev != null) {
                            if (acdc_asim == 1) {   // постоянка
                                act1 += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyAct_s1_dc(), block32_c3_0.getEnergyAct_s1_dc());
                                react1 += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyReact_s1_dc(), block32_c3_0.getEnergyReact_s1_dc());
                            }
                            else if (acdc_asim == 2) {
                                act1 += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyAct_s1_ac(), block32_c3_0.getEnergyAct_s1_ac());
                                act1_rec += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyActRec_ac_s1(), block32_c3_0.getEnergyActRec_ac_s1());
                                react1 += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyReAct_s1_ac(), block32_c3_0.getEnergyReAct_s1_ac());
                                react1_rec += ChartArrays.getDiffEnCnt(block32_c3_0_prev.getEnergyReactRec_ac_s1(), block32_c3_0.getEnergyReactRec_ac_s1());
                            }
                        }
                        block32_c3_0_prev = block32_c3_0;
                        break;
                    case 2:
                        Block32_C3_1 block32_c3_1 = new Block32_C3_1(arrBlock32.get(i).getValues());
                        if (block32_c3_1_prev != null) {
                            if (acdc_asim == 1) {   // постоянка
                                act2 += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyAct_s2_dc(), block32_c3_1.getEnergyAct_s2_dc());
                                react2 += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyReact_s2_dc(), block32_c3_1.getEnergyReact_s2_dc());
                            }
                            else if (acdc_asim == 2) {
                                act2 += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyAct_s2_ac(), block32_c3_1.getEnergyAct_s2_ac());
                                act2_rec += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyActRec_ac_s2(), block32_c3_1.getEnergyActRec_ac_s2());
                                react2 += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyReAct_s2_ac(), block32_c3_1.getEnergyReAct_s2_ac());
                                react2_rec += ChartArrays.getDiffEnCnt(block32_c3_1_prev.getEnergyReactRec_ac_s2(), block32_c3_1.getEnergyReactRec_ac_s2());
                            }
                        }
                        block32_c3_1_prev = block32_c3_1;
                        break;
                    case 3:
                        Block32_C3_2 block32_c3_2 = new Block32_C3_2(arrBlock32.get(i).getValues());
                        if (block32_c3_2_prev != null) {
                            if (acdc_asim == 1) {   // постоянка
                                act3 += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyAct_s3_dc(), block32_c3_2.getEnergyAct_s3_dc());
                                react3 += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyReact_s3_dc(), block32_c3_2.getEnergyReact_s3_dc());
                            }
                            else if (acdc_asim == 2) {
                                act3 += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyAct_s3_ac(), block32_c3_2.getEnergyAct_s3_ac());
                                act3_rec += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyActRec_ac_s3(), block32_c3_2.getEnergyActRec_ac_s3());
                                react3 += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyReAct_s3_ac(), block32_c3_2.getEnergyReAct_s3_ac());
                                react3_rec += ChartArrays.getDiffEnCnt(block32_c3_2_prev.getEnergyReactRec_ac_s3(), block32_c3_2.getEnergyReactRec_ac_s3());
                            }
                        }
                        block32_c3_2_prev = block32_c3_2;
                        break;
                    case 4:
                        Block32_C3_3 block32_c3_3 = new Block32_C3_3(arrBlock32.get(i).getValues());
                        if (block32_c3_3_prev != null) {
                            if (acdc_asim == 1) {   // постоянка
                                act4 += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyAct_s4_dc(), block32_c3_3.getEnergyAct_s4_dc());
                                react4 += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyReact_s4_dc(), block32_c3_3.getEnergyReact_s4_dc());
                            }
                            else if (acdc_asim == 2) {
                                act4 += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyAct_s4_ac(), block32_c3_3.getEnergyAct_s4_ac());
                                act4_rec += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyActRec_ac_s4(), block32_c3_3.getEnergyActRec_ac_s4());
                                react4 += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyReAct_s4_ac(), block32_c3_3.getEnergyReAct_s4_ac());
                                react3_rec += ChartArrays.getDiffEnCnt(block32_c3_3_prev.getEnergyReactRec_ac_s4(), block32_c3_3.getEnergyReactRec_ac_s4());
                            }
                        }
                        block32_c3_3_prev = block32_c3_3;
                        break;

                }

            }

        }
        addToSheet(row);
    }

    private void addToSheet(int row) {
//        if (cur_coordinate >= 0) ExcelReports.toCell(sheet, row, 0, cur_coordinate,  styleRight);
//        else ExcelReports.toCell(sheet, row, 0, "",  styleRight);
        int col = 0;
        if (cur_second >= 0) ExcelReports.toCell(sheet, row, col++, cur_second,  styleRight);
        else ExcelReports.toCell(sheet, row, col++, "",  styleRight);
        ExcelReports.toCell(sheet, row, col++, localTime.format(formatTime),  styleRight);

        if (curTypeLoc == VL10) {
            ExcelReports.toCell(sheet, row, col++, act, styleRight);
            ExcelReports.toCell(sheet, row, col++, rec, styleRight);
        }
        else {
            ExcelReports.toCell(sheet, row, col++, act1, styleRight);
            ExcelReports.toCell(sheet, row, col++, act1_rec, styleRight);
            ExcelReports.toCell(sheet, row, col++, react1, styleRight);
            ExcelReports.toCell(sheet, row, col++, react1_rec, styleRight);

            ExcelReports.toCell(sheet, row, col++, act2, styleRight);
            ExcelReports.toCell(sheet, row, col++, act2_rec, styleRight);
            ExcelReports.toCell(sheet, row, col++, react2, styleRight);
            ExcelReports.toCell(sheet, row, col++, react2_rec, styleRight);

            ExcelReports.toCell(sheet, row, col++, act3, styleRight);
            ExcelReports.toCell(sheet, row, col++, act3_rec, styleRight);
            ExcelReports.toCell(sheet, row, col++, react3, styleRight);
            ExcelReports.toCell(sheet, row, col++, react3_rec, styleRight);

            ExcelReports.toCell(sheet, row, col++, act4, styleRight);
            ExcelReports.toCell(sheet, row, col++, act4_rec, styleRight);
            ExcelReports.toCell(sheet, row, col++, react4, styleRight);
            ExcelReports.toCell(sheet, row, col++, react4_rec, styleRight);
        }
        ExcelReports.toCell(sheet, row, col++, (double) block32_gp.getKm(),  styleRight);
        ExcelReports.toCell(sheet, row, col, (double) block32_gp.getPk(),  styleRight);
    }
}
