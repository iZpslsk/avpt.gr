package avpt.gr.common;

public class Roads {

    private static final String WRONG = "неверный код дороги!";

    public static final int _ALL_0 = 0;
    public static final int _OCT_1 = 1;
    public static final int _KAL_10 = 10;
    public static final int _MOS_17 = 17;
    public static final int _GOR_24 = 24;
    public static final int _SEV_28 = 28;
    public static final int _SEV_KAV_51 = 51;
    public static final int _UG_VOS_58 = 58;
    public static final int _PRI_61 = 61;
    public static final int _KUY_63 = 63;
    public static final int _SVE_76 = 76;
    public static final int _UG_URAL_80 = 80;
    public static final int _ZAP_SIB_83 = 83;
    public static final int _KRA_88 = 88;
    public static final int _VO_SIB_92 = 92;
    public static final int _ZAB_94 = 94;
    public static final int _DAL_96 = 96;
    public static final int _SAH_99 = 99;
    public static final int _BEL_13 = 13;

    public static String getName(int code) {
        String result = WRONG;
        switch (code) {
            case _ALL_0:
                result = "Все дороги";
                break;
            case _OCT_1:
                result = "Октябрьская";
                break;
            case _KAL_10:
                result = "Калининградская";
                break;
            case _MOS_17:
                result = "Московская";
                break;
            case _GOR_24:
                result = "Горьковская";
                break;
            case _SEV_28:
                result = "Северная";
                break;
            case _SEV_KAV_51:
                result = "Сев.-Кавказская";
                break;
            case _UG_VOS_58:
                result = "Юго-Восточная";
                break;
            case _PRI_61:
                result = "Приволжская";
                break;
            case _KUY_63:
                result = "Куйбышевская";
                break;
            case _SVE_76:
                result = "Свердловская";
                break;
            case _UG_URAL_80:
                result = "Южно-Уральская";
                break;
            case _ZAP_SIB_83:
                result = "Зап.-Сибирская";
                break;
            case _KRA_88:
                result = "Красноярская";
                break;
            case _VO_SIB_92:
                result = "Вост.-Сибирская";
                break;
            case _ZAB_94:
                result = "Забайкальская";
                break;
            case _DAL_96:
                result = "Дальневосточная";
                break;
            case _SAH_99:
                result = "Сахалинская";
                break;
            case _BEL_13:
                result = "Белорусская";
                break;
        }
        return  result;
    }
}
