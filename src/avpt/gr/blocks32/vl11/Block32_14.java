package avpt.gr.blocks32.vl11;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_14 {

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

    private byte[] values;

    public Block32_14(byte[] values) {
        this.values = values;
    }

    /**
     * @return Давление в УР - 1-я секция
     */
    public double getPressUR_s1() {
        double val = toUnsignedInt(values[0]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return Давление в ТМ - 1-я секция
     */
    public double getPressTM_s1() {
        double val = toUnsignedInt(values[1]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return Давление в ТЦ - 1-я секция
     */
    public double getPressTC_s1() {
        double val = toUnsignedInt(values[2]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return Давление в УР - 2-я секция
     */
    public double getPressUR_s2() {
        double val = toUnsignedInt(values[3]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return Давление в ТМ - 2-я секция
     */
    public double getPressTM_s2() {
        double val = toUnsignedInt(values[4]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return Давление в ТЦ - 2-я секция
     */
    public double getPressTC_s2() {
        double val = toUnsignedInt(values[2]) * 0.08549;
        return val > 4.0 && val < 100.0 ? (val - 4.0) / 1.6 : Double.NaN;
    }

    /**
     * @return часов
     */
    private int getHour() {
        byte[] b = {values[14]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return минут
     */
    private int getMinute() {
        byte[] b = {values[15]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return секунд
     */
    private int getSecond() {
        byte[] b = {values[16]};
        return (int) UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return время
     */
    public LocalTime getTime() throws DateTimeException {
        //return LocalTime.of(getHour(), getMinute(), getSecond());
        int hour = getHour();
        int min = getMinute();
        int sec = getSecond();
        return UtilsArmG.getTimeCheck(hour, min,sec);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.2 с
     * @return код
     */
    private int getKeyPush02() {
        return toUnsignedInt(values[17]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.4 с
     * @return код
     */
    private int getKeyPush04() {
        return toUnsignedInt(values[18]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.6 с
     * @return код
     */
    private int getKeyPush06() {
        return toUnsignedInt(values[19]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 0.8 с
     * @return код
     */
    private int getKeyPush08() {
        return toUnsignedInt(values[20]);
    }

    /**
     * Запись нажатия кнопок на клавиатуре 1.0 с
     * @return код
     */
    private int getKeyPush1() {
        return toUnsignedInt(values[21]);
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
     * @return ТМ 1-я секция
     */
    public int getTM1() {
        return toUnsignedInt(values[24]) & 0x01;
    }

    /**
     * @return БВ 1-я секция
     */
    public int getBV1() {
        return (toUnsignedInt(values[24]) >>> 1) & 0x01;
    }

    /**
     * @return ПСР 1-я секция
     */
    public int getPSR1() {
        return (toUnsignedInt(values[24]) >>> 2) & 0x01;
    }

    /**
     * @return КП 1-я секция
     */
    public int getKP1() {
        return (toUnsignedInt(values[24]) >>> 3) & 0x01;
    }

    /**
     * @return UKK 1-я секция
     */
    public int getUKK1() {
        return (toUnsignedInt(values[25]) >>> 1) & 0x01;
    }

    /**
     * @return кнопка полн служеб 1-я секция
     */
    public int getSerBrake1() {
        return (toUnsignedInt(values[25]) >>> 2) & 0x01;
    }

    /**
     * @return кнопка вмешательство машиниста 1-я секция
     */
    public int getInterDriver1() {
        return (toUnsignedInt(values[25]) >>> 3) & 0x01;
    }

    /**
     * @return МВ 1-я секция
     */
    public int getMV1() {
        return (toUnsignedInt(values[25]) >>> 4) & 0x01;
    }

    /**
     * @return С-СП 1-я секция
     */
    public int getSSP1() {
        return (toUnsignedInt(values[25]) >>> 5) & 0x01;
    }

    /**
     * @return ПР 1-я секция
     */
    public int getPR1() {
        return (toUnsignedInt(values[25]) >>> 6) & 0x01;
    }

    /**
     * @return ДБ 1-я секция
     */
    public int getDB1() {
        return (toUnsignedInt(values[25]) >>> 7) & 0x01;
    }

    /**
     * @return ТМ 2-я секция
     */
    public int getTM2() {
        return toUnsignedInt(values[26]) & 0x01;
    }

    /**
     * @return БВ 2-я секция
     */
    public int getBV2() {
        return (toUnsignedInt(values[26]) >>> 1) & 0x01;
    }

    /**
     * @return ПСР 2-я секция
     */
    public int getPSR2() {
        return (toUnsignedInt(values[26]) >>> 2) & 0x01;
    }

    /**
     * @return КП 2-я секция
     */
    public int getKP2() {
        return (toUnsignedInt(values[26]) >>> 3) & 0x01;
    }

    /**
     * @return UKK 2-я секция
     */
    public int getUKK2() {
        return (toUnsignedInt(values[27]) >>> 1) & 0x01;
    }

    /**
     * @return кнопка полн служеб 2-я секция
     */
    public int getSerBrake2() {
        return (toUnsignedInt(values[27]) >>> 2) & 0x01;
    }

    /**
     * @return кнопка вмешательство машиниста 2-я секция
     */
    public int getInterDriver2() {
        return (toUnsignedInt(values[27]) >>> 3) & 0x01;
    }

    /**
     * @return МВ 2-я секция
     */
    public int getMV2() {
        return (toUnsignedInt(values[27]) >>> 4) & 0x01;
    }

    /**
     * @return С-СП 2-я секция
     */
    public int getSSP2() {
        return (toUnsignedInt(values[27]) >>> 5) & 0x01;
    }

    /**
     * @return ПР 2-я секция
     */
    public int getPR2() {
        return (toUnsignedInt(values[27]) >>> 6) & 0x01;
    }

    /**
     * @return ДБ 2-я секция
     */
    public int getDB2() {
        return (toUnsignedInt(values[27]) >>> 7) & 0x01;
    }

    /**
     * 1 - зеленый
     * 2 - желтый
     * 4 - кж
     * 8 - красный
     * 16 - белый
     * @return алсн 1-я секция
     */
    public int getALSN1() {
        return (toUnsignedInt(values[25]) & 0x01) << 4 |    // hi
                toUnsignedInt(values[24]) >>> 4;            // lo
    }

    public int getALSN2() {
        return (toUnsignedInt(values[27]) & 0x01) << 4 |    // hi
                toUnsignedInt(values[26]) >>> 4;            // lo
    }
}
