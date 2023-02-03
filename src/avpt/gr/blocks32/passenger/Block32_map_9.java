package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * информация о станциях
 */
public class Block32_map_9 {

    private final byte[] values;

    public Block32_map_9(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return № перегона
     */
    public int getStationId() {
        return toUnsignedInt(values[2]) << 8  |  // hi
                toUnsignedInt(values[1]);         // lo
    }

    /**
     * @return ЕСР станции
     */
    public long getECP() {
        return (long) toUnsignedInt(values[6]) << 24 | // hi
                (long) toUnsignedInt(values[5]) << 16 |
                (long) toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[3]);         // lo
    }

    /**
     * @return тип станции
     */
    public int getTypeStation() {
        return toUnsignedInt(values[9]);
    }

    /**
     * @return время прибытия по расписанию (сек сначала суток)
     */
    public long getTimeArrivalSchedule() {
        return (long) toUnsignedInt(values[12]) << 16 | // hi
                (long) toUnsignedInt(values[11]) << 8 |
                toUnsignedInt(values[10]);       // lo
    }

    /**
     * @return время прибытия фактическое (сек сначала суток)
     */
    public long getTimeArrivalFact() {
        return (long) toUnsignedInt(values[15]) << 16 | // hi
                (long) toUnsignedInt(values[14]) << 8 |
                toUnsignedInt(values[13]);       // lo
    }

    /**
     * @return время отправления по распиманию (сек сначала суток)
     */
    public long getTimeDepartureSchedule() {
        return (long) toUnsignedInt(values[18]) << 16 | // hi
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[16]);       // lo
    }

    /**
     * @return  время отправления фактическое (сек сначала суток)
     */
    public long getTimeDepartureFact() {
        return (long) toUnsignedInt(values[21]) << 16 | // hi
                (long) toUnsignedInt(values[20]) << 8 |
                toUnsignedInt(values[19]);       // lo
    }

    /**
     * @return время сдвига расписания (секунд)
     */
    public long getTimeShiftSchedule() {
        return -1;
    }

    /**
     * @return длина перегона м.
     */
    public long getLenHaul() {
        return (long) toUnsignedInt(values[24]) << 16 | // hi
                (long) toUnsignedInt(values[23]) << 8 |
                toUnsignedInt(values[22]);       // lo
    }

    /**
     * @return абсолютная координата м.
     */
    public long getCoordinate() {
        return (long) toUnsignedInt(values[27]) << 16 | // hi
                (long) toUnsignedInt(values[26]) << 8 |
                toUnsignedInt(values[25]);       // lo
    }

}
