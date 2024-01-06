package avpt.gr.common;

import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import avpt.gr.start.StartFrame;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.CharacterIterator;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;

//import static com.sun.xml.internal.bind.WhiteSpaceProcessor.trim;
import static avpt.gr.start.StartFrame.sizeFont;

public class UtilsArmG {
    // максимальный размер файла поездки
    public static final int MAX_SIZE_FILE = 160000000;
    // кодировки
    public static final Charset cp866 = Charset.forName("CP866");
    public static final Charset windows1251 = Charset.forName("Windows-1251");
    public static final Charset utf8 = Charset.forName("UTF-8");
    // шрифты
    public static Font commonFont = new Font(Font.SANS_SERIF, Font.BOLD, sizeFont); // дата, время, широта, долгота
    public static Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, sizeFont); // шрифт для заголовка
    public static Font descriptFont = new Font(Font.MONOSPACED, Font.BOLD, sizeFont); // шрифт для значений - crosshair
    public static Font signalFont = new Font(Font.MONOSPACED, Font.BOLD, sizeFont); // // шрифт для значений дискретных сигналов - crosshair
    public static Font trainFont = new Font(Font.MONOSPACED, Font.BOLD, sizeFont - 1); //
    public static Font statusFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
    public static Font hexTableFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
    public static Font labelYAxisFont = new Font(Font.SERIF, Font.BOLD, sizeFont);// шрифт для шкалы  - Y
    public static Font tickFont = new Font(Font.MONOSPACED, Font.ITALIC, sizeFont - 1);// шрифт для значений шкал X и Y
    public static Font trMarkerFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
    public static Font profNameFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
    public static Font stationNameFont = new Font(Font.SANS_SERIF, Font.BOLD, sizeFont - 1);
    public static Font menuFont = new Font(Font.SANS_SERIF, Font.PLAIN, sizeFont);
    public static Font captionTrainFont = new Font(Font.SANS_SERIF, Font.BOLD, sizeFont + 10);
    public static Font btnTrainsFont = new Font(Font.MONOSPACED, Font.PLAIN, sizeFont);
    // формат даты
    public static final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter formatDateTimeForFileName = DateTimeFormatter.ofPattern("yyyy.MM.dd_HHmmss");
    public static final DateTimeFormatter formatDateTimeSQLite = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter formatDateSQLite = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // имя SQLite базы данных
    public static final String SQULite_BASE_NAME = "data";

    /**
     * передача double по ссылке
     */
    public static class MutableDouble {
        private double value;

        public MutableDouble(double value) {
            this.value = value;
        }

        public MutableDouble() {
            this.value = 0;
        }

        public double get() {
            return value;
        }

        public void set(double value) {
            this.value = value;
        }
    }

    /**
     * @param b - байт
     * @return - беззнаковый байт
     */
    public static int toUnsignedInt(byte b) {
        return ((int) b) & 0xff;
    }

    /**
     * если секунды превышают суточный предел то осуществляется переход на следующие сутки
     * @param date - дата
     * @param seconds - секунд сначала суток
     * @return LocalDateTime дату и время
     */
    public static LocalDateTime getDateTime(LocalDate date, int seconds) {
        final int MAX_SEC = 86399;
        int days = 0;
        if (seconds > MAX_SEC) {
            days = seconds / MAX_SEC;
            seconds = seconds % MAX_SEC;
        }
        date = date.plusDays(days);
        LocalTime time = LocalTime.ofSecondOfDay(seconds);
        return LocalDateTime.of(date, time);
    }

    public static LocalTime getTime(int seconds) {
        final int MAX_SEC = 86399;
        if (seconds > MAX_SEC) {
            seconds = seconds % MAX_SEC;
        }
        return LocalTime.ofSecondOfDay(seconds);
    }

    /**
     * @param seconds -
     * @return продолжительность в виде строки чч:мм:сс
     */
    public static String getDurationTime(long seconds) {
        if (seconds < 0) return "??:??:??";
//        long seconds = Duration.between(dateTimeStart, dateTimeEnd).getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     *
     * @param hour - часов
     * @param min - минут
     * @param sec - секунд
     * @return - получение времени с проверкой корректности вх параметров
     */
    public static LocalTime getTimeCheck(int hour, int min, int sec) {
        if (hour >= 0 && hour < 24 && min >= 0 && min < 60 && sec >= 0 && sec < 60)
            return LocalTime.of(hour, min, sec);
        else
            return null;

//        if (sec > 59 || min > 59 || hour > 23)
//            return null;
//        else
//            return LocalTime.of(hour, min, sec);
    }

    /**
     * @param i - List Iterator
     * @param index - индекс
     * @return получение значения итератора по индексу;
     */
    private static <T> T get(ListIterator<? extends T> i, int index) {
        T obj = null;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

    /**
     * @param l -
     * @param key -
     * @param low -
     * @param high -
     * @param c -
     * @return бинарный поиск в определенном интервале;
     */
    public static <T> int iteratorBinarySearch(
            List<? extends T> l, T key, int low, int high, Comparator<? super T> c) {
//        int low = 0;
//        int high = l.size()-1;
        ListIterator<? extends T> i = l.listIterator();

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = get(i, mid);
            int cmp = c.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    /**
     * "/avp_t/arm_g/node"
     * @param node - имя конечного узла
     * @return Preferences текущего user
     */
    public static Preferences getNode(String node) {
        String pathName = "/avpt/arm/arm_g/" + node;
        Preferences root = Preferences.userRoot();
        return root.node(pathName);
    }

    /**
     * @param dir - создаваемая директория
     * @throws FileNotFoundException -
     */
    private static void makePath(File dir) throws FileNotFoundException {
        if (!dir.exists() ) {
            if (!dir.mkdir())
                throw new FileNotFoundException("не удалось создать " + dir.toString());
        }
    }

    /**
     * @param slaveSubPath - завершающая ветка пути
     * @param isTemp - temporary - временный каталог
     * @return - полное имя пути в каталоге пользователя /arm_g/
     * @throws FileNotFoundException
     */
    public static File makeUserPath(String slaveSubPath, boolean isTemp) throws FileNotFoundException {
      //  File outDir = new File(System.getProperty("user.home") + "/arm_g/");
        //File outDir = new File(System.getProperty("java.io.tmpdir") + "/arm_g/");
        String DIR = "/arm_g/";
        File outDir = isTemp ?
                new File(System.getProperty("java.io.tmpdir") + DIR) :
                new File(System.getProperty("user.home") + DIR);
        makePath(outDir);
        outDir = new File(outDir, slaveSubPath);
        makePath(outDir);
        return outDir;
    }

    /**
     * распологаем на экране два окна окна
     * @param win_top - верхнее окно
     * @param win_bottom - нижнее окно
     */
    public static void setWinBound(Window win_top, Window win_bottom) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        double h_perc = screenSize.getHeight() * 3 / 100;
        double w_perc = screenSize.getWidth() * 6 / 100;
        win_top.setBounds(0, 0, (int)screenSize.getWidth() - (int)w_perc, (int)screenSize.getHeight() / 2);
        int top = (int)screenSize.getHeight() / 2;
        win_bottom.setBounds(0, top - 10, (int)screenSize.getWidth() - (int)w_perc, (int)screenSize.getHeight() / 2 - (int)h_perc);
    }

    /**
     * устанавливаем размеры и положение главного окна
     * @param win - окно
     * @param pref - узел
     */
    public static void setWinBound(Window win, Preferences pref) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        double h_perc = screenSize.getHeight() * 3 / 100;
        double w_perc = screenSize.getWidth() * 6 / 100;
        int left = pref.getInt("left", 0);
        int top = pref.getInt("top", 0);
        int width = pref.getInt("width", (int)screenSize.getWidth() - (int)w_perc);
        int height = pref.getInt("height", (int)screenSize.getHeight() - (int)h_perc);
        win.setBounds(left, top, width, height);
    }

    /**
     * сохраняем оптимальные размеры и положение главного окна
     * @param pref - узел
     */
    public static void saveWinBoundOptimal(Preferences pref) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        double h_perc = screenSize.getHeight() * 3 / 100;
        double w_perc = screenSize.getWidth() * 6 / 100;
        pref.putInt("left", 0);
        pref.putInt("top", 0);
        pref.putInt("width", (int)screenSize.getWidth() - (int)w_perc);
        pref.putInt("height", (int)screenSize.getHeight() - (int)h_perc);
    }

    /**
     * установить максимальный размер главного окна
     * @param win - окно
     */
    public static void setWinBoundMax(Window win) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        win.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
    }

    /**
     * устанавливаем размеры и положение окна отчетов
     * @param win - окно
     * @param pref - узел
     */
    public static void setWinBoundRep(Window win, Preferences pref) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int def_width = (int)screenSize.getWidth() / 2;
        int def_height = (int)screenSize.getHeight() / 2;
        int left = pref.getInt("left_rep", def_width - def_width / 2);
        int top = pref.getInt("top_rep", def_height - def_height / 2);
        int width = pref.getInt("width_rep", def_width);
        int height = pref.getInt("height_rep", def_height);
        win.setBounds(left, top, width, height);
    }

    /**
     * сохраняем размеры и положение главного окна
     * @param win - окно
     * @param pref - узел
     */
    public static void saveWinBound(Window win, Preferences pref) {
        pref.putInt("left", win.getX());
        pref.putInt("top", win.getY());
        pref.putInt("width", win.getWidth());
        pref.putInt("height", win.getHeight());
    }

    /**
     * сохраняем размеры и положение окна отчетов
     * @param win - окно
     * @param pref - узел
     */
    public static void saveWinBoundRep(Window win, Preferences pref) {
        pref.putInt("left_rep", win.getX());
        pref.putInt("top_rep", win.getY());
        pref.putInt("width_rep", win.getWidth());
        pref.putInt("height_rep", win.getHeight());
    }

    /**
     * @param fullName - полное имя файла
     * @return - короткое имя файла
     */
    public static String getShortNameFile(String fullName) {
        File file = new File(fullName);
        return file.getName();
    }

    /**
     * @param bcd - массив
     * @return - decimal
     */
    public static long BCDToDecimal(byte[] bcd) {
        return Long.parseLong(BCDtoString(bcd));
    }

    private static String BCDtoString(byte bcd) {
        StringBuilder sb = new StringBuilder();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }

    private static String BCDtoString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bcd.length; i++) {
            sb.append(BCDtoString(bcd[i]));
        }
        return sb.toString();
    }

    /**
     * @param hex строка в формате 0x00
     * @return -
     */
    public static int[] getIntFromHex(String hex) {
        int id = 0;
        int sub_id = 0;
        String[] splitter = hex.split("[x, _]+");
        if (splitter.length > 1)
            id = Integer.parseInt(splitter[1], 16);
        if (splitter.length > 2)
            sub_id = Integer.parseInt(splitter[2], 16);
        int [] result = {id, sub_id};
        return result;
    }

    /**
     * @param ds - XYDataset
     * @param x - координата
     * @param i - индекс XYPlot (subplot)
     * @return - value
     */
    public static double getVal(XYDataset ds, double x, int i) {
        // return DatasetUtilities.findYValue(ds, i, x); // значение
        int[] indices = DatasetUtilities.findItemIndicesForX(ds, i, x);
        if (indices[0] == -1)
            return Double.NaN;
        else
            return ds.getYValue(i, indices[0]);
    }

    /**
     * @param val - значение широты или долготы из файла поездки
     * @return - шитора или догота
     */
    public static double getGps(long val) {
        return val * 1.8 / (3.14 * 1000000);
    }

    public static double frac(double number) {
        return number - Math.floor(number);
    }

    public static class GPS {
        int gr;     // градус
        int min;    // минута
        double sec; // секунда

        public GPS(double val) {
            gr = (int)val;
            double valMin = frac(val) * 60;
            min = (int)valMin;
            sec = frac(valMin) * 60;
        }
    }

    /**
     * строка дополняется слева пробелами
     * @param string строка
     * @param length длина
     * @return - строка фиксированной длины
     */
    public static String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    /**
     * Открываем диалоговое окно выбора [Да], [Нет]
     * выбор кнопок с помощью стрелок
     * @param msg - текст сообщения
     * @param caption - текст заголовка окна
     * @return - true - [Да]
     */
    public static boolean ShowDialog_Yes_No(String msg, String caption) {
        Object[] options = {"Да", "Нет"};
        JOptionPane pane = new JOptionPane(msg, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null,
                options, options[1]);
        JDialog dialog = pane.createDialog(caption);
        dialog.setIconImage(new ImageIcon(StartFrame.class.getResource("/avpt/gr/images/armlogo.png")).getImage());

        Set<AWTKeyStroke> focusTraversalKeysRight = new HashSet<AWTKeyStroke>(dialog.getFocusTraversalKeys(0));
        focusTraversalKeysRight.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED));
        Set<AWTKeyStroke> focusTraversalKeysLeft = new HashSet<AWTKeyStroke>(dialog.getFocusTraversalKeys(1));
        focusTraversalKeysLeft.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED));
        dialog.setFocusTraversalKeys(0, focusTraversalKeysRight);
        dialog.setFocusTraversalKeys(1, focusTraversalKeysLeft);

        dialog.setVisible(true);
        dialog.dispose();
        Object option = pane.getValue();
        if (option instanceof String)
            return ((String)option).equalsIgnoreCase((String)options[0]);
        else
            return false;
    }

    public static void ShowDialog_Ok(String msg, String caption, String capBtn) {
        Object[] options = {capBtn};
        JOptionPane pane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null,
                options, options[0]);
        JDialog dialog = pane.createDialog(caption);
        dialog.setIconImage(new ImageIcon(StartFrame.class.getResource("/avpt/gr/images/armlogo.png")).getImage());
        dialog.setVisible(true);
        dialog.dispose();
    }

    /**
     * @param fileName - имя файла
     * @return - заменить имя расширения
     */
    public static String replaceExtFile(String fileName, String newExt) {
        return fileName.replaceFirst("[.][^.]+$", newExt);
    }

    /**
     * создаем PrintWriter для nameFile.output рядом с *.jar
     * @param nameFile - файл поездки
     * @return PrintWriter
     */
    public static PrintWriter getWriter(String nameFile) throws URISyntaxException {
        String out_file_name = replaceExtFile(nameFile, ".output");	// out file рядом с файлом поездки
        PrintWriter writer = null;
        try {
            try {
                writer = new PrintWriter(out_file_name, "cp1251");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /**
     * @param codeError - код ошибки
     * @param mess - текстовое сообшение
     * @param fileName - имя файла поездки
     */
    public static void outWriteAndExit(int codeError, String mess, String fileName, boolean isExit)  {
         PrintWriter writer = null;
        try {
            writer = getWriter(fileName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            writer.printf("%d\n%s", codeError, mess);
            writer.close();
        }
//        if (codeError != 0)
        if (isExit)
            System.exit(codeError);
    }

    /**
     * @param localDateTime -
     * @return - строка timestamp
     */
    public static String getTimestampStr(LocalDateTime localDateTime) {
        //final long duration = 3600 * 3 * 1000; // +3 часа (Москва)
        Timestamp timestamp = DateTimeUtils.toSqlTimestamp(localDateTime);
        timestamp.setTime(timestamp.getTime());
        return timestamp.toString();
    }

    /**
     * @param array -
     * @return - индекс с максимальным значением
     */
    public static int getIndexOfLargest(int[] array) {
        if ( array == null || array.length == 0 ) return -1;
        int largest = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] > array[largest] ) largest = i;
        }
        return largest;
    }

    /**
     * @param n - количество байт (разрядность числа)
     * @param val - значение
     * @return - массив байт для числа val
     * @throws IOException -
     */
    public static byte[] getBytes(int n, long val) throws IOException {
        switch (n) {
            case 1: return ByteBuffer.allocate(n).put((byte)val).array();
            case 2: return ByteBuffer.allocate(n).putShort((short)val).array();
            case 4: return ByteBuffer.allocate(n).putInt((int)val).array();
            case 8: return ByteBuffer.allocate(n).putLong(val).array();
            default: throw new IOException("n должно быть равно 1, 2, 4 или 8");
        }
    }

    /**
     * отрезаем символы слева и справа
     * @param str - исходная строка
     * @param left - кол символов справа
     * @param right - кол символов слева
     * @return исходная строка
     */
    public static String trimFirstLast(String str, int left, int right) {
        if (str != null && str.length() > (left + right))
            return str.substring(left, str.length() - right);
        else
            return "";
    }

    /**
     * @param str - исходная строка
     * @return HashMap<String, Boolean>
     */
    public static HashMap<String, Boolean> getMapFromStr(String str) {
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        String[] pairs = str.split(",");
        for (int i=0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1) {
                hashMap.put(keyValue[0].trim(), Boolean.valueOf(keyValue[1]));
                //hashMap.put(trim(keyValue[0]).toString(), Boolean.valueOf(keyValue[1]));
            }
        }
        return  hashMap;
    }

    /**
     * @param fileName - имя файла поездки
     * @throws IOException -
     */
    public static void checkSizeFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists() && file.length() > MAX_SIZE_FILE)
            throw new IOException(
                    String.format("'%s'\n\nФайл поездки превышает допустимый размер!", fileName));
    }

    public static class PairInt {

        private int first;
        private int second;

        public void incFirst() {
            first++;
        }

        public void incSecond() {
            second++;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }

    /**
     * реализация класса pair
     * @param <U> - first
     * @param <V> - second
     */
    public static class Pair<U, V>
    {
        public final U first;       // первое поле пары
        public final V second;      // второе поле пары

        // Создает новую пару с указанными значениями
        public Pair(U first, V second)
        {
            this.first = first;
            this.second = second;
        }

        @Override
        // Проверяет, является ли указанный объект "равным" текущему объекту или нет
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pair<?, ?> pair = (Pair<?, ?>) o;

            // вызвать метод equals() базовых объектов
            if (!first.equals(pair.first)) {
                return false;
            }
            return second.equals(pair.second);
        }

        @Override
        // Вычисляет хеш-код для объекта для поддержки хеш-таблиц
        public int hashCode()
        {
            // используем хеш-коды базовых объектов
            return 31 * first.hashCode() + second.hashCode();
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

//        // Фабричный метод для создания неизменяемого экземпляра типизированной пары
//        public static <U, V> Pair <U, V> of(U a, V b)
//        {
//            // вызывает приватный конструктор
//            return new Pair<U, V>(a, b);
//        }
    }

    /**
     * @param text -
     * @return - первая буква заглавная остальные прописные
     */
    public static String getFirstLaterUpper(String text) {
        if (!text.isEmpty())
            return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        else return "";
    }

    /**
     * представление количества байтов в удобочитаемом виде
     * @param bytes - всего байт
     * @return строка типа "10 МБ"
     */
    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " Б";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("КМГТПЕ");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cБ", value / 1024.0, ci.current());
    }

    /**
     *
     * @param value - значение
     * @param places - кол знакрв после запятой
     * @return округленное значение
     */
    public static double round(double value, int places) {
        if (Double.isNaN(value)) return Double.NaN;
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * @param year -
     * @param month -
     * @param day -
     * @return - true дата неверна
     */
    public static boolean isDateWrong(int year, int month, int day) {
        String s = String.format("%d-%02d-%02d", year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) == null;
    }

    /**
     * @param stringToParse - строка
     * @param defaultValue- значение по умолчанию
     * @return строка в integer, если строка некорректна возвращаем значение по умолчанию
     */
    public static int parseToInt(String stringToParse, int defaultValue) {
        try {
            return Integer.parseInt(stringToParse);
        } catch(NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     *
     * @param arr - исходный массив
     * @param n - значение увеличения массива
     * @return увеличенный массив на n
     */
    public static int[] arrIncrease(int[] arr, int n) {
        int[] result = new int[arr.length + n];
        System.arraycopy(arr, 0, result, 0, arr.length);
        return result;
    }

    /**
     * свойства колонки
     * @param column - колонка таблицы
     * @param width	- ширина колонки
     * @param header - заголовок
     */
    public static void setPropColumn(TableColumn column, int width, String header) {
        column.setMinWidth(width / 2);
        column .setMaxWidth(width * 2);
        column.setPreferredWidth(width);
        column.setHeaderValue(header);
    }

}
