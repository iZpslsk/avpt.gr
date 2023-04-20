package avpt.gr.maps;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;
import avpt.gr.blocks32.overall.Block32_21_9;
import avpt.gr.graph.ChartArm;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static avpt.gr.graph.ChartArm.SPEED_LABEL;


//import static avpt.gr.graph.NamesSubplot.LIMIT_LABEL;

/**
 * список ограниченй карты
 */
public class Limits {

    public static Color COLOR_LIM_MAP = new Color(0x3BFF84DF, true);
    private final ArrayList<Limit> limits = new ArrayList<Limit>();
    private final java.util.List<XYAnnotation> annotations = new ArrayList<XYAnnotation>();
    private int last_second;
    private long last_coordinate;

    //
    public static class Limit {
        int second;
        int second_end = -1;
        long len;
        double speed;
        long coordinate;
        long coordinate_end;
        int nBl;

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public long getLen() {
            return len;
        }

        public void setLen(long len) {
            this.len = len;
        }

        public int getSecond_end() {
            return second_end;
        }

        public double getSpeed() {
            return speed;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public Limit (int second, double speed, long len, long coordinate, int nBl) {
            this.second = second;
            this.speed = speed;
            this.len = len;
            this.coordinate = coordinate;
            this.nBl = nBl;
        }

        public static boolean isLimit(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 3) return true;
            }
            return false;
        }
    }

    public Limits(ArrBlock32 arrBlock32) {
//        Block32 block32_prev = null;
//        Block32 block32_cur = null;
//        Limits.Limit limit = null;

        for (int i = 0; i < arrBlock32.size(); i++) {

            if (arrBlock32.isG())
                if (arrBlock32.get(i).getId() == 0x21) {
                    int subId = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                    if (subId == 0x09) {
                        Block32 block32 = arrBlock32.get(i);
                        last_second = block32.getSecond(); // последняя секунда
                        Block32_21_9 block32_21_9 = new Block32_21_9(block32.getValues());
                        last_coordinate = block32_21_9.getCoordinate();
                        int [] arrId = block32_21_9.getArrId();

                        if (Limits.Limit.isLimit(arrId)) {
                            limits.add(new Limits.Limit(block32.getSecond(),
                                    block32_21_9.getLimSpeed(),
                                    block32_21_9.getLenLimit(),
                                    block32_21_9.getCoordinate(), i));
                        }
                    }
                }
        }
//        if (limit != null) limits.add(limit);
    }

    public void addLimitsToTrains(ArrTrains arrTrains) {
//        String label = plot.getRangeAxis().getLabel();
//        if (!label.equals(SPEED_LABEL) || limits.size() == 0) return;
        if (limits.size() == 0) return;
//        ArrTrains arrTrains = chartArm.getChartDataset().getArrTrains();
        for (int i = 0; i < limits.size(); i++) {
            Limit limit = limits.get(i);
            for (int j = 0; j < arrTrains.size(); j++) {
                Train train = arrTrains.get(j);
                java.util.List<Limit> limits_train =  train.getLimits_c();
                if (i < limits.size() - 1) {
                    limit.second_end = limits.get(i + 1).second;
                }
                else {
                    limit.second_end = train.getSecondsEnd();
                }

                if (limit.second <= train.getSecondsEnd() && limit.getSecond_end() > train.getSecondStart()) {
                    limits_train.add(limit);
 //                   System.out.println(j + ": " + limit.getSecond() + "<=" + avpt.gr.train.getSecondsEnd() + " && " + limit.getSecond_end() + ">" + avpt.gr.train.getSecondStart());
                }

//                if (avpt.gr.train.getSecondsEnd() >= limit.second && avpt.gr.train.getSecondStart() < limit.second_end) {
//                    limits_train.add(limit);
//                    System.out.println(limit.getSecond());
//                }
            }

        }
//        for (int i = 0; i < arrTrains.size(); i++) {
//            Train avpt.gr.train = arrTrains.get(i);
//            int trSecondStart = avpt.gr.train.getSecondStart();
//            int trSecondEnd = avpt.gr.train.getSecondsEnd();
//            for (int j = 0; j < limits.size(); j++) {
//                Limit limit = limits.get(j);
//                int limSecondStart = limit.second;
//                int limSecondEnd = limit.second_end;
//                if (trSecondEnd > limSecondStart && trSecondEnd < limSecondEnd) {
//                    System.out.println(avpt.gr.train.getBlStart() + "_" + limit.speed);
//                }
//            }
//            //System.out.println(avpt.gr.train.getBlStart());
//        }
    }

    public void addAnnotationLimit(ChartArm chartArm, XYPlot plot) {
        String label = plot.getRangeAxis().getLabel();
        if (!label.equals(SPEED_LABEL) || limits.size() == 0) return;
        Limit limit_prev = null;
        if (limits.size() > 1) {
            for (int i = 1; i < limits.size(); i++) {
                limit_prev = limits.get(i - 1);
                limit_prev.second_end = limits.get(i).second;
                limit_prev.coordinate_end = limits.get(i).coordinate;

                int len = limit_prev.second_end - limit_prev.second;
                XYShapeAnnotation a = new XYShapeAnnotation(
                        new Rectangle2D.Double(limit_prev.second, limit_prev.speed, len, 100),
                        new BasicStroke(1f), new Color(0x0FF84DF, true), COLOR_LIM_MAP);

                Train train = chartArm.getChartDataset().getArrTrains().getTrain(
                        chartArm.getChartDataset().getArrBlock32(), limit_prev.second);
                if (train != null) {
                //    plot.addAnnotation(a);
                    annotations.add(a);
                    train.incCLim();
                }
            }
        }
        // обработка последнего ограничения
        Limit limit_last = limits.get(limits.size() - 1);
        limit_last.second_end = last_second;
        limit_last.coordinate_end = last_coordinate;
        int len = limit_last.second_end - limit_last.second;
        XYShapeAnnotation a = new XYShapeAnnotation(
                new Rectangle2D.Double(limit_last.second, limit_last.speed, len, 100),
                new BasicStroke(1f), new Color(0x0FF84DF,true), COLOR_LIM_MAP);
        //plot.addAnnotation(a);
        annotations.add(a);
    }

    // ограничение
    private Limit getLimit(int index) {
        Limit l = null;
        if (index >= 0 && index < limits.size())
            l = limits.get(index);
        return l;
    }

    public static double getMapLimit(ChartArm chartArm, int x) {
        ArrayList<Limit> limits = chartArm.getLimits().limits;
        for (int i = limits.size() - 1; i >= 0; i--) {
            Limit limit = limits.get(i);
            if (limit.getSecond() <= x)
                return limit.getSpeed();
        }
        return Double.NaN;
    }

    public static double getMapLimit_old(ChartArm chartArm, int x) {
        int[] val = null;
        while (val == null && x > 0 /*&& startSecond < x*/) {
            val = chartArm.getChartDataset().getValLimit(x);
            --x;
        }
        if (val != null && val[0] == 3) {

            Limits.Limit limit = chartArm.getLimits().getLimit(val[1]);
            if (limit != null) {
                return limit.getSpeed();
            }
        }
        return Double.NaN;
    }

    public List<XYAnnotation> getAnnotations() {
        return annotations;
    }
}
