package avpt.gr.blocks32;

import avpt.gr.blocks32.asim.Block32_C0_0;
import avpt.gr.blocks32.overall.Block32_16_56;
import avpt.gr.blocks32.overall.Block32_21_1;
import avpt.gr.train.Train;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static avpt.gr.blocks32.SubIdGr.getSubId;
import static avpt.gr.common.UtilsArmG.iteratorBinarySearch;
import static avpt.gr.train.ArrTrains.*;

public class ArrBlock32 {

    private final ArrayList<Block32_gp> arrayList = new ArrayList<Block32_gp>();          // массив элементов Block32
    private MappedByteBuffer buf = null;            		//
    private int len;                                        // длина channel
    private final boolean isShift;								// сдвижка
    private final String fileName;    // имя файла

    // присутствует ли данный тип движения в файле поездки
    private boolean isASIM;
    private boolean isG;
    private final Set<Integer> setIdBlk = new HashSet<Integer>();

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
        byte[] b = new byte[Block32_gp.SIZE_BLOCK];
        long cnt_shift = 0;
        int cnt_asim = 0;
        int cnt_p = 0;
        int cnt_pt = 0;
        int cnt_g = 0;
        int cnt_everything = 0;
        while (buf.hasRemaining()) {
            if ((buf.position() + Block32_gp.SIZE_BLOCK) > len)
                buf.position(len);
            else
                buf.get(b);
            
            Block32_gp block32_gp = new Block32_gp(b);
            if (!isShift || block32_gp.crcTruth()) {
                cnt_shift = 0;
                if ((block32_gp.getId() >= 0x10 && block32_gp.getId() <= 0x1F) || // g
                    (block32_gp.getId() == 0x21) ||
                    (block32_gp.getId() >= 0x50 && block32_gp.getId() <= 0x5F) || // g
                    (block32_gp.getId() == 0x61) ||
                    (block32_gp.getId() >= 0xC0 && block32_gp.getId() <= 0xC7)) {  // asim
                    addIdToSetId(block32_gp);
                    arrayList.add(block32_gp);
                }

                if (block32_gp.getId() != 0x21) cnt_everything++; // игнориоуем 0x21 посылку
                if ((block32_gp.getId() >= 0x10 && block32_gp.getId() <= 0x1F) ||
                    (block32_gp.getId() >= 0x50 && block32_gp.getId() <= 0x5F)) cnt_g++;   // g
                if (block32_gp.getId() >= 0xC0 && block32_gp.getId() <= 0xC7) cnt_asim++; // asim
//                else cnt_not_asim++;
            }
            else {
                if (++cnt_shift < 3200000)
                    buf.position(buf.position() - Block32_gp.SIZE_BLOCK + 1); // сдвижка
            }
        }
        double percent_asim = (double) cnt_asim / (cnt_everything) * 100.0;
        double percent_g = (double) cnt_g / (cnt_everything) * 100.0;
        isASIM = percent_asim > 0;
        isG = percent_g > 0;

