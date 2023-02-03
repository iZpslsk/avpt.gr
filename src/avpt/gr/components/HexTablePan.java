package avpt.gr.components;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;
import avpt.gr.common.UtilsArmG;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import static avpt.gr.common.UtilsArmG.hexTableFont;

public class HexTablePan extends JPanel {
    public static int hexWidthNumBlk = 70;
    public static int hexWidthIdBlk = 55;
    public static int hexWidthSecond = 70;
    public static int hexWidthCell = 30;
    private static final Color BACK_COLOR_SEL = new Color(0x555457);
    private final ArrBlock32 arrBlock32;
    private final JTable table;
    private final int startBlk;
    private final int endBlk;
    private int curId;
    private int curSubId;
    private int curRow = -1;
    private int curBl = 0;
    private int curType = -1;
    private int curSubType = -1;
    private int curSecond = -1;

    // порядковые номера колонок
    private static final int COL_BL = 0;
    private static final int COL_TYPE = 1;
    private static final int COL_SECOND = 2;
    // search radio items
    private final JMenu menu_0x21 = new JMenu("0x21");
    private final JMenu menu_0x1D = new JMenu("0x1D");

    private final JMenu menu_0xC0 = new JMenu("0xC0");
    private final JMenu menu_0xC2 = new JMenu("0xC2");
    private final JMenu menu_0xC3 = new JMenu("0xC3");
    private final JMenu menu_0xC5 = new JMenu("0xC5");

    private final JMenu menu_0x2D = new JMenu("0x2D");
    private final JMenu menu_0x6D = new JMenu("0x6D");
    private final JMenu menu_0x9D = new JMenu("0x9D");
    private final JMenu menu_0x71 = new JMenu("0x71");
    private final JMenu menu_0x44 = new JMenu("0x44");

    private JRadioButtonMenuItem item_0x21;
    private JRadioButtonMenuItem item_0x21_1;
    private JRadioButtonMenuItem item_0x21_2;
    private JRadioButtonMenuItem item_0x21_3;
    private JRadioButtonMenuItem item_0x21_4;
    private JRadioButtonMenuItem item_0x21_5;
    private JRadioButtonMenuItem item_0x21_6;
    private JRadioButtonMenuItem item_0x21_7;
    private JRadioButtonMenuItem item_0x21_8;
    private JRadioButtonMenuItem item_0x21_9;
    private JRadioButtonMenuItem item_0x21_A;
    private JRadioButtonMenuItem item_0x21_B;
    private JRadioButtonMenuItem item_0x21_C;
    private JRadioButtonMenuItem item_0x21_D;
    private JRadioButtonMenuItem item_0x21_E;

    private JRadioButtonMenuItem item_0x1D_4;
    private JRadioButtonMenuItem item_0x1D_5;
    private JRadioButtonMenuItem item_0x1D_8;
    private JRadioButtonMenuItem item_0x1D_9;
    private JRadioButtonMenuItem item_0x1D_A;
    private JRadioButtonMenuItem item_0x1D_B;
    private JRadioButtonMenuItem item_0x1D_C;
    private JRadioButtonMenuItem item_0x1D_D;
    private JRadioButtonMenuItem item_0x1D_E;
    private JRadioButtonMenuItem item_0x1D_F;

    private JRadioButtonMenuItem item_0x2D_9;
    private JRadioButtonMenuItem item_0x2D_A;
    private JRadioButtonMenuItem item_0x2D_4;
    private JRadioButtonMenuItem item_0x6D_9;
    private JRadioButtonMenuItem item_0x6D_A;
    private JRadioButtonMenuItem item_0x6D_4;
    private JRadioButtonMenuItem item_0x9D_9;
    private JRadioButtonMenuItem item_0x9D_A;
    private JRadioButtonMenuItem item_0x9D_4;
    private JRadioButtonMenuItem item_0x71_9;
    private JRadioButtonMenuItem item_0x71_A;
    private JRadioButtonMenuItem item_0x71_4;
    private JRadioButtonMenuItem item_0x44_9;
    private JRadioButtonMenuItem item_0x44_A;
    private JRadioButtonMenuItem item_0x44_4;

    private JRadioButtonMenuItem item_0x50;
    private JRadioButtonMenuItem item_0x51;
    private JRadioButtonMenuItem item_0x52;
    private JRadioButtonMenuItem item_0x53;
    private JRadioButtonMenuItem item_0x54;
    private JRadioButtonMenuItem item_0x55;
    private JRadioButtonMenuItem item_0x56;
    private JRadioButtonMenuItem item_0x57;
    private JRadioButtonMenuItem item_0x58;
    private JRadioButtonMenuItem item_0x59;
    private JRadioButtonMenuItem item_0x5A;
    private JRadioButtonMenuItem item_0x5B;
    private JRadioButtonMenuItem item_0x5C;
    private JRadioButtonMenuItem item_0x5D;
    private JRadioButtonMenuItem item_0x5E;
    private JRadioButtonMenuItem item_0x5F;

    private JRadioButtonMenuItem item_0x10;
    private JRadioButtonMenuItem item_0x11;
    private JRadioButtonMenuItem item_0x12;
    private JRadioButtonMenuItem item_0x13;
    private JRadioButtonMenuItem item_0x14;
    private JRadioButtonMenuItem item_0x15;
    private JRadioButtonMenuItem item_0x16;
    private JRadioButtonMenuItem item_0x17;
    private JRadioButtonMenuItem item_0x18;
    private JRadioButtonMenuItem item_0x19;
    private JRadioButtonMenuItem item_0x1A;
    private JRadioButtonMenuItem item_0x1B;
    private JRadioButtonMenuItem item_0x1C;
    private JRadioButtonMenuItem item_0x1D;
    private JRadioButtonMenuItem item_0x1E;
    private JRadioButtonMenuItem item_0x1F;

    private JRadioButtonMenuItem item_0xC0_0;
    private JRadioButtonMenuItem item_0xC0_1;
    private JRadioButtonMenuItem item_0xC0_2;
    private JRadioButtonMenuItem item_0xC0_3;
    private JRadioButtonMenuItem item_0xC2_0;
    private JRadioButtonMenuItem item_0xC2_1;
    // секции
    private JRadioButtonMenuItem item_0xC3_1;
    private JRadioButtonMenuItem item_0xC3_2;
    private JRadioButtonMenuItem item_0xC3_3;
    private JRadioButtonMenuItem item_0xC3_4;
    //
    private JRadioButtonMenuItem item_0xC4;
    private JRadioButtonMenuItem item_0xC5_0;
    private JRadioButtonMenuItem item_0xC5_1;
    private JRadioButtonMenuItem item_0xC6;
    private JRadioButtonMenuItem item_0xC7;

    private ButtonGroup buttonGroup;

