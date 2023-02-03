package avpt.gr.blocks32.overall;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.*;

public class Block32_16_56 {

    private byte[] values;

    public Block32_16_56(byte[] values) {
        this.values = values;
    }

    /**
     * Признак включения питания
     * @return 1 – БР только что включился, 0 – питание уже включено
     */
    public int getSignPower() {
        return toUnsignedInt(values[1]) & 0x01;
    }

    /**
     * @return Заводской номер
     * БР
     */
    public long getNumFactoryBR() {
        return (long) toUnsignedInt(values[3]) << 24 | // hi
                (long) toUnsignedInt(values[4]) << 16 |
                (long) toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[6]);        // lo
    }

    /**
     * @return Версия ПО БР
     */
    public String getVerBR() {
        char[] arrCh = new char[4];
        arrCh[0] = values[11] > 32 && values[11] < 127 ? (char)values[11] : '?';
        arrCh[1] = values[12] > 32 && values[12] < 127 ? (char)values[12] : '?';//(char)values[12];
        arrCh[2] = values[13] > 32 && values[13] < 127 ? (char)values[13] : '?';//(char)values[13];
        arrCh[3] = values[14] > 32 && values[14] < 127 ? (char)values[14] : '?';//(char)values[14];
        return new String(arrCh);
    }

    /**
     * @return Номер  электровоза (номер локототива)
     */
    public long getLocNum() {
        return (long) toUnsignedInt(values[15]) << 16 |    // hi
                (long) toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[17]);           // lo
    }

    /**
     * @return номер секции
     */
    public int getSectNum() {
        return toUnsignedInt(values[18]);
    }

    /**
     * @return Тип локомотива по таблице АСОУП
     */
    public int getLocTypeASOUP() {
        return  toUnsignedInt(values[19]) << 8 | toUnsignedInt(values[20]);
    }

    /**
     * @return дата
     */
    private int getDay() {
        byte[] b = {values[24]};
        return (int)UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return месяц
     */
    private int getMonth() {
        byte[] b = {values[25]};
        return (int)UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return год
     */
    private int getYear() {
        byte[] b = {values[26]};
        int result = (int)UtilsArmG.BCDToDecimal(b);
        return 2000 + result;
    }

    /**
     * @return дата инициализации
     * @throws DateTimeException -
     */
    public LocalDate getDateInit() throws DateTimeException {
        int year = getYear();
        int month = getMonth();
        int day = getDay();
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return null;// LocalDate.ofEpochDay(0);
        }

//        if (isDateWrong(year, month, day))
//            return  null;
//        else
//            return LocalDate.of(getYear(), getMonth(), getDay());
    }

    /**
     * @return код типа локомотива
     */
    public int getTypeLoc() {
        int codeAsoup = getLocTypeASOUP();
        switch(codeAsoup) {
            case 123:
            case 138: return VL10;
            case 134: return VL11;
            case 240: return VL80;
            case 244: return VL85;
            case 220:
            case 222:
            case 253: return S5K;
            case 145: return S6;
            case 144:
            case 115: return S4K;
        }
        return -1;
        //return toUnsignedInt(values[27]) & 0x0F;
    }

}
