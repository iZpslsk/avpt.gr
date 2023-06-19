package avpt.gr.graph;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.*;
import avpt.gr.chart_dataset.keysEnum.LineKeys;
import avpt.gr.common.UtilsArmG;
import avpt.gr.common.WeightBlocks;
import avpt.gr.components.ScrollBarArm;
import avpt.gr.components.hex_tab.HexTab;
import avpt.gr.maps.Limits;
import avpt.gr.maps.Objects;
import avpt.gr.maps.Profiles;
import avpt.gr.maps.Stations;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.Range;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.threeten.bp.LocalDateTime;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import static avpt.gr.common.WeightBlocks.*;
import static avpt.gr.common.UtilsArmG.labelYAxisFont;
import static avpt.gr.common.UtilsArmG.tickFont;
import static avpt.gr.common.UtilsArmG.trMarkerFont;

/**
 * диаграммы линий, сигналов, задач
 */
public class ChartArm extends JFreeChart {
    public static final int MAX_RANGE = 97200 * 5;//129600;// 64800;// prefSizeHeight
    public static final int MAX_RANGE_COORDINATE = 97200 * 50;
    private int maxRange = MAX_RANGE;
    private int minDuration;
    private int maxDuration;
    private static final Color FONT_TICK_COLOR = Color.BLACK;
    private static final Color FONT_LABEL_COLOR = Color.BLACK;
    private static final Color BACKGROUND_COLOR = new Color(0x000000);
    private final CombinedDomainXYPlot plotCombine;
    private final Stations stations;
    private final Objects objects;
    private final Profiles profiles;
    private final Limits limits;
    private final ChartDataset chartDataset;
    private final NumberAxis domainAxis;
    private final HexTab hexTab;
    private final UtilsArmG.MutableDouble boundUpper;
    private boolean isTextAnnotationProfile = true;
    private boolean isAllAnnotationProfile;
    private SeriesLines seriesLines;
    private SeriesSignalsDiscrete seriesSignalsDiscrete;
    private SeriesSignalsAutodisp seriesSignalsAutodis;
    private SeriesSignalsBHV seriesSignalsBHV;
    private SeriesSignalsTed seriesSignalsTed;
    private SeriesSignalsTed_s5k seriesSignalsTed_s5k;
    private SeriesSignalsLink seriesSignalsLink;
    private boolean isSelected = false; // режим выделения

    private TaskAlsn seriesAlsn;
    private TaskAlsnClub seriesAlsnClub;
    private TaskAlsnBr seriesAlsnBr;
    private TaskAlsnBr_vl80 seriesAlsnBr_vl80;
    private TaskAutoDrive seriesAutoDrive;
    private TaskPneumatic seriesPneumatic;
    private TaskPneumaticUsavp seriesPneumaticUsavp;
    private TaskPneumatic seriesPneumatic2;
    private TaskUatl seriesUatl;
    private TaskKKM_s5k seriesKKM_s5k;
    private TaskKKM_s5k_2 seriesKKM_s5k_2;
    private TaskKKM_vl10 seriesKKM_vl10;
    private TaskKKM_s5 seriesKKM_s5;
    private TaskVsc seriesVsc;
    private TaskCabin seriesCabin;
    private TaskPushKey seriesPushKey;
    private TaskMainControl seriesMainControl;
    private TaskRevControl seriesRevControl;
    private TaskSchema seriesSchema;
    private TaskBHV seriesBHV;

    private XYPlot plotTrain;
    private XYPlot plotVoltage_cs;
    private XYPlot plotVoltage;
    private XYPlot plotVoltage2;
    private XYPlot plotVoltage3;
    private XYPlot plotVoltage4;
    private XYPlot plotAmperage_common;
    private XYPlot plotAmperage_anchor;
    private XYPlot plotAmperage_excitation;
    private XYPlot plotAmperage_engine;
    private XYPlot plotAmperage;
    private XYPlot plotAmperage3;
    private XYPlot plotAmperage4;
    private XYPlot plotConsumption;
    private XYPlot plotPower;
    private XYPlot plotTail;
    private XYPlot plotPress;
    private XYPlot plotSpeed;
    private XYPlot plotProfile;
    private XYPlot plotPosition;
    private XYPlot plotMap;
    private XYPlot plotAlsn;
    private XYPlot plotAlsn_club;
    private XYPlot plotAlsn_br;
    private XYPlot plotAlsn_br_vl80;
    private XYPlot plotAuto;
    private XYPlot plotPneumatic;
    private XYPlot plotPneumatic2;
    private XYPlot plotPneumaticUsavp;
    private XYPlot plotUatl;
    private XYPlot plotKkm_s5k;
    private XYPlot plotKkm_s5;
    private XYPlot plotKkm_s5k_2;
    private XYPlot plotKkm_vl10;
    private XYPlot plotBHV;
    private XYPlot plotVsc;
    private XYPlot plotCabin;
    private XYPlot plotMainControl;
    private XYPlot plotRevControl;
    private XYPlot plotSchema;
    private XYPlot plotPushKey;
    private XYPlot plotSignals;
    private XYPlot plotAutodis;
    private XYPlot plotSignBhv;
    private XYPlot plotTed;
    private XYPlot plotTed_s5k;
    private XYPlot plotLink;

    private ArrayList<ValueMarker> valueMarkers = new ArrayList<ValueMarker>(); // маркер для вертикольного курсора
    private ArrayList<Marker> intervalMarkers = new ArrayList<Marker>();
    private ArrayList trainLabelMarkers;			// маркеры для названий поездов
  //  private static HashMap<Comparable<String>, Boolean> mapViewLines = new HashMap<Comparable<String>, Boolean>();

    private int weight_signal = 100;

    // названия для разделов (subplot)
    public static final String TRAIN_LABEL = "Поезд ";
    public static final String VOLTAGE_CS_LABEL = "Сеть ";
    public static final String VOLTAGE_ENGINE_LABEL = "Напряжение";
    public static final String AMPERAGE_COMMON_LABEL = "Общ ток ";
    public static final String AMPERAGE_ANCHOR_LABEL = "Ток якоря";
    public static final String AMPERAGE_EXCITATION_LABEL = "Ток возб.";
    public static final String AMPERAGE_ENGINE_LABEL = "Ток дв.";
    public static final String AMPERAGE_LABEL = "Ток";
    public static final String CONSUMPTION_LABEL = "Энергия ";
    public static final String POWER_LABEL = "Сила ";
    public static final String TAIL_LABEL = "Хвост ";
    public static final String MODEM_LABEL = "Модем ";  // уровень сигнала модема
    public static final String PRESS_LABEL = "Давление ";
    public static final String SPEED_LABEL = "Скорость ";

