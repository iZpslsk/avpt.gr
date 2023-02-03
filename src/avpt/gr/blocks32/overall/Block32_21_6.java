package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Расписание от автодиспетчера
 */
public class Block32_21_6 {

    private byte[] values;

    public Block32_21_6(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;    // lo
    }

    /**
     * @return id станции по базе 1
     */
    public int getIdStation1() {
        return (toUnsignedInt(values[2]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[1]);                    // lo
    }

    /**
     * @return время отправления в минутах 1
     */
    public int getTimeDeparture1() {
        return (toUnsignedInt(values[4]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[2]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 1
     */
    public int getTimeArrival1() {
        return (toUnsignedInt(values[4]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[3]);                // lo
    }




    /**
     * @return id станции по базе 2
     */
    public int getIdStation2() {
        return (toUnsignedInt(values[6]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[5]);                    // lo
    }

    /**
     * @return время отправления в минутах 2
     */
    public int getTimeDeparture2() {
        return (toUnsignedInt(values[8]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[6]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 2
     */
    public int getTimeArrival2() {
        return (toUnsignedInt(values[8]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[7]);                // lo
    }

    /**
     * @return id станции по базе 3
     */
    public int getIdStation3() {
        return (toUnsignedInt(values[10]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[9]);                    // lo
    }

    /**
     * @return время отправления в минутах 3
     */
    public int getTimeDeparture3() {
        return (toUnsignedInt(values[12]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[10]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 3
     */
    public int getTimeArrival3() {
        return (toUnsignedInt(values[12]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[11]);                // lo
    }

    /**
     * @return id станции по базе 4
     */
    public int getIdStation4() {
        return (toUnsignedInt(values[14]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[13]);                    // lo
    }

    /**
     * @return время отправления в минутах 4
     */
    public int getTimeDeparture4() {
        return (toUnsignedInt(values[16]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[14]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 4
     */
    public int getTimeArrival4() {
        return (toUnsignedInt(values[16]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[15]);                // lo
    }

    /**
     * @return id станции по базе 5
     */
    public int getIdStation5() {
        return (toUnsignedInt(values[18]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[17]);                    // lo
    }

    /**
     * @return время отправления в минутах 5
     */
    public int getTimeDeparture5() {
        return (toUnsignedInt(values[20]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[18]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 5
     */
    public int getTimeArrival5() {
        return (toUnsignedInt(values[20]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[19]);                // lo
    }

    /**
     * @return id станции по базе 6
     */
    public int getIdStation6() {
        return (toUnsignedInt(values[22]) & 0x03) << 8 |      // hi
                toUnsignedInt(values[21]);                    // lo
    }

    /**
     * @return время отправления в минутах 6
     */
    public int getTimeDeparture6() {
        return (toUnsignedInt(values[24]) & 0xF8) << 5 |      // hi
                toUnsignedInt(values[22]) >>> 2;              // lo
    }

    /**
     * @return время прибытия в минутах 6
     */
    public int getTimeArrival6() {
        return (toUnsignedInt(values[24]) & 0x07) << 8 |  // hi
                toUnsignedInt(values[23]);                // lo
    }

    /**
     * @return id расписания
     */
    public int getIdSchedule() {
        return toUnsignedInt(values[25]);
    }

    /**
     * @return всего станций
     */
    public int getAmountStations() {
        return toUnsignedInt(values[26]);
    }

    /**
     * @return номер фрейма
     */
    public int getNumFrame() {
        return toUnsignedInt(values[27]) & 0x1F;
    }

    /**
     * @return всего фреймов
     */
    public int getAmountFrames() {
        return toUnsignedInt(values[28]) & 0x1F;
    }

}
