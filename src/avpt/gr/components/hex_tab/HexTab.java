package avpt.gr.components.hex_tab;

import avpt.gr.blocks32.ArrBlock32;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * таблица содержимого блокоа в hex формате
 * используем шаблон Builder, т.к. имеем дело с большим количеством параметров конструктора
 *
 */
public class HexTab extends JPanel {

    public static class Builder {
        private ArrBlock32 blocks = null;

        private int startBl;
        private int endBl;
        private int widthNumBl;
        private int widthIdBl;
        private int widthSecond;
        private int widthCoordinate;
        private int widthCell;
        private Color colorBack;
        private Color colorSelect;
        private Font font;

        /**
         * @param blocks - массив блоков
         */
        public Builder(ArrBlock32 blocks) {
            this.blocks = blocks;

            startBl = 0;
            endBl = blocks.size() - 1;
            widthNumBl = 70;
            widthIdBl = 55;
            widthSecond= 70;
            widthCoordinate = 70;
            widthCell = 30;
            colorBack = new Color(0x121212);;
            colorSelect = new Color(0x555457);
            font = new Font(Font.MONOSPACED, Font.PLAIN, 12); ;
        }

        /**
         * setter для startBl - номер первой посылки
         * @return сам себя - Builder
         */
        public Builder startBl(int val) {
            this.startBl = val;
            return this;
        }
        /**
         * setter для endBl - номер последней посылки
         * @return сам себя - Builder
         */
        public Builder endBl(int val) {
            this.endBl = val;
            return this;
        }
        /**
         * setter для widthNumBl - ширина столбца с номером блока
         * @return сам себя - Builder
         */
        public Builder widthNumBl(int val) {
            this.widthNumBl = val;
            return this;
        }
        /**
         * setter для widthIdBl - ширина столбца с Id блока
         * @return сам себя - Builder
         */
        public Builder widthIdBl(int val) {
            this.widthIdBl =  val;
            return this;
        }
        /**
         * setter для widthSecond - ширана столбца секунд
         * @return сам себя - Builder
         */
        public Builder widthSecond(int val) {
            this.widthSecond =  val;
            return this;
        }
        /**
         * setter для widthCoordinate - ширана столбца метров
         * @return сам себя - Builder
         */
        public Builder widthCoordinate(int val) {
            this.widthCoordinate =  val;
            return this;
        }
        /**
         * setter для widthCell - ширина столбца байтового значения
         * @return сам себя - Builder
         */
        public Builder widthCell(int val) {
            this.widthCell =  val;
            return this;
        }
        /**
         * setter для colorBack - цвет фона таблицы
         * @return сам себя - Builder
         */
        public Builder colorBack(Color val) {
            this.colorBack =  val;
            return this;
        }
        /**
         * setter для colorSelect - цвет выделения строки таблицы
         * @return сам себя - Builder
         */
        public Builder colorSelect(Color val) {
            this.colorSelect =  val;
            return this;
        }
        /**
         * setter для font - шрифт текста таблицы
         * @return сам себя - Builder
         */
        public Builder font(Font  val) {
            this.font =  val;
            return this;
        }
        /**
         * возвращаем HexTab
         * @return - HexTab
         */
        public HexTab build() {
            return new HexTab(this);
        }
    }

    private final ArrBlock32 blocks;
    private final JTable table;
    private final int startBl;
    // id - сопоставление цвета и id (subId) блока
    private int curIdColor = -1;
    private int curSubIdColor = -1;
    // номер выделенной строки таблицы
    private int curRow = -1;
    // номер блока выделенной строки таблицы
    private int curBl = 0;
    // секунда выделенной строки таблицы
    private int curSecond = -1;
    // номер id (subId) блока выделенной строки таблицы
    private int curIdBl = -1;
    private int curSubIdBl = -1;
    // номер id (subId) блока выделенного пункта меню поиска
    private int curIdBlItem = -1;
    private int curSubIdBlItem = -1;

    // порядковые номера колонок
    private static final int COL_NUM_BL = 0;
    private static final int COL_ID_BL = 1;
    private static final int COL_SECOND = 2;
    private static final int COL_COORDINATE = 3;

    // событие смены выделенной строки таблицы
    private ListSelectionListener listSelectionListener;

