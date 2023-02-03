package avpt.gr.blocks32.vl80;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5B {

    private final byte[] values;

    public Block32_5B(byte[] values) {
        this.values = values;
    }

    /**
     * @return активная мощность 1-я секция
     */
    public long getActPower_s1() {
        return (long) toUnsignedInt(values[5]) << 16 |
                (long) toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[3]);               // lo
    }

    /**
     * @return реактивная мощность 1-я секция
     */
    public long getReactPower_s1() {
        return (long) toUnsignedInt(values[8]) << 16 |
                (long) toUnsignedInt(values[7]) << 8 |
                toUnsignedInt(values[6]);               // lo
    }

    /**
     * @return активная мощность 2-я секция
     */
    public long getActPower_s2() {
        return (long) toUnsignedInt(values[17]) << 16 |
                (long) toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[15]);               // lo
    }

    /**
     * @return реактивная мощность 2-я секция
     */
    public long getReactPower_s2() {
        return (long) toUnsignedInt(values[20]) << 16 |
                (long) toUnsignedInt(values[19]) << 8 |
                toUnsignedInt(values[18]);               // lo
    }
}
