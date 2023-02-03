package avpt.gr.reports;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.common.Roads;
import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartPanelArm;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.threeten.bp.LocalDateTime;
import avpt.gr.sqlite_base.CreateInsertSQLite;
import avpt.gr.sqlite_base.QueryingDataSQLite;
import avpt.gr.train.Train;

import java.awt.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static avpt.gr.common.HelperExl_POI.*;
import static avpt.gr.common.UtilsArmG.*;

public class ExcelReports {

    private static final String TEMP_INFO_TRAIN = "templates/InfoTrain.xlsx";
    private static final String TEMP_INFO_WAGS = "templates/InfoWags.xlsx";
    private static final String TEMP_INFO_SCHEDULE = "templates/InfoSchedule.xlsx";
    private static final String TEMP_CONSUMP_ENERGY = "templates/Consump.xlsx";
    private static final String TEMP_CONSUMP_SINGLE_ENERGY = "templates/ConsumpSingle.xlsx";
    private static final String TEMP_VIRT_COUP = "templates/VirtCoup.xlsx";
    private static final String TEMP_VIRT_COUP_ALL = "templates/VirtCoupAll.xlsx";
    private static final String TEMP_REP_TRAIN_ALL = "templates/RepTrainQuery.xlsx";
    private static final String TEMP_DIAGN_VSC = "templates/RepDiagnVscQuery.xlsx";
    private static final String TEMP_INFO_EMPTY = "templates/Empty.xlsx";

    private ArrBlock32 arrBlock32;
    private XSSFWorkbook book;

    // вид отчета
    public enum KindReport {
         info_tr        // информация о поезде
        ,info_wags      // информация о вагонах
        ,info_schedule  // информация о расписании
        ,consump_energy // расход энергии - выгрузка
        ,consump_single_energy // расход энергии - по одной секции
        ,virt_coup      // виртуальная сцепка для конкретного поезда - выгрузка
        ,virt_coup_all  // виртуальная сцепка для всех поездок - выгрузка

        ,trains_query // по поездам
        ,diagn_vsc_query // диагностика виртуальной сцепки
    }

//    /**
//     * режим работы
//     * 1 - стоянка
//     * 2 - тяга
//     * 3 - выбег
//     * 4 - рекуперация
//     * 5 - превматическое торможение
//     * 6 - комбинированное торможение
//     */
//    public static class ModeWork {
//        private final int code;
//        private final String name;
//
//        public ModeWork(int code, String name) {
//            this.code = code;
//            this.name = name;
//        }
//
//        public int getCode() {
//            return code;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }


