package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_21_2 {

    private byte[] values;

    public Block32_21_2(byte[] values) {
        this.values = values;
    }

    /**
     * @return тип вагона 1
     */
    public int getTypeWag1() {
        return (toUnsignedInt(values[2]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 1
     */
    public int getNumWag1() {
        return (toUnsignedInt(values[2]) & 0x01) << 7 |
               toUnsignedInt(values[3]) & 0x7F;
    }

    /**
     * @return вес вагона 1
     */
    public int getWeightWag1() {
        return toUnsignedInt(values[4]) & 0x7F;
    }

    /**
     * @return тип вагона 2
     */
    public int getTypeWag2() {
        return (toUnsignedInt(values[5]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 2
     */
    public int getNumWag2() {
        return (toUnsignedInt(values[5]) & 0x01) << 7 |
                toUnsignedInt(values[6]) & 0x7F;
    }

    /**
     * @return вес вагона 2
     */
    public int getWeightWag2() {
        return toUnsignedInt(values[7]) & 0x7F;
    }

    /**
     * @return тип вагона 3
     */
    public int getTypeWag3() {
        return (toUnsignedInt(values[8]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 3
     */
    public int getNumWag3() {
        return (toUnsignedInt(values[8]) & 0x01) << 7 |
                toUnsignedInt(values[9]) & 0x7F;
    }

    /**
     * @return вес вагона 3
     */
    public int getWeightWag3() {
        return toUnsignedInt(values[10]) & 0x7F;
    }

    /**
     * @return тип вагона 4
     */
    public int getTypeWag4() {
        return (toUnsignedInt(values[11]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 4
     */
    public int getNumWag4() {
        return (toUnsignedInt(values[11]) & 0x01) << 7 |
                toUnsignedInt(values[12]) & 0x7F;
    }

    /**
     * @return вес вагона 4
     */
    public int getWeightWag4() {
        return toUnsignedInt(values[13]) & 0x7F;
    }

    /**
     * @return тип вагона 5
     */
    public int getTypeWag5() {
        return (toUnsignedInt(values[14]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 5
     */
    public int getNumWag5() {
        return (toUnsignedInt(values[14]) & 0x01) << 7 |
                toUnsignedInt(values[15]) & 0x7F;
    }

    /**
     * @return вес вагона 5
     */
    public int getWeightWag5() {
        return toUnsignedInt(values[16]) & 0x7F;
    }

    /**
     * @return тип вагона 6
     */
    public int getTypeWag6() {
        return (toUnsignedInt(values[17]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 6
     */
    public int getNumWag6() {
        return (toUnsignedInt(values[17]) & 0x01) << 7 |
                toUnsignedInt(values[18]) & 0x7F;
    }

    /**
     * @return вес вагона 6
     */
    public int getWeightWag6() {
        return toUnsignedInt(values[19]) & 0x7F;
    }

    /**
     * @return тип вагона 7
     */
    public int getTypeWag7() {
        return (toUnsignedInt(values[20]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 7
     */
    public int getNumWag7() {
        return (toUnsignedInt(values[20]) & 0x01) << 7 |
               toUnsignedInt(values[21]) & 0x7F;
    }

    /**
     * @return вес вагона 7
     */
    public int getWeightWag7() {
        return toUnsignedInt(values[22]) & 0x7F;
    }

    /**
     * @return тип вагона 8
     */
    public int getTypeWag8() {
        return (toUnsignedInt(values[23]) & 0x7F) >>> 2;
    }

    /**
     * @return номер вагона 8
     */
    public int getNumWag8() {
        return (toUnsignedInt(values[23]) & 0x01) << 7 |
                toUnsignedInt(values[24]) & 0x7F;
    }

    /**
     * @return вес вагона 8
     */
    public int getWeightWag8() {
        return toUnsignedInt(values[25]) & 0x7F;
    }

    /**
     * @return количество вагонов
     */
    public int getCountWags() {
        return toUnsignedInt(values[26]);
    }

    /**
     * @return номер фрейма
     */
    public int getNumFrame() {
        return toUnsignedInt(values[27]) & 0x1F;
    }

    /**
     * @return количество фреймов
     */
    public int getCountFrames() {
        return toUnsignedInt(values[28]) & 0x1F;
    }
}
