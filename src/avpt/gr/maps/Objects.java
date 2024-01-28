package avpt.gr.maps;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32_gp;
import avpt.gr.blocks32.asim.Block32_C4_0;
import avpt.gr.blocks32.overall.Block32_21_4;
import avpt.gr.blocks32.overall.Block32_21_9;
import org.jfree.chart.annotations.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static avpt.gr.blocks32.SubIdGr.getSubId;
import static avpt.gr.chart_dataset.ChartArrays.LINE_MAP;

/**
 * список путевых объектов
 */
public class Objects {

    private final ImageIcon iconTrafficLight = new ImageIcon(getClass().getResource("/avpt/gr/images/map/trafficlight.png"));
    private final ImageIcon iconThermometer = new ImageIcon(getClass().getResource("/avpt/gr/images/map/thermometer.png"));
    private final ImageIcon iconNeutralInsert = new ImageIcon(getClass().getResource("/avpt/gr/images/map/neutral_insert.png"));
    private final ImageIcon iconCrossing = new ImageIcon(getClass().getResource("/avpt/gr/images/map/crossing.png"));
    private final ImageIcon iconBrake = new ImageIcon(getClass().getResource("/avpt/gr/images/map/brake.png"));
    private final ImageIcon iconLandslide = new ImageIcon((getClass().getResource("/avpt/gr/images/map/landslide.png")));
    private final ImageIcon iconMarkerPink = new ImageIcon((getClass().getResource("/avpt/gr/images/map/markerPink16.png")));
    private final ImageIcon iconMarkerChartre = new ImageIcon((getClass().getResource("/avpt/gr/images/map/markerChartre16.png")));
    private ArrayList<Traffic_light> traffic_light = new ArrayList<Traffic_light>();
    private ArrayList<Thermometer> thermometer = new ArrayList<Thermometer>();
    private ArrayList<NeutralInsert> neutral_insert = new ArrayList<NeutralInsert>();
    private ArrayList<Crossing> crossing = new ArrayList<Crossing>();
    private ArrayList<CheckBrake> check_brake = new ArrayList<CheckBrake>();
    private ArrayList<Landslide> landslide = new ArrayList<Landslide>();
    private ArrayList<CorrectCoordinate> correct_coordinate = new ArrayList<CorrectCoordinate>();

   // private int prev_coordinate = -1;
    private Block32_gp prev_block32_gp = null;

    // Проверка тормозов
    public static class CheckBrake {
        int second;
        long len;
        int speed;
        long coordinate;

        public CheckBrake(int second, long len, int speed, long coordinate) {
            this.second = second;
            this.len = len;
            this.speed = speed;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public long getLen() {
            return len;
        }

        public int getSpeed() {
            return speed;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isCheckBrake(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 6) return true;
            }
            return false;
        }
    }

    // Переезд
    public static class Crossing {
        int second;
        int speed;
        long coordinate;

        public Crossing(int second, int speed, long coordinate) {
            this.second = second;
            this.speed = speed;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public int getSpeed() {
            return speed;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isCrossing(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 9) return true;
            }
            return false;
        }
    }

    // Нейтральная вставка
    public static class NeutralInsert {
        int second;
        long len;
        long coordinate;

        public NeutralInsert(int second, long len, long coordinate) {
            this.second = second;
            this.len = len;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public long getLen() {
            return len;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isNeutralInsert(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 4) return true;
            }
            return false;
        }
    }

    // ПОНАБ
    public static class Thermometer {
        int second;
        long coordinate;

