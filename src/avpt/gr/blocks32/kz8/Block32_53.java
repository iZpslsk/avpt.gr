package avpt.gr.blocks32.kz8;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_53 {

    private byte[] values;

    public Block32_53(byte[] values) {
        this.values = values;
    }

    /**
     * @return Напряжение контактной сети
     */
    public double getVoltage() {
        return (toUnsignedInt(values[25]) << 8 | toUnsignedInt(values[24]));
    }

    /**
     *
     * @return ток якоря
     */
    public double getAmperageAnchor() {
        return (toUnsignedInt(values[26]));
    }

    /**
     * @return - сила Реализованная
     */
    public double getPowerImplement() {
        return (toUnsignedInt(values[19]) << 24 | toUnsignedInt(values[18]) << 16 | toUnsignedInt(values[17]) << 8 | toUnsignedInt(values[16])) * 0.1;
    }

}