    public static final String MAP_LABEL =              "Объекты    ";
    public static final String PROF_LABEL =             "Профиль    ";
    public static final String POSITION_LABEL =         "Позиция    ";
    public static final String ALSN_LABEL =             "АЛСН       ";
    public static final String ALSN_CLUB_LABEL =        "АЛСН-К     ";
    public static final String ALSN_BR_LABEL =          "АЛСН-БР    ";
    public static final String ALSN_BR_VL80_LABEL =     "АЛСН-БР\t  ";
    public static final String AUTO_LABEL =             "Автовед    ";
    public static final String PNEUMATIC_LABEL =        "Пневм      ";
    public static final String PNEUMATIC2_LABEL =       "Пневм 2    ";
    public static final String PNEUMATIC_USAVP_LABEL =  "Пневм\t      ";
    public static final String UATL_LABEL =             "УАТЛ       ";
    public static final String KKM_S5K_LABEL =          "ККМ        ";
    public static final String KKM_S5_LABEL =           "ККМ\t\t\t      ";
    public static final String KKM_S5K_2_LABEL =        "ККМ\t\t      ";
    public static final String KKM_VL10_LABEL =         "ККМ\t       ";
    public static final String BHV_LABEL =              "БХВ        ";
    public static final String VSC_LABEL =              "ВСЦ        ";
    public static final String CABIN_LABEL =            "Кабина     ";
    public static final String MAIN_CONTROL_LABEL =     "Рукоятка г ";
    public static final String REV_CONTROL_LABEL =      "Рукоятка р ";
    public static final String SCHEMA_LABEL =           "Схема      ";
    public static final String KEYS_LABEL =             "Клавиши    ";
    public static final String SIGNALS_LABEL =          "Сигналы    ";
    public static final String AUTODIS_LABEL =          "Автодис    ";
    public static final String SIGN_TED_LABEL =         "ТЭД        ";
    public static final String SIGN_TED_S5K_LABEL =     "ТЭД\t       ";
    public static final String SIGN_LINK_LABEL =        "Связь      ";
    public static final String SIGN_BHV_LABEL =         "Сигналы БХВ";

    public ChartArm(ChartDataset chartDataset, HexTab hexTab, UtilsArmG.MutableDouble boundUpper) {
        super(new CombinedDomainXYPlot(new NumberAxis()));
        this.chartDataset = chartDataset;
        minDuration = 90;
        maxDuration = 16000;
        if (!chartDataset.isTime()) {
            minDuration *= 10;
            maxDuration *= 10;
        }
        this.hexTab = hexTab;
        this.boundUpper = boundUpper;
        this.trainLabelMarkers = new ArrayList();
        clearSubtitles();
        plotCombine = getPlotCombine();
        domainAxis = getDomainAxis(plotCombine);

        seriesLines = chartDataset.getSeriesLines();
        seriesSignalsDiscrete = chartDataset.getSeriesSignalsDiscrete();
        seriesSignalsAutodis = chartDataset.getSeriesSignalsAutodisp();
        seriesSignalsBHV = chartDataset.getSeriesSignalsBHV();
        seriesSignalsTed = chartDataset.getSeriesSignalsTed();
        seriesSignalsTed_s5k = chartDataset.getSeriesSignalsTed_s5k();
        seriesSignalsLink = chartDataset.getSeriesSignalsLink();
        seriesAlsn = chartDataset.getSeriesAlsn();
        seriesAlsnClub = chartDataset.getSeriesAlsnClub();
        seriesAlsnBr = chartDataset.getSeriesAlsnBr_vl10();
        seriesAlsnBr_vl80 = chartDataset.getSeriesAlsnBr_vl80();
        seriesAutoDrive = chartDataset.getSeriesAutoDrive();
        seriesPneumatic = chartDataset.getSeriesPneumatic();
        seriesPneumatic2 = chartDataset.getSeriesPneumatic2();
        seriesPneumaticUsavp = chartDataset.getSeriesPneumaticUsavp();
        seriesUatl = chartDataset.getSeriesUatl();
        seriesKKM_s5k = chartDataset.getSeriesKKM_s5k();
        seriesKKM_s5 = chartDataset.getSeriesKKM_s5();
        seriesKKM_s5k_2 = chartDataset.getSeriesKKM_s5k_2();
        seriesKKM_vl10 = chartDataset.getSeriesKKM_vl10();
        seriesBHV = chartDataset.getSeriesBhv_valve();
        seriesVsc = chartDataset.getSeriesVsc();
        seriesCabin = chartDataset.getSeriesCabin();
        seriesMainControl = chartDataset.getSeriesMainControl();
        seriesRevControl = chartDataset.getSeriesRevControl();
        seriesSchema = chartDataset.getSeriesSchema();
        seriesPushKey = chartDataset.getSeriesPushKey();

        ArrBlock32 arrBlock32 = chartDataset.getArrBlock32();
        stations = new Stations(arrBlock32);
        objects = new Objects(arrBlock32);
        profiles = new Profiles(arrBlock32);
        limits = new Limits(arrBlock32);

        if (seriesLines != null) {
            plotTrain = addLinesToPlotCombine(TRAIN_LABEL, seriesLines.getSerTrainCollect(), 20);               // поезд
            plotVoltage_cs = addLinesToPlotCombine(VOLTAGE_CS_LABEL, seriesLines.getSerVoltageCollect_cs(), getWeight_voltage_cs(false));                                   // напряжение к.с.
            plotVoltage = addLinesToPlotCombine(VOLTAGE_ENGINE_LABEL, seriesLines.getSerVoltageCollect(), getWeight_voltage(false));                                         // напряжение
            plotAmperage_common = addLinesToPlotCombine(AMPERAGE_COMMON_LABEL, seriesLines.getSerAmperageCollect_common(), getWeight_amperage_common(false));                          // общий ток
            plotAmperage_anchor = addLinesToPlotCombine(AMPERAGE_ANCHOR_LABEL, seriesLines.getSerAmperageCollect_anchor(), getWeight_amperage_anchor(false));                                       // ток
            plotAmperage_excitation = addLinesToPlotCombine(AMPERAGE_EXCITATION_LABEL, seriesLines.getSerAmperageCollect_excitation(), getWeight_amperage_excitation(false));                                       // ток
            plotAmperage_engine = addLinesToPlotCombine(AMPERAGE_ENGINE_LABEL, seriesLines.getSerAmperageCollect_engine(), getWeight_amperage_engine(false));
            plotAmperage = addLinesToPlotCombine(AMPERAGE_LABEL, seriesLines.getSerAmperageCollect(), getWeight_amperage(false));
            plotConsumption = addLinesToPlotCombine(CONSUMPTION_LABEL, seriesLines.getSerConsumptionEnCollect(), getWeight_consumption(false));                                 // расход
            plotPower = addLinesToPlotCombine(POWER_LABEL, seriesLines.getSerPowerCollect(), getWeight_power(false));                                             // сила
            plotTail = addLinesToPlotCombine(TAIL_LABEL, seriesLines.getSerDistanceTailCollect(), getWeight_tail(false));                                               // расстояние до хвоста
            plotPress = addLinesToPlotCombine(PRESS_LABEL, seriesLines.getSerPressCollect(), getWeight_press(false));                                             // давление
            plotSpeed = addLinesToPlotCombine(SPEED_LABEL, seriesLines.getSerSpeedCollect(), getWeight_speed(false));                                             // скорость
            plotPosition = addLinesToPlotCombine(POSITION_LABEL, seriesLines.getSerPositionCollect(), getWeight_position(false));                                       // позиция контроллера
            plotMap = addLinesToPlotCombine(MAP_LABEL, seriesLines.getSerMapCollect(), getWeight_map(false));                                                 // карта (объекты пути)
            plotProfile = addLinesToPlotCombine(PROF_LABEL, seriesLines.getSerProfileCollect(), getWeight_prof(false));                                            // профиль
        }
        if (seriesAlsn != null) plotAlsn = addTaskToPlotCombine(ALSN_LABEL, seriesAlsn.createDataset(), getWeight_alsn(false));
        if (seriesAlsnClub != null) plotAlsn_club = addTaskToPlotCombine(ALSN_CLUB_LABEL, seriesAlsnClub.createDataset(), getWeight_alsn_club(false));
        if (seriesAlsnBr != null) plotAlsn_br = addTaskToPlotCombine(ALSN_BR_LABEL, seriesAlsnBr.createDataset(), getWeight_alsn_br(false));
        if (seriesAlsnBr_vl80 != null) plotAlsn_br_vl80 = addTaskToPlotCombine(ALSN_BR_VL80_LABEL, seriesAlsnBr_vl80.createDataset(), getWeight_alsn_br(false));
        if (seriesAutoDrive != null) plotAuto = addTaskToPlotCombine(AUTO_LABEL, seriesAutoDrive.createDataset(), getWeight_auto_drive(false));
        if (seriesPneumatic != null) plotPneumatic = addTaskToPlotCombine(PNEUMATIC_LABEL, seriesPneumatic.createDataset(), getWeight_pneumatic1(false));
        if (seriesPneumatic2 != null) plotPneumatic2 = addTaskToPlotCombine(PNEUMATIC2_LABEL, seriesPneumatic2.createDataset(), getWeight_pneumatic2(false));
        if (seriesPneumaticUsavp != null) plotPneumaticUsavp = addTaskToPlotCombine(PNEUMATIC_USAVP_LABEL, seriesPneumaticUsavp.createDataset(), getWeight_pneumatic_usavp(false));
        if (seriesUatl != null) plotUatl = addTaskToPlotCombine(UATL_LABEL, seriesUatl.createDataset(), getWeight_uatl(false));
        if (seriesKKM_s5k != null) plotKkm_s5k = addTaskToPlotCombine(KKM_S5K_LABEL, seriesKKM_s5k.createDataset(), getWeight_kkm(false));
        if (seriesKKM_s5 != null) plotKkm_s5 = addTaskToPlotCombine(KKM_S5_LABEL, seriesKKM_s5.createDataset(), getWeight_kkm_s5(false));
        if (seriesKKM_s5k_2 != null) plotKkm_s5k_2 = addTaskToPlotCombine(KKM_S5K_2_LABEL, seriesKKM_s5k_2.createDataset(), getWeight_kkm2(false));
        if (seriesKKM_vl10 != null) plotKkm_vl10 = addTaskToPlotCombine(KKM_VL10_LABEL, seriesKKM_vl10.createDataset(), getWeight_kkm_vl10(false));
        if (seriesBHV != null) plotBHV = addTaskToPlotCombine(BHV_LABEL, seriesBHV.createDataset(), getWeight_bhv(false));
        if (seriesVsc != null) plotVsc = addTaskToPlotCombine(VSC_LABEL, seriesVsc.createDataset(), getWeight_vsc(false));
        if (seriesCabin != null) plotCabin = addTaskToPlotCombine(CABIN_LABEL, seriesCabin.createDataset(), getWeight_cabin(false));
        if (seriesMainControl != null) plotMainControl = addTaskToPlotCombine(MAIN_CONTROL_LABEL, seriesMainControl.createDataset(), getWeight_main_control(false));
        if (seriesRevControl != null) plotRevControl = addTaskToPlotCombine(REV_CONTROL_LABEL, seriesRevControl.createDataset(), getWeight_rev_control(false));
        if (seriesSchema != null) plotSchema = addTaskToPlotCombine(SCHEMA_LABEL, seriesSchema.createDataset(), getWeight_schema(false));
        if (seriesPushKey != null) plotPushKey = addTaskToPlotCombine(KEYS_LABEL, seriesPushKey.createDataset(), getWeight_push_key(false));
        if (seriesSignalsDiscrete != null) plotSignals = addSignalsToPlotCombine(seriesSignalsDiscrete, SIGNALS_LABEL, getWeight_signals(false));
        if (seriesSignalsTed != null) plotTed = addSignalsToPlotCombine(seriesSignalsTed, SIGN_TED_LABEL, getWeight_signals_ted(false));
        if (seriesSignalsTed_s5k != null) plotTed_s5k = addSignalsToPlotCombine(seriesSignalsTed_s5k, SIGN_TED_S5K_LABEL, getWeight_signals_ted_s5k(false));
        if (seriesSignalsLink != null) plotLink = addSignalsToPlotCombine(seriesSignalsLink, SIGN_LINK_LABEL, getWeight_signals_link(false));
        if (seriesSignalsAutodis != null) plotAutodis = addSignalsToPlotCombine(seriesSignalsAutodis, AUTODIS_LABEL, getWeight_signals_autodis(false));
        if (seriesSignalsBHV != null) plotSignBhv = addSignalsToPlotCombine(seriesSignalsBHV, SIGN_BHV_LABEL, getWeight_signals_bhv(false));

//        setViewLines(false);
    }

