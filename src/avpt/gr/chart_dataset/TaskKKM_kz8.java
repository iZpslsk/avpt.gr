package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKKM_kz8 extends SeriesTasks {

    // kkm
    public static final int KKM_RELEASE = 0xF;          // I - отпуск
    public static final int KKM_TRAIN = 0xA;            // II - поездное
    public static final int KKM_OVERLAP_NO_SUPPLY = 0x9;// III - перекрыша без питания
    public static final int KKM_OVERLAP_SUPPLY = 0xC;   // IV - перекрыша с питанием
    public static final int KKM_BRAKING_SLOW = 0x6;     // Va - торможение с замедлением
    public static final int KKM_BRAKING = 0x3;          // V - торможение служебное
    public static final int KKM_BRAKING_EMERGENCY = 0x0;// VI - торможение экстренное

    // kkbt
    public static final int KKBT_RELEASE_TRAIN = 1;            // I - отпуск/поездн.
    public static final int KKBT_RELEASE = 2;          // II - отпуск
    public static final int KKBT_OVERLAP = 3;          // III - перекрыша
    public static final int KKBT_BRAKE = 4;          // IV - тормоз
    public static final int KKBT_BRAKE_FIX = 5;          // V - тормоз (с фиксацией)

    public TaskKKM_kz8(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int i) {
        switch (i) {
            case 0:
                getMapPaints(0).put(KKM_RELEASE, new Color(0xA9A726));
                getMapPaints(0).put(KKM_TRAIN, new Color(0x136417));
                getMapPaints(0).put(KKM_OVERLAP_NO_SUPPLY, new Color(0x2CABC7));
                getMapPaints(0).put(KKM_OVERLAP_SUPPLY, new Color(0x0066FF));
                getMapPaints(0).put(KKM_BRAKING_SLOW, new Color(0xA6576D));
                getMapPaints(0).put(KKM_BRAKING, new Color(0x8B0D6E));
                getMapPaints(0).put(KKM_BRAKING_EMERGENCY, new Color(0xFF0101));
                break;
            case 1:
                getMapPaints(1).put(KKBT_RELEASE_TRAIN, new Color(0x1B4B40));
                getMapPaints(1).put(KKBT_RELEASE, new Color(0xA9A726));
                getMapPaints(1).put(KKBT_OVERLAP, new Color(0x79A926));
                getMapPaints(1).put(KKBT_BRAKE, new Color(0x0000FF));
                getMapPaints(1).put(KKBT_BRAKE_FIX, new Color(0xFF00CC));
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
        String result = " нет";
        switch (val) {
            case KKM_RELEASE:
                result = " I - отпуск";
                break;
            case KKM_TRAIN:
                result = " II - поездное";
                break;
            case KKM_OVERLAP_NO_SUPPLY:
                result = " III - перекрыша без питания";
                break;
            case KKM_OVERLAP_SUPPLY:
                result = " IV - перекрыша с питанием";
                break;
            case KKM_BRAKING_SLOW:
                result = " V - торможение с замедлением";
                break;
            case KKM_BRAKING:
                result = " Va - торможение служебное";
                break;
            case KKM_BRAKING_EMERGENCY:
                result = " VI - торможенме экстренное";
                break;
        }
        return " ККМ: " + result;
    }

    private String getDescriptKKBT(int val) {
        String result = " нет";
        switch (val) {
            case KKBT_RELEASE_TRAIN:
                result = " I - отпуск/поездн.";
                break;
            case KKBT_RELEASE:
                result = " II - отпуск";
                break;
            case KKBT_OVERLAP:
                result = " III - перекрыша";
                break;
            case KKBT_BRAKE:
                result = " IV - тормоз";
                break;
            case KKBT_BRAKE_FIX:
                result = " V - тормоз (с фиксацией)";
                break;
        }
        return " ККБТ: " + result;
    }
}