        public Thermometer(int second, long coordinate) {
            this.second = second;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isThermometer(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 10) return true;
            }
            return false;
        }
    }

    // светофор
    public static class Traffic_light {
        int second;
        int speed;
        int type;
        long coordinate;

        public Traffic_light(int second, int speed, int type, long coordinate) {
            this.second = second;
            this.speed = speed;
            this.type = type;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public int getSpeed() {
            return speed;
        }

        public int getType() {
            return type;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isTrafficLight(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 7) return true;
            }
            return false;
        }
    }

    // Обрывоопастные места
    public static class Landslide {
        int second;
        long len;
        long coordinate;

        public Landslide(int second, long len, long coordinate) {
            this.second = second;
            this.len = len;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public long getLen() {
            return len;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public static boolean isLandslide(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 19) return true;
            }
            return false;
        }
    }

    public static class CorrectCoordinate {
        int second;
        long coordinate;
        // корректировка координат - true, скачек координат - false
        boolean correct;

        public CorrectCoordinate(int second, long coordinate, boolean correct) {
            this.second = second;
            this.coordinate = coordinate;
            this.correct = correct;
        }

        public int getSecond() {
            return second;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public boolean isCorrect() {
            return correct;
        }

        //        public static boolean isCorrectCoordinate(int [] arrId) {
//            for (int i = 0; i < arrId.length; i++) {
//                if (arrId[i] == 50) return true;
//            }
//            return false;
//        }
    }

    /**
     * в конструкторе заполняем список путевых объектов
     * @param arrBlock32 - массив ArrBlock32
     */
    public Objects(ArrBlock32 arrBlock32) {
        for (int i = 0; i < arrBlock32.size(); i++) {
            if (arrBlock32.get(i).getId() == 0xC4) {
                int subId = getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (subId == 0x00) {
                    Block32_gp block32_gp = arrBlock32.get(i);
                    Block32_C4_0 block32_c4_0 = new Block32_C4_0(block32_gp.getValues());
                    int [] arrId = block32_c4_0.getArrId();
                    if (Traffic_light.isTrafficLight(arrId))
                        traffic_light.add(new Traffic_light(block32_gp.getSecond(),
                                block32_c4_0.getSpeed(),
                                block32_c4_0.getTypeTrafficLight(),
                                block32_c4_0.getCoordinate()));

                    if (Thermometer.isThermometer(arrId))
                        thermometer.add(new Thermometer(block32_gp.getSecond(),
                                block32_c4_0.getCoordinate()));

                    if (NeutralInsert.isNeutralInsert(arrId))
                        neutral_insert.add(new NeutralInsert(block32_gp.getSecond(),
                                block32_c4_0.getLenNeutral(),
                                block32_c4_0.getCoordinate()));

                    if (Crossing.isCrossing(arrId))
                        crossing.add(new Crossing(block32_gp.getSecond(),
                                block32_c4_0.getSpeed(),
                                block32_c4_0.getCoordinate()));

                    if (CheckBrake.isCheckBrake(arrId))
                        check_brake.add(new CheckBrake(block32_gp.getSecond(),
                                block32_c4_0.getLenTryBake(),
                                block32_c4_0.getSpeed(),
                                block32_c4_0.getCoordinate()));

                    if (Landslide.isLandslide(arrId))
                        landslide.add(new Landslide(block32_gp.getSecond(),
                                block32_c4_0.getLenDanger(),
                                block32_c4_0.getCoordinate()));

                }
            }

            if (arrBlock32.get(i).getId() == 0x21) {
                int subId = getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (subId == 0x09) {
                    Block32_gp block32_gp = arrBlock32.get(i);
                    Block32_21_9 block32_21_9 = new Block32_21_9(block32_gp.getValues());
                    int [] arrId = block32_21_9.getArrId();

                    if (Traffic_light.isTrafficLight(arrId))
                        traffic_light.add(new Traffic_light(block32_gp.getSecond(),
                                    block32_21_9.getSpeed(),
                                    block32_21_9.getTypeTrafficLight(),
                                    block32_21_9.getCoordinate()));

                    if (Thermometer.isThermometer(arrId))
                        thermometer.add(new Thermometer(block32_gp.getSecond(),
                                block32_21_9.getCoordinate()));

                    if (NeutralInsert.isNeutralInsert(arrId))
                        neutral_insert.add(new NeutralInsert(block32_gp.getSecond(),
                                block32_21_9.getLenNeutral(),
                                block32_21_9.getCoordinate()));

                    if (Crossing.isCrossing(arrId))
                        crossing.add(new Crossing(block32_gp.getSecond(),
                                block32_21_9.getSpeed(),
                                block32_21_9.getCoordinate()));

                    if (CheckBrake.isCheckBrake(arrId))
                        check_brake.add(new CheckBrake(block32_gp.getSecond(),
                                block32_21_9.getLenTryBake(),
                                block32_21_9.getSpeed(),
                                block32_21_9.getCoordinate()));

                    if (Landslide.isLandslide(arrId))
                        landslide.add(new Landslide(block32_gp.getSecond(),
                                block32_21_9.getLenDanger(),
                                block32_21_9.getCoordinate()));
                }
                if (subId == 0x04) {
                    Block32_gp block32_gp = arrBlock32.get(i);
                    Block32_21_4 block32_21_4 = new Block32_21_4(block32_gp.getValues());
                    if (block32_21_4.getCorrectCoordinate() > 0) { // корректировка
                        correct_coordinate.add(new CorrectCoordinate(block32_gp.getSecond(), block32_gp.getCoordinate(), true));
                    } // скачок
                    else if (prev_block32_gp != null) {
                        LocalDateTime cur = block32_gp.getDateTime();
                        LocalDateTime pre = prev_block32_gp.getDateTime();
                        long duration = cur != null && pre != null ? Duration.between(pre, cur).getSeconds() : -1;
                        if (duration > 7) {
                            correct_coordinate.add(new CorrectCoordinate(prev_block32_gp.getSecond(), block32_gp.getCoordinate(), false));
                        }
//                        int d_km = Math.abs(block32_gp.getKm() - prev_block32_gp.getKm());
//                        int d_pk = block32_gp.getPk() - prev_block32_gp.getPk();
//                        // исключаем повтор на следующей секунде
//                        if (!correct_coordinate.isEmpty() && (block32_gp.getSecond() - correct_coordinate.get(correct_coordinate.size() - 1).getSecond() > 1)) {
//                            if (d_km == 0 && Math.abs(d_pk) > 1) {
//                                correct_coordinate.add(new CorrectCoordinate(block32_gp.getSecond(), block32_gp.getCoordinate(), false));
//                            } else if (d_km == 1 && d_pk + (prev_block32_gp.getPk()) > 1) {
//                                correct_coordinate.add(new CorrectCoordinate(block32_gp.getSecond(), block32_gp.getCoordinate(), false));
//                            } else if (d_km > 1) {
//                                correct_coordinate.add(new CorrectCoordinate(block32_gp.getSecond(), block32_gp.getCoordinate(), false));
//                            }
//                        }
                    }
                    prev_block32_gp = block32_gp;
                }
            }
        }
    }

    public void addAnnotationsTrafficLight(XYPlot plot) {
        final double d = 6;
        for (int i = 0; i < traffic_light.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(traffic_light.get(i).getSecond(), LINE_MAP + d, iconTrafficLight.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(traffic_light.get(i).getSecond(), LINE_MAP, traffic_light.get(i).getSecond(), LINE_MAP + d,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    private final double Y = LINE_MAP + 4.5;

    public void addAnnotationThermometer(XYPlot plot) {
        for (int i = 0; i < thermometer.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(thermometer.get(i).getSecond(), Y, iconThermometer.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(thermometer.get(i).getSecond(), LINE_MAP, thermometer.get(i).getSecond(), Y,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    public void addAnnotationNeutralInsert(XYPlot plot) {
        for (int i = 0; i < neutral_insert.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(neutral_insert.get(i).getSecond(), Y, iconNeutralInsert.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(neutral_insert.get(i).getSecond(), LINE_MAP, neutral_insert.get(i).getSecond(), Y,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    public void addAnnotationCrossing(XYPlot plot) {
        for (int i = 0; i < crossing.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(crossing.get(i).getSecond(), Y, iconCrossing.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(crossing.get(i).getSecond(), LINE_MAP, crossing.get(i).getSecond(), Y,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    public void addAnnotationCheckBrake(XYPlot plot) {
        for (int i = 0; i < check_brake.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(check_brake.get(i).getSecond(), Y, iconBrake.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(check_brake.get(i).getSecond(), LINE_MAP, check_brake.get(i).getSecond(), Y,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    public void addAnnotationLandslide(XYPlot plot) {
        for (int i = 0; i < landslide.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(landslide.get(i).getSecond(), Y, iconLandslide.getImage());
            plot.addAnnotation(a);
            XYAnnotation b = new XYLineAnnotation(landslide.get(i).getSecond(), LINE_MAP, landslide.get(i).getSecond(), Y,
                    new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    public void addAnnotationsCorrectCoordinate(XYPlot plot) {
        for (int i = 0; i < correct_coordinate.size(); i++) {
            XYAnnotation a = null;
            if (correct_coordinate.get(i).isCorrect())
                a = new XYImageAnnotation(correct_coordinate.get(i).getSecond(), 4, iconMarkerChartre.getImage());
            else
                a = new XYImageAnnotation(correct_coordinate.get(i).getSecond(), 4, iconMarkerPink.getImage());
            plot.addAnnotation(a);
        }
    }

    // проверка тормозов
    public CheckBrake getCheckBrake(int index) {
        CheckBrake ch_b = null;
        if (check_brake != null && index > 0 && index < check_brake.size() - 1)
            ch_b = check_brake.get(index);
        return ch_b;
    }
    // светофор
    public Traffic_light getTraffic_lights(int index) {
        Traffic_light tl = null;
        if (traffic_light != null && index > 0 && index < traffic_light.size() - 1)
            tl = traffic_light.get(index);
        return tl;
    }
    // переезд
    public Crossing getCrossing(int index) {
        Crossing cr = null;
        if (crossing != null && index > 0 && index < crossing.size() - 1)
            cr = crossing.get(index);
        return cr;
    }
    // нейтральная вставка
    public NeutralInsert getNeutralInsert(int index) {
        NeutralInsert ni = null;
        if (neutral_insert != null && index > 0 && index < neutral_insert.size() - 1)
            ni = neutral_insert.get(index);
        return ni;
    }
    // понаб
    public Thermometer getThermometer(int index) {
        Thermometer t = null;
        if (thermometer != null && index > 0 && index < thermometer.size() - 1)
            t = thermometer.get(index);
        return t;
    }
}
