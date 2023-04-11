package avpt.gr.components.hex_tab;

import avpt.gr.blocks32.ArrBlock32;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * класс - меню выбора типа посылки для поиска ближайшей посылки аналогичного типа
 */
class SearchMenu extends JPopupMenu {

    static class Item extends JRadioButtonMenuItem {

        private final int id;
        private final int subId;

        /**
         * @param text - текст
         * @param id - id блока
         * @param subId - subId блока
         */
        Item(String text, int id, int subId) {
            super(text);
            this.id = id;
            this.subId = subId;
        }

        int getId() {
            return id;
        }

        int getSubId() {
            return subId;
        }
    }

    private ActionListener actionListener;

    private final static String temp = "<html>%s  <font color=\"#5F9EA0\"size=\"2\"> F3</font>" +
            "↓<font color=\"#5F9EA0\" size=\"2\"> +Shift</font>↑</html>";

    private final Item item_0X10 = new Item(String.format(temp, "0x10"),0x10, 0x00);
    private final Item item_0X20 = new Item(String.format(temp, "0x20"),0x20, 0x00);
    private final Item item_0X40 = new Item(String.format(temp, "0x40"),0x40, 0x00);
    private final Item item_0X50 = new Item(String.format(temp, "0x50"),0x50, 0x00);
    private final Item item_0X60 = new Item(String.format(temp, "0x60"),0x60, 0x00);
    private final Item item_0X70 = new Item(String.format(temp, "0x70"),0x70, 0x00);
    private final Item item_0X90 = new Item(String.format(temp, "0x90"),0x90, 0x00);

    private final Item item_0X11 = new Item(String.format(temp, "0x11"),0x11, 0x00);
    private final Item item_0X21 = new Item(String.format(temp, "0x21"),0x21, 0x00);
    private final Item item_0X51 = new Item(String.format(temp, "0x51"),0x51, 0x00);
    private final Item item_0X61 = new Item(String.format(temp, "0x61"),0x61, 0x00);
    private final Item item_0X91 = new Item(String.format(temp, "0x91"),0x91, 0x00);

    private final Item item_0X12 = new Item(String.format(temp, "0x12"),0x12, 0x00);
    private final Item item_0X22 = new Item(String.format(temp, "0x22"),0x22, 0x00);
    private final Item item_0X52 = new Item(String.format(temp, "0x52"),0x52, 0x00);
    private final Item item_0X62 = new Item(String.format(temp, "0x62"),0x62, 0x00);
    private final Item item_0X72 = new Item(String.format(temp, "0x72"),0x72, 0x00);
    private final Item item_0X92 = new Item(String.format(temp, "0x92"),0x92, 0x00);

    private final Item item_0X13 = new Item(String.format(temp, "0x13"),0x13, 0x00);
    private final Item item_0X23 = new Item(String.format(temp, "0x23"),0x23, 0x00);
    private final Item item_0X53 = new Item(String.format(temp, "0x53"),0x53, 0x00);
    private final Item item_0X63 = new Item(String.format(temp, "0x63"),0x63, 0x00);
    private final Item item_0X73 = new Item(String.format(temp, "0x73"),0x73, 0x00);
    private final Item item_0X93 = new Item(String.format(temp, "0x93"),0x93, 0x00);
    private final Item item_0XC3 = new Item(String.format(temp, "0xC3"),0xC3, 0x00);

    private final Item item_0X14 = new Item(String.format(temp, "0x14"),0x14, 0x00);
    private final Item item_0X24 = new Item(String.format(temp, "0x24"),0x24, 0x00);
    private final Item item_0X54 = new Item(String.format(temp, "0x54"),0x54, 0x00);
    private final Item item_0X64 = new Item(String.format(temp, "0x64"),0x64, 0x00);
    private final Item item_0X74 = new Item(String.format(temp, "0x74"),0x74, 0x00);
    private final Item item_0X94 = new Item(String.format(temp, "0x94"),0x94, 0x00);
    private final Item item_0XC4 = new Item(String.format(temp, "0xC4"),0xC4, 0x00);

    private final Item item_0X15 = new Item(String.format(temp, "0x15"),0x15, 0x00);
    private final Item item_0X25 = new Item(String.format(temp, "0x25"),0x25, 0x00);
    private final Item item_0X55 = new Item(String.format(temp, "0x55"),0x55, 0x00);
    private final Item item_0X65 = new Item(String.format(temp, "0x65"),0x65, 0x00);
    private final Item item_0X75 = new Item(String.format(temp, "0x75"),0x75, 0x00);
    private final Item item_0X95 = new Item(String.format(temp, "0x95"),0x95, 0x00);

