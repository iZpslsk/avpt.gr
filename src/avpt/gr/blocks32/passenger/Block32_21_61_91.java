package avpt.gr.blocks32.passenger;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * (=) 0x21
 * (~) 0x61
 * (=~) 0x91
 */

public class Block32_21_61_91 {

    private final byte[] values;

    public Block32_21_61_91(byte[] values) {
        this.values = values;
    }

    public static int BANDAGE_CONST = 1100;

    /**
     * @return Оставшийся путь до зонной станции в м.
     */
    public int getRest() {
        if (getVersion() == 1)
            return  (toUnsignedInt(values[0]) & 0x1F) << 16 | toUnsignedInt(values[1]) << 8 | toUnsignedInt(values[2]);
        else
            return 0;
    }

    /**
     * @return Оставшийся путь до зонной станции в ПК.
     */
    public int getRestPK() {
        if (getVersion() == 0)
            return  (toUnsignedInt(values[0]) & 0x1F) << 8 | toUnsignedInt(values[1]);
        else
            return 0;
    }

    /**
     * @return номер поезда
     */
    public int getNumTrain() {
        if (getVersion() == 1)
            return toUnsignedInt(values[3]) | (toUnsignedInt(values[4]) << 8);
        else
            return toUnsignedInt(values[3]) | (toUnsignedInt(values[2]) << 8);
    }

    /**
     * @return Диаметр бандажа
     */
    public int getBandage() {
        if (getVersion() == 1)
            return toUnsignedInt(values[5]) + BANDAGE_CONST;
        else
            return toUnsignedInt(values[5]) | (toUnsignedInt(values[4]) & 0x0F) << 8 ;
    }

    /**
     * 0 – постоянное ограничение
     * 1 – временное ограничение
     * 2 – оперативное ограничение (задано машинистом)
     * 3 – ограничение задано системой САУТ
     * 4 – ограничение задано системой КЛУБ-У
     * 5..7 - резерв
     * @return Тип текущего ограничения скорости Vmax
     */
    public int getTypeCurVal() {
        if (getVersion() == 1)
            return toUnsignedInt(values[6]) & 0x07;
        else
            return (toUnsignedInt(values[22]) >>> 6) & 0x01;
    }

