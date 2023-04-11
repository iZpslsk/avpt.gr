package avpt.gr.components.hex_tab;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;

import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

interface CurIdListener {
    void setCurId(int id, int subId);
}

class TableModel extends AbstractTableModel implements ActionListener {

    // количество колонок
    private static final int NUM_COLUMNS = 33;
    // порядковые номера колонок
    private static final int COL_BL = 0;
    private static final int COL_TYPE = 1;
    private static final int COL_SECOND = 2;
    private static final int COL_COORDINATE = 3;

    private final ArrBlock32 blocks;
    private final int startBl;
    private final int endBl;

    private final CurIdListener curIdListener;

    TableModel(ArrBlock32 blocks, int startBl, int endBl, CurIdListener curIdListener) {
        this.blocks = blocks;
        this.startBl = startBl;
        this.endBl = endBl;
        this.curIdListener = curIdListener;
    }

    @Override
    public int getRowCount() {
        return endBl - startBl + 1;
    }

    @Override
    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    @Override
    public Object getValueAt(int i, int col) {
        final String TAG = "%02X";
        String subId = "";
        i += startBl;
        int curId = blocks.get(i).getId();
        int curSubId = 0;
        curSubId = Block32.getSubId(curId, blocks.get(i).getValues());
        if (curSubId > 0) subId = String.format("_%X", curSubId);
        curIdListener.setCurId(curId, curSubId); // передача id и subId в балбицу
        switch(col) {
            case COL_BL: return String.format("%,08d", i);                     // ord
            case COL_TYPE: return String.format("0x" + TAG, curId) + subId;    // id
            case COL_SECOND: return String.valueOf(blocks.getSecond(i));
            case COL_COORDINATE: return String.valueOf(blocks.getCoordinate(i));
            case 4: return String.format(TAG, blocks.get(i).get(0));
            case 5: return String.format(TAG, blocks.get(i).get(1));
            case 6: return String.format(TAG, blocks.get(i).get(2));
            case 7: return String.format(TAG, blocks.get(i).get(3));
            case 8: return String.format(TAG, blocks.get(i).get(4));
            case 9: return String.format(TAG, blocks.get(i).get(5));
            case 10: return String.format(TAG, blocks.get(i).get(6));
            case 11: return String.format(TAG, blocks.get(i).get(7));
            case 12: return String.format(TAG, blocks.get(i).get(8));
            case 13: return String.format(TAG, blocks.get(i).get(9));
            case 14: return String.format(TAG, blocks.get(i).get(10));
            case 15: return String.format(TAG, blocks.get(i).get(11));
            case 16: return String.format(TAG, blocks.get(i).get(12));
            case 17: return String.format(TAG, blocks.get(i).get(13));
            case 18: return String.format(TAG, blocks.get(i).get(14));
            case 19: return String.format(TAG, blocks.get(i).get(15));
            case 20: return String.format(TAG, blocks.get(i).get(16));
            case 21: return String.format(TAG, blocks.get(i).get(17));
            case 22: return String.format(TAG, blocks.get(i).get(18));
            case 23: return String.format(TAG, blocks.get(i).get(19));
            case 24: return String.format(TAG, blocks.get(i).get(20));
            case 25: return String.format(TAG, blocks.get(i).get(21));
            case 26: return String.format(TAG, blocks.get(i).get(22));
            case 27: return String.format(TAG, blocks.get(i).get(23));
            case 28: return String.format(TAG, blocks.get(i).get(24));
            case 29: return String.format(TAG, blocks.get(i).get(25));
            case 30: return String.format(TAG, blocks.get(i).get(26));
            case 31: return String.format(TAG, blocks.get(i).get(27));
            case 32: return String.format(TAG, blocks.get(i).get(28));
            default: return -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
