package avpt.gr.blocks32.s5;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_54 {

    private final byte[] values;

    public Block32_54(byte[] values) {
        this.values = values;
    }

    /**
     * @return давление УР
     */
    public double getPressUR() {
        return (toUnsignedInt(values[3]) << 8 | toUnsignedInt(values[2])) * 0.01;
    }

    /**
     * @return давление ТЦ
     */
    public double getPressTC() {
        return (toUnsignedInt(values[5]) << 8 | toUnsignedInt(values[4])) * 0.01;
    }

    /**
     * @return давление ТМ
     */
    public double getPressTM() {
        return (toUnsignedInt(values[7]) << 8 | toUnsignedInt(values[6])) * 0.01;
    }

    /**
     * @return давление НМ
     */
    public double getPressNM() {
        return (toUnsignedInt(values[9]) << 8 | toUnsignedInt(values[8])) * 0.01;
    }

    /**
     * @return давление ТЦ2
     */
    public double getPressTC2() {
        return (toUnsignedInt(values[11]) << 8 | toUnsignedInt(values[10])) * 0.01;
    }

    /**
     * @return КАЭТ
     */
    public int getKAET() {
        return toUnsignedInt(values[26]) > 0 ? 1 : 0;
    }

    /**
     *     1:
     *     2:
     *     4:
     *     8:
     *     16:
     *     32:
     *     64:
     * @return сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[23]) & 0x7F;
    }

    /**
     *     1: 2
     *     2: 3
     *     3: 4
     *     4: 5
     *     5: 6
     * @return - ККБТ
     */
    public int getKKBT() {
        return toUnsignedInt(values[25]) & 0x0F;
    }

    /**
     *     1:  отпуск
     *     2:  поездное
     *     3:  перекрыша без питания
     *     4:  перекрыша c питанием
     *     5:  торможение с замедлением
     *     6:  торможение служебное
     *     7:  торможение экстренное
     * @return - состояние пневматики 1-я секция
     */
    public int getPneumaticState_s1() {
        return toUnsignedInt(values[15]);
    }

    /**
     * @return - состояние пневматики 2
     */
    public int getPneumaticState_s2() {
        return toUnsignedInt(values[12]);
    }

    /**
     *     $FF:  Нет команд БЛОК
     *     $11:  Для отпуска перевести ККМ в I
     *     $13:  Необходимо перевести ККМ в II
     *     $15:  Работа в составе БЛОК
     *     $51..$55:  Торможение от ККМ
     *     $0:  Неизвестное задание
     *   else
     *     Result  Управление от БЛОК
     * @return - Статус_БЛОК
     */
    public int getStatusBlock() {
        return toUnsignedInt(values[14]);
    }

}
