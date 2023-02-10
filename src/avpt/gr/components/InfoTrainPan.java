package avpt.gr.components;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartArrays;
import avpt.gr.chart_dataset.ItemWag;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartPanelArm;
import avpt.gr.maps.Stations;
import avpt.gr.reports.ExcelReports;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static avpt.gr.common.UtilsArmG.formatDate;
import static avpt.gr.common.UtilsArmG.formatDateTime;
import static avpt.gr.reports.ExcelReports.KindReport.*;
import static avpt.gr.train.Train.VL10;

public class InfoTrainPan extends JEditorPane {

    private final static String HTML = "<html><body>%s</body></html>";
    private final static String TITLE = "<table border=0 cellspacing=5 cellpadding=0><tr><th>%s</th></tr></table>";
    private final static String TABLE = "<table border=0 cellspacing=5 cellpadding=0>%s</table>";
    private final static String TEMP_1COL = "<tr><td align=right width=300><b>%s:</b></td> <td> </td> </tr>";
    private final static String TEMP_2COL = "<tr><td align=right width=300>%s </td><td align=left bgcolor=white color=blue width=250><i>%s</i></Td></tr>";
    private final static String HREF = "<a href=\"%s\" >%s</a>";
    private final static String WEIGHT_COUNT = "<table><tr><td align=center>%s, %s</td></tr></table>";

    private final static String TABLE_WAGS =
            "<table border=1 cellspacing=0 cellpadding=0 bgcolor=White>%s</table>";
    private final static String HEAD_WAGS =
            "<tr align=center bgcolor=#CCCCFF><td>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td></tr>";
    private final static String WAGS =
            "<tr><td style=\"text-align:right\";\"border:1px\";>%s</td>" +
            "<td style=\"width:300px\";\"text-align:center\";>%s</td>" +
            "<td style=\"width:200px\";\"text-align:center\";>%s</td></tr>";

    private final static String TABLE_STATIONS =
                    "<table bgcolor=\"CCCCFF\" border=1 cellspacing=0 cellpadding=3>%s</table>";
    private final static String HEAD_STATIONS =
            "<tr align=center>" +
                "<td rowspan=2>%s</td>" +
                "<td colspan=3>%s</td>" +
                "<td colspan=3>%s</td>" +
                "<td rowspan=2>%s</td>" +
                "</tr>" +

                "<tr align=center>" +
                "<td><span style=\"font-size:10px\">по расп</span></td>" +
                "<td><span style=\"font-size:10px\">фактически</span></td>" +
                "<td><span style=\"font-size:10px\">отклон</span></td>" +
                "<td><span style=\"font-size:10px\">по расп</span></td>" +
                "<td><span style=\"font-size:10px\">фактически</span></td>" +
                "<td><span style=\"font-size:10px\">отклон</span></td>" +
            "</tr>";
    private final static String STATIONS =
            "<tr align=center bgcolor=white>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
                "<td><span style=\"font-size:10px\">%s</span></td>" +
            "</tr>";

    private final static String STYLE_TABLE = "table {width: 550px; font-family: arial;}";
    private final static String STYLE_TH = "th {background-color: #7979FF; color: white; font-size: 20pt;}"; // заголовок
    private final static String STYLE_TD = "td {font-size: 16pt;}";

    private final static String LINK_WAGS = "link_wags";            // идентификатор гиперссылки на список вагонов
    private final static String LINK_SCHEDULE = "link_schedule";    // идентификатор гиперссылки на расписание
    private final static String LINK_STATION = "link_station";    // идентификатор гиперссылки на название станции

    private final Stack<String> stack = new Stack<String>();
    private final ChartPanelArm chartPanelArm;
    private JButton btnBack = null;
    private JButton btnPrint = null;
    private JButton btnVirtCoup = null;
    private JButton btnConsumEnergy = null;
    private JButton btnClose = null;
    private final Train train;
    private ArrTrains trains = null;
    private int indexTrain = -1;
    private ArrBlock32 arrBlock32;
    private ExcelReports.KindReport def_kind_rep = info_tr;

