package avpt.gr.chart_dataset;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.common.UtilsArmG;
import avpt.gr.train.Train;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import avpt.gr.train.ArrTrains;

import java.util.ArrayList;

/**
 * контейнер для:
 * arrBlock32 - массив 32-х байтных посылок
 * chartArrays - массивы линий, сигналов, задач (ListLines, ListSignals, ListTasks)
 * seriesLines - набор из XYSeriesCollection (XYSeriesCollection - набор линий для одного блока)
 */
public class ChartDataset {

    private ArrBlock32 arrBlock32;
    private ChartArrays chartArrays;
    private SeriesLines seriesLines;
    private SeriesSignalsDiscrete seriesSignalsDiscrete;
    private SeriesSignalsAutodisp seriesSignalsAutodisp;
    private SeriesSignalsBHV seriesSignalsBHV;
    private SeriesSignalsTed seriesSignalsTed;
    private SeriesSignalsTed_s5k seriesSignalsTed_s5k;
    private SeriesSignalsLink seriesSignalsLink;

    private TaskAlsn seriesAlsn;
    private TaskAlsnClub seriesAlsnClub;
    private TaskAlsnBr seriesAlsnBr_vl10;
    private TaskAlsnBr_vl80 seriesAlsnBr_vl80;
    private TaskAutoDrive seriesAutoDrive;
    private TaskPneumatic seriesPneumatic;
    private TaskPneumatic seriesPneumatic2;
    private TaskPneumaticUsavp seriesPneumaticUsavp;
    private TaskUatl seriesUatl;
    private TaskKM130 seriesKM130;
    private TaskKKM_s5k seriesKKM_s5k;
    private TaskKKM_s5k_2 seriesKKM_s5k_2;
    private TaskKKM_vl10 seriesKKM_vl10;
    private TaskKKM_kz8  seriesKKM_kz8;
    private TaskKKM_s5  seriesKKM_s5;
    private TaskBHV seriesBhv_valve;
    private TaskBHV seriesBhv_command;
    private TaskBHV seriesBhv_voltage;
    private TaskVsc seriesVsc;
    private TaskCabin seriesCabin;
    private TaskPushKey seriesPushKey;
    private TaskMainControl seriesMainControl;
    private TaskRevControl seriesRevControl;
    private TaskSchema seriesSchema;

//    private ArrayList<LocalDate> listDate = new ArrayList<LocalDate>();
//    private ArrayList<LocalTime> listTime = new ArrayList<LocalTime>();
//    private ArrayList<Double> listSpeedLimTmp = new ArrayList<Double>();
    private ArrayList<ChartArrays.RailCoordinate> listRailCoordinate = new ArrayList<ChartArrays.RailCoordinate>();
    private ArrayList<ChartArrays.GpsCoordinate> listGpsCoordinate = new ArrayList<ChartArrays.GpsCoordinate>();
    private ArrayList<LocalDateTime> listDateTime = new ArrayList<LocalDateTime>();
    private ArrayList<int[]> listObjects = new ArrayList<int[]>();
    private ArrayList<int[]> listProfiles = new ArrayList<int[]>();
    private ArrayList<int[]> listLimits = new ArrayList<int[]>();
    private ArrayList<Integer> listCoordinates = new ArrayList<Integer>();
    private LocalDate curDate = null;
    private LocalTime curTime = null;
    private boolean isTime;

    private final ArrTrains arrTrains = new ArrTrains();

    public ChartDataset(ArrBlock32 arrBlock32, boolean isTime, boolean isFill) {
        this.arrBlock32 = arrBlock32;
        this.isTime = isTime;
        this.chartArrays = new ChartArrays(arrBlock32, arrTrains, isTime);
//        listTime = chartArrays.getListTime();
//        listDate = chartArrays.getListDate();
        listRailCoordinate = chartArrays.getListRailCoordinate();
        listGpsCoordinate = chartArrays.getListGpsCoordinate();
        //listSpeedLimTmp = chartArrays.getListSpeedLimTmp();
        listDateTime = chartArrays.getListDateTimes();
        listObjects = chartArrays.getListObjects();
        listProfiles = chartArrays.getListProfiles();
        listLimits = chartArrays.getListLimits();
        listCoordinates = chartArrays.getListCoordinates();
        if (isFill)
            chartArrays.fill(0, arrBlock32.size() - 1); // заполняем все
    }

    /**
     * @param secondStart - начальная секунда для отображения линии
     * @param secondEnd - завершающая секунда для отображения линии
     * @param precision - точность отображения линии
     *                  1 - точность как есть 1:1
     *                  N - точность 1:N (отображаем только каждую N-ю точку)
     */
    public void setSeriesLines(int secondStart, int secondEnd, int precision) {
        this.seriesLines = new SeriesLines(chartArrays.getListLines(), secondStart, secondEnd, precision);
    }