    public NumberAxis getDomainAxis() {
        return domainAxis;
    }

    public int getSecondCoordinate() {
        return chartDataset.getChartArrays().getSecondCoordinate();
    }

    private void setPlotWeightDef(String title) {
//        if (title.equals(VOLTAGE_CS_LABEL)) {
//            SeriesLines.mapDefWeight.put(title, 1);
//        } else if (title.equals(VOLTAGE_ENGINE_LABEL)) {
//            SeriesLines.mapDefWeight.put(title, W_LINE);
//        }

//        if (title.equals(PROF_LABEL))
//            SeriesLines.mapDefWeight.put(title, W_LINE / 5);
//        else if (title.equals(POSITION_LABEL))
//            SeriesLines.mapDefWeight.put(title, W_LINE / 2);
//        else
//            SeriesLines.mapDefWeight.put(title, W_LINE);
    }

    /**
     * добавляем блок с линиями в JFreeChart
     * @param title - название болока линий
     * @param dataset - XYSeriesCollection - набор данных для линий
     * @param weight - "вес"
     */
    private XYPlot  addLinesToPlotCombine(String title, XYDataset dataset, int weight) {
       // setPlotWeightDef(title);
        XYSeriesCollection seriesCollection = (XYSeriesCollection)dataset;
        double min = seriesCollection.getRangeLowerBound(false);
        double max =  seriesCollection.getRangeUpperBound(false);
        XYItemRenderer renderer = getRenderer(title);
      //  setRangeAxis(title, range);
        NumberAxis range = createRangeAxis(title, min, max);
        range.setLabelLocation(AxisLabelLocation.MIDDLE);
        XYPlot plot = makeSubPlot(dataset, range, renderer);

        if (title.equals(MAP_LABEL)) {
            range.setTickLabelsVisible(false);
            range.setTickMarksVisible(false);
            range.setLabelAngle(Math.PI/2.0);
            setAnnotationsMap(plot);
            stations.addStationsToTrains(getChartDataset().getArrTrains());
        }

        if (title.equals(PROF_LABEL)) {
       //     plotProfile = plot;
            range.setTickLabelsVisible(false);
            range.setTickMarksVisible(false);
            range.setLabelAngle(Math.PI/2.0);
            profiles.addAnnotationProfile(plot, false);
        }

        if (title.equals(POSITION_LABEL)) {
            range.setTickLabelsVisible(false);
            range.setTickMarksVisible(false);
            range.setLabelAngle(Math.PI/2.0);
        }

        if (title.equals(TRAIN_LABEL)) {
            plot.getRangeAxis().setTickMarksVisible(false);
            plot.getRangeAxis().setTickLabelsVisible(false);
            plot.getRangeAxis().setVisible(false);
        }

        if (title.equals(SPEED_LABEL)) {
            limits.addAnnotationLimit(this, plot);
            limits.addLimitsToTrains(getChartDataset().getArrTrains());
        }
        int cnt = plot.getSeriesCount();
        int totalDots = 0;
        // толщина и цвет линий диаграмм для линейных графиков
        for (int i = 0; i < cnt; i++) {
            LineKeys key = (LineKeys) plot.getDataset().getSeriesKey(i);
            SeriesLines.setColorVisibleAndStrokeLines(i, key, renderer); // цвет и толщина линий
            if (key == LineKeys.SPEED_MAX)
                setLimMap(plot, key, !plot.getRenderer().isSeriesVisible(i));
            totalDots += ((XYSeriesCollection) dataset).getSeries(i).getItemCount();
        }
        setCrosshair(plot);
        addMarker(plot);
        addMarkerTrains(plot, chartDataset.getArrTrains());
        // добавляем где есть данные или plot с информацией о поезде
        if (totalDots > 0 | title.equals(ChartArm.TRAIN_LABEL)) {
            plotCombine.add(plot, 1);
            plot.setWeight(weight);
        }
        return plot;
    }

