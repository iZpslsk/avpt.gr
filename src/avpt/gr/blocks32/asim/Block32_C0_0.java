package avpt.gr.blocks32.asim;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * инициализация АСИМ, подтип - 0  (Block32_21_1)
 */
public class Block32_C0_0 {

    private final byte[] values;

    public Block32_C0_0(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[28]);
    }

    /**
     * 1 - электровоз
     * 2 - тепловоз
     * @return - тип тяги
     */
    public int getTypeTract() {
        return toUnsignedInt(values[0]) >>> 5;
    }

    /**
     * 1 - грузовой
     * 2 - пассажирский
     * @return - тип движения
     */
    public int getTypeMove() {
        return (toUnsignedInt(values[0]) & 0x1F) >>> 2;
    }

    /**
     * 1- постоянка
     * 2 - переменка
     * 3 - тепловоз
     * @return - тип питания
     */
    public int getTypeACDC() {
        return toUnsignedInt(values[0]) & 0x03;
    }

    /**
     * 16 ВЛ11
     * 17 ВЛ15
     * 20 ВЛ85
     * 32 ВЛ10
     * 46 ВЛ11К
     * 51 ВЛ80С
     * 52 ВЛ80Т
     * 53 ВЛ80ТК
     * 54 ВЛ80Р
     * 988 ВЛ80СК
     * 989 ВЛ10УК
     * 995 ВЛ10К
     * 996 ВЛ10У
     * 997 ВЛ11М
     * @return Индекс серии локомотива
     */
    public int getTypeLoc() {
        return  toUnsignedInt(values[23]) << 8  |    // hi
                toUnsignedInt(values[1]);            // lo
    }

    /**
     * @return номер локототива
     */
    public long getLocNum() {
        return (long) toUnsignedInt(values[2]) << 16 |    // hi
               (long) toUnsignedInt(values[3]) << 8 |
                      toUnsignedInt(values[4]);           // lo
    }

    /**
     * @return номер поезда
     */
    public int getNumTrain() {
        return toUnsignedInt(values[5]) << 24 |     // hi
               toUnsignedInt(values[6]) << 16 |
               toUnsignedInt(values[7]) << 8  |
               toUnsignedInt(values[8]);            // lo
    }

    /**
     * @return Вес поезда (T)
     */
    public int getWeight() {
        return toUnsignedInt(values[9]) << 8 | // hi
               toUnsignedInt(values[10]);      // lo
    }

    /**
     * @return Количество вагонов
     */
    public int getNumWags() {
        return toUnsignedInt(values[11]);
    }

    /**
     * @return Табельный номер машиниста
     */
    public long getTabNum() {
        return (long) toUnsignedInt(values[12]) << 24 |        // hi
               (long) toUnsignedInt(values[13]) << 16 |       //
               (long) toUnsignedInt(values[14]) << 8 |        //
                      toUnsignedInt(values[15]);              // lo
    }


    // день
    private int getDay() {
        return toUnsignedInt(values[19]);
    }

    // месяц
    private int getMonth() {
        return toUnsignedInt(values[20]);
    }

    // год
    private int getYear() {
        return 2000 + toUnsignedInt(values[21]);
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
        return toUnsignedInt(values[16]);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        return toUnsignedInt(values[17]);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        return toUnsignedInt(values[18]);
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

    /**
     * @return номер текущей секции
     */
    public int getNumSection() {
        return toUnsignedInt(values[22]);
    }

    /**
     * @return - счетчик принятых расписаний
     */
    public int getCntSchedule() {
        return toUnsignedInt(values[24]);
    }

}
