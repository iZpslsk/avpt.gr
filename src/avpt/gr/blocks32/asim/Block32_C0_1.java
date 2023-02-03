package avpt.gr.blocks32.asim;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.common.UtilsArmG.windows1251;

/**
 * маршрут АСИМ
 */
public class Block32_C0_1 {

    private final byte[] values;

    public Block32_C0_1(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return название маршрута
     */
    public String getNameRoute() {
        byte[] bytes = Arrays.copyOfRange(values, 0, 25);
        return new String(bytes, windows1251).trim();
    }

    /**
     * @return № маршрута
     */
    public int getRouteId() {
        return toUnsignedInt(values[26]) << 8  |  // hi
                toUnsignedInt(values[27]);        // lo
    }


}
