package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.*;

/**
 * (=) 0x20
 * (~) 0x60
 * (=~) 0x90
 */
public class Block32_20_60_90 {

    private final byte[] values;
    private final int typeLoc;

    // коэф. эп20
    public final static int K_TR_U_H = 30;
    public final static int K_TR_U = 250;
    public final static int  K_TR_I = 120;

    public Block32_20_60_90(int typeLoc, byte[] values) {
        this.values = values;
        this.typeLoc = typeLoc;
    }

    private int getValue_1() {
        return toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1]);
    }

    private int getValue_2() {
        return toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3]);
    }

    private int getValue_3() {
        return toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5]);
    }

    private int getValue_4() {
        return toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7]);
    }

    private int getValue_5() {
        return toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9]);
    }

    private int getValue_6() {
        return toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11]);
    }

    private int getValue_7() {
        return toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13]);
    }

    private int getValue_8() {
        return toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15]);
    }

    private int getValue_9() {
        return toUnsignedInt(values[16]) << 8 | toUnsignedInt(values[17]);
    }

    private int getValue_10() {
        return toUnsignedInt(values[18]) << 8 | toUnsignedInt(values[19]);
    }

    private int getValue_11() {
        return toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21]);
    }

    private int getValue_12() {
        return toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23]);
    }

    private int getValue_13() {
        return toUnsignedInt(values[24]) << 8 | toUnsignedInt(values[25]);
    }

    private int getValue_14() {
        return toUnsignedInt(values[26]) << 8 | toUnsignedInt(values[27]);
    }

    // -----------------  напряжение контактной сети  ---------------------------------------

    /**
     * @return напряжение контактной сети 1-я секция
     */
    public double getVoltage_s1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_KAUD_F:
            case CHS7_EKS2:
            case CHS7_KAUD_L:
                return getValue_11() * 1.235;
            case CHS8_KAUD:
                return getValue_11() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение контактной сети 2-я секция
     */
    public double getVoltage_s2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_KAUD_F:
            case CHS7_EKS2:
            case CHS7_KAUD_L:
                return getValue_12() * 1.235;
            case CHS8_KAUD:
                return getValue_12() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение контактной сети
     */
    public double getVoltage() {
        switch (typeLoc) {
            case CHS2_RED_FLAG:
            case CHS2_KAUD:
            case CHS2_EKS_2:
            case CHS2K_USAVPP:
            case CHS2K_KAUD:
            case CHS2K_ESAUP:
            case CHS2T_KAUD:
            case EP2K_KAUD:
            case KZ4AT:
                return getValue_11() * 1.235;
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
                return getValue_11() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение контактной сети (=) (ЭП20)
     */
    public double getVoltageDC() {
        if (typeLoc == EP20_KAUD) {
            return getValue_11() * 4500.0 / 65535;
        }
        else {
            return 0;
        }
    }

    /**
     * @return напряжение контактной сети (~) (ЭП20)
     */
    public double getVoltageAC() {
        if (typeLoc == EP20_KAUD) {
            return getValue_12() * 250.0 * 0.01;
        }
        else {
            return 0;
        }
    }

    // ------------------------- напряженме на двигателях -------------------------------
    /**
     * @return напряжение на двигателе 1 (чс2к, чс4т, эп2к)
     */
    public double getVoltageEngine1() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_8() * 0.4521;
            case CHS4T_KAUD:
                return getValue_12() == 0xFFFF ? 0 : getValue_12() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателе 2 (чс2к, эп2к)
     */
    public double getVoltageEngine2() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_9() * 0.173;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателе 3 (чс2к, эп2к)
     */
    public double getVoltageEngine3() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_10() * 0.22605;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателе 4 (чс2к, эп2к)
     */
    public double getVoltageEngine4() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_12() * 1.235;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателе 5 (чс2к, эп2к)
     */
    public double getVoltageEngine5() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_13() * 1.235;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателе 6 (чс2к, эп2к)
     */
    public double getVoltageEngine6() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case EP2K_KAUD:
                return getValue_14() * 1.235;
            case CHS4T_KAUD:
                return getValue_13() == 0xFFFF ? 0 : getValue_13() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателях 1-4 ()
     */
    public double getVoltageEngine1_4() {
        switch (typeLoc) {
            case CHS8_KAUD:
                return getValue_13() == 0xFFFF ? 0 : getValue_13() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return напряжение на двигателях 5-8 ()
     */
    public double getVoltageEngine5_8() {
        switch (typeLoc) {
            case CHS8_KAUD:
                return getValue_14() == 0xFFFF ? 0 : getValue_14() * 1.0;
            default: return 0;
        }
    }

    // --------------------------------------------------------

    /**
     * @return общий ток 1 секц (чс8, чс7, чс6)
     */
    public double getAmperage_s1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_3() * 0.4521;
            case CHS8_KAUD:
                return getValue_3() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return общий ток 2 секц (чс8, чс7, чс6)
     */
    public double getAmperage_s2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_8() * 0.4521;
            case CHS8_KAUD:
                return getValue_8() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return общий ток (чс2, чс4, эп1, эп2, kz4)
     */
    public double getAmperage() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case CHS2K_KAUD:
            case CHS2K_USAVPP:
            case CHS2_EKS_2:
            case CHS2_KAUD:
            case CHS2_RED_FLAG:
            case CHS2T_KAUD:
            case EP2K_KAUD:
                return getValue_3() * 0.4521;
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case KZ4AT:
                return getValue_3() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return общий ток (~) (эп20)
     */
    public double getAmperageAC() {
        if (typeLoc == EP20_KAUD) {
            return (short)getValue_8() * K_TR_I * 0.1;
        }
        else {
            return 0;
        }
    }

    /**
     * @return общий ток (=) (эп20)
     */
    public double getAmperageDC() {
        if (typeLoc == EP20_KAUD) {
            return (short)getValue_3() * 3000.0 / 32767;
        }
        else {
            return 0;
        }
    }

    // ----------------------------------------

    /**
     * @return Ток ТЭД 1-2  (чс7, чс6, чс2к)
     */
    public double getAmperageTed1_2_s1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_1() * 0.3014;
            case CHS2K_ESAUP:
            case CHS2K_KAUD:
            case CHS2K_USAVPP:
                return getValue_2() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток ТЭД 1-2 (чс7, чс6, чс2к)
     */
    public double getAmperageTed3_4_s1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_2() * 0.3014;
            case CHS2K_ESAUP:
            case CHS2K_KAUD:
            case CHS2K_USAVPP:
                return getValue_1() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток ТЭД 5-6  (чс7, чс6, чс2к)
     */
    public double getAmperageTed5_6_s2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_6() * 0.3014;
            case CHS2K_ESAUP:
            case CHS2K_KAUD:
            case CHS2K_USAVPP:
                return getValue_4() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток ТЭД 7-8  (чс7, чс6)
     */
    public double getAmperageTed7_8_s2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_7() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей СР,С,СПР,СП, ПР,П (чс2)
     */
    public double getAmperageTed_sr_s_spr_sp_pr_p() {
        switch (typeLoc) {
            case CHS2_EKS_2:
            case CHS2_KAUD:
            case CHS2_RED_FLAG:
                return getValue_1() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей СПР,СП,ПР,П (чс2)
     */
    public double getAmperageTed_spr_sp_pr_p() {
        switch (typeLoc) {
            case CHS2_EKS_2:
            case CHS2_KAUD:
            case CHS2_RED_FLAG:
                return getValue_2() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей ПР,П (чс2, чс2т)
     */
    public double getAmperageTed_pr_p() {
        switch (typeLoc) {
            case CHS2_EKS_2:
            case CHS2_KAUD:
            case CHS2_RED_FLAG:
            case CHS2T_KAUD:
                return getValue_4() * 0.173;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей СР,СПР,СП, ПР, П (чс2т)
     */
    public double getAmperageTed_sr_spr_sp_pr_p() {
        switch (typeLoc) {
            case CHS2T_KAUD:
                return getValue_1() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей С,СПР,СП,ПР,П (чс2т)
     */
    public double getAmperageTed_s_spr_sp_pr_p() {
        switch (typeLoc) {
            case CHS2T_KAUD:
                return getValue_2() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 1 (эп1)
     */
    public double getAmperageEngine1() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_1() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 2 (эп1)
     */
    public double getAmperageEngine2() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_2() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 3 (эп1)
     */
    public double getAmperageEngine3() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_4() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 4 (эп1)
     */
    public double getAmperageEngine4() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_6() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 5 (эп1)
     */
    public double getAmperageEngine5() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_7() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателя 6 (эп1)
     */
    public double getAmperageEngine6() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_9() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей 1-2 (эп2к)
     */
    public double getAmperageEngine1_2() {
        switch (typeLoc) {
            case EP2K_KAUD:
                return getValue_1() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей 3-4 (эп2к)
     */
    public double getAmperageEngine3_4() {
        switch (typeLoc) {
            case EP2K_KAUD:
                return getValue_2() * 0.3014;
            default: return 0;
        }
    }

    /**
     * @return Ток электродвигателей 5-6 (эп2к)
     */
    public double getAmperageEngine5_6() {
        switch (typeLoc) {
            case EP2K_KAUD:
                return getValue_4() * 0.173;
            default: return 0;
        }
    }

    // -------------------------------------------------------------

    /**
     * @return Якорный ток двигателя 2 (чс4)
     */
    public double getAmperageAnchor2() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_4() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Якорный ток двигателя 3 (чс4)
     */
    public double getAmperageAnchor3() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_8() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Якорный ток двигателя 4 (чс4, чс8)
     */
    public double getAmperageAnchor4() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_9() * 1.0;
            case CHS8_KAUD:
                return getValue_1() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Якорный ток двигателя 5 (чс4)
     */
    public double getAmperageAnchor5() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_10() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Якорный ток двигателя 6 (чс4)
     */
    public double getAmperageAnchor6() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_1() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Якорный ток двигателя 8 (чс8)
     */
    public double getAmperageAnchor8() {
        switch (typeLoc) {
            case CHS8_KAUD:
                return getValue_2() * 1.0;
            default: return 0;
        }
    }

    // --------------------------------------------------------------

    /**
     * @return Ток отопления 1 секц (чс7, чс6)
     */
    public double getAmperageHeat_s1() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_5() == 0xFFFF ? Double.NaN : getValue_5() * 0.22605;
            case CHS8_KAUD:
                return getValue_5() == 0xFFFF ? Double.NaN : getValue_5() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток отопления 2 секц (чс7, чс6)
     */
    public double getAmperageHeat_s2() {
        switch (typeLoc) {
            case CHS6_KAUD:
            case CHS7:
            case CHS7_EKS:
            case CHS7_EKS2:
            case CHS7_KAUD_F:
            case CHS7_KAUD_L:
                return getValue_10() == 0xFFFF ? Double.NaN : getValue_10() * 0.22605;
            case CHS8_KAUD:
                return getValue_10() == 0xFFFF ? Double.NaN : getValue_10() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток отопления (чс2, чс4, эп1, эп2, kz4)
     */
    public double getAmperageHeat() {
        switch (typeLoc) {
            case CHS2K_ESAUP:
            case CHS2K_KAUD:
            case CHS2K_USAVPP:
            case CHS2_EKS_2:
            case CHS2_KAUD:
            case CHS2_RED_FLAG:
            case CHS2T_KAUD:
            case EP2K_KAUD:
                return getValue_5() * 0.22605;
            case CHS4T_KAUD:
            case EP1_KAUD:
            case EP1_U:
            case KZ4AT:
                return getValue_5() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return Ток отопления (=) (эп 20)
     */
    public double getAmperageHeatDC() {
        switch (typeLoc) {
            case EP20_KAUD:
                return (short)getValue_5() * 750.0 /32767;
            default: return 0;
        }
    }

    /**
     * @return Ток отопления (~) (эп 20)
     */
    public double getAmperageHeatAC() {
        switch (typeLoc) {
            case EP20_KAUD:
                return (short)getValue_10() * K_TR_I * 0.1;
            default: return 0;
        }
    }

    // ----------------------------------------------------------

    /**
     * @return ток возбуждения двигателя 1 (чс4)
     */
    public double getAmperageExcitation1() {
        switch (typeLoc) {
            case CHS4T_KAUD:
                return getValue_2() * 1.0;
            case CHS8_KAUD:
                return getValue_6() * 1.0;
            default: return 0;
        }
    }

    /**
     * @return ток возбуждения двигателя (чс8)
     */
    public double getAmperageExcitation5() {
        switch (typeLoc) {
            case CHS8_KAUD:
                return getValue_7() * 1.0;
            default: return 0;
        }
    }

    // ---------------------------------------------------------

    /**
     * @return ток вспомогательных машин (эп2к)
     */
    public double getAmperageHelp() {
        switch (typeLoc) {
            case EP2K_KAUD:
                return getValue_6() * 0.3014;
            default: return 0;
        }
    }

    // ------------------------------------------------------

    /**
     * @return ток ЭПТ (эп1)
     */
    public double getAmperageEpt() {
        switch (typeLoc) {
            case EP1_KAUD:
            case EP1_U:
                return getValue_10() * 1.0;
            default: return 0;
        }
    }

    // ------------------------------------------------------

    /**
     * @return Сила тяги/торможения ТД-1, кН (эп20)
     */
    public double getPowerThrustBrake_1() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_1() < 500 ? getValue_1() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила тяги/торможения ТД-2, кН (эп20)
     */
    public double getPowerThrustBrake_2() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_2() < 500 ? getValue_2() : Double.NaN;
            default: return Double.NaN;
        }
    }


    /**
     * @return Сила тяги/торможения ТД-3, кН (эп20)
     */
    public double getPowerThrustBrake_3() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_4() < 500 ? getValue_4() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила тяги/торможения ТД-4, кН (эп20)
     */
    public double getPowerThrustBrake_4() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_6() < 500 ? getValue_6() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила тяги/торможения ТД-5, кН (эп20)
     */
    public double getPowerThrustBrake_5() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_7() < 500 ? getValue_7() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила тяги/торможения ТД-6, кН (эп20)
     */
    public double getPowerThrustBrake_6() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_9() < 500 ? getValue_9() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила тяги локомотива кН (эп20)
     */
    public double getPowerThrust() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_13() < 500 ? getValue_13() : Double.NaN;
            default: return Double.NaN;
        }
    }

    /**
     * @return Сила торможения локомотива кН (эп20)
     */
    public double getPowerBrake() {
        switch (typeLoc) {
            case EP20_KAUD:
                return getValue_14() < 500 ? getValue_14() : Double.NaN;
            default: return Double.NaN;
        }
    }

}
