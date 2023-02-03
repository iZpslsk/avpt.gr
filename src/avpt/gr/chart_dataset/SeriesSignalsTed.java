package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

/**
 * отключение ТЭД для ЭС5К МСУД-15
 */
public class SeriesSignalsTed extends SeriesSignals {

    public SeriesSignalsTed(ListSignals listSignals) {
        addTaskSeries(KEY_TED1_1S, listSignals.getListTed1_1s());
        addTaskSeries(KEY_TED2_1S, listSignals.getListTed2_1s());
        addTaskSeries(KEY_TED3_1S, listSignals.getListTed3_1s());
        addTaskSeries(KEY_TED4_1S, listSignals.getListTed4_1s());
        addTaskSeries(KEY_TED1_2S, listSignals.getListTed1_2s());
        addTaskSeries(KEY_TED2_2S, listSignals.getListTed2_2s());
        addTaskSeries(KEY_TED3_2S, listSignals.getListTed3_2s());
        addTaskSeries(KEY_TED4_2S, listSignals.getListTed4_2s());
        addTaskSeries(KEY_TED1_3S, listSignals.getListTed1_3s());
        addTaskSeries(KEY_TED2_3S, listSignals.getListTed2_3s());
        addTaskSeries(KEY_TED3_3S, listSignals.getListTed3_3s());
        addTaskSeries(KEY_TED4_3S, listSignals.getListTed4_3s());
        addTaskSeries(KEY_TED1_4S, listSignals.getListTed1_4s());
        addTaskSeries(KEY_TED2_4S, listSignals.getListTed2_4s());
        addTaskSeries(KEY_TED3_4S, listSignals.getListTed3_4s());
        addTaskSeries(KEY_TED4_4S, listSignals.getListTed4_4s());
    }

    public Color getColorSeries(int key) {
        Color color =Color.GRAY;
        switch (key) {
            case KEY_TED1_1S : color = new Color(0xFFA07A);
                break;
            case KEY_TED2_1S : color = new Color(0xFF5C43);
                break;
            case KEY_TED3_1S : color = new Color(0xFF0A51);
                break;
            case KEY_TED4_1S : color = new Color(0xFF1498);
                break;
            case KEY_TED1_2S : color = new Color(0xD399FF);
                break;
            case KEY_TED2_2S : color = new Color(0x8E79FF);
                break;
            case KEY_TED3_2S : color = new Color(0x464AFF);
                break;
            case KEY_TED4_2S : color = new Color(0x59B0FF);
                break;
            case KEY_TED1_3S : color = new Color(0x60D4FF);
                break;
            case KEY_TED2_3S : color = new Color(0x95FFD4);
                break;
            case KEY_TED3_3S : color = new Color(0x66FF90);
                break;
            case KEY_TED4_3S : color = new Color(0x9FFF56);
                break;
            case KEY_TED1_4S : color = new Color(0xE1FF91);
                break;
            case KEY_TED2_4S : color = new Color(0xEFFF69);
                break;
            case KEY_TED3_4S : color = new Color(0xFFDF5E);
                break;
            case KEY_TED4_4S : color = new Color(0xFFB964);
                break;
        }
        return color;
    }
}
