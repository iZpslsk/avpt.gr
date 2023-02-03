package avpt.gr.blocks32.s5;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_55 {

    private final byte[] values;

    public Block32_55(byte[] values) {
        this.values = values;
    }

    /**
      * @return - БЛОК
     */
    public int getBlock() {
        return (toUnsignedInt(values[26]) & 0x01) << 8 | toUnsignedInt(values[25]);
    }

    /**
     * 1 Ручка ККМ
     * 2 САУТ
     * 3 РУТП
     * 4 ИСАВП-РТ
     * 5 МПСУ
     * @return превматика источник задания
     */
    public int getPneumaticSrcTask() {
        return toUnsignedInt(values[12]);
    }
}
