package avpt.gr.blocks32.s5k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_54 {

    private final byte[] values;

    public Block32_54(byte[] values) {
        this.values = values;
    }

    /**
     * @return Ток якоря-1 4секции
     */
    public int getAmperageAnchor1_s4() {
        return Math.abs((short)(toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])));
    }

    /**
     * @return Ток якоря-2 4секции
     */
    public int getAmperageAnchor2_s4() {
        return Math.abs((short)(toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])));
    }

    /**
     * @return Ток якоря-3 4секции
     */
    public int getAmperageAnchor3_s4() {
        return Math.abs((short)(toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])));
    }

    /**
     * @return Ток якоря-4 4секции
     */
    public int getAmperageAnchor4_s4() {
        return Math.abs((short)(toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])));
    }

    /**
     * @return Ток возбужедния-1 4сек
     */
    public int getAmperageExcitation1_s4() {
        return Math.abs((short)(toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9])));
    }

    /**
     * @return Ток возбужедния-2 4сек
     */
    public int getAmperageExcitation2_s4() {
        return Math.abs((short)(toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])));
    }

    /**
     * @return Ток возбужедния-3 4сек
     */
    public int getAmperageExcitation3_s4() {
        return Math.abs((short)(toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])));
    }

    /**
     * @return Ток возбужедния-4 4сек
     */
    public int getAmperageExcitation4_s4() {
        return Math.abs((short)(toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])));
    }

    /**
     * @return Напряжение двигателя 1 4 секция
     */
    public int getVoltageEngine1_s4() {
        return Math.abs((short)(toUnsignedInt(values[16]) << 8 | toUnsignedInt(values[17])));
    }

    /**
     * @return Напряжение двигателя 2 4 секция
     */
    public int getVoltageEngine2_s4() {
        return Math.abs((short)(toUnsignedInt(values[18]) << 8 | toUnsignedInt(values[19])));
    }

    /**
     * @return Напряжение двигателя 3 4 секция
     */
    public int getVoltageEngine3_s4() {
        return Math.abs((short)(toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21])));
    }

    /**
     * @return Напряжение двигателя 4 4 секция
     */
    public int getVoltageEngine4_s4() {
        return Math.abs((short)(toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])));
    }
}
