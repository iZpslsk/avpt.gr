package avpt.gr.common;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.UnitType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

/**
 * Renderer линий арма
 * переопределяем drawItem для уменьшения количества прорисованых точек при уменьшении масштаба
 */
public class StandardXYItemRendererArm extends StandardXYItemRenderer {
    // точность - по времени 1, 2, 3... , по координате 10, 20, 30... .
    private int precision = 1;
    // всего тиков в arrBlock32
    private int count;
    // динамическая величена upperBound
    private UtilsArmG.MutableDouble mutable_duration;
    // развертка по времени или по координате
    private boolean isTime;

    private double transX1_pre = 0;
    private double transX0_pre = 0;
    private double d_x = 0;
    private double d_y = 0;

    public StandardXYItemRendererArm(UtilsArmG.MutableDouble mutable_duration, int count, boolean isTime) {
        this.mutable_duration = mutable_duration;
        this.count = count;
        this.isTime = isTime;
    }

    /**
     * эмперически определяем значение точности - precision
     * @param n - множитель, для развертки по времени 1, по координите 10
     */
    private void setPrecision(int n) {
        int zoom = (int)(mutable_duration.get() / 1500);
        if (zoom <= n) {
            precision = 1;
        }
        else if (zoom <= 2 * n) {
            precision = 1;
        }
        else if (zoom <= 3 * n) {
            precision = 2;
        }
        else if (zoom <= 4 * n) {
            precision = 2;
        }
        else if (zoom <= 5 * n) {
            precision = 3;
        }
        else if (zoom <= 6 * n) {
            precision = 3;
        }
        else if (zoom <= 7 * n) {
            precision = 4;
        }
        else if (zoom <= 8 * n) {
            precision = 4;
        }
        else if (zoom <= 9 * n) {
            precision = 5;
        }
        else if (zoom <= 10 * n) {
            precision = 5;
        }
        else precision = 5;
    }

    /**
     * получаем процент точек на графике относительно количества тиков
     * @param series - конкретная линия из набора данных dataset
     * @param dataset - набор данных
     * @return - процент дискретизации
     */
    private double getPercent(int series, XYDataset dataset) {
        int cnt = dataset.getItemCount(series);
        return cnt / (double)count * 100.0;
    }

    /**
     * немного подправили исходник
     * @param g2 -
     * @param state -
     * @param dataArea -
     * @param info -
     * @param plot -
     * @param domainAxis -
     * @param rangeAxis -
     * @param dataset -
     * @param series -
     * @param item -
     * @param crosshairState -
     * @param pass -
     */
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
       // super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState, pass);

        String label = rangeAxis.getLabel();
//        if (label.equals(ChartArm.POWER_LABEL)) {
//            domainAxis.getRange().getLength();
//            int cnt = dataset.getSeriesCount();
//            cnt = dataset.getItemCount(series);
//            LineKeys description = (LineKeys) dataset.getSeriesKey(series);
//            double upper = rangeAxis.getUpperBound();
//            double lower = rangeAxis.getLowerBound();
//            double percent = cnt / (double)count * 100.0;
//            System.out.println("lower=" + lower + " upper=" + upper + " %=" + percent + " description=" + description);
//        }

