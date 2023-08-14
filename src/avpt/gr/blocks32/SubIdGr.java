package avpt.gr.blocks32;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * для грзового движения и асим
 * предоставляет метод для определения подтипа для посылок с неоднозначным типом
 */
public class SubIdGr {

    /**
     * @param values - массив данных блока
     * @return - подтип для блока 0x21 (0x61)
     */
    private static int getSubId_0x21(byte[] values) {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;     // lo
    }

    /**
     * @param values - массив данных блока
     * @return - подтип для блоков 0x1D или 0x21
     */
    private static int getSubId_0x1D(byte[] values) {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     *
     * @param values - массив данных блока
     * @return подтип для блокав асим
     */
    private static int getSubId_ASIM(byte[] values) {
        return toUnsignedInt(values[28]);
    }

    /**
     * @param id - идентификатор посылки
     * @param values - данные посылки
     * @return - подтип для посылок с неоднозначным типом
     */
    public static int getSubId(int id, byte[] values) {
        if (id == 0x21) return getSubId_0x21(values);
        if (id == 0x1D) return getSubId_0x1D(values);
        if (id == 0x2D) return getSubId_0x1D(values);
        if (id == 0x6D) return getSubId_0x1D(values);
        if (id == 0x9D) return getSubId_0x1D(values);
        if (id == 0x71) return getSubId_0x1D(values);
        if (id == 0x44) return getSubId_0x1D(values);
        if (id >= 0xC0 && id <= 0xCF) return getSubId_ASIM(values);
        return 0;
    }

}
