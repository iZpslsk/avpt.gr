package avpt.gr.graph;

import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import avpt.gr.components.InfoPanel;
import avpt.gr.components.ScrollBarArm;
import avpt.gr.components.StatusPanelArm;
import avpt.gr.components.hex_tab.HexTab;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static avpt.gr.common.UtilsArmG.*;
import static avpt.gr.common.WeightBlocks.*;

/**
 * панель - контейнер с расположением визуальных компонентов:
 * splitPanVertical - содержит: hexTablePan - верхняя часть, chartPanel - нижняя часть
 * splitPanHorisontal - содержит: splitPanVertical (hexTablePan, chartPanel) - левая часть, new JScrollPane(infoPanel) - правая часть
 * внизу панель с компонентами: ScrollBarArm и  StatusPanelArm
 */
public class ChartPanelArm extends JPanel {
    private final JSplitPane splitPanVertical;
    private final ChartArm chartArm;
    private final ChartPanel chartPanel;
    private final ScrollBarArm scrollBar;
    private final HexTab hexTab;
    private final ChartDataset chartDataset;
    private final InfoPanel infoPanel;
    private final UtilsArmG.MutableDouble boundUpper; // тип для передаче по ссыл

    private ChartPanelArm chartPanelArmMain; // если второе окно - chartPanelArmMain != null;
    private ChartPanelArm chartPanelArmSlave; // если первое окно и есть второе

    // шрифты
//    public static Font commonFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont); // дата, время, широта, долгота
//    public static Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, sizeFont); // шрифт для заголовка
//    public static Font descriptFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont); // шрифт для значений - crosshair
//    public static Font signalFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont); // // шрифт для значений дискретных сигналов - crosshair
//    public static Font trainFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont - 1); // шрифт для значений - crosshair
//    public static Font statusFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
//    public static Font hexTableFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
//    public static Font labelYAxisFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
//    public static Font tickFont = new Font(Font.MONOSPACED, Font.ITALIC, sizeFont);
//    public static Font trMarkerFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
//    public static Font profNameFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
//    public static Font stationNameFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
//    public static Font menuFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);

    public ChartPanelArm(final ChartDataset chartDataset, final JPanel hexTab) {
        super(new BorderLayout());
        UIManager.put("Menu.font", menuFont);
        UIManager.put("MenuItem.font", menuFont);
        UIManager.put("RadioButtonMenuItem.font", menuFont);
        this.chartDataset = chartDataset;
        this.hexTab = (HexTab) hexTab;
//        JTable table = hexTablePan.getTable();
//        table.setFocusable(false);
//        if (table.getRowCount() > 0) hexTablePan.selectRow(0);
        boundUpper = new UtilsArmG.MutableDouble();
        chartArm = new ChartArm(chartDataset, this.hexTab, boundUpper);
        scrollBar = new ScrollBarArm(chartArm);
        infoPanel = new InfoPanel(scrollBar, chartArm);
        StatusPanelArm statusPanel = new StatusPanelArm(this);
        JPanel southPanel = makeSouthPanel(statusPanel, scrollBar);
        add(southPanel, BorderLayout.SOUTH);
        chartPanel = new ChartPanelInheritor(chartArm, scrollBar);
        chartPanel.setMaximumDrawWidth(10000);
        chartPanel.setMinimumDrawWidth(10);
        chartPanel.setMaximumDrawHeight(5000);
        chartPanel.setMinimumDrawHeight(50);
//        chartPanel.addMouseWheelListener(new MouseWheelListener() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                final int rotation = e.getWheelRotation();
//                if (rotation == 0) return;
//                double d = ((ChartPanelInheritor)chartPanel).doZoomByCrosshair(new Point(e.getX(), e.getY()), rotation);
//            }
//        });
        splitPanVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hexTab, chartPanel);
        // отключаем панель с таблиицей при getDividerLocation() < 30 для обеспечения возможности закрытия сплитера
        splitPanVertical.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                hexTab.setVisible(splitPanVertical.getDividerLocation() > 30);
            }
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (splitPanVertical.getDividerLocation() > 30)
//                    hexTablePan.setVisible(true);
//                else
//                    hexTablePan.setVisible(false);
//            }
        });
        JSplitPane splitPanHorisontal = makeSplitPanHorisontal(splitPanVertical, infoPanel);
        add(splitPanHorisontal, BorderLayout.CENTER);
        setKeyEventMoveCursor();

        // событие смены выделенной строки таблицы
        (this.hexTab).addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setMarkerByRowTab();
            }
        });

