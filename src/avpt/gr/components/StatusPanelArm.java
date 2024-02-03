package avpt.gr.components;

import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartArm;
import avpt.gr.graph.ChartPanelArm;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static avpt.gr.common.UtilsArmG.statusFont;

public class StatusPanelArm extends JPanel {

    private ChartArm chartArm;
    private ChartDataset chartDataset;

    private JPopupMenu popupZoom = new JPopupMenu();
    private ButtonGroup buttonGroupZoom = new ButtonGroup();

    public StatusPanelArm(ChartPanelArm chartPanelArm) {
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        chartArm = chartPanelArm.getChartArm();
        chartDataset = chartArm.getChartDataset();
        add(makeLabelZoom());
        add(makeLabelFileName(chartPanelArm.getChartDataset().getArrBlock32().getFileName()));
        add(makeLabelTime());
        add(makeLabelRailCoordinate());
        add(makeLabelLatLon());
        add(makeEmptyLabel());
    }

    private ActionListener actionListenerZoom;

    private class ItemActionListenerZoom implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (actionListenerZoom != null)
                actionListenerZoom.actionPerformed(
                        new ActionEvent(e.getSource(), e.getID(), e.getActionCommand()));
        }
    }

    public void addActionListenerZoom(ActionListener actionListener) {
        this.actionListenerZoom = actionListener;
    }

    /**
     * @param item - item
     * @param name - строковое значение duration (разница между upperBound и lowerBound)
     *             по времени сек:
     *             150-     1:0.1;
     *             750-     1:0.5
     *             1500-    1:1;
     *             3000-    1:2;
     *             7500-    1:5;
     *             15000-   1:10;
     *             по координате м:
     *             1500-    1:1;
     *             3000-    1:2;
     *             7500-    1:5;
     *             15000-   1:10;
     *             30000-   1:20;
     *             75000-   1:50;
     *             150000-  1:100
     */
    private void initItem(JMenuItem item, String name) {
        buttonGroupZoom.add(item);
        item.addActionListener(new ItemActionListenerZoom());
        item.setName(name);
        popupZoom.add(item);
        int duration = Integer.parseInt(name);
        int boundUpper = (int)chartArm.getBoundUpper().get();
        if (duration == boundUpper)
            item.setSelected(true);
    }

    /**
     * @param isTime - развертка по времени или координате (true/false)
     * @return JPopupMenu
     */
    private JPopupMenu createPopupZoom(boolean isTime) {
        if (isTime) {
            JMenuItem ten = new JMenuItem("1:10 сек");
            initItem(ten, "15000");
            JMenuItem five = new JMenuItem("1:5 сек");
            initItem(five, "7500");
            JMenuItem two = new JMenuItem("1:2 сек");
            initItem(two, "3000");
            JMenuItem one = new JMenuItem("1:1 сек");
            initItem(one, "1500");
            JMenuItem five_tenth = new JMenuItem("1:0.5 сек");
            initItem(five_tenth, "750");
            JMenuItem one_tenth = new JMenuItem("1:0.1 сек");
            initItem(one_tenth, "150");
        }
        else {
            JMenuItem hundred = new JMenuItem("1:100 м");
            initItem(hundred, "150000");
            JMenuItem fifty = new JMenuItem("1:50 м");
            initItem(fifty, "75000");
            JMenuItem twenty = new JMenuItem("1:20 м");
            initItem(twenty, "30000");
            JMenuItem ten = new JMenuItem("1:10 м");
            initItem(ten, "15000");
            JMenuItem five = new JMenuItem("1:5 м");
            initItem(five, "7500");
            JMenuItem two = new JMenuItem("1:2 м");
            initItem(two, "3000");
            JMenuItem one = new JMenuItem("1:1 м");
            initItem(one, "1500");
        }
        return popupZoom;
    }

    private JLabel makeLabelZoom() {
        JLabel label = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double duration = chartArm != null ? chartArm.getBoundUpper().get() : 0;
                String txt = String.format("Масштаб: 1:%.2f %s", UtilsArmG.round(duration / 1500, 2),
                        chartDataset.isTime() ? "сек" : "м");
                setText(txt);
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
        label.setComponentPopupMenu(createPopupZoom(chartDataset.isTime()));
        return label;
    }

    /**
     * @return JLabel с именем файла поездки
     */
    private JLabel makeLabelFileName(String fileName) {
        JLabel label = new JLabel("Файл: " + UtilsArmG.getShorterNameFile(fileName) + " ");
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
        return label;
    }

    /**
     * @return JLabel для даты-времени на текущее положение курсора
     */
    private JLabel makeLabelTime() {
        JLabel label = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double curX;
                if (chartArm != null)
                    curX = chartArm.getXMarker();
                else
                    curX = 0;
                String date_txt = chartDataset.getDateText((int)curX);
                String time_txt = chartDataset.getTimeText((int)curX);
                setText(date_txt + "; " + time_txt + " ");
                //this.repaint();
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
        return label;
    }

    /**
     * @return JLabel для широты-долготы на текущее положение курсора
     */
    private JLabel makeLabelLatLon() {
        JLabel label = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double curX;
                if (chartArm != null)
                    curX = chartArm.getXMarker();
                else
                    curX = 0;
                String lat_txt = chartDataset.getLatitudeText((int)curX);
                String lon_txt = chartDataset.getLongitudeText((int)curX);
                setText(lat_txt + " " + lon_txt);
              //  this.repaint();
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
        return label;
    }

    private JLabel makeLabelRailCoordinate() {
        JLabel label = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double curX;
                if (chartArm != null)
                    curX = chartArm.getXMarker();
                else
                    curX = 0;
                String coordinate_txt = chartDataset.getRailCoordinateText((int)curX);
                setText(coordinate_txt);
              //  this.repaint();
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
        return label;
    }

    /**
     * @return пустой JLabel
     */
    private JLabel makeEmptyLabel() {
        int w = 10000;
        int h = 20;
        JLabel label = new JLabel("");
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setMinimumSize(new Dimension(w, h));
        label.setPreferredSize(new Dimension(w, h));
        label.setMaximumSize(new Dimension(w, h));
        return label;
    }
}
