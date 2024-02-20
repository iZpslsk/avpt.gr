package avpt.gr.graph;

import avpt.gr.chart_dataset.*;
import avpt.gr.chart_dataset.keysEnum.LineKeys;
import avpt.gr.common.UtilsArmG;
import avpt.gr.common.WeightBlocks;
import avpt.gr.components.ScrollPopupCheck;
import avpt.gr.maps.Limits;
import avpt.gr.maps.Profiles;
import avpt.gr.maps.Stations;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import avpt.gr.train.Train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import static avpt.gr.chart_dataset.ListSignals.*;
import static avpt.gr.common.UtilsArmG.*;
import static avpt.gr.graph.ChartArm.*;
import static avpt.gr.maps.Limits.COLOR_LIM_MAP;


/**
 * наследует ChartPanel
 * отключаем zoom, стандартное PopupMenu
 * реализуем события мыши и перерисовку
 */
public class ChartPanelInheritor extends ChartPanel {

    public static final Color COLOR_DESCRIPTION_FONT = new Color(0xFFDFDC);
    private ChartArm chartArm;
    private ChartDataset chartDataset;
    private Point2D startPoint;
    private JScrollBar scrollBar;
    private double xCrosshair = 0;
    private XYPlot curSubplot;
    private boolean isRepaintCursorValues = true;
    private Point lastCursorPoint;
    private int shiftVertCur = 0; // вертикальное смещение данных курсора
    private int shiftHorCur = 0;  // горизонтальное смещение данных курсора

    CombinedDomainXYPlot cdPlot;// = (CombinedDomainXYPlot) chartArm.getXYPlot();
    ChartRenderingInfo chartInfo;// = getChartRenderingInfo();
    List subplotsList;// = cdPlot.getSubplots();

    /**
     * установка размера вертикально шкалы
     * @param rotation - направление
     */
    private void setSizeScale(int rotation) {
        XYDataset ds = curSubplot.getDataset(0);

        if (ds instanceof XYSeriesCollection) {
            String title = curSubplot.getRangeAxis().getLabel();
            if (title.equals(POSITION_LABEL) || title.equals(PROF_LABEL) || title.equals(TRAIN_LABEL) ) return;
            ValueAxis range = curSubplot.getRangeAxis();
            double curScaleUp;
            double curScaleDown;
            curScaleUp = range.getUpperBound() + rotation * (range.getUpperBound() / 10);
            curScaleDown = range.getLowerBound();
            range.setUpperBound(curScaleUp);
            range.setLowerBound(curScaleDown);
        }
    }

