package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKKM_s5 extends SeriesTasks {

    // kkm
    public static final int KKM_UNKNOWN = 0;
    public static final int KKM_RELEASE = 1;          // I - отпуск
    public static final int KKM_TRAIN = 2;            // II - поездное
    public static final int KKM_OVERLAP_NO_SUPPLY = 4;// III - перекрыша без питания
    public static final int KKM_OVERLAP_SUPPLY = 8;   // IV - перекрыша с питанием
    public static final int KKM_BRAKING_SLOW = 16;     // Va - торможение с замедлением
    public static final int KKM_BRAKING = 32;          // V - торможение служебное
    public static final int KKM_BRAKING_EMERGENCY = 64;// VI - торможение экстренное

    // kkbt
    public static final int KKBT_TRAIN = 1;            // поездное
    public static final int KKBT_STAIR_1 = 2;          // I ступень
    public static final int KKBT_STAIR_2 = 3;          // II ступень
    public static final int KKBT_STAIR_3 = 4;          // III ступень
    public static final int KKBT_STAIR_4 = 5;          // IV ступень

    public TaskKKM_s5(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int i) {
        switch (i) {
            case 0:
                getMapPaints(0).put(KKM_UNKNOWN, Color.GRAY);
                getMapPaints(0).put(KKM_RELEASE, new Color(0xA9A726));
                getMapPaints(0).put(KKM_TRAIN, new Color(0x136417));
                getMapPaints(0).put(KKM_OVERLAP_NO_SUPPLY, new Color(0x2CABC7));
                getMapPaints(0).put(KKM_OVERLAP_SUPPLY, new Color(0x0066FF));
                getMapPaints(0).put(KKM_BRAKING_SLOW, new Color(0xA6576D));
                getMapPaints(0).put(KKM_BRAKING, new Color(0x8B0D6E));
                getMapPaints(0).put(KKM_BRAKING_EMERGENCY, new Color(0xFF0101));
                break;
            case 1:
                getMapPaints(1).put(KKBT_TRAIN, new Color(0x1B4B40));
                getMapPaints(1).put(KKBT_STAIR_1, new Color(0xA9A726));
                getMapPaints(1).put(KKBT_STAIR_2, new Color(0x79A926));
                getMapPaints(1).put(KKBT_STAIR_3, new Color(0x0000FF));
                getMapPaints(1).put(KKBT_STAIR_4, new Color(0xFF00CC));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        if (key == 0)
            return getDescriptKKM(val);
        else
            return getDescriptKKBT(val);
    }

    private String getDescriptKKM(int val) {
        String result = "нет";
        switch (val) {
            case KKM_RELEASE:
                result = "I - отпуск";
                break;
            case KKM_TRAIN:
                result = "II - поездное";
                break;
            case KKM_OVERLAP_NO_SUPPLY:
                result = "III - перекрыша без питания";
                break;
            case KKM_OVERLAP_SUPPLY:
                result = "IV - перекрыша с питанием";
                break;
            case KKM_BRAKING_SLOW:
                result = "V - торможение с замедлением";
                break;
            case KKM_BRAKING:
                result = "Va - торможение служебное";
                break;
            case KKM_BRAKING_EMERGENCY:
                result = "VI - торможенме экстренное";
                break;
        }
        return " ККМ: " + result;
    }

    private String getDescriptKKBT(int val) {
        String result = "нет";
        switch (val) {
            case KKBT_TRAIN:
                result = "поездное";
                break;
            case KKBT_STAIR_1:
                result = "I ступень";
                break;
            case KKBT_STAIR_2:
                result = "II ступень";
                break;
            case KKBT_STAIR_3:
                result = "III ступень";
                break;
            case KKBT_STAIR_4:
                result = "IV ступень";
                break;
        }
        return " ККБТ: " + result;
    }
}