    public void setSeriesSignalsDiscrete() {
        this.seriesSignalsDiscrete = new SeriesSignalsDiscrete(chartArrays.getListSignals());
    }

    public void setSeriesSignalsAutodisp() {
        this.seriesSignalsAutodisp = new SeriesSignalsAutodisp(chartArrays.getListSignals());
    }

    public void setSeriesSignalsBHV() {
        seriesSignalsBHV = new SeriesSignalsBHV(chartArrays.getListSignals());
    }

    public void setSeriesSignalsTed() {
        this.seriesSignalsTed = new SeriesSignalsTed(chartArrays.getListSignals());
    }

    public void setSeriesSignalsTed_s5k() {
        this.seriesSignalsTed_s5k = new SeriesSignalsTed_s5k(chartArrays.getListSignals());
    }

    public void setSeriesSignalsLink() {
        this.seriesSignalsLink = new SeriesSignalsLink((chartArrays.getListSignals()));
    }

    public void setSeriesAlsn() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListALSN_USAVP());
        this.seriesAlsn = new TaskAlsn(list, false);
    }

    public void setSeriesAlsnClub() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListALSN_CLUB());
        this.seriesAlsnClub = new TaskAlsnClub(list, false);
    }

    public void setSeriesAlsnBr_vl10() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListALSN_vl10_1());
        list.add(chartArrays.getListTasks().getListALSN_vl10_2());
        this.seriesAlsnBr_vl10 = new TaskAlsnBr(list, false);
    }

    public void setSeriesAlsnBr_vl80() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListALSN_vl80_1());
        list.add(chartArrays.getListTasks().getListALSN_vl80_2());
        this.seriesAlsnBr_vl80 = new TaskAlsnBr_vl80(list, false);
    }

    public void setSeriesAuto() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListAuto());
        this.seriesAutoDrive = new TaskAutoDrive(list, false);
    }

    public void setSeriesPneumatic() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListPneumatic1());
        list.add(chartArrays.getListTasks().getListPneumaticStatusBlk());
        list.add(chartArrays.getListTasks().getListPneumaticSrcTask());
        list.add(chartArrays.getListTasks().getListPneumatic2());

        this.seriesPneumatic = new TaskPneumatic(list, false);
        if (arrTrains != null && arrTrains.size() > 0)
            this.seriesPneumatic.setType_loc(arrTrains.get(0).getTypeLoc());
    }

    public void setSeriesUatl() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListUatlMode());
        list.add(chartArrays.getListTasks().getListUatlWork());
        list.add(chartArrays.getListTasks().getListUatlCommand());
        this.seriesUatl = new TaskUatl(list, false);
    }

    public void setSeriesKM130() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKM130_StatusSystemBrake());
        list.add(chartArrays.getListTasks().getListKM130_VirPosKM());
        this.seriesKM130 = new TaskKM130(list, false);
    }

