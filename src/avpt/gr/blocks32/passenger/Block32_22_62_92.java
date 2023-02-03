package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * (=) 0x22
 * (~) 0x62
 * (=~) 0x92
 */
public class Block32_22_62_92 {

    private final byte[] values;
    private final int typeLoc;

    public Block32_22_62_92(int typeLoc, byte[] values) {
        this.values = values;
        this.typeLoc = typeLoc;
    }

    /**
     * @return статус ДПС1
     */
    public int getDps1Status() {
        return toUnsignedInt(values[0]);
    }

    /**
     * @return фаза ДПС1
     */
    public int getDps1Phase() {
        return toUnsignedInt(values[1]);
    }

    /**
     * @return код ускорения ДПС1
     */
    public int getDps1Acceleration() {
        return toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3]);
    }

    /**
     * @return счетчик ДПС1
     */
    public int getDps1Counter() {
        return  toUnsignedInt(values[4]) << 24 |
                toUnsignedInt(values[5]) << 16 |
                toUnsignedInt(values[6]) << 8 |
                toUnsignedInt(values[7]);
    }

    /**
     * @return статус ДПС2
     */
    public int getDps2Status() {
        return toUnsignedInt(values[8]);
    }

    /**
     * @return фаза ДПС2
     */
    public int getDps2Phase() {
        return toUnsignedInt(values[9]);
    }

    /**
     * @return код ускорения ДПС1
     */
    public int getDps2Acceleration() {
        return toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11]);
    }

    /**
     * @return счетчик ДПС2
     */
    public int getDps2Counter() {
        return  toUnsignedInt(values[12]) << 24 |
                toUnsignedInt(values[13]) << 16 |
                toUnsignedInt(values[14]) << 8 |
                toUnsignedInt(values[15]);
    }

}
