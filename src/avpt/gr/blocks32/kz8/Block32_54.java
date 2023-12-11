package avpt.gr.blocks32.kz8;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_54 {

    private byte[] values;

    public Block32_54(byte[] values) {
        this.values = values;
    }

    /**
     * @return БУТ
     */
    public double getSpeedBoot() {
        return (toUnsignedInt(values[1]) << 8 | toUnsignedInt(values[0])) * 0.1;
    }

    /**
     * @return КЛУБ
     */
    public double getSpeedClub() {
        return toUnsignedInt(values[8]);
    }
}