//    public void setSeriesPneumatic2() {
//        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
//        ArrayList<ListTasks.ItemTask> tasks = chartArrays.getListTasks().getListPneumatic2();
//        if (tasks.size() == 1 && tasks.get(0).value == 0) return;
//        list.add(tasks);
//        this.seriesPneumatic2 = new TaskPneumatic(list, false);
//    }

    public void setSeriesPneumaticUsavp() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListPneumaticUsavp());
        this.seriesPneumaticUsavp = new TaskPneumaticUsavp(list, false);
    }

    public void setSeriesKKM_s5k() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKkm());
        this.seriesKKM_s5k = new TaskKKM_s5k(list, false);
    }

    public void setSeriesKKM_s5k_2() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKkm2());
        this.seriesKKM_s5k_2 = new TaskKKM_s5k_2(list, false);
    }

    public void setSeriesKKM_vl10() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKkm_1());
        list.add(chartArrays.getListTasks().getListKkm_2());
        this.seriesKKM_vl10 = new TaskKKM_vl10(list, false);
    }

    public void setSeriesKKM_kz8() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKkm_kz8());
        list.add(chartArrays.getListTasks().getListKkbt_kz8());
        this.seriesKKM_kz8 = new TaskKKM_kz8(list, false);
    }

    public void setSeriesKKM_s5() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListKkm_s5());
        list.add(chartArrays.getListTasks().getListKkbt_s5());
        this.seriesKKM_s5 = new TaskKKM_s5(list, false);
    }

    public void setSeriesBhv() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListBHV_valve());
        list.add(chartArrays.getListTasks().getListBHV_command());
        list.add(chartArrays.getListTasks().getListBHV_voltage());
        this.seriesBhv_valve = new TaskBHV(list, false);
    }

    public void setSeriesPushKey() {
        boolean isEmpty = true;
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        if (chartArrays.getListTasks().getListKeyStart().size() > 1 ||
                chartArrays.getListTasks().getListKeyF1().size() > 1 ||
                chartArrays.getListTasks().getListKeyEnter().size() > 1 ||
                chartArrays.getListTasks().getListKeyEsc().size() > 1 ||
                chartArrays.getListTasks().getListKeyDel().size() > 1 ||
                chartArrays.getListTasks().getListKeyUp().size() > 1 ||
                chartArrays.getListTasks().getListKeyDown().size() > 1 ||
                chartArrays.getListTasks().getListKeyLeft().size() > 1 ||
                chartArrays.getListTasks().getListKeyRight().size() > 1 ||
                chartArrays.getListTasks().getListKeyZero().size() > 1 ||
                chartArrays.getListTasks().getListKeyOne().size() > 1 ||
                chartArrays.getListTasks().getListKeyTwo().size() > 1 ||
                chartArrays.getListTasks().getListKeyThree().size() > 1 ||
                chartArrays.getListTasks().getListKeyFour().size() > 1 ||
                chartArrays.getListTasks().getListKeyFive().size() > 1 ||
                chartArrays.getListTasks().getListKeySix().size() > 1 ||
                chartArrays.getListTasks().getListKeySeven().size() > 1 ||
                chartArrays.getListTasks().getListKeyEight().size() > 1 ||
                chartArrays.getListTasks().getListKeyNine().size() > 1)
            isEmpty = false;
        list.add(chartArrays.getListTasks().getListKeyStart());     // 0
        list.add(chartArrays.getListTasks().getListKeyF1());        // 1
        list.add(chartArrays.getListTasks().getListKeyEnter());     // 2
        list.add(chartArrays.getListTasks().getListKeyEsc());       // 3
        list.add(chartArrays.getListTasks().getListKeyDel());       // 4
        list.add(chartArrays.getListTasks().getListKeyUp());        // 5
        list.add(chartArrays.getListTasks().getListKeyDown());      // 6
        list.add(chartArrays.getListTasks().getListKeyLeft());      // 7
        list.add(chartArrays.getListTasks().getListKeyRight());     // 8
        list.add(chartArrays.getListTasks().getListKeyZero());      // 9
        list.add(chartArrays.getListTasks().getListKeyOne());       // 10
        list.add(chartArrays.getListTasks().getListKeyTwo());       // 11
        list.add(chartArrays.getListTasks().getListKeyThree());     // 12
        list.add(chartArrays.getListTasks().getListKeyFour());      // 13
        list.add(chartArrays.getListTasks().getListKeyFive());      // 14
        list.add(chartArrays.getListTasks().getListKeySix());       // 15
        list.add(chartArrays.getListTasks().getListKeySeven());     // 16
        list.add(chartArrays.getListTasks().getListKeyEight());     // 17
        list.add(chartArrays.getListTasks().getListKeyNine());      // 18

        if (!isEmpty)
            this.seriesPushKey = new TaskPushKey(list, true);
    }

    public void setSeriesVsc() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListVsc());
        this.seriesVsc = new TaskVsc(list, false);
    }

    public void setSeriesCabin() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListCabin());
        this.seriesCabin = new TaskCabin(list, false);
    }

    public void setSeriesMainControl() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListMainControl());
        this.seriesMainControl = new TaskMainControl(list, false);
    }

    public void setSeriesRevControl() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListRevControl());
        this.seriesRevControl = new TaskRevControl(list, false);
    }

    public void setSeriesSchema() {
        ArrayList<ArrayList<ListTasks.ItemTask>> list = new ArrayList<ArrayList<ListTasks.ItemTask>>();
        list.add(chartArrays.getListTasks().getListSchema());
        this.seriesSchema = new TaskSchema(list, false);
    }

    public int[] getValLimit(int x) {
        if (x > 0 && x < listLimits.size()) {
            int[] val = listLimits.get(x);
            if (val != null && val[0] == 3)
                return val;
            else
                return null;
        }
        else return  null;
    }

    public int[] getValProfile(int x) {
        if (x >= 0 && x < listProfiles.size()) {
            int[] val = listProfiles.get(x);
            if (val != null && val[0] == 1)
                return val;
            else
                return null;
        }
        else return null;
    }

    public int[] getValObject(int x, int step) {
      //  final int d = step;
//        System.out.println(step);
        if (x >= 0 && x < listObjects.size()) {
            int xx = x;
            for (int i = Math.max(x - step, 0); i < Math.min(x + step, listObjects.size()); i++) {
                int[] val = listObjects.get(i);
                if (val != null) {
                    xx = i;
                    break;
                }
            }
            return listObjects.get(xx);
        }
        else
            return null;
    }