    /**
     * создаем таблицу между startBlk и endBlk блоками из массива arrBlock32 (сторок в таблице = endBlk - startBlk)
     * @param arrBlock32  - массив данных
     * @param startBlk - номер блока начала
     * @param endBlk - номер блока конца
     */
    public HexTablePan(ArrBlock32 arrBlock32, int startBlk, int endBlk) {
        setLayout(new BorderLayout());
        if (startBlk < 0) startBlk = 0;
        if (endBlk > arrBlock32.size() - 1) endBlk = arrBlock32.size() - 1;
        this.arrBlock32 = arrBlock32;
        this.startBlk = startBlk;
        this.endBlk = endBlk;
        TableModel tableModel = new TableModel();
        table = new JTable(tableModel);
        table.setBackground(Color.BLACK);
        table.setSelectionBackground(BACK_COLOR_SEL);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(hexTableFont);
//        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent event) {
//                try {
//                    doChange();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                // установка вертикального курсора в соответствии с таблицей hexTab
//          //      setMarkerByRowTab(tab);
//            }
//        });
        table.setFocusable(false);
        if (table.getRowCount() > 0) selectRow(0);

        setPropColumn(table.getColumnModel().getColumn(0), hexWidthNumBlk, "№ блока");
        setPropColumn(table.getColumnModel().getColumn(1), hexWidthIdBlk, "Id блока");
        setPropColumn(table.getColumnModel().getColumn(2), hexWidthSecond, "Секунд");
        for (int i = 3; i <= 31; i++)
            setPropColumn(table.getColumnModel().getColumn(i), hexWidthCell, Integer.toString(i - 2));
        for (int i = 0; i < table.getColumnCount(); i++) {
            // блок - цвет
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {

				public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    if (column == 0) {
                        setForeground(Color.WHITE);
                        table.setSelectionForeground(Color.WHITE);
                    }
                    else {
                        setForeground(getColorById(curId, curSubId));
                        table.setSelectionForeground(getColorById(curId, curSubId));
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected,
                            hasFocus, row, column);
                }
            });
            setHotKeys();
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        JPopupMenu popupMenu = makePopupMenu();
        table.setComponentPopupMenu(popupMenu);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                setSelectedSearchItem(curType, curSubType);
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    /**
     * выделение пункта при открытии меню поиска по типу
     * @param curType - тип блока
     * @param curSubType - подтип блока
     */
    private void setSelectedSearchItem(int curType, int curSubType) {
        switch (curType) {
            case 0x21 :
                item_0x21.setSelected(true);
                switch (curSubType) {
                    case 0x01: item_0x21_1.setSelected(true);
                        break;
                    case 0x02: item_0x21_2.setSelected(true);
                        break;
                    case 0x03: item_0x21_3.setSelected(true);
                        break;
                    case 0x04: item_0x21_4.setSelected(true);
                        break;
                    case 0x05: item_0x21_5.setSelected(true);
                        break;
                    case 0x06: item_0x21_6.setSelected(true);
                        break;
                    case 0x07: item_0x21_7.setSelected(true);
                        break;
                    case 0x08: item_0x21_8.setSelected(true);
                        break;
                    case 0x09: item_0x21_9.setSelected(true);
                        break;
                    case 0x0A: item_0x21_A.setSelected(true);
                        break;
                    case 0x0B: item_0x21_B.setSelected(true);
                        break;
                    case 0x0C: item_0x21_C.setSelected(true);
                        break;
                    case 0x0D: item_0x21_D.setSelected(true);
                        break;
                    case 0x0E: item_0x21_E.setSelected(true);
                        break;
                }
                break;
            case 0x1D :
                switch (curSubType) {
                    case 0x04: item_0x1D_4.setSelected(true);
                        break;
                    case 0x05: item_0x1D_5.setSelected(true);
                        break;
                    case 0x08: item_0x1D_8.setSelected(true);
                        break;
                    case 0x09: item_0x1D_9.setSelected(true);
                        break;
                    case 0x0A: item_0x1D_A.setSelected(true);
                        break;
                    case 0x0B: item_0x1D_B.setSelected(true);
                        break;
                    case 0x0C: item_0x1D_C.setSelected(true);
                        break;
                    case 0x0D: item_0x1D_D.setSelected(true);
                        break;
                    case 0x0E: item_0x1D_E.setSelected(true);
                        break;
                    case 0x0F: item_0x1D_F.setSelected(true);
                        break;
                }
                break;
            case 0x2D :
                switch (curSubType) {
                    case 0x09: item_0x2D_9.setSelected(true);
                        break;
                    case 0x0A: item_0x2D_A.setSelected(true);
                        break;
                    case 0x04: item_0x2D_4.setSelected(true);
                        break;
                }
                break;
            case 0x6D :
                switch (curSubType) {
                    case 0x09: item_0x6D_9.setSelected(true);
                        break;
                    case 0x0A: item_0x6D_A.setSelected(true);
                        break;
                    case 0x04: item_0x6D_4.setSelected(true);
                        break;
                }
                break;
            case 0x9D :
                switch (curSubType) {
                    case 0x09: item_0x9D_9.setSelected(true);
                        break;
                    case 0x0A: item_0x9D_A.setSelected(true);
                        break;
                    case 0x04: item_0x9D_4.setSelected(true);
                        break;
                }
                break;
            case 0x50 : item_0x50.setSelected(true);
                break;
            case 0x51 : item_0x51.setSelected(true);
                break;
            case 0x52 : item_0x52.setSelected(true);
                break;
            case 0x53 : item_0x53.setSelected(true);
                break;
            case 0x54 : item_0x54.setSelected(true);
                break;
            case 0x55 : item_0x55.setSelected(true);
                break;
            case 0x56 : item_0x56.setSelected(true);
                break;
            case 0x57 : item_0x57.setSelected(true);
                break;
            case 0x58 : item_0x58.setSelected(true);
                break;
            case 0x59 : item_0x59.setSelected(true);
                break;
            case 0x5A : item_0x5A.setSelected(true);
                break;
            case 0x5B : item_0x5B.setSelected(true);
                break;
            case 0x5C : item_0x5C.setSelected(true);
                break;
            case 0x5D : item_0x5D.setSelected(true);
                break;
            case 0x5E : item_0x5E.setSelected(true);
                break;
            case 0x5F : item_0x5F.setSelected(true);
                break;

            case 0x10 : item_0x10.setSelected(true);
                break;
            case 0x11 : item_0x11.setSelected(true);
                break;
            case 0x12 : item_0x12.setSelected(true);
                break;
            case 0x13 : item_0x13.setSelected(true);
                break;
            case 0x14 : item_0x14.setSelected(true);
                break;
            case 0x15 : item_0x15.setSelected(true);
                break;
            case 0x16 : item_0x16.setSelected(true);
                break;
            case 0x17 : item_0x17.setSelected(true);
                break;
            case 0x18 : item_0x18.setSelected(true);
                break;
            case 0x19 : item_0x19.setSelected(true);
                break;
            case 0x1A : item_0x1A.setSelected(true);
                break;
            case 0x1B : item_0x1B.setSelected(true);
                break;
            case 0x1C : item_0x1C.setSelected(true);
                break;
       //     case 0x1D : item_0x5D.setSelected(true);
       //         break;
            case 0x1E : item_0x1E.setSelected(true);
                break;
            case 0x1F : item_0x1F.setSelected(true);
                break;

            case 0xC0 :
                switch (curSubType) {
                    case 0x00: item_0xC0_0.setSelected(true);
                        break;
                    case 0x01: item_0xC0_1.setSelected(true);
                        break;
                    case 0x02: item_0xC0_2.setSelected(true);
                        break;
                    case 0x03: item_0xC0_3.setSelected(true);
                        break;
                }
                break;
            case 0xC2 :
                switch (curSubType) {
                    case 0x00: item_0xC2_0.setSelected(true);
                        break;
                    case 0x01: item_0xC2_1.setSelected(true);
                        break;
                }
                break;
            case 0xC3 :
                switch (curSubType) {
                    case 0x01: item_0xC3_1.setSelected(true);
                        break;
                    case 0x02: item_0xC3_2.setSelected(true);
                        break;
                    case 0x03: item_0xC3_3.setSelected(true);
                        break;
                    case 0x04: item_0xC3_4.setSelected(true);
                        break;
                }
                break;
            case 0xC4 : item_0xC4.setSelected(true);
                break;
            case 0xC5 :
                switch (curSubType) {
                    case 0x00: item_0xC5_0.setSelected(true);
                        break;
                    case 0x01: item_0xC5_1.setSelected(true);
                        break;
                }
                break;
            case 0xC6 : item_0xC6.setSelected(true);
                break;
            case 0xC7 : item_0xC7.setSelected(true);
                break;
        }
    }

