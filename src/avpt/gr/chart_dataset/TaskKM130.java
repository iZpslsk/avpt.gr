package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskKM130 extends SeriesTasks {

    // Статус системы исполнения торможения
    public static final int KM130_MODE_WORK = 0xFF; // Режим работы от ручки ККМ. Нет команд от системы БЛОК
    public static final int KM130_NECESSARY_SUPERCHARGING = 0x11; // Для отпуска необходимо кратковременно перевести ручку ККМ в положение «Сверхзарядка»
    public static final int KM130_NECESSARY_TRAVEL = 0x13; // Необходимо перевести ручку в положение «Поездное»
    public static final int KM130_MODE_BLOK = 0x15; // Режим работы в составе системы БЛОК
    public static final int KM130_MODE_KKM = 0x51; // (0x51-0x55) Режим работы от ручки ККМ.. Торможение.
    //public static final int KM130_CONTROL_BLOK ; // Управление от системы БЛОК. Отпуск запрещен.

    // положение ккм
    public static final int KM130_KKM1 = 0x01; // Положение 1 ККМ
    public static final int KM130_KKM2 = 0x02; // Положение 2 ККМ
    public static final int KM130_KKM3 = 0x04; // Положение 3 ККМ
    public static final int KM130_KKM4 = 0x08; // Положение 4 ККМ
    public static final int KM130_KKM5A = 0x10; // Положение 5А ККМ
    public static final int KM130_KKM5 = 0x20; // Положение 5 ККМ
    public static final int KM130_KKM6 = 0x40; // Положение 6 ККМ

    // Виртуальное положение ручки КМ при исполнении команд управления
    public static final int KM130_VKM1 = 0x01; // Положение 1 VКМ
    public static final int KM130_VKM2 = 0x02; // Положение 2 VКМ
    public static final int KM130_VKM3 = 0x03; // Положение 3 VКМ
    public static final int KM130_VKM4 = 0x04; // Положение 4 VКМ
    public static final int KM130_VKM5A = 0x05; // Положение 5А VКМ
    public static final int KM130_VKM5 = 0x06; // Положение 5 VКМ
    public static final int KM130_VKM6 = 0x07; // Положение 6 VКМ

    public TaskKM130(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        switch (index) {
            case 0:
                getMapPaints(0).put(KM130_MODE_WORK, new Color(0xA2212E));
                getMapPaints(0).put(KM130_NECESSARY_SUPERCHARGING, new Color(0xC3A14B));
                getMapPaints(0).put(KM130_NECESSARY_TRAVEL, new Color(0x0F1EC7));
                getMapPaints(0).put(KM130_MODE_BLOK, new Color(0x420FC4));
                getMapPaints(0).put(KM130_MODE_KKM, new Color(0x32AD42));
                break;
            case  2:
                getMapPaints(2).put(KM130_KKM1, new Color(0xAB68AB));
                getMapPaints(2).put(KM130_KKM2, new Color(0xBCBC75));
                getMapPaints(2).put(KM130_KKM3, new Color(0xB08A69));
                getMapPaints(2).put(KM130_KKM4, new Color(0x78AE82));
                getMapPaints(2).put(KM130_KKM5A, new Color(0xB4656D));
                getMapPaints(2).put(KM130_KKM5, new Color(0x40AEC2));
                getMapPaints(2).put(KM130_KKM6, new Color(0x3839BF));
                break;
            case  1:
                getMapPaints(1).put(KM130_VKM1, new Color(0xAB68AB));
                getMapPaints(1).put(KM130_VKM2, new Color(0xBCBC75));
                getMapPaints(1).put(KM130_VKM3, new Color(0xB08A69));
                getMapPaints(1).put(KM130_VKM4, new Color(0x78AE82));
                getMapPaints(1).put(KM130_VKM5A, new Color(0xB4656D));
                getMapPaints(1).put(KM130_VKM5, new Color(0x40AEC2));
                getMapPaints(1).put(KM130_VKM6, new Color(0x3839BF));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int loc_type) {
        switch (key) {
            case 0:
                return getDescriptStatusBrake(val);
            case 2:
                return getDescriptPosKKm(val);
            case 1:
                return getDescriptVirPosKKm(val);
            default: return "";
        }
    }

    private String getDescriptStatusBrake(int val) {
        String result = "";
        switch (val) {
            case KM130_MODE_WORK:
                result = " работа от ККМ";
                break;
            case KM130_NECESSARY_SUPERCHARGING:
                result = " в «Сверхзарядка»";
                break;
            case KM130_NECESSARY_TRAVEL:
                result = " в «Поездное»";
                break;
            case KM130_MODE_BLOK:
                result = " режим БЛОК";
                break;
            case KM130_MODE_KKM:
                result = " торможение";
                break;
            default: result = " отпуск запрещен";
        }
        return " статус :" + result;
    }

    private String getDescriptPosKKm(int val) {
        String result = "";
        switch (val) {
            case KM130_KKM1:
                result = " I";
                break;
            case KM130_KKM2:
                result = " II";
                break;
            case KM130_KKM3:
                result = " III";
                break;
            case KM130_KKM4:
                result = " IV";
                break;
            case KM130_KKM5A:
                result = " V(A)";
                break;
            case KM130_KKM5:
                result = " V";
                break;
            case KM130_KKM6:
                result = " VI";
                break;
        }
        return " ккм    :" + result;
    }

    private String getDescriptVirPosKKm(int val) {
        String result = "";
        switch (val) {
            case KM130_VKM1:
                result = " I";
                break;
            case KM130_VKM2:
                result = " II";
                break;
            case KM130_VKM3:
                result = " III";
                break;
            case KM130_VKM4:
                result = " IV";
                break;
            case KM130_VKM5A:
                result = " V(A)";
                break;
            case KM130_VKM5:
                result = " V";
                break;
            case KM130_VKM6:
                result = " VI";
                break;
        }
        return " вирт км:" + result;
    }

}