    /**
     * устанавливаем ограничения карты
     * @param plot -
     * @param key - LineKeys - тип линии
     * @param isVisible - включена
     */
    public void setLimMap(XYPlot plot, LineKeys key, boolean isVisible) {
        if (key == LineKeys.SPEED_MAX) {
            if (isVisible) {
                for (XYAnnotation a : limits.getAnnotations()) {
                    plot.removeAnnotation(a);
                }
            }
            else {
                for (XYAnnotation a : limits.getAnnotations()) {
                    plot.removeAnnotation(a);
                }
                for (XYAnnotation a : limits.getAnnotations()) {
                    plot.addAnnotation(a);
                }
            }
        }
    }

    private XYPlot addSignalsToPlotCombine(SeriesSignals seriesSignals, String title, int weight) {
        IntervalXYDataset dataset = seriesSignals.getTaskSeriesCollection();
        String[] symbols = new String[dataset.getSeriesCount()];
        Arrays.fill(symbols, "");
        SymbolAxis yAxis = new SymbolAxis(title , symbols);
        yAxis.setLabelAngle(Math.PI/2.0);
        yAxis.setGridBandsVisible(false);
        yAxis.setTickLabelsVisible(false);
        yAxis.setLabelFont(labelYAxisFont);
        yAxis.setInverted(true);

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setUseYInterval(true);
        renderer.setShadowVisible(false);

        XYPlot plot = new XYPlot(dataset, null, yAxis, renderer);

        for (int i = plot.getSeriesCount() - 1; i >= 0; i--) {
            //String name = ((XYTaskDataset) dataset).getTasks().getSeries(i).getKey().toString();
            int key = Integer.parseInt(((XYTaskDataset) dataset).getTasks().getSeries(i).getKey().toString());
            renderer.setSeriesPaint(i, seriesSignals.getColorSeries(key));
        }

        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.getRangeAxis().setTickMarksVisible(false);
        ((XYBarRenderer) plot.getRenderer()).setBarPainter(new StandardXYBarPainter()); // убрать градиент

        setCrosshair(plot);
        addMarker(plot);
        addMarkerTrains(plot, chartDataset.getArrTrains());
        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(false);
        if (seriesSignals.getTaskSeriesCollection().getSeriesCount() > 0) {
            plotCombine.add(plot, 1);
            plot.setWeight(weight);
        }
        return plot;
    }


    /**
     * добавляет XYPlot
     * @param dataset - набор данных
     * @param weight - вес (высота plot)
     */
    private XYPlot addTaskToPlotCombine(String title, IntervalXYDataset dataset, int weight) {

        if (dataset.getSeriesCount() == 0) return null;
        String[] symbols = new String[dataset.getSeriesCount()];
        Arrays.fill(symbols, "");

        XYBarRenderer renderer = null;
        if (title.equals(ALSN_LABEL)) {
            renderer = chartDataset.getSeriesAlsn().createRenderer();
        }
        if (title.equals(ALSN_CLUB_LABEL)) {
            renderer = chartDataset.getSeriesAlsnClub().createRenderer();
        }
        if (title.equals(ALSN_BR_LABEL)) {
            renderer = chartDataset.getSeriesAlsnBr_vl10().createRenderer();
        }
        if (title.equals(ALSN_BR_VL80_LABEL)) {
            renderer = chartDataset.getSeriesAlsnBr_vl80().createRenderer();
        }
        if (title.equals(AUTO_LABEL)) {
            renderer = chartDataset.getSeriesAutoDrive().createRenderer();
        }
        if (title.equals(PNEUMATIC_LABEL)) {
            renderer = chartDataset.getSeriesPneumatic().createRenderer();
        }
        if (title.equals(PNEUMATIC2_LABEL)) {
            renderer = chartDataset.getSeriesPneumatic2().createRenderer();
        }
        if (title.equals(PNEUMATIC_USAVP_LABEL)) {
            renderer = chartDataset.getSeriesPneumaticUsavp().createRenderer();
        }
        if (title.equals(UATL_LABEL)) {
            renderer = chartDataset.getSeriesUatl().createRenderer();
        }
        if (title.equals(KKM_S5K_LABEL)) {
            renderer = chartDataset.getSeriesKKM_s5k().createRenderer();
        }
        if (title.equals(KKM_S5K_2_LABEL)) {
            renderer = chartDataset.getSeriesKKM_s5k_2().createRenderer();
        }
        if (title.equals(KKM_S5_LABEL)) {
            renderer = chartDataset.getSeriesKKM_s5().createRenderer();
        }
        if (title.equals(KKM_VL10_LABEL)) {
            renderer = chartDataset.getSeriesKKM_vl10().createRenderer();
        }
        if (title.equals(BHV_LABEL)) {
            renderer = chartDataset.getSeriesBhv_valve().createRenderer();
        }
//        if (title.equals(KKM_VL10_LABEL)) {
//            renderer = chartDataset.getSeriesKKM_vl10().createRenderer();
//        }
        if (title.equals(KEYS_LABEL)) {
            renderer = chartDataset.getSeriesPushKey().createRenderer();
        }
        if (title.equals(VSC_LABEL)) {
            renderer = chartDataset.getSeriesVsc().createRenderer();
        }
        if (title.equals(CABIN_LABEL)) {
            renderer = chartDataset.getSeriesCabin().createRenderer();
        }
        if (title.equals(MAIN_CONTROL_LABEL)) {
            renderer = chartDataset.getSeriesMainControl().createRenderer();
        }
        if (title.equals(REV_CONTROL_LABEL)) {
            renderer = chartDataset.getSeriesRevControl().createRenderer();
        }
        if (title.equals(SCHEMA_LABEL)) {
            renderer = chartDataset.getSeriesSchema().createRenderer();
        }

        if (renderer == null) return null;

        SymbolAxis yAxis = new SymbolAxis(title , symbols);
        yAxis.setLabelAngle(Math.PI/2.0);
        yAxis.setGridBandsVisible(false); // отключить полосы
        yAxis.setTickLabelsVisible(false);
        yAxis.setLabelFont(labelYAxisFont);
//		if (title.equals(ChartArm.SAZDT_LABEL)) {
//			yAxis.setLabelPaint(new Color(0x0052AE));
//		}
        yAxis.setInverted(true);// секция А сверху

//		XYBarRenderer renderer = null;
//		if (title.equals(ChartArm.SAZDT_LABEL)) {
//			renderer = datasets.getArrayTasksSazdt().createRenderer();
//		}
//		if (title.equals(ChartArm.ALSN_LABEL)) {
//			renderer = datasets.getArrayTasksAlsn().createRenderer();
//		}
//		if (title.equals(ChartArm.MENU_LABEL)) {
//			renderer = datasets.getArrayTasksMenu().createRenderer();
//		}
//		if (title.equals(ChartArm.MAC_LABEL)) {
//			renderer = datasets.getArrayTasksMac().createRenderer();
//		}
//		if (renderer == null) return;

        renderer.setUseYInterval(true);
        renderer.setShadowVisible(false);

        XYPlot plot = new XYPlot(dataset, null, yAxis, renderer);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.getRangeAxis().setTickMarksVisible(false);
        ((XYBarRenderer) plot.getRenderer()).setBarPainter(new StandardXYBarPainter()); // убрать градиент

        setCrosshair(plot);
        addMarker(plot);
        addMarkerTrains(plot, chartDataset.getArrTrains());

        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(false);
        plotCombine.add(plot, 1);
        plot.setWeight(weight);
        return plot;
    }

