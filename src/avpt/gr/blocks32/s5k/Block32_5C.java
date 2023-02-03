package avpt.gr.blocks32.s5k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5C {

    private final byte[] values;

    public Block32_5C(byte[] values) {
        this.values = values;
    }

    /**
     * @return Ток якоря-1 3секции
     */
    public int getAmperageAnchor1_s3() {
        return Math.abs((short)(toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])));
    }

    /**
     * @return Ток якоря-2 3секции
     */
    public int getAmperageAnchor2_s3() {
        return Math.abs((short)(toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])));
    }

    /**
     * @return Ток якоря-3 3секции
     */
    public int getAmperageAnchor3_s3() {
        return Math.abs((short)(toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])));
    }

    /**
     * @return Ток якоря-4 3секции
     */
    public int getAmperageAnchor4_s3() {
        return Math.abs((short)(toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])));
    }

    /**
     * @return Ток возбужедния-1 3сек
     */
    public int getAmperageExcitation1_s3() {
        return Math.abs((short)(toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9])));
    }

    /**
     * @return Ток возбужедния-2 3сек
     */
    public int getAmperageExcitation2_s3() {
        return Math.abs((short)(toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])));
    }

    /**
     * @return Ток возбужедния-3 3сек
     */
    public int getAmperageExcitation3_s3() {
        return Math.abs((short)(toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])));
    }

    /**
     * @return Ток возбужедния-4 3сек
     */
    public int getAmperageExcitation4_s3() {
        return Math.abs((short)(toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])));
    }

    /**
     * @return Напряжение двигателя 1 3 секция
     */
    public int getVoltageEngine1_s3() {
        return Math.abs((short)(toUnsignedInt(values[16]) << 8 | toUnsignedInt(values[17])));
    }

    /**
     * @return Напряжение двигателя 2 3 секция
     */
    public int getVoltageEngine2_s3() {
        return Math.abs((short)(toUnsignedInt(values[18]) << 8 | toUnsignedInt(values[19])));
    }

    /**
     * @return Напряжение двигателя 3 3 секция
     */
    public int getVoltageEngine3_s3() {
        return Math.abs((short)(toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21])));
    }

    /**
     * @return Напряжение двигателя 4 3 секция
     */
    public int getVoltageEngine4_s3() {
        return Math.abs((short)(toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])));
    }

    /**
     * @return Сила торможения, кН
     */
    public int getPowerBrake() {
        //return //toUnsignedInt(values[27]) << 24 | // hi
               // toUnsignedInt(values[26]) << 16 |
        int val = toUnsignedInt(values[25]) << 8 |
                toUnsignedInt(values[24]);        // lo
        if (val > 1000) val = 0;
        return val * -1;
    }
}