//        hexTablePan.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent event) {
//                try {
//                    hexTablePan.doChange();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                // установка вертикального курсора в соответствии с таблицей hexTab
//                setMarkerByRowTab();
//            }
//        });
       // chartArm.doZoom(1);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int height = e.getComponent().getHeight();
                setWeightResize(height);
                setLabelResize(height);
//                if (chartPanel != null) {
//                    chartPanel.setMaximumDrawHeight(e.getComponent().getHeight());
//                    chartPanel.setMaximumDrawWidth(e.getComponent().getWidth());
//                    chartPanel.setMinimumDrawWidth(e.getComponent().getWidth());
//                    chartPanel.setMinimumDrawHeight(e.getComponent().getHeight());
//                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }

    private void setSizeAngle(XYPlot plot, double size, double angle) {
        ValueAxis axis = plot.getRangeAxis();
        axis.setLabelAngle(angle);
        axis.setLabelFont(labelYAxisFont.deriveFont((float) size));
    }

    private void setSizeAngleLabel(double size, double angle) {
        if (chartArm.getPlotTrain() != null) chartArm.getPlotTrain().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotVoltage_cs() != null) setSizeAngle(chartArm.getPlotVoltage_cs(), size, angle);
        if (chartArm.getPlotVoltage() != null)  setSizeAngle(chartArm.getPlotVoltage(), size, angle);
        if (chartArm.getPlotVoltage2() != null)  setSizeAngle(chartArm.getPlotVoltage2(), size, angle);
        if (chartArm.getPlotVoltage3() != null)  setSizeAngle(chartArm.getPlotVoltage3(), size, angle);
        if (chartArm.getPlotVoltage4() != null)  setSizeAngle(chartArm.getPlotVoltage4(), size, angle);
        if (chartArm.getPlotAmperage_common() != null) setSizeAngle(chartArm.getPlotAmperage_common(), size, angle);
        if (chartArm.getPlotAmperage_anchor() != null) setSizeAngle(chartArm.getPlotAmperage_anchor(), size, angle);
        if (chartArm.getPlotAmperage_excitation() != null) setSizeAngle(chartArm.getPlotAmperage_excitation(), size, angle);
        if (chartArm.getPlotAmperage3() != null) setSizeAngle(chartArm.getPlotAmperage3(), size, angle);
        if (chartArm.getPlotAmperage4() != null) setSizeAngle(chartArm.getPlotAmperage4(), size, angle);
        if (chartArm.getPlotConsumption() != null) setSizeAngle(chartArm.getPlotConsumption(), size, angle);
        if (chartArm.getPlotPower() != null) setSizeAngle(chartArm.getPlotPower(), size, angle);
        if (chartArm.getPlotTail() != null) setSizeAngle(chartArm.getPlotTail(), size, angle);
        if (chartArm.getPlotPress() != null) setSizeAngle(chartArm.getPlotPress(), size, angle);
        if (chartArm.getPlotSpeed() != null) setSizeAngle(chartArm.getPlotSpeed(), size, angle);

        if (chartArm.getPlotMap() != null) chartArm.getPlotMap().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotProfile() != null) chartArm.getPlotProfile().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotPosition() != null) chartArm.getPlotPosition().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAlsn() != null) chartArm.getPlotAlsn().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAlsn_club() != null) chartArm.getPlotAlsn_club().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAlsn_br() != null) chartArm.getPlotAlsn_br().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAlsn_br_vl80() != null) chartArm.getPlotAlsn_br_vl80().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAuto() != null) chartArm.getPlotAuto().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotPneumatic() != null) chartArm.getPlotPneumatic().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotPneumatic2() != null) chartArm.getPlotPneumatic2().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotPneumaticUsavp() != null) chartArm.getPlotPneumaticUsavp().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotUatl() != null) chartArm.getPlotUatl().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotKkm_s5k() != null) chartArm.getPlotKkm_s5k().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotKkm_s5k_2() != null) chartArm.getPlotKkm_s5k_2().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotKkm_vl10() != null) chartArm.getPlotKkm_vl10().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotBHV() != null) chartArm.getPlotBHV().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotVsc() != null) chartArm.getPlotVsc().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotCabin() != null) chartArm.getPlotCabin().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotMainControl() != null) chartArm.getPlotMainControl().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotRevControl() != null) chartArm.getPlotRevControl().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotSchema() != null) chartArm.getPlotSchema().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotPushKey() != null) chartArm.getPlotPushKey().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotSignals() != null) chartArm.getPlotSignals().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotAutodis() != null) chartArm.getPlotAutodis().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotSignBhv() != null) chartArm.getPlotSignBhv().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotTed() != null) chartArm.getPlotTed().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
        if (chartArm.getPlotTed_s5k() != null) chartArm.getPlotTed_s5k().getRangeAxis().setLabelFont(labelYAxisFont.deriveFont((float) size));
    }

    private void setHeightBlock(int size) {
        chartArm.setAlsnWeight(size);
        chartArm.setAlsnClubWeight(size);
        chartArm.setAlsnBrWeight(size);
        chartArm.setAutoWeight(size);
        chartArm.setPneumaticWeight(size);
        chartArm.setPneumatic2Weight(size);
        chartArm.setPneumaticUsavpWeight(size);
        chartArm.setKkmWeight_s5k(size);
        chartArm.setKkmWeight_s5k_2(size);
        chartArm.setKkmWeight_vl10(size);
        chartArm.setBhvWeight(size);
        chartArm.setVscWeight(size);
        chartArm.setCabinWeight(size);
        chartArm.setMainControlWeight(size);
        chartArm.setRevControlWeight(size);
        chartArm.setSchemaWeight(size);
    }

    /**
     * корректировка названий в зависимости от высоты окна
     * @param height - высота окна
     */
    public void setLabelResize(int height) {

        if (height > 900) {
            setSizeAngleLabel(14.0, 0);
        }
        else if (height > 700) {
            setSizeAngleLabel(13.0, 0);
        }
        else if (height > 500) {
            setSizeAngleLabel(12.0, Math.PI/10);
        }
        else if (height > 300) {
            setSizeAngleLabel(11.0, Math.PI/5);
        }
        else if (height > 100) {
            setSizeAngleLabel(10.0, Math.PI/3);
        }
        else {
            setSizeAngleLabel(9.0, Math.PI/2);
        }
    }

    /**
     * корректировка высоты блоков в зависимости от высоты всего окна
     * @param height высота окна
     */
    public void setWeightResize(int height) {
     //   System.out.println(height);
        if (height > 1200) {
            chartArm.setTrainWeight(W_TRAIN);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT +2);
        }
        else if (height > 1150) {
            chartArm.setTrainWeight(W_TRAIN + 1);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 2);
        }
        else if (height > 1100) {
            chartArm.setTrainWeight(W_TRAIN + 2);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 3);
        }
        else if (height > 1000) {
            chartArm.setTrainWeight(W_TRAIN + 3);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 4);
        }
        else if (height > 900) {
            chartArm.setTrainWeight(W_TRAIN + 4);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 5);
        }
        else if (height > 800) {
            chartArm.setTrainWeight(W_TRAIN + 5);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 6);
        }
        else if (height > 700) {
            chartArm.setTrainWeight(W_TRAIN + 7);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 7);
        }
        else if (height > 600) {
            chartArm.setTrainWeight(W_TRAIN + 10);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 9);
        }
        else if (height > 500) {
            chartArm.setTrainWeight(W_TRAIN + 15);
            chartArm.setProfileWeight(W_PROF);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 13);
        }
        else if (height > 400) {
            chartArm.setTrainWeight(W_TRAIN + 21);
            chartArm.setProfileWeight(W_PROF + 2);
            chartArm.setPositionWeight(W_POS + 1);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 19);
        }
        else if (height > 300) {
            chartArm.setTrainWeight(W_TRAIN + 24);
            chartArm.setProfileWeight(W_PROF + 2);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 23);
        }
        else {
            chartArm.setTrainWeight(100);
            chartArm.setTrainWeight(W_TRAIN + 30);
            chartArm.setProfileWeight(W_PROF + 4);
            chartArm.setPositionWeight(W_POS);
            chartArm.setSignalsWeight(W_SIGNAL);
            chartArm.setMapWeight(W_MAP);
            setHeightBlock(W_GANTT + 27);
        }
    }

    /**
     * @param splitPanVertical - вертикальный сплит
     * @param infoPanel - инфо-панель
     * @return - горизонтальный сплит
     */
    private JSplitPane makeSplitPanHorisontal(JSplitPane splitPanVertical, InfoPanel infoPanel) {
        JSplitPane splitPanHorisontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPanHorisontal.setResizeWeight(0.75);
        splitPanHorisontal.setOneTouchExpandable(true);
        splitPanHorisontal.setLeftComponent(splitPanVertical);
        splitPanHorisontal.setRightComponent(new JScrollPane(infoPanel));
        return splitPanHorisontal;
    }

    /**
     * панель расположена внизу и содержит ScrollBar и StatusPanel
     * @param statusPanel -
     * @param scrollBar -
     * @return - панель в нижней части экрана
     */
    private JPanel makeSouthPanel(StatusPanelArm statusPanel, ScrollBarArm scrollBar) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setPreferredSize(new Dimension(getWidth(), 40));
        panel.add(scrollBar);
        panel.add(statusPanel);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    /**
     * @return - позиция вертикального сплита
     */
    public int getVerticalSplitPos() {
        return splitPanVertical.getDividerLocation();
    }

    /**
     * установка позиции вертикального сплиттера
     * @param pos - позиция сплиттера
     */
    public void setVerticalSplitPos(int pos) {
        boolean isClose = false;
        if (pos <= 1) {
            isClose = true;
            pos = 150;
        }
        splitPanVertical.setDividerLocation(pos);
        splitPanVertical.setOneTouchExpandable(true);
        if (isClose)
            splitPanVertical.setDividerLocation(0.0d);
    }

    /**
     * установка вертикального курсора в соответсвии со строкой в таблице
     */
    private void setMarkerByRowTab() {
//        final int nCol = 2; // индекс колонки "секунды"
//        int row = hexTablePan.getCurRow();
//        if (row == -1) return;
//        int xx = Integer.parseInt((String)hexTablePan.getTable().getValueAt(row, nCol));
//        if (chartArm != null) {
//            chartArm.paintMarker(chartPanelArmMain, chartPanelArmSlave, scrollBar, xx);
//            infoPanel.repaint();
//        }
        int row = hexTab.getCurRow();
        if (row == -1) return;
        int xx = hexTab.getCurSecond();
        if (chartArm != null) {
            chartArm.paintMarker(chartPanelArmMain, chartPanelArmSlave, scrollBar, xx);
            infoPanel.repaint();
        }
    }

    public void initHeight() {
        setWeightResize(getHeight());
        setLabelResize(getHeight());
    }