    /**
     * создаем subrange (ось y для одной series)
     * @param title - название
     * @return NumberAxis
     */
    private NumberAxis createRangeAxis(String title, double min, double max) {
        NumberAxis axis = new NumberAxis(title);
        axis.setLabelLocation(AxisLabelLocation.MIDDLE);
        axis.setLabelFont(labelYAxisFont);
        axis.setTickLabelFont(tickFont);
        axis.setLabelPaint(FONT_LABEL_COLOR);
        axis.setTickLabelPaint(FONT_TICK_COLOR);
        axis.setStandardTickUnits(new NumberTickUnitSrcValue(true, min, max));
        axis.setAutoRange(true);
        axis.setLabelInsets(new RectangleInsets(0.1, 0.1, 0.1, 0.1));
        axis.setAutoTickUnitSelection(true);
        return axis;
    }


    private XYPlot makeSubPlot(XYDataset dataset, NumberAxis range, XYItemRenderer renderer) {
        XYPlot plot = new XYPlot(dataset, null, range, renderer);
        plot.setBackgroundPaint(BACKGROUND_COLOR);
        plot.setDomainGridlinesVisible(false);
        final BasicStroke stroke = new BasicStroke(0.1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0.1f, new float[] {4.0f, 2.0f}, 0);
        plot.setRangeGridlineStroke(stroke);
        return plot;
    }

    private XYItemRenderer getRenderer(String title) {
        XYItemRenderer renderer;
        if (title.equals(POSITION_LABEL)) {// заливка под позицией
            renderer = new XYStepAreaRenderer();
        }
        else {
            renderer = new StandardXYItemRenderer();
            // renderer = new XYStepRenderer();
        }
        return renderer;
    }

    private  CombinedDomainXYPlot getPlotCombine() {
        CombinedDomainXYPlot plot = (CombinedDomainXYPlot)this.getXYPlot();
        plot.setGap(-0.5);
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);
        return plot;
    }

    /**
     * возвращаем ось X (DomainAxis)
     * @return NumberAxis
     */
    public NumberAxis getDomainAxis(XYPlot plot) {
        NumberAxis axis = (NumberAxis) plot.getDomainAxis();
        axis.setTickLabelFont(tickFont);
        axis.setTickLabelPaint(FONT_TICK_COLOR);
        axis.setStandardTickUnits(new NumberTickUnitSrcTime(chartDataset));
        if (chartDataset.isTime()) {
          //  axis.setStandardTickUnits(new NumberTickUnitSrcTime(chartDataset));
            maxRange = MAX_RANGE;
        }
        else
            maxRange = MAX_RANGE_COORDINATE;
        axis.setRange(0, maxRange / 60f);
        return axis;
    }

    public UtilsArmG.MutableDouble getBoundUpper() {
        return boundUpper;
    }

    private void setAnnotationsMap(XYPlot plot) {
        stations.setAnnotationsToPlot(plot);
        objects.addAnnotationsTrafficLight(plot);
        objects.addAnnotationThermometer(plot);
        objects.addAnnotationNeutralInsert(plot);
        objects.addAnnotationCrossing(plot);
        objects.addAnnotationCheckBrake(plot);
        objects.addAnnotationLandslide(plot);
    }

//    private void setAnnotationMapLimit(XYPlot plot) {
//        objects.addAnnotationMapLimit(plot);
//    }

