package avpt.gr.components;

import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartArm;
import avpt.gr.graph.ChartPanelArm;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

import static avpt.gr.common.UtilsArmG.statusFont;

public class StatusPanelArm extends JPanel {

    ChartArm chartArm;
    ChartDataset chartDataset;

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

    private JLabel makeLabelZoom() {
        JLabel label = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double duration = chartArm != null ? chartArm.getBoundUpper().get() : 0;
                String txt = String.format("Масштаб: 1:%.2f %s", UtilsArmG.round(duration / 1500, 2),
                        chartDataset.isTime() ? "сек" : "м");
                setText(txt);
              //  this.repaint();
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setFont(statusFont);
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
