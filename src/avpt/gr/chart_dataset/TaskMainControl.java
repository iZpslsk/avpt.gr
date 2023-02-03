package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

import static avpt.gr.train.Train.S4K;

/**
 * Положение главной рукоятки контроллера (физическое)
 */
public class TaskMainControl extends SeriesTasks {

    // эс5к
    private static final int UNKNOWN = 0;       //
    private static final int POS_0 = 1;         // 0x1 – положение «0»;
    private static final int POS_P_TRACT = 2;   // 0x2 – положение «П» тяги;
    private static final int POS_P_REC = 3;     // 0x3 – положение «П» рекуперации;
    private static final int POS_HP_TRACT = 4;  // 0x4 – положение «НР» тяги;
    private static final int POS_HP_REC = 5;    // 0x5 – положение «НР» рекуперации

  // эс5к_2
//     * 0x1 – положение «0»;
//     * 0x2 – положение «П» тяги;
//     * 0x3 – положение «П» рекуперации;
//     * 0x4 – положение «НР» тяги;
//     * 0x5 – положение «НР» рекуперации.

// эс4к
//     * 0x1 – положение «0»;
//     * 0x2 – положение «П» тяги;
//     * 0x4 – положение «ПТ» торможения;
//     * 0x8 – положение «ТЯГА»;
//     * 0x16 – положение «ТОРМОЖЕНИЕ».

    public TaskMainControl(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(UNKNOWN, Color.GRAY);
        getMapPaints().put(POS_0, new Color(0xB7B536));
        getMapPaints().put(POS_P_TRACT, new Color(0xB7501B));
        getMapPaints().put(POS_P_REC, new Color(0x8B1B83));
        getMapPaints().put(POS_HP_TRACT, new Color(0x10178B));
        getMapPaints().put(POS_HP_REC, new Color(0x4F8B3E));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        //System.out.println(val);
        String result = "нет";
        switch (val) {
            case POS_0:
                result = "положение «0»";
                break;
            case POS_P_TRACT:
                result = "положение «П» тяги";
                break;
            case POS_P_REC:
                if (type_loc == S4K)
                    result = "положение «ПТ» торможения";
                else
                    result = "положение «П» рекуперации";
                break;
            case POS_HP_TRACT:
                if (type_loc == S4K)
                    result = "«ТЯГА»";
                else
                    result = "положение «НР» тяги";
                break;
            case POS_HP_REC:
                if (type_loc == S4K)
                    result = "«ТОРМОЖЕНИЕ»";
                else
                    result = "положение «НР» рекуперации";
                break;
        }
        return result;
    }
}
