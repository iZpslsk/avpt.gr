package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskPneumaticUsavp extends SeriesTasks {

    public static final int PNEUMATIC_UNKNOWN = 0;
    public static final int PNEUMATIC_BRAKE = 1;        //торможение
    public static final int PNEUMATIC_OVERLAP = 2;      // перекрыша
    public static final int PNEUMATIC_RELEASE_BRAKE = 3;    // отпуск
    public static final int PNEUMATIC_TRAIN = 4;        // поездное

    public TaskPneumaticUsavp(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int i) {
        getMapPaints(0).put(PNEUMATIC_UNKNOWN, Color.GRAY);
        getMapPaints(0).put(PNEUMATIC_BRAKE, new Color(0xFF0101));
        getMapPaints(0).put(PNEUMATIC_OVERLAP, new Color(0x0066FF));
        getMapPaints(0).put(PNEUMATIC_RELEASE_BRAKE, new Color(0xA9A726));
        getMapPaints(0).put(PNEUMATIC_TRAIN, new Color(0x136417));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = "нет";
        switch (val) {
            case PNEUMATIC_BRAKE:
                result = "торможение";
                break;
            case PNEUMATIC_OVERLAP:
                result = "перекрыша";
                break;
            case PNEUMATIC_RELEASE_BRAKE:
                result = "отпуск";
                break;
            case PNEUMATIC_TRAIN:
                result = "поездное";
                break;
        }
        return result;
    }
}
