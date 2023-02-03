package avpt.gr.blocks32.s5k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * дискретные сигналы для эс5к
 */
public class Block32_5F {

    private byte[] values;

    public Block32_5F(byte[] values) {
        this.values = values;
    }

    /**
     * вал контроллера в нулевом положении
     * @return 0-1
     */
    public int getControlZero() {
        return toUnsignedInt(values[0]) & 0x01;
    }

    /**
     * вал контроллера в положении "П" тяги
     * @return 0-1
     */
    public int getControlPThrust() {
        return (toUnsignedInt(values[0]) >>> 1) & 0x01;
    }

    /**
     * вал контроллера в положении "П" рекуперации
     * @return 0-1
     */
    public int getControlPRec() {
        return (toUnsignedInt(values[0]) >>> 2) & 0x01;
    }

    /**
     * вал контроллера в положении "НР" тяги
     * @return 0-1
     */
    public int getControlHPThrust() {
        return (toUnsignedInt(values[0]) >>> 3) & 0x01;
    }

    /**
     * вал контроллера в положении "НР" рекуперации
     * @return 0-1
     */
    public int getControlHPRec() {
        return (toUnsignedInt(values[0]) >>> 4) & 0x01;
    }

    /**
     * 0x1 – положение «0»;
     * 0x2 – положение «П» тяги;
     * 0x4 – положение «П» рекуперации;
     * 0x8 – положение «НР» тяги;
     * 0x16 – положение «НР» рекуперации.
   //  * 0x32 - рукоятка задания скорости в нулевом положении
     *
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
//            case 0x1: return 1;
//            case 0x2: return 2;
//            case 0x4: return 3;
//            case 0x8: return 4;
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
     * Срабатывание ГВ
     * @return 0-1
     */
    public int getGV() {
        return toUnsignedInt(values[1]) & 0x01;
    }

    /**
     * Срабатывание защиты
     * @return 0-1
     */
    public int getProtect() {
        return (toUnsignedInt(values[1]) >>> 1) & 0x01;
    }

    /**
     * Боксование
     * @return 0-1
     */
    public int getBox() {
        return (toUnsignedInt(values[1]) >>> 2) & 0x01;
    }

    /**
     * давл в ТЦ > 1,1 кгс/см2
     * @return 0-1
     */
    public int getPressOver() {
        return (toUnsignedInt(values[1]) >>> 3) & 0x01;
    }

    /**
     * тумблер-"авторег
     * @return 0-1
     */
    public int getAutoReg() {
        return (toUnsignedInt(values[1]) >>> 4) & 0x01;
    }

    /**
     * управление ОП
     * @return 0-1
     */
    public int getControlOP() {
        return (toUnsignedInt(values[1]) >>> 5) & 0x01;
    }

    /**
     * б. у. в автоведении
     * @return 0-1
     */
    public int getControlModeAuto() {
        return (toUnsignedInt(values[1]) >>> 6) & 0x01;
    }

