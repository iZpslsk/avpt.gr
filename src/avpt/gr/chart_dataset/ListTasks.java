package avpt.gr.chart_dataset;

import java.util.ArrayList;

public class ListTasks {

    // АЛСН
    private final ArrayList<ItemTask> listALSN_USAVP = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listALSN_CLUB = new ArrayList<ItemTask>();      //
    private final ArrayList<ItemTask> listALSN_vl10_1 = new ArrayList<ItemTask>();      // (ВЛ10)
    private final ArrayList<ItemTask> listALSN_vl10_2 = new ArrayList<ItemTask>();      // (ВЛ10)
    private final ArrayList<ItemTask> listALSN_vl80_1 = new ArrayList<ItemTask>();      // (ВЛ80)
    private final ArrayList<ItemTask> listALSN_vl80_2 = new ArrayList<ItemTask>();      // (ВЛ80)
    // пневматика
    private final ArrayList<ItemTask> listPneumatic1 = new ArrayList<ItemTask>();     // положение первого
    private final ArrayList<ItemTask> listPneumatic2 = new ArrayList<ItemTask>();     // положение второго
    private final ArrayList<ItemTask> listPneumaticUsavp = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listPneumaticStatusBlk = new ArrayList<ItemTask>();     // источник задания
    private final ArrayList<ItemTask> listPneumaticSrcTask = new ArrayList<ItemTask>();     //  статус блок

    // уатл
    private final ArrayList<ItemTask> listUatlCommand = new ArrayList<ItemTask>();  // Команды управления УАТЛ
    private final ArrayList<ItemTask> listUatlMode = new ArrayList<ItemTask>();     // Режимы управления УАТЛ
    private final ArrayList<ItemTask> listUatlWork = new ArrayList<ItemTask>();     // Режимы работы УАТЛ

    // ккм
    private final ArrayList<ItemTask> listKKM = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkm = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkm2 = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkm_1 = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkm_2 = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkm_s5 = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listKkbt_s5 = new ArrayList<ItemTask>();     //
    // бхв
    private final ArrayList<ItemTask> listBHV_valve = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listBHV_command = new ArrayList<ItemTask>();     //
    private final ArrayList<ItemTask> listBHV_voltage = new ArrayList<ItemTask>();     //
    // режим всц
    private final ArrayList<ItemTask> listVsc = new ArrayList<ItemTask>();     //
    // режим кабина
    private final ArrayList<ItemTask> listCabin = new ArrayList<ItemTask>();     //
    // автоведение
    private final ArrayList<ItemTask> listAuto = new ArrayList<ItemTask>();     // 0x21_4
    // Положение главной рукоятки контроллера
    private final ArrayList<ItemTask> listMainControl = new ArrayList<ItemTask>();
    // Положение реверсивной рукоятки контроллера
    private final ArrayList<ItemTask> listRevControl = new ArrayList<ItemTask>();
    // Состояние схемы
    private final ArrayList<ItemTask> listSchema = new ArrayList<ItemTask>();

    // клавиши
    private final ArrayList<ItemTask> listKeyStart = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyF1 = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyEnter = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyEsc = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyDel = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyUp = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyDown = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyLeft = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyRight = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyZero = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyOne = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyTwo = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyThree = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyFour = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyFive = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeySix = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeySeven = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyEight = new ArrayList<ItemTask>();
    private final ArrayList<ItemTask> listKeyNine = new ArrayList<ItemTask>();

    /**
     * begin- начало
     * end - конец
     * val - значение
     */
    public static class ItemTask {
        int begin;
        int end;
        int value;
        int loc_type;

        private ItemTask(int begin, int end, int value, int loc_type) {
            this.begin = begin;
            this.end = end;
            this.value = value;
            this.loc_type = loc_type;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }

        public int getValue() {
            return value;
        }

        public int getLoc_type() {
            return loc_type;
        }
    }

//    /**
//     * @param arrDots - набор участков
//     * @param second - тек секунда
//     * @param value -
//     */
//    public void add(ArrayList<ItemTask> arrDots, int second, int value) {
//        if (arrDots.size() == 0) {
//            arrDots.add(new ItemTask(second, second + 1, value));
//        }
//        else {
//            int prev_val = arrDots.get(arrDots.size() - 1).value;
//            if (prev_val != value)
//                arrDots.add(new ItemTask(second, second + 1, value));
//            else
//                arrDots.get(arrDots.size() - 1).end = second + 1;
//        }
//    }

    /**
     * @param arrDots - набор участков
     * @param second - тек секунда
     * @param value -
     */
    public void add(ArrayList<ItemTask> arrDots, int second, int value, int type_loc, int max_second) {
        if (arrDots.size() == 0) {
            arrDots.add(new ItemTask(second, second + 1, value, type_loc));
        }
        else {
            ItemTask item = arrDots.get(arrDots.size() - 1);
            int prev_val = item.value;
            int end = item.end;
            int d = second - end;
            if (max_second > -1 && d > max_second) {
                item = new ItemTask(end, end, -1, type_loc);
                arrDots.add(item);
            }

            if (prev_val != value) {
                if (arrDots.size() > 1)
                    arrDots.get(arrDots.size() - 2).end = arrDots.get(arrDots.size() - 1).begin;
                arrDots.add(new ItemTask(second, second + 1, value, type_loc));
            }
            else
                arrDots.get(arrDots.size() - 1).end = second + 1;
        }
    }