    /**
     * @param chartPanelArm -
     * @param indexTrain - индекс поезда (-1 все поезда)
     */
    public InfoTrainPan(ChartPanelArm chartPanelArm, int indexTrain) {
//        Color color = new Color(0x7979FF);
        this.chartPanelArm = chartPanelArm;
        arrBlock32 = chartPanelArm.getChartDataset().getArrBlock32();
        trains = chartPanelArm.getChartDataset().getArrTrains();
        this.indexTrain = indexTrain;
        train = trains.get(indexTrain);
        setContentType("text/html");
        setEditable(false);

        initDocument();
        addHyperlinkListener(new hyperlinkListener());
        if (indexTrain < 0) showWhole();
        else showTrain();
        setKeyEvent();
    }

    /**
     * вагон
     */
    public static class Wag {

        private final String description;
        private final int weight;
        private final int length;
        private final int n_axel;

        /**
         * конструктор
         * @param id_type - идентификатор типа вагоно
         */
        public Wag(int id_type) {
            switch (id_type) {
                case 1:
                    description = "4-осн. пассажирский вагон";
                    weight = 55;
                    length = 25;
                    n_axel = 4;
                    break;
                case 2:
                    description = "4-осн. крытый вагон";
                    weight = 23;
                    length = 15;
                    n_axel = 4;
                    break;
                case 3:
                    description = "4-осн. вагон-думпкар";
                    weight = 30;
                    length = 12;
                    n_axel = 4;
                    break;
                case 4:
                    description = "4-осн. платформа";
                    weight = 21;
                    length = 15;
                    n_axel = 4;
                    break;
                case 5:
                    description = "4-осн. цистерна";
                    weight = 25;
                    length = 14;
                    n_axel = 4;
                    break;
                case 6:
                    description = "4-осн. полувагон";
                    weight = 22;
                    length = 14;
                    n_axel = 4;
                    break;
                case 7:
                    description = "8-осн. цистерна";
                    weight = 49;
                    length = 21;
                    n_axel = 8;
                    break;
                case 8:
                    description = "4-осн. вагон-рефрижиратор";
                    weight = 45;
                    length = 20;
                    n_axel = 4;
                    break;
                case 9:
                    description = "4-осн. вагон-хоппер";
                    weight = 22;
                    length = 14;
                    n_axel = 4;
                    break;
                default:
                    description = "прочие вагоны";
                    weight = 22;
                    length = 14;
                    n_axel = 4;
            }
        }

        public String getDescription() {
            return description;
        }

        public int getWeight() {
            return weight;
        }

        public int getLength() {
            return length;
        }

        public int getN_axel() {
            return n_axel;
        }
    }

    private void showWags() {
        def_kind_rep = info_wags;
      //  Train avpt.gr.train = chartPanelArm.getChartDataset().getArrTrains().get(indexTrain);
        List<ItemWag> wags = train.getWags();
        StringBuilder wags_content = new StringBuilder();
        int loaded = 0;
        for (ItemWag item : wags) {
            wags_content.append(String.format(WAGS, item.getNum(), new Wag(item.getType()).description, item.getWeight()));
            if (item.getWeight() > 0) loaded ++;
        }
        String title = String.format(TITLE, "Информация о составе поезда №" + train.getNumTrain() + " от " +
                train.getDateTimeStart().format(formatDateTime));
        String count_weight = String.format(WEIGHT_COUNT, "Количество вагонов - <b>" + train.getWags().size(), "</b>вес состава - <b>" + train.getWeightTrain() + "</b> т");
        String loaded_empty = String.format(WEIGHT_COUNT, "(загруженых - <b>" + loaded, "</b>порожних - <b>" + (train.getWags().size() - loaded) + "</b>)");
        String head = String.format(HEAD_WAGS, "№№", "Тип вагона", "Загрузка, т");

        String html = String.format(HTML, title + count_weight + loaded_empty + String.format(TABLE_WAGS, head + wags_content));
        setText(html);
    }

