package avpt.gr.maps;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32_gp;
import avpt.gr.blocks32.asim.Block32_C5_0;
import avpt.gr.blocks32.asim.Block32_C5_1;
import avpt.gr.blocks32.overall.Block32_1D_9;
import avpt.gr.blocks32.overall.Block32_1D_A;
import avpt.gr.blocks32.overall.Block32_21_4;
import avpt.gr.common.UtilsArmG;
import org.jfree.chart.annotations.*;
import org.jfree.chart.plot.XYPlot;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static avpt.gr.blocks32.SubIdGr.getSubId;
//import static avpt.gr.blocks32.SubIdGr.getSubId_ASIM;
import static avpt.gr.chart_dataset.ChartArrays.LINE_MAP;
import static avpt.gr.common.UtilsArmG.stationNameFont;

/**
 * список станций
 */
public class Stations {
    private final ImageIcon iconStation = new ImageIcon(getClass().getResource("/avpt/gr/images/map/station.png"));

    private final ArrayList<Station> stations = new ArrayList<Station>();

    public static class Station {
        int index;
        int second;
        int id;
        long ecp;
        int type;
        long timeArrivalSchedule;
        long timeArrivalFact;
        long timeDepartureSchedule;
        long timeDepartureFact;
        long timeShiftSchedule;
        long coordinate;
        String nameStation;
        int percentAuto;

        /**
         * станция
         * @param second - секунда
         * @param id - номер перегона
         * @param ecp - еср код
         * @param type - тип станции
         * @param timeArrivalSchedule - время прибытия по расписанию (сек сначала суток)
         * @param timeArrivalFact - время прибытия фактическое (сек сначала суток)
         * @param timeDepartureSchedule - время отправления по распиманию (сек сначала суток)
         * @param timeDepartureFact - время отправления фактическое (сек сначала суток)
         * @param timeShiftSchedule - время сдвига расписания (секунд)
         * @param coordinate - линейная координата (пробег сначала маршрута м.)
         */
        public Station(int index, int second, int id, long ecp, int type,
                       long timeArrivalSchedule, long timeArrivalFact,
                       long timeDepartureSchedule, long timeDepartureFact,
                       long timeShiftSchedule, long coordinate) {
            this.index = index;
            this.second = second;
            this.id = id;
            this.ecp = ecp;
            this.type = type;
            this.timeArrivalSchedule = timeArrivalSchedule;
            this.timeArrivalFact = timeArrivalFact;
            this.timeDepartureSchedule = timeDepartureSchedule;
            this.timeDepartureFact = timeDepartureFact;
            this.timeShiftSchedule = timeShiftSchedule;
            this.coordinate = coordinate;
        }

        public int getSecond() {
            return second;
        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public long getEcp() {
            return ecp;
        }

        public int getType() {
            return type;
        }

        public long getTimeArrivalSchedule() {
            return timeArrivalSchedule;
        }

        public long getTimeArrivalFact() {
            return timeArrivalFact;
        }

        public long getTimeDepartureSchedule() {
            return timeDepartureSchedule;
        }

        public long getTimeDepartureFact() {
            return timeDepartureFact;
        }

        public long getTimeShiftSchedule() {
            return timeShiftSchedule;
        }

        public long getCoordinate() {
            return coordinate;
        }

        public String getNameStation() {
            return nameStation;
        }

        public void setNameStation(String nameStation) {
            this.nameStation = nameStation;
        }

        public int getPercentAuto() {
            return this.percentAuto;
        }
    }

