package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

/**
 * наличие связи
 */
public class SeriesSignalsLink extends SeriesSignals {

    public SeriesSignalsLink(ListSignals listSignals) {
        addTaskSeries(KEY_MAIN_CHANNEL, listSignals.getListMainChannel());
        addTaskSeries(KEY_ADDITIONAL_CHANNEL, listSignals.getListAdditionChannel());
    }

    @Override
    public Color getColorSeries(int key) {
        Color color =Color.GRAY;
        switch (key) {
            case KEY_MAIN_CHANNEL : color = new Color(0xF6F6AA);
                break;
            case KEY_ADDITIONAL_CHANNEL : color = new Color(0xCFF352);
                break;
        }
        return color;
    }
}