    private void showSchedule() {
        def_kind_rep = info_schedule;
     //   Stations stations = chartPanelArm.getChartArm().getStations();
        String title = String.format(TITLE, "Информация о расписании поезда №" + train.getNumTrain() + " от " +
                train.getDateTimeStart().format(formatDateTime));
//        int start = train.getSecondStart();
//        int end = train.getSecondsEnd();
        StringBuilder stations_content = new StringBuilder();

        List<Stations.Station> stations = train.getStations();
        for (Stations.Station station : stations) {
            int val_arrival = (int)(station.getTimeArrivalFact() - station.getTimeArrivalSchedule());
            int val_departure = (int)(station.getTimeDepartureFact() - station.getTimeDepartureSchedule());
            stations_content.append(String.format(STATIONS,
                    String.format(HREF, station.getIndex(), station.getNameStation()),
                    UtilsArmG.getTime((int)station.getTimeArrivalSchedule()),
                    UtilsArmG.getTime((int)station.getTimeArrivalFact()),
                    String.format("%4.1f", val_arrival / 60.0),
                    UtilsArmG.getTime((int)station.getTimeDepartureSchedule()),
                    UtilsArmG.getTime((int)station.getTimeDepartureFact()),
                    String.format("%4.1f", val_departure / 60.0),
                    station.getPercentAuto()));
        }

//        for (int i = 0; i < stations.size(); i++) {
//            Stations.Station station = stations.getStation(i);
//            int val_arrival = (int)(station.getTimeArrivalFact() - station.getTimeArrivalSchedule());
//            int val_departure = (int)(station.getTimeDepartureFact() - station.getTimeDepartureSchedule());
//            if (station.getSecond() >= start && station.getSecond() <= end) {
//                stations_content.append(String.format(STATIONS,
//                        String.format(HREF, i, station.getNameStation()),
//                        UtilsArmG.getTime((int)station.getTimeArrivalSchedule()),
//                        UtilsArmG.getTime((int)station.getTimeArrivalFact()),
//                        String.format("%4.1f", val_arrival / 60.0),
//                        UtilsArmG.getTime((int)station.getTimeDepartureSchedule()),
//                        UtilsArmG.getTime((int)station.getTimeDepartureFact()),
//                        String.format("%4.1f", val_departure / 60.0),
//                        station.getPercentAuto()));
//            }
//        }
        String head = String.format(HEAD_STATIONS, "Станция", "Прибытие", "Отправление", "% в<br>авто-<br>ведении");
        String html = String.format(HTML, title + String.format(TABLE_STATIONS, head + stations_content));
        setText(html);
    }

    /**
     * показать информацию о всех поездах
     */
    private void showWhole() {
        def_kind_rep = info_tr;
        ArrTrains trains = chartPanelArm.getChartDataset().getArrTrains();
        String title = String.format(TITLE, "Все поездки");
        String content = String.format(TEMP_2COL, "Кол-во поездок", trains.size());
        content += String.format(TEMP_2COL, "Пройденный путь, км", distance_all(trains));
        content += String.format(TEMP_2COL, "Путь с УСАВПГ (авто / подсказка), км", distance(trains));
        content += String.format(TEMP_2COL, "Затраченное впемя", time_all(trains));
        content += String.format(TEMP_2COL, "Время с УСАВПГ (авто / подсказка)", time(trains));
        String html = String.format(HTML, title + String.format(TABLE, content));
        setText(html);
    }

