package avpt.gr.blocks32.s5k_2;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Данные системы управления МСУД – состояние ведущей секций
 */
public class Block32_54 {

    private byte[] values;

    public Block32_54(byte[] values) {
        this.values = values;
    }

    /**
     * @return Скорость колесной пары 1 *0.1 км/ч
     */
    public double getSpeedWheelPir1() {
        return toUnsignedInt(values[1]) << 8 |    // hi
                toUnsignedInt(values[0]);         // lo
    }

    /**
     * @return Скорость колесной пары 2 *0.1 км/ч
     */
    public double getSpeedWheelPir2() {
        return toUnsignedInt(values[3]) << 8 |    // hi
                toUnsignedInt(values[2]);         // lo
    }

    /**
     * @return Скорость колесной пары 3 *0.1 км/ч
     */
    public double getSpeedWheelPir3() {
        return toUnsignedInt(values[5]) << 8 |    // hi
                toUnsignedInt(values[4]);         // lo
    }

    /**
     * @return Скорость колесной пары 4 *0.1 км/ч
     */
    public double getSpeedWheelPir4() {
        return toUnsignedInt(values[7]) << 8 |    // hi
                toUnsignedInt(values[6]);         // lo
    }

    /**
     * @return Ток якоря ТЭД 1 *10 А
     */
    public double getAmperageAnchor1_main() {
        return toUnsignedInt(values[8]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 2 *10 А
     */
    public double getAmperageAnchor2_main() {
        return toUnsignedInt(values[9]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 3 *10 А
     */
    public double getAmperageAnchor3_main() {
        return toUnsignedInt(values[10]) * 10;
    }

    /**
     * @return Ток якоря ТЭД 4 *10 А
     */
    public double getAmperageAnchor4_main() {
        return toUnsignedInt(values[11]) * 10;
    }

    /**
     * @return Ток возбуждения ТЭД *10 А
     */
    public double getAmperageExcitation_main() {
        return toUnsignedInt(values[12]) * 10;
    }

    /**
     * @return Напряжение ТЭД 1 *10 В
     */
    public double getVoltage1_main() {
        return toUnsignedInt(values[13]) * 10;
    }

    /**
     * @return Напряжение ТЭД 2 *10 В
     */
    public double getVoltage2_main() {
        return toUnsignedInt(values[14]) * 10;
    }

    /**
     * @return Напряжение ТЭД 3 *10 В
     */
    public double getVoltage3_main() {
        return toUnsignedInt(values[15]) * 10;
    }

    /**
     * @return Напряжение ТЭД 4 *10 В
     */
    public double getVoltage4_main() {
        return toUnsignedInt(values[16]) * 10;
    }

    /**
     * флаг устанавливается (равен 1) при отключении KV12 (отключение секции машинистом)
     * @return 1 = Отключение секции
     */
    public int getShutdownSect() {
        return toUnsignedInt(values[17]) & 0x01;
    }

    /**
     * флаг устанавливается (равен 1) при отключении ГВ в данной секции электровоза
     * @return 1 = отключен ГВ
     */
    public int getGV() {
        return (toUnsignedInt(values[17]) >>> 1) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании клапана замещения рекуперации Y4
     * по причине аварийного разбора схемы,
     * в случае замещения рекуперации при скорости 5 км/ч флаг не устанавливается
     * @return 1 = клапан замещения рекуперации
     */
    public int getReplaceRec() {
        return (toUnsignedInt(values[17]) >>> 2) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) если включено реле KM41
     * @return 1 = включено реле KM41
     */
    public int getKM41() {
        return (toUnsignedInt(values[17]) >>> 3) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) если включено реле KM42
     * @return 1 = включено реле KM42
     */
    public int getKM42() {
        return (toUnsignedInt(values[17]) >>> 4) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 1.1
     */
    public int getSI11() {
        return toUnsignedInt(values[18]) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 1.2
     */
    public int getSI12() {
        return (toUnsignedInt(values[18]) >>> 1) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 2.1
     */
    public int getSI21() {
        return (toUnsignedInt(values[18]) >>> 2) & 0x01;
    }

    /**
     * @return 1 = СИ (снятие импульсов) по ВИП 2.2
     */
    public int getSI22() {
        return (toUnsignedInt(values[18]) >>> 3) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 1
     */
    public int getShutdownTED1() {
        return (toUnsignedInt(values[18]) >>> 4) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 2
     */
    public int getShutdownTED2() {
        return (toUnsignedInt(values[18]) >>> 5) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 3
     */
    public int getShutdownTED3() {
        return (toUnsignedInt(values[18]) >>> 6) & 0x01;
    }

    /**
     * флаг устанавливается (имеет значение 1) при срабатывании БВ ТЭД в данной секции электровоза
     * @return 1 = Отключение ТЭД 4
     */
    public int getShutdownTED4() {
        return (toUnsignedInt(values[18]) >>> 7) & 0x01;
    }

    /**
     * Номер зоны ВИП, наибольший по секции
     * @return Значение от 0 до 3
     */
    public int LargestVIP() {
        return toUnsignedInt(values[19]) & 0x0F;
    }

    /**
     * Номер зоны ВИП, наименьший по секции
     * @return Значение от 4 до 7
     */
    public int LargestLeast() {
        return (toUnsignedInt(values[19]) >>> 4) & 0x0F;
    }

    /**
     * Угол альфа регулируемый, наибольший в секции
     * @return Эл. Град.
     */
    public double getAngleRegLarge() {
        return toUnsignedInt(values[20]) & 0x0F;
    }

    /**
     * Угол альфа регулируемый, наименьший в секции
     * @return Эл. Град.
     */
    public double getAngleRegLeast() {
        return toUnsignedInt(values[21]) & 0x0F;
    }

    /**
     * Угол альфа 0 задержанный, наибольший в секции
     * @return Эл. Град.
     */
    public double getAngle0Largest() {
        return toUnsignedInt(values[22]) & 0x0F;
    }

    /**
     * Угол альфа 0 задержанный, наименьший в секции
     * @return Эл. Град.
     */
    public double getAngle0Least() {
        return toUnsignedInt(values[23]) & 0x0F;
    }

    /**
     * 0x0 – разобрана;
     * 0x1 – сбор;
     * 0x2 – разбор;
     * 0x4 – схема собрана
     * @return Состояние силовой схемы
     */
    private int getStatePowerScheme() {
        return toUnsignedInt(values[24]) & 0x07;
    }

    /**
     * Если поле «Состояние силовой схемы» имеет значение «разобрана»,
     * то силовая схема электровоза не собрана, значение поля «Режим силовой схемы» не учитывается
     * 0x0 – рекуперация
     * 0x1 – тяга с последовательным возбуждением;
     * 0x3 – тяга с независимым возбуждением;
     * @return Режим силовой схемы
     */
    private int getModePowerScheme() {
        return (toUnsignedInt(values[24]) >>> 3) & 0x03;
    }

    /**
     * силовая схема
     * @return 1 - разобрана, 2- в тяге, 4 - в рекуперации
     */
    public int getSchema() {
        int state = getStatePowerScheme();
        if (state != 0x04) return 1;
        else if (getModePowerScheme() == 0) return 4;
        else return 2;
    }

    /**
     * флаг устанавливается (имеет значение 1) при невозможности собрать силовую схему
     * в секции без постановки КМ в положение 0
     * @return 1 = блокировка сбора схемы
     */
    public int LockScheme() {
        return (toUnsignedInt(values[24]) >>> 5) & 0x01;
    }

    /**
     * @return Давление в тормозных цилиндрах тележек, наибольшее значение в секции кПа
     */
    public double getTC() {
        return toUnsignedInt(values[26]) << 8 |    // hi
                toUnsignedInt(values[25]);         // lo
    }


}
