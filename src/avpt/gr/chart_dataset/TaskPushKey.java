package avpt.gr.chart_dataset;

import java.awt.*;
import java.util.ArrayList;

public class TaskPushKey extends SeriesTasks  {

    private static final int KEY_UNKNOWN = -1;        //
    private static final int KEY_SEC_10 = 10;        //     1.0 сек
    private static final int KEY_SEC_08 = 8;         //     0.8 сек
    private static final int KEY_SEC_06 = 6;         //     0.6 сек
    private static final int KEY_SEC_04 = 4;         //     0.4 сек
    private static final int KEY_SEC_02 = 2;         //     0.2 сек

    public TaskPushKey(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        super(arrTasks, isColorByIndex);
    }

    @Override
    void setPaint(int index) {
        getMapPaints().put(KEY_UNKNOWN, Color.BLACK);
        getMapPaints(index).put(KEY_SEC_10,  new Color(0xFD579B));
        getMapPaints(index).put(KEY_SEC_08, new Color(0xFC883D));
        getMapPaints(index).put(KEY_SEC_06, new Color(0xFAC446));
        getMapPaints(index).put(KEY_SEC_04, new Color(0xF2FD47));
        getMapPaints(index).put(KEY_SEC_02, new Color(0x48FD47));
    }

    /**
     * @param key - индекс для списка ArrayList<ArrayList<ListTasks.ItemTask>>
     * @return - название кнопки
     */
    private String getTag(int key) {
        switch (key) {
            case 0: return " 'START'";
            case 1: return " 'F1'";
            case 2: return " 'Enter'";
            case 3: return " 'Esc'";
            case 4: return " 'Del'";
            case 5: return " 'Up'";
            case 6: return " 'Down'";
            case 7: return " 'Left'";
            case 8: return " 'Right'";
            case 9: return " '0'";
            case 10: return " '1'";
            case 11: return " '2'";
            case 12: return " '3'";
            case 13: return " '4'";
            case 14: return " '5'";
            case 15: return " '6'";
            case 16: return " '7'";
            case 17: return " '8'";
            case 18: return " '9'";
            default: return "";
        }
    }

    @Override
    public String getDescript(int val, int key, int type_loc) {
        String result = "";
        switch (val) {
            case KEY_SEC_02: result = " : 0.2 сек. ";
                break;
            case KEY_SEC_04: result = " : 0.4 сек. ";
                break;
            case KEY_SEC_06: result = " : 0.6 сек. ";
                break;
            case KEY_SEC_08: result = " : 0.8 сек. ";
                break;
            case KEY_SEC_10: result = " : 1.0 сек. ";
                break;
        }
        return getTag(key) + result;
    }

    /**
     * @param index индекс линии
     * @return цвет по индексу линии
     */
    public static Color getColorPushKey(int index) {
        switch (index) {
            case 0: return new Color(0xFF434F01, true);
            case 1: return new Color(0xFF6E5001, true);
            case 2: return new Color(0xFF6E3301, true);
            case 3: return new Color(0xFF640E00, true);
            case 4: return new Color(0xFF590147, true);
            case 5: return new Color(0xFF58006E, true);
            case 6: return new Color(0xFF4E078A, true);
            case 7: return new Color(0xFF010973, true);
            case 8: return new Color(0xFF014D75, true);
            case 9: return new Color(0xFF015446, true);
            case 10: return new Color(0xFF016723, true);
            case 11: return new Color(0xFF4C5B01, true);
            case 12: return new Color(0xFF653400, true);
            case 13: return new Color(0xFF260F4B, true);
            case 14: return new Color(0xFF053546, true);
            case 15: return new Color(0xFF034617, true);
            case 16: return new Color(0xFF334204, true);
            case 17: return new Color(0xFF442111, true);
            case 18: return new Color(0xFF370C56, true);
            default: return Color.GRAY;
        }
    }
}