    /**
     * покозать информацию о поезде
     */
    private void showTrain() {
        def_kind_rep = info_tr;
        boolean isAsim = train.isAsim();
      //  Train avpt.gr.train = chartPanelArm.getChartDataset().getArrTrains().get(indexTrain);
        String title = String.format(TITLE, "Поезд №" + train.getNumTrain() + " от " + train.getDateTimeStart().format(formatDateTime));
      //  String content = String.format(TEMP_2COL, "Маршрут", avpt.gr.train.getRoutName() != null ? avpt.gr.train.getRoutName() : "");
        String content = String.format(TEMP_1COL, "Общие сведения");
        content += String.format(TEMP_2COL, "Маршрут", String.format(HREF, LINK_SCHEDULE, train.getRoutName() != null ? train.getRoutName() : ""));
        content += String.format(TEMP_2COL, "Дата и время начала", train.getDateTimeStart().format(formatDateTime));
        content += String.format(TEMP_2COL, "Локомотив", String.format("№%s - %s", train.getNumLoc(), Train.getNameTypeLoc(train.getTypeLoc(), train.getLocTypeAsoup())));
        content += String.format(TEMP_2COL, "Пройденный путь", train.getDistance() / 1000.0 + " км");
        content += String.format(TEMP_2COL, "Путь с УСАВПГ (авто / подсказка)", train.getDistance_auto() / 1000.0 + " км / " + train.getDistance_prompt() / 1000.0 + " км");
        ChartArrays.RailCoordinate start = chartPanelArm.getChartDataset().getRailCoordinate(train.getSecondStart());
        String km_start = start != null ? Integer.toString(start.getKm()) : "___";
        String pk_start = start != null ? Integer.toString(start.getPk()) : "__";
        ChartArrays.RailCoordinate end = chartPanelArm.getChartDataset().getRailCoordinate(train.getSecondsEnd());
        String km_end = end != null ? Integer.toString(end.getKm()) : "___";
        String pk_end = end != null ? Integer.toString(end.getPk()) : "__";
        if (!isAsim) content += String.format(TEMP_2COL, "ЖД координаты", km_start + " км " + pk_start + " пк - " + km_end + " км " + pk_end + " пк");
        content += String.format(TEMP_2COL, "Затраченное время", train.getDuration_time());
        content += String.format(TEMP_2COL, "Время с УСАВПГ (авто / подсказка)", train.getDuration_time_auto() + " / " + train.getDuration_time_prompt());
        if (!isAsim) content += String.format(TEMP_2COL, "Версия БР", String.format("%s", train.getVerBr()));
        if (train.getDateMap() != null) content += String.format(TEMP_2COL, "Дата карты", train.getDateMap().format(formatDate));
        content += String.format(TEMP_2COL, "Вес состава", train.getWeightTrain() + " Т");
        if (!isAsim) content += String.format(TEMP_2COL, "Количество вагонов", String.format(HREF, LINK_WAGS, train.getWags().size()) + " шт");
        if (!isAsim) content += String.format(TEMP_2COL, "Тип состава", Train.getTypeS(train.getTypeS()));
        content += String.format(TEMP_2COL, "Табельный номер машиниста", train.getNumTab());
        if (!isAsim) content += String.format(TEMP_2COL, "Диаметр бандажа", train.getdBandage() + " мм");
        if (!isAsim) content += String.format(TEMP_2COL, "Длина поезда", train.getLengthTrain() + " м");
        if (!isAsim) content += String.format(TEMP_2COL, "Число секций", train.getCntSection());
        content += String.format(TEMP_2COL, "Техническая скорость", String.format("%4.1f", train.getAvgSpeedMove()) + " км/ч");
        content += String.format(TEMP_2COL, "Участковая скорость, км/ч", String.format("%4.1f", train.getAvgSpeed()) + "км/ч");

        if (!isAsim) content += String.format(TEMP_2COL, "Режим РТ", train.getStatusIsavprt(train.getStIsavprt()));
        if (!isAsim && train.getStIsavprt() > 0) {
            content += String.format(TEMP_2COL, "Второй локомотив", String.format("№%s - %s", train.getNumSlave(), train.getNameSlave()));
            content += String.format(TEMP_2COL, "Сетевой адрес", train.getNetAddress());
        }
        // РТ
        if (!isAsim && (train.getStIsavprt() == 4)) {
            content += String.format(TEMP_2COL, "Положение поезда", train.getPositionTrain());
            content += String.format(TEMP_1COL, "Основной канал");
            content += String.format(TEMP_2COL, "Связь с модемом", train.getMainLinkModem().getPercent() + "%");
            content += String.format(TEMP_2COL, "Связь между локомотивами", train.getMainLinkVSC().getPercent() + "%");
            content += String.format(TEMP_1COL, "Дополнительный канал");
         //   content += String.format(TEMP_2COL, "Состояние", avpt.gr.train.getAdditionalChannel());
            content += String.format(TEMP_2COL, "Включение", train.getSlaveIsOn().getPercent() + "%");
            content += String.format(TEMP_2COL, "Связь с модемом", train.getSlaveLinkModem().getPercent() + "%");
            content += String.format(TEMP_2COL, "Связь между локомотивами", train.getSlaveLinkVSC().getPercent() + "%");
        }
        // Виртуальная сцепка
        if (!isAsim && (train.getStIsavprt() == 1 || train.getStIsavprt() == 2)) {
            content += String.format(TEMP_2COL, "Связь с модемом", train.getMainLinkModem().getPercent() + "%");
        }

        if (train.getTypeLoc() == VL10) {
            content += String.format(TEMP_2COL, "Расход энернии", String.format(" %s", train.getAct()) + " кВт");
            content += String.format(TEMP_2COL, "Рекуперация", String.format(" %s", train.getRec()) + " кВт");
        }
        else {
            content += String.format(TEMP_1COL, "Расход энернии, кВт");
            content += String.format(TEMP_2COL, "", String.format("1-я секция: %s", train.getAct1()));
            content += String.format(TEMP_2COL, "", String.format("2-я секция: %s", train.getAct2()));
            content += String.format(TEMP_2COL, "", String.format("3-я секция: %s", train.getAct3()));
            content += String.format(TEMP_2COL, "", String.format("4-я секция: %s", train.getAct4()));
            content += String.format(TEMP_1COL, "Рекуперация, кВт");
            content += String.format(TEMP_2COL, "", String.format("1-я секция: %s", train.getRec1()));
            content += String.format(TEMP_2COL, "", String.format("2-я секция: %s", train.getRec2()));
            content += String.format(TEMP_2COL, "", String.format("3-я секция: %s", train.getRec3()));
            content += String.format(TEMP_2COL, "", String.format("4-я секция: %s", train.getRec4()));
        }
        String html = String.format(HTML, title + String.format(TABLE, content));
        setText(html);
    }

