package avpt.gr.blocks32.asim;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * расход энергии АСИМ 1-я секция
 */
public class Block32_C3_1 {

    private final byte[] values;

    public Block32_C3_1(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа (секция)
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return Активная энергия постоянного тока потребленная 2 секция
     */
    public long getEnergyAct_s2_dc() {
        return (long) toUnsignedInt(values[0]) << 24 |    // hi
               (long) toUnsignedInt(values[1]) << 16 |
               (long) toUnsignedInt(values[2]) << 8 |
                      toUnsignedInt(values[3]);           // lo
    }

    /**
     * @return Реактивная энергия постоянного тока потребленная 2 секция
     */
    public long getEnergyReact_s2_dc() {
        return (long) toUnsignedInt(values[4]) << 24 |    // hi
               (long) toUnsignedInt(values[5]) << 16 |
               (long) toUnsignedInt(values[6]) << 8 |
                      toUnsignedInt(values[7]);           // lo
    }

    /**
     * @return Активная энергия переменного тока рекуперированная 2 секция
     */
    public long getEnergyActRec_ac_s2() {
        return (long) toUnsignedInt(values[8]) << 24 |    // hi
               (long) toUnsignedInt(values[9]) << 16 |
               (long) toUnsignedInt(values[10]) << 8 |
                      toUnsignedInt(values[11]);           // lo
    }

    /**
     * @return Реактивная энергия переменного тока рекуперированная 2 секция
     */
    public long getEnergyReactRec_ac_s2() {
        return (long) toUnsignedInt(values[12]) << 24 |    // hi
               (long) toUnsignedInt(values[13]) << 16 |
               (long) toUnsignedInt(values[14]) << 8 |
                      toUnsignedInt(values[15]);           // lo
    }

    /**
     * @return Активная энергия переменного тока потребленная 2 секция
     */
    public long getEnergyAct_s2_ac() {
        return (long) toUnsignedInt(values[16]) << 24 |    // hi
               (long) toUnsignedInt(values[17]) << 16 |
               (long) toUnsignedInt(values[18]) << 8 |
                      toUnsignedInt(values[19]);           // lo
    }

    /**
     * @return Реактивная энергия переменного тока потребленная 2 секция
     */
    public long getEnergyReAct_s2_ac() {
        return (long) toUnsignedInt(values[20]) << 24 |    // hi
               (long) toUnsignedInt(values[21]) << 16 |
               (long) toUnsignedInt(values[22]) << 8 |
                      toUnsignedInt(values[23]);           // lo
    }
}
