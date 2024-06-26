package avpt.gr.blocks32.kz8;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32_52 {

    private static final int KEY_START = 0x01;         //
    private static final int KEY_F1 = 0x80;         //
    private static final int KEY_ENTER = 0x04;         //
    private static final int KEY_ESC = 0x02;         //
    private static final int KEY_DEL = 0x40;         //
    private static final int KEY_UP = 0x03;         //
    private static final int KEY_DOWN = 0x06;         //
    private static final int KEY_LEFT = 0x05;         //
    private static final int KEY_RIGHT = 0x07;         //
    private static final int KEY_ZERO = 0x20;         //
    private static final int KEY_ONE = 0x21;         //
    private static final int KEY_TWO = 0x22;         //
    private static final int KEY_THREE = 0x23;         //
    private static final int KEY_FOUR = 0x24;         //
    private static final int KEY_FIVE = 0x25;         //
    private static final int KEY_SIX = 0x26;         //
    private static final int KEY_SEVEN = 0x27;         //
    private static final int KEY_EIGHT = 0x28;         //
    private static final int KEY_NINE = 0x29;         //

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
     * @return - Макс. тяга
     */
    public int getPowerMax() {
        int val = toUnsignedInt(values[9]);
        return val == 0xFF ? 0 : val * 10;
    }

    /**
     * @return - Макс. рекуп
     */
    public int getPowerMaxRec() {
        int val = toUnsignedInt(values[10]);
        return val == 0xFF ? 0 : val * -10;
    }

    /**
     * @return позиция контроллера
     */
    public int getPosition() {
        return values[11];
    }

    /**
     * @return Давление в УР
     */
    public double getPressUR() {
        return (toUnsignedInt(values[19]) << 8 | toUnsignedInt(values[18])) * 0.0010197162129779;
    }

    /**
     * @return давление ТЦ
     */
    public double getPressTC() {
        return (toUnsignedInt(values[21]) << 8 | toUnsignedInt(values[20])) * 0.0010197162129779;
    }

    /**
     * @return давление ТМ
     */
    public double getPressTM() {
        return (toUnsignedInt(values[17]) << 8 | toUnsignedInt(values[16])) * 0.0010197162129779;
    }

    /**
     * @return давление НМ
     */
    public double getPressNM() {
        return (toUnsignedInt(values[23]) << 8 | toUnsignedInt(values[22])) * 0.0010197162129779;
    }

    /**
     *     0xF:
     *     0xA:
     *     0x9:
     *     0xC:
     *     0x3:
     *     0x6:
     *     0x0:
     * @return сигналы ККМ
     */
    public int getKKM() {
        return toUnsignedInt(values[12]);
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
        return toUnsignedInt(values[13]);
    }

    /**
     * @return Готов к автоведению
     */
    public int getReadyAuto() {
        return toUnsignedInt(values[8]) & 0x01;
    }

    /**
     * @return кнопка вмешательство машиниста
     */
    public int getInterDriver() {
        return (toUnsignedInt(values[8]) >>> 2) & 0x01;
    }

    /**
     * @return срабатывание ТМ
     */
    public int getTM() {
        return (toUnsignedInt(values[8]) >>> 1) & 0x01;
    }

    /**
     * @return ГВ
     */
    public int getGV() {
        return (toUnsignedInt(values[8]) >>> 3) & 0x01;
    }

    /**
     * @return экстренное торможение
     */
    public int getEmergencyBrake() {
        return (toUnsignedInt(values[8]) >>> 4) & 0x01;
    }

    /**
     * @return боксование
     */
    public int getBox() {
        return (toUnsignedInt(values[8]) >>> 5) & 0x01;
    }

    /**
     * @return юз от tcu
     */
    public int getUzTcu() {
        return (toUnsignedInt(values[8]) >>> 7) & 0x01;
    }

    /**
     * @return юз от wsp
     */
    public int getUzWsp() {
        return (toUnsignedInt(values[8]) >>> 6) & 0x01;
    }

    public int getCntSection() {
        return toUnsignedInt(values[15]);
    }

}
