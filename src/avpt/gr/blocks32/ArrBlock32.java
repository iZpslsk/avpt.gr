package avpt.gr.blocks32;

import avpt.gr.blocks32.asim.Block32_C0_0;
import avpt.gr.blocks32.overall.Block32_16_56;
import avpt.gr.blocks32.overall.Block32_21_1;
import avpt.gr.blocks32.passenger.Block32_26_66_96;
import avpt.gr.blocks32.passenger.Block32_76;
import avpt.gr.train.Train;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static avpt.gr.common.UtilsArmG.iteratorBinarySearch;
import static avpt.gr.train.ArrTrains.*;

public class ArrBlock32 {

    private final ArrayList<Block32> arrayList = new ArrayList<Block32>();          // массив элементов Block32
    private MappedByteBuffer buf = null;            		//
    private int len;                                        // длина channel
    private final boolean isShift;								// сдвижка
    private final String fileName;    // имя файла

    // присутствует ли данный тип движения в файле поездки
    private boolean isASIM;
    private boolean isP;
    private boolean isPT;
    private boolean isG;
    private boolean isNotAsim;
    private Set<Integer> setIdBlk = new HashSet<Integer>();

    /**
     * @param name файл поездки
     * @param isShift сдвижка (исключить посылки с неверной кс)
     * @throws IOException -исключение загрузки файла
     */
    public ArrBlock32(String name, boolean isShift) throws IOException {
        this.fileName = name;
        this.isShift = isShift;
        makeBuffer(name);
        if (!check())
            throw new IOException(name +
                    " (Формат файла не определен)");
        fillArrayList();
       // WeightBlocks.setDefaultAsim(isASIM);
        if (arrayList.size() == 0)
            throw new IOException(
                    String.format("%s (Неизвестный формат файла поездки!)", name));
    }

