package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_76 {

    private final byte[] values;

    public Block32_76(byte[] values) {
        this.values = values;
    }

    /**
     * @return индекс серии локомотива тэп70
     */
    public int getTypeLoc() {
        return  toUnsignedInt(values[11]) + 170;
    }

}