    /**
     * в конструкторе заполняем список станций
     * @param arrBlock32 - массив ArrBlock32
     */
    public Stations(ArrBlock32 arrBlock32) {

        int cnt_auto = 0;
        int cnt_usavp = 0;
        int index_station = 0; // индекс станции в списке (для формирования списка станций одного поезда)

        for (int i = 0; i < arrBlock32.size(); i++) {

            if (arrBlock32.get(i).getId() == 0x21) {
                int curSubId_21 = getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (curSubId_21 == 0x04) {
                    Block32_21_4 block32_21_4 = new Block32_21_4(arrBlock32.get(i).getValues());

                    if (block32_21_4.getSpeed() > 0) {
                        if (block32_21_4.getAuto() == 1) ++cnt_auto;
                        ++cnt_usavp;
                    }
                }
            }

            // asim
            if (arrBlock32.get(i).getId()  == 0xC5) {
                int subId = getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
//                int subId = getSubId_ASIM(arrBlock32.get(i).getValues());
                Block32_gp block32_gp = arrBlock32.get(i);
                if (subId == 0) {
                    Block32_C5_0 block32_c5_0 = new Block32_C5_0(block32_gp.getValues());
                    Station station =
                            new Station(index_station,
                                    block32_gp.getSecond(),
                                    block32_c5_0.getStationId(),
                                    block32_c5_0.getECP(),
                                    block32_c5_0.getTypeStation(),
                                    block32_c5_0.getTimeArrivalSchedule(),
                                    block32_c5_0.getTimeArrivalFact(),
                                    block32_c5_0.getTimeDepartureSchedule(),
                                    block32_c5_0.getTimeDepartureFact(),
                                    block32_c5_0.getTimeShiftSchedule(),
                                    block32_c5_0.getCoordinate());
                    station.percentAuto = 0;
                    cnt_auto = 0;
                    cnt_usavp = 0;
                    stations.add(station);
                    index_station++;

                }
                if (subId == 1) {
                    Block32_C5_1 block32_c5_1 = new Block32_C5_1(block32_gp.getValues());
                    if (stations.size() > 0 && block32_c5_1.getStationId() == stations.get(stations.size() - 1).getId()) {
                        stations.get(stations.size() - 1).setNameStation(block32_c5_1.getNameStation());
                    }

                }
            }

            if (arrBlock32.get(i).getId() == 0x1D) {
                int subId = getSubId(arrBlock32.get(i).getId(), arrBlock32.get(i).getValues());
                if (subId == 0x09) {
                    Block32_gp block32_gp = arrBlock32.get(i);
                    Block32_1D_9 block32_1D_9 = new Block32_1D_9(block32_gp.getValues());
                    Station station =
                            new Station(index_station,
                                    block32_gp.getSecond(),
                                    block32_1D_9.getStationId(),
                                    block32_1D_9.getECP(),
                                    block32_1D_9.getTypeStation(),
                                    block32_1D_9.getTimeArrivalSchedule(),
                                    block32_1D_9.getTimeArrivalFact(),
                                    block32_1D_9.getTimeDepartureSchedule(),
                                    block32_1D_9.getTimeDepartureFact(),
                                    block32_1D_9.getTimeShiftSchedule(),
                                    block32_1D_9.getCoordinate());
                    station.percentAuto = cnt_usavp > 0 ? cnt_auto * 100 / cnt_usavp : 0;
                    cnt_auto = 0;
                    cnt_usavp = 0;
                    stations.add(station);
                    index_station++;
                }
                if (subId == 0x0A) {
                    Block32_gp block32_gp = arrBlock32.get(i);
                    Block32_1D_A block32_1D_A = new Block32_1D_A(block32_gp.getValues());
                    if (stations.size() > 0 && block32_1D_A.getStationId() == stations.get(stations.size() - 1).getId()) {
                        stations.get(stations.size() - 1).setNameStation(block32_1D_A.getNameStation());
                    }
                }
            }
        }
    }

    public void addStationsToTrains(ArrTrains arrTrains) {
        if (stations.size() == 0) return;
   //     ArrTrains arrTrains = chartArm.getChartDataset().getArrTrains();
        for (Station station : stations) {
            for (int j = 0; j < arrTrains.size(); j++) {
                Train train = arrTrains.get(j);
                java.util.List<Station> stations = train.getStations();

                int d = 0;
                // станция начала или конца поездки может уходить за диапазон секунд поездки
                // если координата станции - начало маршрута (3500)
                // расширяем диапазон времени для обнаружения станции
                if (station.getCoordinate() == 3500) {
                    d = 1000;
                }
//                System.out.println(train.getCoordinate() + " " + station.getCoordinate() + " " + station.getNameStation());
//                System.out.println(station.getSecond() + "_" + train.getSecondStart() + "_" + station.getNameStation());
                if (station.getSecond() >= train.getSecondStart() - d && station.getSecond() <= train.getSecondsEnd() + d) {
                    stations.add(station);
                }
            }
        }
    }

    /**
     * визуализация станций
     * @param plot XYPlot
     */
    public void setAnnotationsToPlot(XYPlot plot) {
        for (int i = 0; i < stations.size(); i++) {
            XYAnnotation a = new XYImageAnnotation(stations.get(i).getSecond(), 2.3, iconStation.getImage());
            plot.addAnnotation(a);
            String nameStation = stations.get(i).getNameStation();
            if (nameStation != null) {
                XYTextAnnotation t = new XYTextAnnotation(UtilsArmG.getFirstLaterUpper(stations.get(i).getNameStation()), stations.get(i).getSecond(), 0.5);
                t.setFont(stationNameFont);
                t.setPaint(new Color(0xB0B2F1FF, true));
                plot.addAnnotation(t);
            }

            XYAnnotation b = new XYLineAnnotation(stations.get(i).getSecond(), LINE_MAP, stations.get(i).getSecond(), LINE_MAP + 0.3,
                    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER), Color.WHITE);
            plot.addAnnotation(b);
        }
    }

    /**
     * @param start - секунда начала
     * @param end - секунда конец
     * @return - список станций ограниченый секундами
     */
    public ArrayList<Station> getStations(int start, int end) {
        ArrayList<Station> list = new ArrayList<Station>();
        for (int i = 0; i < this.stations.size(); i++) {
            Stations.Station station = getStation(i);
            if (station.getSecond() >= start && station.getSecond() <= end) {
                list.add(station);
            }
        }
        return list;
    }

    public Station getStation(int index) {
        return stations.get(index);
    }

    public int size() {
        return stations.size();
    }
}
