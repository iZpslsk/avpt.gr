package avpt.gr.blocks32.s5k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;
import static avpt.gr.train.Train.S5K;
import static avpt.gr.train.Train.S5K_2;

/**
 * Обмен с краном 130
 */
public class Block32_55 {

    private final byte[] values;
    private final int typeLoc;

    public Block32_55(int typeLoc, byte[] values) {
        this.values = values;
        this.typeLoc = typeLoc;
    }

    /**
     * UKTOL_SYS_DATA - байт 0
     * 0xFF - Режим работы от ручки ККМ. Нет команд от системы БЛОК
     * 0x11 - Для отпуска необходимо кратковременно перевести ручку ККМ в положение «Сверхзарядка»
     * 0x13 - Необходимо перевести ручку в положение «Поездное»
     * 0x15 - Режим работы в составе системы БЛОК
     * 0x51-0x55 - Режим работы от ручки ККМ.. Торможение.
     * Другое - Управление от системы БЛОК. Отпуск запрещен.
     * @return - Статус системы исполнения торможения
     */
    public int getStatusSystemBrake() {
        if (typeLoc == S5K) {
            int val = toUnsignedInt(values[6]);
            switch (val) {
                case 0x52:
                case 0x53:
                case 0x54:
                case 0x55:
                    val = 0x51;
            }
            return val;
        }
        else
            return 0;
    }

    /**
     * UKTOL_SYS_DATA - байт 1
     * 1-I
     * 2 - II
     * 3 - III
     * 4 - IV
     * 5 - Va
     * 6 - V
     * 7 - VI
     * @return - Виртуальное положение ручки КМ при исполнении команд управления
     */
    public int getVirPosKM() {
        if (typeLoc == S5K)
            return toUnsignedInt(values[7]);
        else
            return 0;
    }

    /**
     * UKTOL_SYS_STATE - байт 1
     * биты:
     * 0 - Положение 1 ККМ (1)
     * 1 - Положение 2 ККМ (2)
     * 2 - Положение 3 ККМ (4)
     * 3 - Положение 4 ККМ (8)
     * 4 - Положение 5a ККМ (16) (0x10)
     * 5 - Положение 5 ККМ (32) (0x20)
     * 6 - Положение 6 ККМ (64) (0x40)
     * 7 - резерв
     * @return - положение ручки ККМ при исполнении команд управления
     */
    public int getPosKKM() {
        if (typeLoc == S5K)
            return toUnsignedInt(values[8]);
        else
            return 0;
    }

}
