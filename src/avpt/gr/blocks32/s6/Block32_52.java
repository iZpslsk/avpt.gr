package avpt.gr.blocks32.s6;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

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
     * @return часов
     */
    private int getHour() {
        byte[] b = {values[0]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        byte[] b = {values[1]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        byte[] b = {values[2]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return время
     */
    public LocalTime getTime() throws DateTimeException {
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
//        if (sec > 59 || min > 59 || hour > 23)
//            return null;
//        else
//            return LocalTime.of(hour, min, sec);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.2 с
     * @return код
     */
    private int getKeyPush02() {
        return toUnsignedInt(values[3]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.4 с
     * @return код
     */
    private int getKeyPush04() {
        return toUnsignedInt(values[4]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.6 с
     * @return код
     */
    private int getKeyPush06() {
        return toUnsignedInt(values[5]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.8 с
     * @return код
     */
    private int getKeyPush08() {
        return toUnsignedInt(values[6]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 1.0 с
     * @return код
     */
    private int getKeyPush1() {
        return toUnsignedInt(values[7]);
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

    /**
     * @return Ток якоря
     */
    public double getAmperageAnchor() {
        return (byte)(toUnsignedInt(values[14])) * 8;
    }

    /**
     * @return Ток возбужедния
     */
    public double getAmperageExcitation() {
        return (byte)(toUnsignedInt(values[15])) * 8;
    }

    /**
     * @return Напряжение контактной сети
     */
    public double getVoltage() {
        return toUnsignedInt(values[16]) * 40.0;
    }

    /**
     * @return - Задание автоведения
     */
    public int getPowerTaskAuto() {
        int result = toUnsignedInt(values[18]);
        if (result >= 101)
            result = (result - 101) * ( toUnsignedInt(values[21]) | ((toUnsignedInt(values[23]) & 0x07) << 8) ) / 100;
        else if (result != 0)
            result = (101 - result) * ( toUnsignedInt(values[22]) | ((toUnsignedInt(values[23]) & 0x38) << 5) ) / 100 * -1;
        return result;
    }

    /**
     * @return - сила Реализованная
     */
    public int getPowerImplement() {
        int result = toUnsignedInt(values[10]);
        if (result >= 101)
            result = (result - 101) * ( toUnsignedInt(values[21]) | ((toUnsignedInt(values[23]) & 0x07) << 8) ) / 100;
        else if (result != 0)
            result = result * -1 * ( toUnsignedInt(values[22]) | ((toUnsignedInt(values[23]) & 0x38) << 5) ) / 100 ;
        return result;
    }

    /**
     * @return боксование юз
     */
    public int getPowerBox() {
        int result = toUnsignedInt(values[13]);
        if (getPowerImplement() >= 0) {
            return result * (toUnsignedInt(values[21]) | ((toUnsignedInt(values[23]) & 0x07) << 8)) / 100;
        }
        else {
            return result * -1 * (toUnsignedInt(values[22]) | ((toUnsignedInt(values[23]) & 0x38) << 5)) / 100;
        }
    }

    /**
     * @return позиция контроллера
     */
    public int getPosition() {
        int mode = toUnsignedInt(values[8]) & 0x03;
        int tad = (toUnsignedInt(values[8]) >>> 2)  & 0x03;
        if (mode == 1)
            return toUnsignedInt(values[11]) & 0x7F;
        else {
            switch (tad) {
                case 1: return 23;
                case 2: return 44;
                case 3: return 65;
                default: return 0;
            }
        }
    }

    /**
     * @return позиция задания автоведения
     */
    public int getPositionTaskAuto() {
        return toUnsignedInt(values[17]) & 0x7F;
    }

    /**
     * @return упр-е от автоведения _
     */
    public int getControlAuto() {
        return (toUnsignedInt(values[11]) >>> 7) & 0x01;
    }

    /**
     * @return Готов к автоведению
     */
    public int getReadyAuto() {
        return (toUnsignedInt(values[8]) >>> 7) & 0x01;
    }

    /**
     * @return ТМ
     */
    public int getTM() {
        return (toUnsignedInt(values[9]) >>> 5) & 0x01;
    }

    /**
     * @return ПСТ
     */
    public int getPST() {
        return (toUnsignedInt(values[9]) >>> 6) & 0x01;
    }

    /**
     * @return ЭПК
     */
    public int getEPK() {
        return (toUnsignedInt(values[9]) >>> 1) & 0x01;
    }

    /**
     * @return свисток ЭПК _
     */
    public int getWhistleEPK() {
        if ( (toUnsignedInt(values[9]) & 0x01) == 1 || ((toUnsignedInt(values[9]) & 0x02) >>> 1) == 1 )
            return 1;
        else
            return 0;
    }



}
