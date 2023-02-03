package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskAutoDrive extends SeriesTasks {

    // режимы автоведения
    public final static int NOT_SAVP = 0;         //  нет
    public final static int MANUAL = 1;           //  режим подсказки(ручной режим) - т.е. флаг автоведения = 0
    public final static int AUTO_SCHEDULE = 2;    //  автоведение по рассписанию (флаг автоведения = 1, флаг алгоритма ведения = 0)
    public final static int AUTO_AVG_SPEED = 3;   //  автоведение по средней скорости (флаг автоведения = 1, флаг алгоритма ведения = 1)
    public final static int CHAIN_OFF = 4;        //  откл цепей
    public final static int BAN_TRACT = 5;        // запрет тяги (флаг автоведения = 1, флаг запрет тяги = 1, флаг запрет торм = 0)
    public final static int BAN_BRAKE_TRACT = 6;  // запрет торм и тяги одновременно (флаг автоведения = 1, флаг запрет торм = 1, флаг запрет тяги = 1)
    public final static int EMULATION = 7;        //  режим эмуляции (флаг автоведения = 1, флаг эмуляции = 1)
    public final static int BAN_AUTO = 8;         //  запрет автоведения (флаг автоведения = 1, флаг запрет автоведения = 1)

    public TaskAutoDrive(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(NOT_SAVP, Color.GRAY);
        getMapPaints().put(MANUAL, new Color(0x0F1EC7));
        getMapPaints().put(AUTO_SCHEDULE, new Color(0x136417));
        getMapPaints().put(AUTO_AVG_SPEED, new Color(0xC3A14B));
        getMapPaints().put(CHAIN_OFF, new Color(0xA51217));
        getMapPaints().put(BAN_TRACT, new Color(0xC7549F));
        getMapPaints().put(BAN_BRAKE_TRACT, new Color(0x136417));
        getMapPaints().put(EMULATION, new Color(0xB594C3));
        getMapPaints().put(BAN_AUTO, new Color(0xD65106));
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = " нет";
        switch (val) {
            case MANUAL:
                result = " Режим УСАВП-Г - режим подсказки";
                break;
            case AUTO_SCHEDULE:
                result = " Ведение по расписанию";
                break;
            case AUTO_AVG_SPEED:
                result = " Ведение по средней скорости";
                break;
            case CHAIN_OFF:
                result = " Откл цепей";
                break;
            case BAN_AUTO:
                result = " Запрет автоведения";
                break;
            case BAN_TRACT:
                result = " Запрет тяги";
                break;
            case BAN_BRAKE_TRACT:
                result = " Запрет тяги и торм";
                break;
            case EMULATION:
                result = " Эмуляция";
                break;
        }
        return result;
    }
}
