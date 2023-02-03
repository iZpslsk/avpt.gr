package avpt.gr.dialogs.packing;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static avpt.gr.common.UtilsArmG.humanReadableByteCountBin;

public class PackTableModel  extends AbstractTableModel {

    private static final int COL_COUNT = 5;
    private java.util.List<RowData> rows;
    private Map<File, RowData> mapLookup;

    public static final int ORD_NUM = 0;
    public static final int ORD_PROGRESS = 1;
    public static final int ORD_CHECKBOX = 2;
    public static final int ORD_LENGTH = 3;
    public static final int ORD_FILE = 4;

    public PackTableModel() {
        rows = new ArrayList<RowData>(25);
        mapLookup = new HashMap<File, RowData>(25);
    }

    public static class RowData {

        private int num;
        private float progress;
        private boolean check;
        private long length;
        private File file;

        public RowData(File file) {
            this.file = file;
            this.length = file.length();
            this.progress = 0f;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public File getFile() {
            return file;
        }

        public long getLength() {
            return length;
        }

        public float getProgress() {
            return progress;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }
    }

    public static class CheckCellRender extends JCheckBox implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setSelected(isSelected);
            return this;
        }
    }

    public static class ProgressCellRender extends JProgressBar implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            int progress = 0;
            if (value instanceof Float) {
                progress = Math.round(((Float) value) * 100f);
            } else if (value instanceof Integer) {
                progress = (Integer) value;
            }
            setValue(progress);
            return this;
        }
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        String name = "???";
        switch (column) {
            case ORD_NUM:
                name = "№";
                break;
            case ORD_PROGRESS:
                name = "прогресс";
                break;
            case ORD_CHECKBOX:
                name = "";
                break;
            case ORD_LENGTH:
                name = "размер";
                break;
            case ORD_FILE:
                name = "файл";
                break;
        }
        return name;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RowData rowData = rows.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case ORD_NUM:
                value = rowData.getNum();
                break;
            case ORD_PROGRESS:
                value = rowData.getProgress();
                break;
            case ORD_LENGTH:
                value = humanReadableByteCountBin(rowData.getLength()) + " ";
                break;
            case ORD_FILE:
                value = " " + rowData.getFile();
                break;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        RowData rowData = rows.get(row);
        switch (col) {
            case ORD_FILE:
                if (aValue instanceof Float) {
                    rowData.setProgress((Float) aValue);
                }
                break;
            case ORD_CHECKBOX:
                if (aValue instanceof  Boolean) {
                    rowData.setCheck((Boolean)aValue);
                }
                break;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case ORD_NUM:
                return Integer.class;
            case ORD_PROGRESS:
                return Object.class;
            case ORD_CHECKBOX:
                return  Boolean.class;
            case ORD_LENGTH:
                return Integer.class;
            case ORD_FILE:
                return String.class;
            default:
                return Object.class;
        }
    }

    public void addFile(File file) {
        RowData rowData = new RowData(file);
        rowData.setNum(rows.size());
        rowData.setCheck(true);
        mapLookup.put(file, rowData);
        rows.add(rowData);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    public void clear() {
        if (rows == null || rows.size() == 0) return;
        fireTableRowsDeleted(0, rows.size() - 1);
        mapLookup.clear();
        rows.clear();
    }

    protected void updateProgress(File file, int progress) {
        RowData rowData = mapLookup.get(file);
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, ORD_FILE);
            fireTableCellUpdated(row, ORD_FILE);
        }
    }

    /**
     * установка ширины колонки
     * @param columnModel TableColumnModel
     * @param indexCol - индекс колонки
     * @param percent - процент для min max
     * @param width - предпочитаемая ширина
     */
    private static void setColWidth(TableColumnModel columnModel, int indexCol, int percent, int width) {
        columnModel.getColumn(indexCol).setMinWidth(width - width * percent / 100);
        columnModel.getColumn(indexCol).setMaxWidth(width + width * percent / 100);
        columnModel.getColumn(indexCol).setPreferredWidth(width);
    }

    /**
     * установка ширины колонок
     * @param table -
     */
    static void setColumnsWidth(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        setColWidth(columnModel, PackTableModel.ORD_NUM, 80, 70);
        setColWidth(columnModel, PackTableModel.ORD_PROGRESS, 80, 300);
        setColWidth(columnModel, PackTableModel.ORD_CHECKBOX, 80, 20);
        setColWidth(columnModel, PackTableModel.ORD_LENGTH, 80, 100);
    }
}