    /**
     * создание пунктов меню для поиска по типу
     * @param menu - menuSearchType
     */
    private void createSearchRadioItems(JMenu menu) {

        String temp = "<html>%s  <font color=\"#5F9EA0\"size=\"2\"> F3</font>" +
                "↓<font color=\"#5F9EA0\" size=\"2\"> +Shift</font>↑</html>";

        item_0x21 = new JRadioButtonMenuItem(String.format(temp, "0x21"));
        item_0x21.addActionListener(new SearchAction(0x21, 0,true));

        item_0x21_1 = new JRadioButtonMenuItem(String.format(temp, "0x21_1"));
        item_0x21_1.addActionListener(new SearchAction(0x21, 0x01, true));
        menu_0x21.add(item_0x21_1);

        item_0x21_2 = new JRadioButtonMenuItem(String.format(temp, "0x21_2"));
        item_0x21_2.addActionListener(new SearchAction(0x21, 0x02, true));
        menu_0x21.add(item_0x21_2);

        item_0x21_3 = new JRadioButtonMenuItem(String.format(temp, "0x21_3"));
        item_0x21_3.addActionListener(new SearchAction(0x21, 0x03, true));
        menu_0x21.add(item_0x21_3);

        item_0x21_4 = new JRadioButtonMenuItem(String.format(temp, "0x21_4"));
        item_0x21_4.addActionListener(new SearchAction(0x21, 0x04, true));
        menu_0x21.add(item_0x21_4);

        item_0x21_5 = new JRadioButtonMenuItem(String.format(temp, "0x21_5"));
        item_0x21_5.addActionListener(new SearchAction(0x21, 0x05, true));
        menu_0x21.add(item_0x21_5);

        item_0x21_6 = new JRadioButtonMenuItem(String.format(temp, "0x21_6"));
        item_0x21_6.addActionListener(new SearchAction(0x21, 0x06, true));
        menu_0x21.add(item_0x21_6);

        item_0x21_7 = new JRadioButtonMenuItem(String.format(temp, "0x21_7"));
        item_0x21_7.addActionListener(new SearchAction(0x21, 0x07, true));
        menu_0x21.add(item_0x21_7);

        item_0x21_8 = new JRadioButtonMenuItem(String.format(temp, "0x21_8"));
        item_0x21_8.addActionListener(new SearchAction(0x21, 0x08, true));
        menu_0x21.add(item_0x21_8);

        item_0x21_9 = new JRadioButtonMenuItem(String.format(temp, "0x21_9"));
        item_0x21_9.addActionListener(new SearchAction(0x21, 0x09, true));
        menu_0x21.add(item_0x21_9);

        item_0x21_A = new JRadioButtonMenuItem(String.format(temp, "0x21_A"));
        item_0x21_A.addActionListener(new SearchAction(0x21, 0x0A, true));
        menu_0x21.add(item_0x21_A);

        item_0x21_B = new JRadioButtonMenuItem(String.format(temp, "0x21_B"));
        item_0x21_B.addActionListener(new SearchAction(0x21, 0x0B, true));
        menu_0x21.add(item_0x21_B);

        item_0x21_C = new JRadioButtonMenuItem(String.format(temp, "0x21_C"));
        item_0x21_C.addActionListener(new SearchAction(0x21, 0x0C, true));
        menu_0x21.add(item_0x21_C);

        item_0x21_D = new JRadioButtonMenuItem(String.format(temp, "0x21_D"));
        item_0x21_D.addActionListener(new SearchAction(0x21, 0x0D, true));
        menu_0x21.add(item_0x21_D);

        item_0x21_E = new JRadioButtonMenuItem(String.format(temp, "0x21_E"));
        item_0x21_E.addActionListener(new SearchAction(0x21, 0x0E, true));
        menu_0x21.add(item_0x21_E);

        item_0x1D_4 = new JRadioButtonMenuItem(String.format(temp, "0x1D_4"));
        item_0x1D_4.addActionListener(new SearchAction(0x1D, 0x04, true));
        menu_0x1D.add(item_0x1D_4);

        item_0x1D_5 = new JRadioButtonMenuItem(String.format(temp, "0x1D_5"));
        item_0x1D_5.addActionListener(new SearchAction(0x1D, 0x05, true));
        menu_0x1D.add(item_0x1D_5);

        item_0x1D_8 = new JRadioButtonMenuItem(String.format(temp, "0x1D_8"));
        item_0x1D_8.addActionListener(new SearchAction(0x1D, 0x08, true));
        menu_0x1D.add(item_0x1D_8);

        item_0x1D_9 = new JRadioButtonMenuItem(String.format(temp, "0x1D_9"));
        item_0x1D_9.addActionListener(new SearchAction(0x1D, 0x09, true));
        menu_0x1D.add(item_0x1D_9);

        item_0x1D_A = new JRadioButtonMenuItem(String.format(temp, "0x1D_A"));
        item_0x1D_A.addActionListener(new SearchAction(0x1D, 0x0A, true));
        menu_0x1D.add(item_0x1D_A);

        item_0x1D_B = new JRadioButtonMenuItem(String.format(temp, "0x1D_B"));
        item_0x1D_B.addActionListener(new SearchAction(0x1D, 0x0B, true));
        menu_0x1D.add(item_0x1D_B);

        item_0x1D_C = new JRadioButtonMenuItem(String.format(temp, "0x1D_C"));
        item_0x1D_C.addActionListener(new SearchAction(0x1D, 0x0C, true));
        menu_0x1D.add(item_0x1D_C);

        item_0x1D_D = new JRadioButtonMenuItem(String.format(temp, "0x1D_D"));
        item_0x1D_D.addActionListener(new SearchAction(0x1D, 0x0D, true));
        menu_0x1D.add(item_0x1D_D);

        item_0x1D_E = new JRadioButtonMenuItem(String.format(temp, "0x1D_E"));
        item_0x1D_E.addActionListener(new SearchAction(0x1D, 0x0E, true));
        menu_0x1D.add(item_0x1D_E);

        item_0x1D_F = new JRadioButtonMenuItem(String.format(temp, "0x1D_F"));
        item_0x1D_F.addActionListener(new SearchAction(0x1D, 0x0F, true));
        menu_0x1D.add(item_0x1D_F);

        item_0x2D_9 = new JRadioButtonMenuItem(String.format(temp, "0x2D_9"));
        item_0x2D_9.addActionListener(new SearchAction(0x2D, 0x09, true));
        menu_0x2D.add(item_0x2D_9);

        item_0x2D_A = new JRadioButtonMenuItem(String.format(temp, "0x2D_A"));
        item_0x2D_A.addActionListener(new SearchAction(0x2D, 0x0A, true));
        menu_0x2D.add(item_0x2D_A);

        item_0x2D_4 = new JRadioButtonMenuItem(String.format(temp, "0x2D_4"));
        item_0x2D_4.addActionListener(new SearchAction(0x2D, 0x04, true));
        menu_0x2D.add(item_0x2D_4);

        item_0x6D_9 = new JRadioButtonMenuItem(String.format(temp, "0x6D_9"));
        item_0x6D_9.addActionListener(new SearchAction(0x6D, 0x09, true));
        menu_0x6D.add(item_0x6D_9);

        item_0x6D_A = new JRadioButtonMenuItem(String.format(temp, "0x6D_A"));
        item_0x6D_A.addActionListener(new SearchAction(0x6D, 0x0A, true));
        menu_0x6D.add(item_0x6D_A);

        item_0x6D_4 = new JRadioButtonMenuItem(String.format(temp, "0x6D_4"));
        item_0x6D_4.addActionListener(new SearchAction(0x6D, 0x04, true));
        menu_0x6D.add(item_0x6D_4);

        item_0x9D_9 = new JRadioButtonMenuItem(String.format(temp, "0x9D_9"));
        item_0x9D_9.addActionListener(new SearchAction(0x9D, 0x09, true));
        menu_0x9D.add(item_0x9D_9);

        item_0x9D_A = new JRadioButtonMenuItem(String.format(temp, "0x9D_A"));
        item_0x9D_A.addActionListener(new SearchAction(0x9D, 0x0A, true));
        menu_0x9D.add(item_0x9D_A);

        item_0x9D_4 = new JRadioButtonMenuItem(String.format(temp, "0x9D_4"));
        item_0x9D_4.addActionListener(new SearchAction(0x9D, 0x04, true));
        menu_0x9D.add(item_0x9D_4);

        item_0x71_9 = new JRadioButtonMenuItem(String.format(temp, "0x71_9"));
        item_0x71_9.addActionListener(new SearchAction(0x71, 0x09, true));
        menu_0x71.add(item_0x71_9);

        item_0x71_A = new JRadioButtonMenuItem(String.format(temp, "0x71_A"));
        item_0x71_A.addActionListener(new SearchAction(0x71, 0x0A, true));
        menu_0x71.add(item_0x71_A);

        item_0x71_4 = new JRadioButtonMenuItem(String.format(temp, "0x71_4"));
        item_0x71_4.addActionListener(new SearchAction(0x71, 0x04, true));
        menu_0x71.add(item_0x71_4);

        item_0x44_9 = new JRadioButtonMenuItem(String.format(temp, "0x44_9"));
        item_0x44_9.addActionListener(new SearchAction(0x44, 0x09, true));
        menu_0x44.add(item_0x44_9);

        item_0x44_A = new JRadioButtonMenuItem(String.format(temp, "0x44_A"));
        item_0x44_A.addActionListener(new SearchAction(0x44, 0x0A, true));
        menu_0x44.add(item_0x44_A);

        item_0x44_4 = new JRadioButtonMenuItem(String.format(temp, "0x44_4"));
        item_0x44_4.addActionListener(new SearchAction(0x44, 0x04, true));
        menu_0x44.add(item_0x44_4);

        item_0x50 = new JRadioButtonMenuItem(String.format(temp, "0x50"));
        item_0x50.addActionListener(new SearchAction(0x50, 0,true));

        item_0x51 = new JRadioButtonMenuItem(String.format(temp, "0x51"));
        item_0x51.addActionListener(new SearchAction(0x51, 0,true));

        item_0x52 = new JRadioButtonMenuItem(String.format(temp, "0x52"));
        item_0x52.addActionListener(new SearchAction(0x52, 0,true));

        item_0x53 = new JRadioButtonMenuItem(String.format(temp, "0x53"));
        item_0x53.addActionListener(new SearchAction(0x53, 0,true));

        item_0x54 = new JRadioButtonMenuItem(String.format(temp, "0x54"));
        item_0x54.addActionListener(new SearchAction(0x54, 0,true));

        item_0x55 = new JRadioButtonMenuItem(String.format(temp, "0x55"));
        item_0x55.addActionListener(new SearchAction(0x55, 0,true));

        item_0x56 = new JRadioButtonMenuItem(String.format(temp, "0x56"));
        item_0x56.addActionListener(new SearchAction(0x56, 0,true));

        item_0x57 = new JRadioButtonMenuItem(String.format(temp, "0x57"));
        item_0x57.addActionListener(new SearchAction(0x57, 0,true));

        item_0x58 = new JRadioButtonMenuItem(String.format(temp, "0x58"));
        item_0x58.addActionListener(new SearchAction(0x58, 0,true));

        item_0x59 = new JRadioButtonMenuItem(String.format(temp, "0x59"));
        item_0x59.addActionListener(new SearchAction(0x59, 0,true));

        item_0x5A = new JRadioButtonMenuItem(String.format(temp, "0x5A"));
        item_0x5A.addActionListener(new SearchAction(0x5A, 0, true));

        item_0x5B = new JRadioButtonMenuItem(String.format(temp, "0x5B"));
        item_0x5B.addActionListener(new SearchAction(0x5B, 0, true));

        item_0x5C = new JRadioButtonMenuItem(String.format(temp, "0x5C"));
        item_0x5C.addActionListener(new SearchAction(0x5C, 0, true));

        item_0x5D = new JRadioButtonMenuItem(String.format(temp, "0x5D"));
        item_0x5D.addActionListener(new SearchAction(0x5D, 0, true));

        item_0x5E = new JRadioButtonMenuItem(String.format(temp, "0x5E"));
        item_0x5E.addActionListener(new SearchAction(0x5E, 0,true));

        item_0x5F = new JRadioButtonMenuItem(String.format(temp, "0x5F"));
        item_0x5F.addActionListener(new SearchAction(0x5F, 0, true));


        item_0x10 = new JRadioButtonMenuItem(String.format(temp, "0x10"));
        item_0x10.addActionListener(new SearchAction(0x10, 0,true));

        item_0x11 = new JRadioButtonMenuItem(String.format(temp, "0x11"));
        item_0x11.addActionListener(new SearchAction(0x11, 0,true));

        item_0x12 = new JRadioButtonMenuItem(String.format(temp, "0x12"));
        item_0x12.addActionListener(new SearchAction(0x12, 0,true));

        item_0x13 = new JRadioButtonMenuItem(String.format(temp, "0x13"));
        item_0x13.addActionListener(new SearchAction(0x13, 0,true));

        item_0x14 = new JRadioButtonMenuItem(String.format(temp, "0x14"));
        item_0x14.addActionListener(new SearchAction(0x14, 0,true));

        item_0x15 = new JRadioButtonMenuItem(String.format(temp, "0x15"));
        item_0x15.addActionListener(new SearchAction(0x15, 0,true));

        item_0x16 = new JRadioButtonMenuItem(String.format(temp, "0x16"));
        item_0x16.addActionListener(new SearchAction(0x56, 0,true));

        item_0x17 = new JRadioButtonMenuItem(String.format(temp, "0x17"));
        item_0x17.addActionListener(new SearchAction(0x17, 0,true));

        item_0x18 = new JRadioButtonMenuItem(String.format(temp, "0x18"));
        item_0x18.addActionListener(new SearchAction(0x18, 0,true));

        item_0x19 = new JRadioButtonMenuItem(String.format(temp, "0x19"));
        item_0x19.addActionListener(new SearchAction(0x19, 0,true));

        item_0x1A = new JRadioButtonMenuItem(String.format(temp, "0x1A"));
        item_0x1A.addActionListener(new SearchAction(0x1A, 0, true));

        item_0x1B = new JRadioButtonMenuItem(String.format(temp, "0x1B"));
        item_0x1B.addActionListener(new SearchAction(0x1B, 0, true));

        item_0x1C = new JRadioButtonMenuItem(String.format(temp, "0x1C"));
        item_0x1C.addActionListener(new SearchAction(0x1C, 0, true));

        item_0x1D = new JRadioButtonMenuItem(String.format(temp, "0x1D"));
        item_0x1D.addActionListener(new SearchAction(0x1D, 0, true));

        item_0x1E = new JRadioButtonMenuItem(String.format(temp, "0x1E"));
        item_0x1E.addActionListener(new SearchAction(0x1E, 0,true));

        item_0x1F = new JRadioButtonMenuItem(String.format(temp, "0x1F"));
        item_0x1F.addActionListener(new SearchAction(0x1F, 0, true));


        // asim
        item_0xC0_0 = new JRadioButtonMenuItem(String.format(temp, "0xC0_0"));
        item_0xC0_0.addActionListener(new SearchAction(0xC0, 0x00, true));
        menu_0xC0.add(item_0xC0_0);

        item_0xC0_1 = new JRadioButtonMenuItem(String.format(temp, "0xC0_1"));
        item_0xC0_1.addActionListener(new SearchAction(0xC0, 0x01, true));
        menu_0xC0.add(item_0xC0_1);

        item_0xC0_2 = new JRadioButtonMenuItem(String.format(temp, "0xC0_2"));
        item_0xC0_2.addActionListener(new SearchAction(0xC0, 0x02, true));
        menu_0xC0.add(item_0xC0_2);

        item_0xC0_3 = new JRadioButtonMenuItem(String.format(temp, "0xC0_3"));
        item_0xC0_3.addActionListener(new SearchAction(0xC0, 0x03, true));
        menu_0xC0.add(item_0xC0_3);

        item_0xC2_0 = new JRadioButtonMenuItem(String.format(temp, "0xC2_0"));
        item_0xC2_0.addActionListener(new SearchAction(0xC2, 0x00, true));
        menu_0xC2.add(item_0xC2_0);

        item_0xC2_1 = new JRadioButtonMenuItem(String.format(temp, "0xC2_1"));
        item_0xC2_1.addActionListener(new SearchAction(0xC2, 0x01, true));
        menu_0xC2.add(item_0xC2_1);

        item_0xC3_1 = new JRadioButtonMenuItem(String.format(temp, "0xC3_1"));
        item_0xC3_1.addActionListener(new SearchAction(0xC3, 0x01, true));
        menu_0xC3.add(item_0xC3_1);

        item_0xC3_2 = new JRadioButtonMenuItem(String.format(temp, "0xC3_2"));
        item_0xC3_2.addActionListener(new SearchAction(0xC3, 0x02, true));
        menu_0xC3.add(item_0xC3_2);

        item_0xC3_3 = new JRadioButtonMenuItem(String.format(temp, "0xC3_3"));
        item_0xC3_3.addActionListener(new SearchAction(0xC3, 0x03, true));
        menu_0xC3.add(item_0xC3_3);

        item_0xC3_4 = new JRadioButtonMenuItem(String.format(temp, "0xC3_4"));
        item_0xC3_4.addActionListener(new SearchAction(0xC3, 0x04, true));
        menu_0xC3.add(item_0xC3_4);

//        item_0xC3 = new JRadioButtonMenuItem(String.format(temp, "0xC3"));
//        item_0xC3.addActionListener(new SearchAction(0xC3, 0, true));

        item_0xC4 = new JRadioButtonMenuItem(String.format(temp, "0xC4"));
        item_0xC4.addActionListener(new SearchAction(0xC4, 0, true));

        item_0xC5_0 = new JRadioButtonMenuItem(String.format(temp, "0xC5_0"));
        item_0xC5_0.addActionListener(new SearchAction(0xC5, 0x00, true));
        menu_0xC5.add(item_0xC5_0);

        item_0xC5_1 = new JRadioButtonMenuItem(String.format(temp, "0xC5_1"));
        item_0xC5_1.addActionListener(new SearchAction(0xC5, 0x01, true));
        menu_0xC5.add(item_0xC5_1);

        item_0xC6 = new JRadioButtonMenuItem(String.format(temp, "0xC6"));
        item_0xC6.addActionListener(new SearchAction(0xC6, 0, true));

        item_0xC7 = new JRadioButtonMenuItem(String.format(temp, "0xC7"));
        item_0xC7.addActionListener(new SearchAction(0xC7, 0, true));

        buttonGroup.add(item_0x21);
        buttonGroup.add(item_0x21_1);
        buttonGroup.add(item_0x21_2);
        buttonGroup.add(item_0x21_3);
        buttonGroup.add(item_0x21_4);
        buttonGroup.add(item_0x21_5);
        buttonGroup.add(item_0x21_6);
        buttonGroup.add(item_0x21_7);
        buttonGroup.add(item_0x21_8);
        buttonGroup.add(item_0x21_9);
        buttonGroup.add(item_0x21_A);
        buttonGroup.add(item_0x21_B);
        buttonGroup.add(item_0x21_C);
        buttonGroup.add(item_0x21_D);
        buttonGroup.add(item_0x21_E);

        buttonGroup.add(item_0x1D_4);
        buttonGroup.add(item_0x1D_5);
        buttonGroup.add(item_0x1D_8);
        buttonGroup.add(item_0x1D_9);
        buttonGroup.add(item_0x1D_A);
        buttonGroup.add(item_0x1D_B);
        buttonGroup.add(item_0x1D_C);
        buttonGroup.add(item_0x1D_D);
        buttonGroup.add(item_0x1D_E);
        buttonGroup.add(item_0x1D_F);

        buttonGroup.add(item_0x2D_9);
        buttonGroup.add(item_0x2D_A);
        buttonGroup.add(item_0x2D_4);
        buttonGroup.add(item_0x6D_9);
        buttonGroup.add(item_0x6D_A);
        buttonGroup.add(item_0x6D_4);
        buttonGroup.add(item_0x9D_9);
        buttonGroup.add(item_0x9D_A);
        buttonGroup.add(item_0x9D_4);
        buttonGroup.add(item_0x71_9);
        buttonGroup.add(item_0x71_A);
        buttonGroup.add(item_0x71_4);
        buttonGroup.add(item_0x44_9);
        buttonGroup.add(item_0x44_A);
        buttonGroup.add(item_0x44_4);

        buttonGroup.add(item_0x50);
        buttonGroup.add(item_0x51);
        buttonGroup.add(item_0x52);
        buttonGroup.add(item_0x53);
        buttonGroup.add(item_0x54);
        buttonGroup.add(item_0x55);
        buttonGroup.add(item_0x56);
        buttonGroup.add(item_0x57);
        buttonGroup.add(item_0x58);
        buttonGroup.add(item_0x59);
        buttonGroup.add(item_0x5A);
        buttonGroup.add(item_0x5B);
        buttonGroup.add(item_0x5C);
        buttonGroup.add(item_0x5D);
        buttonGroup.add(item_0x5E);
        buttonGroup.add(item_0x5F);

        buttonGroup.add(item_0x10);
        buttonGroup.add(item_0x11);
        buttonGroup.add(item_0x12);
        buttonGroup.add(item_0x13);
        buttonGroup.add(item_0x14);
        buttonGroup.add(item_0x15);
        buttonGroup.add(item_0x16);
        buttonGroup.add(item_0x17);
        buttonGroup.add(item_0x18);
        buttonGroup.add(item_0x19);
        buttonGroup.add(item_0x1A);
        buttonGroup.add(item_0x1B);
        buttonGroup.add(item_0x1C);
        buttonGroup.add(item_0x1D);
        buttonGroup.add(item_0x1E);
        buttonGroup.add(item_0x1F);

        buttonGroup.add(item_0xC0_0);
        buttonGroup.add(item_0xC0_1);
        buttonGroup.add(item_0xC0_2);
        buttonGroup.add(item_0xC0_3);
        buttonGroup.add(item_0xC2_0);
        buttonGroup.add(item_0xC2_1);
        buttonGroup.add(item_0xC3_1);
        buttonGroup.add(item_0xC3_2);
        buttonGroup.add(item_0xC3_3);
        buttonGroup.add(item_0xC3_4);
        buttonGroup.add(item_0xC4);
        buttonGroup.add(item_0xC5_0);
        buttonGroup.add(item_0xC5_1);
        buttonGroup.add(item_0xC6);
        buttonGroup.add(item_0xC7);

        if (arrBlock32.isG()) {
            menu.add(menu_0x21);
            menu.add(menu_0x1D);
//            if (arrBlock32.isExistsIdBlk(0x21)) menu.add(item_0x21);
            if (arrBlock32.isExistsIdBlk(0x50)) menu.add(item_0x50);
            if (arrBlock32.isExistsIdBlk(0x51)) menu.add(item_0x51);
            if (arrBlock32.isExistsIdBlk(0x52)) menu.add(item_0x52);
            if (arrBlock32.isExistsIdBlk(0x53)) menu.add(item_0x53);
            if (arrBlock32.isExistsIdBlk(0x54)) menu.add(item_0x54);
            if (arrBlock32.isExistsIdBlk(0x55)) menu.add(item_0x55);
            if (arrBlock32.isExistsIdBlk(0x56)) menu.add(item_0x56);
            if (arrBlock32.isExistsIdBlk(0x57)) menu.add(item_0x57);
            if (arrBlock32.isExistsIdBlk(0x58)) menu.add(item_0x58);
            if (arrBlock32.isExistsIdBlk(0x59)) menu.add(item_0x59);
            if (arrBlock32.isExistsIdBlk(0x5A)) menu.add(item_0x5A);
            if (arrBlock32.isExistsIdBlk(0x5B)) menu.add(item_0x5B);
            if (arrBlock32.isExistsIdBlk(0x5C)) menu.add(item_0x5C);
            if (arrBlock32.isExistsIdBlk(0x5D)) menu.add(item_0x5D);
            if (arrBlock32.isExistsIdBlk(0x5E)) menu.add(item_0x5E);
            if (arrBlock32.isExistsIdBlk(0x5F)) menu.add(item_0x5F);

            if (arrBlock32.isExistsIdBlk(0x10)) menu.add(item_0x10);
            if (arrBlock32.isExistsIdBlk(0x11)) menu.add(item_0x11);
            if (arrBlock32.isExistsIdBlk(0x12)) menu.add(item_0x12);
            if (arrBlock32.isExistsIdBlk(0x13)) menu.add(item_0x13);
            if (arrBlock32.isExistsIdBlk(0x14)) menu.add(item_0x14);
            if (arrBlock32.isExistsIdBlk(0x15)) menu.add(item_0x15);
            if (arrBlock32.isExistsIdBlk(0x16)) menu.add(item_0x16);
            if (arrBlock32.isExistsIdBlk(0x17)) menu.add(item_0x17);
            if (arrBlock32.isExistsIdBlk(0x18)) menu.add(item_0x18);
            if (arrBlock32.isExistsIdBlk(0x19)) menu.add(item_0x19);
            if (arrBlock32.isExistsIdBlk(0x1A)) menu.add(item_0x1A);
            if (arrBlock32.isExistsIdBlk(0x1B)) menu.add(item_0x1B);
            if (arrBlock32.isExistsIdBlk(0x1C)) menu.add(item_0x1C);
            if (arrBlock32.isExistsIdBlk(0x1D)) menu.add(item_0x1D);
            if (arrBlock32.isExistsIdBlk(0x1E)) menu.add(item_0x1E);
            if (arrBlock32.isExistsIdBlk(0x1F)) menu.add(item_0x1F);
        }
        if (arrBlock32.isP()) {
            if (arrBlock32.isExistsIdBlk(0x2D)) menu.add(menu_0x2D);
            if (arrBlock32.isExistsIdBlk(0x6D)) menu.add(menu_0x6D);
            if (arrBlock32.isExistsIdBlk(0x9D)) menu.add(menu_0x9D);
            if (arrBlock32.isExistsIdBlk(0x71)) menu.add(menu_0x71);
            if (arrBlock32.isExistsIdBlk(0x44)) menu.add(menu_0x44);
            if (arrBlock32.isExistsIdBlk(0x21)) menu.add(item_0x21);
        }
        if (arrBlock32.isASIM()) {
            menu.add(menu_0xC0);
            menu.add(menu_0xC2);
            menu.add(menu_0xC3);
            menu.add(item_0xC4);
            menu.add(menu_0xC5);
            menu.add(item_0xC6);
            menu.add(item_0xC7);
        }
    }