//    /**
//     * установка курсора в соответствии с x
//     * @param x - положение курсора
//     */
//    public void drawMarker(int x) {
////        if (chartArm.getDomainAxis() == null) return;
////        double lower = chartArm.getDomainAxis().getRange().getLowerBound();
////        double upper = chartArm.getDomainAxis().getRange().getUpperBound();
////        if (x > upper) scrollBar.setValue((int)(lower + (x - upper)) + (int)(upper - lower) / 2);
////        if (x < lower) scrollBar.setValue((int)(lower - (lower - x)) - (int)(upper - lower) / 2);
////        if (chartArm != null) {
////            chartArm.setXMarker(x);
////            infoPanel.repaint();
//////            labelTime.repaint();
//////            labelLatLon.repaint();
////        }
//        if (chartArm != null) {
//            chartArm.paintMarker(chartPanelArmMain, scrollBar, x);
//           // chartArm.setRowHexTab();
//        }
//
////        if (chartPanelArmMain != null) {
////            LocalDateTime dateTime = chartDataset.getDateTime(x);
////
////
////            int n = chartPanelArmMain.getChartDataset().get_xByDateTime(dateTime);
////            chartPanelArmMain.getChartArm().drawMarker(scrollBar, n);
////
////            chartPanelArmMain.getChartArm().drawMarker(scrollBar, dateTime);
////            chartPanelArmMain.getChartArm().setRowHexTab();
////            chartPanelArmMain.getInfoPanel().repaint();
////        }
//        infoPanel.repaint();
//    }

