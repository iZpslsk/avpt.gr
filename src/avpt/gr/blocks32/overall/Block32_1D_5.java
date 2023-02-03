package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_1D_5 {

    private byte[] values;

    public Block32_1D_5(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return текущий км
     */
    public int getKm() {
        return toUnsignedInt(values[7]) << 8  |  // hi
                toUnsignedInt(values[6]);         // lo
    }

    /**
     * @return текущий пикет
     */
    public int getPicket() {
        return toUnsignedInt(values[8]);
    }
}
