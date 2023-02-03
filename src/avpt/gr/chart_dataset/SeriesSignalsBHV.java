package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

public class SeriesSignalsBHV extends SeriesSignals {

    public SeriesSignalsBHV(ListSignals listSignals) {
        addTaskSeries(KEY_BRAKE_TAIL, listSignals.getListBHV_BrakeTail());
        addTaskSeries(KEY_IS_SLAVE_CHAN, listSignals.getListBHV_IsSlaveChan());
        addTaskSeries(KEY_LINK_SLAVE_CHAN, listSignals.getListBHV_LinkSlaveChan());
        addTaskSeries(KEY_LINK_MAIN_CHAN, listSignals.getListBHV_LinkMainChan());
        addTaskSeries(KEY_IS_COMMAND, listSignals.getListBHV_IsCommand());
        addTaskSeries(KEY_FAN_EMERGENCY_BRAKE, listSignals.getListBHV_FanEmergencyBrake());
        addTaskSeries(KEY_FAN_BRAKE, listSignals.getListBHV_FanBrake());
        addTaskSeries(KEY_SENSOR_PRESS_RK, listSignals.getListBHV_SensorPressRK());
        addTaskSeries(KEY_SENSOR_PRESS_TM, listSignals.getListBHV_SensorPressTM());
        addTaskSeries(KEY_UPDATE_PARAM, listSignals.getListBHV_UpdateParam());
        addTaskSeries(KEY_DEBUG_OTP, listSignals.getListBHV_DebugOtp());
        addTaskSeries(KEY_BHV_READY, listSignals.getListBHV_Ready());
        addTaskSeries(KEY_BHV_OTHER, listSignals.getListBHV_Other());
        addTaskSeries(KEY_INTERVENTION_RADIO, listSignals.getListBHV_InterventionRadio());
        addTaskSeries(KEY_CRASH_AFU_MOST, listSignals.getListBHV_CrashAfuMost());
        addTaskSeries(KEY_IS_BHV, listSignals.getListBHV_IsBhv());
        addTaskSeries(KEY_ALLOW_ANSWER, listSignals.getListBHV_AllowAnswer());
    }

    @Override
    public Color getColorSeries(int key) {
        Color color =Color.GRAY;
        switch (key) {
            case KEY_BRAKE_TAIL: color = new Color(0xFFA07A);
                break;
            case KEY_IS_SLAVE_CHAN: color = new Color(0x32CD32);
                break;
            case KEY_LINK_SLAVE_CHAN: color = new Color(0x800000);
                break;
            case KEY_LINK_MAIN_CHAN: color = new Color(0xBA55D3);
                break;
            case KEY_IS_COMMAND : color = new Color(0x0000FF);
                break;
            case KEY_FAN_EMERGENCY_BRAKE : color = new Color(0x8A2BE2);
                break;
            case KEY_FAN_BRAKE : color = new Color(0xA52A2A);
                break;
            case KEY_SENSOR_PRESS_RK: color = new Color(0xE2E2E2);
                break;
            case KEY_SENSOR_PRESS_TM : color = new Color(0x5F9EA0);
                break;
            case KEY_UPDATE_PARAM: color = new Color(0xD2691E);
                break;
            case KEY_DEBUG_OTP: color = new Color(0x6495ED);
                break;
            case KEY_BHV_READY: color = new Color(0x8B008B);
                break;
            case KEY_BHV_OTHER : color = new Color(0xDC143C);
                break;
            case KEY_INTERVENTION_RADIO : color = new Color(0x008B8B);
                break;
            case KEY_CRASH_AFU_MOST : color = new Color(0xB8860B);
                break;
            case KEY_IS_BHV: color = new Color(0x009B00);
                break;
            case KEY_ALLOW_ANSWER: color = new Color(0x009B7F);
                break;
        }
        return color;
    }
}
