package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

/**
 * Положение реверсивной рукоятки контроллера (физическое)
 */
public class TaskRevControl extends SeriesTasks {

    private static final int UNKNOWN = 0;       //
    private static final int POS_0 = 1;         // 0x1 – положение «0»;
    private static final int POS_FORWARD = 2;   // 0x2 – положение «Вперед»;
    private static final int POS_BACKWARD = 3;  // 0x3 – положение «Назад»;

    public TaskRevControl(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(UNKNOWN, Color.GRAY);
        getMapPaints().put(POS_0, new Color(0xB7B536));
        getMapPaints().put(POS_FORWARD, new Color(0xB7501B));
        getMapPaints().put(POS_BACKWARD, new Color(0x8B1B83));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = " нет";
        switch (val) {
            case POS_0:
                result = " положение «0»";
                break;
            case POS_FORWARD:
                result = " положение «Вперед»";
                break;
            case POS_BACKWARD:
                result = " положение «Назад»";
                break;
        }
        return result;
    }
}