    /**
     * Для многомаршрутных баз – номер маршрута движения (1..7).
     * Для баз с единственным маршрутом поле содержит «0»
     * @return Номер маршрута
     */
    public int getNumRoute() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[6]) >>> 3) & 0x07;
        else
            return 0;
    }

    /**
     * 0 – автоведение
     * 1 – советчик
     * 2 – безопасность
     * 3 – промежуточный
     * @return Режим монитора управления
     */
    public int getModeMonitorControl() {
        return (toUnsignedInt(values[6]) >>> 6) & 0x03;
    }

    /**
     * @return 0 – автоведение 1 – советчик
     */
    public int getStatusSavpe() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[6]) >>> 6)  & 0x01;
        else
            return (toUnsignedInt(values[28]) & 0x18) >>> 3;
    }

    /**
     * @return табельный номер
     */
    public int getNumTab() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[22]) & 0x40) << 10  |  // hi
                    toUnsignedInt(values[8]) << 8 |
                    toUnsignedInt(values[7]);          // lo
        else
            return toUnsignedInt(values[6]) << 8 |
                    toUnsignedInt(values[7]);
    }

    /**
     * @return Координата  ближ. ВО, км
     */
    public int getLimTempKm() {
        if (getVersion() == 1)
            return toUnsignedInt(values[10]) << 8 |
                    toUnsignedInt(values[9]);
        else
            return toUnsignedInt(values[8]) << 8 |
                    toUnsignedInt(values[9]);
    }

    /**
     * @return Координата  ближ. ВО, пк
     */
    public int getLimTempPK() {
        if (getVersion() == 1)
            return toUnsignedInt(values[11]);
        else
            return toUnsignedInt(values[20]) & 0x0F;
    }

    /**
     * @return огр. скорости на ближайшем ВО
     */
    public int getLimTempVal() {
        if (getVersion() == 1)
            return toUnsignedInt(values[12]);
        else
            return (toUnsignedInt(values[16]) & 0x01) << 8 |
                    toUnsignedInt(values[17]);

    }

    /**
     * @return Текущее ограничение скорости, км/ч
     */
    public int getLimCurVal() {
        if (getVersion() == 1)
            return toUnsignedInt(values[13]);
        else
            return (toUnsignedInt(values[18]) & 0x01) << 8 |
                    toUnsignedInt(values[19]);
    }

    /**
     * @return текущая скорость, км/ч
     */
    public int getSpeed() {
        if (getVersion() == 1)
            return toUnsignedInt(values[14]);
        else
            return (toUnsignedInt(values[12]) & 0x01) << 8 |
                    toUnsignedInt(values[13]);

    }

    /**
     * @return количество вагонов
     */
    public int getCountWags() {
        if (getVersion() == 1)
            return toUnsignedInt(values[15]) & 0x1F;
        else
            return (toUnsignedInt(values[14]) & 0x01) << 8 | toUnsignedInt(values[15]);
    }

    /**
     * @return Номер пути
     */
    public int getNumLine() {
        if (getVersion() == 1)
            return toUnsignedInt(values[15]) >>> 5;
        else
            return (toUnsignedInt(values[20]) & 0x70) >>> 4;
    }

    /**
     * @return Тек. номер перегона по ББД
     */
    public int getNumStage() {
        if (getVersion() == 1)
            return toUnsignedInt(values[16]);
        else
            return (toUnsignedInt(values[10]) & 0x01) << 8 |
                    toUnsignedInt(values[11]);
    }

    final int k0_press = 16;
    final int k1_press = 4;

    /**
     * @return Давление в тормозных цилиндрах
     */
    public double getPressTC() {
        if (getVersion() == 1) {
            return toUnsignedInt(values[17]) * 0.01 * k1_press;
        }
        else {
            if (((toUnsignedInt(values[16]) >>> 5) & 0x03) == 0)
                return (toUnsignedInt(values[16]) >>> 1) * 0.01 * k0_press;
            else
                return (toUnsignedInt(values[16]) >>> 1) * 0.01;
        }
    }

    /**
     * @return Давление в УР
     */
    public double getPressUR() {
        if (getVersion() == 1) {
            return toUnsignedInt(values[18]) * 0.01 * k1_press;
        }
        else {
            if (((toUnsignedInt(values[16]) >>> 5) & 0x03) == 0)
                return (toUnsignedInt(values[12]) >>> 1) * 0.01 * k0_press;
            else
                return (toUnsignedInt(values[12]) >>> 1) * 0.01;
        }
    }

    /**
     * @return Давление в  ЗТС
     */
    public double getPressZTC() {
        if (getVersion() == 1) {
            return toUnsignedInt(values[19]) * 0.01 * k1_press;
        }
        else {
            if (((toUnsignedInt(values[16]) >>> 5) & 0x03) == 0)
                return (toUnsignedInt(values[18]) >>> 1) * 0.01 * k0_press;
            else
                return (toUnsignedInt(values[18]) >>> 1) * 0.01;
        }
    }

    /**
     * @return позиция контроллера
     */
    public int getPosition() {
        if (getVersion() == 1)
            return toUnsignedInt(values[20]);
        else
            return toUnsignedInt(values[24]);
    }

    /**
     * Белый 		    8
     * Красный		    16
     * Красно-жёлтый    1
     * Жёлтый		    2
     * Зелёный		    4
     * @return АЛСН-УСАВПП
     */
    public int getALSN() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[21]) >>> 3) & 0x1F;
        else {
            return (toUnsignedInt(values[21]) & 0x40) >>> 2 | (toUnsignedInt(values[25]) & 0x78) >>> 3;
        }
    }

    /**
     * @return ослабление поля
     */
    public int getWeakField() {
        if (getVersion() == 1)
            return toUnsignedInt(values[21]) & 0x07;
        else
            return toUnsignedInt(values[25]) & 0x07;
    }

    /**
     * @return тип тормозов
     */
    public int getTypeBrake() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[22]) >>> 4) & 0x03;
        else
            return (toUnsignedInt(values[26]) >>> 5) & 0x03;
    }

    /**
     0	белый
     1	красный
     2	КЖ
     3	жёлтый
     4	зеленый
     5	белый мигающий
     6	резерв
     7	резерв
     8	резерв
     9	красный АЛС-ЕН
     10	КЖ АЛС-ЕН
     11	1 свободный б.у. (желтый) АЛС-ЕН
     12	2 свободных б.у. (1 зеленый) АЛС-ЕН
     13	3 свободных б.у. (2 зеленых) АЛС-ЕН
     14	4 свободных б.у. (3 зеленых) АЛС-ЕН
     15	5 свободных б.у. (4 зеленых) АЛС-ЕН
     * @return АЛСН КЛУБ
     */
    public int getALSN_club() {
        return toUnsignedInt(values[22]) & 0x0F;
    }

    /**
     * @return «1» - контрольный пост 5 сек
     */
    public int getControlPost() {
        return (toUnsignedInt(values[22]) >>> 7) & 0x01;
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        return toUnsignedInt(values[23]) & 0x3F;
    }

    /**
     * @return минут
     */
    private int getMinute() {
        if (getVersion() == 1)
            return toUnsignedInt(values[23]) >>> 6 | (toUnsignedInt(values[24]) & 0x0F) << 2 ;
        else
            return toUnsignedInt(values[22]) & 0x3F;

    }

    /**
     * @return часов
     */
    private int getHour() {
        if (getVersion() == 1)
            return toUnsignedInt(values[24]) >>> 4 | (toUnsignedInt(values[25]) & 0x01) << 4;
        else
            return toUnsignedInt(values[21]) & 0x1F;
    }

    /**
     * @return день
     */
    private int getDay() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[25]) >>> 1) & 0x1F;
        else
            return (toUnsignedInt(values[10]) >>> 1) & 0x3F;
    }

    /**
     * @return месяц
     */
    private int getMonth() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[26]) & 0x03) << 2 | toUnsignedInt(values[25]) >>> 6;
        else
            return (toUnsignedInt(values[14]) >>> 1) & 0x1F;
    }

    /**
     * @return год
     */
    private int getYear() {
        if (getVersion() == 1)
            return (toUnsignedInt(values[26]) >>> 2) + 2000;
        else
            return (toUnsignedInt(values[4]) >>> 4) & 0x07 + 2000;
    }

    /**
     * «1» - Тумблер «Выходные цепи» включен
     * @return Выходные Цепи
     */
    public int getOutChain() {
        return (toUnsignedInt(values[27]) >>> 7)  & 0x01;
    }

    /**
     * @return «1» - движение по временному ограничению скорости (LimPresent)
     */
    public int getMoveTempLim() {
        return (toUnsignedInt(values[27]) >>> 6)  & 0x01;
    }

    /**
     * «1» - есть признак успешной коррекции координаты по светофору (3 сек)
     * @return Коррекция координаты
     */
    public int getCorrectCoord() {
        return (toUnsignedInt(values[27]) >>> 5)  & 0x01;
    }

    /**
     * @return пуск
     */
    public int getPusk() {
        return (toUnsignedInt(values[27]) >>> 4)  & 0x01;
    }

    /**
     * @return Активность тормозного режима
     */
    public int getBrakeActive() {
        return (toUnsignedInt(values[27]) >>> 3)  & 0x01;
    }

    /**
     * @return Разрешение торможения
     */
    public int getPermissionBrake() {
        return (toUnsignedInt(values[27]) >>> 2)  & 0x01;
    }

    /**
     * @return Разрешение тяги
     */
    public int getPermissionTract() {
        return (toUnsignedInt(values[27]) >>> 1)  & 0x01;
    }

    /**
     * @return Автоведение
     */
    public int getAuto() {
        return toUnsignedInt(values[27])  & 0x01;
    }

    /**
     * @return версия формата посылки (0-старая, 1-новая)
     */
    public int getVersion() {
        return (toUnsignedInt(values[28]) >>> 5) & 0x03;
    }

    /**
     * 0 – ЧС7
     * 1 – ЧС2
     * 2 – ЧС200
     * 3 – ЧС2К
     * 4 – ЧС2Т
     * 5 – ЧС6
     * 6 – ЧС4Т
     * 7 – ЭП1
     * 8 – ЧС8
     * 9 – ЭП2К
     * 10 – ЭП1П
     * 11 – ТЭП70
     * 12 – ТЭМ2
     * 13 – ЭП20 (160км/ч)
     * 14 – ЭП20 (200км/ч)
     * @return Индекс серии локомотива
     */
    public int getTypeLoc() {
        if (getVersion() == 1)
            return toUnsignedInt(values[28]) & 0x1F;
        else
            return toUnsignedInt(values[28]) & 0x07;
    }

    /**
     * @return позиция ЭДТ
     */
    public int getPosEdt() {
        if (getVersion() == 0)
            return toUnsignedInt(values[28]) >>> 7;
        else
            return toUnsignedInt(values[26]) & 0x1F;
    }

    public LocalTime getTime() {
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
//        if (hour == 0 || (min == 0) || sec == 0)
//            return null;
//        else
//            return LocalTime.of(hour, min, sec);
    }

    public LocalDate getDate() {
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
//            return LocalDate.of(year, month, day);
    }
}
