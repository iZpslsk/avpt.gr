package avpt.gr.blocks32.s5k_2;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Данные системы управления МСУД – состояние ведомых секций
 */
public class Block32_55 {

    private byte[] values;

    public Block32_55(byte[] values) {
        this.values = values;
    }

    /**
     * @return Ток якоря ТЭД 1 *10 А
     */
    public double getAmperageAnchor1_s1() {
        return toUnsignedInt(values[0]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 2 *10 А
     */
    public double getAmperageAnchor2_s1() {
        return toUnsignedInt(values[1]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 3 *10 А
     */
    public double getAmperageAnchor3_s1() {
        return toUnsignedInt(values[2]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 4 *10 А
     */
    public double getAmperageAnchor4_s1() {
        return toUnsignedInt(values[3]) * 10;
    }

    /**
     * @return Ток возбуждения ТЭД *10 А
     */
    public double getAmperageExcitation_s1() {
        return toUnsignedInt(values[4]) * 10;
    }

    /**
     * @return Напряжение ТЭД 1 *10 В
     */
    public double getVoltage1_s1() {
        return toUnsignedInt(values[5]) * 10;
    }

    /**
     * @return Напряжение ТЭД 2 *10 В
     */
    public double getVoltage2_s1() {
        return toUnsignedInt(values[6]) * 10;
    }

    /**
     * @return Напряжение ТЭД 3 *10 В
     */
    public double getVoltage3_s1() {
        return toUnsignedInt(values[7]) * 10;
    }

    /**
     * @return Напряжение ТЭД 4 *10 В
     */
    public double getVoltage4_s1() {
        return toUnsignedInt(values[8]) * 10;
    }

    /**
     * флаг устанавливается (равен 1) при отключении KV12 (отключение секции машинистом)
     * @return 1 = Отключение секции
     */
    public int getShutdownSect_s1() {
        return toUnsignedInt(values[9]) & 0x01;
    }

    /**
     * флаг устанавливается (равен 1) при отключении ГВ в данной секции электровоза
     * @return 1 = отключен ГВ
     */
    public int getGV_s1() {
        return (toUnsignedInt(values[9]) >>> 1) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании клапана замещения рекуперации Y4
     * по причине аварийного разбора схемы,
     * в случае замещения рекуперации при скорости 5 км/ч флаг не устанавливается
     * @return 1 = клапан замещения рекуперации
     */
    public int getReplaceRec_s1() {
        return (toUnsignedInt(values[9]) >>> 2) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) если включено реле KM41
     * @return 1 = включено реле KM41
     */
    public int getKM41_s1() {
        return (toUnsignedInt(values[9]) >>> 3) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) если включено реле KM42
     * @return 1 = включено реле KM42
     */
    public int getKM42_s1() {
        return (toUnsignedInt(values[9]) >>> 4) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 1.1
     */
    public int getSI11_s1() {
        return toUnsignedInt(values[10]) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 1.2
     */
    public int getSI12_s1() {
        return (toUnsignedInt(values[10]) >>> 1) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 2.1
     */
    public int getSI21_s1() {
        return (toUnsignedInt(values[10]) >>> 2) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 2.2
     */
    public int getSI22_s1() {
        return (toUnsignedInt(values[10]) >>> 3) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 1
     */
    public int getShutdownTED1_s1() {
        return (toUnsignedInt(values[10]) >>> 4) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 2
     */
    public int getShutdownTED2_s1() {
        return (toUnsignedInt(values[10]) >>> 5) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 3
     */
    public int getShutdownTED3_s1() {
        return (toUnsignedInt(values[10]) >>> 6) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 4
     */
    public int getShutdownTED4_s1() {
        return (toUnsignedInt(values[10]) >>> 7) & 0x01;
    }

    /**
     * Номер зоны ВИП, наибольший по секции
     * @return Значение от 0 до 3
     */
    public int LargestVIP_s1() {
        return toUnsignedInt(values[11]) & 0x0F;
    }

    /**
     * Номер зоны ВИП, наименьший по секции
     * @return Значение от 4 до 7
     */
    public int LargestLeast_s1() {
        return (toUnsignedInt(values[11]) >>> 4) & 0x0F;
    }

    /**
     * Угол альфа регулируемый, наибольший в секции
     * @return Эл. Град.
     */
    public double getAngleRegLarge_s1() {
        return toUnsignedInt(values[12]) & 0x0F;
    }

    /**
     * Угол альфа регулируемый, наименьший в секции
     * @return Эл. Град.
     */
    public double getAngleRegLeast_s1() {
        return toUnsignedInt(values[13]) & 0x0F;
    }

    /**
     * Угол альфа 0 задержанный, наибольший в секции
     * @return Эл. Град.
     */
    public double getAngle0Largest_s1() {
        return toUnsignedInt(values[14]) & 0x0F;
    }

    /**
     * Угол альфа 0 задержанный, наименьший в секции
     * @return Эл. Град.
     */
    public double getAngle0Least_s1() {
        return toUnsignedInt(values[15]) & 0x0F;
    }

    /**
     * 0x0 – разобрана;
     * 0x1 – сбор;
     * 0x2 – разбор;
     * 0x4 – схема собрана
     * @return Состояние силовой схемы
     */
    public int getStatePowerScheme_s1() {
        return toUnsignedInt(values[16]) & 0x07;
    }

    /**
     * Если поле «Состояние силовой схемы» имеет значение «разобрана»,
     * то силовая схема электровоза не собрана, значение поля «Режим силовой схемы» не учитывается.
     * 0x0 – рекуперация
     * 0x1 – тяга с последовательным возбуждением;
     * 0x3 – тяга с независимым возбуждением;
     * @return Режим силовой схемы
     */
    public int getModePowerScheme_s1() {
        return (toUnsignedInt(values[16]) >>> 3) & 0x03;
    }

    /**
     * силовая схема
     * @return 1 - разобрана, 2- в тяге, 3 - в рекуперации
     */
    public int getSchema_s1() {
        int state = getStatePowerScheme_s1();
        if (state != 0x04) return 1;
        else if (getModePowerScheme_s1() == 0) return 3;
        else return 2;
    }

    /**
     * флаг устанавливается (имеет значение 1) при невозможности собрать силовую схему
     * в секции без постановки КМ в положение 0
     * @return 1 = блокировка сбора схемы
     */
    public int LockScheme_s1() {
        return (toUnsignedInt(values[16]) >>> 5) & 0x01;
    }

    /**
     * @return Давление в тормозных цилиндрах тележек, наибольшее значение в секции кПа
     */
    public double getTC_s1() {
        return toUnsignedInt(values[18]) << 8 |    // hi
                toUnsignedInt(values[17]);         // lo
    }
}
