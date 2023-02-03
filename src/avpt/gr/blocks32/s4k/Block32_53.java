package avpt.gr.blocks32.s4k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_53 {

    private byte[] values;

    public Block32_53(byte[] values) {
        this.values = values;
    }

    /**
     * @return Ток якоря-1 2секции
     */
    public int getAmperageAnchor1_s2() {
        return Math.abs((short)(toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])));
    }

    /**
     * @return Ток якоря-2 2секции
     */
    public int getAmperageAnchor2_s2() {
        return Math.abs((short)(toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])));
    }

    /**
     * @return Ток якоря-3 2секции
     */
    public int getAmperageAnchor3_s2() {
        return Math.abs((short)(toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])));
    }

    /**
     * @return Ток якоря-4 2секции
     */
    public int getAmperageAnchor4_s2() {
        return Math.abs((short)(toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])));
    }

    /**
     * @return Ток возбужедния-1 2сек
     */
    public int getAmperageExcitation1_s2() {
        return Math.abs((short)(toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9])));
    }

    /**
     * @return Ток возбужедния-2 2сек
     */
    public int getAmperageExcitation2_s2() {
        return Math.abs((short)(toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])));
    }

    /**
     * @return Ток возбужедния-3 2сек
     */
    public int getAmperageExcitation3_s2() {
        return Math.abs((short)(toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])));
    }

    /**
     * @return Ток возбужедния-4 2сек
     */
    public int getAmperageExcitation4_s2() {
        return Math.abs((short)(toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])));
    }

    /**
     * @return Напряжение двигателя 1 2 секция
     */
    public int getVoltageEngine1_s2() {
        return Math.abs((short)(toUnsignedInt(values[16]) << 8 | toUnsignedInt(values[17])));
    }

    /**
     * @return Напряжение двигателя 2 2 секция
     */
    public int getVoltageEngine2_s2() {
        return Math.abs((short)(toUnsignedInt(values[18]) << 8 | toUnsignedInt(values[19])));
    }

    /**
     * @return Напряжение двигателя 3 2 секция
     */
    public int getVoltageEngine3_s2() {
        return Math.abs((short)(toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21])));
    }

    /**
     * @return Напряжение двигателя 4 2 секция
     */
    public int getVoltageEngine4_s2() {
        return Math.abs((short)(toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])));
    }

    /**
     * @return Сила тяги локомотива, кН
     */
    public int getPowerLocomotive() {
        //toUnsignedInt(values[27]) << 24 | // hi
        // toUnsignedInt(values[26]) << 16 |
        int val = toUnsignedInt(values[25]) << 8 |
                toUnsignedInt(values[24]);        // lo
        if (val > 1000) val = 0;
        return val;
    }
}