//    /**
//     * установка курсора в соответсвии с датой временем
//     * @param dateTime - дата время
//     */
//    public void drawMarker(LocalDateTime dateTime) {
//        int x = chartDataset.get_xByDateTime(dateTime);
//        chartArm.drawMarker(null, scrollBar, x);
//    }

    /**
     * установка горячих клавиш перемещения вертикольного курсора, выделение строки таблицы, zoom
     */
    private void setKeyEventMoveCursor() {
        InputMap imap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "chartPanelArm.right");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "chartPanelArm.left");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK), "chartPanelArm.right.ctrl");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK), "chartPanelArm.left.ctrl");

        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK), "chartPanelArm.right.shift");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK), "chartPanelArm.left.shift");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK), "chartPanelArm.right.ctrl.shift");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK), "chartPanelArm.left.ctrl.shift");

        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "chartPanelArm.up");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "chartPanelArm.down");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK), "chartPanelArm.plus");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK), "chartPanelArm.plus");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK), "chartPanelArm.minus");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK), "chartPanelArm.minus");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "chartPanelArm.home");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "chartPanelArm.end");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "chartPanelArm.search_up");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK), "chartPanelArm.search_down");

        ActionMap amap = this.getActionMap();
        // курсор вправо
        amap.put("chartPanelArm.right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 1, true, false);
 //               chartArm.setRowHexTab();
            }
        });
        // курсор влево
        amap.put("chartPanelArm.left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 1, false, false);
//                chartArm.setRowHexTab();
            }
        });
        // курсор вправо широкий шаг
        amap.put("chartPanelArm.right.ctrl", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 30, true, false);
 //               chartArm.setRowHexTab();
            }
        });
        // курсор влево широкий шаг
        amap.put("chartPanelArm.left.ctrl", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 30, false, false);
