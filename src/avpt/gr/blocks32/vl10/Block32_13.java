package avpt.gr.blocks32.vl10;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_13 {

    private final byte[] values;

    public Block32_13(byte[] values) {
        this.values = values;
    }

    /**
     * @return энергия потребленная
     * БР
     */
    public long getEnergy() {
        long val = (long) toUnsignedInt(values[3]) << 24 |    // hi
                (long) toUnsignedInt(values[4]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[6]);               // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }

    /**
     * @return энергия рекуперации
     * БР
     */
    public long getEnergyRec() {
        long val = (long) toUnsignedInt(values[7]) << 24 |    // hi
                (long) toUnsignedInt(values[8]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[10]);           // lo
        return (BigDecimal.valueOf(val * 0.25).longValue());
    }
}
