package avpt.gr.blocks32.s5k_2;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_53 {

    private final byte[] values;

    public Block32_53(byte[] values) {
        this.values = values;
    }

    /**
     * Значения: 1 или 2
     * @return Номер текущей кабины
     */
    public int getNumCabin() {
        return toUnsignedInt(values[0]) & 0x03;
    }

    /**
     * @return количество секций локомотива
     */
    public int getCntSections() {
        return (toUnsignedInt(values[0]) >>> 2) & 0x07;
    }

    /**
     * Значения: 0, 1, 2, 3
     * @return ослабление поля
     */
    public int getWeakField() {
        return toUnsignedInt(values[1]);
    }

    /**
     * 0x1 – положение «0»;
     * 0x2 – положение «П» тяги;
     * 0x3 – положение «П» рекуперации;
     * 0x4 – положение «НР» тяги;
     * 0x5 – положение «НР» рекуперации.
     * @return Положение главной рукоятки контроллера (физическое)
     */
    public int getMainControl() {
        return toUnsignedInt(values[2]) & 0x07;
    }

    /**
     * 0x1 – положение «0»;
     * 0x2 – положение «Вперед»;
     * 0x3 – положение «Назад»;
     * @return Положение реверсивной рукоятки контроллера (физическое)
     */
    public int getRevControl() {
        return (toUnsignedInt(values[2]) >>> 3) & 0x03;
    }

    /**
     * рукоятка задания скорости в нулевом положении
     * @return 0-1
     */
    public int getHandleSpeedZero() {
        return (toUnsignedInt(values[2]) >>> 5) & 0x01;
    }

    /**
     * Установленный режим регулирования:
     * Ручное или Автоматическое.
     * Переключается тумблером «Ручное регулирование»/ «Автоматическое регулирование»
     * @return 0 – ручное регулирование, 1 - автоматическое
     */
    public int getModeAuto() {
        return (toUnsignedInt(values[2]) >>> 7) & 0x01;
    }

    /**
     * Положение тумблера «Тип возбуждения»
     * В режиме автоведения положение тумблера игнорируется
     * @return 0 – последовательное, 1 – независимое
     */
    public int getModeExcitation() {
        return (toUnsignedInt(values[4]) >>> 3) & 0x01;
    }

    /**
     *Флаг устанавливается (равен 1) если все секции электровоза могут перейти в режим независимого возбуждения.
     * Положение тумблера переключения режима возбуждения (НВ-ПВ) игнорируется в режиме автоведения.
     * @return 1 = разрешен режим независимого возбуждения
     */
    public int getAllowModeIndependentExcited() {
        return (toUnsignedInt(values[4]) >>> 5) & 0x01;
    }

    /**
     * Флаг устанавливается (имеет значение 1) если в меню включено ограничение напряжения на тяговых электродвигателях.
     * @return 1 = включено ограничение напряжения на тяговых электродвигателях
     */
    public int getOnLimitEngine() {
        return (toUnsignedInt(values[4]) >>> 6) & 0x01;
    }

    /**
     * Флаг устанавливается (имеет значение 1) в случае невозможности реализовать ток возбуждения заданный в команде рекуперативного торможения.
     * @return 1 = установлено ограничение тока возбуждения в рекуперации
     */
    public int getOnLimitAmperageExcite() {
        return (toUnsignedInt(values[4]) >>> 7) & 0x01;
    }

    /**
     * Передается ограничение тока якоря заданное положением рукоятки контроллера машиниста (КМ)
     * @return Заданное значение тока якоря ТЭД электровоза, А
     */
    public double getTaskAmperageAnchor() {
        return toUnsignedInt(values[6]) << 8 |    // hi
                toUnsignedInt(values[5]);         // lo
    }

    /**
     * В режиме автоведения поле содержит значение силы тяги/торможения, заданное командой УСАВП.
     * В режиме ручного управления передает значение 0.
     * @return Заданная сила тяги/торможения электровоза, кН
     */
    public double getTaskPower() {
        return toUnsignedInt(values[8]) << 8 |    // hi
                toUnsignedInt(values[7]);         // lo
    }

    /**
     * Поле содержит текущее значение силы тяги/торможения, реализуемое электровозом
     * @return Общая сила тяги / торможения, кН
     */
    public double getCommonPower() {
        return (short)(toUnsignedInt(values[10]) << 8 |    // hi
                toUnsignedInt(values[9]));         // lo
    }

    /**
     * Интегральный флаг, ограничение может вводиться по нескольким причинам, например, по причине перегрева ТЭД
     * @return 1 = введено ограничение тяги/рекуперации
     */
    public int getOnLimitPowerRec() {
        return toUnsignedInt(values[15]) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) если нажата кнопка экстренного торможения на пульте помощника машиниста (КАЭТ, SQ4)
     * @return 1 = клапан аварийного торможения
     */
    public int getEmergencyBrake() {
        return (toUnsignedInt(values[15]) >>> 1) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) при срабатывании сигнала ТМ
     * @return 1 = Сигнал ТМ
     */
    public int getTM() {
        return (toUnsignedInt(values[15]) >>> 2) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) если открыт клапан ускоренного торможения Y31
     * @return 1 = клапан ускоренного торможения
     */
    public int getAcceleratedBrake() {
        return (toUnsignedInt(values[15]) >>> 3) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) если хотя бы на одной тележке электровоза давление в ТЦ превышает указанное значение
     * @return 1 = давление в ТЦ более 1,4 кгс/см2
     */
    public int getPressOver() {
        return (toUnsignedInt(values[15]) >>> 4) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) при боксовании хотя бы одной колесной пары
     * @return 1 = боксование
     */
    public int getBox() {
        return (toUnsignedInt(values[15]) >>> 5) & 0x01;
    }

    /**
     * Флаг устанавливается (равен 1) если хотя бы в одной секции электровоза отключен ГВ
     * @return 1 = отключен ГВ
     */
    public int getGV() {
        return (toUnsignedInt(values[15]) >>> 6) & 0x01;
    }

    /**
     *
     * @return 1 = нажата кнопка «Отпуск тормозов»
     */
    public int getReleaseBrake() {
        return (toUnsignedInt(values[16]) >>> 1) & 0x01;
    }

    /**
     * Включен тумблер S30 «ПЕСОК АВТОМАТИЧЕСКИ»
     * @return 1 = включен режим «Песок автоматически»
     */
    public int getSandAuto() {
        return (toUnsignedInt(values[16]) >>> 2) & 0x01;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * вал контроллера в нулевом положении
     * положение «0»
     * 0х1 - (bit0=1, bit1=0, bit2=0);
     * @return 0-1
     */
    public int getControlZero() {
        return toUnsignedInt(values[2]) & 0x01;
    }

    /**
     * вал контроллера в положении "П" тяги
     * 0x2 – положение «П» тяги;
     * 0х2 - (bit0=0, bit1=1, bit2=0);
     * @return 0-1
     */
    public int getControlPThrust() {
        return (toUnsignedInt(values[2]) >>> 1) & 0x01;
    }

    /**
     * вал контроллера в положении "П" рекуперации
     * 0x3 – положение «П» рекуперации;
     * 0х3 - (bit0=1, bit1=1, bit2=0);
     * @return 0-1
     */
    public int getControlPRec() {
        return (toUnsignedInt(values[2]) & 0x07) == 3 ? 1 : 0;
    }

    /**
     * вал контроллера в положении "НР" тяги
     * 0x4 – положение «НР» тяги;
     * 0х4 - (bit0=0, bit1=0, bit2=1);
     * @return 0-1
     */
    public int getControlHPThrust() {
        return (toUnsignedInt(values[2]) & 0x07) == 6 ? 1 : 0;
    }

    /**
     * вал контроллера в положении "НР" рекуперации
     * 0x5 – положение «НР» рекуперации
     * 0х5 - (bit0=1, bit1=0, bit2=1)
     * @return 0-1
     */
    public int getControlHPRec() {
        return (toUnsignedInt(values[2]) & 0x07) == 5 ? 1 : 0;
    }

    /**
     * реверсивный вал контроллера в положении "0"
     * 0x1 – положение «0»;
     * 0х1 - (bit3=1, bit4=0);
     * @return 0-1
     */
    public int getRevControlZero() {
        return ((toUnsignedInt(values[3]) >>> 3) & 0x03) == 1 ? 1 : 0;
    }

    /**
     * реверсивный вал контроллера в положении "Вперед"
     * 0x2 – положение «Вперед»;
     * 0х2 - (bit3=0, bit4=1);
     * @return 0-1
     */
    public int getRevControlForward() {
        return ((toUnsignedInt(values[3]) >>> 3) & 0x03) == 2 ? 1 : 0;
    }

    /**
     * реверсивный вал контроллера в положении "Назад"
     * 0x3 – положение «Назад»;
     * 0х3 - (bit3=1, bit4=1);
     * @return 0-1
     */
    public int getRevControlBackward() {
        return ((toUnsignedInt(values[3]) >>> 3) & 0x03) == 3 ? 1 : 0;
    }
}
