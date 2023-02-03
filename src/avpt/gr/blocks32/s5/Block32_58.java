package avpt.gr.blocks32.s5;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_58 {

    private final byte[] values;

    public Block32_58(byte[] values) {
        this.values = values;
    }

    /**
     * @return Активная энергия переменного тока потребленная 2 секция
     */
    public long getEnergyAct_s2() {
        long val = (long) toUnsignedInt(values[6]) << 24 |    // hi
                (long) toUnsignedInt(values[5]) << 16 |
                (long) toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[3]);           // lo
        return (BigDecimal.valueOf(val * 0.3333).longValue());
    }

    /**
     * @return Активная энергия переменного тока рекуперированная 2 секция
     * БР
     */
    public long getEnergyActRec_s2() {
        long val = (long) toUnsignedInt(values[10]) << 24 |    // hi
                (long) toUnsignedInt(values[9]) << 16 |
                (long) toUnsignedInt(values[8]) << 8 |
                toUnsignedInt(values[7]);           // lo
        return BigDecimal.valueOf(val * 0.3333).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока потребленная 2 секция
     */
    public long getEnergyReact_s2() {
        long val = (long) toUnsignedInt(values[14]) << 24 |    // hi
                (long) toUnsignedInt(values[13]) << 16 |
                (long) toUnsignedInt(values[12]) << 8 |
                toUnsignedInt(values[11]);           // lo
        return BigDecimal.valueOf(val * 0.3333).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока рекуперированная 2 секция
     * БР
     */
    public long getEnergyReactRec_s2() {
        long val = (long) toUnsignedInt(values[18]) << 24 |    // hi
                (long) toUnsignedInt(values[17]) << 16 |
                (long) toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[15]);           // lo
        return BigDecimal.valueOf(val * 0.3333).longValue();
    }
}
