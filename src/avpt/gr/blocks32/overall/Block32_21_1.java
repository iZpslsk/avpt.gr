package avpt.gr.blocks32.overall;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * посылка инициализации УСАВП
 */
public class Block32_21_1 {

    private final byte[] values;

    public Block32_21_1(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;     // lo
    }

    /**
     * @return номер поезда
     */
    public int getNumTrain() {
        return (toUnsignedInt(values[25]) & 0x60) << 7 |     // hi   >>> 5) << 12
                (toUnsignedInt(values[0]) & 0x1F) << 7 |
                toUnsignedInt(values[1]) & 0x7F;             // lo
    }

    /**
     * @return Индекс серии локомотива
     */
    public int getTypeLoc() {
        return (toUnsignedInt(values[25]) & 0x03) << 2 |    // hi
                (toUnsignedInt(values[2]) & 0x60) >>> 5;     // lo
    }

    /**
     * @return Вес поезда
     */
    public int getWeight() {
        return (toUnsignedInt(values[4]) & 0x7F) << 7 |      // hi
                toUnsignedInt(values[5]) & 0x7F;            // lo
    }

    /**
     * 1: "с порожними вагонами"';
     * 2: "c порожними цистернами"';
     * else  '"общий"';
     *
     * @return тип состава
     */
    public int getTypeS() {
        return (toUnsignedInt(values[4]) & 0x80) >>> 7 |      // lo
                (toUnsignedInt(values[5]) & 0x80) >>> 6;            // hi
    }

    /**
     * @return Диаметр бандажа
     */
    public int getBandage() {
        return (toUnsignedInt(values[6]) & 0x78) << 4 |      // hi
                toUnsignedInt(values[7]) & 0x7F;            // lo
    }

    /**
     * @return уставка тока
     */
    public int getStAmperage() {
        return (toUnsignedInt(values[6]) & 0x07) << 7 |      // hi
                toUnsignedInt(values[8]) & 0x7F;            // lo
    }


    /**
     * @return Табельный номер машиниста
     */
    public long getTabNum() {
        return toUnsignedInt(values[26]) << 19 |                 // hi
                (toUnsignedInt(values[25]) & 0x1C) << 14 |        //        shr 2) shl 16
                (toUnsignedInt(values[9]) & 0x60) << 9 |          //          shr 5) shl 14
                (toUnsignedInt(values[18]) & 0x7F) << 7 |
                toUnsignedInt(values[19]) & 0x7F;               // lo
    }

    /**
     * @return Количество вагонов
     */
    public int getNumWags() {
        return (toUnsignedInt(values[9]) & 0x0F) << 4 |          // hi
                toUnsignedInt(values[10]) & 0x0F;               // lo
    }

    // секунд сначала суток
    private int getSecBeginDay() {
        return (toUnsignedInt(values[10]) & 0x70) << 10 |       // hi
                (toUnsignedInt(values[11]) & 0x7F) << 7 |
                toUnsignedInt(values[12]) & 0x7F;                // lo
    }

    // день
    private int getDay() {
        return (toUnsignedInt(values[16]) & 0x7F) >> 2;
    }

    // месяц
    private int getMonth() {
        return (toUnsignedInt(values[17]) & 0x7F) >> 3;
    }

    // год
    private int getYear() {
        return 2000 +
                ((toUnsignedInt(values[16]) & 0x03) << 3 |     // hi
                        toUnsignedInt(values[17]) & 0x07);           // lo
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

    // время
    public LocalTime getTime() throws DateTimeException {
        return UtilsArmG.getTime(getSecBeginDay());
    }

    /**
     * @return дата и время
     */
    public LocalDateTime getDateTime() {
        if (getDate().getYear() > 2005)
            return UtilsArmG.getDateTime(getDate(), getSecBeginDay());
        else
            return null;
    }

    public LocalDateTime getDateTime(LocalDate date) {
        return UtilsArmG.getDateTime(date, getSecBeginDay());
    }

    /**
     * @return координата УСАВП
     */
    public long getCoordinate() {
        return (toUnsignedInt(values[13]) & 0x3F) << 14 |            // hi
                (toUnsignedInt(values[14]) & 0x7F) << 7 |
                toUnsignedInt(values[15]) & 0x7F;                    // lo
    }

    // день карты
    private int getDayMap() {
        return (toUnsignedInt(values[20]) & 0x7F) >> 2;
    }

    // месяц карты
    private int getMonthMap() {
        return (toUnsignedInt(values[21]) & 0x7F) >> 3;
    }

    // год карты
    private int getYearMap() {
        return 2000 +
                ((toUnsignedInt(values[20]) & 0x03) << 3 |     // hi
                        toUnsignedInt(values[21]) & 0x07);           // lo
    }

    // дата
    public LocalDate getDateMap() throws DateTimeException {
        int year = getYearMap();
        int month = getMonthMap();
        int day = getDayMap();
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
     * case arrTrains[indx].Status_isavprt of
     * 0:  begin
     * if IsBHV_SUTP(indx) then
     * Result := 'поезд с БХВ'
     * else
     * Result := 'одиночный';
     * end;
     * 1:  Result := 'ведущий';
     * 2:  Result := 'ведомый';
     * 4:  Result := 'виртуальная сцепка';
     * else
     * Result := '';
     * end;
     *
     * @return статус ИСАВПРТ  (ведущ/ведом/один)
     */
    public int getStatusIsavprt() {
        return toUnsignedInt(values[23]) >>> 5;
    }

    /**
     * @return Количество условных вагонов
     */
    public int getNumConditionalWags() {
        return (toUnsignedInt(values[28]) & 0x07) << 5 |          // hi
                toUnsignedInt(values[27]) & 0x1F;               // lo
    }

    /**
     * @return число секций
     */
    public int getCntSections() {
        return (toUnsignedInt(values[28]) & 0x80) >>> 5 |            // hi
                (toUnsignedInt(values[28]) & 0x1F) >>> 3;             // lo
    }

}
