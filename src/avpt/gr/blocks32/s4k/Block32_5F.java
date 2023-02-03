package avpt.gr.blocks32.s4k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_5F {

    private final byte[] values;

    public Block32_5F(byte[] values) {
        this.values = values;
    }

//    /**
//     * вал контроллера в нулевом положении
//     * @return 0-1
//     */
//    public int getControlZero() {
//        return toUnsignedInt(values[0]) & 0x01;
//    }
//
//    /**
//     * вал контроллера в положении "П" тяги
//     * @return 0-1
//     */
//    public int getControlPThrust() {
//        return (toUnsignedInt(values[0]) >>> 1) & 0x01;
//    }
//
//    /**
//     * вал контроллера в положении "П" рекуперации
//     * @return 0-1
//     */
//    public int getControlPRec() {
//        return (toUnsignedInt(values[0]) >>> 2) & 0x01;
//    }
//
//    /**
//     * вал контроллера в положении "НР" тяги
//     * @return 0-1
//     */
//    public int getControlHPThrust() {
//        return (toUnsignedInt(values[0]) >>> 3) & 0x01;
//    }
//
//    /**
//     * вал контроллера в положении "НР" рекуперации
//     * @return 0-1
//     */
//    public int getControlHPRec() {
//        return (toUnsignedInt(values[0]) >>> 4) & 0x01;
//    }

    /**
     * 0x1 – положение «0»;
     * 0x2 – положение «П» тяги;
     * 0x4 – положение «ПТ» торможения;
     * 0x8 – положение «ТЯГА»;
     * 0x16 – положение «ТОРМОЖЕНИЕ».
     * @return Положение главной рукоятки контроллера (физическое)
     */
    public int getMainControl() {
        int val = toUnsignedInt(values[0]) & 0x1F;
        if ((val & 0x01) == 0x01)
            return 1;
        else if ((val & 0x02) == 0x02)
            return 2;
        else if ((val & 0x04) == 0x04)
            return 3;
        else if ((val & 0x08) == 0x08)
            return 4;
        else if ((val & 0x16) == 0x16)
            return 5;
        else
            return -1;
//        switch (val) {
//            case 0x01: return 1;
//            case 0x02: return 2;
//            case 0x04: return 3;
//            case 0x08: return 4;
//            case 0x16: return 5;
//            default: return -1;
//        }
    }

    /**
     * рукоятка задания скорости в нулевом положении
     * @return 0-1
     */
    public int getHandleSpeedZero() {
        return (toUnsignedInt(values[0]) >>> 5) & 0x01;
    }

    /**
     * реверсивный вал контроллера в положении "Вперед"
     * @return 0-1
     */
    public int getRevControlForward() {
        return (toUnsignedInt(values[0]) >>> 6) & 0x01;
    }

    /**
     * реверсивный вал контроллера в положении "Назад"
     * @return 0-1
     */
    public int getRevControlBackward() {
        return (toUnsignedInt(values[0]) >>> 7) & 0x01;
    }

    /**
     * 0x0 – положение «0»;
     * 0x1 – положение «Вперед»;
     * 0x2 – положение «Назад»;
     * @return Положение реверсивной рукоятки контроллера (физическое)
     */
    public int getRevControl() {
        return ((toUnsignedInt(values[0]) >>> 6) & 0x03) + 1;
    }

    /**
     * положение тумблера S37 «Реостат - Рекуперация»
     * @return 0 – «Реостат», 1 – «Рекуперация»
     */
    public int getReostRecup() {
        return toUnsignedInt(values[1]) & 0x01;
    }

    /**
     * положение тумблера S38 «НВ - ПВ»
     * @return 0 – «Независимое возбуждение», 1 – «Последовательное возбуждение»
     */
    public int getModeExcited() {
        return (toUnsignedInt(values[1]) >>> 1) & 0x01;
    }

    /**
     * bit3=0, bit2=0 – положение «0», автоматический выбор группировки ТЭД;
     * bit3=0, bit2=1 – положение «С», Сериесное соединение ТЭД,
     * bit3=1, bit2=0 – положение «СП», Сериесно-параллельное соединение ТЭД,
     * bit3=1, bit2=1 – положение «П», Параллельное соединение ТЭД
     * @return положение переключателя SA10 «Соединение ТД»
     */
    public int getConnectTed() {
        return (toUnsignedInt(values[1]) >>> 2) & 0x03;
    }

    /**
     * 0 – отключено, 1 – включено
     * @return положение тумблера S20 «Возбудители»
     */
    public int getModeExciter() {
        return (toUnsignedInt(values[1]) >>> 4) & 0x01;
    }

    /**
     * 0 – ручное управление, 1 – авторегулирование
     * @return положение тумблера «Автоматическое регулирование»
     */
    public int getAutoReg() {
        return (toUnsignedInt(values[1]) >>> 5) & 0x01;
    }

    /**
     * bit7=0, bit6=0 –  «0»,
     * bit7=0, bit6=1 – набор позиций,
     * bit7=1, bit6=0 – сброс позиций
     * @return положение тумблера S32 (вспомогательный контроллер)
     */
    public int getControlHelper() {
        return (toUnsignedInt(values[1]) >>> 6) & 0x03;
    }

    /**
     * ТЭД-1, ТЭД-2 (отключены)
     * @return 0-1
     */
    public int getTed1Off() {
        return toUnsignedInt(values[2]) & 0x01;
    }

    /**
     * ТЭД-3, ТЭД-4 (отключены)
     * @return 0-1
     */
    public int getTed3Off() {
        return (toUnsignedInt(values[2]) >>> 1) & 0x01;
    }

    /**
     * ТЭД-5, ТЭД-6 (отключены)
     * @return 0-1
     */
    public int getTed5Off() {
        return (toUnsignedInt(values[2]) >>> 2) & 0x01;
    }

    /**
     * ТЭД-7, ТЭД-8 (отключены)
     * @return 0-1
     */
    public int getTed7Off() {
        return (toUnsignedInt(values[2]) >>> 3) & 0x01;
    }

    /**
     * ТЭД-9, ТЭД-10 (отключены)
     * @return 0-1
     */
    public int getTed9Off() {
        return (toUnsignedInt(values[2]) >>> 4) & 0x01;
    }

    /**
     * ТЭД-11, ТЭД-12 (отключены)
     * @return 0-1
     */
    public int getTed11Off() {
        return (toUnsignedInt(values[2]) >>> 5) & 0x01;
    }

    /**
     * ТЭД-13, ТЭД-14 (отключены)
     * @return 0-1
     */
    public int getTed13Off() {
        return (toUnsignedInt(values[2]) >>> 6) & 0x01;
    }

    /**
     * ТЭД-15, ТЭД-16 (отключены)
     * @return 0-1
     */
    public int getTed15Off() {
        return (toUnsignedInt(values[2]) >>> 7) & 0x01;
    }

    /**
     * схема разобрана
     * @return 0-1
     */
    public int getShDestroy() {
        return toUnsignedInt(values[3]) & 0x01;
    }

    /**
     * схема в Тяге
     * @return 0-1
     */
    public int getShThrust() {
        return (toUnsignedInt(values[3]) >>> 1) & 0x01;
    }

    /**
     * схема в Рекуперации
     * @return 0-1
     */
    public int getShRec() {
        return (toUnsignedInt(values[3]) >>> 2) & 0x01;
    }

    /**
     * силовая схема
     * @return 1 - разобрана, 2- в тяге, 4 - в рекуперации, 8 - реостат
     */
    public int getSchema() {
        return toUnsignedInt(values[3]) & 0x0F;
    }

    /**
     *
     * @return положение главной рукоятки контроллера, *10 А
     */
    public int getPosMainControl() {
        return toUnsignedInt(values[4]);
    }

    /**
     * @return 1 = отключен БВ
     */
    public int getBV() {
        return (toUnsignedInt(values[5]) >>> 4) & 0x01;
    }

    /**
     * «1» - срабатывание любой из защит, кроме БВ
     * @return 1 = обобщенный сигнал защит
     */
    public int getProtect() {
        return (toUnsignedInt(values[5]) >>> 5) & 0x01;
    }

    /**
     * по «ИЛИ» боксование (юз) колесных пар 1..16
     * @return 1 = боксование
     */
    public int getBox() {
        return (toUnsignedInt(values[5]) >>> 6) & 0x01;
    }

    /**
     * 0 – «0»,
     * 1 – не «0»
     * @return положение тумблера S31 (маневровый контроллер)
     */
    public int getControlMan() {
        return (toUnsignedInt(values[5]) >>> 7) & 0x01;
    }

    /**
     * @return 1 = давление в ТЦ более 1,1 кгс/см2
     */
    public int getPressOver() {
        return toUnsignedInt(values[6]) & 0x01;
    }

    /**
     * @return Клапан авар. экстр. торможения от блока А13
     */
    public int getEmergencyBrake() {
        return (toUnsignedInt(values[6]) >>> 1) & 0x01;
    }

    /**
     * 0 – нет сигнала,
     * 1 – сигнал ЭПК
     * @return Сигнал ЭПК (Y25)
     */
    public int getEPK() {
        return (toUnsignedInt(values[6]) >>> 2) & 0x01;
    }

    /**
     * 0 – нет сигнала,
     * 1 – сигнал ТМ
     * @return Индикатор ТМ (датчик давления тормозной магистрали) KV18
     */
    public int getTM() {
        return (toUnsignedInt(values[6]) >>> 3) & 0x01;
    }

    /**
     * @return 1 = блок управления в режиме автоведения
     */
    public int getModeAuto() {
        return (toUnsignedInt(values[6]) >>> 5) & 0x01;
    }

    /**
     * 0 – запрет управления ОП,
     * 1 – ОП может установлено
     * @return управление ослаблением поля (ОП)
     */
    public int getControlOP() {
        return (toUnsignedInt(values[6]) >>> 6) & 0x01;
    }

    /**
     * 0 – ОП не включено,
     * позиции ОП: 1 – 4
     * @return Номер позиции ослабления поля (ОП)
     */
    public int getOP() {
        return toUnsignedInt(values[17]) & 0x07;
    }

    /**
     * Значения: 1 или 2
     * @return Номер текущей кабины
     */
    public int getNumCabin() {
        return ((toUnsignedInt(values[20]) >>> 5) & 0x01) + 1;
    }

    /**
     * «КЗ»
     * @return Срабатывание защиты
     */
    public int getProtectKZ() {
        return toUnsignedInt(values[24]) & 0x01;
    }

    /**
     * 1 – Питание от АБ (ЗБ), U3
     * @return «Разряд АБ»
     */
    public int getDischargeAB() {
        return (toUnsignedInt(values[24]) >>> 1) & 0x01;
    }

    /**
     * Включен тумблер S30 «ПЕСОК АВТОМАТИЧЕСКИ»
     * @return «Песок авт.»
     */
    public int getSandAuto() {
        return toUnsignedInt(values[25]) & 0x01;
    }

    /**
     * 1 – Включение электропневматического клапана Y11, Y12 или Y13, Y14 песочниц
     * @return «Песочницы»
     */
    public int getSand() {
        return (toUnsignedInt(values[25]) >>> 1) & 0x01;
    }

    /**
     *
     * @return кнопка “Отпуск тормозов”, S23
     */
    public int getReleaseBrake() {
        return (toUnsignedInt(values[25]) >>> 2) & 0x01;
    }

    /**
     * 0 – отключен, 1 – включен
     * @return тумблер “Компрессор”, S14
     */
    public int getCompressorOn() {
        return (toUnsignedInt(values[25]) >>> 3) & 0x01;
    }

    /**
     * 0 – отключен, 1 – включен
     * @return тумблер “Вентилятор1”, S11
     */
    public int getFan1() {
        return (toUnsignedInt(values[25]) >>> 4) & 0x01;
    }

    /**
     * 0 – отключен, 1 – включен
     * @return тумблер “Вентилятор2”, S12
     */
    public int getFan2() {
        return (toUnsignedInt(values[25]) >>> 5) & 0x01;
    }

    /**
     * 0 – свисток отключен, 1 – свисток включен
     * @return состояние катушки свистка HA1
     */
    public int getWhistle() {
        return (toUnsignedInt(values[25]) >>> 6) & 0x01;
    }

    /**
     * 0 – тифон отключен, 1 – тифон включен
     * @return состояние катушки тифона Y17, KV38
     */
    public int getTifon() {
        return (toUnsignedInt(values[25]) >>> 7) & 0x01;
    }

    /**
     * 1 – Подана команда от САУТ-ЦМ на снятие тяги (KV39??)
     * @return «САУТ-снятие тяги»
     */
    public int getReleaseTract() {
        return toUnsignedInt(values[26]) & 0x01;
    }

    /**
     * 1 –Подана команда от САУТ-ЦМ на торможение (KV40)
     * @return «САУТ-торможение»
     */
    public int getBraking() {
        return (toUnsignedInt(values[26]) >>> 1) & 0x01;
    }

    /**
     * 1 – Сигнал от КЛУБ, KV80
     * @return Сигнал КЛУБ-У
     */
    public int getSignalClub() {
        return (toUnsignedInt(values[26]) >>> 2) & 0x01;
    }
}
