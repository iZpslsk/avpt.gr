package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * данные путевых объектов
 */
public class Block32_21_9 {

    private final byte[] values;
    private final int[] arrId;

    // для разных объектов не повторяются или одинаковые
    private double slope = Double.NaN;             // уклон м/км*100  (число со знаком) 16 бит
    private int radius;             // радиус кривизны пути м 16 бит
    private int limSpeed;           // ограничение км/ч 8 бит
    private int speed;              // скорость км/ч 8 бит
    private int typeTrafficLight;   // тип светофора
    private int idStation;          // id станции
    private long latitude;          // широта   32 бит
    private long longitude;         // долгота  32 бит
    private int azimuth;            // азимут   16 бит
    private int height;             // высота   16 бит
    private int typeObGPS;          // тип объекта мест коррекции координаты по GPS
    private int km;                 // километр
    private int picket;             // пикет

    private long coordinate;         //  линейная координата 24 бит

    private long lenLimit;           // длительность ограничений
    private long lenNeutral;         // длительность нейтральной вставки
    private long lenUKSPS;           // длительность УКСПС
    private long lenTryBake;         // длительность пробы тормозов
    private long lenDanger;          // обрывоопастные места

    public Block32_21_9(byte[] values) {
        this.values = values;
        arrId = new int[getAmountObj()];
        fillArrId();
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;    // lo
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
        return toUnsignedInt(values[0]) & 0x1F;
    }

    /**
     * заполнение массива arrId значениями id типов путевых объектов а также установка значений полей объектов
     */
    private void fillArrId() {
        int index = 1; // индекс первого путевого объекта
        for (int i = 0; i < arrId.length; i++) {
          arrId[i] = toUnsignedInt(values[index]) >> 2;
          setValues(arrId[i], index); // установка значений полей объекта
          index += getShiftForNext(arrId[i]); // индекс следующего путевого объекта
        }
    }

    private long getVal_8_1(int index) {
        return toUnsignedInt(values[index]);
        // следующий индекс index + 1
    }

    private long getVal_16_2(int index) {
        return  (long) toUnsignedInt(values[index + 1]) << 8 |  // hi
                toUnsignedInt(values[index]);            // lo
        // следующий индекс index + 2
    }

    private long getVal_24_3(int index) {
        return (long) toUnsignedInt(values[index + 2]) << 16 |  // hi
               (long) toUnsignedInt(values[index + 1]) << 8 |
               toUnsignedInt(values[index]);             // lo
        // следующий индекс index + 3
    }

    private long getVal_32_4(int index) {
        return (long) toUnsignedInt(values[index + 3]) << 24 |  // hi
               (long) toUnsignedInt(values[index + 2]) << 16 |
               (long) toUnsignedInt(values[index + 1]) << 8 |
               toUnsignedInt(values[index]);             // lo
        // следующий индекс index + 4
    }

