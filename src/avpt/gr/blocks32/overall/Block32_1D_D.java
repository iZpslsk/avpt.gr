package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Редко меняющиеся параметры ИСАВП-РТ и виртуальной сцепки (ID = Dh)
 */
public class Block32_1D_D {

    private byte[] values;

    public Block32_1D_D(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * @return
     * сетевой адрес
     */
    public int getAddress() {
        return toUnsignedInt(values[2]) << 8 |      // hi
               toUnsignedInt(values[1]);            // lo
    }

    /**
     * @return Кол-во ведомых (1+)
     */
    public int getAmountSlave() {
        return toUnsignedInt(values[3])  >>> 4;
    }

    /**
     * @return № ведущего(1)/ведомого(2+)
     */
    public int getOrderNum() {
        return toUnsignedInt(values[3]) & 0x0F;
    }

    /**
     * @return Номер секции второго локомотива
     */
    public int getNumSecSlave() {
        return toUnsignedInt(values[4]) >>> 5;
    }

    /**
     * @return Код типа второго локомотива (аналогично посылке 61h)
     */
    public int getTypeSlave() {
        return toUnsignedInt(values[4]) & 0x1F;
    }

    /**
     * @return Номер второго электровоза
     */
    public int getNumSlave() {
        return toUnsignedInt(values[6]) << 8 |      // hi
                toUnsignedInt(values[5]);           // lo
    }

    /**
     * @return Тип локомотива по таблице АСОУП
     */
    public int getTypeASOUP() {
        return toUnsignedInt(values[8]) << 8 |      // hi
                toUnsignedInt(values[7]);           // lo
    }

    /**
     * •	2 – 26.03.2009 РТа - ВЛ10/ВЛ11/ВЛ80 5.3.0
     * •	3 – 26.03.2009 РТф - ВЛ80 5.3.1.76
     * •	5 – 12.12.2014 РТф - 3ЭС5К 5.3.1.75, 2ЭС5 5.3.1.79
     * •	6 – 21.08.2016 РТф - 2ЭС6 5.3.1.90
     * •	8 – 19.03.2018 РТф - 3ЭС5К 5.3.1.94/95, 2ЭС6 5.3.1.94
     * @return Версия протокола ИСАВП-РТ
     */
    public int getVerISAVPRT() {
        return toUnsignedInt(values[9]) & 0x1F;
    }

    /**
     * @return КСВ основного канала
     */
    public int getMainKSV() {
        return toUnsignedInt(values[10]) & 0x3F;
    }

    /**
     * @return КСВ дополнительного канала
     */
    public int getAdditionalKSV() {
        return toUnsignedInt(values[11]) & 0x3F;
    }

    /**
     * @return Связь с ВЭБР
     */
    public int getLinkVEBR() {
        return toUnsignedInt(values[10]) >>> 7;
    }

    /**
     * @return адапт
     */
    public int getAdapt() {
        return (toUnsignedInt(values[10]) >>> 6) & 0x01;
    }

    /**
     * @return Связь с допмодемом
     */
    public int getLinkAdditional() {
        return toUnsignedInt(values[11]) >>> 7;
    }

    /**
     * @return Допканал вкл.
     */
    public int getAdditionalChenOn() {
        return (toUnsignedInt(values[11]) >>> 6) & 0x01;
    }

    /**
     * @return Результат самодиагностики модема основного канала (0 – нет ошибок)
     */
    public int getSelfDiagnostic() {
        return toUnsignedInt(values[12]);
    }

}
