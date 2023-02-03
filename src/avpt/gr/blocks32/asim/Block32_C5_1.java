package avpt.gr.blocks32.asim;


import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.*;

public class Block32_C5_1 {

    private final byte[] values;

    public Block32_C5_1(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return id станции
     */
    public int getStationId() {
        return toUnsignedInt(values[0]) << 8  |  // hi
                toUnsignedInt(values[1]);        // lo
    }

    /**
     * @return название станции
     */
    public String getNameStation() {
        byte[] bytes = Arrays.copyOfRange(values, 2, 27);
        return new String(bytes, cp866).trim();
    }


}
