package avpt.gr.blocks32.passenger;

import avpt.gr.common.UtilsArmG;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * (=) 0x26
 * (~) 0x66
 * (=~) 0x96
 */

public class Block32_26_66_96 {

    private final byte[] values;

    public Block32_26_66_96(byte[] values) {
        this.values = values;
    }

    /**
     * @return Номер версии ПО регистратора
     */
    public long getVerPO() {
        byte[] b = {values[0]};
        return UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return Заводской номер регистратора
     */
    public long getNumFactory() {
        byte[] b = {values[5], values[4], values[3], values[6]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return Заводской номер БИВ1
     */
    public long getNumFactoryBiv1() {
        byte[] b = {values[11], values[10], values[9]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return Заводской номер БИВ2
     */
    public long getNumFactoryBiv2() {
        byte[] b = {values[14], values[13], values[12]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return номер локомотива
     */
    public long getNumLoc() {
        byte[] b = {values[17], values[16], values[15]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return номер локомотива для 2-х секционных
     */
    public long getNumLoc2() {
        byte[] b = {values[23], values[22], values[21]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     * @return Заводской номер картриджа
     */
    public long getFactoryNumCart() {
        byte[] b = {values[27], values[26], values[25], values[24]};
        return  UtilsArmG.BCDToDecimal(b);
    }

    /**
     *ЧС-7	ММ	            1
     * ЧС-7 ЕКС	ММ	        2
     * ЧС-7 КАУД-Ф	БР	    3
     * ЧС-2 Красн.Знамя	ММ	4
     * ЧС-2 КАУД	БР	    5
     * ЧС-2 ЕКС-2	БР	    6
     * ЧС-2К УСАВПП	ММ	    7
     * ЧС-2К КАУД	БР  	8
     * ЧС-2К ЕСАУП	БР  	9
     * ЧС-2Т КАУД	БР  	10
     * ЧС-6/200 КАУД	БР	11
     * ЧС-4Т КАУД	БР	    12
     * ЭП1 КАУД	БР	        13
     * ЧС-8 КАУД	БР	    14
     * ЭП1У КАУД	БР	    15
     * ЧС-7 ЕКС2	БР	    16
     * ЭП2К КАУД	БР	    17
     * ЭП20 КАУД	БР	    18
     * ЧС-7 КАУД-Л	БР	    19
     * KZ4AT	БР	        20
     * ТЭП33А	БР	        21
     * @return индекс серии локомотива
     */
    public int getTypeLoc() {
        return  toUnsignedInt(values[28]) + 100;
    }

}
