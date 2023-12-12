package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

/**
 * состояни силовой схемы
 */
public class TaskSchema extends SeriesTasks {

    private static final int UNKNOWN = 0;       //
    private static final int DESTROYED = 1;     //  разобрана;
    private static final int TRACT = 2;   //  в тяге;
    private static final int REC = 4;  // в рекуперации;
    private static final int REOSTAT = 8;  // в рекуперации;

    public TaskSchema(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }


    @Override
    void setPaint(int index) {
        getMapPaints().put(UNKNOWN, Color.GRAY);
        getMapPaints().put(DESTROYED, new Color(0xB7B536));
        getMapPaints().put(TRACT, new Color(0xB7501B));
        getMapPaints().put(REC, new Color(0x8B1B83));
        getMapPaints().put(REOSTAT, new Color(0x511B8B));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = " нет";
        switch (val) {
            case DESTROYED:
                result = " разобрана";
                break;
            case TRACT:
                result = " в тяге";
                break;
            case REC:
                result = " в рекуперации";
                break;
            case REOSTAT:
                result = " реостат";
                break;
        }
        return result;
    }
}
