package avpt.gr.blocks32.passenger;


import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.*;

/**
 * EKC1
 * (=) 0x28
 * (~) 0x68
 * (=~) 0x98
 */
public class Block32_28_68_98 {

    private final byte[] values;
    private int typeLoc;

    public Block32_28_68_98(byte[] values, int typeLoc) {
        this.values = values;
        this.typeLoc = typeLoc;
    }

    /**
     * @return жд координата клуб-у
     */
    public int getCoordinate_GD_KLUB() {
        return  toUnsignedInt(values[0]) << 16 |
                toUnsignedInt(values[1]) << 8 |
                toUnsignedInt(values[2]);
    }

    /**
     * @return допустимая скорость
     */
    public int getSpeedLimit() {
        return toUnsignedInt(values[3]) | (toUnsignedInt(values[5]) & 0x01) << 8;
    }

    /**
     * @return компрессор
     */
    public int getCompress() {
        if (typeLoc == EP20_KAUD) {
            return (toUnsignedInt(values[17]) >>> 5) & 0x01;
        }
        else {
            return (toUnsignedInt(values[23]) >>> 6) & 0x01;
        }
    }

    /**
     * @return эпк kz4
     */
    public int getEPK_KZ4() {
        if (typeLoc == KZ4AT) {
            return (toUnsignedInt(values[9]) >>> 4) & 0x01;
        }
        else {
            return 0;
        }
    }

    /**
     * для чс 7
     * @return откл БВ секц 1
     */
    public int getBV1_chs7() {
        if (isCHS7(typeLoc))
            return (~toUnsignedInt(values[25]) & 0x01);
        else
            return 0;
    }

    /**
     * для чс 7
     * @return откл БВ секц 2
     */
    public int getBV2_chs7() {
        if (isCHS7(typeLoc))
            return (~(toUnsignedInt(values[25]) >>> 1) & 0x01);
        else
            return 0;
    }

    /**
     * для чс 7
     * @return  отпуск тормоза
     */
    public int getBrakeOff_chs7() {
        if (isCHS7(typeLoc))
            return toUnsignedInt(values[18]) & 0x01;
        else
            return 0;
    }

}
