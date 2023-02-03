package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

/**
 * дискретные сигналы - автодиспетчер
 */
public class SeriesSignalsAutodisp extends SeriesSignals {

    public SeriesSignalsAutodisp(ListSignals listSignals) {
        // 0x21_7
        addTaskSeries(KEY_READY, listSignals.getListReady());
        addTaskSeries(KEY_CAN, listSignals.getListCan());
        addTaskSeries(KEY_ERROR, listSignals.getListError());
        addTaskSeries(KEY_CHANGE_ACCEPTED_SCHEDULE, listSignals.getListChangeAcceptedSchedule());
        addTaskSeries(KEY_CHANGE_VALID_SCHEDULE, listSignals.getListChangeAcceptedValidSchedule());
        addTaskSeries(KEY_CHANGE_LOAD_SCHEDULE, listSignals.getListChangeLoadSchedule());
        addTaskSeries(KEY_SOURCE_SCHEDULE, listSignals.getListSourceSchedule());
        addTaskSeries(KEY_MODE_WORK, listSignals.getListModeWork());
        addTaskSeries(KEY_LEVEL_GPRS, listSignals.getListLevelGPRS());
        addTaskSeries(KEY_SEND_DIAG, listSignals.getListSendDiag());
        addTaskSeries(KEY_UPDATE, listSignals.getListUpdate());
        addTaskSeries(KEY_ESM_BS, listSignals.getListEsmBs());
        addTaskSeries(KEY_LINK_SERVER, listSignals.getListLinkServer());
        addTaskSeries(KEY_LINK_GATEWAY, listSignals.getListLinkGateway());
        addTaskSeries(KEY_GPRS, listSignals.getListGPRS());
        addTaskSeries(KEY_AUTO_SCHEDULE, listSignals.getListAutoSchedule());
    }

    public Color getColorSeries(int key) {
        Color color =Color.GRAY;
        switch (key) {
            case KEY_READY : color = new Color(0xFFA07A);
                break;
            case KEY_CAN : color = new Color(0x32CD32);
                break;
            case KEY_ERROR : color = new Color(0x800000);
                break;
            case KEY_CHANGE_ACCEPTED_SCHEDULE : color = new Color(0xBA55D3);
                break;
            case KEY_CHANGE_VALID_SCHEDULE : color = new Color(0x0000FF);
                break;
            case KEY_CHANGE_LOAD_SCHEDULE : color = new Color(0x8A2BE2);
                break;
            case KEY_SOURCE_SCHEDULE : color = new Color(0xA52A2A);
                break;
            case KEY_MODE_WORK: color = new Color(0xE2E2E2);
                break;
            case KEY_LEVEL_GPRS : color = new Color(0x5F9EA0);
                break;
            case KEY_SEND_DIAG : color = new Color(0xD2691E);
                break;
            case KEY_UPDATE : color = new Color(0x6495ED);
                break;
            case KEY_ESM_BS : color = new Color(0x8B008B);
                break;
            case KEY_LINK_SERVER : color = new Color(0xDC143C);
                break;
            case KEY_LINK_GATEWAY : color = new Color(0x008B8B);
                break;
            case KEY_GPRS : color = new Color(0xB8860B);
                break;
            case KEY_AUTO_SCHEDULE : color = new Color(0x009B00);
                break;
        }
        return color;
    }
}
