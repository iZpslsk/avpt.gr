package avpt.gr.blocks32.s5k;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_58 {

    private final byte[] values;

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
        return BigDecimal.valueOf(val * 0.1).longValue();
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
     * @return Реактивная энергия переменного тока потребленная 2 секция
     * БР
     */
    public long getEnergyReact_s2() {
        long val = (long) toUnsignedInt(values[11]) << 24 |    // hi
                (long) toUnsignedInt(values[10]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[8]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока рекуперированная 2 секция
     * БР
     */
    public long getEnergyReactRec_s2() {
        long val = (long) toUnsignedInt(values[15]) << 24 |    // hi
                (long) toUnsignedInt(values[14]) << 16 |
                (long) toUnsignedInt(values[13]) << 8 |
                toUnsignedInt(values[12]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Полная энергия переменного тока 2 секция
     * БР
     */
    public long getEnergyFull_s2() {
        long val = (long) toUnsignedInt(values[19]) << 24 |    // hi
                (long) toUnsignedInt(values[18]) << 16 |
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[16]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }
}
