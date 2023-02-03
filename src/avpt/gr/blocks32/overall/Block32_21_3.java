package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * посылка временных ограничений
 */
public class Block32_21_3 {

    private final byte[] values;

    public Block32_21_3(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;     // lo
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

    /**
     * @return величина ограничения 1
     */
    public int getValue1() {
        return toUnsignedInt(values[2]) & 0x7F;
    }

    /**
     * @return координата начала 1
     */
    public int getBegin1() {
        return ((toUnsignedInt(values[3]) & 0x7F) << 7 |     // hi
               toUnsignedInt(values[4]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return координата конца 1
     */
    public int getEnd1() {
        return ((toUnsignedInt(values[5]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[6]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return величина ограничения 2
     */
    public int getValue2() {
        return toUnsignedInt(values[7]) & 0x7F;
    }

    /**
     * @return координата начала 2
     */
    public int getBegin2() {
        return ((toUnsignedInt(values[8]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[9]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return координата конца 2
     */
    public int getEnd2() {
        return ((toUnsignedInt(values[10]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[11]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return величина ограничения 3
     */
    public int getValue3() {
        return toUnsignedInt(values[12]) & 0x7F;
    }

    /**
     * @return координата начала 3
     */
    public int getBegin3() {
        return ((toUnsignedInt(values[13]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[14]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return координата конца 3
     */
    public int getEnd3() {
        return ((toUnsignedInt(values[15]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[16]) & 0x7F) * 100 ;     // lo
    }

    /**
     * @return величина ограничения 4
     */
    public int getValue4() {
        return toUnsignedInt(values[17]) & 0x7F;
    }

    /**
     * @return координата начала 4
     */
    public int getBegin4() {
        return ((toUnsignedInt(values[18]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[19]) & 0x7F) * 100 ;           // lo
    }

    /**
     * @return координата конца 4
     */
    public int getEnd4() {
        return ((toUnsignedInt(values[20]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[21]) & 0x7F) * 100 ;           // lo
    }

    /**
     * @return величина ограничения 5
     */
    public int getValue5() {
        return toUnsignedInt(values[22]) & 0x7F;
    }

    /**
     * @return координата начала 5
     */
    public int getBegin5() {
        return ((toUnsignedInt(values[23]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[24]) & 0x7F) * 100 ;           // lo
    }

    /**
     * @return координата конца 5
     */
    public int getEnd5() {
        return ((toUnsignedInt(values[25]) & 0x7F) << 7 |     // hi
                toUnsignedInt(values[26]) & 0x7F) * 100 ;           // lo
    }
}
