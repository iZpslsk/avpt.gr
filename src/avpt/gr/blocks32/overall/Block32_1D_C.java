package avpt.gr.blocks32.overall;

import avpt.gr.chart_dataset.ListLines;
import avpt.gr.chart_dataset.ListTasks;

import java.util.ArrayList;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_1D_C {

    private final byte[] values;

    public Block32_1D_C(byte[] values) {
        this.values = values;
    }

    /**
     * @return Длинна данных (до 27 байт)
     */
    public int getLenData() {
        return toUnsignedInt(values[1]) >>> 3;
    }

    /**
     * @return направление (0 – приём, 1 - передача)
     */
    public int getDirect() {
        return (toUnsignedInt(values[1]) >>> 2) & 0x01;
    }

    /**
     * •	0 – модем АВП
     * •	1 – модем МОСТ
     * •	2 – модем ВЭБР
     * @return номер канала
     */
    public int getNumChanel() {
        return toUnsignedInt(values[1]) & 0x03;
    }

    /**
     * АВП (0 - номер канала)
     * @return Адрес устройства = 0x30
     */
    public int getAddressDevice() {
        return toUnsignedInt(values[2]);
    }

    /**
     * АВП (0 - номер канала)
     * 0x01 – Передача по радио
     * 0x02 – Приём по радио
     * 0x03 – Тестирование
     * 0x05 – Установки задержки КДГ
     * @return Код команды
     */
    public int getCodeCommand() {
        return toUnsignedInt(values[3]);
    }

    /**
     * АВП (0 - номер канала)
     * @return длина команды, полное число байт, начиная с адреса и кончая CRC.
     */
    public int getLenCommand() {
        return toUnsignedInt(values[4]);
    }

    /**
     * АВП (0 - номер канала)
     * @return сетевой адрес.
     */
    public int getAddress() {
        return toUnsignedInt(values[6]) << 8 |
               toUnsignedInt(values[5]);
    }

    /**
     * 1 – ведущий
     * 2 – ведомый
     * 3 – ведомый 2
     * 4 – ведомый 3
     * 15 – широковещательный (только адрес назначения)
     * @return адрес источника
     */
    public int getAddressSource() {
        int result = -1;
        // avp
        if (getNumChanel() == 0 && getAddressDevice() == 0x30) {
          result = toUnsignedInt(values[7]) >>> 4;
        }
        // vebr
        if (getNumChanel() == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C) {
            result = toUnsignedInt(values[6]) >>> 4;
        }
        return result;
    }

    /**
     * 1 – ведущий
     * 2 – ведомый
     * 3 – ведомый 2
     * 4 – ведомый 3
     * 15 – широковещательный (только адрес назначения)
     * @return адрес назначения
     */
    public int getAddressTarget() {
        int result = -1;
        // avp
        if (getNumChanel() == 0 && getAddressDevice() == 0x30) {
            result = toUnsignedInt(values[7]) & 0x0F;
        }
        // vebr
        if (getNumChanel() == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C) {
            result = toUnsignedInt(values[5]) & 0x0F;
        }
        return result;
    }

    /**
     * АВП (0 - номер канала)
     * @return 0xFE - протокол 6 иначе протокол 8
     */
    public int getVerProtocol() {
        return toUnsignedInt(values[8]);
    }

    /**
     * 1: торможение
     * 2: перекрыша
     * 3: отпуск
     * 4: поездное
     * @return пневматика второго, если result = -1 - результат игнорировать
     */
    public int getPneumaticSlaveS6(ArrayList<ListTasks.ItemTask> list) {
        //int result = -1;
        int result = list.size() > 0 ? list.get(list.size() - 1).getValue() : 0;
        // направление - прмем
        if (getDirect() == 0) {
            // номер канала - АВП, адрес устройства - 0x30, код комманды - прием по радио
            if (getNumChanel() == 0 && getAddressDevice() == 0x30 && getCodeCommand() == 2) {
                if (getVerProtocol() == 0xFE) { // ver 6
                    result = toUnsignedInt(values[15]) & 0x07;
                }
                else {                        // ver 8
                    result = toUnsignedInt(values[14]) & 0x07;
                }
            }
            // номер канала - ВЭБР
            if (getNumChanel() == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C) {
                result = toUnsignedInt(values[14]) & 0x07;
            }
        }
        return result;
    }

    /**
     * @return Сила тяги второго локомотива
     */
    public int getPowerSlaveS6(ArrayList<ListLines.ItemLine> list) {
        int result = list.size() > 0 ? (int)list.get(list.size() - 1).getValue() : 0;
        boolean less_nul = false; // меньше нуля
        // направление - прмем
        if (getDirect() == 0 && getAddressSource() >= 2 && getAddressSource() < 5) {
            if (getNumChanel() == 0 && getAddressDevice() == 0x30 && getCodeCommand() == 2) {
                if (getVerProtocol() == 0xFE) { // ver 6
                    less_nul = ((toUnsignedInt(values[18]) >>> 3) & 0x01) > 0;
                    result = toUnsignedInt(values[17]) | ((toUnsignedInt(values[18]) & 0x07) << 8);
                }
                else {                          // ver 8
                    less_nul = ((toUnsignedInt(values[17]) >>> 3) & 0x01) > 0;
                    result = toUnsignedInt(values[16]) | ((toUnsignedInt(values[17]) & 0x07) << 8);
                }
            }
            if (getNumChanel() == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C) {
                less_nul = ((toUnsignedInt(values[17]) >>> 3) & 0x01) > 0;
                result = toUnsignedInt(values[16]) | ((toUnsignedInt(values[17]) & 0x07) << 8);
            }
        }
        return less_nul ? result * -1 : result;
    }

    /**
     * @return Сила тяги второго локомотива
     */
    public int getPowerSlave_s6(ArrayList<ListLines.ItemLine> list) {
        boolean less_nul =false;
        int result = list.size() > 0 ? (int)list.get(list.size() - 1).getValue() : 0;
        int nChan = toUnsignedInt(values[1]) & 0x03;
        if (nChan == 0 && toUnsignedInt(values[2]) == 0x30 && (toUnsignedInt(values[7]) >>> 4) == 2 && (toUnsignedInt(values[8]) == 0xFE)) {  // avp
                  less_nul = ((toUnsignedInt(values[18]) >>> 3) & 0x01) > 0;
                  result = toUnsignedInt(values[17]) | ((toUnsignedInt(values[18]) & 0x07) << 8);
        }
        else if (nChan == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C
                && ((toUnsignedInt(values[6]) >>> 4) >= 2) && ((toUnsignedInt(values[6]) >>> 4) <= 4) ) {   // vebr

                less_nul = ((toUnsignedInt(values[17]) >>> 3) & 0x01) > 0;
                result = toUnsignedInt(values[16]) | ((toUnsignedInt(values[17]) & 0x07) << 8);

        }
        return less_nul ? result * -1 : result;
    }

//    /**
//     * 1: торможение
//     * 2: перекрыша
//     * 3: отпуск
//     * 4: поездное
//     * @return пневматика второго
//     */
//    public int getPneumaticSlave_s6(ArrayList<ListTasks.ItemTask> list) {
//        int result = list.size() > 0 ? list.get(list.size() - 1).getValue() : 0;
//        int nChan = toUnsignedInt(values[1]) & 0x03;
////        if (nChan == 0 && toUnsignedInt(values[2]) == 0x30 && (toUnsignedInt(values[7]) >>> 4) == 2) {
////            if (toUnsignedInt(values[8]) == 0xFE)
////                result = toUnsignedInt(values[15]) & 0x07;
///////            else
//// //               result = toUnsignedInt(values[14]) & 0x07;
////        }
////        else if (nChan == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C
////                && ((toUnsignedInt(values[6]) >>> 4) >= 2) && ((toUnsignedInt(values[6]) >>> 4) <= 4) ) {   // vebr {
////            result = toUnsignedInt(values[14]) & 0x07;
////        }
////        else {
////            result = toUnsignedInt(values[14]) & 0x07;
////        }
//
//
//        if (nChan == 0 && toUnsignedInt(values[2]) == 0x30 && (toUnsignedInt(values[7]) >>> 4) == 2 && (toUnsignedInt(values[8]) == 0xFE)) {  // avp
//            result = toUnsignedInt(values[15]) & 0x07;
//        }
//        else if (nChan == 2 && toUnsignedInt(values[2]) == 0x2B && toUnsignedInt(values[3]) == 0x0C
//                && ((toUnsignedInt(values[6]) >>> 4) >= 2) && ((toUnsignedInt(values[6]) >>> 4) <= 4) ) {   // vebr
//            result = toUnsignedInt(values[14]) & 0x07;
//        }
//        return result;
//    }
}