    private HexTab(Builder builder) {
        super(new BorderLayout());
        blocks = builder.blocks;
        startBl = builder.startBl;
        int endBl = builder.endBl;

        TableModel tableModel = new TableModel(blocks, startBl, endBl, new CurIdListener() {
            // событие - передача текущего id и subId блока из модели таблицы (для сопоставления цвета)
            @Override
            public void setCurId(int id, int subId) {
                curIdColor = id;
                curSubIdColor = subId;
            }
        });
        table = new JTable(tableModel);
        table.setBackground(builder.colorBack);
        table.setSelectionBackground(builder.colorSelect);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(builder.font);
        table.setFocusable(false);
        if (table.getRowCount() > 0) selectRow(0);
        // установка ширины и название шапки колонок
        setPropColumn(table.getColumnModel().getColumn(COL_NUM_BL), builder.widthNumBl, "№ блока");
        setPropColumn(table.getColumnModel().getColumn(COL_ID_BL), builder.widthIdBl, "Id блока");
        setPropColumn(table.getColumnModel().getColumn(COL_SECOND), builder.widthSecond, "Секунд");
        setPropColumn(table.getColumnModel().getColumn(COL_COORDINATE), builder.widthCoordinate, "Метров");
        for (int i = 4; i <= 32; i++)
            setPropColumn(table.getColumnModel().getColumn(i), builder.widthCell, Integer.toString(i - 3));
        // цвет шрифта для строки (блока)
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    if (column == 0) {
                        setForeground(Color.WHITE);
                        table.setSelectionForeground(Color.WHITE);
                    }
                    else {
                        setForeground(getColorById(curIdColor, curSubIdColor));
                        table.setSelectionForeground(getColorById(curIdColor, curSubIdColor));
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected,
                            hasFocus, row, column);
                }
            });
        }

        // событие смены выделенной строки таблицы
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        try {
                            doChange();
                        } catch (ParseException event) {
                            event.printStackTrace();
                        }
                        // обработка ListSelectionListener для HexTab
                        if (listSelectionListener != null) {
                            listSelectionListener.valueChanged(
                                    new ListSelectionEvent(
                                            HexTab.this, e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting())
                            );
                        }
                    }
                }
        );
        makeSearchMenu();
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * создание и добавление меню поиска
     */
    private void makeSearchMenu() {
        SearchMenu searchMenu = new SearchMenu(blocks);
        searchMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchMenu.Item item = (SearchMenu.Item)e.getSource();
                curIdBlItem = item.getId(); // id блока выделенного пункта меню
                curSubIdBlItem = item.getSubId(); // subId блока выделенного пункта меню
                int row = getRowByIdBl(curBl, curIdBlItem, curSubIdBlItem, true);
                if (row >= 0) selectRow(row);
            }
        });
        table.setComponentPopupMenu(searchMenu);
    }

    /**
     * добавить обработчик смены строки в таблице
     * @param listSelectionListener -
     */
    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listSelectionListener = listSelectionListener;
    }

    /**
     * установка курсора на строку
     * @param row - строка
     */
    public void selectRow(int row) {
        if (row >= 0 && row < table.getRowCount())
            table.changeSelection(row, 0, false, false);
    }

    /**
     * установка курсора на секунду
     * @param second - секунда
     */
