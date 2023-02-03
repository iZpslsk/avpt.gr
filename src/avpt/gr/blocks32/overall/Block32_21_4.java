package avpt.gr.blocks32.overall;

import avpt.gr.chart_dataset.TaskAutoDrive;
import avpt.gr.common.UtilsArmG;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * основная посылка УСАВП
 */
public class Block32_21_4 {

    private final byte[] values;
    private static final int MAX_SPEED = 105;

    public Block32_21_4(byte[] values) {
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
     * @return источник задания
     */
    public int getTask() {
        return toUnsignedInt(values[1]) & 0x7F;
    }

    /**
     * @return координата УСАВП
     */
    public int getCoordinate() {
        return (toUnsignedInt(values[2]) & 0x7E) << 13 |            // hi
                (toUnsignedInt(values[4]) & 0x7F) << 7 |
                toUnsignedInt(values[5]) & 0x7F;                    // lo
    }

    /**
     * @return скорость
     */
    public int getSpeed() {
        int result = (toUnsignedInt(values[2]) & 0x01) << 7 |        // hi
                (toUnsignedInt(values[3]) & 0x7F);             // lo
        return Math.min(result, MAX_SPEED);
    }

    /**
     * @return ослабление поля
     */
    public int getWeakField() {
        return (toUnsignedInt(values[6]) & 0x70) >>> 4;
    }

    /**
     * @return тип тормозов
     */
    public int getTypeBrake() {
        return (toUnsignedInt(values[6]) & 0x0E) >>> 1;
    }

    /**
     * @return текущее ограничение скорости
     */
    public int getCurSpeedLimit() {
        return (toUnsignedInt(values[6]) & 0x01) << 7 |        // hi
               (toUnsignedInt(values[7]) & 0x7F);             // lo
    }

    /**
     * @return давление УР
     */
    public double getPressUR() {
        return (toUnsignedInt(values[8]) & 0x7F) * 0.1;
    }

    /**
     * @return давление ТМ
     */
    public double getPressTM() {
        return (toUnsignedInt(values[9]) & 0x7F) * 0.1;
    }

    /**
     * @return давление ТЦ
     */
    public double getPressTC() {
        return (toUnsignedInt(values[10]) & 0x7F) * 0.1;
    }

    /**
     * 1 – Ж
     * 2 – Б
     * 3 – З
     * 4 – КЖ
     * 5 – К
     * @return АЛСН
     */
    public int getALSN() {
        return (toUnsignedInt(values[11]) >>> 3) & 0x07;
    }

    /**
     * @return флаг маневрового
     */
    public int getManeuver() {
        return (toUnsignedInt(values[11]) >>> 6) & 0x01;
    }

    /**
     * @return флаг запрета автоведения
     */
    public int getBanAuto() {
        return (toUnsignedInt(values[14]) >>> 6) & 0x01;
    }

    /**
     * @return секунд сначала суток
     */
    public int getSecBeginDay() {
        return  (toUnsignedInt(values[11]) & 0x07) << 14 |       // hi
                (toUnsignedInt(values[12]) & 0x7F) << 7 |
                toUnsignedInt(values[13]) & 0x7F;                // lo
    }

    /**
     * @return позиция контроллера
     */
    public int getPosition() {
        return toUnsignedInt(values[14]) & 0x3F;
    }

    /**
     * @return тип ограничения
     */
    public int getTypeSpeedLim() {
        return (toUnsignedInt(values[15]) & 0x7F) >>> 5;
    }

    /**
     * @return флаг автоведения
     */
    public int getAuto() {
        return (toUnsignedInt(values[15]) >>> 4) & 0x01;
    }

    /**
     * @return алгоритм автоведения
     */
    public int getAlgorithmAuto() {
        return (toUnsignedInt(values[15]) >>> 3) & 0x01;
    }

    /**
     * @return флаг отключения цепей
     */
    public int getChainOff() {
        return (toUnsignedInt(values[15]) >>> 2) & 0x01;
    }

    /**
     * @return флаг коррекции координаты
     */
    public int getCorrectCoordinate() {
        return (toUnsignedInt(values[15]) >>> 1) & 0x01;
    }

    /**
     * @return расчетная скорость по ERG-у
     */
    public int getSpeedERG() {
        int result = (toUnsignedInt(values[15]) & 0x01) << 7 |        // hi
                (toUnsignedInt(values[16]) & 0x7F);             // lo
        return Math.min(result, MAX_SPEED);
    }

    /**
     * @return доп канал
     */
    public int getAdditionChannel() {
        return (toUnsignedInt(values[17]) >>> 6) & 0x01;
    }

    /**
     * @return Ошибка ПТ. БДУ
     */
    public int getErrBDU_PT() {
        return (toUnsignedInt(values[17]) >>> 5) & 0x01;
    }

    /**
     * @return Ошибка тяги БДУ
     */
    public int getErrBDU_tract() {
        return (toUnsignedInt(values[17]) >>> 4) & 0x01;
    }

    /**
     * @return команда тяги
     */
    public int getCommandTract() {
        return toUnsignedInt(values[17]) & 0x0F;
    }

    /**
     * @return скорость задания или тяги
     */
    public int getSpeedTask() {
        return (toUnsignedInt(values[19]) & 0x01) << 7 |        // hi
                (toUnsignedInt(values[18]) & 0x7F);             // lo
    }

    /**
     * @return координата задания
     */
    public int getCoordinateTask() {
        return  (toUnsignedInt(values[21]) & 0x7F) << 13 |       // hi
                (toUnsignedInt(values[20]) & 0x7F) << 6 |
                (toUnsignedInt(values[19]) & 0x7F) >>> 1;        // lo
    }

    /**
     * @return запрет тяги
     */
    public int getBanTract() {
        return (toUnsignedInt(values[22]) >>> 6) & 0x01;
    }

    /**
     * @return запрет торможения
     */
    public int getBanBrake() {
        return (toUnsignedInt(values[22]) >>> 5) & 0x01;
    }

    /**
     * @return управление тормозным заданием
     */
    public int getControlBrakeTask() {
        return toUnsignedInt(values[22]) & 0x1F;
    }

    /**
     * @return вмешательство машиниста
     */
    public int getInterDriver() {
        return (toUnsignedInt(values[23]) >>> 6) & 0x01;
    }

    /**
     * @return задание на ограничение скорости
     */
    public int getTaskSpeedLimit() {
        return (toUnsignedInt(values[23]) >>> 3) & 0x07;
    }

    /**
     * @return источник команды пневматике
     */
    public int getSourceCommandPneumatic() {
        return toUnsignedInt(values[23]) & 0x07;
    }

    /**
     * @return давление в комманде пневматики
     */
    public int getPressCommandPneumatic() {
        return toUnsignedInt(values[24]) & 0x7F;
    }

    /**
     * @return эмуляция
     */
    public int getEmulate() {
        return (toUnsignedInt(values[25]) >>> 6) & 0x01;
    }

    /**
     * @return тест тормозов не пройден
     */
    public int getTestBrakeCorrupt() {
        return (toUnsignedInt(values[26]) >>> 6) & 0x01;
    }

    /**
     * @return тест тяги не пройден
     */
    public int getTestTractCorrupt() {
        return (toUnsignedInt(values[26]) >>> 5) & 0x01;
    }

    /**
     * @return последняя комманда пневматике
     */
    public int getLastCommandPneumatic() {
        return toUnsignedInt(values[26]) & 0x1F;
    }

    /**
     * @return флаг связи
     */
    public int getFlagTract() {
        return (toUnsignedInt(values[27]) >>> 4) & 0x01;
    }

    /**
     * @return максимальное соединение тяги
     */
    public int getMaxTract() {
        return toUnsignedInt(values[27]) & 0x0F;
    }

    /**
     * @return состояние пневматики
     */
    public int getPneumaticState() {
        return (toUnsignedInt(values[28]) >>> 2) & 0x07;
    }

    /**
     * @return ошибка ПТ
     */
    public int getErrPT() {
        return (toUnsignedInt(values[28]) >>> 1) & 0x01;
    }

    /**
     * @return ПТ в состоянии теста
     */
    public int getTestPT() {
        return (toUnsignedInt(values[28])) & 0x01;
    }

    /**
     * @return Одиноч ош. CAN1
     */
    public int getCAN1SingleErr() {
        return toUnsignedInt(values[1]) >>> 7;
    }

    /**
     * @return CAN1 err. warn.
     */
    public int getCAN1ErrWarn() {
        return toUnsignedInt(values[2]) >>> 7;
    }

    /**
     * @return CAN1 еrr. Passive
     */
    public int getCAN1ErrPassive() {
        return toUnsignedInt(values[3]) >>> 7;
    }

    /**
     * @return CAN1 bus off
     */
    public int getCAN1BusOff() {
        return toUnsignedInt(values[4]) >>> 7;
    }

    /**
     * @return CAN1 no bus free
     */
    public int getCAN1NoBusFree() {
        return toUnsignedInt(values[5]) >>> 7;
    }

    /**
     * @return Одиноч ош. CAN2
     */
    public int getCAN2SingleErr() {
        return toUnsignedInt(values[6]) >>> 7;
    }

    /**
     * @return CAN2 err. warn.
     */
    public int getCAN2ErrWarn() {
        return toUnsignedInt(values[7]) >>> 7;
    }

    /**
     * @return CAN2 еrr. Passive
     */
    public int getCAN2ErrPassive() {
        return toUnsignedInt(values[8]) >>> 7;
    }

    /**
     * @return CAN2 bus off
     */
    public int getCAN2BusOff() {
        return toUnsignedInt(values[9]) >>> 7;
    }

    /**
     * @return CAN2 no bus free
     */
    public int getCAN2NoBusFree() {
        return toUnsignedInt(values[10]) >>> 7;
    }

    /**
     * @return Желт. Миг.
     */
    public int getYellowFlash() {
        return toUnsignedInt(values[11]) >>> 7;
    }

    /**
     * @return Главн. путь
     */
    public int getMainRoad() {
        return toUnsignedInt(values[12]) >>> 7;
    }

    /**
     * @return По удалению
     */
    public int getOnLen() {
        return toUnsignedInt(values[13]) >>> 7;
    }

    /**
     * @return Ключ #1 БДУ-39 3ЭС5К
     */
    public int getKeyBDU39() {
        return toUnsignedInt(values[17]) >>> 7;
    }

    /**
     * @return ЭПК по КЛУБ-У 3ЭС5К
     */
    public int getEPK() {
        return toUnsignedInt(values[18]) >>> 7;
    }

    /**
     * @return Превыш 5мин ток
     */
    public int getOverFiveMinAmperage() {
        return toUnsignedInt(values[19]) >>> 7;
    }

    /**
     * @return Превыш часовой ток
     */
    public int getOverHourAmperage() {
        return toUnsignedInt(values[20]) >>> 7;
    }

    /**
     * @return Прибл к макс нагр
     */
    public int getMaxLoad() {
        return toUnsignedInt(values[21]) >>> 7;
    }

    /**
     * @return Перегрев ТЭД
     */
    public int getOverHeatTED() {
        return toUnsignedInt(values[22]) >>> 7;
    }

    /**
     * @return Тяга на нейт встав
     */
    public int getThrustNV() {
        return toUnsignedInt(values[23]) >>> 7;
    }

    /**
     * @return режим автоведения
     */
    public int getModeAuto() {
        if (getChainOff() == 1) return TaskAutoDrive.CHAIN_OFF;                                   // Откл цепей
        if (getAuto() == 1) {
            if (getBanAuto() == 1) return TaskAutoDrive.BAN_AUTO;                                 // Запрет автоведения
            if (getBanTract() == 1 && getBanBrake() == 0) return TaskAutoDrive.BAN_TRACT;         // Запрет тяги
            if (getBanTract() == 1 && getBanBrake() == 1) return TaskAutoDrive.BAN_BRAKE_TRACT;   // Запрет тяги и торм
            if (getAlgorithmAuto() == 1) return TaskAutoDrive.AUTO_SCHEDULE;                      // Ведение по расписанию
            if (getEmulate() == 1) return TaskAutoDrive.EMULATION;                                // Эмуляция
            return TaskAutoDrive.AUTO_AVG_SPEED;                                                  // Ведение по средней скорости
        }
        else return TaskAutoDrive.MANUAL;                                                         // Режим УСАВП-Г - режим подсказки
    }

    /**
     * @return максимально допустимая скорость - определяет высоту шакалы скорости
     */
    public static int getMaxLimit() {
        return MAX_SPEED;
    }

    /**
     * @return - Задание автоведения
     */
    public int getPowerTaskAuto() {
        int val =  (short) ((toUnsignedInt(values[14]) & 0x3F) << 6 |
                            (toUnsignedInt(values[6]) & 0x70) >>> 1 |
                             toUnsignedInt(values[0]) & 0x07);
        if ((val & 0x800) > 0) val -= 0x1000;
        if (val > 1300) val = 0;
        if (val < -1000) val = 0;
        return val;
    }

    public LocalDateTime getDateTime(LocalDate date) {
        return UtilsArmG.getDateTime(date, getSecBeginDay());
    }

}