//    /**
//     * скроллинг графики левая граница = 0
//     * @param scroll	- ScrollBar
//     * @param bndUp		- правая граница
//     */
//     public void setScroll(JScrollBar scroll, double bndUp) {
//        double x = scroll.getValue();
//       // int index = (arrTrains != null && iTrain < 0) ? arrTrains.getIndexFromX(this.dataset, x) : iTrain;
//       // if (chartArm != null) chartArm.setXMarkerTrainLabel(index, x);
//        if (domainAxis != null) domainAxis.setRange(0 + x, bndUp + x);
//     }

    /**
     * установка позиции вертикалтного курсора
     * @param x - позиция курсора
     */
    public void setXMarker(double x) {
        if (x >= 0 && x <= getSecondCoordinate())
            for (ValueMarker curMarker : valueMarkers) {
                curMarker.setValue(x);
            }
    }

    public void setIntervalXMarker(double x_start, double x_end) {
        if (x_end >= x_start && x_start >= 0 && x_end <= getSecondCoordinate())
            for (Marker intervalMarker : intervalMarkers) {
                ((IntervalMarker)intervalMarker).setStartValue(x_start);
                ((IntervalMarker)intervalMarker).setEndValue(x_end);
            }
    }

    /**
     * настройка вертикального курсора ValueMarker для отдельной XYPlot
     * @param plot - XYPlot
     */
    private void addMarker(XYPlot plot) {
        ValueMarker marker = new ValueMarker(0);
        marker.setPaint(Color.WHITE);
        marker.setStroke(new BasicStroke(0.8f));
        plot.addDomainMarker(marker);
        valueMarkers.add(marker);

        IntervalMarker intervalMarker = new IntervalMarker(0, 0);
        intervalMarker.setPaint(Color.WHITE);
        plot.addDomainMarker(intervalMarker);
        intervalMarker.setPaint(new Color(0x416A9EFD, true));
        intervalMarkers.add(intervalMarker);
    }

    /**
     * установка позиции вспомогательного маркера информации о поезде
     * @param index - индекс поезда
     * @param x - позиция маркера информации о поезде
     */
    public void setXMarkerTrainLabel(int index, double x) {
        if (index >= 0 && index < trainLabelMarkers.size())				// список поездов на канве
                ( (ValueMarker) trainLabelMarkers.get(index)).setValue(x);
    }

    /**
     * добавление линий - разделителя поездов
     * @param plot - XYPlot
     */
    private void addMarkerTrains(XYPlot plot, ArrTrains arrTrains) {
        	// добавляем линии поездов
            for (int i = 0; i < arrTrains.size(); i++)
                addMarkerTrain(plot, arrTrains.get(i), false);
    }

    /**
     * добавлене линии разделителя одного поезда
     * @param plot - XYPlot
     * @param train - поезд
     */
    private void addMarkerTrain(XYPlot plot, Train train, boolean isSingle) {
        double x = train.getSecondStart();
        ValueMarker marker = new ValueMarker(x);
        marker.setPaint(new Color(0x696969));
        marker.setStroke(new BasicStroke(0.8f));
        plot.addDomainMarker(marker);
        addTextMarkerTrain(plot, marker, train, isSingle);
    }

    /**
     * устанавливаем вспомогательный маркер для текста разделителей поездов
     * @param plot - XYPlot
     * @param mainMarker маркер разделителя поезда
     * @param train	поезд
     */
    @SuppressWarnings("unchecked")
    private void addTextMarkerTrain(XYPlot plot, ValueMarker mainMarker, Train train, boolean isSingle) {
        String txt = "";
        int seconds = train.getSecondsEnd() - train.getSecondStart();
        if (plotCombine.getSubplots().size() == 0 && seconds > 180) {
            txt = String.format(" №%s %s-%s таб:%s",
                    train.getNumTrainStr(),
                    Train.getNameTypeLoc(train.getTypeLoc(), train.getLocTypeAsoup()),
                    train.getNumLoc(),
                    train.getNumTab());
        }
        ValueMarker trMarker = new ValueMarker(mainMarker.getValue());
        trMarker.setStroke(new BasicStroke(0.1f));
        trMarker.setLabelPaint(Color.WHITE);
        trMarker.setLabelFont(trMarkerFont);
        trMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        trMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        trMarker.setLabel(txt);
        trainLabelMarkers.add(trMarker);
        plot.addDomainMarker(trMarker);
    }

    /**
     * перемещение вертикального курсора на один шаг
     * @param step - размер шага
     * @param isForward - направление
     * @param isSelect - с выделением
     */
    void stepMarker(int step, boolean isForward, boolean isSelect) {
        int direct = isForward ? 1 : -1;
        double x = getXMarker();
        x += step * direct;
        if (x > 0 && x <= getSecondCoordinate())
            setXMarker(x);
        if (!isSelect)
            setIntervalXMarker(x, x);
    }

    /**
     * перемещение вертикального курсора на один шаг с выделением интервала
     * @param step - размер шага
     * @param isForward - направление
     */
    void stepIntervalMarker(int step, boolean isForward) {
        int direct = isForward ? 1 : -1;
        double x = getXMarker();
        double x_start = ((IntervalMarker)intervalMarkers.get(0)).getStartValue();
        double x_end = ((IntervalMarker)intervalMarkers.get(0)).getEndValue();
        x = x + step * direct;
        if (x > 0 && x <= getSecondCoordinate()) {
            if (x >= x_start) {
                if (isForward) {
                    if (x > x_end)
                        x_end = x;
                    else
                        x_start = x;
                } else
                    x_end = x;
            }
            else {
                x_start = x;
            }
            setIntervalXMarker(x_start, x_end);
        }
    }

    /**
     * @return позиция вертикального курсора
     */
    public double getXMarker() {
        if (valueMarkers != null && valueMarkers.size() > 0)
            return valueMarkers.get(0).getValue();
        else
            return 0;
    }

    public double getEndIntervalXMarker() {
        if (intervalMarkers != null && intervalMarkers.size() > 0)
            return ((IntervalMarker)intervalMarkers.get(0)).getEndValue();
        else
            return 0;
    }

    public double getStartIntervalXMarker() {
        if (intervalMarkers != null && intervalMarkers.size() > 0)
            return ((IntervalMarker)intervalMarkers.get(0)).getStartValue();
        else
            return 0;
    }

    /**
     * прорисовка вертикального курсора
     * @param chartPanelArmMain - если есть второе онко, иначе null
     * @param scrollBar -
     * @param x -
     */
    public void paintMarker(ChartPanelArm chartPanelArmMain, ChartPanelArm chartPanelArmSlave, ScrollBarArm scrollBar, int x) {
        if (getDomainAxis() == null) return;
        double lower = getDomainAxis().getRange().getLowerBound();
        double upper = getDomainAxis().getRange().getUpperBound();
        if (scrollBar != null) {
            if (x > upper) scrollBar.setValue((int) (lower + (x - upper)) + (int) (upper - lower) / 2);
            if (x < lower) scrollBar.setValue((int) (lower - (lower - x)) - (int) (upper - lower) / 2);
        }
        setXMarker(x);
        //setRowHexTab();

        if (chartPanelArmMain != null && chartPanelArmSlave == null) {
            LocalDateTime dateTime = chartDataset.getDateTime(x);
            int n = chartPanelArmMain.getChartDataset().get_xByDateTime(dateTime);
            chartPanelArmMain.getChartArm().paintMarker(null, null, null, n);

            chartPanelArmMain.getChartArm().setRowHexTab();
            chartPanelArmMain.getInfoPanel().repaint();
        }

        if (chartPanelArmSlave != null && chartPanelArmMain == null) {
            LocalDateTime dateTime = chartDataset.getDateTime(x);
            int n = chartPanelArmSlave.getChartDataset().get_xByDateTime(dateTime);
            chartPanelArmSlave.getChartArm().paintMarker(null, null, null, n);

            chartPanelArmSlave.getChartArm().setRowHexTab();
            chartPanelArmSlave.getInfoPanel().repaint();
        }
    }