        // максимальный процент дискретизации линии, меньше которого не будем ее разряжать
        final int max_percent = 25;
        // всего точек в конкретной линии - series;
        final int item_cnt = dataset.getItemCount(series);
        // устанавливаем точность (precession) для текущего масштаба
     //   setPrecision(isTime ? 1 : 10);
        double percent = getPercent(series, dataset);
        if (precision == 0) return;
        boolean itemVisible = this.getItemVisible(series, item);
        Shape entityArea = null;
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }

        PlotOrientation orientation = plot.getOrientation();
        Paint paint = this.getItemPaint(series, item);
        Stroke seriesStroke = this.getItemStroke(series, item);
        g2.setPaint(paint);
        g2.setStroke(seriesStroke);
        double x1 = 0;
        double y1 = 0;
        // обычная отрисовка
        if (percent < max_percent) {
            x1 = dataset.getXValue(series, item);
            y1 = dataset.getYValue(series, item);
        }
        // разряжаем в зависимости от масштаба
        else if (item % precision == 0) {
            x1 = dataset.getXValue(series, item);
            y1 = dataset.getYValue(series, item);
            // берем максимальное значение для y1 от всех игнорируемых items впереди
            for (int i = 1; i < precision; i++) {
                if (item + i < item_cnt)
                    y1 = y1 > 0 ? Math.min(y1, dataset.getYValue(series, item + i)) :
                            Math.max(y1, dataset.getYValue(series, item + i));
            }
        }
        if (java.lang.Double.isNaN(x1) || java.lang.Double.isNaN(y1)) {
            itemVisible = false;
        }

        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        double d = transX1 - transX1_pre;
        d_x += d;
        if (Math.abs(d_x) > 5.0) {

            d_x = 0;
        }
        else return;

        //if (label.equals(ChartArm.VOLTAGE_ENGINE_LABEL))
       // System.out.println(d_sum);
        transX1_pre = transX1;
//        if (d < 1) {
//           // System.out.println(d);
//            transX1_pre = transX1;
//            return;
//        }

