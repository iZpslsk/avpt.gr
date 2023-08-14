package avpt.gr.blocks32;

import org.threeten.bp.LocalDateTime;

/**
 * пассажирское и грузовое движение
 * наследует от Block32 дополняется полями для пассажирского и грузового движений
 */
public class Block32_gp extends Block32 {

    public static final int SIZE_BLOCK = 32;    //  размер посылки

    private LocalDateTime dateTime;                         // текущее время
    private int km;                                         // текущий километр
    private int pk;                                         // текущий пикет
    private double valLimTmp;                               // значение временного ограничения
    private int len_prof;                                   // длина профиля
    private int slope_prof;                                 // уклон профиля

    public Block32_gp(byte[] bytes) {
        super(bytes);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
}