//    /**
//     * @param x -
//     * @return - значение временного ограничения скорости
//     */
//    public Double getSpeedLimitTmp(int x) {
//        if (x >= 0 && x < listSpeedLimTmp.size())
//            return listSpeedLimTmp.get(x);
//        else return null;
//    }

    /**
     * @param x -
     * @return - gps координата
     */
    public ChartArrays.GpsCoordinate getGpsCoordinate(int x) {
        if (x >= 0 && x < listGpsCoordinate.size())
            return listGpsCoordinate.get(x);
        else return null;
    }

    /**
     * @param x -
     * @return - железнодорожная координата
     */
    public ChartArrays.RailCoordinate getRailCoordinate(int x) {
        if (x >= 0 && x < listRailCoordinate.size())
            return listRailCoordinate.get(x);
        else
            return null;
    }

    /**
     * @param x координата (время)
     * @return текущее время
     */
    public LocalTime getTime(int x) {
//        if (x >= 0 && x < listTime.size())
//            return listTime.get(x);
//        else
//            return null;
        if (x >= 0 && x < listDateTime.size()) {
            LocalDateTime dateTime = listDateTime.get(x);
            if (dateTime != null)
                return dateTime.toLocalTime();
            else
                return null;
        }
        else
            return null;
    }

    public LocalDateTime getDateTime(int x) {
        if (x >= 0 && x < listDateTime.size()) {
            return listDateTime.get(x);
        }
        else
            return null;
    }

    public int get_xByDateTime(LocalDateTime dateTime) {
        int result = -1;
        for (int i = 0; i < listDateTime.size(); i++) {
            if (dateTime != null && listDateTime.get(i) != null && dateTime.compareTo(listDateTime.get(i)) <= 0) {
                result = i;
                break;
            }
        }
        return result;
    }

    public ArrBlock32 getArrBlock32() {
        return arrBlock32;
    }

    public ArrTrains getArrTrains() {
        return arrTrains;
    }

    public ListLines getListLines() {
        return chartArrays.getListLines();
    }

//    public ListSignals getListSignals() {
//        return chartArrays.getListSignals();
//    }
//
//    public ListTasks getListTasks() {
//        return chartArrays.getListTasks();
//    }

    public SeriesLines getSeriesLines() {
        return seriesLines;
    }

    public SeriesSignalsDiscrete getSeriesSignalsDiscrete() {
        return seriesSignalsDiscrete;
    }

    public SeriesSignalsAutodisp getSeriesSignalsAutodisp() {
        return seriesSignalsAutodisp;
    }

    public SeriesSignalsBHV getSeriesSignalsBHV() {
        return seriesSignalsBHV;
    }

    public SeriesSignalsTed getSeriesSignalsTed() {
        return seriesSignalsTed;
    }

    public SeriesSignalsTed_s5k getSeriesSignalsTed_s5k() {
        return seriesSignalsTed_s5k;
    }

    public SeriesSignalsLink getSeriesSignalsLink() {
        return seriesSignalsLink;
    }

    public TaskAlsn getSeriesAlsn() {
        return seriesAlsn;
    }

    public TaskAlsnClub getSeriesAlsnClub() {
        return seriesAlsnClub;
    }

    public TaskAlsnBr getSeriesAlsnBr_vl10() {
        return seriesAlsnBr_vl10;
    }

    public TaskAlsnBr_vl80 getSeriesAlsnBr_vl80() {
        return seriesAlsnBr_vl80;
    }

    public TaskAutoDrive getSeriesAutoDrive() {
        return seriesAutoDrive;
    }

    public TaskPneumatic getSeriesPneumatic() {
        return seriesPneumatic;
    }

    public TaskPneumaticUsavp getSeriesPneumaticUsavp() {
        return seriesPneumaticUsavp;
    }

    /**
     * @return пневматика 2-го
     */
    public TaskPneumatic getSeriesPneumatic2() {
        return seriesPneumatic2;
    }

    public TaskUatl getSeriesUatl() {
        return seriesUatl;
    }

    public TaskKM130 getSeriesKM130() {
        return seriesKM130;
    }

    public TaskKKM_s5k getSeriesKKM_s5k() {
        return seriesKKM_s5k;
    }

    public TaskBHV getSeriesBhv_valve() {
        return seriesBhv_valve;
    }

    public TaskKKM_s5k_2 getSeriesKKM_s5k_2() {
        return seriesKKM_s5k_2;
    }

    public TaskKKM_vl10 getSeriesKKM_vl10() {
        return seriesKKM_vl10;
    }

    public TaskKKM_kz8 getSeriesKKM_kz8() {
        return seriesKKM_kz8;
    }

    public TaskKKM_s5 getSeriesKKM_s5() {
        return seriesKKM_s5;
    }

    public TaskVsc getSeriesVsc() {
        return seriesVsc;
    }

    public TaskCabin getSeriesCabin() {
        return seriesCabin;
    }

    public TaskMainControl getSeriesMainControl() {
        return seriesMainControl;
    }

    public TaskRevControl getSeriesRevControl() {
        return seriesRevControl;
    }

    public TaskSchema getSeriesSchema() {
        return seriesSchema;
    }

    public TaskPushKey getSeriesPushKey() {
        return seriesPushKey;
    }

    public ChartArrays getChartArrays() {
        return chartArrays;
    }

    public boolean isTime() {
        return isTime;
    }

    //    public ArrayList<LocalDate> getListDate() {
