package avpt.gr.blocks32.s5;

import java.math.BigDecimal;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_57 {

    private final byte[] values;

    public Block32_57(byte[] values) {
        this.values = values;
    }

    /**
     * @return Активная энергия переменного тока потребленная 1 секция
     */
    public long getEnergyAct_s1() {
        long val = (long) toUnsignedInt(values[6]) << 24 |    // hi
                (long) toUnsignedInt(values[5]) << 16 |
                (long) toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[3]);           // lo
        return (BigDecimal.valueOf(val /* * 0.3333*/).longValue());
    }

    /**
     * @return Активная энергия переменного тока рекуперированная 1 секция
     * БР
     */
    public long getEnergyActRec_s1() {
        long val = (long) toUnsignedInt(values[10]) << 24 |    // hi
                (long) toUnsignedInt(values[9]) << 16 |
                (long) toUnsignedInt(values[8]) << 8 |
                toUnsignedInt(values[7]);           // lo
        return BigDecimal.valueOf(val /* * 0.3333*/).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока потребленная 1 секция
     */
    public long getEnergyReact_s1() {
        long val = (long) toUnsignedInt(values[14]) << 24 |    // hi
                (long) toUnsignedInt(values[13]) << 16 |
                (long) toUnsignedInt(values[12]) << 8 |
                toUnsignedInt(values[11]);           // lo
        return BigDecimal.valueOf(val /* * 0.3333*/).longValue();
    }

    /**
     * @return Реактивная энергия переменного тока рекуперированная 1 секция
     * БР
     */
    public long getEnergyReactRec_s1() {
        long val = (long) toUnsignedInt(values[18]) << 24 |    // hi
                (long) toUnsignedInt(values[17]) << 16 |
                (long) toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[15]);           // lo
        return BigDecimal.valueOf(val /* * 0.3333*/).longValue();
    }

    /**
     * @return Напряжение контактной сети
     */
    public double getVoltage() {
        return ((toUnsignedInt(values[22]) & 0x3F) << 16 | toUnsignedInt(values[21]) << 8 | toUnsignedInt(values[20])) * 2.5;
    }

    /**
     * @return Общий ток электровоза
     */
    public double getAmperage() {
        return ((toUnsignedInt(values[25])  & 0x3F) << 16 | toUnsignedInt(values[24]) << 8 | toUnsignedInt(values[23])) / 1000.0 * 12;
    }

    /**
     * @return cos fi
     */
    public int getCos_fi() {
        return (toUnsignedInt(values[28]) << 16 | toUnsignedInt(values[27]) << 8 | toUnsignedInt(values[26]));
    }

}
