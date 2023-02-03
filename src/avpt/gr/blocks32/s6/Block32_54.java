package avpt.gr.blocks32.s6;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_54 {

    private byte[] values;

    public Block32_54(byte[] values) {
        this.values = values;
    }


    /**
     * @return Давление в УР - 1-я секция
     */
    public double getPressUR() {
        return (toUnsignedInt(values[0]) & 0x7F) * 0.1;
    }

    /**
     * @return давление ТЦ
     */
    public double getPressTC() {
        return (toUnsignedInt(values[1]) & 0x7F) * 0.1;
    }

    /**
     * @return давление ТМ
     */
    public double getPressTM() {
        return (toUnsignedInt(values[2]) & 0x7F) * 0.1;
    }

    /**
     * @return давление НМ
     */
    public double getPressNM() {
        return (toUnsignedInt(values[3]) & 0x7F) * 0.1;
    }

    /**
     * @return давление ТЦ
     */
    public double getPressTC2() {
        return (toUnsignedInt(values[4]) & 0x7F) * 0.1;
    }

    /**
     * @return скорость БС-ДПС
     */
    public double getSpeed_BS_DPS() {
        return (toUnsignedInt(values[12]) << 8 |    // hi
                toUnsignedInt(values[11])) / 256.0;         // lo
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
        return toUnsignedInt(values[7]);
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
    public int getPneumaticStatusBlock() {
        return toUnsignedInt(values[6]);
    }

    /**
     *     1: I - отпуск
     *     2: II - поездное
     *     4: III - перекрыша без питания
     *     8: IV - перекрыша с питанием
     *     16: Va - торможение с замедлением
     *     32: V - торможение служебное
     *     64: VI - торможение экстренное
     * @return сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[8]) & 0x7F;
    }
}
