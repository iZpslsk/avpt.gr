package avpt.gr.blocks32.asim;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_C2_1 {

    private final byte[] values;

    public Block32_C2_1(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return ток электровоза
     */
    public int getAmperage() {
        return (short)(toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1]));
    }

    /**
     * @return напряжение электровоза
     */
    public int getVoltage() {
        return toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3]);
    }



}