    /**
     * выделение строки в результате поиска типа текущего типа блока
     * @param isUp - направление поиска
     */
    public void selectSearchedType(boolean isUp) {
        try {
            doChange();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int row = getRowByType(curBl, curType, curSubType, isUp);
        if (row >= 0) selectRow(row);
    }

    /**
     * поиск и
     * @param nBl - номер блока отсчета поиска
     * @param typeBl - тип блока для поиска
     * @param subTypeBl - дополнительый идентификатор блока
     * @param isUp - направление поиска
     * @return - строку в таблице со следующим или предыдучим блоком по отношению к nBl
     */
    private int getRowByType(int nBl, int typeBl, int subTypeBl, boolean isUp) {
        int nextBl;
        if (isUp)
            nextBl = arrBlock32.getNearToRightBl(nBl, typeBl, subTypeBl, 0);
        else
            nextBl = arrBlock32.getNearToLeftBl(nBl, typeBl, subTypeBl, 0);
        int row = nextBl - startBlk;
        // смена typeBl и вторая попытка
        if (row < 0)
            switch (typeBl) {
                case 0x50 : typeBl = 0x10;
                    break;
                case 0x51 : typeBl = 0x11;
                    break;
                case 0x52 : typeBl = 0x12;
                    break;
                case 0x53 : typeBl = 0x13;
                    break;
                case 0x54 : typeBl = 0x14;
                    break;
                case 0x55 : typeBl = 0x15;
                    break;
                case 0x56 : typeBl = 0x16;
                    break;
                case 0x57 : typeBl = 0x17;
                    break;
                case 0x58 : typeBl = 0x18;
                    break;
                case 0x59 : typeBl = 0x19;
                    break;
            }
        else return row;

        if (isUp)
            nextBl = arrBlock32.getNearToRightBl(nBl, typeBl, subTypeBl, 0);
        else
            nextBl = arrBlock32.getNearToLeftBl(nBl, typeBl, subTypeBl, 0);
        row = nextBl - startBlk;

        if (row >= 0) return row;
        else return -1;
    }

    /**
     * класс обработки события поиска ближайшей строки определенного типа
     */
    private class SearchAction implements ActionListener {

        int  typeBl;
        int subTypeBl;
        boolean isUp;

        /**
         * конструктор
         * @param typeBl - тип
         * @param isUp - направление поиска
         */
        private SearchAction(int typeBl, int subTypeBl, boolean isUp) {
            this.typeBl = typeBl;
            this.subTypeBl = subTypeBl;
            this.isUp = isUp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = getRowByType(curBl, typeBl, subTypeBl, isUp);
            if (row >= 0) selectRow(row);
        }
    }

    /**
     * @return JPopupMenu
     */
    private JPopupMenu makePopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenu menuSearchTypeUp = new JMenu("Поиск по id");
        buttonGroup = new ButtonGroup();
        createSearchRadioItems(menuSearchTypeUp);
        popup.add(menuSearchTypeUp);
        return popup;
    }

