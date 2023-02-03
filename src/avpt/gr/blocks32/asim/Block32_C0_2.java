package avpt.gr.blocks32.asim;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.*;

/**
 * машинист АСИМ
 */
public class Block32_C0_2 {

    private final byte[] values;

    public Block32_C0_2(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return ФИО машиниста
     */
    public String getNameDriver() {
        byte[] bytes = Arrays.copyOfRange(values, 0, 23);
        return new String(bytes, cp866).trim();
    }

    /**
     * @return Табельный номер машиниста
     */
    public long getTabNum() {
        return (long) toUnsignedInt(values[24]) << 24 |       // hi
               (long) toUnsignedInt(values[25]) << 16 |       //
               (long) toUnsignedInt(values[26]) << 8 |        //
                      toUnsignedInt(values[27]);              // lo
    }

}