    public ChartPanelInheritor(final ChartArm chartArm, JScrollBar scrollBar) {
        super(chartArm);
        this.chartArm = chartArm;
        this.chartDataset = chartArm.getChartDataset();
        this.scrollBar = scrollBar;
        this.setForeground(Color.BLACK);
        setRangeZoomable(false);
        setDomainZoomable(false);
        setPopupMenu(null);
        setMouseZoomable(false);
        setFillZoomRectangle(false);
        setHorizontalAxisTrace(false);
        setVerticalAxisTrace(false);

        cdPlot = (CombinedDomainXYPlot) chartArm.getXYPlot();
        chartInfo = getChartRenderingInfo();
        subplotsList = cdPlot.getSubplots();

    //    final ScrollPopupCheck scrollPopupMenu = createScrollPopupMenu();

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if (rotation == 0) return;
                // высота шкалы
                if (e.isControlDown() && !e.isAltDown()) {
                    XYDataset ds = curSubplot.getDataset(0);
                    if (ds instanceof XYSeriesCollection) {
                        String title = curSubplot.getRangeAxis().getLabel();
                        if (title.equals(POSITION_LABEL) || title.equals(PROF_LABEL) || title.equals(TRAIN_LABEL)) return;
                        curSubplot.setWeight(curSubplot.getWeight() - rotation * 10);
                        if (curSubplot.getWeight() > 700) curSubplot.setWeight(700);
                        if (curSubplot.getWeight() < 50) curSubplot.setWeight(50);

                        if (title.equals(VOLTAGE_CS_LABEL)) WeightBlocks.setWeight_voltage_cs(curSubplot.getWeight());
                        if (title.equals(VOLTAGE_ENGINE_LABEL)) WeightBlocks.setWeight_voltage(curSubplot.getWeight());
                        if (title.equals(AMPERAGE_COMMON_LABEL)) WeightBlocks.setWeight_amperage_common(curSubplot.getWeight());
                        if (title.equals(AMPERAGE_ANCHOR_LABEL)) WeightBlocks.setWeight_amperage_anchor(curSubplot.getWeight());
                        if (title.equals(AMPERAGE_EXCITATION_LABEL)) WeightBlocks.setWeight_amperage_excitation(curSubplot.getWeight());
                        if (title.equals(AMPERAGE_ENGINE_LABEL)) WeightBlocks.setWeight_amperage_engine(curSubplot.getWeight());
                        if (title.equals(AMPERAGE_LABEL)) WeightBlocks.setWeight_amperage(curSubplot.getWeight());
                        if (title.equals(CONSUMPTION_LABEL)) WeightBlocks.setWeight_consumption(curSubplot.getWeight());
                        if (title.equals(POWER_LABEL)) WeightBlocks.setWeight_power(curSubplot.getWeight());
                        if (title.equals(TAIL_LABEL)) WeightBlocks.setWeight_tail(curSubplot.getWeight());
                        //if (title.equals(MODEM_LABEL)) WeightBlocks.setWeight_m(curSubplot.getWeight());
                        if (title.equals(PRESS_LABEL)) WeightBlocks.setWeight_press(curSubplot.getWeight());
                        if (title.equals(SPEED_LABEL)) WeightBlocks.setWeight_speed(curSubplot.getWeight());
                        if (title.equals(MAP_LABEL)) WeightBlocks.setWeight_map(curSubplot.getWeight());
                    }
                    WeightBlocks.setModified(true);
                }
                // размер шкалы
                else if (!e.isControlDown() && e.isAltDown()) {
                    setSizeScale(rotation);
                }
                else // масштаб
                    doZoomByCrosshair(new Point(e.getX(), e.getY()), rotation);
            }
        });

        addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseMoved(ChartMouseEvent e) {
                lastCursorPoint = e.getTrigger().getPoint();
                setPosCrosshair(e.getTrigger().getX(), e.getTrigger().getY());
                if (!isRepaintCursorValues)
                    repaint();
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                //
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {

                    int oldXMarker = (int)chartArm.getXMarker();
                    int curXMarker = (int)getCoordByMouseXY(e.getX(), e.getY());
                    chartArm.setXMarker(curXMarker);

                    // выделение интервала
                    if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        if (oldXMarker == chartArm.getStartIntervalXMarker()) {
                            if (curXMarker > chartArm.getStartIntervalXMarker() && curXMarker > chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(chartArm.getEndIntervalXMarker(), curXMarker);
                            if (curXMarker < chartArm.getStartIntervalXMarker() && curXMarker < chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(curXMarker, chartArm.getEndIntervalXMarker());
                            if (curXMarker > chartArm.getStartIntervalXMarker() && curXMarker < chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(curXMarker, chartArm.getEndIntervalXMarker());
                        }
                        if (oldXMarker == chartArm.getEndIntervalXMarker()) {
                            if (curXMarker > chartArm.getStartIntervalXMarker() && curXMarker > chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(chartArm.getStartIntervalXMarker(), curXMarker);
                            if (curXMarker < chartArm.getStartIntervalXMarker() && curXMarker < chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(curXMarker, chartArm.getStartIntervalXMarker());
                            if (curXMarker > chartArm.getStartIntervalXMarker() && curXMarker < chartArm.getEndIntervalXMarker())
                                chartArm.setIntervalXMarker(chartArm.getStartIntervalXMarker(), curXMarker);
                        }
                    }
                    else {
                        chartArm.setIntervalXMarker(curXMarker, curXMarker);
                    }

                    chartArm.setRowHexTab();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (SIGNALS_LABEL.equals(curSubplot.getRangeAxis().getLabel())) {
                        //checkPopupSignal.show(ChartPanelInheritor.this, e.getX(), e.getY());
                      //  scrollPopupMenu.show(ChartPanelInheritor.this, e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isRepaintCursorValues = false; // отключаем перерисовку значений у курсора
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    final Rectangle2D dataArea = getScreenDataArea();
                    final Point2D point = e.getPoint();
                    if (dataArea.contains(point))
                        startPoint = point;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isRepaintCursorValues = true; // включаем перерисовку значений у курсора
                lastCursorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isRepaintCursorValues = false;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                isRepaintCursorValues = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {

            }

            // перетаскивание канвы
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseCanvasDragged(e.getX(), getY());
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D)g;
        if (isRepaintCursorValues) {
            drawValuesCursorCrosshair(xCrosshair, curSubplot, lastCursorPoint, g2);
        }
    }

    private double getCoordByMouseXY(int mouseX, int mouseY) {
        double result = Double.NaN;
        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot) chartArm.getXYPlot();
        ChartRenderingInfo chartInfo = getChartRenderingInfo();
        Point2D java2DPoint = translateScreenToJava2D(new Point(mouseX, mouseY));
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        int subplotIndex = plotInfo.getSubplotIndex(java2DPoint);

        if (subplotIndex >= 0) {
            Rectangle2D dArea = plotInfo.getDataArea();
            result = cdPlot.getDomainAxis().java2DToValue(java2DPoint.getX(), dArea, cdPlot.getDomainAxisEdge());
        }
        return result;
    }

    public static String valToDescript(String descript, double val) {
        if (!Double.isNaN(val))
            return String.format(descript, val);	// шаблон - description из заготовки подобно format("ур.лев.\"А\":%5.1f мм", val);
        else {
            int n = descript.indexOf(":");
            return descript.substring(0, n + 1) + " нет";
        }
    }

    /**
     * перетаскивание канвы мышью
     * @param mouseX - координаты мыши x
     * @param mouseY - координаты мыши y
     */
    private void mouseCanvasDragged(int mouseX, int mouseY) {
        try {
            if (startPoint != null) {
                final Rectangle2D scaledDataArea = getScreenDataArea();

                startPoint = ShapeUtilities.getPointInRectangle(startPoint.getX(), startPoint.getY(), scaledDataArea);
                final Point2D pnt2D = ShapeUtilities.getPointInRectangle(
                        mouseX, mouseY, scaledDataArea
                );

                final Plot plot = getChart().getPlot();
                if (plot instanceof XYPlot) {
                    final XYPlot hvp = (XYPlot) plot;
                    final ValueAxis xAxis = hvp.getDomainAxis();

                    if (xAxis != null) {
                        final double translatedStartPoint = xAxis.java2DToValue(
                                (float) startPoint.getX(),
                                scaledDataArea,
                                hvp.getDomainAxisEdge()
                        );
                        final double translatedEndPoint = xAxis.java2DToValue(
                                (float) pnt2D.getX(),
                                scaledDataArea,
                                hvp.getDomainAxisEdge()
                        );
                        final double dX = translatedStartPoint - translatedEndPoint;

                        double newMin = xAxis.getLowerBound() + dX;
                        final double newMax = xAxis.getUpperBound() + dX;

                        if (newMin >= scrollBar.getMinimum() && newMin <= scrollBar.getMaximum()) {
                            xAxis.setLowerBound(newMin);
                            xAxis.setUpperBound(newMax);
                        }
                    }
                }
                startPoint = pnt2D;
                // устанавливаем позицию scrollBar к позиции канвы
                Range r = chartArm.getDomainAxis().getRange();
                scrollBar.setValue((int) Math.round(r.getLowerBound()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * изменение масштаба относительно crosshair (указателя мыши)
     * @param pnt положение курсора
     * @param rotation +1 or -1
     */
    private void doZoomByCrosshair(Point pnt, int rotation) {
        rotation = (rotation > 0) ? 10 : -10;
        setPosCrosshair((int)pnt.getX(), (int)pnt.getY());
        chartArm.doZoom(rotation, scrollBar);
//        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot) chartArm.getXYPlot();
//        ValueAxis valueAxis = cdPlot.getDomainAxis();
//        double result = valueAxis.getRange().getUpperBound() - valueAxis.getRange().getLowerBound();
        double x = xCrosshair;
        setPosCrosshair((int)pnt.getX(), (int)pnt.getY());
        double d = xCrosshair - x;
        scrollBar.setValue(scrollBar.getValue() - (int)Math.round(d));
        setPosCrosshair((int)pnt.getX(), (int)pnt.getY());
        //return result;
    }

    /**
     * установка Crosshair по координатам координатами мыши
     * @param mouseX - координата x
     * @param mouseY - координата н
     */
    private void setPosCrosshair(int mouseX, int mouseY) {

//        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot) chartArm.getXYPlot();
//        ChartRenderingInfo chartInfo = getChartRenderingInfo();
        Point2D java2DPoint = translateScreenToJava2D(new Point(mouseX, mouseY));
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        int subplotIndex = plotInfo.getSubplotIndex(java2DPoint);

        Rectangle2D dArea = plotInfo.getDataArea();
        xCrosshair = cdPlot.getDomainAxis().java2DToValue(java2DPoint.getX(), dArea,
                cdPlot.getDomainAxisEdge());

        if (subplotIndex >= 0) {

            isRepaintCursorValues = true;

            Rectangle2D panelArea = getScreenDataArea(mouseX, mouseY);

//            List subplotsList = cdPlot.getSubplots();
            Iterator iterator = subplotsList.iterator();
            int index = 0;
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            while (iterator.hasNext())
            {
                XYPlot subplot = (XYPlot) iterator.next();
                subplot.setDomainCrosshairValue(xCrosshair, false);

                if (subplotIndex == index) {
                    curSubplot = subplot;
                    double yCrosshair = subplot.getRangeAxis().java2DToValue(mouseY, panelArea, subplot.getRangeAxisEdge());
                    subplot.setRangeCrosshairVisible(true);
                    subplot.setRangeCrosshairValue(yCrosshair, true);
                }
                else
                    subplot.setRangeCrosshairVisible(false);

                index++;
            }
        }
        else {
            isRepaintCursorValues = false;
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * @param chartArm -
     * @param ser -
     * @param key -
     * @param x -
     * @param val -
     * @return - description для линий
     */
    public static  String getDescription(ChartArm chartArm, XYSeries ser, LineKeys key, double x, double val) {
        // отключаем информацию курсора за пределами поезда для ограничений карты и профиля
        Train train = chartArm.getChartDataset().getArrTrains().getTrain(
                chartArm.getChartDataset().getArrBlock32(), x);
        if (train == null) x = Double.NaN;
        switch (key) {
            case PROFILE: return valToDescript(ser.getDescription(), Profiles.getSlopeProfile(chartArm, x));
            case PROFILE_DIRECT: return valToDescript(ser.getDescription(), Profiles.getLenProfile(chartArm, x));
            case SPEED_MAX: return valToDescript(ser.getDescription(), Limits.getMapLimit(chartArm, (int)x));
            default: return valToDescript(ser.getDescription(), val);
        }

//        if (key == LineKeys.PROFILE)
//            return valToDescript(ser.getDescription(), Profiles.getSlopeProfile(chartArm, x));
//        else if (key == LineKeys.PROFILE_DIRECT)
//            return  valToDescript(ser.getDescription(), Profiles.getLenProfile(chartArm, x));
//        else if (key == LineKeys.SPEED_MAX)
//            return valToDescript(ser.getDescription(), Limits.getMapLimit(chartArm, (int)x));
//        else
//            return valToDescript(ser.getDescription(), val);
    }

    /**
     * возвращает description для дискретных сигналов и устанавливает для шрифт и его цвет
     * @param chartArm -
     * @param g2 -
     * @param key -
     * @param x -
     * @return - description для дискретных сигналов
     */
    public static String getDescriptionSignal(ChartArm chartArm, Graphics2D g2, int key, double x) {
        String descript = ListSignals.getDescriptionSygnal(key);
        if (descript.length() > 0) {
            if (chartArm.getChartDataset().getChartArrays().getListSignals().isSignal(key, (int) x)) {
                descript = " Есть: " + descript;
                g2.setFont(signalFont);
                g2.setColor(COLOR_DESCRIPTION_FONT);
            } else {
                descript = " Нет:  " + descript;
                g2.setColor(new Color(0xBBBBBB));  //
                g2.setFont(descriptFont);
            }
        }
        return descript;
    }

    /**
     * установка цвета description для видимых и невидимых линий
     * @param renderer -
     * @param g2 -
     * @param index -
     */
    public static void setVisiblePaint(XYItemRenderer renderer, Graphics2D g2, int index) {
        if (renderer.isSeriesVisible(index))
            g2.setPaint(COLOR_DESCRIPTION_FONT);
        else
            g2.setPaint(Color.GRAY);
    }

    public static SeriesTasks getSeriesTasks(String title, ChartDataset chartDataset) {
        SeriesTasks seriesTasks = null;
        if (title.equals(ALSN_LABEL))
            seriesTasks = chartDataset.getSeriesAlsn();
        if (title.equals(ALSN_CLUB_LABEL))
            seriesTasks = chartDataset.getSeriesAlsnClub();
        if (title.equals(ALSN_BR_LABEL))
            seriesTasks = chartDataset.getSeriesAlsnBr_vl10();
        if (title.equals(ALSN_BR_VL80_LABEL))
            seriesTasks = chartDataset.getSeriesAlsnBr_vl80();
        if (title.equals(AUTO_LABEL))
            seriesTasks = chartDataset.getSeriesAutoDrive();
        if (title.equals(PNEUMATIC_LABEL))
            seriesTasks = chartDataset.getSeriesPneumatic();
        if (title.equals(PNEUMATIC2_LABEL))
            seriesTasks = chartDataset.getSeriesPneumatic2();
        if (title.equals(PNEUMATIC_USAVP_LABEL))
            seriesTasks = chartDataset.getSeriesPneumaticUsavp();
        if (title.equals(UATL_LABEL))
            seriesTasks = chartDataset.getSeriesUatl();
        if (title.equals(KM130_LABEL))
            seriesTasks = chartDataset.getSeriesKM130();
        if (title.equals(KKM_KZ8_LABEL))
            seriesTasks = chartDataset.getSeriesKKM_kz8();
        if (title.equals(KKM_S5K_LABEL))
            seriesTasks = chartDataset.getSeriesKKM_s5k();
        if (title.equals(KKM_S5_LABEL))
            seriesTasks = chartDataset.getSeriesKKM_s5();
        if (title.equals(KKM_S5K_2_LABEL))
            seriesTasks = chartDataset.getSeriesKKM_s5k_2();
        if (title.equals(KKM_VL10_LABEL))
            seriesTasks = chartDataset.getSeriesKKM_vl10();

        if (title.equals(BHV_LABEL))
            seriesTasks = chartDataset.getSeriesBhv_valve();

        if (title.equals(KEYS_LABEL))
            seriesTasks = chartDataset.getSeriesPushKey();

        //                            if (title.equals(BHV_LABEL))
//                                seriesTasks = chartDataset.getSeriesBHV();
        if (title.equals(VSC_LABEL))
            seriesTasks = chartDataset.getSeriesVsc();
        if (title.equals(CABIN_LABEL))
            seriesTasks = chartDataset.getSeriesCabin();

        if (title.equals(MAIN_CONTROL_LABEL))
            seriesTasks = chartDataset.getSeriesMainControl();
        if (title.equals(REV_CONTROL_LABEL))
            seriesTasks = chartDataset.getSeriesRevControl();
        if (title.equals(SCHEMA_LABEL))
            seriesTasks = chartDataset.getSeriesSchema();

        return seriesTasks;
    }

    /**
     * @param title -
     * @return title без сокращения
     */
    public static String withoutShrink(String title) {
        if (AUTODIS_LABEL.equals(title))
            return "Сигналы-автодиспетчер ";
        else if (SIGN_TED_LABEL.equals(title))
            return "Отключение ТЭД ";
        else if (SIGN_TED_S5K_LABEL.equals(title))
            return "Отключение ТЭД ";
        else if (AUTO_LABEL.equals(title))
            return "Автоведение ";
        else if (PNEUMATIC_LABEL.equals(title))
            return "Пневматика ";
        else if (PNEUMATIC2_LABEL.equals(title))
            return "Пневматика второго";
        else if (PNEUMATIC_USAVP_LABEL.equals(title))
            return "Пневматика УСАВП";
        else if (UATL_LABEL.equals(title))
            return "УАТЛ ";
        else if (KM130_LABEL.equals(title))
            return "Обмен с краном 130 ";
        else if (VOLTAGE_CS_LABEL.equals(title))
            return "Напряжение контактной сети ";
        else if (AMPERAGE_COMMON_LABEL.equals(title))
            return "Общий ток локомотива ";
        else if (ALSN_CLUB_LABEL.equals(title))
            return "АЛСН-КЛУБ ";
        else if (MAIN_CONTROL_LABEL.equals(title))
            return "Главная рукоятка контроллера ";
        else if (REV_CONTROL_LABEL.equals(title))
            return "Реверсивная рукоятка контроллера ";
        else if (SCHEMA_LABEL.equals(title))
            return "Состояние силовой схемы ";
        else if (CONSUMPTION_LABEL.equals(title))
            return "Расход энергии";
        else if (AMPERAGE_EXCITATION_LABEL.equals(title))
            return "Ток возбуждения";
        else if (AMPERAGE_ENGINE_LABEL.equals(title))
            return "Ток двигателей";
        else if (VOLTAGE_ENGINE_LABEL.equals(title)) {
             return "Напряжение двигателя";
        }
        else
            return title;
    }

    private int getWd(Graphics2D g2, String str, int curWd) {
        return Math.max(curWd, (int)descriptFont.getStringBounds(str, g2.getFontRenderContext()).getWidth());
    }

    /**
     * рисуем информацию на позиции курсора мыши
     * @param x - crosshair x
     * @param subplot - соответсвующий положению курсора (по y)
     * @param pnt - cursor point
     * @param g2 - Graphics2D
     */
    private void drawValuesCursorCrosshair(double x, XYPlot subplot, Point pnt, Graphics2D g2) {

        if (pnt != null && subplot != null) {
            XYItemRenderer renderer = subplot.getRenderer();
            String title = subplot.getRangeAxis().getLabel(); // текст заголовока;
            g2.setFont(commonFont);
            g2.setPaint(new Color(0xD4D5D4));
            int wd = 0;	// максималная ширина строки description
            int hh = (int)commonFont.getStringBounds(chartDataset.getDateText((int)x), g2.getFontRenderContext()).getHeight();
            int hd = hh;	// hd - высота строки
            if (!TRAIN_LABEL.equals(title)) {
                String str = chartDataset.getDateText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
                str = chartDataset.getTimeText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
                str = chartDataset.getRailCoordinateText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
                str = chartDataset.getLatitudeText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
                str = chartDataset.getLongitudeText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
                str = chartDataset.getCoordinateText((int)x);
                wd = getWd(g2, str, wd);
                g2.drawString(str, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
            } else
                drawTrainInfoCrosshair(g2, x, pnt);

            // карта
            if (MAP_LABEL.equals(title)) {
                drawMapInfoCrosshair(g2, x, pnt, hh, hd);
                return;
            }

            // текущая информация
            g2.setColor(new Color(0xFFEBCD)); // цвет шрифта заголовка
            g2.setFont(titleFont);

//            int wd = 0;	// максималная ширина строки description
            int max_h = 0;
            final int sizeSqr = 10; // размер квадратика перед title
            XYDataset ds = subplot.getDataset(0);

            // title без сокращения
            if (title != null && title.length() > 0)
                g2.drawString(withoutShrink(title), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            for (int i = 0; i < subplot.getSeriesCount(); i++) {
                if (ds instanceof XYSeriesCollection) {	// для линейных графиков
                    if (!renderer.isSeriesVisible(i)) continue; // исключаем невидимые линии из перечня
                    LineKeys key = (LineKeys) subplot.getDataset().getSeriesKey(i);
                    if (key == LineKeys.TRAIN || key == LineKeys.MAP_LINE || key == LineKeys.MAP_DIRECT) continue;
                    double val = UtilsArmG.getVal(ds, x, i);
                    XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
                    String description = getDescription(chartArm, ser, key, x, val);
                    Rectangle2D rec = descriptFont.getStringBounds(description, g2.getFontRenderContext());
                    hd = (int) rec.getHeight(); // высота строки description
                    if (wd < (int) rec.getWidth()) wd = (int) rec.getWidth();    // макс ширина строки description
                    max_h += (int) rec.getHeight();
                    g2.setFont(descriptFont);
                    // квадратик рисуем в цвет текущей series
                    if (key == LineKeys.SPEED_MAX)
                        g2.setColor(COLOR_LIM_MAP);
                    else
                        g2.setColor((Color) renderer.getSeriesPaint(i));
                    hh += hd;
                    //String description = getDescription(chartArm, ser, key, x, val);

                    if (key != LineKeys.PROFILE && key != LineKeys.PROFILE_DIRECT)
                        g2.fill(new Rectangle2D.Double(pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr,
                                sizeSqr, sizeSqr));

                    setVisiblePaint(renderer, g2, i);

                    if (description.length() > 0)
                        g2.drawString(description, pnt.x - shiftHorCur + sizeSqr, pnt.y + hh  - shiftVertCur);

                } else if (ds instanceof XYTaskDataset) { // для списка задач
                    // дискретные сигналы
                    if (SIGNALS_LABEL.equals(title) || AUTODIS_LABEL.equals(title) || SIGN_BHV_LABEL.equals(title)
                            || SIGN_TED_LABEL.equals(title) || SIGN_TED_S5K_LABEL.equals(title) || SIGN_LINK_LABEL.equals(title)) {
                        hh += hd;
                        int key = (Integer) ((XYTaskDataset) ds).getTasks().getSeries(i).getKey();

                        g2.setColor((Color) renderer.getSeriesPaint(i));
                        g2.fill(new Rectangle2D.Double(
                                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr, sizeSqr, sizeSqr));

                        String descript = getDescriptionSignal(chartArm, g2, key, x);
                        if (descript.length() > 0) {
                            g2.drawString(descript, pnt.x - shiftHorCur + sizeSqr, pnt.y + hh - shiftVertCur);
                            int curWidth = (int) descriptFont.getStringBounds(descript, g2.getFontRenderContext()).getWidth();
                            if (wd < curWidth) wd = curWidth;    // макс ширина строки description
                        }
                    }
                    // алсн, ккм, пневматика ...
                    else {
                        hh += hd;
                        int key = (Integer)((XYTaskDataset)ds).getTasks().getSeries(i).getKey();
                        assert title != null;
                        SeriesTasks seriesTasks = getSeriesTasks(title, chartDataset);

                        if (seriesTasks != null) {
                            int val = seriesTasks.getItemVal(key, (int) x);
                            Train train = chartArm.getChartDataset().getArrTrains().getTrain(
                                    chartArm.getChartDataset().getArrBlock32(), x);
                            boolean isAsim = train != null && train.isAsim();
                            String descript = seriesTasks.getDescript(val, key, train != null ? train.getTypeLoc() : -1);
                            Paint paint = seriesTasks.getItemPaint(key, (int)x);
                            if (paint != null) {

                                // если кж
                                if (!isAsim && val == TaskAlsn.ALSN_REDYELLOW && title.equals(ALSN_LABEL)) {
//                                    paint = new GradientPaint(pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr,
//                                            Color.YELLOW, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur, Color.RED);
                                    paint = new DualPaint(Color.YELLOW, Color.RED);
                                }
                                if (isAsim && val == TaskAlsn.ALSN_REDYELLOW_ASIM && title.equals(ALSN_LABEL)) {
//                                    paint = new GradientPaint(pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr,
//                                            Color.YELLOW, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur, Color.RED);
                                    paint = new DualPaint(Color.YELLOW, Color.RED);
                                }
                                if (val == TaskAlsnClub.ALSN_REDYELLOW && title.equals(ALSN_CLUB_LABEL)) {
//                                    paint = new GradientPaint(pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr,
//                                            Color.YELLOW, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur, Color.RED);
                                    paint = new DualPaint(Color.YELLOW, Color.RED);
                                }
                                if (val == TaskAlsnBr.ALSN_REDYELLOW && (title.equals(ALSN_BR_LABEL) || title.equals(ALSN_BR_VL80_LABEL))) {
//                                    paint = new GradientPaint(pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr,
//                                            Color.YELLOW, pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur, Color.RED);
                                    paint = new DualPaint(Color.YELLOW, Color.RED);
                                }
                                g2.setPaint(paint);
                                g2.fill(new Rectangle2D.Double(
                                        pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur - sizeSqr, sizeSqr, sizeSqr));
                            }
                            if (descript != null && descript.length() > 0) g2.setFont(signalFont);
                            g2.setColor(COLOR_DESCRIPTION_FONT);
                            assert descript != null;
                            if (descript.length() > 0) {
                                g2.drawString(descript, pnt.x - shiftHorCur + sizeSqr, pnt.y + hh - shiftVertCur);
                                int curWidth = (int) descriptFont.getStringBounds(descript, g2.getFontRenderContext()).getWidth();
                                if (wd < curWidth) wd = curWidth;    // макс ширина строки description
                            }
                        }
                    }
                }
            }
            Point2D pp = translateScreenToJava2D(pnt);
            Rectangle2D rec = getChartRenderingInfo().getPlotInfo().getPlotArea();

//            // квадратик обрамляющий информацию курсора
//            g2.setColor(new Color(0x90000000, true));
//            g2.fill(new Rectangle2D.Double(pnt.x - shiftHorCur, pnt.y - shiftVertCur - sizeSqr,
//                    wd, hh + hd));

            // если уходим за границу вниз, смещаемся вверх над курсором
            if (rec.getHeight() < pp.getY() + hh)
                shiftVertCur = hh;
            else
                shiftVertCur = 0;


            // если уходим за границу вправо, смещаемся влево за курсор
            if (rec.getWidth() < pp.getX() + wd)
                shiftHorCur = wd;
            else
                shiftHorCur = 0;
        }
    }

    private void drawMapInfoCrosshair(Graphics2D g2, double x, Point pnt, int hh, int hd) {
      //  hh += hd;
        // если уходим за границу вправо, смещаемся влево за курсор
        final int SHIFT_HOR = 100; // величина смещения влево
        int curWidth = (int)getChartRenderingInfo().getPlotInfo().getPlotArea().getWidth();
        if (curWidth < translateScreenToJava2D(pnt).getX() + SHIFT_HOR)
            shiftHorCur = SHIFT_HOR;
        else
            shiftHorCur = 0;
        final int SHIFT_VER = 50;
        int curHeight = (int)getChartRenderingInfo().getPlotInfo().getPlotArea().getHeight();
        if (curHeight < translateScreenToJava2D(pnt).getY() + SHIFT_VER)
            shiftVertCur = SHIFT_VER;
        else
            shiftVertCur = 0;

        double lower = chartArm.getDomainAxis().getRange().getLowerBound();
        double upper = chartArm.getDomainAxis().getRange().getUpperBound();
        int dd = (int)(upper - lower) / 200;
        int[] val = chartDataset.getValObject((int)x, dd);
        if (val == null) return;

        Point2D pp = translateScreenToJava2D(pnt);
        Rectangle2D rec = getChartRenderingInfo().getPlotInfo().getPlotArea();

        // если уходим за границу вниз, смещаемся вверх над курсором
        if (rec.getHeight() < pp.getY() + hh)
            shiftVertCur = hh + hd * 2;
        else
            shiftVertCur = 0;

        g2.setColor(new Color(0xFFEBCD)); // цвет шрифта заголовка
        g2.setFont(titleFont);
        if (val[0] == 8) {
            Stations.Station station = chartArm.getStations().getStation(val[1]);
            g2.drawString(String.format("прибытие: %s", station.getNameStation()),
                    pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        else {
            g2.drawString("Объекты:", pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        g2.setColor(new Color(0xFFFFFFFF));
        if (val[0] == 8) { // станция
            Stations.Station station = chartArm.getStations().getStation(val[1]);
            LocalTime time_arrival_schedule = UtilsArmG.getTime((int)station.getTimeArrivalSchedule());
            LocalTime time_arrival_fact = UtilsArmG.getTime((int)station.getTimeArrivalFact());

            g2.drawString(String.format("план: %s", time_arrival_schedule),
                    pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
            g2.drawString(String.format("факт: %s", time_arrival_fact),
                    pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 4) {
            g2.drawString(String.format(" %s", "нейтральная вставка"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 6) {
            g2.drawString(String.format(" %s", "проверка тормозов"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 7) {
            g2.drawString(String.format(" %s", "светофор"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 9) {
            g2.drawString(String.format(" %s", "переезд"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 10) {
            g2.drawString(String.format(" %s", "понаб"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
        if (val[0] == 19) {
            g2.drawString(String.format(" %s", "обрывоопасное место"), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        }
    }

    /**
     * вывод информации о поезде
     * @param g2 - Graphics2D
     * @param x - координата
     * @param pnt - координата курсора
     */
    private void drawTrainInfoCrosshair(Graphics2D g2, double x, Point pnt) {
        // если уходим за границу вправо, смещаемся влево за курсор
        final int SHIFT = 200; // величина смещения влево
        Color colorCaption = new Color(0xFFFD7A);   // цвет заголовка
        Color colorValue = new Color(0xFEF9FF);     // цвет значений
        int curWidth = (int)getChartRenderingInfo().getPlotInfo().getPlotArea().getWidth();
        if (curWidth < translateScreenToJava2D(pnt).getX() + SHIFT)
            shiftHorCur = SHIFT;
     //   boolean isAsim = chartArm.getChartDataset().getArrBlock32().isASIM();

        g2.setFont(trainFont);
        int hh = (int)trainFont.getStringBounds("", g2.getFontRenderContext()).getHeight();
        int hd = hh - hh / 5;	// hd - высота строки
       // Train avpt.gr.train = getTrain(x);
        Train train = chartArm.getChartDataset().getArrTrains().getTrain(
                chartArm.getChartDataset().getArrBlock32(), x);
        if (train == null) return;
        boolean isAsim = train.isAsim();
        if (train.getDateTimeStart() == null) {
            g2.setColor(Color.RED);
            int shift = (int) descriptFont.getStringBounds(TRAIN_LABEL, g2.getFontRenderContext()).getWidth();
            g2.drawString("Поезда не определены", pnt.x - shiftHorCur + shift, pnt.y + hh - shiftVertCur);
            return;
        }

        g2.setColor(colorCaption);
        int shift = (int) descriptFont.getStringBounds(TRAIN_LABEL, g2.getFontRenderContext()).getWidth();
        g2.drawString(" №" + train.getNumTrainStr(), pnt.x - shiftHorCur + shift, pnt.y + hh - shiftVertCur);
        hh += hd;

        g2.setColor(colorValue);

        String routeName = train.getRoutName();
        if (routeName != null) {
            g2.drawString(String.format("Маршрут:         %s", routeName),
                    pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }

        LocalDateTime dateTimeStart = train.getDateTimeStart();
        if (dateTimeStart != null) {
            g2.drawString(String.format("Время начала:    %s",
                    dateTimeStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))),
                    pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }

        g2.drawString(String.format("Локомотив:       %s %s %s",
                train.getNumLoc() == -1 ? "" : "№%" + train.getNumLoc(), Train.getNameTypeLoc(train.getTypeLoc(), train.getLocTypeAsoup()), train.getNumSectionText()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.drawString(String.format("Вес:             %d т", train.getWeightTrain()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

//        g2.drawString(String.format("Ограничения карты:             %d", avpt.gr.train.getCntCLim(true)), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//        hh += hd;
//        g2.drawString(String.format("Ограничения временные:             %d", avpt.gr.train.getCntVLim()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//        hh += hd;


        g2.drawString(String.format("Вагонов:         %d шт", train.getCntWags()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

        if (!isAsim) {
            g2.drawString(String.format("Тип состава:     %s", Train.getTypeS(train.getTypeS())), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }

        g2.drawString(String.format("Табельный номер: %d", train.getNumTab()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

        if (!isAsim) {
            g2.drawString(String.format("Диаметр бандажа: %d", train.getdBandage()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
            g2.drawString(String.format("Режим РТ:        %s", train.getStatusIsavprt(train.getStIsavprt())), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;

            if (train.getStIsavprt() == 4) {
                g2.drawString(String.format("Положение поезда:%s", train.getPositionTrain()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
            }
            if (train.getStIsavprt() > 0) {
                g2.drawString(String.format("Второй локомотив:№%s-%s",
                        train.getNumSlave(), train.getNameSlave()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
//                g2.drawString(String.format("Сетевой адрес:   %s", avpt.gr.train.getNetAddress()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//                hh += hd;
//                g2.drawString(String.format("Доп канал:       %s", avpt.gr.train.getAdditionalChannel()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//                hh += hd;
            }
            g2.drawString(String.format("Версия БР:       %s", train.getVerBr()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;

            LocalDate dateMap = train.getDateMap();
            if (dateMap != null) {
                g2.drawString(String.format("Дата карты:      %s",
                                train.getDateMap().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                        pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
                hh += hd;
            }

            g2.drawString(String.format("Длина поезда:    %s м", train.getLengthTrain()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
            g2.drawString(String.format("Секций:          %s", train.getCntSection()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
//            if (avpt.gr.train.getStIsavprt() == 4) {
//                g2.drawString(String.format("Положение поезда:%s", avpt.gr.train.getPositionTrain()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//                hh += hd;
//            }
        }
        if (train.getAvgSpeedMove() > -1) {
            g2.drawString(String.format("Тех скорость:    %4.1f км/ч", train.getAvgSpeedMove()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        if (train.getAvgSpeed() > -1) {
            g2.drawString(String.format("Участк скорость: %4.1f км/ч", train.getAvgSpeed()), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        g2.setColor(colorCaption);
        g2.drawString("Пройдено:", pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        String space = " "; // отступ
        g2.setColor(colorValue);
        g2.drawString(String.format(space + "Автоведение:%4.1f км", train.getDistance_auto() / 1000.0),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.drawString(String.format(space + "Подсказка:  %4.1f км", train.getDistance_prompt() / 1000.0),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.drawString(String.format(space + "Всего:      %4.1f км", train.getDistance() / 1000.0),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.setColor(colorCaption);
        g2.drawString("Затраченное время:", pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.setColor(colorValue);
        g2.drawString(String.format(space + "Автоведение:%s", train.getDuration_time_auto()),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.drawString(String.format(space + "Подсказка:  %s", train.getDuration_time_prompt()),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;
        g2.drawString(String.format(space + "Всего:      %s", train.getDuration_time()),
                pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

        g2.setColor(colorCaption);
        g2.drawString("Расход энергии:", pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

        g2.setColor(colorValue);
        long val = train.getAct();
        if (val > 0) {
            g2.drawString(String.format(space + "%d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getAct1();
        if (val > 0) {
            g2.drawString(String.format(space + "1-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getAct2();
        if (val > 0) {
            g2.drawString(String.format(space + "2-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getAct3();
        if (val > 0) {
            g2.drawString(String.format(space + "3-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getAct4();
        if (val > 0) {
            g2.drawString(String.format(space + "4-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }

        g2.setColor(colorCaption);
        g2.drawString("Рекуперация:", pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
        hh += hd;

        g2.setColor(colorValue);
        val = train.getRec();
        if (val > 0) {
            g2.drawString(String.format(space + "%d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getRec1();
        if (val > 0) {
            g2.drawString(String.format(space + "1-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getRec2();
        if (val > 0) {
            g2.drawString(String.format(space + "2-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getRec3();
        if (val > 0) {
            g2.drawString(String.format(space + "3-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
            hh += hd;
        }
        val = train.getRec4();
        if (val > 0) {
            g2.drawString(String.format(space + "4-я секц %d кВт", val), pnt.x - shiftHorCur, pnt.y + hh - shiftVertCur);
//            hh += hd;
        }
    }

//    private CheckPopup createCheckPopupSignal() {
//        CheckPopup checkPopup = new CheckPopup();
//        SeriesSignalsDiscrete seriesSignalsDiscrete = chartDataset.getSeriesSignalsDiscrete();
//        for (int i = KEY_BAN_THRUST; i <= KEY_ALLOW_ANSWER; i++) {
//            String description = ListSignals.getDescriptionSygnal(i);
//            Color color = seriesSignalsDiscrete.getColorSeries(i);
//            if (!description.isEmpty())
//                checkPopup.add(color, 10, 10, description);
//        }
//        return checkPopup;
//    }

    private class CheckItemActionListener implements ActionListener {

        private JPopupMenu getPopup(JCheckBoxMenuItem item) {
            JPopupMenu popupMenu = null;
            while (popupMenu == null) {
                if (item.getParent() instanceof  JPopupMenu) {
                    popupMenu = (JPopupMenu) item.getParent();
                }
            }
            return popupMenu;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ScrollPopupCheck.Item item = (ScrollPopupCheck.Item)e.getSource();
            ScrollPopupCheck scrollPopupMenu = (ScrollPopupCheck) getPopup(item);
            SeriesSignalsDiscrete seriesSignalsDiscrete = chartDataset.getSeriesSignalsDiscrete();
            for (int i = 0; i < scrollPopupMenu.getComponentCount(); i++) {
                if (scrollPopupMenu.getComponent(i) instanceof  JCheckBoxMenuItem) {
                    item = (ScrollPopupCheck.Item) scrollPopupMenu.getComponent(i);
                //    System.out.println(item.getText() + " " + item.getKey() + " " + item.isSelected());
                }
            }


          //  ((XYTaskDataset)seriesSignalsDiscrete.getTaskSeriesCollection()).getTasks().removeAll();

          //  seriesSignalsDiscrete.addTaskSeriesEmpty(4);
         //   System.out.println(item.getText() + " " + item.isSelected());
        }
    }

    private ScrollPopupCheck createScrollPopupMenu() {
        ScrollPopupCheck scrollPopupMenu = new ScrollPopupCheck();
        SeriesSignalsDiscrete seriesSignalsDiscrete = chartDataset.getSeriesSignalsDiscrete();

        //((XYTaskDataset)seriesSignalsDiscrete.getTaskSeriesCollection()).getTasks().getSeries(0).removeAll();
        //System.out.println(key);
       // scrollPopupMenu.getInvoker();

        for (int i = KEY_BAN_THRUST; i <= KEY_ALLOW_ANSWER; i++) {
            String description = ListSignals.getDescriptionSygnal(i);
            Color color = seriesSignalsDiscrete.getColorSeries(i);
            if (!description.isEmpty() && color != Color.GRAY) {
              ScrollPopupCheck.Item item = scrollPopupMenu.add(color, 10, 10, description, i);

              if (seriesSignalsDiscrete.isSeriesSelected(i))
                item.setSelected(true);
              item.addActionListener(new CheckItemActionListener());
            }
        }
        return scrollPopupMenu;
    }

}
