package avpt.gr.blocks32.passenger;

import static avpt.gr.blocks32.passenger.Block32_20_60_90.K_TR_I;
import static avpt.gr.blocks32.passenger.Block32_20_60_90.K_TR_U;
import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.*;

/**
 * (=) 0x23
 * (~) 0x63
 * (=~) 0x93
 */
public class Block32_23_63_93 {

    private final byte[] values;
    private final int typeLoc;
    private final int acdc_ui;

    // 0.0001 var
    public final static double K_ = 0.25;

    public Block32_23_63_93(int typeLoc, byte[] values, int acdc_ui) {
        this.values = values;
        this.typeLoc = typeLoc;
        this.acdc_ui = acdc_ui;
    }

    public int getE1B1() {
        return  toUnsignedInt(values[6]) << 24 |
                toUnsignedInt(values[5]) << 16 |
                toUnsignedInt(values[4]) << 8 |
                toUnsignedInt(values[3]);
    }

    public int getE2B1() {
        return  toUnsignedInt(values[10]) << 24 |
                toUnsignedInt(values[9]) << 16 |
                toUnsignedInt(values[8]) << 8 |
                toUnsignedInt(values[7]);
    }

    public int getE1B2() {
        return  toUnsignedInt(values[14]) << 24 |
                toUnsignedInt(values[13]) << 16 |
                toUnsignedInt(values[12]) << 8 |
                toUnsignedInt(values[11]);
    }

    public int getE2B2() {
        return  toUnsignedInt(values[18]) << 24 |
                toUnsignedInt(values[17]) << 16 |
                toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[15]);
    }

    public int getE1Dop() {
        return  toUnsignedInt(values[22]) << 24 |
                toUnsignedInt(values[21]) << 16 |
                toUnsignedInt(values[20]) << 8 |
                toUnsignedInt(values[19]);
    }

    public int getE2Dop() {
        return  toUnsignedInt(values[26]) << 24 |
                toUnsignedInt(values[25]) << 16 |
                toUnsignedInt(values[24]) << 8 |
                toUnsignedInt(values[23]);
    }

    public double getEnConsumptionSum() {
        switch (typeLoc) {
            case CHS7:
            case CHS7_EKS:
            case CHS8_KAUD:
                return (getE1B1() + getE1B2()) * K_;
            case CHS2_RED_FLAG:
            case CHS2K_USAVPP:
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP1_P:
                return getE1B1() * K_;
            case KZ4AT:
                return getE1B1();
            case CHS7_KAUD_F:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case CHS6_KAUD:
            case CHS7_EKS2:
            case CHS7_KAUD_L:
                return (getE1B1() + getE1B2() + getE2B1() + getE2B2()) * K_;
            case EP2K_KAUD:
                return (getE1B1() + getE2B1() + getE1Dop()) * K_;
            case EP20_KAUD:
                // постоянный
                if (acdc_ui == 1) {
                    return (getE1B2() + getE2Dop()) * K_ ;
                }
                // переменный
                else if (acdc_ui == 2) {
                    return getE1B1() * 0.0001 * K_TR_U * K_TR_I;
                }
            default: return 0;
        }
    }

    public double getEnConsumptionRec() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
            case EP1_P:
                return getE2B1() * K_;
            case KZ4AT:
                return getE2B1();
            case EP20_KAUD:
                // постоянный
                if (acdc_ui == 1) {
                    return getE2B2() * K_;
                }
                // переменный
                else if (acdc_ui == 2) {
                    return getE2B1() * 0.0001 * K_TR_U * K_TR_I;
                }
            default: return 0;
        }
    }

    public double getEnConsumptionHelp() {
        switch (typeLoc) {
            case CHS7:
            case CHS7_EKS:
                return ((getE1B1() + getE1B2()) - (getE2B1() + getE2B2())) * K_;
            case CHS2_RED_FLAG:
            case CHS2K_USAVPP:
                return (getE1B1() + getE2B1()) * K_;
            case CHS4T_KAUD:
            case EP2K_KAUD:
                return getE1B1() + getE1Dop() * K_;
            case EP1_KAUD:
            case EP1_U:
            case EP1_P:
                return (getE1B1() - getE2B1() - getE1Dop()) * K_;
            case KZ4AT:
                return (getE1B1() - getE2B1() - getE1Dop()) ;
            case CHS8_KAUD:
                return ((getE1B1() + getE1B2()) - (getE1Dop() + getE2Dop())) * K_;
            case CHS7_KAUD_F:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case CHS6_KAUD:
            case CHS7_EKS2:
            case CHS7_KAUD_L:
                return (getE1B1() + getE1B2()) * K_;
            case EP20_KAUD:
                // постоянный
                if (acdc_ui == 1) {
                    return getE1B2() * K_;
                }
                // переменный
                else if (acdc_ui == 2) {
                    return (getE1B1() - getE1Dop()) * 0.0001 * K_TR_U * K_TR_I;
                }
            default: return 0;
        }
    }

    public double getEnConsumptionHeat() {
        switch (typeLoc) {
            case CHS7:
            case CHS7_EKS:
            case CHS7_KAUD_F:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case CHS6_KAUD:
            case CHS7_EKS2:
            case CHS7_KAUD_L:
                return (getE2B1() + getE2B2()) * K_;
            case CHS2_RED_FLAG:
            case CHS2K_USAVPP:
            case EP2K_KAUD:
                return getE2B1() * K_;
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case EP1_P:
                return getE1Dop() * K_;
            case KZ4AT:
                return getE1Dop();
            case CHS8_KAUD:
                return (getE1Dop() + getE2Dop()) * K_;
            case EP20_KAUD:
                // постоянный
                if (acdc_ui == 1) {
                    return getE2Dop() * K_ ;
                }
                // переменный
                else if (acdc_ui == 2) {
                    return getE1Dop() * 0.0001 * K_TR_U * K_TR_I;
                }
            default: return 0;
        }
    }

}
