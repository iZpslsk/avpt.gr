package avpt.gr.blocks32.s5k_2;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_52 {

    private final byte[] values;

    public Block32_52(byte[] values) {
        this.values = values;
    }

    /**
     * @return часов
     */
    private int getHour() {
        byte[] b = {values[0]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        byte[] b = {values[1]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        byte[] b = {values[2]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return время
     */
    public LocalTime getTime() throws DateTimeException {
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
//        if (sec > 59 || min > 59 || hour > 23)
//            return null;
//        else
//            return LocalTime.of(hour, min, sec);
    }

    /**
     * @return Давление в ТМ - 1-я секция
     */
    public int getPressTM() {
        return toUnsignedInt(values[4]) << 8 |    // hi
                toUnsignedInt(values[3]);         // lo
    }



    /**
     * @return Давление в ПМ если ведущая кабина 1 или давление в ТЦ если кабина 2,
     */
    public double getPressPMTC() {
        return toUnsignedInt(values[8]) << 8 |    // hi
                toUnsignedInt(values[7]);          // lo
    }

    /**
     *     3   :  1-2
     *     5   :  3-4
     *     9   :  5-5a
     *     8   :  6
     * @return Сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[9]) & 0x0F;
    }

    /**
     * @return кнопка ПСТ
     */
    public int getKeyPst() {
        return (toUnsignedInt(values[9]) >>> 4) & 0x01;
    }

    /**
     * @return кнопка ЭТ
     */
    public int getKeyET() {
        return (toUnsignedInt(values[9]) >>> 5) & 0x01;
    }

    /**
     * @return ДДР - ТМ
     */
    public int getKeyDDR_TM() {
        return (toUnsignedInt(values[9]) >>> 6) & 0x01;
    }

    /**
     * @return ДТЦ - ТМ
     */
    public int getKeyDTC_TM() {
        return (toUnsignedInt(values[9]) >>> 7) & 0x01;
    }

}
