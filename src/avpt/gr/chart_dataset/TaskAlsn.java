package avpt.gr.chart_dataset;

import avpt.gr.graph.DualColor;

import java.awt.*;
import java.util.ArrayList;

import static avpt.gr.train.Train.*;

public class TaskAlsn extends SeriesTasks {

    public static final int ALSN_UNKNOWN = 0;
    public static final int ALSN_YELLOW = 1;
    public static final int ALSN_WHITE = 2;
    public static final int ALSN_GREEN = 3;
    public static final int ALSN_REDYELLOW = 4;
    public static final int ALSN_RED = 5;

    public static final int SHIFT_ASIM = 10;
    public static final int ALSN_UNKNOWN_ASIM = -1;
    public static final int ALSN_YELLOW_ASIM = 3 + SHIFT_ASIM;
    public static final int ALSN_WHITE_ASIM = SHIFT_ASIM;
    public static final int ALSN_GREEN_ASIM = 4 + SHIFT_ASIM;
    public static final int ALSN_REDYELLOW_ASIM = 2 + SHIFT_ASIM;
    public static final int ALSN_RED_ASIM = 1 + SHIFT_ASIM;

    public static final Color COLOR_ALSN_UNKNOWN = Color.GRAY;
    public static final Color COLOR_ALSN_WHITE = new Color(0xFFFEF9FF, true);
    public static final Color COLOR_ALSN_GREEN = new Color(0x136417);
    private static final Color YELLOW = new Color(0xD4CC0B);
    public static final Color COLOR_ALSN_YELLOW = YELLOW;
    public static final Paint COLOR_ALSN_REDYELLOW = new DualColor(YELLOW, new Color(0xA90F14));
    public static final Color COLOR_ALSN_RED = new Color(0xA51217);
    public static final GradientPaint COLOR_ALSN_YELLOW_FLASH = new GradientPaint(
            0f, 0f, new Color(0xA0A0A0), 0f, 0f, new Color(0xD4CC0B));

    private int typeLoc;

    public TaskAlsn(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
        for (int i = 0; i < arrTasks.get(0).size() && typeLoc == 0; i++) {
            typeLoc = arrTasks.get(0).get(i).loc_type;
        }
    }

    private boolean isAsim(int typeLoc) {
        return (typeLoc >= VL11_ASIM && typeLoc <= VL80P_ASIM) || (typeLoc >= VL80SK_ASIM && typeLoc <= VL11M_ASIM);
    }

    @Override
    void setPaint(int index) {
//        if (!isAsim(typeLoc)) {
            getMapPaints().put(ALSN_UNKNOWN, COLOR_ALSN_UNKNOWN);
            getMapPaints().put(ALSN_WHITE, COLOR_ALSN_WHITE);
            getMapPaints().put(ALSN_GREEN, COLOR_ALSN_GREEN);
            getMapPaints().put(ALSN_YELLOW, COLOR_ALSN_YELLOW);
            getMapPaints().put(ALSN_REDYELLOW, COLOR_ALSN_REDYELLOW);
            getMapPaints().put(ALSN_RED, COLOR_ALSN_RED);
//        }
//        else {
            getMapPaints().put(ALSN_UNKNOWN_ASIM, COLOR_ALSN_UNKNOWN);
            getMapPaints().put(ALSN_WHITE_ASIM, COLOR_ALSN_WHITE);
            getMapPaints().put(ALSN_GREEN_ASIM, COLOR_ALSN_GREEN);
            getMapPaints().put(ALSN_YELLOW_ASIM, COLOR_ALSN_YELLOW);
            getMapPaints().put(ALSN_REDYELLOW_ASIM, COLOR_ALSN_REDYELLOW);
            getMapPaints().put(ALSN_RED_ASIM, COLOR_ALSN_RED);
//        }
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        if (!isAsim(typeLoc))
            return getDescriptNotAsim(val);
        else
            return getDescriptAsim(val);
    }

    public String getDescriptNotAsim(int val) {
        String result = "нет";
        switch (val) {
            case ALSN_WHITE: result = " Белый";
                break;
            case ALSN_GREEN: result = " Зеленый";
                break;
            case ALSN_YELLOW: result = " Желтый";
                break;
            case ALSN_REDYELLOW: result = " КЖ";
                break;
            case ALSN_RED: result = " Красный";
                break;
        }
        return result;
    }

    public String getDescriptAsim(int val) {
        String result = "нет";
        switch (val) {
            case ALSN_WHITE_ASIM: result = " Белый";
                break;
            case ALSN_GREEN_ASIM: result = " Зеленый";
                break;
            case ALSN_YELLOW_ASIM: result = " Желтый";
                break;
            case ALSN_REDYELLOW_ASIM: result = " КЖ";
                break;
            case ALSN_RED_ASIM: result = " Красный";
                break;
        }
        return result;
    }

}
