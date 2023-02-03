package avpt.gr.chart_dataset;

import java.util.ArrayList;

import static avpt.gr.chart_dataset.TaskAlsn.*;
import static avpt.gr.chart_dataset.TaskAlsn.COLOR_ALSN_RED;

public class TaskAlsnClub extends SeriesTasks {

    public static final int ALSN_UNKNOWN = -1;
    public static final int ALSN_YELLOW = 3;
    public static final int ALSN_WHITE = 0;
    public static final int ALSN_GREEN = 4;
    public static final int ALSN_REDYELLOW= 2;
    public static final int ALSN_RED = 1;
    public static final int ALSN_YELLOW_FLASH = 5;

    public TaskAlsnClub(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(ALSN_UNKNOWN, COLOR_ALSN_UNKNOWN);
        getMapPaints().put(ALSN_WHITE, COLOR_ALSN_WHITE);
        getMapPaints().put(ALSN_GREEN, COLOR_ALSN_GREEN);
        getMapPaints().put(ALSN_YELLOW, COLOR_ALSN_YELLOW);
        getMapPaints().put(ALSN_REDYELLOW, COLOR_ALSN_REDYELLOW);
        getMapPaints().put(ALSN_RED, COLOR_ALSN_RED);
        getMapPaints().put(ALSN_YELLOW_FLASH, COLOR_ALSN_YELLOW_FLASH);
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
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
            case ALSN_YELLOW_FLASH: result = " Мигающий";
                break;
        }

        return result;
    }
}