    private final Item item_0X16 = new Item(String.format(temp, "0x16"),0x16, 0x00);
    private final Item item_0X26 = new Item(String.format(temp, "0x26"),0x26, 0x00);
    private final Item item_0X56 = new Item(String.format(temp, "0x56"),0x56, 0x00);
    private final Item item_0X66 = new Item(String.format(temp, "0x66"),0x66, 0x00);
    private final Item item_0X76 = new Item(String.format(temp, "0x76"),0x76, 0x00);
    private final Item item_0X96 = new Item(String.format(temp, "0x96"),0x96, 0x00);

    private final Item item_0X57 = new Item(String.format(temp, "0x57"),0x57, 0x00);
    private final Item item_0X77 = new Item(String.format(temp, "0x77"),0x77, 0x00);
    private final Item item_0X97 = new Item(String.format(temp, "0x97"),0x97, 0x00);
    private final Item item_0XC7 = new Item(String.format(temp, "0xC7"),0xC7, 0x00);

    private final Item item_0X28 = new Item(String.format(temp, "0x28"),0x28, 0x00);
    private final Item item_0X58 = new Item(String.format(temp, "0x58"),0x58, 0x00);
    private final Item item_0X68 = new Item(String.format(temp, "0x68"),0x68, 0x00);
    private final Item item_0X78 = new Item(String.format(temp, "0x78"),0x78, 0x00);
    private final Item item_0X98 = new Item(String.format(temp, "0x98"),0x98, 0x00);

    private final Item item_0X29 = new Item(String.format(temp, "0x29"),0x29, 0x00);
    private final Item item_0X59 = new Item(String.format(temp, "0x59"),0x59, 0x00);
    private final Item item_0X69 = new Item(String.format(temp, "0x69"),0x69, 0x00);
    private final Item item_0X79 = new Item(String.format(temp, "0x79"),0x79, 0x00);
    private final Item item_0X99 = new Item(String.format(temp, "0x99"),0x99, 0x00);

    private final Item item_0X2A = new Item(String.format(temp, "0x2A"),0x2A, 0x00);
    private final Item item_0X5A = new Item(String.format(temp, "0x5A"),0x5A, 0x00);
    private final Item item_0X6A = new Item(String.format(temp, "0x6A"),0x6A, 0x00);
    private final Item item_0X7A = new Item(String.format(temp, "0x7A"),0x7A, 0x00);
    private final Item item_0X9A = new Item(String.format(temp, "0x9A"),0x9A, 0x00);
    private final Item item_0XAA = new Item(String.format(temp, "0xAA"),0xAA, 0x00);
    private final Item item_0XEA = new Item(String.format(temp, "0xEA"),0xEA, 0x00);

    private final Item item_0X2B = new Item(String.format(temp, "0x2B"),0x2B, 0x00);
    private final Item item_0X5B = new Item(String.format(temp, "0x5B"),0x5B, 0x00);
    private final Item item_0X7B = new Item(String.format(temp, "0x7B"),0x7B, 0x00);
    private final Item item_0X9B = new Item(String.format(temp, "0x9B"),0x9B, 0x00);

    private final Item item_0X2C = new Item(String.format(temp, "0x2C"),0x2C, 0x00);
    private final Item item_0X5C = new Item(String.format(temp, "0x5C"),0x5C, 0x00);
    private final Item item_0X6C = new Item(String.format(temp, "0x6C"),0x6C, 0x00);
    private final Item item_0X7C = new Item(String.format(temp, "0x7C"),0x7C, 0x00);
    private final Item item_0X9C = new Item(String.format(temp, "0x9C"),0x9C, 0x00);

    private final Item item_0X5D = new Item(String.format(temp, "0x5D"),0x5D, 0x00);
    private final Item item_0X7D = new Item(String.format(temp, "0x7D"),0x7D, 0x00);

    private final Item item_0X2E = new Item(String.format(temp, "0x2E"),0x2E, 0x00);
    private final Item item_0X5E = new Item(String.format(temp, "0x5E"),0x5E, 0x00);
    private final Item item_0X7E = new Item(String.format(temp, "0x7E"),0x7E, 0x00);

    private final Item item_0X2F = new Item(String.format(temp, "0x2F"),0x2F, 0x00);
    private final Item item_0X5F = new Item(String.format(temp, "0x5F"),0x5F, 0x00);
    private final Item item_0X6F = new Item(String.format(temp, "0x6F"),0x6F, 0x00);
    private final Item item_0X7F = new Item(String.format(temp, "0x7F"),0x7F, 0x00);
    private final Item item_0X9F = new Item(String.format(temp, "0x9F"),0x9F, 0x00);


