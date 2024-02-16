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

    // Виртуальное положение ручки КМ при исполнении команд управления
    public static final int KM130_KKM1 = 0x01; // Положение 1 ККМ
    public static final int KM130_KKM2 = 0x02; // Положение 2 ККМ
    public static final int KM130_KKM3 = 0x04; // Положение 3 ККМ
    public static final int KM130_KKM4 = 0x08; // Положение 4 ККМ
    public static final int KM130_KKM5A = 0x10; // Положение 5А ККМ
    public static final int KM130_KKM5 = 0x20; // Положение 5 ККМ
    public static final int KM130_KKM6 = 0x40; // Положение 6 ККМ

    public TaskKM130(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        switch (index) {
            case 0:
                getMapPaints(0).put(KM130_MODE_WORK, new Color(0x721920));
                getMapPaints(0).put(KM130_NECESSARY_SUPERCHARGING, new Color(0xC3A14B));
                getMapPaints(0).put(KM130_NECESSARY_TRAVEL, new Color(0x0F1EC7));
                getMapPaints(0).put(KM130_MODE_BLOK, new Color(0x420FC4));
                getMapPaints(0).put(KM130_MODE_KKM, new Color(0x1E6727));
                break;
            case  1:
                getMapPaints(1).put(KM130_KKM1, new Color(0x724572));
                getMapPaints(1).put(KM130_KKM2, new Color(0x5E5E3A));
                getMapPaints(1).put(KM130_KKM3, new Color(0x735944));
                getMapPaints(1).put(KM130_KKM4, new Color(0x466C4F));
                getMapPaints(1).put(KM130_KKM5A, new Color(0x5E3136));
                getMapPaints(1).put(KM130_KKM5, new Color(0x276573));
                getMapPaints(1).put(KM130_KKM6, new Color(0x20216D));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int loc_type) {
        switch (key) {
            case 0:
                return getDescriptStatusBrake(val);
            case 1:
                return getDescriptVirPosKm(val);
            default: return "";
        }
    }

    private String getDescriptStatusBrake(int val) {
        String result = "";
        switch (val) {
            case KM130_MODE_WORK:
                result = " режим работы от ручки ККМ";
                break;
            case KM130_NECESSARY_SUPERCHARGING:
                result = " перевести ручку в положение «Сверхзарядка»";
                break;
            case KM130_NECESSARY_TRAVEL:
                result = " перевести ручку в положение «Поездное»";
                break;
            case KM130_MODE_BLOK:
                result = " режим работы в составе системы БЛОК";
                break;
            case KM130_MODE_KKM:
                result = " режим работы от ручки ККМ.. Торможение.";
                break;
            default: result = " управление от системы БЛОК. Отпуск запрещен.";
        }
        return " статус торм.:" + result;
    }

    private String getDescriptVirPosKm(int val) {
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
        return " положение КМ:" + result;
    }

}
