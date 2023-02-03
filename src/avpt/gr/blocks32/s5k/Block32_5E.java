package avpt.gr.blocks32.s5k;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5E {

    private byte[] values;

    public Block32_5E(byte[] values) {
        this.values = values;
    }

    /**
     * @return Давление в ТМ - 1-я секция
     */
    public int getPressTM_1s() {
        return toUnsignedInt(values[0]) << 8 |    // hi
                toUnsignedInt(values[1]);         // lo
    }

    /**
     * @return Давление в УР - 1-я секция
     */
    public int getPressUR_1s() {
        return toUnsignedInt(values[2]) << 8 |    // hi
                toUnsignedInt(values[3]);         // lo
    }

    /**
     * @return Давление в ПМ - 1-я секция
     */
    public double getPressPM() {
        return (toUnsignedInt(values[4]) << 8 |    // hi
                toUnsignedInt(values[5])) * 0.0001389;         // lo
    }

    /**
     * @return Давление в ТМ - 2-я секция
     */
    public int getPressTM_2s() {
        return toUnsignedInt(values[6]) << 8 |    // hi
                toUnsignedInt(values[7]);         // lo
    }

    /**
     * @return Давление в УР - 2-я секция
     */
    public int getPressUR_2s() {
        return toUnsignedInt(values[8]) << 8 |    // hi
                toUnsignedInt(values[9]);         // lo
    }

    /**
     * @return Давление в ТЦ - 2-я секция
     */
    public int getPressTC_2s() {
        return toUnsignedInt(values[10]) << 8 |    // hi
                toUnsignedInt(values[11]);         // lo
    }

    /**
     * @return Скорость /10, км/ч локомотива от МСУД
     */
    public double getSpeedMSUD() {
        return (toUnsignedInt(values[12]) << 8 |    // hi
                toUnsignedInt(values[13])) / 10.0;         // lo
    }

    /**
     * @return Минимальная температура ТЭД
     */
    public long getMinTemperatureTED() {
        return (long) toUnsignedInt(values[14]) << 24 | // hi
                (long) toUnsignedInt(values[15]) << 16 |
                (long) toUnsignedInt(values[16]) << 8 |
                toUnsignedInt(values[17]);        // lo
    }

    /**
     * @return Максимальная температура ТЭД
     */
    public long getMaxTemperatureTED() {
        return (long) toUnsignedInt(values[18]) << 24 | // hi
                (long) toUnsignedInt(values[19]) << 16 |
                (long) toUnsignedInt(values[20]) << 8 |
                toUnsignedInt(values[21]);        // lo
    }

    /**
     * @return часов
     */
    private int getHour() {
        byte[] b = {values[23]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        byte[] b = {values[24]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        byte[] b = {values[25]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return время
     */
    public LocalTime getTime() throws DateTimeException {
        //return LocalTime.of(getHour(), getMinute(), getSecond());
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
    }

    /**
     * @return ТМ
     */
    public int getTM() {
        return (toUnsignedInt(values[26]) >>> 2) & 0x01;
    }

    /**
     * @return кнопка ПСТ
     */
    public int getKeyPst() {
        return (toUnsignedInt(values[28]) >>> 5) & 0x01;
    }

    /**
     *     132, 12   :  1-2
     *     144, 72   :  3-4
     *     208, 24   :  5-5a
     *     80, 16    :  6
     * @return Сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[28]) & 0xDC;
    }

    /**
     * @return Сигналы ккм
     */
    public String getKKMStr() {
        int val = getKKM();
        String result = "";
        switch (val) {
            case 132:
            case 12: result = "I-II";
                break;
            case 144:
            case 72: result = "III-IV";
                break;
            case 208:
            case 24: result = "V-Va";
                break;
            case 80:
            case 16: result = "VI";
        }
        return result;
    }
}