    // submenu -------------------------------------------------------------------------------------

    private final JMenu menu_0x4D = new JMenu("0x4D");
    private final Item item_0X4D_sub_0X40 = new Item(String.format(temp, "0x40"),0x4D, 0x40);
    private final Item item_0X4D_sub_0X80 = new Item(String.format(temp, "0x80"),0x4D, 0x80);
    private final Item item_0X4D_sub_0XC0 = new Item(String.format(temp, "0xC0"),0x4D, 0xC0);
    private final JMenu menu_0x4E = new JMenu("0x4E");
    private final Item item_0X4E_sub_0X40 = new Item(String.format(temp, "0x40"),0x4E, 0x40);
    private final Item item_0X4E_sub_0X80 = new Item(String.format(temp, "0x80"),0x4E, 0x80);
    private final Item item_0X4E_sub_0XC0 = new Item(String.format(temp, "0xC0"),0x4E, 0xC0);
    // грузовой
    private final JMenu menu_0x1D = new JMenu("0x1D");
    private final Item item_0X1D_sub_0X04 = new Item(String.format(temp, "0x04"),0x1D, 0x04);
    private final Item item_0X1D_sub_0X05 = new Item(String.format(temp, "0x05"),0x1D, 0x05);
    private final Item item_0X1D_sub_0X08 = new Item(String.format(temp, "0x08"),0x1D, 0x08);
    private final Item item_0X1D_sub_0X09 = new Item(String.format(temp, "0x09"),0x1D, 0x09);
    private final Item item_0X1D_sub_0X0A = new Item(String.format(temp, "0x0A"),0x1D, 0x0A);
    private final Item item_0X1D_sub_0X0B = new Item(String.format(temp, "0x0B"),0x1D, 0x0B);
    private final Item item_0X1D_sub_0X0C = new Item(String.format(temp, "0x0C"),0x1D, 0x0C);
    private final Item item_0X1D_sub_0X0D = new Item(String.format(temp, "0x0D"),0x1D, 0x0D);
    private final Item item_0X1D_sub_0X0E = new Item(String.format(temp, "0x0E"),0x1D, 0x0E);
    // пассажирский
    private final JMenu menu_0x71 = new JMenu("0x71");
    private final Item item_0X71_sub_0X04 = new Item(String.format(temp, "0x04"),0x71, 0x04);
    private final Item item_0X71_sub_0X09 = new Item(String.format(temp, "0x09"),0x71, 0x09);
    private final Item item_0X71_sub_0X0A = new Item(String.format(temp, "0x0A"),0x71, 0x0A);
    private final JMenu menu_0x44 = new JMenu("0x44");
    private final Item item_0X44_sub_0X04 = new Item(String.format(temp, "0x04"),0x44, 0x04);
    private final Item item_0X44_sub_0X09 = new Item(String.format(temp, "0x09"),0x44, 0x09);
    private final Item item_0X44_sub_0X0A = new Item(String.format(temp, "0x0A"),0x44, 0x0A);
    private final JMenu menu_0x2D = new JMenu("0x2D");
    private final Item item_0X2D_sub_0X04 = new Item(String.format(temp, "0x04"),0x2D, 0x04);
    private final Item item_0X2D_sub_0X09 = new Item(String.format(temp, "0x09"),0x2D, 0x09);
    private final Item item_0X2D_sub_0X0A = new Item(String.format(temp, "0x0A"),0x2D, 0x0A);
    private final JMenu menu_0x6D = new JMenu("0x6D");
    private final Item item_0X6D_sub_0X04 = new Item(String.format(temp, "0x04"),0x6D, 0x04);
    private final Item item_0X6D_sub_0X09 = new Item(String.format(temp, "0x09"),0x6D, 0x09);
    private final Item item_0X6D_sub_0X0A = new Item(String.format(temp, "0x0A"),0x6D, 0x0A);
    private final JMenu menu_0x9D = new JMenu("0x9D");
    private final Item item_0X9D_sub_0X04 = new Item(String.format(temp, "0x04"),0x9D, 0x04);
    private final Item item_0X9D_sub_0X09 = new Item(String.format(temp, "0x09"),0x9D, 0x09);
    private final Item item_0X9D_sub_0X0A = new Item(String.format(temp, "0x0A"),0x9D, 0x0A);
    // грузовой
    private final JMenu menu_0x21 = new JMenu("0x21");
    private final Item item_0X21_sub_0X01 = new Item(String.format(temp, "0x01"),0x21, 0x01);
    private final Item item_0X21_sub_0X02 = new Item(String.format(temp, "0x02"),0x21, 0x02);
    private final Item item_0X21_sub_0X03 = new Item(String.format(temp, "0x03"),0x21, 0x03);
    private final Item item_0X21_sub_0X04 = new Item(String.format(temp, "0x04"),0x21, 0x04);
    private final Item item_0X21_sub_0X06 = new Item(String.format(temp, "0x06"),0x21, 0x06);
    private final Item item_0X21_sub_0X07 = new Item(String.format(temp, "0x07"),0x21, 0x07);
    private final Item item_0X21_sub_0X08 = new Item(String.format(temp, "0x08"),0x21, 0x08);
    private final Item item_0X21_sub_0X09 = new Item(String.format(temp, "0x09"),0x21, 0x09);
    private final Item item_0X21_sub_0X0A = new Item(String.format(temp, "0x0A"),0x21, 0x0A);
    private final Item item_0X21_sub_0X0B = new Item(String.format(temp, "0x0B"),0x21, 0x0B);
    private final Item item_0X21_sub_0X0E = new Item(String.format(temp, "0x0E"),0x21, 0x0E);
    // асим
    private final JMenu menu_0xC0 = new JMenu("0xС0");
    private final Item item_0XC0_sub_0X00 = new Item(String.format(temp, "0x00"),0xC0, 0x00);
    private final Item item_0XC0_sub_0X01 = new Item(String.format(temp, "0x01"),0xC0, 0x01);
    private final Item item_0XC0_sub_0X02 = new Item(String.format(temp, "0x02"),0xC0, 0x02);
    private final JMenu menu_0xC2 = new JMenu("0xС2");
    private final Item item_0XC2_sub_0X00 = new Item(String.format(temp, "0x00"),0xC2, 0x00);
    private final Item item_0XC2_sub_0X01 = new Item(String.format(temp, "0x01"),0xC2, 0x01);
    private final JMenu menu_0xC5 = new JMenu("0xС5");
    private final Item item_0XC5_sub_0X00 = new Item(String.format(temp, "0x00"),0xC5, 0x00);
    private final Item item_0XC5_sub_0X01 = new Item(String.format(temp, "0x01"),0xC5, 0x01);

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JMenu menu = new JMenu("Поиск по id");
    private final ArrBlock32 blocks;

