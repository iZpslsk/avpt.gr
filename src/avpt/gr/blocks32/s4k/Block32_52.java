package avpt.gr.blocks32.s4k;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_52 {

    private static final int KEY_START = 0x4d;         //
    private static final int KEY_F1 = 0x7e;         //
    private static final int KEY_ENTER = 0x3a;         //
    private static final int KEY_ESC = 0x2b;         //
    private static final int KEY_DEL = 0x71;         //
    private static final int KEY_UP = 0x63;         //
    private static final int KEY_DOWN = 0x60;         //
    private static final int KEY_LEFT = 0x61;         //
    private static final int KEY_RIGHT = 0x6a;         //
    private static final int KEY_ZERO = 0x70;         //
    private static final int KEY_ONE = 0x69;         //
    private static final int KEY_TWO = 0x72;         //
    private static final int KEY_THREE = 0x7a;         //
    private static final int KEY_FOUR = 0x6b;         //
    private static final int KEY_FIVE = 0x73;         //
    private static final int KEY_SIX = 0x74;         //
    private static final int KEY_SEVEN = 0x6c;         //
    private static final int KEY_EIGHT = 0x75;         //
    private static final int KEY_NINE = 0x7d;         //

    private final byte[] values;

    public Block32_52(byte[] values) {
        this.values = values;
    }

    /**
     * @return Ток якоря-1 1секции
     */
    public int getAmperageAnchor1_s1() {
        return Math.abs ((short)(toUnsignedInt(values[0]) << 8 | toUnsignedInt(values[1])));
    }

    /**
     * @return Ток якоря-2 1секции
     */
    public int getAmperageAnchor2_s1() {
        return Math.abs((short)(toUnsignedInt(values[2]) << 8 | toUnsignedInt(values[3])));
    }

    /**
     * @return Ток якоря-3 1секции
     */
    public int getAmperageAnchor3_s1() {
        return Math.abs ((short)(toUnsignedInt(values[4]) << 8 | toUnsignedInt(values[5])));
    }

    /**
     * @return Ток якоря-4 1секции
     */
    public int getAmperageAnchor4_s1() {
        return Math.abs ((short)(toUnsignedInt(values[6]) << 8 | toUnsignedInt(values[7])));
    }

    /**
     * @return Ток возбужедния-1 1сек
     */
    public int getAmperageExcitation1_s1() {
        return Math.abs ((short)(toUnsignedInt(values[8]) << 8 | toUnsignedInt(values[9])));
    }

    /**
     * @return Ток возбужедния-2 1сек
     */
    public int getAmperageExcitation2_s1() {
        return Math.abs ((short)(toUnsignedInt(values[10]) << 8 | toUnsignedInt(values[11])));
    }

    /**
     * @return Ток возбужедния-3 1сек
     */
    public int getAmperageExcitation3_s1() {
        return Math.abs ((short)(toUnsignedInt(values[12]) << 8 | toUnsignedInt(values[13])));
    }

    /**
     * @return Ток возбужедния-4 1сек
     */
    public int getAmperageExcitation4_s1() {
        return Math.abs ((short)(toUnsignedInt(values[14]) << 8 | toUnsignedInt(values[15])));
    }

    /**
     * @return Напряжение двигателя 1 1 секция
     */
    public int getVoltageEngine1_s1() {
        return Math.abs ((short)(toUnsignedInt(values[16]) << 8 | toUnsignedInt(values[17])));
    }

    /**
     * @return Напряжение двигателя 2 1 секция
     */
    public int getVoltageEngine2_s1() {
        return Math.abs ((short)(toUnsignedInt(values[18]) << 8 | toUnsignedInt(values[19])));
    }

    /**
     * @return Напряжение двигателя 3 1 секция
     */
    public int getVoltageEngine3_s1() {
        return Math.abs ((short)(toUnsignedInt(values[20]) << 8 | toUnsignedInt(values[21])));
    }

    /**
     * @return Напряжение двигателя 4 1 секция
     */
    public int getVoltageEngine4_s1() {
        return Math.abs ((short)(toUnsignedInt(values[22]) << 8 | toUnsignedInt(values[23])));
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.2 с
     * @return код
     */
    public int getKeyPush02() {
        return toUnsignedInt(values[24]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.4 с
     * @return код
     */
    public int getKeyPush04() {
        return toUnsignedInt(values[25]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.6 с
     * @return код
     */
    public int getKeyPush06() {
        return toUnsignedInt(values[26]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.8 с
     * @return код
     */
    public int getKeyPush08() {
        return toUnsignedInt(values[27]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 1.0 с
     * @return код
     */
    public int getKeyPush1() {
        return toUnsignedInt(values[28]);
    }

    private int getPushSecond(int codeKey) {
        if (getKeyPush1() == codeKey) return  10;
        else if (getKeyPush08() == codeKey) return 8;
        else if (getKeyPush06() == codeKey) return 6;
        else if (getKeyPush04() == codeKey) return 4;
        else if (getKeyPush02() == codeKey) return 2;
        else return -1;
    }

    /**
     * нажатие клавиши пуск (секунд)
     * @return - секунд
     */
    public int getKeyStart() {
        return getPushSecond(KEY_START);
    }

    /**
     * нажатие клавиши F1 (секунд)
     * @return - секунд
     */
    public int getKeyF1() {
        return getPushSecond(KEY_F1);
    }

    /**
     * нажатие клавиши Enter (секунд)
     * @return - секунд
     */
    public int getKeyEnter() {
        return getPushSecond(KEY_ENTER);
    }

    /**
     * нажатие клавиши Esc (секунд)
     * @return - секунд
     */
    public int getKeyEsc() {
        return getPushSecond(KEY_ESC);
    }

    /**
     * нажатие клавиши Del (секунд)
     * @return - секунд
     */
    public int getKeyDel() {
        return getPushSecond(KEY_DEL);
    }

    /**
     * нажатие клавиши Up (секунд)
     * @return - секунд
     */
    public int getKeyUp() {
        return getPushSecond(KEY_UP);
    }

    /**
     * нажатие клавиши Down (секунд)
     * @return - секунд
     */
    public int getKeyDown() {
        return getPushSecond(KEY_DOWN);
    }

    /**
     * нажатие клавиши Left (секунд)
     * @return - секунд
     */
    public int getKeyLeft() {
        return getPushSecond(KEY_LEFT);
    }

    /**
     * нажатие клавиши Right (секунд)
     * @return - секунд
     */
    public int getKeyRight() {
        return getPushSecond(KEY_RIGHT);
    }

    /**
     * нажатие клавиши Zero (секунд)
     * @return - секунд
     */
    public int getKeyZero() {
        return getPushSecond(KEY_ZERO);
    }

    /**
     * нажатие клавиши One (секунд)
     * @return - секунд
     */
    public int getKeyOne() {
        return getPushSecond(KEY_ONE);
    }

    /**
     * нажатие клавиши Two (секунд)
     * @return - секунд
     */
    public int getKeyTwo() {
        return getPushSecond(KEY_TWO);
    }

    /**
     * нажатие клавиши Three (секунд)
     * @return - секунд
     */
    public int getKeyThree() {
        return getPushSecond(KEY_THREE);
    }

    /**
     * нажатие клавиши Four (секунд)
     * @return - секунд
     */
    public int getKeyFour() {
        return getPushSecond(KEY_FOUR);
    }

    /**
     * нажатие клавиши Five (секунд)
     * @return - секунд
     */
    public int getKeyFive() {
        return getPushSecond(KEY_FIVE);
    }

    /**
     * нажатие клавиши Six (секунд)
     * @return - секунд
     */
    public int getKeySix() {
        return getPushSecond(KEY_SIX);
    }

    /**
     * нажатие клавиши Seven (секунд)
     * @return - секунд
     */
    public int getKeySeven() {
        return getPushSecond(KEY_SEVEN);
    }

    /**
     * нажатие клавиши Eight (секунд)
     * @return - секунд
     */
    public int getKeyEight() {
        return getPushSecond(KEY_EIGHT);
    }

    /**
     * нажатие клавиши Nine (секунд)
     * @return - секунд
     */
    public int getKeyNine() {
        return getPushSecond(KEY_NINE);
    }
}
