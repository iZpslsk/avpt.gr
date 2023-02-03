package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskCabin  extends SeriesTasks  {

    private static final int UNKNOWN = 0;
    private static final int CABIN_1 = 1;
    private static final int CABIN_2 = 2;

    public TaskCabin(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(UNKNOWN, Color.GRAY);
        getMapPaints().put(CABIN_1, new Color(0x080E79));
        getMapPaints().put(CABIN_2, new Color(0x0A7D0B));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = "";
        switch (val) {
            case CABIN_1: result = " 1";
                break;
            case CABIN_2: result = " 2";
                break;
        }
        return result;
    }
}