    /**
     * создание меню поиска
     * @param blocks - массив блоков
     */
    SearchMenu(ArrBlock32 blocks) {
        this.blocks = blocks;
        initItems();
        add(menu);
    }

    private void initItem(Item item) {
        if (blocks.isNotExistsIdBl(item.getId(), 0)) return;
        buttonGroup.add(item);
        item.addActionListener(new ItemActionListener());
        menu.add(item);
    }

    private void initItem(JMenu menu, Item item) {
        if (blocks.isNotExistsIdBl(item.getId(), item.getSubId())) return;
        buttonGroup.add(item);
        menu.add(item);
        item.addActionListener(new ItemActionListener());
        this.menu.add(menu);
    }

    private void initItems() {
        initItem(item_0X10);
        initItem(item_0X40);
        initItem(item_0X20);
        initItem(item_0X50);
        initItem(item_0X60);
        initItem(item_0X70);
        initItem(item_0X90);

        initItem(item_0X11);
        initItem(item_0X21);
        initItem(item_0X51);
        initItem(item_0X61);
        initItem(item_0X91);

        initItem(item_0X12);
        initItem(item_0X22);
        initItem(item_0X52);
        initItem(item_0X62);
        initItem(item_0X72);
        initItem(item_0X92);

        initItem(item_0X13);
        initItem(item_0X23);
        initItem(item_0X63);
        initItem(item_0X53);
        initItem(item_0X73);
        initItem(item_0X93);
        initItem(item_0XC3);

        initItem(item_0X14);
        initItem(item_0X24);
        initItem(item_0X54);
        initItem(item_0X64);
        initItem(item_0X74);
        initItem(item_0X94);
        initItem(item_0XC4);

        initItem(item_0X15);
        initItem(item_0X25);
        initItem(item_0X55);
        initItem(item_0X65);
        initItem(item_0X75);
        initItem(item_0X95);

        initItem(item_0X16);
        initItem(item_0X26);
        initItem(item_0X56);
        initItem(item_0X66);
        initItem(item_0X76);
        initItem(item_0X96);

        initItem(item_0X57);
        initItem(item_0X77);
        initItem(item_0X97);
        initItem(item_0XC7);

        initItem(item_0X28);
        initItem(item_0X58);
        initItem(item_0X68);
        initItem(item_0X78);
        initItem(item_0X98);

        initItem(item_0X29);
        initItem(item_0X59);
        initItem(item_0X69);
        initItem(item_0X79);
        initItem(item_0X99);

        initItem(item_0X5A);
        initItem(item_0X2A);
        initItem(item_0X6A);
        initItem(item_0X7A);
        initItem(item_0X9A);
        initItem(item_0XAA);
        initItem(item_0XEA);

        initItem(item_0X2B);
        initItem(item_0X5B);
        initItem(item_0X7B);
        initItem(item_0X9B);

        initItem(item_0X2C);
        initItem(item_0X5C);
        initItem(item_0X6C);
        initItem(item_0X7C);
        initItem(item_0X9C);

        initItem(item_0X5D);
        initItem(item_0X7D);

        initItem(item_0X2E);
        initItem(item_0X5E);
        initItem(item_0X7E);

        initItem(item_0X5F);
        initItem(item_0X2F);
        initItem(item_0X6F);
        initItem(item_0X7F);
        initItem(item_0X9F);


        // submenu

        initItem(menu_0x4D, item_0X4D_sub_0X80);
        initItem(menu_0x4D, item_0X4D_sub_0X40);
        initItem(menu_0x4D, item_0X4D_sub_0XC0);

        initItem(menu_0x4E, item_0X4E_sub_0X80);
        initItem(menu_0x4E, item_0X4E_sub_0X40);
        initItem(menu_0x4E, item_0X4E_sub_0XC0);

        // грузовой
        initItem(menu_0x1D, item_0X1D_sub_0X04);
        initItem(menu_0x1D, item_0X1D_sub_0X05);
        initItem(menu_0x1D, item_0X1D_sub_0X08);
        initItem(menu_0x1D, item_0X1D_sub_0X09);
        initItem(menu_0x1D, item_0X1D_sub_0X0A);
        initItem(menu_0x1D, item_0X1D_sub_0X0B);
        initItem(menu_0x1D, item_0X1D_sub_0X0C);
        initItem(menu_0x1D, item_0X1D_sub_0X0D);
        initItem(menu_0x1D, item_0X1D_sub_0X0E);

        // пассажирский
        initItem(menu_0x71, item_0X71_sub_0X09);
        initItem(menu_0x71, item_0X71_sub_0X0A);
        initItem(menu_0x71, item_0X71_sub_0X04);

        initItem(menu_0x44, item_0X44_sub_0X09);
        initItem(menu_0x44, item_0X44_sub_0X0A);
        initItem(menu_0x44, item_0X44_sub_0X04);

        initItem(menu_0x2D, item_0X2D_sub_0X09);
        initItem(menu_0x2D, item_0X2D_sub_0X0A);
        initItem(menu_0x2D, item_0X2D_sub_0X04);

        initItem(menu_0x6D, item_0X6D_sub_0X09);
        initItem(menu_0x6D, item_0X6D_sub_0X0A);
        initItem(menu_0x6D, item_0X6D_sub_0X04);

        initItem(menu_0x9D, item_0X9D_sub_0X09);
        initItem(menu_0x9D, item_0X9D_sub_0X0A);
        initItem(menu_0x9D, item_0X9D_sub_0X04);

        // грузовой
        initItem(menu_0x21, item_0X21_sub_0X01);
        initItem(menu_0x21, item_0X21_sub_0X02);
        initItem(menu_0x21, item_0X21_sub_0X03);
        initItem(menu_0x21, item_0X21_sub_0X04);
        initItem(menu_0x21, item_0X21_sub_0X06);
        initItem(menu_0x21, item_0X21_sub_0X07);
        initItem(menu_0x21, item_0X21_sub_0X08);
        initItem(menu_0x21, item_0X21_sub_0X09);
        initItem(menu_0x21, item_0X21_sub_0X0A);
        initItem(menu_0x21, item_0X21_sub_0X0B);
        initItem(menu_0x21, item_0X21_sub_0X0E);

        // асим
        initItem(menu_0xC0, item_0XC0_sub_0X00);
        initItem(menu_0xC0, item_0XC0_sub_0X01);
        initItem(menu_0xC0, item_0XC0_sub_0X02);

        initItem(menu_0xC2, item_0XC2_sub_0X00);
        initItem(menu_0xC2, item_0XC2_sub_0X01);

        initItem(menu_0xC5, item_0XC5_sub_0X00);
        initItem(menu_0xC5, item_0XC5_sub_0X01);
    }

    private class ItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (actionListener != null)
                actionListener.actionPerformed(
                        new ActionEvent(e.getSource(), e.getID(), e.getActionCommand()));
        }
    }

    void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
