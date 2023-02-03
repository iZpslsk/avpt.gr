package avpt.gr.blocks32.s4k;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5E {

    private final byte[] values;

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
        return (toUnsignedInt(values[13]) << 8 |    // hi
                toUnsignedInt(values[12])) / 10.0;         // lo
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
     //   return LocalTime.of(getHour(), getMinute(), getSecond());
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
     * @return кнопка ПСТ
     */
    public int getKeyPst() {
        return (toUnsignedInt(values[26]) >>> 5) & 0x01;
    }

    /**
     * @return 1 = Кнопка экстренного торможения (помощника)
     */
    public int getEmergencyBrake() {
        return (toUnsignedInt(values[26]) >>> 6) & 0x01;
    }

    /**
     * @return ТМ
     */
    public int getTM() {
        return (toUnsignedInt(values[26]) >>> 7) & 0x01;
    }

    /**
     *     3 :  1-2
     *     5 :  3-4
     *     9 :  5-5a
     *     12 : 6
     * @return Сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[26]) & 0x0F;
    }
}
