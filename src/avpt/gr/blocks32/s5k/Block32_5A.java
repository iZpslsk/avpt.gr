package avpt.gr.blocks32.s5k;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5A {

    private final byte[] values;

    public Block32_5A(byte[] values) {
        this.values = values;
    }

    /**
     * @return Активная энергия переменного тока потребленная 4 секция
     * БР
     */
    public long getEnergyAct_s4() {
        long val = (long) toUnsignedInt(values[19]) << 24 |    // hi
                (long) toUnsignedInt(values[18]) << 16 |
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[16]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Активная энергия переменного тока рекуперированная 4 секция
     * БР
     */
    public long getEnergyActRec_s4() {
        long val = (long) toUnsignedInt(values[7]) << 24 |    // hi
                (long) toUnsignedInt(values[6]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[4]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока потребленная 4 секция
     * БР
     */
    public long getEnergyReact_s4() {
        long val = (long) toUnsignedInt(values[11]) << 24 |    // hi
                (long) toUnsignedInt(values[10]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[8]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока рекуперированная 4 секция
     * БР
     */
    public long getEnergyReactRec_s4() {
        long val = (long) toUnsignedInt(values[15]) << 24 |    // hi
                (long) toUnsignedInt(values[14]) << 16 |
                (long) toUnsignedInt(values[13]) << 8 |
                toUnsignedInt(values[12]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }

    /**
     * @return Полная энергия переменного тока 4 секция
     * БР
     */
    public long getEnergyFull_s4() {
        long val = (long) toUnsignedInt(values[19]) << 24 |    // hi
                (long) toUnsignedInt(values[18]) << 16 |
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[16]);           // lo
        return BigDecimal.valueOf(val * 0.1).longValue();
    }
}
