package avpt.gr.blocks32;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

class Block32 {

    static final int SIZE_BLOCK = 32;        //  размер посылки

    private final byte id;                                  // идентификатор типа посылки
    private byte[] values;                                  // данные посылки
    private final byte crc16H;                              // кс - старший байт
    private final byte crc16L;                              // кс - младший байт
    private final int crc16;                                // кс - факт

    private int coordinate;                                 // текущая координата
    private int second;										// текущая координата

    Block32(byte[] bytes) {
        id = bytes[0];
        values = Arrays.copyOfRange(bytes, 1, SIZE_BLOCK - 2);
        crc16H = bytes[30];
        crc16L = bytes[31];
        crc16 = getCrc16(Arrays.copyOfRange(bytes, 0, SIZE_BLOCK - 2));
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    /**
     *
     * @return идентификатор типа посылки
     */
    public int getId() {
        return toUnsignedInt(id);
    }

    /**
     *
     * @return контрольная сумма из посылки
     */
    private int getCrc() {
        return toUnsignedInt(crc16H) << 8 | toUnsignedInt(crc16L);
    }

    /**
     * @param nByte - порядковый номер байта 0-29
     * @return  беззнаковое значение байта
     */
    public int get(int nByte) {
        return toUnsignedInt(values[nByte]);
    }

    /**
     *
     * @return  знаковый массив байт данных
     */
    public byte[] getValues() {
        return values;
    }

    /**
     * @return корректна ли контрольная сумма
     */
    boolean crcTruth() {
        return getCrc() == crc16;
    }

    /**
     * @param bytes - массив байт
     * @return расчетная контрольная сумма
     */
    private int getCrc16(final byte[] bytes) {

        int crc = 0;
        for (byte b : bytes) {
            crc = crc ^ b << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) == 0x8000)
                    crc = (crc << 1) ^ 0x1021;
                else
                    crc = crc << 1;
            }
        }
        return crc & 0xFFFF;
    }

    /**
     * получение кода типа движения для текущей посылки: 1-пасс, 2-тэп70, 3-гр, 4-манев, 7-асим
     * @param block32 - текущая посылка
     * @param cur_type_loc_tep - текущий тип локомотива тепловоза - получаем из посылок 0x76 или 0x78
     *(текущих или предшествующих в результате итерации) если посылки 0x76 или 0x78 не встречаются передаем -1;
     * @return - 1-пасс, 2-тэп70, 3-гр, 4-манев, 7-асим
     */
    public static byte makeCodeMove(Block32 block32, int cur_type_loc_tep) {

        int id = block32.getId();
        byte[] values = block32.getValues();

        if (id == 0xAA) {
            switch (cur_type_loc_tep) {
                case 1:
                case 2:
                case 3:
                    return 2;
                default:
                    return 1;
            }
        }

        switch (id) {
            case 21:
            case 61:
                if ((values[0] >>> 5) == 2) return 3;   // freight
                else return 1;                          // pass
            case 91:
                return 1;                               // pass
            case 79:
                return 2;                               // pass_t
        }

        switch ((id & 0Xf0) >>> 4) {
            case 0x2:
            case 0x4:
            case 0x6:
            case 0x9:
                return 1;                       // pass
            case 0x1:
            case 0x5:
                return 3;                       // freight
            case 0x7:
                switch (cur_type_loc_tep) {
                    case 1:
                    case 2:
                    case 3:
                        return 2;               // tep70
                    default:
                        return 4;               // freight_t
                }
            case 0xC:
                return 7;                       // asim
            default:
                return -1;
        }
    }

}
