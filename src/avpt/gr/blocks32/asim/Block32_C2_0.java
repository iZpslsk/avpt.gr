package avpt.gr.blocks32.asim;

import avpt.gr.chart_dataset.TaskAlsn;
import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_C2_0 {

    private final byte[] values;

    public Block32_C2_0(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * @return Давление в тормозных цилиндрах
     */
    public double getPressTC() {
        return toUnsignedInt(values[0]) << 8 |    // hi
               toUnsignedInt(values[1]);          // lo
    }

    /**
     * @return Давление в ТМ
     */
    public int getPressTM() {
        return toUnsignedInt(values[2]) << 8 |    // hi
                toUnsignedInt(values[3]);         // lo
    }

    /**
     * @return Давление в УР
     */
    public double getPressUR() {
        return toUnsignedInt(values[4]) << 8 |    // hi
                toUnsignedInt(values[5]);         // lo
    }

    /**
     * @return скорость расчетная
     */
    public int getSpeedCalc() {
        return toUnsignedInt(values[6]);
    }

    /**
     * @return скорость
     */
    public int getSpeed() {
        return toUnsignedInt(values[7]) == 0xFF ? 0 : toUnsignedInt(values[7]);
    }

    /**
     * @return скорость
     */
    public int getSpeedGPS() {
        return toUnsignedInt(values[8]);
    }

    /**
     * Белый 		0
     * Красный		1
     * Красно-жёлтый 	2
     * Жёлтый		3
     * Зелёный		4
     * Отсутствует		255
     * @return АЛСН
     */
    public int getALSN() {
        return toUnsignedInt(values[9]) + TaskAlsn.SHIFT_ASIM;
    }

    /**
     * @return - тек ограничение скорости
     */
    public int getSpeedLimit() {
        return toUnsignedInt(values[12]);
    }

    /**
     * @return - тип ограниченмя скорости
     */
    public int getTypeSpeedLimit() {
        return toUnsignedInt(values[13]);
    }

    /**
     * @return - счетчик принятых сообщений ЕСМ БС
     */
    public int cntMessEsmBs() {
        return toUnsignedInt(values[14]);
    }

    /**
     * @return координата АСИМ (m)
     */
    public long getCoordinate() {
        return (long) toUnsignedInt(values[16]) << 16 |    // hi
               (long) toUnsignedInt(values[17]) << 8 |
                toUnsignedInt(values[18]);           // lo
    }

    /**
     * @return текущий км
     */
    public int getKm() {
        return toUnsignedInt(values[19]) << 8  |  // hi
                toUnsignedInt(values[20]);         // lo
    }

    /**
     * @return текущий пикет
     */
    public int getPicket() {
        return toUnsignedInt(values[21]);
    }



    // день
    private int getDay() {
        return toUnsignedInt(values[25]);
    }

    // месяц
    private int getMonth() {
        return toUnsignedInt(values[26]);
    }

    // год
    private int getYear() {
        return 2000 + toUnsignedInt(values[27]);
    }

    // дата
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
     * @return часов
     */
    private int getHour() {
        return toUnsignedInt(values[22]);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        return toUnsignedInt(values[23]);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        return toUnsignedInt(values[24]);
    }

    /**
     * @return время
     */
    public LocalTime getTime() throws DateTimeException {
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
    }

    /**
     * @return дата и время
     */
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(getDate(), getTime());
    }

    public LocalDateTime getDateTime(LocalDate date) {
        return LocalDateTime.of(date, getTime());
    }

}