    /**
     * переход курсора к станции на канве
     * @param index - индеч станции
     */
    private void goToStation(int index) {
        Stations stations = chartPanelArm.getChartArm().getStations();
        Stations.Station station = null;
        if (index <stations.size())
            station = stations.getStation(index);
        if (station != null) {
            int second = station.getSecond();
            ScrollBarArm scroll = chartPanelArm.getScrollBar();
            chartPanelArm.getChartArm().paintMarker(null, null, scroll, second);
        }
    }

    /**
     * @param trains - массив поездов
     * @return - пройденное расстояние всех поездов (км)
     */
    private double distance_all(ArrTrains trains) {
        int distance = 0;
        for (int i = 0; i < trains.size(); i++) distance += trains.get(i).getDistance();
        return distance / 1000.0;
    }

    /**
     * @param trains - массив поездов
     * @return - пройденное расстояние автоведение/подсказка всех поездов (км)
     */
    private String distance(ArrTrains trains) {
        double auto = 0;
        double prompt = 0;
        for (int i = 0; i < trains.size(); i++) {
            auto += trains.get(i).getDistance_auto();
            prompt += trains.get(i).getDistance_prompt();
        }
        return auto / 1000.0  + " / " + prompt / 1000.0;
    }

    /**
     * @param trains - массив поездов
     * @return - затрачено времени для всех поездов (км)
     */
    private String time_all(ArrTrains trains) {
        int seconds = 0;
        for (int i = 0; i < trains.size(); i++) seconds += trains.get(i).getSeconds();
        return  UtilsArmG.getDurationTime(seconds);
    }

    /**
     * @param trains - массив поездов
     * @return - затраченное время автоведение/подсказка всех поездов
     */
    private String time(ArrTrains trains) {
        int auto = 0;
        int prompt = 0;
        for (int i = 0; i < trains.size(); i++) {
            auto += trains.get(i).getSeconds_auto();
            prompt += trains.get(i).getSeconds_prompt();
        }
        return UtilsArmG.getDurationTime(auto) + " / " + UtilsArmG.getDurationTime(prompt);
    }

    /**
     * подготовка
     */
    private void initDocument() {
        setOpaque(false);
        setEditable(false);
        HTMLEditorKit kit = new HTMLEditorKit();
        setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(STYLE_TABLE);
        styleSheet.addRule(STYLE_TH);
        styleSheet.addRule(STYLE_TD);
        setDocument(kit.createDefaultDocument());
    }

    /**
     * событие активации гиперссылки
     */
    class hyperlinkListener implements HyperlinkListener {

        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                stack.push(getText());
                if (btnBack != null && stack.size() > 0) btnBack.setEnabled(true);

