package avpt.gr.blocks32.s5k_2;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5B {

    private final byte[] values;

    public Block32_5B(byte[] values) {
        this.values = values;
    }

    /**
     * @return Среднеквадратическое значение напряжения 1 секция (напряжение контактной сети)
     * БР
     */
    public long getRmsVoltage_s1() {
        return (long) toUnsignedInt(values[3]) << 24 | // hi
                (long) toUnsignedInt(values[2]) << 16 |
                (long) toUnsignedInt(values[1]) << 8 |
                toUnsignedInt(values[0]);        // lo
    }

    /**
     * @return Среднеквадратическое значение силы тока 1 секция (общий ток)
     * БР
     */
    public long getRmsAmperage_s1() {
        return (long)toUnsignedInt(values[7]) << 24 | // hi
                (long)toUnsignedInt(values[6]) << 16 |
                (long)toUnsignedInt(values[5]) << 8 |
                (long)toUnsignedInt(values[4]);        // lo
    }

    /**
     * @return Полная мощность 1 секция
     * БР
     */
    public long getFullPower_s1() {
        return (long)toUnsignedInt(values[11]) << 24 | // hi
                (long)toUnsignedInt(values[10]) << 16 |
                (long)toUnsignedInt(values[9]) << 8 |
                (long)toUnsignedInt(values[8]);        // lo
    }

    /**
     * @return Среднеквадратическое значение напряжения 2 секция (напряжение контактной сети)
     * БР
     */
    public long getRmsVoltage_s2() {
        return (long)toUnsignedInt(values[15]) << 24 | // hi
                (long)toUnsignedInt(values[14]) << 16 |
                (long)toUnsignedInt(values[13]) << 8 |
                (long)toUnsignedInt(values[12]);        // lo
    }

    /**
     * @return Среднеквадратическое значение силы тока 2 секция (общий ток)
     * БР
     */
    public long getRmsAmperage_s2() {
        return (long)toUnsignedInt(values[19]) << 24 | // hi
                (long)toUnsignedInt(values[18]) << 16 |
                (long)toUnsignedInt(values[17]) << 8 |
                (long)toUnsignedInt(values[16]);        // lo
    }

    /**
     * @return Полная мощность 2 секция
     * БР
     */
    public long getFullPower_s2() {
        return (long)toUnsignedInt(values[23]) << 24 | // hi
                (long)toUnsignedInt(values[22]) << 16 |
                (long)toUnsignedInt(values[21]) << 8 |
                (long)toUnsignedInt(values[20]);        // lo
    }

}
