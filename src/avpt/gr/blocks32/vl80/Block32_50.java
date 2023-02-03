package avpt.gr.blocks32.vl80;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_50 {

    private final byte[] values;

    public Block32_50(byte[] values) {
        this.values = values;
    }

    /**
     * @return напряжение двигателя 1-я секция
     */
    public double getVoltageEngine_s1() {
      return Math.abs( (short) (toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])) ) * 0.06103515625;
    }

    /**
     * @return Ток якоря-1 1-й секции
     */
    public double getAmperageAnchor1_s1() {
        return Math.abs( (short) (toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])) ) * 0.091552734375;
    }

    /**
     * @return Ток якоря-2 1-й секции
     */
    public double getAmperageAnchor2_s1() {
        return Math.abs( (short) (toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])) ) * 0.091552734375;
    }

    /**
     * @return Ток возбужедния 1-й секции
     */
    public double getAmperageExcitation_s1() {
        return Math.abs( (short) (toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])) ) * 0.091552734375;
    }

    /**
     * @return напряжение двигателя 2-я секция
     */
    public double getVoltageEngine_s2() {
        return Math.abs( (short) (toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])) ) * 0.06103515625;
    }

    /**
     * @return Ток якоря-1 2-й секции
     */
    public double getAmperageAnchor1_s2() {
        return Math.abs( (short) (toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])) ) * 0.091552734375;

    }

    /**
     * @return Ток якоря-2 2-й секции
     */
    public double getAmperageAnchor2_s2() {
        return Math.abs( (short) (toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])) ) * 0.091552734375;
    }

    /**
     * @return Ток возбужедния 2-й секции
     */
    public double getAmperageExcitation_s2() {
        return Math.abs( (short) (toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])) ) * 0.091552734375;
    }

    /**
     * @return ЭПК1
     */
    public int getEPK1() {
        return (toUnsignedInt(values[8]) >>> 6) & 0x01;
    }

    /**
     * @return ПСТ1
     */
    public int getPST1() {
        return (toUnsignedInt(values[8]) >>> 5) & 0x01;
    }

    /**
     * @return ЭПК2
     */
    public int getEPK2() {
        return (toUnsignedInt(values[9]) >>> 6) & 0x01;
    }

    /**
     * @return ПСТ2
     */
    public int getPST2() {
        return (toUnsignedInt(values[9]) >>> 5) & 0x01;
    }

    /**
     * 9 - отпуск
     * 25 - поездное
     * 12 - перекрыша без питания
     * 28 - перекрыша с питанием
     * 26 - торможение служебное
     * 10 - торможение c замедлением
     * 2 - торможение экстренное
     * @return Сигналы ККМ 1-я секц
     */
    public int getKKM1() {
        return toUnsignedInt(values[8]) & 0x1F;
    }

    /**
     * @return KKM 2-я секция
     */
    public int getKKM2() {
        return toUnsignedInt(values[9]) & 0x1F;
    }


}
