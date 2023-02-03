package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * (=) 0x25
 * (~) 0x65
 * (=~) 0x95
 */
public class Block32_25_65_95 {

    private final byte[] values;

    public Block32_25_65_95(int typeLoc, byte[] values) {
        this.values = values;
    }

    public int getPress1() {
        return toUnsignedInt(values[0]);
    }

    public int getPress2() {
        return toUnsignedInt(values[1]);
    }

    public int getPress3() {
        return toUnsignedInt(values[2]);
    }

    public int getPress4() {
        return toUnsignedInt(values[3]);
    }

    public int getPress5() {
        return toUnsignedInt(values[4]);
    }

    public int getPress6() {
        return toUnsignedInt(values[5]);
    }

    public int getPress7() {
        return toUnsignedInt(values[6]);
    }

    public int getPress8() {
        return toUnsignedInt(values[7]);
    }

    public int getPress9() {
        return toUnsignedInt(values[8]);
    }

    public int getPress10() {
        return toUnsignedInt(values[9]);
    }

    public int getPress11() {
        return toUnsignedInt(values[10]);
    }

    public int getPress12() {
        return toUnsignedInt(values[11]);
    }

    public int getPress13() {
        return toUnsignedInt(values[12]);
    }

    public int getPress14() {
        return toUnsignedInt(values[13]);
    }

}
