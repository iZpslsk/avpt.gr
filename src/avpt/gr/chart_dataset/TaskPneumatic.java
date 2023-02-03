package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskPneumatic extends SeriesTasks {

    //0 положение
    public static final int PNEUMATIC_UNKNOWN = 0;
    public static final int PNEUMATIC_RELEASE = 1;          // отпуск
    public static final int PNEUMATIC_TRAIN = 2;            // поездное
    public static final int PNEUMATIC_OVERLAP_NO_SUPPLY = 3;// перекрыша без питания
    public static final int PNEUMATIC_OVERLAP_SUPPLY = 4;   // перекрыша с питанием
    public static final int PNEUMATIC_BRAKING_SLOW = 5;     // торможение с замедлением
    public static final int PNEUMATIC_BRAKING = 6;          // торможение служебное
    public static final int PNEUMATIC_BRAKING_EMERGENCY = 7;// торможение экстренное

    //1 block
   // public static final int BLOCK_CONTROL = 0x00;         // управление от блок
    public static final int BLOCK_NO_COMMAND = 0xFF;        // нет комманд блок
    public static final int BLOCK_KKM_I = 0x11;             // перевести ККМ в I
    public static final int BLOCK_KKM_II = 0x13;            // перевести ККМ в II
    public static final int BLOCK_WORK = 0x15;              // работа в составе блок
    public static final int BLOCK_BRAKE_KKM_1 = 0x51;       // торможение от ккм
    public static final int BLOCK_BRAKE_KKM_2 = 0x52;       // торможение от ккм
    public static final int BLOCK_BRAKE_KKM_3 = 0x53;       // торможение от ккм
    public static final int BLOCK_BRAKE_KKM_4 = 0x54;       // торможение от ккм
    public static final int BLOCK_BRAKE_KKM_5 = 0x55;       // торможение от ккм
    public static final int BLOCK_CONTROL = 0x00;           // неизвестное занание

    //2 task
    public static final int TASK_HAND = 1;        // ручка ККМ
    public static final int TASK_SAUT = 2;        // САУТ
    public static final int TASK_RUTP = 3;        // РУТП
    public static final int TASK_ISAVPRT = 4;     // ИСАВП-РТ
    public static final int TASK_MPSU = 5;        // МПСУ

    //3 положенме второго
    public static final int PNEUMATIC_SLAVE_BRAKING = 1;    // торможение
    public static final int PNEUMATIC_SLAVE_OVERLAP = 2;    // перекрыша
    public static final int PNEUMATIC_SLAVE_RELEASE = 3;    // отпуск
    public static final int PNEUMATIC_SLAVE_TRAIN = 4;      // поездное


    private int type_loc = -1;


    public TaskPneumatic(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int i) {
        switch (i) {
            case 0:// положение
                getMapPaints(0).put(PNEUMATIC_UNKNOWN, Color.GRAY);
                getMapPaints(0).put(PNEUMATIC_RELEASE, new Color(0xA9A726));
                getMapPaints(0).put(PNEUMATIC_TRAIN, new Color(0x136417));
                getMapPaints(0).put(PNEUMATIC_OVERLAP_NO_SUPPLY, new Color(0x2CABC7));
                getMapPaints(0).put(PNEUMATIC_OVERLAP_SUPPLY, new Color(0x0066FF));
                getMapPaints(0).put(PNEUMATIC_BRAKING_SLOW, new Color(0xA6576D));
                getMapPaints(0).put(PNEUMATIC_BRAKING, new Color(0x8B0D6E));
                getMapPaints(0).put(PNEUMATIC_BRAKING_EMERGENCY, new Color(0xFF0101));
                break;
            case 1:// статус блок
                getMapPaints(1).put(BLOCK_NO_COMMAND, new Color(0x1B4B40));
                getMapPaints(1).put(BLOCK_KKM_I, new Color(0x47A926));
                getMapPaints(1).put(BLOCK_KKM_II, new Color(0x79A926));
                getMapPaints(1).put(BLOCK_WORK, new Color(0x0000FF));
                getMapPaints(1).put(BLOCK_BRAKE_KKM_1, new Color(0xFF1717));
                getMapPaints(1).put(BLOCK_BRAKE_KKM_2, new Color(0xFF1717));
                getMapPaints(1).put(BLOCK_BRAKE_KKM_3, new Color(0xFF1717));
                getMapPaints(1).put(BLOCK_BRAKE_KKM_4, new Color(0xFF1717));
                getMapPaints(1).put(BLOCK_BRAKE_KKM_5, new Color(0xFF1717));
                getMapPaints(1).put(BLOCK_CONTROL, new Color(0x016723));
                break;
            case 2:// источник задания
                getMapPaints(2).put(TASK_HAND, new Color(0x508793));
                getMapPaints(2).put(TASK_SAUT, new Color(0x42587A));
                getMapPaints(2).put(TASK_RUTP, new Color(0xFD7198));
                getMapPaints(2).put(TASK_ISAVPRT, new Color(0xF872D9));
                getMapPaints(2).put(TASK_MPSU, new Color(0x593E2E));
                break;
            case 3:// положенме второго
                getMapPaints(3).put(PNEUMATIC_SLAVE_BRAKING, new Color(0xFF1717));
                getMapPaints(3).put(PNEUMATIC_SLAVE_OVERLAP, new Color(0x0066FF));
                getMapPaints(3).put(PNEUMATIC_SLAVE_RELEASE, new Color(0xA9A726));
                getMapPaints(3).put(PNEUMATIC_SLAVE_TRAIN, new Color(0x136417));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        switch (key) {
            case 0:
                return getDescriptPneumatic(val);
            case 1:
                return getDescriptBlock(val);
            case 2:
                return getDescriptTask(val);
            case 3:
                return getDescriptPneumaticSlave(val);
            default: return "";
        }
    }

    // 2
    private String getDescriptTask(int val) {
        String result = "неизвестно";
        switch (val) {
            case TASK_HAND:
                result = "ручка ККМ";
                break;
            case TASK_SAUT:
                result = "САУТ";
                break;
            case TASK_RUTP:
                result = "РУТП";
                break;
            case TASK_ISAVPRT:
                result = "ИСАВП-РТ";
                break;
            case TASK_MPSU:
                result = "МПСУ";
                break;
        }
        return " источник задания: " + result;
    }

    // 1
    private String getDescriptBlock(int val) {
        String result = "управление от блок";
        switch (val) {
            case BLOCK_NO_COMMAND:
                result = "нет команд БЛОК";
                break;
            case BLOCK_KKM_I:
                result = "перевести ККМ в I";
                break;
            case BLOCK_KKM_II:
                result = "перевести ККМ в II";
                break;
            case BLOCK_WORK:
                result = "работа в составе блок";
                break;
            case BLOCK_BRAKE_KKM_1:
            case BLOCK_BRAKE_KKM_2:
            case BLOCK_BRAKE_KKM_3:
            case BLOCK_BRAKE_KKM_4:
            case BLOCK_BRAKE_KKM_5:
                result = "торможение от ккм";
                break;
            case BLOCK_CONTROL:
                result = "управление от блок";
                break;
        }
        return " статус БЛОК: " + result;
    }

    // 0
    private String getDescriptPneumatic(int val) {
        String result = "нет";
        switch (val) {
            case PNEUMATIC_RELEASE:
                result = "отпуск";
                break;
            case PNEUMATIC_TRAIN:
                result = "поездное";
                break;
            case PNEUMATIC_OVERLAP_NO_SUPPLY:
                result = "перекрыша без питания";
                break;
            case PNEUMATIC_OVERLAP_SUPPLY:
                result = "перекрыша с питанием";
                break;
            case PNEUMATIC_BRAKING_SLOW:
                result = "торможение с замедлением";
                break;
            case PNEUMATIC_BRAKING:
                result = "торможение служебное";
                break;
            case PNEUMATIC_BRAKING_EMERGENCY:
                result = "торможенме экстренное";
                break;
        }
        return " положение: " + result;
    }

    // 3
    private String getDescriptPneumaticSlave(int val) {
        String result = "";
        switch (val) {
            case PNEUMATIC_SLAVE_BRAKING:
                result = "торможение";
                break;
            case PNEUMATIC_SLAVE_OVERLAP:
                result = "перекрыша";
                break;
            case PNEUMATIC_SLAVE_RELEASE:
                result = "отпуск";
                break;
            case PNEUMATIC_SLAVE_TRAIN:
                result = "поездное";
                break;
        }
        return " положенме 2-го " + result;
    }

    public void setType_loc(int type_loc) {
        this.type_loc = type_loc;
    }
}
