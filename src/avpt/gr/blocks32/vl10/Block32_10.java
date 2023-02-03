package avpt.gr.blocks32.vl10;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_10 {

    private final byte[] values;

    public Block32_10(byte[] values) {
        this.values = values;
    }

    /**
     * @return Напряжение контактной сети
     */
    public double getVoltage() {
        return (toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])) * 0.06866455078125;
    }

    /**
     * @return ЭДС двигателя 1-я секция
     */
    public double getVoltageEngine_s1() {
        return (toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])) * 0.06866455078125;
    }

    /**
     * @return Общий ток электровоза
     */
    public double getAmperage() {
        return (short) (toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])) * 0.1068115234375;
    }

    /**
     * @return Ток якоря-1 1секции
     */
    public double getAmperageAnchor1_s1() {
        return (short) (toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])) * 0.02288818359375;
    }

    /**
     * @return Ток возбуждения 1сек
     */
    public double getAmperageExcitation_s1() {
        return (short) (toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9])) * 0.02288818359375;
    }

    /**
     * @return ЭДС двигателя 2-я секция
     */
    public double getVoltageEngine_s2() {
        return (toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])) * 0.06866455078125;
    }

    /**
     * @return Ток якоря-1 2секции
     */
    public double getAmperageAnchor1_s2() {
        return (short) (toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])) * 0.02288818359375;
    }

    /**
     * @return Ток якоря-2 2секции
     */
    public double getAmperageAnchor2_s2() {
        return (short) (toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])) * 0.02288818359375;
    }

    /**
     * @return Ток якоря-2 1секции
     */
    public double getAmperageAnchor2_s1() {
        return (short) (toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21])) * 0.02288818359375;
    }

    /**
     * @return Ток возбуждения 2сек
     */
    public double getAmperageExcitation_s2() {
        return (short) (toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])) * 0.02288818359375;
    }
}
