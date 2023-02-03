package avpt.gr.blocks32.vl80;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_59 {

    private final byte[] values;

    public Block32_59(byte[] values) {
        this.values = values;
    }

    /**
     * @return Активная энергия прямого направления 1-я секция
     * БР
     */
    public long getEnergyAct_s1() {
        if (toUnsignedInt(values[3]) == 0xFF) return 0;
        long val = (long) toUnsignedInt(values[3]) << 24 |    // hi
                (long) toUnsignedInt(values[4]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[6]);               // lo
        return (BigDecimal.valueOf(val * 0.646551724).longValue());
    }

    /**
     * @return Активная энергия обратного направления 1-я секция
     * БР
     */
    public long getEnergyActRec_s1() {
        if (toUnsignedInt(values[7]) == 0xFF) return 0;
        long val = (long) toUnsignedInt(values[7]) << 24 |    // hi
                (long) toUnsignedInt(values[8]) << 16 |
                (long) toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[10]);               // lo
        return (BigDecimal.valueOf(val * 0.646551724).longValue());
    }

    /**
     * @return Активная энергия прямого направления 2-я секция
     * БР
     */
    public long getEnergyAct_s2() {
        if (toUnsignedInt(values[11]) == 0xFF) return 0;
        long val = (long) toUnsignedInt(values[11]) << 24 |    // hi
                (long) toUnsignedInt(values[12]) << 16 |
                (long) toUnsignedInt(values[13]) << 8 |
                toUnsignedInt(values[14]);               // lo
        return (BigDecimal.valueOf(val * 0.646551724).longValue());
    }

    /**
     * @return Активная энергия обратного направления 2-я секция
     * БР
     */
    public long getEnergyActRec_s2() {
        if (toUnsignedInt(values[15]) == 0xFF) return 0;
        long val = (long) toUnsignedInt(values[15]) << 24 |    // hi
                (long) toUnsignedInt(values[16]) << 16 |
                (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[18]);               // lo
        return (BigDecimal.valueOf(val * 0.646551724).longValue());
    }

    /**
     * @return напряжение 1-я секция
     */
    public double getVoltage_s1() {
        return (toUnsignedInt(values[22]) << 16 | toUnsignedInt(values[21]) << 8 | toUnsignedInt(values[20]))  / 100.0 * 107.758620690;
    }

    /**
     * @return ток 1-я секция
     */
    public int getAmperage_s1() {
        return (toUnsignedInt(values[25]) << 16 | toUnsignedInt(values[24]) << 8 | toUnsignedInt(values[23])) * 60;
    }

    /**
     * @return cos fi 1-я секция
     */
    public int getCos_fi_s1() {
        return toUnsignedInt(values[28]) << 16 | toUnsignedInt(values[27]) << 8 | toUnsignedInt(values[26]);
    }
}
