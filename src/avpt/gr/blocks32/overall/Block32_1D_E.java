package avpt.gr.blocks32.overall;


import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Данные виртуальной сцепки
 */
public class Block32_1D_E {

    private byte[] values;

    public Block32_1D_E(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return Переданная команда(0x90 - пусто)
     */
    public int getTransmitCommand() {
        return toUnsignedInt(values[1]);
    }

    /**
     * @return Параметр 1 переданной команды
     */
    public int getFirstParamTransmit() {
        return toUnsignedInt(values[2]);
    }

    /**
     * @return Параметр 2 переданной команды
     */
    public int getSecondParamTransmit() {
        return toUnsignedInt(values[3]);
    }

    /**
     * @return Параметр 3 переданной команды
     */
    public int getThirdParamTransmit() {
        return toUnsignedInt(values[4]);
    }

    /**
     * @return Принятая команда(0x90 - пусто)
     */
    public int getAcceptCommand() {
        return toUnsignedInt(values[5]);
    }

    /**
     * @return Параметр 1 принятой команды
     */
    public int getFirstParamAccept() {
        return toUnsignedInt(values[6]);
    }

    /**
     * @return Параметр 2 принятой команды
     */
    public int getSecondParamAccept() {
        return toUnsignedInt(values[7]);
    }

    /**
     * @return Параметр 3 принятой команды
     */
    public int getThirdParamAccept() {
        return toUnsignedInt(values[8]);
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
//        return (toUnsignedInt(values[7]) & 0x03) << 11 | // hi
//                toUnsignedInt(values[6]) << 3 |          //
//                toUnsignedInt(values[5]) >>> 5;          // lo
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
//        return toUnsignedInt(values[5]) & 1x0F;
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

    /**
     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
     * @return Номер секции
     */
    public int getNumSection() {
        return (toUnsignedInt(values[4]) >>> 4) & 0x07;
    }

//    /**
//     * При совпадении сетевых адресов на месте команд записываются типы и номера локомотивов в сети
//     * @return Номер секции
//     */
//    public int getNumSection2() {
//        return (toUnsignedInt(values[8]) >>> 4) & 0x07;
//    }

    /**
     * @return Линейная координата второго
     */
    public long getCoordinateSlave() {
        return ((toUnsignedInt(values[15]) & 0x01) << 16 | // hi
                toUnsignedInt(values[10]) << 8 |
                toUnsignedInt(values[9])) * 10;                 // lo
    }

    /**
     * @return Скорость второго (в 0.1 км/ч)
     */
    public double getSpeedSlave() {
        return ((toUnsignedInt(values[14]) & 0x70) << 4 | // hi
                toUnsignedInt(values[11])) * 0.1;               // lo
    }

    /**
     * @return Длина второго поезда (в 10м)
     */
    public int getLenSlave() {
        return toUnsignedInt(values[12]);
    }

    /**
     * @return Подтверждённый сч. команды
     */
    public int getCntConfirm() {
        return toUnsignedInt(values[13]) >>> 4;
    }

    /**
     * @return Переданный сч. команды сч. команды
     */
    public int getCntTransmit() {
        return toUnsignedInt(values[13]) & 0x0F;
    }

    /**
     * @return Принятый сч. команды сч. команды
     */
    public int getCntAccept() {
        return toUnsignedInt(values[14]) & 0x0F;
    }

    /**
     * @return 1 - команда 1 принята по доп. каналу
     */
    public int getAcceptedByAdd() {
        return toUnsignedInt(values[14]) >>> 7;
    }

    /**
     * 0 – советчик
     * 1 – кнопочный контроллер
     * 2 – автоведение
     * @return режим автоведения второго
     */
    public int getModeAutoSlave() {
        return toUnsignedInt(values[15]) >>> 6;
    }

    /**
     * 0 – неизвестно
     * 1 – отпуск
     * 2 – поездное
     * 3 – перекрыша без питания
     * 4 – перекрыша с питанием
     * 5 – торможение
     * 6 – замедленное торможение
     * 7 – экстренное торможение
     * @return Сост. ПТ второго локомотива
     */
    public int getStatePTSlave() {
        return (toUnsignedInt(values[15]) & 0x38) >>> 3;
    }

    /**
     * @return Не все принятые команды записаны
     */
    public int getWrongAccept() {
        return (toUnsignedInt(values[15]) >>> 2) & 0x01;
    }

    /**
     * @return Не все переданные команды записаны
     */
    public int getWrongTransmit() {
        return (toUnsignedInt(values[15]) >>> 1) & 0x01;
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
        return toUnsignedInt(values[16]) & 0x0F;
    }

    /**
     * @return Сила тяги второго локомотива (знаковая величина, в 10кН)
     */
    public int getPowerSlave() {
        return toUnsignedInt(values[17]);
    }

    /**
     * @return Соответствие маршрутов
     */
    public int getConformityRoute() {
        return toUnsignedInt(values[18]) >>> 7;
    }

    /**
     * @return Состояние передачи базы по радио
     */
    public int getStateTransmitRadio() {
        return (toUnsignedInt(values[18]) >>> 4) & 0x03;
    }

    /**
     * @return АЛСН второго
     */
    public int getAlsnSecond() {
        return toUnsignedInt(values[18]) & 0x07;
    }

    /**
     * @return ступень ПТ (знаковая величина, в 0,1 бар)
     */
    public int getStepPT() {
        return toUnsignedInt(values[19]) & 0x3F;
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
     * @return Принято сообщ. по доп. каналу
     */
    public int getAmountAcceptMessAdditional() {
        return toUnsignedInt(values[22]) >>> 4;
    }

    /**
     * @return Передано сообщ. по доп. каналу
     */
    public int getAmountTransmitMessAdditional() {
        return toUnsignedInt(values[22]) & 0x0F;
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
     * Положение второго состава – где находится поезд к которому относятся записанные данные («xxx второго локомотива/поезда»)
     * 0 – следующий за нашим
     * 1 – впередиидущий
     * @return положение второго сост.
     */
    public int getPlaceSlave() {
        return toUnsignedInt(values[28]) >>> 7;
    }

    /**
     * @return совпад. адр.
     */
    public int getCompareAddress() {
        return (toUnsignedInt(values[28]) >>> 6) & 0x01;
    }

    /**
     * @return рабочий режим
     */
    public int getWorkMode() {
        return (toUnsignedInt(values[28]) >>> 5) & 0x01;
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