        buf.clear();
        buf = null;
    }

    /**
     * @param nBl индекс посылки
     * @return  32-байтовая посылка
     */
    public Block32_gp get(int nBl) {
    	if (nBl >= 0 && nBl < arrayList.size())
    		return arrayList.get(nBl);
    	else
    		return null;
    }

    /**
     * @param nBl - номер блока
     * @return - секунда на номер блока
     */
    public int getSecond(int nBl) {
        if (nBl >= 0 && nBl < arrayList.size())
            return arrayList.get(nBl).getSecond();
        else
            return -1;
    }

    /**
     * @param nBl - номер блока
     * @return - метр на номер блока
     */
    public int getCoordinate(int nBl) {
        if (nBl >= 0 && nBl < arrayList.size())
            return arrayList.get(nBl).getCoordinate();
        else
            return -1;
    }

    /**
     * поиск индекса по блоку Block32
     * @param block32_gp -
     * @return индекс массива arrBlock
     */
    public int getIndexByBlock32(Block32_gp block32_gp) {
        return arrayList.indexOf(block32_gp);
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
                curSubType = getSubId(arrayList.get(nBl32).getId(), arrayList.get(nBl32).getValues());
            if (maxCnt > 0) cnt++;
        }
        while (nBl32 > 0 && (arrayList.get(nBl32).getId() != idType || curSubType != subType) && cnt <= maxCnt);

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
                curSubType = getSubId(arrayList.get(nBl32).getId(), arrayList.get(nBl32).getValues());
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
        byte[] b = new byte[Block32_gp.SIZE_BLOCK];
        Block32_gp bl32 = new Block32_gp(b);
        bl32.setSecond(second);
        return iteratorBinarySearch(arrayList, bl32, low, high,
    	        new Comparator() {
    				@Override
    				public int compare(Object arg0, Object arg1) {
    					return ((Block32_gp)arg0).getSecond().compareTo(((Block32_gp)arg1).getSecond());
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
        byte[] b = new byte[Block32_gp.SIZE_BLOCK];
        Block32_gp bl32 = new Block32_gp(b);
        bl32.setCoordinate(coordinate);
        return iteratorBinarySearch(arrayList, bl32, low, high,
                new Comparator() {
                    @Override
                    public int compare(Object arg0, Object arg1) {
                        return ((Block32_gp)arg0).getCoordinate().compareTo(((Block32_gp)arg1).getCoordinate());
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
        byte[] b = new byte[Block32_gp.SIZE_BLOCK];

        while (buf.hasRemaining()) {
            if ((buf.position() + Block32_gp.SIZE_BLOCK) > len)
                buf.position(len);
            else
                buf.get(b);            
            
            Block32_gp block32_gp = new Block32_gp(b);
            if (block32_gp.crcTruth()) {
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
                int curSubId_21 = getSubId(get(n).getId(), get(n).getValues());
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

    /**
     * добавляем id и subId блока в множество блоков, существующих в поездке
     * первые 8 бит - id
     * следующие 8 бит - subId
     * @param block32_gp - Block32
     */
    private void addIdToSetId(Block32_gp block32_gp) {
        int idBl = block32_gp.getId();
        int subIdBl = getSubId(idBl, block32_gp.getValues());
        int id = (subIdBl & 0xFF) << 8 | (idBl & 0xFF);   // 0..7 - id, 8..15 - subId
        setIdBlk.add(id);
    }

    public int getFirstTypeTrain(int nBl) {
        for (int n = nBl; n < size(); n++) {
//            if (get(n).getId() == 0x56 || get(n).getId() == 0x16) {
//                Block32_16_56 block32_16_56 = new Block32_16_56(get(n).getValues());
//                return block32_16_56.getTypeLoc();
//            }
            if (isG && get(n).getId() == 0x21) {
                int curSubId_21 = getSubId(get(n).getId(), get(n).getValues());
                if (curSubId_21 == 0x01) {
                    Block32_21_1 block32_21_1 = new Block32_21_1(get(n).getValues());
                    return block32_21_1.getTypeLoc();
                }
            }
            if (isASIM && get(n).getId() == 0xC0) {
                int curSubId_C0 = getSubId(get(n).getId(), get(n).getValues());
                if (curSubId_C0 == 0x00) {
                    Block32_C0_0 block32_c0_0 = new Block32_C0_0(get(n).getValues());
                    return block32_c0_0.getTypeLoc();
                }
            }
        }
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

    /**
     * @return полное имя файла
     */
    public String getFileName() {
        return fileName;
    }

    public boolean isG() {
        return isG;
    }

    /**
     * сущесвует ли блок с идентификатором idBl и subIdBl
     * @param idBl - идентификатор блока
     * @param subIdBl - доп идентификатор
     * @return - да/нет
     */
    public boolean isNotExistsIdBl(int idBl, int subIdBl) {
        return !setIdBlk.contains((idBl & 0xFF) | ((subIdBl & 0xFF) << 8));
    }
}