//    /**
//     * установка курсора в соответсвии с датой временем
//     * @param dateTime - дата время
//     */
//    public void drawMarker(ScrollBarArm scrollBar, LocalDateTime dateTime) {
//        int x = chartDataset.get_xByDateTime(dateTime);
//        drawMarker(null, scrollBar, x);
//    }

    /**
     * перемещение вертикального курсора на один шаг со сдвижкой scrollBar
     * @param step - величина шага
     * @param isForward - направление
     * @param isSelect - с выделением участка
     */
    public void stepCursor(ScrollBarArm scrollBar, int step, boolean isForward, boolean isSelect) {
        this.isSelected = isSelect;
        double lower = domainAxis.getRange().getLowerBound();
        double upper = domainAxis.getRange().getUpperBound();
        int d = (int)((upper - lower) / 1500);
        if (isForward) {
            if (isSelect)
                stepIntervalMarker(step + d, true);
            stepMarker(step + d, true, isSelect);
            if (getXMarker() > upper - step) {
                scrollBar.setValue((int)lower + (int)(getXMarker() - upper) + 1);
            }
        }
        else {
            if (isSelect)
                stepIntervalMarker(step + d, false);
            stepMarker(step + d, false, isSelect);
            if (getXMarker() <= lower) {
                scrollBar.setValue((int)lower - (int)(lower - getXMarker()) - 1);
            }
        }
        // установка строки для hexTab
        setRowHexTab();
    }

    /**
     * установка строки в таблице в соответсвии с положением вертикального курсора
     */
    public void setRowHexTab() {
        if (hexTab == null) return;
        double x = getXMarker();
        int row;
        ArrBlock32 arrBlock32;
        if (chartDataset.isTime()) {
            //    row = chartDataset.getChartArrays().getNBlockBySecond((int)x);
            arrBlock32 = chartDataset.getArrBlock32();
            row = arrBlock32.searchIndexBySecond((int) x, 0, arrBlock32.size() - 1);
        }
        else {
            arrBlock32 = chartDataset.getArrBlock32();
            row = arrBlock32.searchIndexBySecond((int)x, 0, arrBlock32.size() - 1);
        }
      //  System.out.println((int)x + "_" + Math.abs(row));
        if (row >= 0)
            hexTab.selectRow(row);
    }

    /**
     *  изменение масштаба относительно вертикального курсора
     * @param scrollBar -
     * @param xMarker -
     * @param step шаг увеличения/уменьшения
     */
    public void doZoomByCursor(ScrollBarArm scrollBar, int xMarker, int step, int cnt) {
        double d1 = domainAxis.getRange().getUpperBound() - domainAxis.getRange().getLowerBound();
        for (int i = 0; i < cnt; i++)
            doZoom(step, scrollBar);
        double d2 = domainAxis.getRange().getUpperBound() - domainAxis.getRange().getLowerBound();
        double percent = (d1 - d2) * 100 / d1;
        double dmark = xMarker - domainAxis.getRange().getLowerBound();
        double dmarkproc = percent * dmark / 100;
        scrollBar.setValue(scrollBar.getValue() + (int)Math.round(dmarkproc));
//        return d2;
    }

    public void doZoomByCursor(ScrollBarArm scrollBar, int xMarker, double duration) {
        double duration_old = domainAxis.getRange().getUpperBound() - domainAxis.getRange().getLowerBound();
        doZoom(duration, scrollBar);
        double percent = (duration_old - duration) * 100 / duration_old;
        double dmark = xMarker - domainAxis.getRange().getLowerBound();
        double dmarkproc = percent * dmark / 100;
        scrollBar.setValue(scrollBar.getValue() + (int)Math.round(dmarkproc));
    }

    /**
     * изменение масштаба относительно начала координат
     * @param step - шаг увеличения/уменьшения
     */
    public void doZoom(final int step, JScrollBar scrollBar) {
        double lower = domainAxis.getRange().getLowerBound();
        double upper = domainAxis.getRange().getUpperBound();
        upper += (upper - lower) / step;
        final double duration = upper - lower;
        if (duration < minDuration || duration > maxDuration) return;   // ограничения
        boundUpper.set(duration);
        ((ScrollBarArm)scrollBar).changeMaximumSize();
        domainAxis.setRange(lower, upper);
        setAnnotationProfile(duration); // профиль устанавливаем в зависимости от масштаба
    }

    private void doZoom (double duration, JScrollBar scrollBar) {
        double upper = domainAxis.getRange().getLowerBound() + duration;
        if (duration < minDuration) duration = minDuration;
        if (duration > maxDuration) duration = maxDuration;
        boundUpper.set(duration);
        ((ScrollBarArm)scrollBar).changeMaximumSize();
        domainAxis.setRange(domainAxis.getRange().getLowerBound(), upper);
        isTextAnnotationProfile = false;
        setAnnotationProfile(duration); // профиль устанавливаем в зависимости от масштаба
    }

    /**
     * установка аннотацй профиля в зависимости от масштаба
     * @param duration -
     */
    public void setAnnotationProfile(double duration) {
        int LOW = 700;
        int HIGH = 10000;
        if (!chartDataset.isTime()) {
            LOW *= 20;
            HIGH *= 10;
        }

        if (duration < LOW && !isTextAnnotationProfile) {
            plotProfile.clearAnnotations();
            profiles.addAnnotationProfile(plotProfile, true);
            isTextAnnotationProfile = true;
            isAllAnnotationProfile = true;
        }
        if (duration >= LOW && duration < HIGH && isTextAnnotationProfile) {
            plotProfile.clearAnnotations();
            profiles.addAnnotationProfile(plotProfile, false);
            isTextAnnotationProfile = false;
            isAllAnnotationProfile = true;
        }
        if (duration >= HIGH && isAllAnnotationProfile) {
            plotProfile.clearAnnotations();
            isAllAnnotationProfile = false;
            isTextAnnotationProfile = true;

        }
    }

    /**
     * настройка Crosshair для отдельной XYPlot
     * @param plot - XYPlot
     */
    private void setCrosshair(XYPlot plot) {

        final BasicStroke domainStroke = new BasicStroke(0.23f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0.2f, new float[] {12.0f, 4.0f}, 0);
        final BasicStroke rangeStroke = new BasicStroke(0.27f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0.2f, new float[] {9.0f, 3.0f}, 0);
        // domain (вертикальн)
        plot.setDomainCrosshairPaint(new Color(0xF0E68C));
        plot.setDomainCrosshairLockedOnData(true);
        plot.setDomainCrosshairStroke(domainStroke);
        plot.setDomainCrosshairVisible(true);
        // range (горизонтальн)
        plot.setRangeCrosshairPaint(new Color(0xF0E68C));
        plot.setRangeCrosshairLockedOnData(true);
        plot.setRangeCrosshairStroke(rangeStroke);
        plot.setRangeCrosshairVisible(true);
    }

    /**
     * установка сохраненной видимости линий
     */
    @SuppressWarnings("unchecked")
    public void setViewLines(boolean isDef) {
        Map<String, Boolean> mapVisible = isDef ? SeriesLines.mapDefVisible : SeriesLines.getMapVisible();
        if (mapVisible.isEmpty()) return;
        for (Object ob : plotCombine.getSubplots()) {
            XYPlot subplot = (XYPlot) ob;
            if (subplot.getRangeAxis().getLabel().equals(TRAIN_LABEL)) continue;
            XYDataset ds = subplot.getDataset(0);
            XYItemRenderer rend = subplot.getRenderer();
            WeightBlocks.setWeightDef(subplot);
            double maxY = 0;
            double minY = 0;
            for (int i = 0; i < subplot.getSeriesCount(); i++) {
                if (ds instanceof XYSeriesCollection) {
                    // сохраняем текущую шкалу
                    ValueAxis curRange = subplot.getRangeAxis();
                    Range range = curRange.getRange();
                    double upper = range.getUpperBound();
                    double lower = range.getLowerBound();
                    curRange.setRange(lower, upper);
                    //
                    XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
                    LineKeys key = (LineKeys)ser.getKey();
                    Boolean isView = mapVisible.get(((LineKeys)ser.getKey()).getName());

                    if (key != LineKeys.MAP_DIRECT && key != LineKeys.MAP_LINE && key != LineKeys.PROFILE && key != LineKeys.PROFILE_DIRECT) {
                        rend.setSeriesVisible(i, isView, true);
                        setLimMap(subplot, key, !rend.isSeriesVisible(i));
                    }
//                    if (rend.isSeriesVisible(i)) {
//                        maxY = !Double.isNaN(ser.getMaxY()) ? Math.max(ser.getMaxY(), maxY) : 0;
//                    }
                    if (rend.isSeriesVisible(i)) {
                        maxY = !Double.isNaN(ser.getMaxY()) ? Math.max(ser.getMaxY(), maxY) : 0;
                        minY = !Double.isNaN(ser.getMinY()) ? Math.min(ser.getMinY(), minY) : 0;
                    }
                }
            }
            if (maxY != 0 || minY != 0) {
                ValueAxis curRange = subplot.getRangeAxis();
//                Range range = curRange.getRange();
                curRange.setRange(minY, maxY);
            }
//            if (maxY > 0) {
//                ValueAxis curRange = subplot.getRangeAxis();
//                Range range = curRange.getRange();
//                curRange.setRange(range.getLowerBound(), maxY + 3);
//            }
        }
        WeightBlocks.setModified(true);
        SeriesLines.setMapVisible(new HashMap<String, Boolean>(SeriesLines.mapDefVisible));
    }

    public ChartDataset getChartDataset() {
        return chartDataset;
    }

    public int getW_signal() {
        return weight_signal;
    }

    public Stations getStations() {
        return stations;
    }

    public Objects getObjects() {
        return objects;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public Limits getLimits() {
        return limits;
    }

    ///
    public void setTrainWeight(int weight) {
        if (plotTrain == null) return;
        WeightBlocks.setWeight_train(weight);
        plotTrain.setWeight(weight);
    }

    public void setMapWeight(int weight) {
        if (plotMap == null) return;
        plotMap.setWeight(getWeight_map(false));
        setWeight_map(weight);
    }

    public void setProfileWeight(int weight) {
        if (plotProfile == null) return;
        plotProfile.setWeight(getWeight_prof(false));
        setWeight_prof(weight);
    }

    public void setPositionWeight(int weight) {
        if (plotPosition == null) return;
        plotPosition.setWeight(getWeight_position(false));
        setWeight_position(weight);
    }

    public void setAlsnWeight(int weight) {
        if (plotAlsn == null) return;
        plotAlsn.setWeight(getWeight_alsn(false));
        setWeight_alsn(weight);
    }

    public void setAlsnClubWeight(int weight) {
        if (plotAlsn_club == null) return;
        plotAlsn_club.setWeight(getWeight_alsn_club(false));
        setWeight_alsn_club(weight);
    }

    public void setAlsnBrWeight(int weight) {
        if (plotAlsn_br != null)
            plotAlsn_br.setWeight(getWeight_alsn_br(false));
        if (plotAlsn_br_vl80 != null)
            plotAlsn_br_vl80.setWeight(getWeight_alsn_br(false));
        setWeight_alsn_br(weight);
    }

    public void setAutoWeight(int weight) {
        if (plotAuto == null) return;
        plotAuto.setWeight(getWeight_auto_drive(false));
        setWeight_auto_drive(weight);
    }

    public void setPneumaticWeight(int weight) {
        if (plotPneumatic == null) return;
        plotPneumatic.setWeight(getWeight_pneumatic1(false));
        setWeight_pneumatic1(weight);
    }

    public void setPneumatic2Weight(int weight) {
        if (plotPneumatic2 == null) return;
        plotPneumatic2.setWeight(getWeight_pneumatic2(false));
        setWeight_pneumatic2(weight);
    }

    public void setPneumaticUsavpWeight(int weight) {
        if (plotPneumaticUsavp == null) return;
        plotPneumaticUsavp.setWeight(getWeight_pneumatic_usavp(false));
        setWeight_pneumatic_usavp(weight);
    }

    public void setUatlWeight(int weight) {
        if (plotUatl == null) return;
        plotUatl.setWeight(getWeight_uatl(false));
        setWeight_uatl(weight);
    }

    public void setKkmWeight_s5k(int weight) {
        if (plotKkm_s5k == null) return;
        plotKkm_s5k.setWeight(getWeight_kkm(false));
        setWeight_kkm(weight);
    }

    public void setKkmWeight_s5(int weight) {
        if (plotKkm_s5 == null) return;
        plotKkm_s5.setWeight(getWeight_kkm_s5(false));
        setWeight_kkm_s5(weight);
    }

    public void setKkmWeight_s5k_2(int weight) {
        if (plotKkm_s5k_2 == null) return;
        plotKkm_s5k_2.setWeight(getWeight_kkm2(false));
        setWeight_kkm2(weight);
    }

    public void setKkmWeight_vl10(int weight) {
        if (plotKkm_vl10 == null) return;
        plotKkm_vl10.setWeight(getWeight_kkm_vl10(false));
        setWeight_kkm_vl10(weight);
    }

    public void setBhvWeight(int weight) {
        if (plotBHV == null) return;
        plotBHV.setWeight(getWeight_bhv(false));
        setWeight_bhv(weight);
    }

    public void setVscWeight(int weight) {
        if (plotVsc == null) return;
        plotVsc.setWeight(getWeight_vsc(false));
        setWeight_vsc(weight);
    }

    public  void setCabinWeight(int weight) {
        if (plotCabin == null) return;
        plotCabin.setWeight(getWeight_cabin(false));
        setWeight_cabin(weight);
    }

    public  void setMainControlWeight(int weight) {
        if (plotMainControl == null) return;
        plotMainControl.setWeight(getWeight_main_control(false));
        setWeight_main_control(weight);
    }

    public  void setRevControlWeight(int weight) {
        if (plotRevControl == null) return;
        plotRevControl.setWeight(getWeight_rev_control(false));
        setWeight_rev_control(weight);
    }

    public  void setSchemaWeight(int weight) {
        if (plotSchema == null) return;
        plotSchema.setWeight(getWeight_schema(false));
        setWeight_schema(weight);
    }

    public void setPushKeyWeight(int weight) {
        if (plotPushKey == null) return;
        plotPushKey.setWeight(getWeight_push_key(false));
        setWeight_push_key(weight);
    }

    public void setSignalsWeight(int weight) {
        if (plotSignals == null) return;
        plotSignals.setWeight(getWeight_signals(false));
        setWeight_signals(weight);
    }

    public void setAutodisWeight(int weight) {
        if (plotAutodis == null) return;
        plotAutodis.setWeight(getWeight_signals_autodis(false));
        setWeight_signals(weight);
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public XYPlot getPlotSpeed() {
        return plotSpeed;
    }

    public XYPlot getPlotVoltage() {
        return plotVoltage;
    }

    public XYPlot getPlotVoltage2() {
        return plotVoltage2;
    }

    public XYPlot getPlotVoltage3() {
        return plotVoltage3;
    }

    public XYPlot getPlotVoltage4() {
        return plotVoltage4;
    }

    public XYPlot getPlotAmperage_common() {
        return plotAmperage_common;
    }

    public XYPlot getPlotConsumption() {
        return plotConsumption;
    }

    public XYPlot getPlotPress() {
        return plotPress;
    }

    public XYPlot getPlotTrain() {
        return plotTrain;
    }

    public XYPlot getPlotVoltage_cs() {
        return plotVoltage_cs;
    }

    public XYPlot getPlotAmperage_anchor() {
        return plotAmperage_anchor;
    }

    public XYPlot getPlotAmperage_excitation() {
        return plotAmperage_excitation;
    }

    public XYPlot getPlotAmperage_engine() {
        return plotAmperage_engine;
    }

    public XYPlot getPlotAmperage() {
        return plotAmperage;
    }

    public XYPlot getPlotAmperage3() {
        return plotAmperage3;
    }

    public XYPlot getPlotAmperage4() {
        return plotAmperage4;
    }

    public XYPlot getPlotPower() {
        return plotPower;
    }

    public XYPlot getPlotTail() {
        return plotTail;
    }

    public XYPlot getPlotProfile() {
        return plotProfile;
    }

    public XYPlot getPlotPosition() {
        return plotPosition;
    }

    public XYPlot getPlotMap() {
        return plotMap;
    }

    public XYPlot getPlotAlsn() {
        return plotAlsn;
    }

    public XYPlot getPlotAlsn_club() {
        return plotAlsn_club;
    }

    public XYPlot getPlotAlsn_br() {
        return plotAlsn_br;
    }

    public XYPlot getPlotAlsn_br_vl80() {
        return plotAlsn_br_vl80;
    }

    public XYPlot getPlotAuto() {
        return plotAuto;
    }

    public XYPlot getPlotPneumatic() {
        return plotPneumatic;
    }

    public XYPlot getPlotPneumatic2() {
        return plotPneumatic2;
    }

    public XYPlot getPlotPneumaticUsavp() {
        return plotPneumaticUsavp;
    }

    public XYPlot getPlotUatl() {
        return plotUatl;
    }

    public XYPlot getPlotKkm_s5k() {
        return plotKkm_s5k;
    }

    public XYPlot getPlotKkm_s5k_2() {
        return plotKkm_s5k_2;
    }

    public XYPlot getPlotKkm_vl10() {
        return plotKkm_vl10;
    }

    public XYPlot getPlotKkm_s5() {
        return plotKkm_s5;
    }

    public XYPlot getPlotBHV() {
        return plotBHV;
    }

    public XYPlot getPlotVsc() {
        return plotVsc;
    }

    public XYPlot getPlotCabin() {
        return plotCabin;
    }

    public XYPlot getPlotMainControl() {
        return plotMainControl;
    }

    public XYPlot getPlotRevControl() {
        return plotRevControl;
    }

    public XYPlot getPlotSchema() {
        return plotSchema;
    }

    public XYPlot getPlotPushKey() {
        return plotPushKey;
    }

    public XYPlot getPlotSignals() {
        return plotSignals;
    }

    public XYPlot getPlotAutodis() {
        return plotAutodis;
    }

    public XYPlot getPlotSignBhv() {
        return plotSignBhv;
    }

    public XYPlot getPlotTed() {
        return plotTed;
    }

    public XYPlot getPlotTed_s5k() {
        return plotTed_s5k;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