//        return listDate;
//    }
//
//    public ArrayList<LocalTime> getListTime() {
//        return listTime;
//    }

    public ArrayList<LocalDateTime> getListDateTime() {
        return listDateTime;
    }

    public LocalDate getCurDate() {
        return curDate;
    }

    public LocalTime getCurTime() {
        return curTime;
    }

    /**
     * @param x - секунда
     * @return - текст даты для курсора и инфопанели
     */
    public String getDateText(int x) {
        final String TEXT = "Дата: ";
        LocalDateTime dateTime = getDateTime(x);
        return dateTime != null ? TEXT + dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : TEXT;
    }

    /**
     * @param x - секунда
     * @return - текст времени для курсора и инфопанели
     */
    public String getTimeText(int x) {
        final String TEXT = "Время: ";
        LocalDateTime dateTime = getDateTime(x);
        return dateTime != null ? TEXT + dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : TEXT;
    }

    /**
     * @param x - секунда
     * @return - текст железнодорожной координаты для курсора и инфопанели
     */
    public String getRailCoordinateText(int x) {
        final String TEXT = "ЖД координата: ";
        final String SPACE = "           ";
        ChartArrays.RailCoordinate railCoordinate = getRailCoordinate(x);
        if (railCoordinate != null) {
            int km = railCoordinate.getKm();
            int pk = railCoordinate.getPk();
            return km > -1 && pk > -1 ? TEXT + UtilsArmG.fixedLengthString(String.format("%sкм %sпк ", km, pk), 12) : TEXT + SPACE;
        }
        else return TEXT + SPACE;
    }

    /**
     * @param x - секунда
     * @return - текст широты для курсора и инфопанели
     */
    public String getLatitudeText(int x) {
        final String TEXT = "Широта: ";
        ChartArrays.GpsCoordinate gpsCoordinate = getGpsCoordinate(x);
        if (gpsCoordinate != null) {
            double lat = gpsCoordinate.getLat();
            return !Double.isNaN(lat) ? TEXT + UtilsArmG.fixedLengthString(String.format("%4.6f ", lat), 12) : TEXT;
        }
        else return TEXT;
    }

    /**
     * @param x - секунда
     * @return - текст долготы для курсора и инфопанели
     */
    public String getLongitudeText(int x) {
        final String TEXT = "Долгота:";
        ChartArrays.GpsCoordinate gpsCoordinate = getGpsCoordinate(x);
        if (gpsCoordinate != null) {
            double lon = gpsCoordinate.getLon();
            return !Double.isNaN(lon) ? TEXT + UtilsArmG.fixedLengthString(String.format("%4.6f ", lon), 12) : TEXT;
        }
        else return TEXT;
    }

    public int getCoordinate(int x) {
        if (x >= 0 && x < listCoordinates.size()) {
            return listCoordinates.get(x);
        }
        return 0;
    }

    public String getCoordinateText(int x) {
        Train train = getArrTrains().getTrain(getArrBlock32(), x);
        final String TEXT = "Пробег: ";

        if (train != null) {
            int begin_coord = train.getCoordinateStart();
            double distance = getCoordinate(x) - begin_coord;
            if (distance < 0) distance = 0;
            double val = distance / 1000.0;
            String str = String.format("%.3f км", val);
            return TEXT + str;
        }
        return TEXT;
    }

}