    public ExcelReports(java.util.List<String> list, KindReport kindReport) throws FileNotFoundException {
        String outName = makeOutFileName(kindReport);
        InputStream in = getStreamTemplate(kindReport);
        book = getBookXLS(in);
        if (book != null) {
            XSSFSheet sheet = book.getSheetAt(0);
            switch (kindReport) {
                case info_tr: train_info(sheet, list);
                break;
                case info_wags: wags_info(sheet, list);
                break;
                case info_schedule: schedule_info(sheet, list);
            }
            try {
                writeToBook(outName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ExcelReports(ChartPanelArm chartPanelArm, Train train, KindReport kindReport) throws FileNotFoundException {
//        ArrBlock32 arrBlock32 = chartPanelArm.getChartDataset().getArrBlock32();
        String outName = makeOutFileName(kindReport);
        InputStream in = getStreamTemplate(kindReport);
        book = getBookXLS(in);
        if (book != null) {
            XSSFSheet sheet = book.getSheetAt(0);
            switch (kindReport) {
                case virt_coup:
                case virt_coup_all:
                    OutLoadVSC outLoadVSC = new OutLoadVSC(sheet, chartPanelArm, train);
                    outLoadVSC.outLoad();
                    break;
                case consump_energy:
                case consump_single_energy:
                    OutConsumptionEn outConsumptionEn = new OutConsumptionEn(sheet, chartPanelArm, train);
                    outConsumptionEn.outConsumptionEn();
                    break;
            }
            try {
                writeToBook(outName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ExcelReports(LocalDateTime beginDate, LocalDateTime endDate, KindReport kindReport,
                        RoadTchPan roadTchPan) throws FileNotFoundException, SQLException, ParseException {

        String outName = makeOutFileName(kindReport);
        InputStream in = getStreamTemplate(kindReport);
        book = getBookXLS(in);
        if (book != null) {
            XSSFSheet sheet = book.getSheetAt(0);
            boolean isWrite = true;
            switch (kindReport) {
                case trains_query:
                    isWrite = train_to_xlsx(beginDate, endDate, sheet, roadTchPan);
                    break;
                case diagn_vsc_query:
                    isWrite = diagn_vsc_to_xlsx(beginDate, endDate, sheet, roadTchPan);
                    break;
            }
            try {
                if(isWrite)  writeToBook(outName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean diagn_vsc_to_xlsx(LocalDateTime beginDate, LocalDateTime endDate, XSSFSheet sheet,
                                      RoadTchPan roadTchPan) throws SQLException, ParseException {

        long cnt = QueryingDataSQLite.getCount_trains(CreateInsertSQLite.getConnection(),
                beginDate, endDate, roadTchPan.getCodeRoad(), roadTchPan.getNumTch(), roadTchPan.getNumLoc(), roadTchPan.getTypeLoc());
        if (showMessMoreMax(cnt)) return false;

        String tch = (roadTchPan.getNumTch() > 0) ? Integer.toString(roadTchPan.getNumTch()) : "все";
        String road = (roadTchPan.getCodeRoad() > 0) ? Roads.getName(roadTchPan.getCodeRoad()) : "все";
        String loc = (roadTchPan.getTypeLoc() > 0) ?
                String.format("№%d %s", roadTchPan.getTypeLoc(), Train.getNameTypeLoc(roadTchPan.getTypeLoc(), roadTchPan.getCodeAsoup())) : "все";
        toCell(sheet, 0, 0,
                String.format("Диагностика ВСЦ с %s по %s Дорога: %s ТЧЭ-%s Локомотив: %s",
                        beginDate.format(formatDateTime), endDate.format(formatDateTime), road, tch, loc), null);

        ResultSet rs = QueryingDataSQLite.getResultSet_vsc_diagn(CreateInsertSQLite.getConnection(), beginDate, endDate,
                roadTchPan.getCodeRoad(), roadTchPan.getNumTch(), roadTchPan.getNumLoc(), roadTchPan.getTypeLoc(), roadTchPan.getCodeAsoup());

        int row = 3;
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int curId = 0;
        int col = 0;

        while (rs.next()) {
            col = 0;
            row++;
            toCell(sheet, row, col++, rs.getString("name_type_loc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("num_loc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, dateTimeFormat.parse(rs.getString("date_begin")), getStyleDateTime(book, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("train_num"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getString("route_name"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, UtilsArmG.round(rs.getDouble("seconds") / 3600.0, 2), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("distance") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("distance_auto") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("distance_auto_percent"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getString("type_vsc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("main_link_modem_perc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("main_link_vsc_perc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("slave_is_on_perc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("slave_link_modem_perc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("slave_link_vsc_perc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("num_link_loc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("test_thrust"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("test_brake"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("u_max"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("is_alsn"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, getShortNameFile(rs.getString("file_name")), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col++, rs.getInt("num_section"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
        }
        return true;
    }

    private boolean train_to_xlsx(LocalDateTime beginDate, LocalDateTime endDate, XSSFSheet sheet,
                                  RoadTchPan roadTchPan) throws SQLException, ParseException {

        long cnt = QueryingDataSQLite.getCount_trains(CreateInsertSQLite.getConnection(),
                beginDate, endDate, roadTchPan.getCodeRoad(), roadTchPan.getNumTch(), roadTchPan.getNumLoc(), roadTchPan.getTypeLoc());
        if (showMessMoreMax(cnt)) return false;

        String tch = (roadTchPan.getNumTch() > 0) ? Integer.toString(roadTchPan.getNumTch()) : "все";
        String road = (roadTchPan.getCodeRoad() > 0) ? Roads.getName(roadTchPan.getCodeRoad()) : "все";
        String loc = (roadTchPan.getTypeLoc() > 0) ?
                String.format("№%d %s", roadTchPan.getTypeLoc(), Train.getNameTypeLoc(roadTchPan.getTypeLoc(), roadTchPan.getCodeAsoup())) : "все";
        toCell(sheet, 0, 0,
                String.format("Поездки за период с %s по %s Дорога: %s ТЧЭ-%s Локомотив: %s",
                        beginDate.format(formatDateTime), endDate.format(formatDateTime), road, tch, loc), null);

        ResultSet rs = QueryingDataSQLite.getResultSet_trains(CreateInsertSQLite.getConnection(), beginDate, endDate,
                roadTchPan.getCodeRoad(), roadTchPan.getNumTch(), roadTchPan.getNumLoc(), roadTchPan.getTypeLoc(), roadTchPan.getCodeAsoup());

        int row = 3;
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        int curId = 0;
        int preId = 0;
        int col = 0;
        int col_en = 0;
        while (rs.next()) {
            curId = rs.getInt("train_id");
            int num = rs.getInt("num");
            if (curId != preId) {
                col = 0;
                row++;
                toCell(sheet, row, col++, dateTimeFormat.parse(rs.getString("date_begin")), getStyleDateTime(book, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("train_num"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("tab_num"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getString("name_type_loc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("num_loc"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getString("stations_start_end_name"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getDouble("work") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("distance") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("distance_auto") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("distance_auto_percent"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("distance_prompt") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("distance_prompt_percent"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, getDurationTime(rs.getInt("seconds")), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                col_en = col;
                col += 8;
                toCell(sheet, row, col++, rs.getDouble("speed"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getDouble("speed_move"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("v_lim_cnt"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("v_lim_len") / 1000.0, getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("wags_cnt"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("weight"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("wags_empty_cnt"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getString("route_name"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, getShortNameFile(rs.getString("file_name")), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, dateTimeFormat.parse(rs.getString("date_save")), getStyleDateTime(book, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getString("name_road"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("num_tch"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getString("ver_po"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, dateFormat.parse(rs.getString("date_map")), getStyleDate(book, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("is_shed_load"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
                toCell(sheet, row, col++, rs.getInt("num_section"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            }
            toCell(sheet, row, col_en++, rs.getInt("act"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col_en++, rs.getInt("rec"), getStyle(sheet, CellStyle.ALIGN_LEFT, true));

            preId = curId;
        }
        return true;
    }

    /**
     * @param cnt - строк в запросе
     * @return - true если строк больше максимального
     */
    private boolean showMessMoreMax(long cnt) {
        final int MAX_CNT = 100000;
        if (cnt > MAX_CNT) {
            String mess = String.format("Запрос имеет %d строк!\n" +
                    "Рекомендуется выбрать конкретный локомотив\n" +
                    "и/или уменьшить интервал времени", cnt);
            UtilsArmG.ShowDialog_Ok(mess, "Сообщение!", "Закрыть");
            return true;
        }
        return false;
    }

    private String makeOutFileName(KindReport kindReport) throws FileNotFoundException {
        String tempName = getNameTemplate(kindReport);
        File outDir = UtilsArmG.makeUserPath("reports", true);
        String outName = new File(outDir.getPath(),
                tempName.substring(tempName.lastIndexOf("/") + 1)).toString().replaceFirst("[.][^.]+$", "");
        outName += "_" + LocalDateTime.now().format(UtilsArmG.formatDateTimeForFileName) + ".xlsx";
        return outName;
    }

    /**
     * @param kindReport - вид отчета
     * @return - имя файла отчета
     */
    private String getNameTemplate(KindReport kindReport) {
        String result;
        switch (kindReport) {
            case info_tr:
                result = getClass().getResource(TEMP_INFO_TRAIN).toString();
                break;
            case info_wags:
                result = getClass().getResource(TEMP_INFO_WAGS).toString();
                break;
            case info_schedule:
                result = getClass().getResource(TEMP_INFO_SCHEDULE).toString();
                break;
            case consump_energy:
                result = getClass().getResource(TEMP_CONSUMP_ENERGY).toString();
                break;
            case consump_single_energy:
                result = getClass().getResource(TEMP_CONSUMP_SINGLE_ENERGY).toString();
                break;
            case virt_coup:
                result = getClass().getResource(TEMP_VIRT_COUP).toString();
                break;
            case virt_coup_all:
                result = getClass().getResource(TEMP_VIRT_COUP_ALL).toString();
                break;
            case trains_query:
                result = getClass().getResource(TEMP_REP_TRAIN_ALL).toString();
                break;
            case diagn_vsc_query:
                result = getClass().getResource(TEMP_DIAGN_VSC).toString();
                break;
            default:
                result = getClass().getResource(TEMP_INFO_EMPTY).toString();
                break;
        }
        return result.substring(result.indexOf('/') + 1);
    }

    /**
     * @param kindReport - вид отчета
     * @return - InputStream файла отчета
     */
    private InputStream getStreamTemplate(KindReport kindReport) {
        InputStream in;
        switch (kindReport) {
            case info_tr:
                in = getClass().getResourceAsStream(TEMP_INFO_TRAIN);
                break;
            case info_wags:
                in = getClass().getResourceAsStream(TEMP_INFO_WAGS);
                break;
            case info_schedule:
                in = getClass().getResourceAsStream(TEMP_INFO_SCHEDULE);
                break;
            case consump_energy:
                in = getClass().getResourceAsStream(TEMP_CONSUMP_ENERGY);
                break;
            case consump_single_energy:
                in = getClass().getResourceAsStream(TEMP_CONSUMP_SINGLE_ENERGY);
                break;
            case virt_coup:
                in = getClass().getResourceAsStream(TEMP_VIRT_COUP);
                break;
            case virt_coup_all:
                in = getClass().getResourceAsStream(TEMP_VIRT_COUP_ALL);
                break;
            case trains_query:
                in = getClass().getResourceAsStream(TEMP_REP_TRAIN_ALL);
                break;
            case diagn_vsc_query:
                in = getClass().getResourceAsStream(TEMP_DIAGN_VSC);
                break;
            default:
                in = getClass().getResourceAsStream(TEMP_INFO_EMPTY);
                break;
        }
        return in;
    }

    /**
     * @param in - InputStream файла шаблона
     * @return XSSFWorkbook
     */
    private XSSFWorkbook getBookXLS(final InputStream in) {
        try {
            return (XSSFWorkbook) WorkbookFactory.create(in);
        } catch (IOException e) {
            return null;
        } catch (InvalidFormatException e) {
            return null;
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * отчет excel - информация о поезде
     * @param sheet -
     * @param list -
     */
    public static void train_info(XSSFSheet sheet, java.util.List<String> list) {
        if (sheet == null) return;
        XSSFFont font_blue = makeFont(sheet.getWorkbook(), 12, false, true, 10);
        CellStyle styleRightCol = getStyle(sheet, CellStyle.ALIGN_LEFT, true);
        styleRightCol.setFont(font_blue);
        Iterator<String> iterator = list.iterator();
        iterator.next();
        int row = 0;
        int col = 0;
        toCell(sheet, row, col, iterator.next(), getStyle(sheet, CellStyle.ALIGN_CENTER, false));
        while (iterator.hasNext()) {
            toCell(sheet, ++row, col, iterator.next(), getStyle(sheet, CellStyle.ALIGN_LEFT, true));
            toCell(sheet, row, col + 1, iterator.next(), styleRightCol);
        }
    }

    /**
     * отчет excel - информация о вагонах
     * @param sheet -
     * @param list -
     */
    public static void wags_info(XSSFSheet sheet, java.util.List<String> list) {
        if (sheet == null) return;
        XSSFFont font_smale_caption = makeFont(sheet.getWorkbook(), 8, false, true, 10);
        CellStyle styleSmaleCaption = getStyle(sheet, CellStyle.ALIGN_CENTER, false);
        CellStyle styleCenter = getStyle(sheet, CellStyle.ALIGN_CENTER, true);
        CellStyle styleLeft = getStyle(sheet, CellStyle.ALIGN_LEFT, true);
        styleSmaleCaption.setFont(font_smale_caption);
        Iterator<String> iterator = list.iterator();
        iterator.next();
        int row = 0;
        int col = 0;
        toCell(sheet, row, col, iterator.next(), getStyle(sheet, CellStyle.ALIGN_CENTER, false));
        toCell(sheet, ++row, col, iterator.next(), styleSmaleCaption);
        toCell(sheet, ++row, col, iterator.next(), styleSmaleCaption);

        while (iterator.hasNext()) {
            toCell(sheet, ++row, col, iterator.next(), styleCenter);
            toCell(sheet, row, col + 1, iterator.next(), styleLeft);
            toCell(sheet, row, col + 2, iterator.next(), styleLeft);
        }
    }

    /**
     * отчет excel - информация о станциях
     * @param sheet -
     * @param list -
     */
    public static void schedule_info(XSSFSheet sheet, java.util.List<String> list) {
        if (sheet == null) return;
        CellStyle styleLeft = getStyle(sheet, CellStyle.ALIGN_LEFT, true);
        Iterator<String> iterator = list.iterator();
        iterator.next();
        int row = 0;
        int col = 0;
        toCell(sheet, row, col, iterator.next(), getStyle(sheet, CellStyle.ALIGN_CENTER, false));
        row += 2;
        // пропускаем данные заголовка (они в шаблоне)
        for (int i = 0; i < 10; i++) {
            iterator.next();
        }
        while (iterator.hasNext()) {
            toCell(sheet, ++row, col, iterator.next(), styleLeft);
            toCell(sheet, row, col + 1, iterator.next(), styleLeft);
            toCell(sheet, row, col + 2, iterator.next(), styleLeft);
            toCell(sheet, row, col + 3, iterator.next(), styleLeft);
            toCell(sheet, row, col + 4, iterator.next(), styleLeft);
            toCell(sheet, row, col + 5, iterator.next(), styleLeft);
            toCell(sheet, row, col + 6, iterator.next(), styleLeft);
            toCell(sheet, row, col + 7, iterator.next(), styleLeft);
        }
    }

    /**
     * отчет excel - посекундная выгрузка о расходе энергии
     * @param sheet - лист excel
     * @param arrBlock32 -
     * @param train -
     */
    public static void energy_consump(XSSFSheet sheet, ArrBlock32 arrBlock32, Train train) {

    }

    // процедуры для отчетов ----------------------------------------------------------------------

    private static Cell makeCell(XSSFSheet sheet, int nRow, int nCol) {
        Row row = sheet.getRow(nRow);
        if (row == null)
            row = sheet.createRow(nRow);
        Cell cell = row.getCell(nCol);
        if (cell == null)
            cell = row.createCell(nCol);
        return cell;
    }

    public static void toCell(XSSFSheet sheet, int nRow, int nCol, String val, CellStyle style) {
        Cell cell = makeCell(sheet, nRow, nCol);
        if (style != null) cell.setCellStyle(style);

        if (val.matches("\\d+"))                                    // if integer
            cell.setCellValue(Integer.parseInt(val));
        else if (val.matches("^[-]?+[0-9]+([,.][0-9]?)?$")) {            // if double
            val = val.replace(",", ".");
            cell.setCellValue(Double.parseDouble(val));
        }
        else
            cell.setCellValue(val);
    }

    public static Cell toCell(XSSFSheet sheet, int nRow, int nCol, int val, CellStyle style) {
        Cell cell = makeCell(sheet, nRow, nCol);
        if (style != null) cell.setCellStyle(style);
        cell.setCellValue(val);
        return cell;
    }

    public static void toCell(XSSFSheet sheet, int nRow, int nCol, double val, CellStyle style) {
        Cell cell = makeCell(sheet, nRow, nCol);
        if (style != null) cell.setCellStyle(style);
        if (!Double.isNaN(val) && val != 0)
            cell.setCellValue(val);
        else
            cell.setCellValue("-");
    }

    public static void toCell(XSSFSheet sheet, int nRow, int nCol, Date date, CellStyle style) {
        Cell cell = makeCell(sheet, nRow, nCol);
        cell.setCellStyle(style);
        cell.setCellValue(date);
    }

    /**
     * записать в исходный файл книгу отчета
     * @param outFileName - имя исходного файла
     */
    private void writeToBook(String outFileName) throws IOException {
        FileOutputStream outputStream = null;
        try {
            try {
                outputStream = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            book.write(outputStream);
            if (ShowDialog_Yes_No("Файл отчета сохранен под именем:\n" + outFileName + "\nОткрыть?", "Вопрос?"))
                Desktop.getDesktop().open(new File(outFileName));
        }
        finally {
            if (outputStream != null)
                outputStream.close();
        }
    }
}
