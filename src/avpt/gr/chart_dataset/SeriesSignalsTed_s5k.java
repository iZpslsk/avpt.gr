package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

/**
 * отключение ТЭД для ЭС5К
 */
public class SeriesSignalsTed_s5k extends SeriesSignals {

    public SeriesSignalsTed_s5k(ListSignals listSignals) {
        addTaskSeries(KEY_TED_1_2, listSignals.getListTed_1_2());
        addTaskSeries(KEY_TED_3_4, listSignals.getListTed_3_4());
        addTaskSeries(KEY_TED_5_6, listSignals.getListTed_5_6());
        addTaskSeries(KEY_TED_7_8, listSignals.getListTed_7_8());
        addTaskSeries(KEY_TED_9_10, listSignals.getListTed_9_10());
        addTaskSeries(KEY_TED_11_12, listSignals.getListTed_11_12());
        addTaskSeries(KEY_TED_13_14, listSignals.getListTed_13_14());
        addTaskSeries(KEY_TED_15_16, listSignals.getListTed_15_16());
    }

    public Color getColorSeries(int key) {
        Color color = Color.GRAY;
        switch (key) {
            case KEY_TED_1_2 : color = new Color(0xFFA07A);
                break;
            case KEY_TED_3_4 : color = new Color(0xFF5C43);
                break;
            case KEY_TED_5_6 : color = new Color(0xFF0A51);
                break;
            case KEY_TED_7_8 : color = new Color(0xFF1498);
                break;
            case KEY_TED_9_10 : color = new Color(0xD399FF);
                break;
            case KEY_TED_11_12 : color = new Color(0x8E79FF);
                break;
            case KEY_TED_13_14 : color = new Color(0x464AFF);
                break;
            case KEY_TED_15_16 : color = new Color(0x59B0FF);
                break;
        }
        return color;
    }
}
