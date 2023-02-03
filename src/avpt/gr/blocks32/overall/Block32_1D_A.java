package avpt.gr.blocks32.overall;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.common.UtilsArmG.windows1251;

/**
 * название станции
 */
public class Block32_1D_A {

    private byte[] values;

    public Block32_1D_A(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return № перегона
     */
    public int getStationId() {
        return toUnsignedInt(values[28]) << 8  |  // hi
                toUnsignedInt(values[27]);         // lo
    }

    /**
     * @return название станции
     */
    public String getNameStation() {
        byte[] bytes = Arrays.copyOfRange(values, 1, 27);
        return new String(bytes, windows1251).trim();
    }

}
