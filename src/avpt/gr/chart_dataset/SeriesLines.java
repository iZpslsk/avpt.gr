package avpt.gr.chart_dataset;

import avpt.gr.chart_dataset.keysEnum.LineKeys;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static avpt.gr.maps.Limits.COLOR_LIM_MAP;

/**
 * создание линий XYSeries и их группировка в XYSeriesCollection
 */
public class SeriesLines {

    private static final Color COLOR_PROFILE = new Color(0xFFD69750, true);
    private final ListLines listLines;
    private final int precision;  // точность
    private static Map<String, Boolean> mapVisible = new HashMap<String, Boolean>();
    public static final Map<String, Boolean> mapDefVisible = new HashMap<String, Boolean>();
    // блоки линий
    private final XYSeriesCollection serTrainCollect = new XYSeriesCollection();    // поезд
    private final XYSeriesCollection serVoltageCollect_cs = new XYSeriesCollection();  // напряжение контактной сети
    private final XYSeriesCollection serVoltageCollect = new XYSeriesCollection();  // напряжение
    private final XYSeriesCollection serAmperageCollect_common = new XYSeriesCollection(); // ток общий
    private final XYSeriesCollection serAmperageCollect_anchor = new XYSeriesCollection(); // ток якоря
    private final XYSeriesCollection serAmperageCollect_excitation = new XYSeriesCollection(); // ток возбуждения
    private final XYSeriesCollection serAmperageCollect_engine = new XYSeriesCollection(); // ток двигателя
    private final XYSeriesCollection serAmperageCollect = new XYSeriesCollection(); // ток отопления
    private final XYSeriesCollection serConsumptionEnCollect = new XYSeriesCollection();   // энергия
    private final XYSeriesCollection serPowerCollect = new XYSeriesCollection();    // сила
    private final XYSeriesCollection serDistanceTailCollect = new XYSeriesCollection();    // расстояние до хвоста
    private final XYSeriesCollection serPressCollect = new XYSeriesCollection();          // давление
    private final XYSeriesCollection serSpeedCollect = new XYSeriesCollection();          // скорость
    private final XYSeriesCollection serProfileCollect = new XYSeriesCollection();  // профиль
    private final XYSeriesCollection serPositionCollect = new XYSeriesCollection(); // позиция контроллера
    private final XYSeriesCollection serMapCollect = new XYSeriesCollection();      // карта

    /**
     * конструктор
     * @param listLines - массивы всех линий
     * @param secondStart - начальная секунда
     * @param secondEnd - конечная секунда
     * @param precision - точность  1:N
     */
    public SeriesLines(ListLines listLines, int secondStart, int secondEnd, int precision) {
        this.listLines = listLines;
        this.precision = precision;
        makeSeries(secondStart, secondEnd);
    }

    /**
     * @param key - ключь линии
     * @param arrDots - массив точек
     * @param sStart - начальная секунда
     * @param sEnd - конечная секунда
     * @return - XYSeries линия
     */
    private XYSeries makeSerDots(LineKeys key, ArrayList<ListLines.ItemLine> arrDots, int sStart, int sEnd) {
        XYSeries serDots = new XYSeries(key);
        serDots.setDescription(key.getDescription());
        ListLines.ItemLine item = null;
        int prevSecond = 0;
        double prevValue = Double.NaN;
        final int DRTN = 30; // продолжительность
        int dd;
        for (ListLines.ItemLine arrDot : arrDots) {
            if (arrDot.getSecond() >= sStart && arrDot.getSecond() <= sEnd) {
                item = arrDot;
                dd = Math.abs(item.getSecond() - prevSecond);
                if (Double.isNaN(item.getValue()) && Double.isNaN(prevValue)) {
                    continue;
                }

                if (item.getSecond() % precision == 0) {
                    if (dd > DRTN)
                        serDots.add(item.getSecond(), prevValue);
                    serDots.add(item.getSecond(), item.getValue());
                } else {
                    if (dd > DRTN) {
                        serDots.add(prevSecond, prevValue);
                        serDots.add(item.getSecond(), prevValue);
                    }
                }
                prevSecond = item.getSecond();
                prevValue = item.getValue();
            }
        }
        if (item != null)
            serDots.add(item.getSecond(), item.getValue());
        return serDots;
    }

