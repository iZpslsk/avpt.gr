package avpt.gr.blocks32.asim;


import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * информация о станциях
 */
public class Block32_C5_0 {

    private final byte[] values;

    public Block32_C5_0(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return id станции
     */
    public int getStationId() {
        return toUnsignedInt(values[0]) << 8  |  // hi
                toUnsignedInt(values[1]);        // lo
    }

    /**
     * @return ЕСР станции
     */
    public long getECP() {
        return (long) toUnsignedInt(values[2]) << 24 | // hi
                (long) toUnsignedInt(values[3]) << 16 |
                (long) toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[5]);               // lo
    }

    /**
     * @return тип станции
     */
    public int getTypeStation() {
        return toUnsignedInt(values[6]);
    }

    /**
     * @return время прибытия по расписанию (сек сначала суток)
     */
    public long getTimeArrivalSchedule() {
        return (long) toUnsignedInt(values[7]) << 16 |  // hi
                (long) toUnsignedInt(values[8]) << 8 |
                toUnsignedInt(values[9]);               // lo
    }

    /**
     * @return время прибытия фактическое (сек сначала суток)
     */
    public long getTimeArrivalFact() {
        return (long) toUnsignedInt(values[10]) << 16 | // hi
                (long) toUnsignedInt(values[11]) << 8 |
                toUnsignedInt(values[12]);              // lo
    }

    /**
     * @return время отправления по распиманию (сек сначала суток)
     */
    public long getTimeDepartureSchedule() {
        return (long) toUnsignedInt(values[13]) << 16 | // hi
                (long) toUnsignedInt(values[14]) << 8 |
                toUnsignedInt(values[15]);              // lo
    }

    /**
     * @return  время отправления фактическое (сек сначала суток)
     */
    public long getTimeDepartureFact() {
        return (long) toUnsignedInt(values[16]) << 16 | // hi
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[18]);              // lo
    }

    /**
     * @return время сдвига расписания (секунд)
     */
    public long getTimeShiftSchedule() {
        return 0;       // lo
    }

    /**
     * @return линейная координата (пробег сначала маршрута м.)
     */
    public long getCoordinate() {
        return (long) toUnsignedInt(values[25]) << 16 | // hi
                (long) toUnsignedInt(values[26]) << 8 |
                toUnsignedInt(values[27]);              // lo
    }


}
