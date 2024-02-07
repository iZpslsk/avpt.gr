package avpt.gr.common;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.UnitType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

public class StandardXYItemRendererArm extends StandardXYItemRenderer {
    private int precision;
    private UtilsArmG.MutableDouble mutable_duration;

    private void setPrecision() {
        int zoom = (int)(mutable_duration.get() / 1500);
        switch (zoom) {
            case 1:
            case 2:
                precision = 1;
                break;
            case 3:
                precision = 2;
                break;
            case 4:
                precision = 3;
                break;
            case 5:
                precision = 6;
                break;
            case 6:
                precision = 8;
                break;
            case 7:
                precision = 10;
                break;
            case 8:
                precision = 15;
                break;
            case 9:
                precision = 20;
                break;
            case 10:
                precision = 25;
                break;
        }
    }

    public void setMutable_duration(UtilsArmG.MutableDouble mutable_duration) {
        this.mutable_duration = mutable_duration;
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
       // super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState, pass);
        setPrecision();
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
        if (item % precision == 0) {
            x1 = dataset.getXValue(series, item);
            y1 = dataset.getYValue(series, item);
        }
        if (java.lang.Double.isNaN(x1) || java.lang.Double.isNaN(y1)) {
            itemVisible = false;
        }

        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
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
                if (item % precision == 0) {
                    xx = dataset.getXValue(series, item - precision);
                    yy = dataset.getYValue(series, item - precision);
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
                        if (java.lang.Double.isNaN(transX0) || java.lang.Double.isNaN(transY0) || java.lang.Double.isNaN(transX1) || java.lang.Double.isNaN(transY1)) {
                            return;
                        }

                        if (orientation == PlotOrientation.HORIZONTAL) {
                            state.workingLine.setLine(transY0, transX0, transY1, transX1);
                        } else if (orientation == PlotOrientation.VERTICAL) {
                            if (item % precision == 0) {
                                state.workingLine.setLine(transX0, transY0, transX1, transY1);
                            }
                        }

                        if (state.workingLine.intersects(dataArea)) {
                            if (item % precision == 0) {
                                if (transX1 - transX0 < 100 || precision < 4)
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


