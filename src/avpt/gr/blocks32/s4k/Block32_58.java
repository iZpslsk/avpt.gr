package avpt.gr.blocks32.s4k;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_58 {

    private byte[] values;

    public Block32_58(byte[] values) {
        this.values = values;
    }

    /**
     * @return Активная энергия переменного тока потребленная 2 секция
     * БР
     */
    public long getEnergyAct_s2() {
        long val = (long) toUnsignedInt(values[3]) << 24 |    // hi
                (long) toUnsignedInt(values[2]) << 16 |
                (long) toUnsignedInt(values[1]) << 8 |
                toUnsignedInt(values[0]);           // lo
        return (BigDecimal.valueOf(val * 0.1).longValue());
    }

    /**
     * @return Активная энергия переменного тока рекуперированная 2 секция
     * БР
     */
    public long getEnergyActRec_s2() {
        long val = (long) toUnsignedInt(values[7]) << 24 |    // hi
                (long) toUnsignedInt(values[6]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[4]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }


    /**
     * @return Среднеквадратическое значение напряжения 2 секция (напряжение контактной сети)
     * БР
     */
    public long getRmsVoltage_s2() {
        return (long) toUnsignedInt(values[11]) << 24 | // hi
                (long) toUnsignedInt(values[10]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[8]);        // lo
    }

    /**
     * @return Среднеквадратическое значение силы тока 2 секция (общий ток)
     * БР
     */
    public long getRmsAmperage_s2() {
        return (long) toUnsignedInt(values[15]) << 24 | // hi
                (long) toUnsignedInt(values[14]) << 16 |
                (long) toUnsignedInt(values[13]) << 8 |
                toUnsignedInt(values[12]);        // lo
    }
}