    private void makeBuffer(String name) throws IOException {
        RandomAccessFile file = new RandomAccessFile(name, "r");
        FileChannel channel = null;
    	try {
            channel = file.getChannel();
    		len = (int)channel.size();
//            if (len > 160 * 1000000)
//                throw new IOException(
//                        String.format("%s (файл поездки слишком велик!)", name));
    		buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, len);
    	}
    	finally {
    	    if (channel != null) channel.close();
    	}
    }

    /**
     * заполнение списка блоков
     */
    private void fillArrayList() {
        if (buf == null) return;
        buf.position(0);
        byte[] b = new byte[Block32.SIZE_BLOCK];
        long cnt_shift = 0;
        int cnt_asim = 0;
        int cnt_p = 0;
        int cnt_pt = 0;
        int cnt_g = 0;
        int cnt_not_asim = 0;
        int cnt_everything = 0;
        while (buf.hasRemaining()) {
            if ((buf.position() + Block32.SIZE_BLOCK) > len)
                buf.position(len);
            else
                buf.get(b);
            
            Block32 block32 = new Block32(b);
            if (!isShift || block32.crcTruth()) {
                cnt_shift = 0;
                if ((block32.getId() >= 0x10 && block32.getId() <= 0x1F) || // g
                    (block32.getId() == 0x21) ||
 //                   (block32.getId() >= 0x20 && block32.getId() <= 0x2F) || // p
//                    (block32.getId() >= 0x40 && block32.getId() <= 0x4F) || // p
                    (block32.getId() >= 0x50 && block32.getId() <= 0x5F) || // g
                    (block32.getId() == 0x61) ||
//                    (block32.getId() >= 0x60 && block32.getId() <= 0x6F) || // p
//                    (block32.getId() >= 0x70 && block32.getId() <= 0x7F) || // pt
//                    (block32.getId() >= 0x90 && block32.getId() <= 0x9F) || // p
//                     block32.getId() == 0xAA ||                             // p
//                    (block32.getId() >= 0xF0 && block32.getId() <= 0xFF) || //
                    (block32.getId() >= 0xC0 && block32.getId() <= 0xC7))   // asim
                    arrayList.add(block32);

                if (block32.getId() != 0x21) cnt_everything++; // игнориоуем 0x21 посылку
//                if (((block32.getId() == 0x20) || (block32.getId() >= 0x22 && block32.getId() <= 0x2F)) ||
//                    (block32.getId() >= 0x40 && block32.getId() <= 0x4F) ||
//                    (block32.getId() >= 0x60 && block32.getId() <= 0x6F) ||
//                    (block32.getId() == 0xAA) ||
//                    (block32.getId() >= 0x90 && block32.getId() <= 0x9F)) cnt_p++;  // p
                if ((block32.getId() >= 0x10 && block32.getId() <= 0x1F) ||
                    (block32.getId() >= 0x50 && block32.getId() <= 0x5F)) cnt_g++;   // g
//                if (block32.getId() >= 0x70 && block32.getId() <= 0x7F) cnt_pt++;   // pt
                if (block32.getId() >= 0xC0 && block32.getId() <= 0xC7) cnt_asim++; // asim
                else cnt_not_asim++;
                setIdBlk.add(block32.getId());
            }
            else {
                if (++cnt_shift < 3200000)
                    buf.position(buf.position() - Block32.SIZE_BLOCK + 1); // сдвижка
            }
        }
        double percent_asim = (double) cnt_asim / (cnt_everything) * 100.0;
        double percent_g = (double) cnt_g / (cnt_everything) * 100.0;
        double percent_p = (double) cnt_p / (cnt_everything) * 100.0;
        double percent_pt = (double) cnt_pt / (cnt_everything) * 100.0;
        isASIM = percent_asim > 0;
        isP = percent_p > 0;
        isPT = percent_pt > 0;
        isG = percent_g > 0;
        isNotAsim = percent_asim < 100;

        buf.clear();
        buf = null;

//        Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buf).cleaner();
//        if (cleaner != null) {
//            cleaner.clean();
//        }
    }

    /**
     * @param nBl индекс посылки
     * @return  32-байтовая посылка
     */
    public Block32 get(int nBl) {
    	if (nBl >= 0 && nBl < arrayList.size())
    		return arrayList.get(nBl);
    	else
    		return null;
    }

    /**
     * поиск индекса по блоку Block32
     * @param block32 -
     * @return индекс массива arrBlock
     */
    public int getIndexByBlock32(Block32 block32) {
        return arrayList.indexOf(block32);
    }
    
    /**
     * @return всего посылок
     */
    public int size() {
        return arrayList.size();
    }

    /**
     * @param nBl32 - номер опорного блока
     * @param idType - код идентификатора (0x51, 0x52...)
     * @param subType - дополнительный идентификатор посылки, для 0x5_ равен 0
     * @param maxCnt - максимальное значени счетчика, если = 0 , то счетчик отключен
     * @return ближайший слева номер блока
     */
    public int getNearToLeftBl(int nBl32, int idType, int subType, int maxCnt) {
    	int result = -1;
    	int cnt = 0;
    	if (nBl32 <= 0 || nBl32 >= arrayList.size()) return result;

        int curSubType = 0;
//        if (!isASIM) {
        do {
            --nBl32;
            if (isTrue(idType))
                curSubType = Block32.getSubId(arrayList.get(nBl32).getId(), arrayList.get(nBl32).getValues());
            if (maxCnt > 0) cnt++;
        }
        while (nBl32 > 0 && (arrayList.get(nBl32).getId() != idType || curSubType != subType) && cnt <= maxCnt);
//        }
//        else {
//            do {
//                --nBl32;
//                curSubType = Block32.getSubId_ASIM(arrayList.get(nBl32).getValues());
//                if (maxCnt > 0) cnt++;
//            }
//            while (nBl32 > 0 && (arrayList.get(nBl32).getId() != idType || curSubType != subType) && cnt <= maxCnt);
//        }

    	if (arrayList.get(nBl32).getId() == idType && curSubType == subType)
    		result = nBl32;

    	return result;
    }

    /**
     * @param nBl32 - номер опорного блока
     * @param idType - код идентификатора (0x51, 0x52...)
     * @param subType - дополнительный идентификатор посылки, для 0x5_ равен 0
     * @param maxCnt - максимальное значени счетчика если = 0 , то счетчик отключен
     * @return ближайший справа номер блока
     */
    public int getNearToRightBl(int nBl32, int idType, int subType, int maxCnt) {
        int result = -1;
        int cnt = 0;
        if (nBl32 < 0 || nBl32 >= arrayList.size() - 1) return result;

        int curSubType = 0;
//        if (!isASIM) {
        do {
            ++nBl32;
            if (isTrue(idType))
                curSubType = Block32.getSubId(arrayList.get(nBl32).getId(), arrayList.get(nBl32).getValues());
            if (maxCnt > 0) cnt++;
        }
        while (nBl32 < arrayList.size() - 1 && (arrayList.get(nBl32).getId() != idType || curSubType != subType) && cnt <= maxCnt);
///        }
//        else {
//            do {
//                ++nBl32;
//                curSubType = Block32.getSubId_ASIM(arrayList.get(nBl32).getValues());
//                if (maxCnt > 0) cnt++;
//            }
//            while (nBl32 < arrayList.size() - 1 && (arrayList.get(nBl32).getId() != idType || curSubType != subType) && cnt <= maxCnt);
//        }

        if (arrayList.size() > nBl32 && arrayList.get(nBl32).getId() == idType && curSubType == subType)
            result = nBl32;

        return result;
    }

    private boolean isTrue(int idType) {
        if (isG) {
            return (idType == 0x21 || idType == 0x1D);
        }
        if (isASIM) {
            return idType == 0xC0 || idType == 0xC2 || idType == 0xC3 || idType == 0xC5;
        }
        if (isP) {
            return idType == 0x1D || idType == 0x2D || idType == 0x5D || idType == 0x6D || idType == 0x9D;
        }
        return false;
    }

    /**
     * поиск индекса arrBlock32 по текущей секунде
     * @param second значение текущей секунды
     * @param low нижняя граница массива
     * @param high верхняя граница массива
     * @return индекс массива arrBlock
     */
    public int searchIndexBySecond(int second, int low, int high) {
        byte[] b = new byte[Block32.SIZE_BLOCK];
        Block32 bl32 = new Block32(b);
        bl32.setSecond(second);
        return iteratorBinarySearch(arrayList, bl32, low, high,
    	        new Comparator() {
    				@Override
    				public int compare(Object arg0, Object arg1) {
    					return ((Block32)arg0).getSecond().compareTo(((Block32)arg1).getSecond());
    				}
            	});
    }

    /**
     * поиск индекса arrBlock32 по текущей координате
     * @param coordinate значение текущей координаты
     * @param low нижняя граница массива
     * @param high верхняя граница массива
     * @return индекс массива arrBlock
     */
    public int searchIndexByCoordinate(int coordinate, int low, int high) {
        byte[] b = new byte[Block32.SIZE_BLOCK];
        Block32 bl32 = new Block32(b);
        bl32.setCoordinate(coordinate);
        return iteratorBinarySearch(arrayList, bl32, low, high,
                new Comparator() {
                    @Override
                    public int compare(Object arg0, Object arg1) {
                        return ((Block32)arg0).getCoordinate().compareTo(((Block32)arg1).getCoordinate());
                    }
                });
    }

    /**
     * проверка файла поездки на соответствие формату (если удачных посылок больше N - удачно)
     * @return - boolean
     */
    private boolean check() {
        buf.position(0);
        final int N = 20;
        int cnt = 0;
        byte[] b = new byte[Block32.SIZE_BLOCK];

        while (buf.hasRemaining()) {
            if ((buf.position() + Block32.SIZE_BLOCK) > len)
                buf.position(len);
            else
                buf.get(b);            
            
            Block32 block32 = new Block32(b);           
            if (block32.crcTruth()) {
                cnt++;
            }
        }
        return cnt > N;
    }

    private int getFirstInit(int nBl) {
        for (int i = nBl; i < size(); i++) {
            if (get(nBl).getId() == 0x56 || get(nBl).getId() == 0x16) return nBl;
            if (get(nBl).getId() == 0x21) {
                Block32_21_1 bl = new Block32_21_1(get(nBl).getValues());
                if (bl.getSubId() == 0x01) return nBl;
            }
            if (get(nBl).getId() == 0xC0) {
                Block32_C0_0 bl = new Block32_C0_0(get(nBl).getValues());
                if (bl.getSubId() == 0x00) return nBl;
            }
        }
        return 0;
    }

    public int getFirstTypeMove(int nBl) {
        for (int n = nBl; n < size(); n++) {
            int id = get(n).getId();
            if ((id >= 0x10 && id <= 0x1F) || (id >= 0x50 && id <= 0x5F)) return CODE_MOVE_G;
            else if ((id >= 0x22 && id <= 0x2F) || (id >= 0x60 && id <= 0x6F) || (id >= 0x90 && id <= 0x9F)) return CODE_MOVE_P;
            else if (id >= 0xF0 && id <= 0xFF) return CODE_MOVE_PT;
            else if (id >= 0xC0 && id <= 0xC7) return CODE_MOVE_ASIM;
        }
        return -1;
    }

    public Train getFirstTrain(int nBl) {
        Train train = null;
        for (int n = nBl; n < size(); n++) {
            if (get(n).getId() == 0x16 || get(n).getId() == 0x56) {
                if (train == null) train = new Train(n);
                Block32_16_56 block32_16_56 = new Block32_16_56(get(n).getValues());
                train.setNumLoc(block32_16_56.getTypeLoc());
            }
            if (get(n).getId() == 0x21) {
                int curSubId_21 = Block32.getSubId(get(n).getId(), get(n).getValues());
                if (curSubId_21 == 0x01) {
                    if (train == null) train = new Train(n);
                    Block32_21_1 block32_21_1 = new Block32_21_1(get(n).getValues());
                    train.setNumTrain(block32_21_1.getNumTrain());
//                    return block32_21_1.getTypeLoc();
                }
            }
        }
        return train;
    }

    public int getFirstTypeTrain(int nBl) {
        for (int n = nBl; n < size(); n++) {
//            if (get(n).getId() == 0x56 || get(n).getId() == 0x16) {
//                Block32_16_56 block32_16_56 = new Block32_16_56(get(n).getValues());
//                return block32_16_56.getTypeLoc();
//            }
            if (isG && get(n).getId() == 0x21) {
                int curSubId_21 = Block32.getSubId(get(n).getId(), get(n).getValues());
                if (curSubId_21 == 0x01) {
                    Block32_21_1 block32_21_1 = new Block32_21_1(get(n).getValues());
                    return block32_21_1.getTypeLoc();
                }
            }
            if (isASIM && get(n).getId() == 0xC0) {
                int curSubId_C0 = Block32.getSubId(get(n).getId(), get(n).getValues());
                if (curSubId_C0 == 0x00) {
                    Block32_C0_0 block32_c0_0 = new Block32_C0_0(get(n).getValues());
                    return block32_c0_0.getTypeLoc();
                }
            }
            if (isP && get(n).getId() == 0x26 || get(n).getId() == 0x66 || get(n).getId() == 0x96) {
                    Block32_26_66_96 block32_26_66_96 = new Block32_26_66_96(get(n).getValues());
                    return  block32_26_66_96.getTypeLoc();
                }
            if (isP && get(n).getId() == 0x76) {
                Block32_76 block32_76 = new Block32_76(get(n).getValues());
                return  block32_76.getTypeLoc();
            }
        }
//            if (get(n).getId() == 0x16 || get(n).getId() == 0x56) {
//                Block32_16_56 block32_16_56 = new Block32_16_56(get(n).getValues());
//                return block32_16_56.getTypeLoc();
//            }
//        }
        return -1;
    }

    /**
     *  nBl - начальный блок
     * @return номер ближайшей посылки инициализации АСИМ (0xС0_0)
     */
    private int getFirstInitASIM(int nBl) {
        if (get(nBl).getId() == 0xC0) {
            Block32_C0_0 bl = new Block32_C0_0(get(nBl).getValues());
            if (bl.getSubId() == 0x00) return nBl;
        }
        int subId = 0;
        do {
            nBl++;
            if (get(nBl).getId() == 0xC0) {
                Block32_C0_0 bl = new Block32_C0_0(get(nBl).getValues());
                subId = bl.getSubId();
            }
        }
        while ((get(nBl).getId() != 0xC0 || subId != 0x00) && nBl < size() - 1);
        if (nBl >= size() - 1) nBl = -1; // если небыло инициализационных посылок
        return nBl;
    }

    /**
     *  nBl - начальный блок
     * @return номер ближайшей посылки инициализации УСАВП (0x21_1)
     */
    private int getFirstInitUSAVP(int nBl) {
        if (get(nBl).getId() == 0x21) {
            Block32_21_1 bl = new Block32_21_1(get(nBl).getValues());
            if (bl.getSubId() == 0x01) return nBl;
        }
        int subId = 0;
        do {
            nBl++;
            if (get(nBl).getId() == 0x21) {
                Block32_21_1 bl = new Block32_21_1(get(nBl).getValues());
                subId = bl.getSubId();
            }
        }
        while ((get(nBl).getId() != 0x21 || subId != 0x01) && nBl < size() - 1);
        if (nBl >= size() - 1) nBl = -1; // если небыло инициализационных посылок
        return nBl;
    }

    /**
     *  nBl - начальный блок
     * @return номер ближайшей посылки инициализации БР (0x56)
     */
    private int getFirstInitBR(int nBl) {
        if (get(nBl).getId() == 0x56 || get(nBl).getId() == 0x16) return nBl;
        do {
            nBl++;
        }
        while ((get(nBl).getId() != 0x56 || get(nBl).getId() != 0x16) && nBl < size() - 1);
        if (nBl >= size() - 1) nBl = -1; // если небыло инициализационных посылок
        return nBl;
    }

    /**
     * @param nBl - начальный блок
     * @return - тип локомотива из первого блока инициализации БР
     */
    private int getFirstTypeBr(int nBl) {
        int n = getFirstInitBR(nBl);
        if (n == -1) return -1;
        Block32_16_56 block32_16_56 = new Block32_16_56(get(n).getValues());
        return block32_16_56.getTypeLoc();
    }

    /**
     * @param nBl - начальный блок
     * @return - тип локомотива из первого блока инициализации АСИМ
     */
    private int getFirstTypeASIM(int nBl) {
        int n = getFirstInitASIM(nBl);
        if (n == -1) return -1;
        Block32_C0_0 block32_C0_0 = new Block32_C0_0((get(n).getValues()));
        return block32_C0_0.getTypeLoc();
    }

    /**
     * @param nBl - начальный блок
     * @return - тип локомотива из первого блока инициализации УСАВП
     */
    private int getFirstTypeUSAVP(int nBl) {
        int n = getFirstInitUSAVP(nBl);
        if (n == -1) return -1;
        Block32_21_1 block32_21_1 = new Block32_21_1(get(n).getValues());
        return block32_21_1.getTypeLoc();
    }

    public int getFirstType(int nBl) {
        int type = getFirstTypeUSAVP(nBl);
        if (type == -1)
            type = getFirstTypeBr(nBl);
        if (type == -1)
            type = getFirstTypeASIM(nBl);
        return type;
    }

    /**
     * @return полное имя файла
     */
    public String getFileName() {
        return fileName;
    }

    public boolean isASIM() {
        return isASIM;
    }

    public boolean isNotAsim() {
        return isNotAsim;
    }

    public boolean isP() {
        return isP;
    }

    public boolean isPT() {
        return isPT;
    }

    public boolean isG() {
        return isG;
    }

    public boolean isExistsIdBlk(int idBlk) {
        return setIdBlk.contains(idBlk);
    }
}
