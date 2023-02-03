package avpt.gr.common;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HelperExl_POI {

        /**
         * установка рамки
         * @param style -
         */
        private static void setStyleBorder(CellStyle style) {
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        }


        public static CellStyle getStyle(XSSFSheet sheet, short align, boolean is_border) {
            XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
            style.setAlignment(align);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            if (is_border)
                setStyleBorder(style);
            return style;
        }

        /**
         * @param sheet -
         * @return CellStyle пустой с рамкой
         */
        public static CellStyle getStyleCenter(XSSFSheet sheet) {
            CellStyle style = sheet.getWorkbook().createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER);
            setStyleBorder(style);
            return style;
        }

        /**
         * @param sheet XSSFSheet
         * @return CellStyle - выравнивание цифрового значения по левому краю
         */
        public static CellStyle getStyleLeft(XSSFSheet sheet) {
            CellStyle style = sheet.getWorkbook().createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);

            setStyleBorder(style);
            return style;
        }

        /**
         * @param sheet -
         * @param index_color - IndexedColors.RED.getIndex()
         * @param isBold -
         * @param isItalic -
         * @param height -
         * @return - установка цвета, утолщения, наклона, размера
         */
        public static CellStyle getStyleFont(XSSFSheet sheet, short index_color, boolean isBold, boolean isItalic, short height) {
            XSSFWorkbook workbook = sheet.getWorkbook();
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setColor(index_color);
            font.setBold(isBold);
            font.setItalic(isItalic);
            font.setFontHeight(height);
            style.setFont(font);
            setStyleBorder(style);
            return style;
        }

    /**
     * @param book - XSSFWorkbook
     * @return формат даты для вставки в cell Excel
     */
    public static CellStyle getStyleDateTime(XSSFWorkbook book, short align, boolean is_border) {
        CreationHelper createHelper = book.getCreationHelper();
        CellStyle style = book.createCellStyle();
        style.setAlignment(align);
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm"));
        if (is_border)
            setStyleBorder(style);
        return style;
    }

    /**
     * @param book - XSSFWorkbook
     * @return формат даты для вставки в cell Excel
     */
    public static CellStyle getStyleDate(XSSFWorkbook book, short align, boolean is_border) {
        CreationHelper createHelper = book.getCreationHelper();
        CellStyle style = book.createCellStyle();
        style.setAlignment(align);
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        if (is_border)
            setStyleBorder(style);
        return style;
    }

    public static CellStyle getStyTime(XSSFWorkbook book, boolean is_border) {
        CreationHelper createHelper = book.getCreationHelper();
        CellStyle style = book.createCellStyle();
        style.setDataFormat(createHelper.createDataFormat().getFormat("HH:mm"));
        if (is_border)
            setStyleBorder(style);
        return style;
    }

    /**
     * @param workbook -
     * @param index_color -
     * @param isBold -
     * @param isItalic -
     * @param height -
     * @return - font
     */
        public static XSSFFont makeFont(XSSFWorkbook workbook, int index_color, boolean isBold, boolean isItalic, int height) {
            XSSFFont font = workbook.createFont();
            font.setColor((short) index_color);
            font.setBold(isBold);
            font.setItalic(isItalic);
            font.setFontHeight(height);
            return font;
        }
}
