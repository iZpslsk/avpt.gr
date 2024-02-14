package avpt.gr.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CheckPopup extends JPopupMenu {

    /**
     * Icon - прорисовка прямоугольника соответстующего цвета
     */
    private static class RectangleIcon implements Icon {

        private int width;
        private int height;
        private Paint paint;

        public RectangleIcon(Paint paint, int width, int height) {
            this.width = width;
            this.height = height;
            this.paint = paint;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(paint);
            g2.fill(new Rectangle2D.Double(x, y, width, height));
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }

    /**
     * пункт мнею
     */
    private static class Item extends JCheckBoxMenuItem {

        public Item(Paint paint, int width, int height, String text) {
            super(text, new RectangleIcon(paint, width, height));
            // запрет скрытия окна при щелчке мыши
            setUI(new BasicCheckBoxMenuItemUI() {
                @Override
                protected void doClick(MenuSelectionManager msm) {
                    menuItem.doClick(0);
                }
            });
        }
    }

    /**
     * добавляем пункт меню
     * @param paint - цвет прямоугольника
     * @param width - ширина прямоугольника
     * @param height - высота прямоугольника
     * @param text - текст
     */
    public JMenuItem add(Paint paint, int width, int height, String text) {
        return super.add(new Item(paint, width, height, text));
    }
}
