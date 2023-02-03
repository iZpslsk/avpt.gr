package avpt.gr.blocks32.overall;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Данные КЛУБ-У
 */
public class Block32_21_E {

    private final byte[] values;

    public Block32_21_E(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;    // lo
    }

    /**
     * @return год
     */
    private int getYear() {
        return toUnsignedInt(values[1]) << 8 |    // hi
               toUnsignedInt(values[2]);    // lo
    }

    /**
     * @return месяц
     */
    private int getMonth() {
        return toUnsignedInt(values[3]);
    }

    /**
     * @return день
     */
    private int getDay() {
        return toUnsignedInt(values[4]);
    }

    /**
     * @return дата
     */
    public LocalDate getDate() throws DateTimeException {
        int year = getYear();
        int month = getMonth();
        int day = getDay();
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return null;// LocalDate.ofEpochDay(0);
        }
//        if (isDateWrong(year, month, day))
//            return null;
//        else
//            return LocalDate.of(getYear(), getMonth(), getDay());
    }

    /**
     * @return час
     */
    private int getHour() {
        return toUnsignedInt(values[5]);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        return toUnsignedInt(values[6]);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        return toUnsignedInt(values[7]);
    }

    /**
     * @return время
     * @throws DateTimeException -
     */
    public LocalTime getTime() throws DateTimeException {
        //return LocalTime.of(getHour(), getMinute(), getSecond());
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
     * @return дата время
     * @throws DateTimeException -
     */
    public LocalDateTime getDateTime() throws DateTimeException {
        return LocalDateTime.of(getDate(), getTime());
    }

    /**
     * @return номер выполненного теста
     */
    public int getNumTest() {
        return toUnsignedInt(values[8]) >>> 4;
    }

    /**
     * @return результат теста
     */
    public int getResultTest() {
        return toUnsignedInt(values[8]) & 0x0F;
    }

    /**
     * @return направление 0 - вперед, 1 - назад
     */
    public int getDirection() {
        return toUnsignedInt(values[9]) >> 7;
    }

    /**
     * @return ключ эпк кабины 2 0 – выкл, 1 – вкл
     */
    public int getEPK_2() {
        return (toUnsignedInt(values[9]) >> 5) & 0x01;
    }

    /**
     * @return ключ эпк кабины 1 0 – выкл, 1 – вкл
     */
    public int getEPK_1() {
        return (toUnsignedInt(values[9]) >> 4) & 0x01;
    }

    /**
     * @return состояние рукоятки РБ 0 – не нажата, 1 – нажата.
     */
    public int getStateHandleRB() {
        return (toUnsignedInt(values[9]) >> 3) & 0x01;
    }

    /**
     * @return состояние рукоятки РБС 0 – не нажата, 1 – нажата.
     */
    public int getStateHandleRBS() {
        return (toUnsignedInt(values[9]) >> 2) & 0x01;
    }

    /**
     * @return состояние ЭПК 0 – без тока, 1 – под током
     */
    public int getStateEPK() {
        return (toUnsignedInt(values[9]) >> 1) & 0x01;
    }

    /**
     * @return включение предварительной сигнализации
     */
    public int getPreSignalingOn() {
        return toUnsignedInt(values[9]) & 0x01;
    }

    /**
     * @return фактическая скорость
     */
    public int getSpeed() {
        return (toUnsignedInt(values[9]) & 0x40) << 2 |      // hi
                toUnsignedInt(values[10]);                   // lo
    }

    /**
     * @return допустимая скорость
     */
    public int getPermissibleSpeed() {
        return (toUnsignedInt(values[11]) & 0x80) << 1 |     // hi
                toUnsignedInt(values[12]);                   // lo
    }

    /**
     * @return целевая скорость
     */
    public int getTargetSpeed() {
        return (toUnsignedInt(values[11]) & 0x40) << 2 |     // hi
                toUnsignedInt(values[13]);                   // lo
    }

    /**
     * 0 – Б,            А – КЖ (по АЛС-ЕН)
     * 1 – К,            B – 1 б/у
     * 2 – КЖ,         С – 2 б/у
     * 3 – Ж,           D – 3 б/у
     * 4 – З,            E – 4 б/у
     * 5 – БМ,         F – 5 б/у
     * 9 – К (по АЛС-ЕН)
     * @return код светофора
     */
    public int getTrafficLight() {
        return toUnsignedInt(values[14]) >>> 4;
    }

    /**
     * @return вид цели
     */
    public int getKindTarget() {
        return toUnsignedInt(values[14]) & 0x0F;
    }

    /**
     * @return расстояние до цели
     */
    public int getDistanceTarget() {
        return (toUnsignedInt(values[11]) & 0x1F) << 8 |      // hi
                toUnsignedInt(values[15]);                    // lo
    }

    /**
     * @return широта
     */
    public long getLatitude() {
        return (long) toUnsignedInt(values[16]) << 24 | // hi
                (long) toUnsignedInt(values[17]) << 16 |
                (long) toUnsignedInt(values[18]) << 8 |
               toUnsignedInt(values[19]);        // lo
    }

    public double getGpsLat() {
        return UtilsArmG.getGps(getLatitude());
    }

    /**
     * @return долгота
     */
    public long getLongitude() {
        return (toUnsignedInt(values[20]) & 0x7F) << 24 | // hi
                (long) toUnsignedInt(values[21]) << 16 |
                (long) toUnsignedInt(values[22]) << 8 |
                toUnsignedInt(values[23]);        // lo
    }

    public double getGpsLon() {
        return UtilsArmG.getGps(getLongitude());
    }

    /**
     * 0 – данные достоверны, 1 – данные недостоверны
     * @return признак недостоверности данных GPS
     */
    public int getInvalidGPS() {
        return toUnsignedInt(values[20]) >>> 7;
    }

    /**
     * @return Граница б/у АЛС-ЕН
     */
    public int getBorderALSNEN() {
        return (toUnsignedInt(values[24]) >>> 4) & 0x01;
    }

    /**
     * @return Сигнал АЛСН-ЕН
     */
    public int getSignalALSNEN() {
        return (toUnsignedInt(values[24]) >>> 3) & 0x01;
    }

    /**
     * @return Сигнал АЛСН
     */
    public int getSignalALSN() {
        return (toUnsignedInt(values[24]) >>> 2) & 0x01;
    }

    /**
     * @return КПТ7
     */
    public int getKPT7() {
        return (toUnsignedInt(values[24]) >>> 1) & 0x01;
    }

    /**
     * @return КПТ5
     */
    public int getKPT5() {
        return toUnsignedInt(values[24]) & 0x01;
    }
}