                if (e.getDescription().equals(LINK_WAGS))
                    showWags();
                else if (e.getDescription().equals(LINK_SCHEDULE))
                    showSchedule();
                else {
                    goToStation(Integer.parseInt(e.getDescription())); // активация индекса станции
                    closeParenDialog();
                }
                InfoTrainPan.this.setCaretPosition(0);
            }
        }
    }

    /**
     * закрыть родительский диалог
     */
    private void closeParenDialog() {
        Container container = InfoTrainPan.this;
        while (!(container instanceof  JDialog))
            container = container.getParent();
        container.setVisible(false);
    }

    /**
     * событие кнопки назад
     */
    class btnBackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doBack();
        }
    }

    /**
     * событие кнопки btnPrint
     */
    class btnPrintListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    btnPrint.getParent().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        try {
                            Document doc = InfoTrainPan.this.getDocument();
                            String txt = doc.getText(0, doc.getLength());
                            List<String> list = Arrays.asList(txt.split("\n"));
                            new ExcelReports(list, def_kind_rep);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    finally {
                        btnPrint.getParent().getParent().setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
            t.start();
        }
    }

    /**
     * событие кнопки btnVirtCoup
     */
    class btnVirtCoupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    btnVirtCoup.getParent().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        try {
                            if (train != null)
                                new ExcelReports(chartPanelArm, train, virt_coup);
                            else
                                new ExcelReports(chartPanelArm, null, virt_coup_all);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    finally {
                        btnVirtCoup.getParent().getParent().setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
            t.start();
        }
    }

    /**
     * событие кнопки btnConsumpEnergy
     */
    class btnConsumpEnergyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    btnConsumEnergy.getParent().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        try {
                            int loc_type = train != null ? train.getTypeLoc() : trains.get(0).getTypeLoc();
                            if (loc_type == VL10) {
                                new ExcelReports(chartPanelArm, train, consump_single_energy);
                            }
                            else {
                                new ExcelReports(chartPanelArm, train, consump_energy);
                            }
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    finally {
                        btnConsumEnergy.getParent().getParent().setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
            t.start();
        }
    }

    /**
     * событие кнопки btnClose, закрываем окно диалога InfoTrainsDialog
     */
    class btnCloseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            closeParenDialog();
        }
    }

    /**
     * установка события для кнопки возврата
     * @param btnBack -
     */
    public void setActionBtnBack(JButton btnBack) {
        if (btnBack != null) {
            this.btnBack = btnBack;
            for (ActionListener l : btnBack.getActionListeners())
                btnBack.removeActionListener(l);
            btnBack.setEnabled(stack.size() > 0);
            btnBack.addActionListener(new btnBackListener());
        }
    }

    /**
     * установка события для кнопки btnPrint
     * @param btnPrint -
     */
    public void setActionBtnPrint(JButton btnPrint) {
        if (btnPrint != null) {
            this.btnPrint = btnPrint;
            for (ActionListener l : btnPrint.getActionListeners())
                btnPrint.removeActionListener(l);
            btnPrint.addActionListener(new btnPrintListener());
        }
    }

    /**
     * установка события для кнопки btnVirtCoup
     * @param btnVirtCoup -
     */
    public void setActionBtnVirtCoup(JButton btnVirtCoup) {
        if (btnVirtCoup != null) {
            this.btnVirtCoup = btnVirtCoup;
            for (ActionListener l : btnVirtCoup.getActionListeners())
                btnVirtCoup.removeActionListener(l);
            btnVirtCoup.addActionListener(new btnVirtCoupListener());
        }
    }

    /**
     * установка события для кнопки btnConsumpEnergy
     * @param btnConsumEnergy -
     */
    public void setActionBtnConsumpEnergy(JButton btnConsumEnergy) {
        if (btnVirtCoup != null) {
            this.btnConsumEnergy = btnConsumEnergy;
            for (ActionListener l : btnConsumEnergy.getActionListeners())
                btnConsumEnergy.removeActionListener(l);
            btnConsumEnergy.addActionListener(new btnConsumpEnergyListener());
        }
    }

    /**
     * установка события для закрытие окна
     * @param btnClose -
     */
    public void setActionBtnClose(JButton btnClose) {
        if (btnClose != null) {
            this.btnClose = btnClose;
            for (ActionListener l : btnClose.getActionListeners())
                btnClose.removeActionListener(l);
            btnClose.addActionListener(new btnCloseListener());
        }
    }

    /**
     * установка горячих клавиш
     */
    public void setKeyEvent() {

        InputMap imap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "back");
        ActionMap amap = this.getActionMap();

        amap.put("back", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doBack();
            }
        });
    }

    /**
     * событие назад
     */
    private void doBack() {
        if (stack.size() > 0) {
            switch (def_kind_rep) {
                case info_wags:
                case info_schedule:
                    def_kind_rep = info_tr;
                    break;
            }
            InfoTrainPan.this.setText(stack.pop());
            if (btnBack != null && stack.size() == 0)
                btnBack.setEnabled(false);
        }
    }
}
