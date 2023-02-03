package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_78 {

    private final byte[] values;

    public Block32_78(byte[] values) {
        this.values = values;
    }

    /**
     * @return индекс серии локомотива тэп70
     */
    public int getTypeLoc() {
        return  toUnsignedInt(values[9]) + 170;
    }

}
