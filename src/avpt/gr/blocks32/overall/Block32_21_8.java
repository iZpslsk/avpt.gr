package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * БХВ
 */
public class Block32_21_8 {

    private final byte[] values;

    public Block32_21_8(byte[] values) {
        this.values = values;
    }

    /**
     * дискретный сигнал
     * @return торможение с хвоста
     */
    public int getBrakeTail() {
        return (toUnsignedInt(values[0]) >>> 3) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return есть доп канал
     */
    public int getIsSaveChan() {
        return (toUnsignedInt(values[6]) >>> 7) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return Связь по доп.
     */
    public int getLinkSlaveCh() {
        return (toUnsignedInt(values[2])  >>> 5) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return Связь по осн.
     */
    public int getLinkMainCh() {
        return (toUnsignedInt(values[2])  >>> 4) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return наличие команды
     */
    public int getIsCommand() {
        return (toUnsignedInt(values[6])  >>> 4) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return отказ вентилятора экстренного торможения
     */
    public int getFanEmergencyBrake() {
        if (getTypeBHV() == 1)
            return (toUnsignedInt(values[10])  >>> 7) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return отказ вентилятора служебного торможения
     */
    public int getFanBrake() {
        if (getTypeBHV() == 1)
            return (toUnsignedInt(values[10])  >>> 6) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return отказ датч давл РК
     */
    public int getSensorPressRK() {
        if (getTypeBHV() == 1)
            return (toUnsignedInt(values[10])  >>> 5) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return отказ датч давл ТМ
     */
    public int getSensorPressTM() {
        if (getTypeBHV() == 1)
            return (toUnsignedInt(values[10])  >>> 4) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return обновление параметров
     */
    public int getUpdateParam() {
        if (getTypeBHV() == 1)
            return (toUnsignedInt(values[12])  >>> 7) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return отладка режима отпуска
     */
    public int getDebugOtp() {
        if (getTypeBHV() == 1)
            return toUnsignedInt(values[12]) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return бхв настроен
     */
    public int getBhvReady() {
        return (toUnsignedInt(values[0]) >>> 4) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return бхв другие
     */
    public int getBhvOther() {
        return (toUnsignedInt(values[2]) >>> 6) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return вмешательство по радио
     */
    public int getInterventionRadio() {
        return (toUnsignedInt(values[6]) >>> 6) & 0x01;
    }

    /**
     * дискретный сигнал
     * @return авария афу мост
     */
    public int getCrashAfuMost() {
        if (getTypeMainModem() == 4)
            return (toUnsignedInt(values[15]) >>> 1) & 0x01;
        else
            return 0;
    }

    /**
     * дискретный сигнал
     * @return бхв включен
     */
    public int getIsBhv() {
        return getTypeBHV() > 0 ? 1 : 0;
    }

    /**
     * дискретный сигнал
     * @return Незапр. ответы
     */
    public int getAllowAnswer() {
        return (toUnsignedInt(values[6]) >>> 5) & 0x01;
    }

    /**
     * 0 – выключен
     * 1 – СУТП БХВ
     * 2 – РУТП БХВ
     * 7 – эмуляция
     * @return тип БХВ
     */
    public int getTypeBHV() {
        return toUnsignedInt(values[0]) & 0x07;
    }

    /**
     * 0 – нет модема
     * 1 – КСЛ
     * 2 – РК/АВП (СУЛР)
     * 3 – ВЭБР
     * 4 - МОСТ
     * 7 - нуль модем
     * @return тип дополнительного модема
     */
    public int getTypeSlaveModem() {
        return (toUnsignedInt(values[14]) >>> 3) & 0x07;
    }

    /**
     * 0 – нет модема
     * 1 – КСЛ
     * 2 – РК/АВП (СУЛР)
     * 3 – ВЭБР
     * 4 - МОСТ
     * 7 - нуль модем
     * @return тип основного модема
     */
    public int getTypeMainModem() {
        return toUnsignedInt(values[14]) & 0x07;
    }

    /**
     * @return Давление в ТМ хвоста в 1/100 атм
     */
    public double getPressTmTail() {
        return ((toUnsignedInt(values[2]) & 0x03) << 8 |     // hi
                toUnsignedInt(values[1])) * 0.01;                    // lo
    }

    /**
     * @return Давление в рабочей камере в 1/25 атм
     */
    public double getPressWorkCam() {
        if (getTypeBHV() == 1) {
            return (toUnsignedInt(values[8])) * 0.04;
        }
        else {
            return 0;
        }
    }

    /**
     * 0 закрыты
     * 1 открыт клапан служжебного торможения
     * 3 открыт клапан экстреннноо торможения
     * @return состояние клапанов
     */
    public int getStateValve() {
        if (getLinkMainCh() == 1 || getLinkSlaveCh() == 1)
            return (toUnsignedInt(values[2])  >>> 2) & 0x03;
        else
            return -1;
    }

    /**
     * 0 закрыть клапана
     * 1 служебное торможение до давления
     * 2 экстренное тормжение
     * @return Команда торм.
     */
    public int getCommandBrake() {
        return (toUnsignedInt(values[6])  >>> 2) & 0x03;
    }

    /**
     * 0: 11
     * 1: 11 - 11.5
     * 2: 11.5 - 12
     * 3: > 12
     * @return напряжение БХВ
     */
    public int getVoltageBHV() {
        if (getTypeBHV() == 1 && ((getLinkMainCh() == 1 || getLinkSlaveCh() == 1)))
            return (toUnsignedInt(values[7]) >>> 6) & 0x03;
        else
            return -1;
    }

    /**
     * @return Напр. БХВ при приёме в 0,2В от 10В
     */
    public double getVoltageGet() {
        if (getTypeBHV() == 1) {
            return ((toUnsignedInt(values[11]) & 0x0F) + 50) * 0.2;
        }
        else {
            return 0;
        }
    }

    /**
     * @return Напр. БХВ при прд. в 0,2В от 10В.
     */
    public double getVoltageSend() {
        if (getTypeBHV() == 1) {
            return ((toUnsignedInt(values[11]) >>> 4) + 50) * 0.2;
        }
        else {
            return 0;
        }
    }

}
