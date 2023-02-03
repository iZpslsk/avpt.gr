package avpt.gr.blocks32.passenger;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * объекты карты
 * 0x2D, 0x6D, 0x9D, 0x71, 0x44
 * интерфейс аналогичен 0x21 9;
 */
public class Block32_map_4 {

    private final byte[] values;
    private final int[] arrId;

    private double slope = Double.NaN;             // уклон
    private int limSpeed;           // ограничение км/ч
    private int speed;              // скорость км/ч
    private int typeTrafficLight;   // тип светофора
    private int km;                 // километр
    private int picket;             // пикет
    private long coordinate;        //  линейная координата

    public Block32_map_4(byte[] values) {
        this.values = values;
        arrId = new int[getAmountObj()];
        fillArrId();
    }

    /**
     * @return id подтипа
     */
    public int getSubId() {
        return toUnsignedInt(values[0]) >>> 4;
    }

    /**
     * @return массив идентификаторов объектов пути
     */
    public int[] getArrId() {
        return arrId;
    }

    /**
     * @return количество путевых объектов
     */
    private int getAmountObj() {
        int cnt = 0;
        if (toUnsignedInt(values[18]) > 0) cnt++;
        if (toUnsignedInt(values[20]) > 0) cnt++;
        if (toUnsignedInt(values[22]) > 0) cnt++;
        if (toUnsignedInt(values[24]) > 0) cnt++;
        return cnt;
    }

    /**
     * заполнение массива arrId значениями id типов путевых объектов а также установка значений полей объектов
     */
    private void fillArrId() {
        int index = 0;
        if (toUnsignedInt(values[18]) > 0) arrId[index++] = correctTypeObj(toUnsignedInt(values[18]));
        if (toUnsignedInt(values[20]) > 0) arrId[index++] = correctTypeObj(toUnsignedInt(values[20]));
        if (toUnsignedInt(values[22]) > 0) arrId[index++] = correctTypeObj(toUnsignedInt(values[22]));
        if (toUnsignedInt(values[24]) > 0) arrId[index] = correctTypeObj(toUnsignedInt(values[24]));
    }

    /**
     * @param val - код типа объекта по пассажирскому формату
     * @return - код типа объекта по грузовому формату
     */
    private int correctTypeObj(int val) {
        switch (val) {
            case 1: return 7;
            case 3: return 10;
            case 7: return 4;
            default: return 0;
        }
    }

    /**
     * @return номер перегона
     */
    public int getNumHaul() {
        return toUnsignedInt(values[2]) << 8 |
               toUnsignedInt(values[1]);
    }

    /**
     * @return абсолютная координата (м)
     */
    public long getAbsCoordinate() {
        return  (long)toUnsignedInt(values[5]) << 16 |
                (long)toUnsignedInt(values[4]) << 8 |
                      toUnsignedInt(values[3]);
    }

    /**
     * @return километр
     */
    public int getKm() {
        return toUnsignedInt(values[7]) << 8 |
                toUnsignedInt(values[6]);
    }

    /**
     * @return пикет
     */
    public int getPk() {
        return toUnsignedInt(values[8]);
    }

    /**
     * @return координата (м)
     */
    public long getCoordinate() {
        return  (long)toUnsignedInt(values[11]) << 16 |
                (long)toUnsignedInt(values[10]) << 8 |
                toUnsignedInt(values[9]);
    }

    /**
     * @return текущая скорость
     */
    public int getCurSpeed() {
        return (toUnsignedInt(values[12]) & 0xC0) << 2 |
                toUnsignedInt(values[13]);
    }

    /**
     * @return текущее ограничение скорости
     */
    public int getCurLimSpeed() {
        return (toUnsignedInt(values[12]) & 0x30) << 4 |
                toUnsignedInt(values[14]);
    }

    /**
     * @return расчетная скорость
     */
    public int getCalcSpeed() {
        return (toUnsignedInt(values[12]) & 0xC0) << 6 |
                toUnsignedInt(values[15]);
    }

    /**
     * @return текущий уклон
     */
    public double getCurSlope() {
       // System.out.println((short)(toUnsignedInt(values[17]) << 8 | toUnsignedInt(values[16])) * 10);
        return  (short)(toUnsignedInt(values[17]) << 8 | toUnsignedInt(values[16])) * 10;
    }

}