    /**
     * создаем линии для отображения XSeries на основе массивов линий
     * @param secondStart - секунда начала
     * @param secondEnd - секунда конца
     */
    private void makeSeries(int secondStart, int secondEnd) {
        // максимальное количество точек для включения линии в коллекцию
        int MAX_ITEM_CNT = 5;

        // поезд
        XYSeries serTrain = makeSerDots(LineKeys.TRAIN, listLines.getListTrains(),
                secondStart, secondEnd);
        serTrainCollect.addSeries(serTrain);

        // напряжение контактной сети
        if (listLines.getListVoltage().size() > MAX_ITEM_CNT) {
            serVoltageCollect_cs.addSeries(makeSerDots(LineKeys.VOLT, listLines.getListVoltage(), secondStart, secondEnd));
        }
        if (listLines.getListVoltage_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect_cs.addSeries(makeSerDots(LineKeys.VOLT_S1, listLines.getListVoltage_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltage_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect_cs.addSeries(makeSerDots(LineKeys.VOLT_S2, listLines.getListVoltage_s2(), secondStart, secondEnd));
        }
        if (listLines.getListVoltage_s3().size() > MAX_ITEM_CNT) {
            serVoltageCollect_cs.addSeries(makeSerDots(LineKeys.VOLT_S3, listLines.getListVoltage_s3(), secondStart, secondEnd));
        }
        if (listLines.getListVoltage_s4().size() > MAX_ITEM_CNT) {
            serVoltageCollect_cs.addSeries(makeSerDots(LineKeys.VOLT_S4, listLines.getListVoltage_s4(), secondStart, secondEnd));
        }
        // напряжение двигателя
        if (listLines.getListVoltageEngine_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG_S1, listLines.getListVoltageEngine_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG_S2, listLines.getListVoltageEngine_s2(), secondStart, secondEnd));
        }
        // напряжение двигателя 1
        if (listLines.getListVoltageEngine1_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG1_S1, listLines.getListVoltageEngine1_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine1_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG1_S2, listLines.getListVoltageEngine1_s2(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine1_s3().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG1_S3, listLines.getListVoltageEngine1_s3(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine1_s4().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG1_S4, listLines.getListVoltageEngine1_s4(), secondStart, secondEnd));
        }
        // напряжение двигателя 2
        if (listLines.getListVoltageEngine2_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG2_S1, listLines.getListVoltageEngine2_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine2_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG2_S2, listLines.getListVoltageEngine2_s2(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine2_s3().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG2_S3, listLines.getListVoltageEngine2_s3(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine2_s4().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG2_S4, listLines.getListVoltageEngine2_s4(), secondStart, secondEnd));
        }
        // напряжение двигателя 3
        if (listLines.getListVoltageEngine3_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG3_S1, listLines.getListVoltageEngine3_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine3_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG3_S2, listLines.getListVoltageEngine3_s2(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine3_s3().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG3_S3, listLines.getListVoltageEngine3_s3(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine3_s4().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG3_S4, listLines.getListVoltageEngine3_s4(), secondStart, secondEnd));
        }
        // напряжение двигателя 4
        if (listLines.getListVoltageEngine4_s1().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG4_S1, listLines.getListVoltageEngine4_s1(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine4_s2().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG4_S2, listLines.getListVoltageEngine4_s2(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine4_s3().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG4_S3, listLines.getListVoltageEngine4_s3(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageEngine4_s4().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_ENG4_S4, listLines.getListVoltageEngine4_s4(), secondStart, secondEnd));
        }
        // напряжение бхв акб
        if (listLines.getListVoltageBhvAkbGet().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_AKB_BHV_GET, listLines.getListVoltageBhvAkbGet(), secondStart, secondEnd));
        }
        if (listLines.getListVoltageBhvAkbSend().size() > MAX_ITEM_CNT) {
            serVoltageCollect.addSeries(makeSerDots(LineKeys.VOLT_AKB_BHV_SEND, listLines.getListVoltageBhvAkbSend(), secondStart, secondEnd));
        }
        // общий ток
        if (listLines.getListAmperage().size() > MAX_ITEM_CNT) {
            serAmperageCollect_common.addSeries(makeSerDots(LineKeys.AMP, listLines.getListAmperage(), secondStart, secondEnd));
        }
        if (listLines.getListAmperage_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_common.addSeries(makeSerDots(LineKeys.AMP_S1, listLines.getListAmperage_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperage_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_common.addSeries(makeSerDots(LineKeys.AMP_S2, listLines.getListAmperage_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperage_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_common.addSeries(makeSerDots(LineKeys.AMP_S3, listLines.getListAmperage_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperage_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_common.addSeries(makeSerDots(LineKeys.AMP_S4, listLines.getListAmperage_s4(), secondStart, secondEnd));
        }
        // ток якоря
        if (listLines.getListAmperageAnchor().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC, listLines.getListAmperageAnchor(), secondStart, secondEnd));
        }
        // ток якоря 1
        if (listLines.getListAmperageAnchor1_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC1_S1, listLines.getListAmperageAnchor1_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor1_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC1_S2, listLines.getListAmperageAnchor1_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor1_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC1_S3, listLines.getListAmperageAnchor1_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor1_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC1_S4, listLines.getListAmperageAnchor1_s4(), secondStart, secondEnd));
        }
        // ток якоря 2
        if (listLines.getListAmperageAnchor2_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC2_S1, listLines.getListAmperageAnchor2_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor2_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC2_S2, listLines.getListAmperageAnchor2_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor2_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC2_S3, listLines.getListAmperageAnchor2_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor2_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC2_S4, listLines.getListAmperageAnchor2_s4(), secondStart, secondEnd));
        }
        // ток якоря 3
        if (listLines.getListAmperageAnchor3_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC3_S1, listLines.getListAmperageAnchor3_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor3_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC3_S2, listLines.getListAmperageAnchor3_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor3_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC3_S3, listLines.getListAmperageAnchor3_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor3_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC3_S4, listLines.getListAmperageAnchor3_s4(), secondStart, secondEnd));
        }
        // ток якоря 4
        if (listLines.getListAmperageAnchor4_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC4_S1, listLines.getListAmperageAnchor4_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor4_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC4_S2, listLines.getListAmperageAnchor4_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor4_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC4_S3, listLines.getListAmperageAnchor4_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageAnchor4_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_anchor.addSeries(makeSerDots(LineKeys.AMP_ANC4_S4, listLines.getListAmperageAnchor4_s4(), secondStart, secondEnd));
        }
        // ток возбуждения
        if (listLines.getListAmperageExcitation().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC, listLines.getListAmperageExcitation(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC_S1, listLines.getListAmperageExcitation_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC_S2, listLines.getListAmperageExcitation_s2(), secondStart, secondEnd));
        }
        // ток возбуждения 1
        if (listLines.getListAmperageExcitation1_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC1_S1, listLines.getListAmperageExcitation1_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation1_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC1_S2, listLines.getListAmperageExcitation1_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation1_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC1_S3, listLines.getListAmperageExcitation1_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation1_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC1_S4, listLines.getListAmperageExcitation1_s4(), secondStart, secondEnd));
        }
        // ток возбуждения 2
        if (listLines.getListAmperageExcitation2_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC2_S1, listLines.getListAmperageExcitation2_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation2_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC2_S2, listLines.getListAmperageExcitation2_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation2_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC2_S3, listLines.getListAmperageExcitation2_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation2_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC2_S4, listLines.getListAmperageExcitation2_s4(), secondStart, secondEnd));
        }
        // ток возбуждения 3
        if (listLines.getListAmperageExcitation3_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC3_S1, listLines.getListAmperageExcitation3_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation3_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC3_S2, listLines.getListAmperageExcitation3_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation3_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC3_S3, listLines.getListAmperageExcitation3_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation3_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC3_S4, listLines.getListAmperageExcitation3_s4(), secondStart, secondEnd));
        }
        // ток возбуждения 4
        if (listLines.getListAmperageExcitation4_s1().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC4_S1, listLines.getListAmperageExcitation4_s1(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation4_s2().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC4_S2, listLines.getListAmperageExcitation4_s2(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation4_s3().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC4_S3, listLines.getListAmperageExcitation4_s3(), secondStart, secondEnd));
        }
        if (listLines.getListAmperageExcitation4_s4().size() > MAX_ITEM_CNT) {
            serAmperageCollect_excitation.addSeries(makeSerDots(LineKeys.AMP_EXC4_S4, listLines.getListAmperageExcitation4_s4(), secondStart, secondEnd));
        }
        // энергия
        if (listLines.getListConsumptionEn().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_EN, listLines.getListConsumptionEn(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_s1().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_EN_S1, listLines.getListConsumptionEn_s1(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_s2().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_EN_S2, listLines.getListConsumptionEn_s2(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_s3().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_EN_S3, listLines.getListConsumptionEn_s3(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_s4().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_EN_S4, listLines.getListConsumptionEn_s4(), secondStart, secondEnd));
        }
        // рекуперация
        if (listLines.getListConsumptionEn_r().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_R_EN, listLines.getListConsumptionEn_r(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_r_s1().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_R_EN_S1, listLines.getListConsumptionEn_r_s1(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_r_s2().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_R_EN_S2, listLines.getListConsumptionEn_r_s2(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_r_s3().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_R_EN_S3, listLines.getListConsumptionEn_r_s3(), secondStart, secondEnd));
        }
        if (listLines.getListConsumptionEn_r_s4().size() > MAX_ITEM_CNT) {
            serConsumptionEnCollect.addSeries(makeSerDots(LineKeys.CONSUMPTION_R_EN_S4, listLines.getListConsumptionEn_r_s4(), secondStart, secondEnd));
        }
        // сила
        if (listLines.getS5k_listPowerImplementation().size() > MAX_ITEM_CNT) {
            serPowerCollect.addSeries(makeSerDots(LineKeys.POWER_LOC, listLines.getS5k_listPowerImplementation(), secondStart, secondEnd));
        }
        if (listLines.getListPowerMax().size() > MAX_ITEM_CNT) {
            serPowerCollect.addSeries(makeSerDots(LineKeys.POWER_MAX, listLines.getListPowerMax(), secondStart, secondEnd));
        }
        if (listLines.getListPowerMaxRec().size() > MAX_ITEM_CNT) {
            serPowerCollect.addSeries(makeSerDots(LineKeys.POWER_MAX_REC, listLines.getListPowerMaxRec(), secondStart, secondEnd));
        }
        if (listLines.getListPowerTaskControl().size() > MAX_ITEM_CNT) {
            serPowerCollect.addSeries(makeSerDots(LineKeys.POWER_TASK_CONTROL, listLines.getListPowerTaskControl(), secondStart, secondEnd));
        }
        if (listLines.getListPowerTaskAuto().size() > MAX_ITEM_CNT) {
            serPowerCollect.addSeries(makeSerDots(LineKeys.POWER_TASK_AUTO, listLines.getListPowerTaskAuto(), secondStart, secondEnd));
        }
        // хвост
        if (listLines.getListDistanceTail().size() > MAX_ITEM_CNT) {
            serDistanceTailCollect.addSeries(makeSerDots(LineKeys.DISTANCE_TAIL, listLines.getListDistanceTail(), secondStart, secondEnd));
        }
        // давление
        if (listLines.getListPressUR_USAVP().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_UR_USAVP, listLines.getListPressUR_USAVP(), secondStart, secondEnd));
        }
        if (listLines.getListPressTM_USAVP().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TM_USAVP, listLines.getListPressTM_USAVP(), secondStart, secondEnd));
        }
        if (listLines.getListPressTC_USAVP().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TC_USAVP, listLines.getListPressTC_USAVP(), secondStart, secondEnd));
        }
        if (listLines.getListPressZTS_USAVP().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_ZTS_USAVP, listLines.getListPressZTS_USAVP(), secondStart, secondEnd));
        }
        if (listLines.getListPressPM().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_PM, listLines.getListPressPM(), secondStart, secondEnd));
        }
        if (listLines.getListPressUR().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_UR, listLines.getListPressUR(), secondStart, secondEnd));
        }
        if (listLines.getListPressTM().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TM, listLines.getListPressTM(), secondStart, secondEnd));
        }
        if (listLines.getListPressTC().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TC, listLines.getListPressTC(), secondStart, secondEnd));
        }
        if (listLines.getListPressNM().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_NM, listLines.getListPressNM(), secondStart, secondEnd));
        }
        // давление 1-я секция
        if (listLines.getListPressUR_s1().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_UR_S1, listLines.getListPressUR_s1(), secondStart, secondEnd));
        }
        if (listLines.getListPressTM_s1().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TM_S1, listLines.getListPressTM_s1(), secondStart, secondEnd));
        }
        if (listLines.getListPressTC_s1().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TC_S1, listLines.getListPressTC_s1(), secondStart, secondEnd));
        }
        // давление 2-я секция
        if (listLines.getListPressUR_s2().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_UR_S2, listLines.getListPressUR_s2(), secondStart, secondEnd));
        }
        if (listLines.getListPressTM_s2().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TM_S2, listLines.getListPressTM_s2(), secondStart, secondEnd));
        }
        if (listLines.getListPressTC_s2().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TC_S2, listLines.getListPressTC_s2(), secondStart, secondEnd));
        }
        //
        if (listLines.getListPressTmTail().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_TM_TAIL, listLines.getListPressTmTail(), secondStart, secondEnd));
        }
        if (listLines.getListPressWorkCameraBhv().size() > MAX_ITEM_CNT) {
            serPressCollect.addSeries(makeSerDots(LineKeys.PRESS_CAMERA_BHV, listLines.getListPressWorkCameraBhv(), secondStart, secondEnd));
        }
        // скорость
        if (listLines.getListSpeedLimitConst().size() > 3)
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_MAX, listLines.getListSpeedLimitConst(), secondStart, secondEnd));
        serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_LIM_TMP, listLines.getListSpeedLimitTemp(), secondStart, secondEnd));
        if (listLines.getListCurSpeedLimit().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_LIM_CUR, listLines.getListCurSpeedLimit(), secondStart, secondEnd));
        }
        if (listLines.getListPermissibleSpeed_CLUB().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_CLUB_LIM, listLines.getListPermissibleSpeed_CLUB(), secondStart, secondEnd));
        }
        if (listLines.getS5k_listSpeed_MSUD().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_MSUD, listLines.getS5k_listSpeed_MSUD(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_CLUB().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_CLUB, listLines.getListSpeed_CLUB(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_BS_DPS().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_BS_DPS, listLines.getListSpeed_BS_DPS(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_USAVP().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_USAVP, listLines.getListSpeed_USAVP(), secondStart, secondEnd));
        }
        if (listLines.getListSpeedCalc().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_CALC, listLines.getListSpeedCalc(), secondStart, secondEnd));
        }
        if (listLines.getListSpeedFact().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_FACT, listLines.getListSpeedFact(), secondStart, secondEnd));
        }
        if (listLines.getListSpeedGPS().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_GPS, listLines.getListSpeedGPS(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_BUT().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_BUT, listLines.getListSpeed_BUT(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_BLOCK().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_BLOCK, listLines.getListSpeed_BLOCK(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_Task().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_TASK, listLines.getListSpeed_Task(), secondStart, secondEnd));
        }
        if (listLines.getListSpeedSlave().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_SLAVE, listLines.getListSpeedSlave(), secondStart, secondEnd));
        }
        if (listLines.getListSpeed_ERG().size() > MAX_ITEM_CNT) {
            serSpeedCollect.addSeries(makeSerDots(LineKeys.SPEED_ERG, listLines.getListSpeed_ERG(), secondStart, secondEnd));
        }
        // позиция
        if (listLines.getListPositionS5k().size() > MAX_ITEM_CNT) {
            serPositionCollect.addSeries(makeSerDots(LineKeys.POSITION_S5k, listLines.getListPositionS5k(), secondStart, secondEnd));
        }
        if (listLines.getListPosition().size() > MAX_ITEM_CNT) {
            serPositionCollect.addSeries(makeSerDots(LineKeys.POSITION, listLines.getListPosition(), secondStart, secondEnd));
        }
        if (listLines.getListWeakField().size() > MAX_ITEM_CNT) {
            serPositionCollect.addSeries(makeSerDots(LineKeys.WEAK_FIELD, listLines.getListWeakField(), secondStart, secondEnd));
        }
        if (listLines.getListPositionTaskAuto().size() > MAX_ITEM_CNT) {
            serPositionCollect.addSeries(makeSerDots(LineKeys.POSITION_TASK_AUTO, listLines.getListPositionTaskAuto(), secondStart, secondEnd));
        }
        // профиль
        serProfileCollect.addSeries(makeSerDots(LineKeys.PROFILE_DIRECT, listLines.getListProfileDirect(), secondStart, secondEnd));
        serProfileCollect.addSeries(makeSerDots(LineKeys.PROFILE, listLines.getListProfile(), secondStart, secondEnd));
        // карта
        serMapCollect.addSeries(makeSerDots(LineKeys.MAP_DIRECT, listLines.getListMapDirect(), secondStart, secondEnd));
        serMapCollect.addSeries(makeSerDots(LineKeys.MAP_LINE, listLines.getListMapLine(), secondStart, secondEnd));
    }

    private static void setRenderer(LineKeys key, LineKeys cur_key, int index, XYItemRenderer renderer, Color color, boolean is_visible) {
        if (key == cur_key) {
            setStrokeDef(index, renderer);
            renderer.setSeriesPaint(index, color);
            setSeriesVisible(renderer, index, key, is_visible);
        }
    }

    /**
     * установка цветов для линий, и видимости при старте
     * @param index - индекс линии в plot
     * @param key - ключ
     * @param renderer -
     */
    public static void setColorVisibleAndStrokeLines(int index, LineKeys key, XYItemRenderer renderer) {
        // поезд
        if (key == LineKeys.TRAIN) {
            renderer.setSeriesStroke(index, new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            renderer.setSeriesPaint(index, new Color(0xFFFFF94C, false));
        }
        // напряжение контактной сети
        setRenderer(key, LineKeys.VOLT, index, renderer, new Color(0xFFD6756D, true), true);
        setRenderer(key, LineKeys.VOLT_S1, index, renderer, new Color(0xFFD63720, true), true);
        setRenderer(key, LineKeys.VOLT_S2, index, renderer, new Color(0xFF4E4BD6, true), false);
        setRenderer(key, LineKeys.VOLT_S3, index, renderer, new Color(0xFFD63E8D, true), false);
        setRenderer(key, LineKeys.VOLT_S4, index, renderer, new Color(0xFF74D668, true), false);
        // напряжение двигателя
        setRenderer(key, LineKeys.VOLT_ENG_S1, index, renderer, new Color(0xFF625DD6, true), true);
        setRenderer(key, LineKeys.VOLT_ENG_S2, index, renderer, new Color(0xFF789ED6, true), false);
        // напрядения двигателя 1
        setRenderer(key, LineKeys.VOLT_ENG1_S1, index, renderer, new Color(0xFF625DD6, true), true);
        setRenderer(key, LineKeys.VOLT_ENG1_S2, index, renderer, new Color(0xFF789ED6, true), false);
        setRenderer(key, LineKeys.VOLT_ENG1_S3, index, renderer, new Color(0xFF65D6C1, true), false);
        setRenderer(key, LineKeys.VOLT_ENG1_S4, index, renderer, new Color(0xFF8BD67A, true), false);
        // напрядения двигателя 2
        setRenderer(key, LineKeys.VOLT_ENG2_S1, index, renderer, new Color(0xFFC488D6, true), false);
        setRenderer(key, LineKeys.VOLT_ENG2_S2, index, renderer, new Color(0xFFD6A4AF, true), false);
        setRenderer(key, LineKeys.VOLT_ENG2_S3, index, renderer, new Color(0xFFD6615E, true), false);
        setRenderer(key, LineKeys.VOLT_ENG2_S4, index, renderer, new Color(0xFFD6B26F, true), false);
        // напрядения двигателя 3
        setRenderer(key, LineKeys.VOLT_ENG3_S1, index, renderer, new Color(0xFFB43FD6, true), false);
        setRenderer(key, LineKeys.VOLT_ENG3_S2, index, renderer, new Color(0xFFD656A1, true), false);
        setRenderer(key, LineKeys.VOLT_ENG3_S3, index, renderer, new Color(0xFFD61C16, true), false);
        setRenderer(key, LineKeys.VOLT_ENG3_S4, index, renderer, new Color(0xFFD6A32E, true), false);
        // напрядения двигателя 4
        setRenderer(key, LineKeys.VOLT_ENG4_S1, index, renderer, new Color(0xFFA303D6, true), false);
        setRenderer(key, LineKeys.VOLT_ENG4_S2, index, renderer, new Color(0xFFD60383, true), false);
        setRenderer(key, LineKeys.VOLT_ENG4_S3, index, renderer, new Color(0xFFB3D607, true), false);
        setRenderer(key, LineKeys.VOLT_ENG4_S4, index, renderer, new Color(0xFFB3D694, true), false);
        // напряжение бхв
        setRenderer(key, LineKeys.VOLT_AKB_BHV_GET, index, renderer, new Color(0xFFD67F14, true), true);
        setRenderer(key, LineKeys.VOLT_AKB_BHV_SEND, index, renderer, new Color(0xFF6DCE17, true), true);
        // общий ток
        setRenderer(key, LineKeys.AMP, index, renderer, new Color(0xFFD60F0D, true), true);
        setRenderer(key, LineKeys.AMP_S1, index, renderer, new Color(0xFFD60F0D, true), true);
        setRenderer(key, LineKeys.AMP_S2, index, renderer, new Color(0xFFD67F14, true), false);
        setRenderer(key, LineKeys.AMP_S3, index, renderer, new Color(0xFFD61D94, true), false);
        setRenderer(key, LineKeys.AMP_S4, index, renderer, new Color(0xFFA951D6, true), false);
        // ток якоря
        setRenderer(key, LineKeys.AMP_ANC, index, renderer, new Color(0xFFA127D6, true), true);
        // ток якоря 1
        setRenderer(key, LineKeys.AMP_ANC1_S1, index, renderer, new Color(0xFFA127D6, true), true);
        setRenderer(key, LineKeys.AMP_ANC1_S2, index, renderer, new Color(0xFFD6316B, true), false);
        setRenderer(key, LineKeys.AMP_ANC1_S3, index, renderer, new Color(0xFFD68399, true), false);
        setRenderer(key, LineKeys.AMP_ANC1_S4, index, renderer, new Color(0xFFD6424B, true), false);
        // ток якоря 2
        setRenderer(key, LineKeys.AMP_ANC2_S1, index, renderer, new Color(0xFFD6424B, true), false);
        setRenderer(key, LineKeys.AMP_ANC2_S2, index, renderer, new Color(0xFFD6CB7F, true), false);
        setRenderer(key, LineKeys.AMP_ANC2_S3, index, renderer, new Color(0xFFD69E59, true), false);
        setRenderer(key, LineKeys.AMP_ANC2_S4, index, renderer, new Color(0xFFB6D68E, true), false);
        // ток якоря 3
        setRenderer(key, LineKeys.AMP_ANC3_S1, index, renderer, new Color(0xFF75D622, true), false);
        setRenderer(key, LineKeys.AMP_ANC3_S2, index, renderer, new Color(0xFF54D6B7, true), false);
        setRenderer(key, LineKeys.AMP_ANC3_S3, index, renderer, new Color(0xFFD574D6, true), false);
        setRenderer(key, LineKeys.AMP_ANC3_S4, index, renderer, new Color(0xFFD633B1, true), false);
        // ток якоря 4
        setRenderer(key, LineKeys.AMP_ANC4_S1, index, renderer, new Color(0xFF7788D6, true), false);
        setRenderer(key, LineKeys.AMP_ANC4_S2, index, renderer, new Color(0xFFD65B86, true), false);
        setRenderer(key, LineKeys.AMP_ANC4_S3, index, renderer, new Color(0xFFC2D695, true), false);
        setRenderer(key, LineKeys.AMP_ANC4_S4, index, renderer, new Color(0xFFAF96D6, true), false);
        // ток возбуждения
        setRenderer(key, LineKeys.AMP_EXC, index, renderer, new Color(0xFFD6CF77, true), true);
        setRenderer(key, LineKeys.AMP_EXC_S1, index, renderer, new Color(0xFFD6CF77, true), true);
        setRenderer(key, LineKeys.AMP_EXC_S2, index, renderer, new Color(0xFFD68962, true), false);
        // ток возбуждения 1
        setRenderer(key, LineKeys.AMP_EXC1_S1, index, renderer, new Color(0xFFD6CF77, true), true);
        setRenderer(key, LineKeys.AMP_EXC1_S2, index, renderer, new Color(0xFFD68962, true), false);
        setRenderer(key, LineKeys.AMP_EXC1_S3, index, renderer, new Color(0xFFC2D67B, true), false);
        setRenderer(key, LineKeys.AMP_EXC1_S4, index, renderer, new Color(0xFF74D670, true), false);
        // ток возбуждения 2
        setRenderer(key, LineKeys.AMP_EXC2_S1, index, renderer, new Color(0xFFD69D7B, true), false);
        setRenderer(key, LineKeys.AMP_EXC2_S2, index, renderer, new Color(0xFFD6727E, true), false);
        setRenderer(key, LineKeys.AMP_EXC2_S3, index, renderer, new Color(0xFFD6569E, true), false);
        setRenderer(key, LineKeys.AMP_EXC2_S4, index, renderer, new Color(0xFFCE65D6, true), false);
        // ток возбуждения 3
        setRenderer(key, LineKeys.AMP_EXC3_S1, index, renderer, new Color(0xFFAE58D6, true), false);
        setRenderer(key, LineKeys.AMP_EXC3_S2, index, renderer, new Color(0xFF8553D6, true), false);
        setRenderer(key, LineKeys.AMP_EXC3_S3, index, renderer, new Color(0xFF9E8ED6, true), false);
        setRenderer(key, LineKeys.AMP_EXC3_S4, index, renderer, new Color(0xFF5677D6, true), false);
        // ток возбуждения 4
        setRenderer(key, LineKeys.AMP_EXC4_S1, index, renderer, new Color(0xFFB4D6BD, true), false);
        setRenderer(key, LineKeys.AMP_EXC4_S2, index, renderer, new Color(0xFF9095D6, true), false);
        setRenderer(key, LineKeys.AMP_EXC4_S3, index, renderer, new Color(0xFFD68CD1, true), false);
        setRenderer(key, LineKeys.AMP_EXC4_S4, index, renderer, new Color(0xFFD68A86, true), false);
        // сила
        setRenderer(key, LineKeys.POWER_LOC, index, renderer, new Color(0xFFD60863, true), true);
        setRenderer(key, LineKeys.POWER_LOC_SLAVE, index, renderer, new Color(0xFFD61EA6, true), true);
        setRenderer(key, LineKeys.POWER_TASK_CONTROL, index, renderer, new Color(0xFFD6D31E, true), true);
        setRenderer(key, LineKeys.POWER_TASK_AUTO, index, renderer, new Color(0xFF3A7ACE, true), true);
        setRenderer(key, LineKeys.POWER_MAX, index, renderer, new Color(0xFF831ED6, true), true);
        setRenderer(key, LineKeys.POWER_MAX_REC, index, renderer, new Color(0xFFD01ED6, true), true);
        // расход энергии
        setRenderer(key, LineKeys.CONSUMPTION_EN, index, renderer, new Color(0xFFD60F0D, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_EN_S1, index, renderer, new Color(0xFFD60F0D, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_EN_S2, index, renderer, new Color(0xFFD65106, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_EN_S3, index, renderer, new Color(0xFFD60863, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_EN_S4, index, renderer, new Color(0xFFD61EA6, true), true);

        setRenderer(key, LineKeys.CONSUMPTION_R_EN, index, renderer, new Color(0xFF41D667, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_R_EN_S1, index, renderer, new Color(0xFF41D667, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_R_EN_S2, index, renderer, new Color(0xFFB2D667, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_R_EN_S3, index, renderer, new Color(0xFF74D6C4, true), true);
        setRenderer(key, LineKeys.CONSUMPTION_R_EN_S4, index, renderer, new Color(0xFF6C93D6, true), true);
        // расстояние до хвоста
        setRenderer(key, LineKeys.DISTANCE_TAIL, index, renderer, new Color(0xFF2FD60D, true), true);
        // давление УСАВП
        setRenderer(key, LineKeys.PRESS_UR_USAVP, index, renderer, new Color(0xFFD65B60, true), true);
        setRenderer(key, LineKeys.PRESS_TM_USAVP, index, renderer, new Color(0xFFD60FCE, true), true);
        setRenderer(key, LineKeys.PRESS_TC_USAVP, index, renderer, new Color(0xFFD6CC68, true), true);
        setRenderer(key, LineKeys.PRESS_ZTS_USAVP, index, renderer, new Color(0xFF9DD668, true), true);
        // давление
        setRenderer(key, LineKeys.PRESS_PM, index, renderer, new Color(0xFFD6880B, true),false);
        setRenderer(key, LineKeys.PRESS_UR, index, renderer, new Color(0xFFD65B60, true),false);
        setRenderer(key, LineKeys.PRESS_TM, index, renderer, new Color(0xFF7F31D6, true),false);
        setRenderer(key, LineKeys.PRESS_TC, index, renderer, new Color(0xFF47D6A5, true),false);
        setRenderer(key, LineKeys.PRESS_NM, index, renderer, new Color(0xFFE54DBE, true),false);
        // давление 1-я секция
        setRenderer(key, LineKeys.PRESS_UR_S1, index, renderer, new Color(0xFFD6A5A9, true),false);
        setRenderer(key, LineKeys.PRESS_TM_S1, index, renderer, new Color(0xFF7F31D6, true),false);
        setRenderer(key, LineKeys.PRESS_TC_S1, index, renderer, new Color(0xFF47D6A5, true),false);
        // давление 2-я секция
        setRenderer(key, LineKeys.PRESS_UR_S2, index, renderer, new Color(0xFFD67470, true),false);
        setRenderer(key, LineKeys.PRESS_TM_S2, index, renderer, new Color(0xFFA87FD6, true),false);
        setRenderer(key, LineKeys.PRESS_TC_S2, index, renderer, new Color(0xFFBCD690, true),false);
        setRenderer(key, LineKeys.PRESS_TM_TAIL, index, renderer, new Color(0xFF2FD60D, true),false);
        setRenderer(key, LineKeys.PRESS_CAMERA_BHV, index, renderer, new Color(0xFFD6CC0D, true),false);
        // скорость
        setRenderer(key, LineKeys.SPEED_MAX, index, renderer, COLOR_LIM_MAP,true); // максимальная скорость (невидимая линия над временными ограничениями)
        if (key == LineKeys.SPEED_LIM_TMP) { // ограничения скорости временные
            BasicStroke basicStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
            renderer.setSeriesStroke(index, basicStroke);
            renderer.setSeriesPaint(index, new Color(0x7F8265FF, true));
        }
        setRenderer(key, LineKeys.SPEED_LIM_CUR, index, renderer, new Color(0xFFFFFFFF, true),false); // текущее ограничение скорости
        setRenderer(key, LineKeys.SPEED_USAVP, index, renderer, new Color(0xFFFFDC0E, true),true); // УСАВП
        setRenderer(key, LineKeys.SPEED_FACT, index, renderer, new Color(0xFFFFDC0E, true),true); // факт asim
        setRenderer(key, LineKeys.SPEED_CALC, index, renderer, new Color(0xFF4EE0B9, true),false); // расчетная asim
        setRenderer(key, LineKeys.SPEED_GPS, index, renderer, new Color(0xFFFF7E09, true),false); // клуб asim
        setRenderer(key, LineKeys.SPEED_ERG, index, renderer, new Color(0xFF4EE0B9, true),false); // расчетная по эргу
        setRenderer(key, LineKeys.SPEED_CLUB, index, renderer, new Color(0xFFFF7E09, true),false); // клуб
        setRenderer(key, LineKeys.SPEED_BS_DPS, index, renderer, new Color(0xFF09FF42, true),false); //
        setRenderer(key, LineKeys.SPEED_CLUB_LIM, index, renderer, new Color(0xFFFF3AFB, true),false); //
        setRenderer(key, LineKeys.SPEED_SLAVE, index, renderer, new Color(0xFFB2E86D, true),false); // второго
        setRenderer(key, LineKeys.SPEED_MSUD, index, renderer, new Color(0xFF9841FF, true),false); // МСУД
        setRenderer(key, LineKeys.SPEED_BUT, index, renderer, new Color(0xFF414AFF, true),false); // БУТ
        setRenderer(key, LineKeys.SPEED_BLOCK, index, renderer, new Color(0xFFFF4183, true),false); // БУТ
        setRenderer(key, LineKeys.SPEED_TASK, index, renderer, new Color(0xFF41FFB6, true),false); //
        // позиция, ослабление поля
        setRenderer(key, LineKeys.POSITION_S5k, index, renderer, new Color(0x3FD7D53E, true),true); //
        setRenderer(key, LineKeys.POSITION, index, renderer, new Color(0x3FD7D53E, true),true); //
        setRenderer(key, LineKeys.POSITION_TASK_AUTO, index, renderer, new Color(0x5FD635B0, true),true); //
        setRenderer(key, LineKeys.WEAK_FIELD, index, renderer, new Color(0x473550D6, true),true); //
        // профиль
        if (key == LineKeys.PROFILE) {
            setStrokeDef(index, renderer);
            renderer.setSeriesPaint(index, COLOR_PROFILE);
        }
        if (key == LineKeys.PROFILE_DIRECT) {
            setStrokeDef(index, renderer);
            renderer.setSeriesPaint(index, new Color(0x00FFFFFF, true));
        }
        if (key == LineKeys.MAP_DIRECT) {
            setStrokeDef(index, renderer);
            renderer.setSeriesPaint(index, new Color(0x00FFFFFF, true));
        }
        if (key == LineKeys.MAP_LINE) {
            renderer.setSeriesStroke(index, new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            renderer.setSeriesPaint(index, new Color(0xFFFFFFFF, true));
        }
    }

    /**
     * установка параметров линии по умолчанию
     * @param i - индекс линии в plot
     * @param rend - XYItemRenderer
     */
    private static void setStrokeDef(int i, XYItemRenderer rend) {
        BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        rend.setSeriesStroke(i, basicStroke);
    }

    /**
     * установка видимости линии с сохранением в mapVisible
     * @param renderer -
     * @param index - индекс линии
     * @param key - ключ линии
     * @param def - видимость по умолчанию
     */
    private static void setSeriesVisible(XYItemRenderer renderer, int index, LineKeys key, boolean def) {
       // renderer.setSeriesVisible(index, mapVisible.get(key) != null ? mapVisible.get(key) : def, true);
        mapDefVisible.put(key.getName(), def);
        if (mapVisible.get(key.getName()) == null) {
            renderer.setSeriesVisible(index, def, true);
            mapVisible.put(key.getName(), def);
        }
        else {
            renderer.setSeriesVisible(index, mapVisible.get(key.getName()), true);
        }
    }

    public XYSeriesCollection getSerVoltageCollect_cs() {
        return serVoltageCollect_cs;
    }

    public XYSeriesCollection getSerVoltageCollect() {
        return serVoltageCollect;
    }

    public XYSeriesCollection getSerAmperageCollect_anchor() {
        return serAmperageCollect_anchor;
    }

    public XYSeriesCollection getSerAmperageCollect_excitation() {
        return serAmperageCollect_excitation;
    }

    public XYSeriesCollection getSerAmperageCollect_engine() {
        return serAmperageCollect_engine;
    }

    public XYSeriesCollection getSerAmperageCollect() {
        return serAmperageCollect;
    }

    public XYSeriesCollection getSerPressCollect() {
        return serPressCollect;
    }

    public XYSeriesCollection getSerSpeedCollect() {
        return serSpeedCollect;
    }

    public XYSeriesCollection getSerConsumptionEnCollect() {
        return serConsumptionEnCollect;
    }

    public XYSeriesCollection getSerPowerCollect() {
        return serPowerCollect;
    }

    public XYSeriesCollection getSerDistanceTailCollect() {
        return serDistanceTailCollect;
    }

    public XYSeriesCollection getSerProfileCollect() {
        return serProfileCollect;
    }

    public XYSeriesCollection getSerPositionCollect() {
        return serPositionCollect;
    }

    public XYSeriesCollection getSerMapCollect() {
        return serMapCollect;
    }

    public XYSeriesCollection getSerTrainCollect() {
        return serTrainCollect;
    }

    public XYSeriesCollection getSerAmperageCollect_common() {
        return serAmperageCollect_common;
    }

    public static Map<String, Boolean> getMapVisible() {
        return mapVisible;
    }

    public static void setMapVisible(Map<String, Boolean> mapVisible) {
        SeriesLines.mapVisible = mapVisible;
    }
}