    /**
     * установка значений полей объекта
     * @param id - идентификатор объекта пути
     * @param index - индекс для id объекта пути
     */
    private void setValues(int id, int index) {
        switch (id) {
            case 1: {   // профиль
                slope = (short) getVal_16_2(index += 1);
                coordinate = getVal_24_3(index + 2);
            //    System.out.println("x=" + coordinate + " slope=" + slope);
            }
            break;
            case 2: {   // план (радиусы кривизны пути)
                radius = (int) getVal_16_2(index += 1);
                coordinate = getVal_24_3(index + 2);
            }
            break;
            case 3: {   // постоянные ограничения
                lenLimit = getVal_24_3(index += 1);
                limSpeed = (int) getVal_8_1(index += 3);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 4: {   // нейтральные вставки
                lenNeutral = getVal_24_3(index += 1);
                coordinate = getVal_24_3(index + 3);
            }
            break;
            case 5: {   // УКСПС
                lenUKSPS = getVal_24_3(index += 1);
                coordinate = getVal_24_3(index + 3);
            }
            break;
            case 6: {   // пробы тормозов
                lenTryBake = getVal_24_3(index += 1);
                speed = (int)getVal_8_1(index += 3);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 7: {   // светофоры
                speed = (int)getVal_8_1(index += 1);
                typeTrafficLight = (int)getVal_8_1(index += 1);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 8: {   // Станции
                idStation = (int)getVal_8_1(index += 1);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 9: {   // переезды
                speed = (int)getVal_8_1(index += 1);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 10:    // КТСМ (ПОНАБ, ДИСК)
            case 11:    // Путевые генераторы САУТ
            case 12:    // Мосты
            case 13:    // Тоннели
            case 14:    // Опасные места
            case 15:    // Переходы
            case 16:    // Газопроводы
            case 17:    // Путепроводы
            case 18:    // Места подачи сигналов
                coordinate = getVal_24_3(index + 1);
            break;
            case 19: { // Обрывоопасные места
                lenDanger = getVal_24_3(index += 1);
                coordinate = getVal_24_3(index + 3);
            }
            break;
            case 20: {  // места коррекции координат GPS
                latitude = getVal_32_4(index += 1);
                longitude = getVal_32_4(index += 4);
                azimuth = (int)getVal_16_2(index += 4);
                height = (int)getVal_16_2(index += 2);
                typeObGPS = (int)getVal_8_1(index += 2);
                coordinate = getVal_24_3(index + 1);
            }
            break;
            case 63: {
                km = (int)getVal_16_2(index + 1);
                picket = (int)getVal_8_1(index += 2);
                coordinate = getVal_24_3(index + 1);
            }
            break;
        }
    }

    /**
     * @param id - идентификатор текущего объекта
     * @return смещение для следующего объекта
     */
    private int getShiftForNext(int id) {
        byte shift = -1;
        switch (id) {
            case 1:
            case 2:
            case 7:
                shift = 6;
            break;
            case 3:
            case 6:
                shift = 8;
            break;
            case 4:
            case 5:
            case 19:
            case 63:
                shift = 7;
            break;
            case 8:
            case 9:
                shift = 5;
            break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                shift = 4;
            break;
            case 20:
                shift = 17;
            break;
        }
        return shift;
    }

    /**
     * @return уклон м/км*100  (число со знаком) 16 бит
     */
    public double getSlope() {
        return slope;
    }

    /**
     * @return радиус кривизны пути м
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return ограничение км/ч
     */
    public int getLimSpeed() {
        return limSpeed;
    }

    /**
     * @return скорость км/ч
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @return тип светофора
     */
    public int getTypeTrafficLight() {
        return typeTrafficLight;
    }

    /**
     * @return id станции
     */
    public int getIdStation() {
        return idStation;
    }

    /**
     * @return широта
     */
    public long getLatitude() {
        return latitude;
    }

    /**
     * @return долгота
     */
    public long getLongitude() {
        return longitude;
    }

    /**
     * @return азимут
     */
    public int getAzimuth() {
        return azimuth;
    }

    /**
     * @return высота
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return тип объекта мест коррекции координаты по GPS
     */
    public int getTypeObGPS() {
        return typeObGPS;
    }

    /**
     * @return километр
     */
    public int getKm() {
        return km;
    }

    /**
     * @return пикет
     */
    public int getPicket() {
        return picket;
    }

    /**
     * @return линейная координата
     */
    public long getCoordinate() {
        return coordinate;
    }

    /**
     * @return длительность ограничений (м)
     */
    public long getLenLimit() {
        return lenLimit;
    }

    /**
     * @return длительность нейтральной вставки
     */
    public long getLenNeutral() {
        return lenNeutral;
    }

    /**
     * @return длительность обрывоопастного места
     */
    public long getLenDanger() {
        return lenDanger;
    }

    /**
     * @return длительность УКСПС
     */
    public long getLenUKSPS() {
        return lenUKSPS;
    }

    /**
     * @return длительность пробы тормозов
     */
    public long getLenTryBake() {
        return lenTryBake;
    }
}
