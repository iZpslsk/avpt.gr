package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskUatl extends SeriesTasks {

    // комманды управления
    public static final int UATL_COMMAND_OFF = 0;   // отключить
    public static final int UATL_COMMAND_STEP1 = 1; // ступень 1
    public static final int UATL_COMMAND_STEP2 = 2; // ступень 2

    // режимы управления
    public static final int UATL_MODE_OFF = 0;   // отключено
    public static final int UATL_MODE_AUTO = 1;  // авто
    public static final int UATL_MODE_MANUAL = 2;// ручной
    public static final int UATL_MODE_AZ = 3;    // АЗ

    // режим работы
    public static final int UATL_WORK_OFF = 0;  //  отключен
    public static final int UATL_WORK_STEP1 = 1;//  ступень 1
    public static final int UATL_WORK_STEP2 = 2;//  ступень 2

    // режимы работы

    public TaskUatl(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        switch (index) {
            case 2:
                getMapPaints(2).put(UATL_COMMAND_OFF, new Color(0x626872));
                getMapPaints(2).put(UATL_COMMAND_STEP1, new Color(0x2B5E2F));
                getMapPaints(2).put(UATL_COMMAND_STEP2, new Color(0x276573));
                break;
            case 0:
                getMapPaints(0).put(UATL_MODE_OFF, new Color(0x626872));
                getMapPaints(0).put(UATL_MODE_AUTO, new Color(0xC3A14B));
                getMapPaints(0).put(UATL_MODE_MANUAL, new Color(0x0F1EC7));
                getMapPaints(0).put(UATL_MODE_AZ, new Color(0x420FC4));
                break;
            case  1:
                getMapPaints(1).put(UATL_WORK_OFF, new Color(0x626872));
                getMapPaints(1).put(UATL_WORK_STEP1, new Color(0x2B5E2F));
                getMapPaints(1).put(UATL_WORK_STEP2, new Color(0x276573));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int loc_type) {
        switch (key) {
            case 2:
                return getDescriptCommad(val);
            case 0:
                return getDescriptMode(val);
            case 1:
                return getDescriptWork(val);
            default: return "";
        }
    }

    private String getDescriptCommad(int val) {
        String result = "";
        switch (val) {
            case UATL_COMMAND_OFF:
                result = "отключить";
                break;
            case UATL_COMMAND_STEP1:
                result = "ступень 1";
                break;
            case UATL_COMMAND_STEP2:
                result = "ступень 2";
                break;
        }
        return " комманда управления: " + result;
    }

    private String getDescriptMode(int val) {
        String result = "";
        switch (val) {
            case UATL_MODE_OFF:
                result = "отключено";
                break;
            case UATL_MODE_AUTO:
                result = "автоматический";
                break;
            case UATL_MODE_MANUAL:
                result = "ручной";
                break;
            case UATL_MODE_AZ:
                result = "режим «А3»";
                break;
        }
        return " режим управления   : " + result;
    }

    private String getDescriptWork(int val) {
        String result = "";
        switch (val) {
            case UATL_WORK_OFF:
                result = "отключен";
                break;
            case UATL_WORK_STEP1:
                result = "ступень 1";
                break;
            case UATL_WORK_STEP2:
                result = "ступень 2";
                break;
        }
        return " режим работы       : " + result;
    }
}
