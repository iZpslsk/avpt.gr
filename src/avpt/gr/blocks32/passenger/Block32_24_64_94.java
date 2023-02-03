package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.*;
import static avpt.gr.train.Train.EP2K_KAUD;

/**
 * (=) 0x24
 * (~) 0x64
 * (=~) 0x94
 */
public class Block32_24_64_94 {

    private final byte[] values;
    private final int typeLoc;

    public Block32_24_64_94(int typeLoc, byte[] values) {
        this.values = values;
        this.typeLoc = typeLoc;
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

    public int getCounterDps1() {
        return toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7]);
    }

    public int getCounterDps2() {
        return toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9]);
    }

    public int getDiscrete1() {
        return toUnsignedInt(values[25]) << 8 | toUnsignedInt(values[24]);
    }

    public int getDiscrete2() {
        return toUnsignedInt(values[27]) << 8 | toUnsignedInt(values[26]);
    }

    public int getStatusController() {
        return toUnsignedInt(values[28]);
    }


    /**
     * @return Срабатывание защиты – БВ/ГВ
     */
    public int getProtect() {
        switch (typeLoc) {
            case CHS2_RED_FLAG:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_USAVPP:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
                return ~getDiscrete1() & 0x01;
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP2K_KAUD:
            case EP20_KAUD:
                return getDiscrete1() & 0x01;
            default: return 0;
        }
    }

    /**
     * @return Срабатывание защиты – БВ/ГВ каб 1
     */
    public int getProtect1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete1() >>> 2) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return Срабатывание защиты – БВ/ГВ каб 2
     */
    public int getProtect2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete2() >>> 2) & 0x01;
            default: return 0;
        }
    }

    /**
     * ЭП20
     * @return ЭПК - 1каб
     */
    public int getEPK1() {
        switch (typeLoc) {
            case CHS2_RED_FLAG:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_USAVPP:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case CHS4T_KAUD:
            case EP2K_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP20_KAUD:
                return (getDiscrete1() >>> 1)  & 0x01;
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return getDiscrete1()  & 0x01;
            default: return 0;
        }
    }

    /**
     * ЭП20
     * @return ЭПК - 2каб
     */
    public int getEPK2() {
        switch (typeLoc) {
            case CHS2_RED_FLAG:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_USAVPP:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case CHS4T_KAUD:
            case EP2K_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP20_KAUD:
                return (getDiscrete1() >>> 2)  & 0x01;
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return getDiscrete2()  & 0x01;
            default: return 0;
        }
    }

    /**
     * @return боксование
     */
    public int getBox() {
        switch (typeLoc) {
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP2K_KAUD:
            case EP20_KAUD:
                return (getDiscrete1() >>> 9) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return Срабатывание реле боксования каб 1
     */
    public int getBox1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete1() >>> 1) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return Срабатывание реле боксования каб 2
     */
    public int getBox2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete2() >>> 1) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return УККНП (коррекция координаты)
     */
    public int getUKKNP() {
        switch (typeLoc) {
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP2K_KAUD:
                return (getDiscrete1() >>> 3) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return УККНП (коррекция координаты) 1 каб
     */
    public int getUKKNP1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete1() >>> 3) & 0x01;
            default: return 0;
        }
    }

    /**
     * @return УККНП (коррекция координаты) 2 каб
     */
    public int getUKKNP2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS8_KAUD:
                return (getDiscrete2() >>> 3) & 0x01;
            default: return 0;
        }
    }

    /**
     * ЭП20
     * @return 2 - переменный, 1 - постоянный  (результат аналогичен АСИМ)
     */
    public int getAcDcUI() {
        return ((getDiscrete1() >>> 10) & 0x01) == 1 ? 2 : 1;
    }

}
