package avpt.gr.blocks32;

import org.threeten.bp.LocalDateTime;

import java.util.*;
import static avpt.gr.common.UtilsArmG.toUnsignedInt;

public class Block32 {

    public static final byte SIZE_VALUES = 29;  //  размер сегмента данных посылки
    public static final int SIZE_BLOCK = 32;    //  размер посылки


    private final byte id;                                  // идентификатор типа посылки
    private byte[] values;// = new byte[SIZE_VALUES];       // сегмент данных посылки
    private final byte crc16H;                              // кс - старший байт
    private final byte crc16L;                              // кс - младший байт
    private final int crc16;                                // кс - факт
    private int second;									    // текущая секунда
    private LocalDateTime dateTime;                                 // текущее время
    private int coordinate;                                 // текущая координата
    private int km;                                         // текущий километр
    private int pk;                                         // текущий пикет
    private double valLimTmp;                                  // значение временного ограничения
    private int len_prof;                                   // длина профиля
    private int slope_prof;                                 // уклон профиля

    public Block32(byte[] bytes) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getPk() {
        return pk;
    }

    public int getLen_prof() {
        return len_prof;
    }

    public void setLen_prof(int len_prof) {
        this.len_prof = len_prof;
    }

    public double getSlope_prof() {
        return slope_prof * 0.01;
    }

    public void setSlope_prof(int slope_prof) {
        this.slope_prof = slope_prof;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public double getValLimTmp() {
        return valLimTmp;
    }

    public void setValLimTmp(double valLimTmp) {
        this.valLimTmp = valLimTmp;
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
     * @return  массив байт данных (знаковый)
     */
    public byte[] getValues() {
        return values;
    }
    
    /**
     * @param bytes - массив байт
     * @return расчетная контрольная сумма
     */
    private static int getCrc16(final byte[] bytes) {

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
     * @return корректна ли контрольная сумма
     */
    public boolean crcTruth() {
        return getCrc() == crc16;
    }

    /**
     * @param values - массив данных блока
     * @return - подтип для блока 0x21 (0x61)
     */
    public static int getSubId_0x21(byte[] values) {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
               (toUnsignedInt(values[28]) & 0x60) >>> 5;     // lo
    }

    /**
     * @param values - массив данных блока
     * @return - подтип для блоков 0x1D или 0x21
     */
    public static int getSubId_0x1D(byte[] values) {
        return toUnsignedInt(values[0])  >>> 4;
    }

    /**
     *
     * @param values - массив данных блока
     * @return подтип для блокав асим
     */
    public static int getSubId_ASIM(byte[] values) {
        return toUnsignedInt(values[28]);
    }

    public static int getSubId(int id, byte[] values) {
        if (id == 0x21) return getSubId_0x21(values);
        if (id == 0x1D) return getSubId_0x1D(values);
        if (id == 0x2D) return getSubId_0x1D(values);
        if (id == 0x6D) return getSubId_0x1D(values);
        if (id == 0x9D) return getSubId_0x1D(values);
        if (id == 0x71) return getSubId_0x1D(values);
        if (id == 0x44) return getSubId_0x1D(values);
        if (id >= 0xC0 && id <= 0xCF) return getSubId_ASIM(values);
        return 0;
    }
}



