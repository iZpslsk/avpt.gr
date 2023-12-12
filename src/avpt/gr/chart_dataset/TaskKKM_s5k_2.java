package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKKM_s5k_2 extends SeriesTasks {

    private static final int KKM_UNKNOWN = 0;       //
    private static final int KKM_I_II = 3;       //
    private static final int KKM_III_IV = 5;     //
    private static final int KKM_V_Va = 9;       //
    private static final int KKM_VI = 8;          //

    public TaskKKM_s5k_2(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(KKM_UNKNOWN, Color.GRAY);
        getMapPaints().put(KKM_I_II, new Color(0xB7B536));
        getMapPaints().put(KKM_III_IV, new Color(0x348B2A));
        getMapPaints().put(KKM_V_Va, new Color(0x8B4243));
        getMapPaints().put(KKM_VI, new Color(0x79467C));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = " нет";
        switch (val) {
            case KKM_I_II:
                result = " I-II";
                break;
            case KKM_III_IV:
                result = " III-IV";
                break;
            case KKM_V_Va:
                result = " V-Va";
                break;
            case KKM_VI:
                result = " VI";
                break;
        }
        return result;
    }
}
