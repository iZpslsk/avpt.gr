package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskVsc extends SeriesTasks {

    public static final int VSC_UNKNOWN = -1;    // режим ведомого ВСЦ выключен
    public static final int VSC_OFF = 0;    // режим ведомого ВСЦ выключен
    public static final int VSC_ON = 1;     // следование в виртуальной сцепке
    public static final int VSC_ALSN = 2;   // переход на АЛСН
    public static final int VSC_NO_TRAFFIC_LIGHT = 3; // нет открытия светофора


    public TaskVsc(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(VSC_UNKNOWN, Color.GRAY);
        getMapPaints().put(VSC_OFF, new Color(0xB7B536));
        getMapPaints().put(VSC_ON, new Color(0x19A521));
        getMapPaints().put(VSC_ALSN, new Color(0x0F1EC7));
        getMapPaints().put(VSC_NO_TRAFFIC_LIGHT, new Color(0x8B190B));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = "нет";
        switch (val) {
            case VSC_OFF:
                result = "режим ведомого ВСЦ выключен";
                break;
            case VSC_ON:
                result = "следование в виртуальной сцепке";
                break;
            case VSC_ALSN:
                result = "переход на АЛСН";
                break;
            case VSC_NO_TRAFFIC_LIGHT:
                result = "нет открытия светофора";
                break;
        }
        return result;
    }
}