//        if (d > 1) {
//            transX1_pre = transX1;
//            System.out.println(d);
//        }
        double xx = 0;
        double yy = 0;
        int rangeAxisIndex;
        if (this.getPlotLines()) {
            if (this.getDrawSeriesLineAsPath()) {
                StandardXYItemRenderer.State s = (StandardXYItemRenderer.State)state;
                if (s.getSeriesIndex() != series) {
                    s.seriesPath.reset();
                    s.setLastPointGood(false);
                    s.setSeriesIndex(series);
                }

                if (itemVisible && !java.lang.Double.isNaN(transX1) && !java.lang.Double.isNaN(transY1)) {
                    float x = (float)transX1;
                    float y = (float)transY1;
                    if (orientation == PlotOrientation.HORIZONTAL) {
                        x = (float)transY1;
                        y = (float)transX1;
                    }

                    if (s.isLastPointGood()) {
                        s.seriesPath.lineTo(x, y);
                    } else {
                        s.seriesPath.moveTo(x, y);
                    }

                    s.setLastPointGood(true);
                } else {
                    s.setLastPointGood(false);
                }

                if (item == dataset.getItemCount(series) - 1 && s.getSeriesIndex() == series) {
                    g2.setStroke(this.lookupSeriesStroke(series));
                    g2.setPaint(this.lookupSeriesPaint(series));
                    g2.draw(s.seriesPath);
                }
            } else if (item != 0 && itemVisible) {
                // обычная отрисовка
                if (percent < max_percent) {
                    xx = dataset.getXValue(series, item - 1);
                    yy = dataset.getYValue(series, item - 1);
                }
                // разряжаем в зависимости от масштаба
                else if (item % precision == 0) {
                    xx = dataset.getXValue(series, item - precision);
                    yy = dataset.getYValue(series, item - precision);
                    // берем максимальное значение для yy от всех игнорируемых items сзади
                    for (int i = 1; i < precision; i++) {
                        yy = yy > 0 ? Math.min(yy, dataset.getYValue(series, item - i)) : Math.max(yy, dataset.getYValue(series, item - i));
                    }
                }
                if (!java.lang.Double.isNaN(xx) && !java.lang.Double.isNaN(yy)) {
                    boolean drawLine = true;
                    if (this.getPlotDiscontinuous()) {
                        rangeAxisIndex = dataset.getItemCount(series);
                        double minX = dataset.getXValue(series, 0);
                        double maxX = dataset.getXValue(series, rangeAxisIndex - 1);
                        if (this.getGapThresholdType() == UnitType.ABSOLUTE) {
                            drawLine = Math.abs(x1 - xx) <= this.getGapThreshold();
                        } else {
                            drawLine = Math.abs(x1 - xx) <= (maxX - minX) / (double)rangeAxisIndex * this.getGapThreshold();
                        }
                    }

                    if (drawLine) {
                        double transX0 = domainAxis.valueToJava2D(xx, dataArea, xAxisLocation);
                        double transY0 = rangeAxis.valueToJava2D(yy, dataArea, yAxisLocation);
                        d = transX0 - transX0_pre;
                        d_y += d;
                        if (Math.abs(d_y) > 5.0) {

                            d_y = 0;
                        }
                      //  else return;
                        transX0_pre = transX0;

                        if (java.lang.Double.isNaN(transX0) || java.lang.Double.isNaN(transY0) || java.lang.Double.isNaN(transX1) || java.lang.Double.isNaN(transY1)) {
                            return;
                        }

                        if (orientation == PlotOrientation.HORIZONTAL) {
                            state.workingLine.setLine(transY0, transX0, transY1, transX1);
                        } else if (orientation == PlotOrientation.VERTICAL) {
                            // обычная отрисовка
                            if (percent < max_percent) {
                                state.workingLine.setLine(transX0, transY0, transX1, transY1);
                            }
                            // прорисовка зависит от масштаба
                            else if (item % precision == 0) {
                                // выравниваем наклонную линию
//                                if (transX1 - transX0 > 20) {
//                                    transY1 = transY0;
//                                }
                                state.workingLine.setLine(transX0, transY0, transX1, transY1);
                            }
                        }

                        if (state.workingLine.intersects(dataArea)) {
                            // обычная отрисовка
                            if (percent < max_percent) {
                                g2.draw(state.workingLine);
                            }
                            // прорисовка зависит от масштаба
                            else if (item % precision == 0) {
                                g2.draw(state.workingLine);
                            }
                        }
                    }
                }
            }
        }

        if (itemVisible) {
            if (this.getBaseShapesVisible()) {
                Shape shape = this.getItemShape(series, item);
                if (orientation == PlotOrientation.HORIZONTAL) {
                    shape = ShapeUtilities.createTranslatedShape(shape, transY1, transX1);
                } else if (orientation == PlotOrientation.VERTICAL) {
                    shape = ShapeUtilities.createTranslatedShape(shape, transX1, transY1);
                }

                if (shape.intersects(dataArea)) {
                    if (this.getItemShapeFilled(series, item)) {
                        g2.fill(shape);
                    } else {
                        g2.draw(shape);
                    }
                }

                entityArea = shape;
            }

            if (this.getPlotImages()) {
                Image image = this.getImage(plot, series, item, transX1, transY1);
                if (image != null) {
                    Point hotspot = this.getImageHotspot(plot, series, item, transX1, transY1, image);
                    g2.drawImage(image, (int)(transX1 - hotspot.getX()), (int)(transY1 - hotspot.getY()), (ImageObserver)null);
                    entityArea = new java.awt.geom.Rectangle2D.Double(transX1 - hotspot.getX(), transY1 - hotspot.getY(), (double)image.getWidth((ImageObserver)null), (double)image.getHeight((ImageObserver)null));
                }
            }

            xx = transX1;
            yy = transY1;
            if (orientation == PlotOrientation.HORIZONTAL) {
                xx = transY1;
                yy = transX1;
            }

            if (this.isItemLabelVisible(series, item)) {
                this.drawItemLabel(g2, orientation, dataset, series, item, xx, yy, y1 < 0.0D);
            }

            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            this.updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex, rangeAxisIndex, transX1, transY1, orientation);
            if (entities != null && isPointInRect(dataArea, xx, yy)) {
                this.addEntity(entities, (Shape)entityArea, dataset, series, item, xx, yy);
            }
        }
    }

};


