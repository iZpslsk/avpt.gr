package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKKM_s5k extends SeriesTasks {

    private static final int KKM_UNKNOWN = 0;       //
    private static final int KKM_I_II_1 = 132;       //
    private static final int KKM_I_II_2 = 12;        //
    private static final int KKM_III_IV_1 = 144;     //
    private static final int KKM_III_IV_2 = 72;      //
    private static final int KKM_V_Va_1 = 208;       //
    private static final int KKM_V_Va_2 = 24;        //
    private static final int KKM_VI_1 = 80;          //
    private static final int KKM_VI_2 = 16;          //

    public TaskKKM_s5k(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(KKM_UNKNOWN, Color.GRAY);
        getMapPaints().put(KKM_I_II_1, new Color(0xB7B536));
        getMapPaints().put(KKM_I_II_2, new Color(0xB7B536));
        getMapPaints().put(KKM_III_IV_1, new Color(0x348B2A));
        getMapPaints().put(KKM_III_IV_2, new Color(0x348B2A));
        getMapPaints().put(KKM_V_Va_1, new Color(0x8B4243));
        getMapPaints().put(KKM_V_Va_2, new Color(0x8B4243));
        getMapPaints().put(KKM_VI_1, new Color(0x624464));
        getMapPaints().put(KKM_VI_2, new Color(0x624464));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = " нет";
        switch (val) {
            case KKM_I_II_1:
            case KKM_I_II_2:
                result = " I-II";
                break;
            case KKM_III_IV_1:
            case KKM_III_IV_2:
                result = " III-IV";
                break;
            case KKM_V_Va_1:
            case KKM_V_Va_2:
                result = " V-Va";
                break;
            case KKM_VI_1:
            case KKM_VI_2:
                result = " VI";
                break;
        }
        return result;
    }
}
