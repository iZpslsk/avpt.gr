package avpt.gr.maps;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;
import avpt.gr.blocks32.asim.Block32_C4_0;
import avpt.gr.blocks32.overall.Block32_21_9;
import avpt.gr.graph.ChartArm;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.util.ArrayList;

import static avpt.gr.common.UtilsArmG.profNameFont;
import static avpt.gr.graph.ChartArm.PROF_LABEL;

/**
 * список профилей
 */
public class Profiles {

    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private int last_second;
    private long last_coordinate;
    final private BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    final private Color colorLine = new Color(0x00FF84DF,true);
    final private Color colorFill = new Color(0x86FFB29C, true);

    // профиль
    public static class Profile {
        private int second;
        private int second_end = -1;
        private double slope;
        private double length;
        private long coordinate;
        private long coordinate_end;

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getSecond_end() {
            return second_end;
        }

        public double getLen() {
            return length;
            //return Math.abs(coordinate_end - coordinate) < 1000 ? coordinate_end - coordinate : Double.NaN;
        }

        public double getSlope() {
            return slope;
        }

        public double getLength() {
            return length;
        }

        public void setLen(double length) {
            this.length = length;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public Profile (int second, double slope, long coordinate) {
            this.second = second;
            this.slope = slope;
            this.coordinate = coordinate;
        }

        public static boolean isProfile(int [] arrId) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i] == 1) return true;
            }
            return false;
        }
    }

    public Profiles(ArrBlock32 arrBlock32) {
        Block32 block32_prev = null;
        Block32 block32_cur = null;
       // Limits.Limit limit = null;
        Profiles.Profile profile = null;

        Profile prev_prof = null;
        Profile cur_prof = null;
        for (int i = 0; i < arrBlock32.size(); i++) {

            if (arrBlock32.get(i).getId() == 0xC4) {
                int subId = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (subId == 0x00) {
                    Block32 block32 = arrBlock32.get(i);
                    last_second = block32.getSecond(); // последняя секунда
                    Block32_C4_0 block32_c4_0 = new Block32_C4_0(block32.getValues());
                    last_coordinate = block32_c4_0.getCoordinate();
                    int [] arrId = block32_c4_0.getArrId();

                    if (Profiles.Profile.isProfile(arrId)) {
                        prev_prof = cur_prof;
                        cur_prof = new Profiles.Profile(block32.getSecond(),
                                block32_c4_0.getSlope(),
                                block32_c4_0.getCoordinate());
                        if (prev_prof != null) {
                            int diff = (int) (block32_c4_0.getCoordinate() - prev_prof.getCoordinate());
                            prev_prof.length = Math.abs(diff) < 1000 ? diff : Double.NaN;
                        }
                        profiles.add(cur_prof);
                    }
                }
            }

            if (arrBlock32.get(i).getId() == 0x21) {
                int subId = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (subId == 0x09) {
                    Block32 block32 = arrBlock32.get(i);
                    last_second = block32.getSecond(); // последняя секунда
                    Block32_21_9 block32_21_9 = new Block32_21_9(block32.getValues());
                    last_coordinate = block32_21_9.getCoordinate();
                    int [] arrId = block32_21_9.getArrId();

                    if (Profiles.Profile.isProfile(arrId)) {
                        prev_prof = cur_prof;
                        cur_prof = new Profiles.Profile(block32.getSecond(),
                                block32_21_9.getSlope(),
                                block32_21_9.getCoordinate());
                        if (prev_prof != null) {
                            int diff = (int) (block32_21_9.getCoordinate() - prev_prof.getCoordinate());
                            prev_prof.length = Math.abs(diff) < 1000 ? diff : Double.NaN;
                        }
                        profiles.add(cur_prof);
                    }
                }
            }

//            if (arrBlock32.isP()) {
//                switch (arrBlock32.get(i).getId()) {
//                    case 0x1D:
//                    case 0x2D:
//                    case 0x6D:
//                    case 0x9D:
//                    case 0x71:
//                    case 0x44:
//                        int subId = Block32.getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
//                        if (subId == 0x04) {
//                            block32_cur = arrBlock32.get(i);
//                            last_second = block32_cur.getSecond();
//                            Block32_map_4 block32_map_4_cur = new Block32_map_4(block32_cur.getValues());
//                            last_coordinate = block32_map_4_cur.getCoordinate();
//
//                            if (profile == null)
//                                profile = new Profiles.Profile(
//                                        block32_cur.getSecond(),
//                                        block32_map_4_cur.getCurSlope(),
//                                        block32_map_4_cur.getCoordinate());
//
//                            if (profile.getSlope() != block32_map_4_cur.getCurSlope()) {
//                                if (block32_prev != null)
//                                    profile.setLen(block32_cur.getSecond() - profile.getSecond());
//                                profiles.add(profile);
//                                profile = null;
//                            }
//                        }
//                }
//                block32_prev = block32_cur;
//            }
        }
        //if (profile != null) profiles.add(profile);
    }

    public void addAnnotationProfile(XYPlot plot, boolean isText) {
        final int MAX_LEN = 1000;
        String label = plot.getRangeAxis().getLabel();
        if (!label.equals(PROF_LABEL)) return;

        for (int i = 1; i < profiles.size(); i++) {
            Profile profile_prev = profiles.get(i - 1);
            profile_prev.second_end = profiles.get(i).second;
            profile_prev.coordinate_end = profiles.get(i).coordinate;

            // если длина профиля 0 и уклон совпадает, значит соединяем в один
            if (profile_prev.coordinate == profile_prev.coordinate_end && profile_prev.slope == profiles.get(i).slope) {
                profiles.get(i).second = profile_prev.second;
                profile_prev.coordinate = profiles.get(i).coordinate;
                profile_prev.coordinate_end = profiles.get(i).coordinate_end;
            }

            int len = profile_prev.second_end - profile_prev.second;
            if (len > MAX_LEN) continue; // пропускаем подозрительно длинные профили
            makeAnnotation(plot, isText, profile_prev, len);

        }
        if (!profiles.isEmpty()) {
            Profile profile_last = profiles.get(profiles.size() - 1);
            profile_last.second_end = last_second;
            profile_last.coordinate_end = last_coordinate;
            int len = profile_last.second_end - profile_last.second;
            if (len < MAX_LEN)
                makeAnnotation(plot, isText, profile_last, len);
        }
    }

    private void makeAnnotation(XYPlot plot, boolean isText, Profiles.Profile profile_prev, int len) {
        final int H = 2;
        double[] polygon;
        double slope = profile_prev.slope;
        if (slope > 0) {
            polygon = new double[] {
                    profile_prev.getSecond(), 0,
                    profile_prev.getSecond() + len, H,
                    profile_prev.getSecond() + len, 0
            };
        }
        else if (slope < 0) {
            polygon = new double[] {
                    profile_prev.getSecond(), 0,
                    profile_prev.getSecond(), H,
                    profile_prev.getSecond() + len, 0
            };
        }
        else {
            polygon = new double[] {
                    profile_prev.getSecond(), 0,
                    profile_prev.getSecond(), H / 2.0,
                    profile_prev.getSecond() + len, H / 2.0,
                    profile_prev.getSecond() + len, 0
            };
        }
        XYAnnotation a = new XYPolygonAnnotation(polygon, stroke, colorLine, colorFill);
        XYTextAnnotation t = new XYTextAnnotation(Double.toString(profile_prev.getSlope() / 100),
                profile_prev.getSecond() + (profile_prev.getSecond_end() - profile_prev.getSecond()) / 2.0, 0.8);
        t.setFont(profNameFont);
        t.setPaint(new Color(0xB8EEFAFF, true));

        plot.addAnnotation(a);
        if (isText) {
            plot.addAnnotation(t);
        }
    }

    // профиль
    public Profile getProfile(int index) {
        Profile p = null;
        if (profiles != null && index >= 0 && index < profiles.size())
            p = profiles.get(index);
        return p;
    }

    public int size() {
        return profiles.size();
    }

    /**
     * @param x -
     * @return длина профиля
     */
    public static double getLenProfile(ChartArm chartArm, double x) {

        int[] val = null;
        while (val == null && x > 0) {
            val = chartArm.getChartDataset().getValProfile((int)x);
            --x;
        }
        if (val != null && val[0] == 1) {

            Profiles.Profile profile = chartArm.getProfiles().getProfile(val[1]);
            if (profile != null && profile.getLen() > 0) {
                return profile.getLen();
            }
        }
        return Double.NaN;
    }

    /**
     * @param x -
     * @return длина профиля
     */
    public static double getSlopeProfile(ChartArm chartArm, double x) {
        int[] val = null;
        while (val == null && x > 0) {
            val = chartArm.getChartDataset().getValProfile((int)x);
            --x;
        }
        if (val != null && val[0] == 1) {

            Profiles.Profile profile = chartArm.getProfiles().getProfile(val[1]);
            if (profile != null) {
                return profile.getSlope() / 100;
            }
        }
        return Double.NaN;
    }
}
