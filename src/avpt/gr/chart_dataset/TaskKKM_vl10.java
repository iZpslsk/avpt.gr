package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKKM_vl10 extends SeriesTasks {

    private static final int KKM_UNKNOWN = 0;         //
    private static final int KKM_RELEASE = 9;         // Отпуск
    private static final int KKM_DRAWING = 25;        // Поездное
    private static final int KKM_WITHOUT_EAT = 12;    // Перекрыша без питания
    private static final int KKM_WITH_EAT = 28;       // Перекрыша с питанием
    private static final int KKM_BRAKE_SER = 26;      // 10 // Торможение служебное
    private static final int KKM_BRAKE_SLOWDOWN = 10; // 26 // Торможение с замедлением
    private static final int KKM_BRAKE_EMERGENCY = 2; // Экстренное торможение

    public TaskKKM_vl10(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(KKM_UNKNOWN, Color.GRAY);
        getMapPaints().put(KKM_RELEASE, new Color(0xB7B536));
        getMapPaints().put(KKM_DRAWING, new Color(0x348B2A));
        getMapPaints().put(KKM_WITHOUT_EAT, new Color(0xB78349));
        getMapPaints().put(KKM_WITH_EAT, new Color(0x7C8B25));
        getMapPaints().put(KKM_BRAKE_SER, new Color(0x8B4243));
        getMapPaints().put(KKM_BRAKE_SLOWDOWN, new Color(0x8A2D8B));
        getMapPaints().put(KKM_BRAKE_EMERGENCY, new Color(0xFF0101));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = "нет";
        String tag = (key + 1) + "-я секция ";
        switch (val) {
            case KKM_RELEASE:
                result = "I - отпуск";
                break;
            case KKM_DRAWING:
                result = "II - поездное";
                break;
            case KKM_WITHOUT_EAT:
                result = "III - перекрыша без питания";
                break;
            case KKM_WITH_EAT:
                result = "IV - перекрыша с питанием";
                break;
            case KKM_BRAKE_SER:
                result = "V - торможение служебное";
                break;
            case KKM_BRAKE_SLOWDOWN:
                result = "Va - торможение с замедлением";
                break;
            case KKM_BRAKE_EMERGENCY:
                result = "VI - экстренное торможение";
                break;
        }
        return tag + result;
    }
}
