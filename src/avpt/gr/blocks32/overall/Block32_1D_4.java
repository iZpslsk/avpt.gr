package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * посекундный дополнительный кадр данных
 */
public class Block32_1D_4 {

    private final byte[] values;

    public Block32_1D_4(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x1D
     */
    public int getSubId() {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     * Режим управления ВСЦ:
     * 0 – режим ведомого ВСЦ выключен
     * 1 – следование в виртуальной сцепке
     * 2 – переход на АЛСН
     * 3 – нет открытия светофора
     * @return режим следования в всц
     */
    public int getMoveVSC() {
        return toUnsignedInt(values[5]) & 0x07;
    }

    /**
     * @return текущий км
     */
    public int getKm() {
        return toUnsignedInt(values[7]) << 8  |  // hi
               toUnsignedInt(values[6]);         // lo
    }

    /**
     * @return текущий пикет
     */
    public int getPicket() {
        return toUnsignedInt(values[8]);
    }

    /**
     * @return пробег сначала маршрута
     */
    public int getDistance() {
        return toUnsignedInt(values[11]) << 16 | // hi
                toUnsignedInt(values[10]) << 8 |
                toUnsignedInt(values[9]);         // lo
    }

    /**
     * @return скорость
     */
    public int getSpeed() {
        return (toUnsignedInt(values[12]) & 0xC0) << 2 | // hi
                toUnsignedInt(values[13]);               // lo
    }

    /**
     * @return текущее ограничение скорости
     */
    public int getCurSpeedLimit() {
        return (toUnsignedInt(values[12]) & 0x30) << 4 | // hi
                toUnsignedInt(values[14]);               // lo
    }

    /**
     * @return расчетная скорость
     */
    public int getCalcSpeed() {
        return (toUnsignedInt(values[12]) & 0x0C) << 6 | // hi
                toUnsignedInt(values[15]);               // lo
    }

    /**
     * @return уклон профиля
     */
    public int getSlope() {
        return toUnsignedInt(values[17]) << 8 |  // hi
               toUnsignedInt(values[16]);        // lo
    }

    /**
     * @return режим задания эл торм
     */
    public int getModeTaskBrake() {
        return toUnsignedInt(values[18]) >>> 5;
    }

    /**
     * @return управление тормозными заданиями
     */
    public int getControlBrakeTask() {
        return toUnsignedInt(values[18]) & 0x1F;
    }

    /**
     * @return запрет ЭТ
     */
    public int getBanET() {
        return (toUnsignedInt(values[19]) >>> 7) & 0x01;
    }

    /**
     * @return запрет ПТ
     */
    public int getBanPT() {
        return (toUnsignedInt(values[19]) >>> 6) & 0x01;
    }
}
