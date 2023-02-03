package avpt.gr.blocks32.vl11;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_13 {

    private byte[] values;

    public Block32_13(byte[] values) {
        this.values = values;
    }

    /**
     * @return энергия потребленная 1-секция
     * БР
     */
    public long getEnergy_s1() {
        long val = (long) toUnsignedInt(values[3]) << 24 |    // hi
                (long) toUnsignedInt(values[4]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[6]);               // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }

    /**
     * @return энергия рекуперации 1-секция
     * БР
     */
    public long getEnergyRec_s1() {
        long val = (long) toUnsignedInt(values[7]) << 24 |    // hi
                (long) toUnsignedInt(values[8]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[10]);           // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }

    /**
     * @return энергия потребленная 2-секция
     * БР
     */
    public long getEnergy_s2() {
        long val = (long) toUnsignedInt(values[11]) << 24 |    // hi
                (long) toUnsignedInt(values[12]) << 16 |
                (long) toUnsignedInt(values[13]) << 8 |
                toUnsignedInt(values[14]);               // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }

    /**
     * @return энергия рекуперации 2-секция
     * БР
     */
    public long getEnergyRec_s2() {
        long val = (long) toUnsignedInt(values[15]) << 24 |    // hi
                (long) toUnsignedInt(values[16]) << 16 |
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[18]);           // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }
}
