package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_21_B {
    private final byte[] values;

    public Block32_21_B(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 | // hi
               (toUnsignedInt(values[28]) & 0x60) >>> 5;  // lo
    }

    /**
     * @return совпад. адр.
     */
    public int getCompareAddress() {
        return (toUnsignedInt(values[0]) >>> 4) & 0x01;
    }

    /**
     * @return рабочий режим
     */
    public int getWorkMode() {
        return (toUnsignedInt(values[0]) >>> 3) & 0x01;
    }

    /**
     * @return это ведущий
     */
    public int getIsMain() {
        return (toUnsignedInt(values[0]) >>> 2) & 0x01;
    }

    /**
     * @return Автоведение на втором лок
     */
    public int getModeAutoSlave() {
        return (toUnsignedInt(values[0]) >>> 1) & 0x01;
    }

    /**
     * @return Команда 1 ведущего (0x90 - пусто)
     */
    public int getCommandMain1() {
        return toUnsignedInt(values[1]);
    }

    /**
     * @return Параметр 1 команды 1 ведущего
     */
    public int getFirstParamMain1() {
        return toUnsignedInt(values[2]);
    }

    /**
     * @return Параметр 2 команды 1 ведущего
     */
    public int getSecondParamMain1() {
        return toUnsignedInt(values[3]);
    }

    /**
     * @return Параметр 3 команды 1 ведущего
     */
    public int getThirdParamMain1() {
        return toUnsignedInt(values[4]);
    }

    /**
     * @return Команда 2 ведущего (0x90 - пусто)
     */
    public int getCommandMain2() {
        return toUnsignedInt(values[5]);
    }

    /**
     * @return Параметр 1 команды 2 ведущего
     */
    public int getFirstParamMain2() {
        return toUnsignedInt(values[6]);
    }

    /**
     * @return Параметр 2 команды 2 ведущего
     */
    public int getSecondParamMain2() {
        return toUnsignedInt(values[7]);
    }

    /**
     * @return Параметр 3 команды 2 ведущего
     */
    public int getThirdParamMain2() {
        return toUnsignedInt(values[8]);
    }

    /**
     * @return Команда ведомого (0x90 - пусто)
     */
    public int getCommandSlave() {
        return toUnsignedInt(values[9]);
    }

    /**
     * @return Параметр 1 команды ведомого
     */
    public int getFirstParamSlave() {
        return toUnsignedInt(values[10]);
    }

    /**
     * @return Параметр 2 команды ведомого
     */
    public int getSecondParamSlave() {
        return toUnsignedInt(values[11]);
    }

    /**
     * @return Параметр 3 команды ведомого
     */
    public int getThirdParamSlave() {
        return toUnsignedInt(values[12]);
    }

    /**
     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
     * @return Тип локомотива по АСОУП
     */
    public int getTypeASOUP() {
        return (toUnsignedInt(values[3]) & 0x03) << 11 |     // hi
                toUnsignedInt(values[2]) << 3 |
                toUnsignedInt(values[1]) >>> 5;              // lo
    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Тип локомотива по АСОУП
//     */
//    public int getTypeASOUP2() {
//        return (toUnsignedInt(values[7]) & 0x03) << 11 |     // hi
//                toUnsignedInt(values[6]) << 3 |
//                toUnsignedInt(values[5]) >>> 5;              // lo
//    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Тип локомотива по АСОУП
//     */
//    public int getTypeASOUP3() {
//        return (toUnsignedInt(values[11]) & 0x03) << 11 |     // hi
//                toUnsignedInt(values[10]) << 3 |
//                toUnsignedInt(values[9]) >>> 5;              // lo
//    }

    /**
     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
     * @return Код типа локомотива (по БР)
     */
    public int getTypeLoc() {
        return toUnsignedInt(values[1]) & 0x1F;
    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Код типа локомотива (по БР)
//     */
//    public int getTypeLoc2() {
//        return toUnsignedInt(values[5]) & 0x1F;
//    }
//
//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Код типа локомотива (по БР)
//     */
//    public int getTypeLoc3() {
//        return toUnsignedInt(values[9]) & 0x1F;
//    }

    /**
     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
     * @return Номер локомотива
     */
    public int getNumLoc() {
        return  (toUnsignedInt(values[4]) & 0x0F) << 6 | // hi
                toUnsignedInt(values[3]) >>> 2;          // lo
    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Номер локомотива
//     */
//    public int getNumLoc2() {
//        return  (toUnsignedInt(values[8]) & 0x0F) << 6 | // hi
//                toUnsignedInt(values[7]) >>> 2;          // lo
//    }
//
//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Номер локомотива
//     */
//    public int getNumLoc3() {
//        return  (toUnsignedInt(values[12]) & 0x0F) << 6 | // hi
//                toUnsignedInt(values[11]) >>> 2;          // lo
//    }

    /**
     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
     * @return Номер секции
     */
    public int getNumSection1() {
        return (toUnsignedInt(values[4]) >>> 4) & 0x07;
    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Номер секции
//     */
//    public int getNumSection2() {
//        return (toUnsignedInt(values[8]) >>> 4) & 0x07;
//    }
//
//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Номер секции
//     */
//    public int getNumSection3() {
//        return (toUnsignedInt(values[12]) >>> 4) & 0x07;
//    }

    /**
     * @return Переданный сч. команды 1
     */
    public int getCntTransmit1() {
        return toUnsignedInt(values[13]) & 0x07;
    }

    /**
     * @return Переданный сч. команды 2
     */
    public int getCntTransmit2() {
        return (toUnsignedInt(values[14]) & 0x01) << 2 | // hi
               toUnsignedInt(values[13]) >>> 6;          // lo
    }

    /**
     * @return Подтвержденный сч. команды 1
     */
    public int getCntConfirm1() {
        return (toUnsignedInt(values[13]) >>> 3) & 0x07;
    }

    /**
     * @return Принятый сч. команды 1
     */
    public int getCntAccept1() {
        return (toUnsignedInt(values[14]) >>> 4) & 0x07;
    }

    /**
     * @return Подтверждённый сч. команды 2 / принятый сч. команды 2
     */
    public int getCntConfirm2() {
        return (toUnsignedInt(values[14]) >>> 1) & 0x07;
    }

    /**
     * @return 1 - команда 1 принята по доп. каналу
     */
    public int getAcceptedByAdd1() {
        return (toUnsignedInt(values[14]) >>> 7) & 0x01;
    }

    /**
     * @return Сост. эл.торм второго лок.
     */
    public int getStateEBrakeSlave() {
        return toUnsignedInt(values[15]) >>> 6;
    }

    /**
     * @return Сост. ПТ второго локомотива
     */
    public int getStatePTSlave() {
        return (toUnsignedInt(values[15]) >>> 3) & 0x07;
    }

    /**
     * @return Не все команды ведомого записаны
     */
    public int getWrongCommandSlave() {
        return (toUnsignedInt(values[15]) >>> 2) & 0x01;
    }

    /**
     * @return Не все команды ведущего записаны
     */
    public int getWrongCommandMain() {
        return (toUnsignedInt(values[15]) >>> 1) & 0x01;
    }

    /**
     * @return 1 - команда 2 принята по доп. каналу
     */
    public int getAcceptedByAdd2() {
        return toUnsignedInt(values[15]) & 0x01;
    }

    /**
     * @return Число принятых команд
     */
    public int getAmountAcceptCommand() {
        return toUnsignedInt(values[16]) >>> 4;
    }

    /**
     * @return Число переданных команд
     */
    public int getAmountTransmitCommand() {
        return toUnsignedInt(values[16]) & 0x07;
    }

    /**
     * @return Сила тяги второго локомотива
     */
    public int getPowerSlave() {
        return (toUnsignedInt(values[18]) & 0x07) << 8 | // hi
                toUnsignedInt(values[17]);               // lo
    }

    /**
     * @return Соответствие маршрутов
     */
    public int getCompareRoute() {
        return toUnsignedInt(values[18]) >>> 7;
    }

    /**
     * @return Блоковка включения автоведения с ведомого
     */
    public int getBlockAutoSlave() {
        return (toUnsignedInt(values[18]) >>> 6) & 0x01;
    }

    /**
     * @return Состояние передачи базы по радио
     */
    public int getStateTransmitRadio() {
        return (toUnsignedInt(values[18]) >>> 4) & 0x03;
    }

    /**
     * @return Знак силы тяги второго локомотива
     */
    public int getFlagPowerSlave() {
        return (toUnsignedInt(values[18]) >>> 3) & 0x01;
    }

    /**
     * @return Разница скоростей локомотивов в 0.2 км/ч на 80 км/ч, знаковая величина
     */
    public int getDifferenceSpeed() {
        return values[19];
    }

    /**
     * @return Счётчик переданного  сообщения по доп. каналу
     */
    public int getCntTransmitMessAdditional() {
        return (toUnsignedInt(values[28]) & 0x04) << 2 |
                toUnsignedInt(values[20]) >>> 4;
    }

    /**
     * @return Счётчик переданного  сообщения по осн. каналу
     */
    public int getCntTransmitMessMain() {
        return (toUnsignedInt(values[28]) & 0x02) << 3 |
                toUnsignedInt(values[20]) & 0x0F;
    }

    /**
     * @return Принято сообщ. по осн. каналу
     */
    public int getAmountAcceptMessMain() {
        return toUnsignedInt(values[21]) >>> 4;
    }

    /**
     * @return Передано сообщ. по осн. каналу
     */
    public int getAmountTransmitMessMain() {
        return toUnsignedInt(values[21]) & 0x0F;
    }

    /**
     * @return Счётчик принятого сообщения основного канала
     */
    public int getCntAcceptMessMain() {
        return toUnsignedInt(values[23]);
    }

    /**
     * @return Счётчик принятого сообщения дополнительного канала
     */
    public int getCntAcceptMessAdditional() {
        return toUnsignedInt(values[24]);
    }

    /**
     * @return Уровень сигнала ВЭБР (в 5*у.е.)
     */
    public int getLevelSignalVEBR() {
        return toUnsignedInt(values[25]);
    }

    /**
     * @return Уровень шума ВЭБР (в 5*у.е.)
     */
    public int getNoiseVEBR() {
        return toUnsignedInt(values[26]);
    }

    /**
     * @return активен приём блока данных
     */
    public int getAcceptDataAct() {
        return (toUnsignedInt(values[28]) >>> 4) & 0x01;
    }

    /**
     * @return активна передача блока данных
     */
    public int getTransmitDataAct() {
        return (toUnsignedInt(values[28]) >>> 3) & 0x01;
    }

    /**
     * @return Ресет модема (осн)
     */
    public int getResetModem() {
        return toUnsignedInt(values[28]) & 0x01;
    }
}
