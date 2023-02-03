package avpt.gr.blocks32.overall;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.common.UtilsArmG.windows1251;

/**
 * Информация о машинисте ID = 8
 */
public class Block32_1D_8 {

    private byte[] values;

    public Block32_1D_8(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return табельный номер
     */
    public int getNumTab() {
        return toUnsignedInt(values[28]) << 16  |  // hi
               toUnsignedInt(values[27]) << 8  |
               toUnsignedInt(values[26]);          // lo
    }

    /**
     * @return фамилия машиниста
     */
    public String getSurname() {
        byte[] bytes = Arrays.copyOfRange(values, 1, 27);
        return new String(bytes, windows1251).trim();
    }

}