//                chartArm.setRowHexTab();
            }
        });

        // курсор вправо с выделением участка
        amap.put("chartPanelArm.right.shift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 1, true, true);
                //               chartArm.setRowHexTab();
            }
        });
        // курсор влево с выделением участка
        amap.put("chartPanelArm.left.shift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 1, false, true);
//                chartArm.setRowHexTab();
            }
        });
        // курсор вправо широкий шаг с выделением участка
        amap.put("chartPanelArm.right.ctrl.shift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 30, true, true);
                //               chartArm.setRowHexTab();
            }
        });
        // курсор влево широкий шаг с выделением участка
        amap.put("chartPanelArm.left.ctrl.shift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.stepCursor(scrollBar, 30, false, true);
//                chartArm.setRowHexTab();
            }
        });

        // вначало канвы
        amap.put("chartPanelArm.home", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
//                drawMarker(1);
                if (chartArm != null) {
                    chartArm.paintMarker(chartPanelArmMain, chartPanelArmSlave, scrollBar, 1);
                    chartArm.setRowHexTab();
                    infoPanel.repaint();
                }
            }
        });
        // в конец канвы
        amap.put("chartPanelArm.end", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
//                drawMarker(chartDataset.getChartArrays().getSecond());
                if (chartArm != null) {
                    chartArm.paintMarker(chartPanelArmMain, chartPanelArmSlave, scrollBar, chartDataset.getChartArrays().getSecondCoordinate());
                    chartArm.setRowHexTab();
                    infoPanel.repaint();
                }
            }
        });

        // масштаб ++
        amap.put("chartPanelArm.plus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.doZoomByCursor(scrollBar, (int)chartArm.getXMarker(), -10, 1);
            }
        });
        // масштаб --
        amap.put("chartPanelArm.minus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chartArm.doZoomByCursor(scrollBar, (int)chartArm.getXMarker(), 10, 1);
            }
        });

        // hexTablePanel - строка вверх
        amap.put("chartPanelArm.up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int row = hexTab.getCurRow();// table.getSelectedRow();
                if (row > 0) row--;
                hexTab.selectRow(row);
            }
        });
        // hexTablePanel - строка вниз
        amap.put("chartPanelArm.down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
//                JTable table = hexTablePan.getTable();
//                table.requestFocus(true);
//                int row = hexTablePan.getCurRow();
//                if (row < table.getRowCount() - 1) row++;
//                hexTablePan.selectRow(row);

                int row = hexTab.getCurRow();
                if (row < hexTab.getRowCount() - 1) row++;
                hexTab.selectRow(row);
            }
        });

        amap.put("chartPanelArm.search_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                hexTab.selectSearchedIdBl(true);
            }
        });

        amap.put("chartPanelArm.search_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                hexTab.selectSearchedIdBl(false);
            }
        });
    }

    public ChartArm getChartArm() {
        return chartArm;
    }

    public void setChartPanelArmMain(ChartPanelArm chartPanelArmMain) {
        this.chartPanelArmMain = chartPanelArmMain;
    }

    public void setChartPanelArmSlave(ChartPanelArm chartPanelArmSlave) {
        this.chartPanelArmSlave = chartPanelArmSlave;
    }

    public ChartDataset getChartDataset() {
        return chartDataset;
    }

//    public HexTablePan getHexTablePan() {
//        return hexTablePan;
//    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public ScrollBarArm getScrollBar() {
        return scrollBar;
    }
}
