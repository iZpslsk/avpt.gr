package avpt.gr.blocks32.s5k_2;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.S5K_2;

public class Block32_51 {

    private final byte[] values;
    private final int typeLoc;

    public Block32_51(int typeLoc, byte[] values) {
        this.values = values;
        this.typeLoc = typeLoc;
    }

    /**
     * @return - тест управления
     */
    public int getTest() {
        if (typeLoc == S5K_2)
            return (toUnsignedInt(values[0]) >>> 7) & 0x01;
        else
            return 0;

    }

    /**
     * @return - комманды управления
     */
    public int getCommand() {
        if (typeLoc == S5K_2)
            return toUnsignedInt(values[0]) & 0x7F;
        else
            return 0xFFFF;
    }

    /**
     * @return - режимы управления
     */
    public int getMode() {
        if (typeLoc == S5K_2)
            return toUnsignedInt(values[1]);
        else
            return 0xFFFF;
    }

    /**
     * @return - режим работы
     */
    public int getWork() {
        if (typeLoc == S5K_2)
            return toUnsignedInt(values[2]);
        else
            return 0xFFFF;
    }

    /**
     * @return - код ошибки
     */
    public int getCodeError() {
        if (typeLoc == S5K_2)
            return toUnsignedInt(values[4]) << 8 |
                    toUnsignedInt(values[3]);
        else
            return 0xFFFF;
    }

}