//    public void selectSecond(int second) {
//        int row = blocks.getIdBySecond(second);// getMapSecond().get(second);
//        selectRow(row);
//    }

    /**
     * [F3] - поиск
     * выделение ближайшей строки таблицы с текущим id блока
     * @param isUp - направление поиска
     */
    public void selectSearchedIdBl(boolean isUp) {
        try {
            doChange();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int row = getRowByIdBl(curBl, curIdBlItem, curSubIdBlItem, isUp);
        if (row >= 0) selectRow(row);
    }

    /**
     * @return - количество строк в таблице
     */
    public int getRowCount() {
        return table.getRowCount();
    }

    /**
     * @return номер выделенной строки таблицы, (-1 не инициализирована)
     */
    public int getCurRow() {
        return curRow;
    }

    /**
     * @return текущая секунда
     */
    public int getCurSecond() {
        return curSecond;
    }

    /**
     * @return - текущий id блока
     */
    public int getCurIdBl() {
        return curIdBl;
    }

    /**
     * @return - текущий subId блока
     */
    public int getCurSubIdBl() {
        return curSubIdBl;
    }


    /**
     * установка ширины и названия шапки колонки
     * @param column - колонка таблицы
     * @param width	- ширина колонки
     * @param header - заголовок
     */
    private static void setPropColumn(TableColumn column, int width, String header) {
        column.setMinWidth(width / 2);
        column .setMaxWidth(width * 2);
        column.setPreferredWidth(width);
        column.setHeaderValue(header);
    }

    /**
     * цвет строки в зависимости от id блока
     * @param id - id блока
     * @return цвет строки
     */
    private static Color getColorById(int id, int subId) {
        switch(id) {
            case 0x0E: return Color.WHITE;
            case 0x21:
            case 0x61:
            case 0x91:
            case 0x79:
            case 0x40:
                // грузовой
                switch (subId) {
                    case 0x01: return new Color(0x17FF9D);
                    case 0x02: return new Color(0xFF8085);
                    case 0x03: return new Color(0xE109FF);
                    case 0x04: return new Color(0xFFE469);
                    case 0x06: return new Color(0x945AFF);
                    case 0x07: return new Color(0xFF2727);
                    case 0x08: return new Color(0xAEAEFA);
                    case 0x09: return new Color(0xDEFF00);
                    case 0x0A: return new Color(0xFC5757);
                    case 0x0B: return new Color(0xFD953E);
                    case 0x0E: return new Color(0x5DBAFC);
                }
                // пассажирский
                return new Color(0xFFE469);
            case 0x10:
            case 0x20:
            case 0x50:
            case 0x60:
            case 0x70:
            case 0x90: return new Color(0xDEFF00);
            case 0x11: return new Color(0xDE9798);
            case 0x12:
            case 0x22:
            case 0x52:
            case 0x62:
            case 0x72:
            case 0x92: return new Color(0xC0A1FF);
            case 0x13:
            case 0x23:
            case 0x53:
            case 0x63:
            case 0x73:
            case 0x93: return new Color(0xFF2727);
            case 0x14:
            case 0x24:
            case 0x54:
            case 0x64:
            case 0x74:
            case 0x94: return new Color(0xAEFAB2);
            case 0x15:
            case 0x25:
            case 0x55:
            case 0x65:
            case 0x75:
            case 0x95: return new Color(0x5DBAFC);
            case 0x16:
            case 0x26:
            case 0x56:
            case 0x66:
            case 0x76:
            case 0x96: return new Color(0xFC5757);
            case 0x57: return new Color(0xFC5758);
            case 0x27:
            case 0x77: return new Color(0xD571FF);
            case 0x97: return new Color(0xFC5755);
            case 0x18:
            case 0x28:
            case 0x58:
            case 0x68:
            case 0x98:
            case 0x78: return new Color(0xFD953E);
            case 0x19:
            case 0x29:
            case 0x59:
            case 0x69:
            case 0x99:
            case 0x7A: return new Color(0x17FFB9);
            case 0x5A:
            case 0x2A:
            case 0x6A:
            case 0x9A:
            case 0x2B:
            case 0x7B: return new Color(0xFDC13E);
            case 0x7C: return new Color(0xFF8768);
            case 0x7D: return new Color(0xFFBBBB);
            case 0x7E: return new Color(0xFC876E);
            case 0x2F:
            case 0x5F:
            case 0x6F:
            case 0x9F: return new Color(0xD518FF);
            case 0x5B:
            case 0x9B:
            case 0x7F: return new Color(0x9AFADD);
            case 0x2C:
            case 0x6C:
            case 0x5C:
            case 0x9C:
            case 0xAA: return new Color(0xFCE0E0);
            case 0xEA: return new Color(0xFF8686);
            case 0x5D: return new Color(0x57B7FF);
            case 0x2E:
            case 0x5E: return new Color(0xFF8687);
            // пассажирский
            case 0x4E:
                switch (subId) {
                    case 0x80: return new Color(0x17FF9D);
                    case 0x40: return new Color(0xFF8085);
                    case 0xC0: return new Color(0xE109FF);
                }
                break;
            case 0x4D:
                switch (subId) {
                    case 0x80: return new Color(0x2DFAA3);
                    case 0x40: return new Color(0xFF8085);
                }
                break;
            case 0x2D:
            case 0x6D:
            case 0x9D:
            case 0x71:
            case 0x44:
                switch (subId) {
                    case 0x09: return new Color(0x2DFF6B);
                    case 0x0A: return new Color(0xD5FF80);
                    case 0x04: return new Color(0xFCDDDD);
                }
                break;
            // грузовой
            case 0x1D:
                switch (subId) {
                    case 0x04: return new Color(0x2DFF6B);
                    case 0x05: return new Color(0xD5FF80);
                    case 0x08: return new Color(0xFCDDDD);
                    case 0x09: return new Color(0xFF8085);
                    case 0x0A: return new Color(0xE109FF);
                    case 0x0B: return new Color(0x57B7FC);
                    case 0x0C: return new Color(0xFDC13E);
                    case 0x0D: return new Color(0x9AFADD);
                    case 0x0E: return new Color(0xFD953E);
                }
                break;
            // асим
            case 0xC0:
                switch (subId) {
                    case 0x01: return new Color(0x17FF9D);
                    case 0x02: return new Color(0xFF8085);
                }
                return new Color(0xFFE469);
            case 0xC2:
                switch (subId) {
                    case 0x01: return new Color(0xE109FF);
                }
                return new Color(0xFDC13E);
            case 0xC3: // энергия по секциям
//                switch (subId) {
//                    case 0x01: return new Color(0x17FF9D);
//                    case 0x02: return new Color(0xFF8085);
//                    case 0x03: return new Color(0x9AFADD);
//                }
                return new Color(0xFF8085);
            case 0xC4: return new Color(0xFCE0E1);
            case 0xC5:
                switch (subId) {
                    case 0x01: return new Color(0xFD953E);
                }
                return new Color(0xFDC13E);
            case 0xC7: return new Color(0x104F23);
        }
        return new Color(0x434F01);
    }

    /**
     * поиск и
     * @param nBl - номер блока отсчета поиска
     * @param idBl - тип блока для поиска
     * @param subIdBl - дополнительый идентификатор блока
     * @param isUp - направление поиска
     * @return - строку в таблице со следующим или предыдущим блоком по отношению к nBl
     */
    private int getRowByIdBl(int nBl, int idBl, int subIdBl, boolean isUp) {
        int nextBl;
        if (isUp)
            nextBl = blocks.getNearToRightBl(nBl, idBl, subIdBl, 0);
        else
            nextBl = blocks.getNearToLeftBl(nBl, idBl, subIdBl, 0);
        return nextBl - startBl;
    }

//    /**
//     * фокусировка по умолчанию
//     * @param isFocusable -
//     */
//    public void setTableFocusable(boolean isFocusable) {
//        table.setFocusable(isFocusable);
//    }

    /**
     * @param row - номер строки
     * @return - номер блока выделенной строки таблицы
     */
    private int getBlockByRow(int row) throws ParseException {
        if (row >= 0 && row < table.getRowCount()) {
            String str = (String) table.getValueAt(row, COL_NUM_BL);
            NumberFormat nf = NumberFormat.getNumberInstance();
            return nf.parse(str).intValue();
        } else return -1;
    }

    /**
     * @param hex строка в формате 0x00
     * @return -
     */
    private static int[] getIntFromHex(String hex) {
        int id = 0;
        int sub_id = 0;
        String[] splitter = hex.split("[x, _]+");
        if (splitter.length > 1)
            id = Integer.parseInt(splitter[1], 16);
        if (splitter.length > 2)
            sub_id = Integer.parseInt(splitter[2], 16);
        return new int[]{id, sub_id};
    }

    /**
     * @param row - строка
     * @return id блока выделенной строки таблицы (index = 0 - id, index = 1 - subId)
     */
    private int[] getIdBlFromTable(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            String str = (String) table.getValueAt(row, COL_ID_BL);
            return getIntFromHex(str);
        }
        else
            return null;
    }

    /**
     * @param row - строка
     * @return секунда выделенной строки таблицы
     */
    private int getSecondFromTable(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            String str = (String) table.getValueAt(row, COL_SECOND);
            return  Integer.parseInt(str);
        }
        else
            return -1;
    }

    /**
     * событие смены выделенной строки таблицы
     */
    private void doChange() throws ParseException {
        curRow = table.getSelectedRow();
        if (curRow > -1) {
            curBl = getBlockByRow(curRow);
            int[] id = getIdBlFromTable(curRow);
            if (id != null) {
                curIdBl = id[0];
                curSubIdBl = id[1];
            }
            curSecond = getSecondFromTable(curRow);
        }
    }
}