    /**
     * событие смены выделенной строки таблицы
     */
    public void doChange() throws ParseException {
        curRow = table.getSelectedRow();
        if (curRow > -1) {
            curBl = getBlockByRow(curRow);
            int[] types = getTypeFromTable(curRow);
            if (types != null) {
                curType = types[0];
                curSubType = types[1];
            }
            curSecond = getSecondFromTable(curRow);
        }
    }

    /**
     * @param row - номер строки
     * @return - значение nBlock соответствующее строке таблицы
     */
    private int getBlockByRow(int row) throws ParseException {
        if (row >= 0 && row < table.getRowCount()) {
            String str = (String) table.getValueAt(row, COL_BL);
            NumberFormat nf = NumberFormat.getNumberInstance();
            return nf.parse(str).intValue();
        } else return -1;
    }

    /**
     * @param row - строка
     * @return тип из таблицы
     */
    private int[] getTypeFromTable(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            String str = (String) table.getValueAt(row, COL_TYPE);
            return  UtilsArmG.getIntFromHex(str);
        }
        else
            return null;
    }

    /**
     * @param row - строка
     * @return second form table
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
     * @return исходная таблица
     */
    public JTable getTable() {
    		return table;
    }

    /**
     * свойства колонки
     * @param column - колонка таблицы
     * @param width	- ширина колонки
     * @param header - заголовок
     */
    public static void setPropColumn(TableColumn column, int width, String header) {
        column.setMinWidth(width / 2);
        column .setMaxWidth(width * 2);
        column.setPreferredWidth(width);
        column.setHeaderValue(header);
    }

    /**
     * цвет строки в зависимости от id блока
     * @param idBlock - id блока
     * @return цвет строки
     */
    private static Color getColorById(int idBlock, int subId) {
        switch(idBlock) {
            case 0x21:
            case 0x61:
            case 0x91:
            case 0x79:
            case 0x40:
                switch (subId) {
                    case 0x01: return new Color(0xFFBD05);
                    case 0x02: return new Color(0xFFE575);
                    case 0x03: return new Color(0xFFD8AC);
                    case 0x04: return new Color(0xFFA7D1);
                    case 0x05: return new Color(0xFFB77A);
                    case 0x06: return new Color(0xFF880B);
                    case 0x07: return new Color(0xFF8257);
                    case 0x08: return new Color(0xFFA9A7);
                    case 0x09: return new Color(0xFF0019);
                    case 0x0A: return new Color(0xFF4780);
                    case 0x0B: return new Color(0xFF9EBD);
                    case 0x0C: return new Color(0xFA9EFF);
                    case 0x0D: return new Color(0xFFB2DB);
                    case 0x0E: return new Color(0xFF0B8B);
                    default:  return new Color(0xFFBD07);
                }
             //   break;
            // asim
            case 0xC0:
                switch (subId) {
                    case 0x00: return new Color(0xFFBD05);
                    case 0x01: return new Color(0xFFBDDD);
                    case 0x02: return new Color(0xF890B8);
                    case 0x03: return new Color(0x70CCFA);
                }
                break;
            case 0xC2:
                switch (subId) {
                    case 0x00: return new Color(0x8FFF05);
                    case 0x01: return new Color(0x6868FF);
                }
                break;
            case 0xC3: //return new Color(0xE109FF);
                switch (subId) {
                    case 0x01: return new Color(0xE109FF);
                    case 0x02: return new Color(0x03FF57);
                    case 0x03: return new Color(0xCEFF6B);
                    case 0x04: return new Color(0x65F6DB);
                }
            break;

            case 0xC4: return new Color(0xC468FF);
            case 0xC5:
                switch (subId) {
                    case 0x00: return new Color(0x15FA61);
                    case 0x01: return new Color(0xF85F5F);
                }
                break;
            case 0xC6: return new Color(0xFCEA79);
            case 0xC7: return new Color(0xB6FF68);
            //
            case 0x1D:
            case 0x2D:
            case 0x6D:
            case 0x9D:
            case 0x71:
            case 0x44:
                switch (subId) {
                    case 0x04: return new Color(0x17FF9D);
                    case 0x05: return new Color(0xFF8085);
                    case 0x08: return new Color(0xE109FF);
                    case 0x09: return new Color(0xEBFF6C);
                    case 0x0A: return new Color(0xA2FD6E);
                    case 0x0B: return new Color(0xC098FF);
                    case 0x0C: return new Color(0x988EFF);
                    case 0x0D: return new Color(0xD39CFF);
                    case 0x0E: return new Color(0x3B34FF);
                    case 0x0F: return new Color(0xA662FF);
                }
                break;
            case 0x70:
            case 0x20:
            case 0x60:
            case 0x90:
            case 0x10:
            case 0x50: return new Color(0x47E6D8);
            case 0x11:
            case 0x51: return new Color(0xA5D4E6);
            case 0x72:
            case 0x22:
            case 0x62:
            case 0x92:
            case 0x12:
            case 0x52: return new Color(0x04D8E6);
            case 0x73:
            case 0x23:
            case 0x63:
            case 0x93:
            case 0x13:
            case 0x53: return new Color(0x01E697);
            case 0x24:
            case 0x64:
            case 0x94:
            case 0x14:
            case 0x54: return new Color(0xA8E686);
            case 0x25:
            case 0x65:
            case 0x95:
            case 0x15:
            case 0x55: return new Color(0x53E669);
            case 0x76:
            case 0x26:
            case 0x66:
            case 0x96:
            case 0x16:
            case 0x56: return new Color(0x03FF32);
            case 0x27:
            case 0x67:
            case 0x97:
            case 0x17:
            case 0x57: return new Color(0xAFFFAC);
            case 0x28:
            case 0x68:
            case 0x98:
            case 0x18:
            case 0x58: return new Color(0xC2FF03);
            case 0x7A:
            case 0x29:
            case 0x69:
            case 0x99:
            case 0x19:
            case 0x59: return new Color(0x00EAFF);
            case 0x2A:
            case 0x6A:
            case 0x9A:
            case 0x5A: return new Color(0xFFD15C);
            case 0x7B:
            case 0x2B:
            case 0x6B:
            case 0x9B:
            case 0x5B: return new Color(0xF5FF97);
            case 0x2C:
            case 0x6C:
            case 0x9C:
            case 0x5C: return new Color(0xC2FF9F);
            case 0x5D: return new Color(0xFFD7A4);
            case 0x2E:
            case 0x6E:
            case 0x9E:
            case 0x5E: return new Color(0xFF876C);
            case 0x7F:
            case 0x2F:
            case 0x6F:
            case 0x9F:
            case 0x5F: return new Color(0xE4A257);
        }
        return new Color(0xA8A6A7);
    }

    private class TableModel extends AbstractTableModel {

        private static final int NUM_COLUMNS = 32;

        public int getRowCount() {
//            return arrBlock32.size();
        	return endBlk - startBlk + 1;
            
        }

        public int getColumnCount() {
            return NUM_COLUMNS;
        }

        public Object getValueAt(int i, int col) {
            final String TAG = "%02X";
            String subId = "";
        	i += startBlk;
        	curId = arrBlock32.get(i).getId();
        	if (arrBlock32.isG() && (curId == 0x21 || curId == 0x61)) {
                curSubId = Block32.getSubId_0x21(arrBlock32.get(i).getValues());
            } else if (arrBlock32.isG() && curId == 0x1D) {
                curSubId = Block32.getSubId_0x1D(arrBlock32.get(i).getValues());
            } else if (arrBlock32.isASIM() && (curId == 0xC0 || curId == 0xC2 || curId == 0xC3 || curId == 0xC4 || curId == 0xC5)) {
                curSubId = Block32.getSubId_ASIM(arrBlock32.get(i).getValues());
            } else if (arrBlock32.isP() && (curId == 0x2D || curId == 0x6D || curId == 0x9D || curId == 0x71 || curId == 0x44)) {
                curSubId = Block32.getSubId_0x1D(arrBlock32.get(i).getValues());
            } else curSubId = 0;
        	if (curSubId > 0) subId = String.format("_%X", curSubId);

            switch(col) {
                case COL_BL: return String.format("%,08d", i);                               // ord
                case COL_TYPE: return String.format("0x" + TAG, curId) + subId;    // id
                case COL_SECOND: return String.valueOf(arrBlock32.get(i).getSecond()); //String.format("%,08d", arrBlock32.get(i).getSeconds());
                case 3: return String.format(TAG, arrBlock32.get(i).get(0));
                case 4: return String.format(TAG, arrBlock32.get(i).get(1));
                case 5: return String.format(TAG, arrBlock32.get(i).get(2));
                case 6: return String.format(TAG, arrBlock32.get(i).get(3));
                case 7: return String.format(TAG, arrBlock32.get(i).get(4));
                case 8: return String.format(TAG, arrBlock32.get(i).get(5));
                case 9: return String.format(TAG, arrBlock32.get(i).get(6));
                case 10: return String.format(TAG, arrBlock32.get(i).get(7));
                case 11: return String.format(TAG, arrBlock32.get(i).get(8));
                case 12: return String.format(TAG, arrBlock32.get(i).get(9));
                case 13: return String.format(TAG, arrBlock32.get(i).get(10));
                case 14: return String.format(TAG, arrBlock32.get(i).get(11));
                case 15: return String.format(TAG, arrBlock32.get(i).get(12));
                case 16: return String.format(TAG, arrBlock32.get(i).get(13));
                case 17: return String.format(TAG, arrBlock32.get(i).get(14));
                case 18: return String.format(TAG, arrBlock32.get(i).get(15));
                case 19: return String.format(TAG, arrBlock32.get(i).get(16));
                case 20: return String.format(TAG, arrBlock32.get(i).get(17));
                case 21: return String.format(TAG, arrBlock32.get(i).get(18));
                case 22: return String.format(TAG, arrBlock32.get(i).get(19));
                case 23: return String.format(TAG, arrBlock32.get(i).get(20));
                case 24: return String.format(TAG, arrBlock32.get(i).get(21));
                case 25: return String.format(TAG, arrBlock32.get(i).get(22));
                case 26: return String.format(TAG, arrBlock32.get(i).get(23));
                case 27: return String.format(TAG, arrBlock32.get(i).get(24));
                case 28: return String.format(TAG, arrBlock32.get(i).get(25));
                case 29: return String.format(TAG, arrBlock32.get(i).get(26));
                case 30: return String.format(TAG, arrBlock32.get(i).get(27));
                case 31: return String.format(TAG, arrBlock32.get(i).get(28));
                default: return -1;
            }
        }
    }

    /**
     * @return текущая строка, (-1 не инициализирована)
     */
    public int getCurRow() {
        if (curRow == -1) curRow = getTable().getSelectedRow();
        return curRow;
    }

    /**
     * @return текущий блок, (-1 не инициализирована)
     */
    public int getCurBl() {
        return curBl;
    }

    /**
     * @return текущий тип, (-1 не инициализирована)
     */
    public int getCurType() {
        return curType;
    }

    /**
     * @return текущая секунда, (-1 не инициализирована)
     */
    public int getCurSecond() {
        return curSecond;
    }

    /**
     * установка курсора на строку
     * @param row - строка
     */
    public void selectRow(int row) {
        if (row >= 0 && row < table.getRowCount())
            table.changeSelection(row, 0, false, false);
    }

    private void setHotKeys() {
        InputMap inpMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actMap = this.getActionMap();
        inpMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "search_up");
        inpMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK), "search_down");

        actMap.put("search_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                selectSearchedType(true);
            }
        });

        actMap.put("search_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                selectSearchedType(false);
            }
        });
    }
}