    public ArrayList<ItemTask> getListALSN_USAVP() {
        return listALSN_USAVP;
    }

    public ArrayList<ItemTask> getListALSN_CLUB() {
        return listALSN_CLUB;
    }

    public ArrayList<ItemTask> getListALSN_vl10_1() {
        return listALSN_vl10_1;
    }

    public ArrayList<ItemTask> getListALSN_vl10_2() {
        return listALSN_vl10_2;
    }

    public ArrayList<ItemTask> getListALSN_vl80_1() {
        return listALSN_vl80_1;
    }

    public ArrayList<ItemTask> getListALSN_vl80_2() {
        return listALSN_vl80_2;
    }

    public ArrayList<ItemTask> getListPneumatic1() {
        return listPneumatic1;
    }

    public ArrayList<ItemTask> getListPneumaticUsavp() {
        return listPneumaticUsavp;
    }

    public ArrayList<ItemTask> getListPneumatic2() {
        return listPneumatic2;
    }

    public ArrayList<ItemTask> getListPneumaticStatusBlk() {
        return listPneumaticStatusBlk;
    }

    public ArrayList<ItemTask> getListPneumaticSrcTask() {
        return listPneumaticSrcTask;
    }


    public ArrayList<ItemTask> getListUatlCommand() {
        return listUatlCommand;
    }

    public ArrayList<ItemTask> getListUatlMode() {
        return listUatlMode;
    }

    public ArrayList<ItemTask> getListUatlWork() {
        return listUatlWork;
    }

    public ArrayList<ItemTask> getListKkm() {
        return listKkm;
    }

    public ArrayList<ItemTask> getListKkm2() {
        return listKkm2;
    }

    public ArrayList<ItemTask> getListKKM() {
        return listKKM;
    }

    public ArrayList<ItemTask> getListKkm_1() {
        return listKkm_1;
    }

    public ArrayList<ItemTask> getListKkm_2() {
        return listKkm_2;
    }

    public ArrayList<ItemTask> getListKkm_s5() {
        return listKkm_s5;
    }

    public ArrayList<ItemTask> getListKkbt_s5() {
        return listKkbt_s5;
    }

    public ArrayList<ItemTask> getListBHV_valve() {
        return listBHV_valve;
    }

    public ArrayList<ItemTask> getListBHV_command() {
        return listBHV_command;
    }

    public ArrayList<ItemTask> getListBHV_voltage() {
        return listBHV_voltage;
    }

    public ArrayList<ItemTask> getListVsc() {
        return listVsc;
    }

    public ArrayList<ItemTask> getListCabin() {
        return listCabin;
    }

    public ArrayList<ItemTask> getListMainControl() {
        return listMainControl;
    }

    public ArrayList<ItemTask> getListRevControl() {
        return listRevControl;
    }

    public ArrayList<ItemTask> getListSchema() {
        return listSchema;
    }

    public ArrayList<ItemTask> getListAuto() {
        return listAuto;
    }

    public ArrayList<ItemTask> getListKeyStart() {
        return listKeyStart;
    }

    public ArrayList<ItemTask> getListKeyF1() {
        return listKeyF1;
    }

    public ArrayList<ItemTask> getListKeyEnter() {
        return listKeyEnter;
    }

    public ArrayList<ItemTask> getListKeyEsc() {
        return listKeyEsc;
    }

    public ArrayList<ItemTask> getListKeyDel() {
        return listKeyDel;
    }

    public ArrayList<ItemTask> getListKeyUp() {
        return listKeyUp;
    }

    public ArrayList<ItemTask> getListKeyDown() {
        return listKeyDown;
    }

    public ArrayList<ItemTask> getListKeyLeft() {
        return listKeyLeft;
    }

    public ArrayList<ItemTask> getListKeyRight() {
        return listKeyRight;
    }

    public ArrayList<ItemTask> getListKeyZero() {
        return listKeyZero;
    }

    public ArrayList<ItemTask> getListKeyOne() {
        return listKeyOne;
    }

    public ArrayList<ItemTask> getListKeyTwo() {
        return listKeyTwo;
    }

    public ArrayList<ItemTask> getListKeyThree() {
        return listKeyThree;
    }

    public ArrayList<ItemTask> getListKeyFour() {
        return listKeyFour;
    }

    public ArrayList<ItemTask> getListKeyFive() {
        return listKeyFive;
    }

    public ArrayList<ItemTask> getListKeySix() {
        return listKeySix;
    }

    public ArrayList<ItemTask> getListKeySeven() {
        return listKeySeven;
    }

    public ArrayList<ItemTask> getListKeyEight() {
        return listKeyEight;
    }

    public ArrayList<ItemTask> getListKeyNine() {
        return listKeyNine;
    }
}
