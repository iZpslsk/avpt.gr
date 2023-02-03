package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskBHV extends SeriesTasks {

    // клапана
    public static final int BHV_VALVE_CLOSE = 0;                // закрыты
    public static final int BHV_VALVE_BRAKING = 1;              // открыт клапан служебного торможения
    public static final int BHV_VALVE_BRAKING_EMERGENCY = 3;    // открыт клапан экстренного торможения

    // комманда торм
    public static final int BHV_COMMAND_CLOSE = 0;              // закрыть клапана
    public static final int BHV_COMMAND_BRAKING = 1;            // служебное торможение до давления
    public static final int BHV_COMMAND_BRAKING_EMERGENCY = 2;  // экстренное торможение

    // напряжение БХВ
    public static final int BHV_VOLTAGE_11 = 0;                 // 11
    public static final int BHV_VOLTAGE_11_5 = 1;               // 11 - 11.5
    public static final int BHV_VOLTAGE_12 = 2;                 // 12
    public static final int BHV_VOLTAGE_MORE_12 = 3;            // > 12

    private int type_loc = -1;

    public TaskBHV(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        switch (index) {
            case 0:// клапана
                getMapPaints(0).put(BHV_VALVE_CLOSE, new Color(0x218196));
                getMapPaints(0).put(BHV_VALVE_BRAKING, new Color(0x918F20));
                getMapPaints(0).put(BHV_VALVE_BRAKING_EMERGENCY, new Color(0x136417));
                break;
            case 1:// комманда торм
                getMapPaints(1).put(BHV_COMMAND_CLOSE, new Color(0x4F812E));
                getMapPaints(1).put(BHV_COMMAND_BRAKING, new Color(0x370C56));
                getMapPaints(1).put(BHV_COMMAND_BRAKING_EMERGENCY, new Color(0xA11729));
                break;
            case 2:// напряжение БХВ
                getMapPaints(2).put(BHV_VOLTAGE_11, new Color(0x91562B));
                getMapPaints(2).put(BHV_VOLTAGE_11_5, new Color(0x9D4C61));
                getMapPaints(2).put(BHV_VOLTAGE_12, new Color(0xA9195A));
                getMapPaints(2).put(BHV_VOLTAGE_MORE_12, new Color(0x8F0505));
                break;
        }
    }

    @Override
    public String getDescript(int val, int key, int loc_type) {
        switch (key) {
            case 0:
                return getDescriptValve(val);
            case 1:
                return getDescriptCommand(val);
            case 2:
                return getDescriptVoltage(val);
            default: return "";
        }
    }

    // клапана
    private String getDescriptValve(int val) {
        String result = "неизвестно";
        switch (val) {
            case BHV_VALVE_CLOSE:
                result = "закрыт";
                break;
            case BHV_VALVE_BRAKING:
                result = "сужебного торможения открыт";
                break;
            case BHV_VALVE_BRAKING_EMERGENCY:
                result = "экстренного торможения открыт";
                break;
        }
        return " клапан: " + result;
    }

    // комманды
    private String getDescriptCommand(int val) {
        String result = "неизвестно";
        switch (val) {
            case BHV_COMMAND_CLOSE:
                result = "закрыть клапана";
                break;
            case BHV_COMMAND_BRAKING:
                result = "служебное торможение";
                break;
            case BHV_COMMAND_BRAKING_EMERGENCY:
                result = "экстренное тормоожение";
                break;
        }
        return " комманда: " + result;
    }

    // напряжение
    private String getDescriptVoltage(int val) {
        String result = "неизвестно";
        switch (val) {
            case BHV_VOLTAGE_11:
                result = "11";
                break;
            case BHV_VOLTAGE_11_5:
                result = "11 - 11.5";
                break;
            case BHV_VOLTAGE_12:
                result = "12";
                break;
            case BHV_VOLTAGE_MORE_12:
                result = "> 12";
                break;
        }
        return " напряжение: " + result;
    }

}