    /**
     * ограничение Iвоз в рекуперации
     * @return 0-1
     */
    public int getLimIRec() {
        return (toUnsignedInt(values[1]) >>> 7) & 0x01;
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
     * @return 1 - разобрана, 2- в тяге, 4 - в рекуперации
     */
    public int getSchema() {
        return toUnsignedInt(values[3]) & 0x07;
    }

    /**
     * задатчик тока - макс.
     * @return 0-1
     */
    public int getControlIMax() {
        return (toUnsignedInt(values[3]) >>> 3) & 0x01;
    }

    /**
     * снятие импульсов с ВИП 1
     * @return 0-1
     */
    public int getVip1() {
        return (toUnsignedInt(values[3]) >>> 4) & 0x01;
    }

    /**
     * снятие импульсов с ВИП 2
     * @return 0-1
     */
    public int getVip2() {
        return (toUnsignedInt(values[3]) >>> 5) & 0x01;
    }

    /**
     * снятие импульсов с ВИП 3
     * @return 0-1
     */
    public int getVip3() {
        return (toUnsignedInt(values[3]) >>> 6) & 0x01;
    }

    /**
     * снятие импульсов с ВИП 4
     * @return 0-1
     */
    public int getVip4() {
        return (toUnsignedInt(values[3]) >>> 7) & 0x01;
    }

    /**
     * @return положение задатчика тяги (главный вал контроллера), *10 А
     */
    public int getPosMainControl() {
        return toUnsignedInt(values[4]);
    }

    /**
     * Ослабление поля
     * @return 0-1
     */
    public int getWeakField() {
        return (toUnsignedInt(values[17]) >>> 4) & 0x01;
    }

    /**
     * Значения: 1 или 2
     * @return Номер текущей кабины
     */
    public int getNumCabin() {
        return ((toUnsignedInt(values[20]) >>> 5) & 0x01) + 1;
    }

    /**
     * МК откл компрессор ("МК" Отключен электродвигатель компрессора М14)
     * @return 0-1
     */
    public int getMKCompressOff() {
        return toUnsignedInt(values[24]) & 0x01;
    }

    /**
     * ДМ давление масла ("ДМ" Нет давления масла в компрессоре)
     * @return 0-1
     */
    public int getDMPressOil() {
        return (toUnsignedInt(values[24]) >>> 1) & 0x01;
    }

    /**
     * ОБ (Отказ оборудования)
     * @return 0-1
     */
    public int getFault() {
        return (toUnsignedInt(values[24]) >>> 3) & 0x01;
    }

    /**
     * ДП (установлен ДП)
     * @return 0-1
     */
    public int getSetDP() {
        return (toUnsignedInt(values[24]) >>> 4) & 0x01;
    }

    /**
     * КЗ (срабатываниу защиты)
     * @return 0-1
     */
    public int getProtectKZ() {
        return (toUnsignedInt(values[24]) >>> 5) & 0x01;
    }

    /**
     * Статус ДП или упр ПЧФ
     * @return 0-1
     */
    public int getStatusDP() {
        return (toUnsignedInt(values[24]) >>> 6) & 0x01;
    }

    /**
     * Песочницы
     * @return 0-1
     */
    public int getSand() {
        return (toUnsignedInt(values[24]) >>> 7) & 0x01;
    }

    /**
     * Лампа ДБ
     * @return 0-1
     */
    public int getLampDB() {
        return toUnsignedInt(values[25]) & 0x01;
    }

    /**
     * Снятие тяги
     * @return 0-1
     */
    public int getRemoveThrust() {
        return (toUnsignedInt(values[25]) >>> 1) & 0x01;
    }

    /**
     * Снятие Рекуперация
     * @return 0-1
     */
    public int getRec() {
        return (toUnsignedInt(values[25]) >>> 2) & 0x01;
    }

    /**
     * KV21-KV23
     * @return 0-1
     */
    public int getKV_21_23() {
        return (toUnsignedInt(values[25]) >>> 5) & 0x01;
    }

    /**
     * САУТ-снятие тяги
     * @return 0-1
     */
    public int getRemoveThrustSAUT() {
        return (toUnsignedInt(values[25]) >>> 6) & 0x01;
    }

    /**
     * Пожар
     * @return 0-1
     */
    public int getFire() {
        return (toUnsignedInt(values[25]) >>> 7) & 0x01;
    }

    /**
     * РКЗ
     * @return 0-1
     */
    public int getRKZ() {
        return toUnsignedInt(values[26]) & 0x01;
    }

    /**
     * Разряд АБ
     * @return 0-1
     */
    public int getAB() {
        return (toUnsignedInt(values[26]) >>> 1) & 0x01;
    }

    /**
     * Песок авт
     * @return 0-1
     */
    public int getSandAuto() {
        return (toUnsignedInt(values[26]) >>> 2) & 0x01;
    }

    /**
     * Вкл. KV15
     * @return 0-1
     */
    public int getKV15() {
        return (toUnsignedInt(values[26]) >>> 3) & 0x01;
    }

    /**
     * Откл РН
     * @return 0-1
     */
    public int getOffPH() {
        return (toUnsignedInt(values[26]) >>> 7) & 0x01;
    }

    /**
     * ВУВ
     * @return 0-1
     */
    public int getVuv() {
        return toUnsignedInt(values[27]) & 0x01;
    }

    /**
     * Откл. вент-р В1
     * @return 0-1
     */
    public int getFanB1Off() {
        return (toUnsignedInt(values[27]) >>> 1) & 0x01;
    }

    /**
     * Откл. вент-р В2
     * @return 0-1
     */
    public int getFanB2Off() {
        return (toUnsignedInt(values[27]) >>> 2) & 0x01;
    }

    /**
     * Откл. вент-р В3
     * @return 0-1
     */
    public int getFanB3Off() {
        return (toUnsignedInt(values[27]) >>> 3) & 0x01;
    }

    /**
     * ВИП
     * @return 0-1
     */
    public int getVip() {
        return (toUnsignedInt(values[27]) >>> 4) & 0x01;
    }

    /**
     * ТР-Р
     * @return 0-1
     */
    public int getTRR() {
        return (toUnsignedInt(values[27]) >>> 5) & 0x01;
    }

    /**
     * РЗ замыкание на корпус ("РЗ" Срабатывание реле KV1 контроля замыкания на корпус цепей питания)
     * @return 0-1
     */
    public int getRZ() {
        return (toUnsignedInt(values[27]) >>> 6) & 0x01;
    }

    /**
     * Нет давления в ГВ
     * @return 0-1
     */
    public int getNoPressGV() {
        return (toUnsignedInt(values[27]) >>> 7) & 0x01;
    }
}
