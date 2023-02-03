package avpt.gr.chart_dataset;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.IntervalXYDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class SeriesTasks {

    /**
     * массив массивов задач
     */
    private final ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks;

    /**
     * сопоставление цветов и значений
     * в getItemPaintRandarer
     */
    private final Map<Integer, Paint> mapPaints = new HashMap<Integer, Paint>();
    private final ArrayList<Map<Integer, Paint>> listPaints = new ArrayList<Map<Integer, Paint>>();
    private final boolean isColorByIndex;

    /**
     * графическое представление
     * коллекции всех задач
     */
    private final TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();

    public SeriesTasks(ArrayList<ArrayList<ListTasks.ItemTask>> arrTasks, boolean isColorByIndex) {
        this.arrTasks = arrTasks;
        this.isColorByIndex = isColorByIndex;
        for (int i = 0; i < arrTasks.size(); i++) {
            listPaints.add(new HashMap<Integer, Paint>());
        }
      //  arrTasks.add(new ArrayList<ListTasks.ItemTask>());
    }

    Map<Integer, Paint> getMapPaints() {
      //  return mapPaints;
        return listPaints.get(0);
    }

    Map<Integer, Paint> getMapPaints(int index) {
        return listPaints.get(index);
    }

    /**
     * размер массива одной линии
     * @param iSer - индекс линии
     * @return int
     */
    public int size(int iSer) {
        return (arrTasks.get(iSer)).size();
    }

    /**
     * Возвращает Item из массива задач
     * @param iSer - индекс линии
     * @param index - индекс задачи
     * @return Item
     */
    public ListTasks.ItemTask get(int iSer, int index) {
        if (arrTasks == null || iSer > arrTasks.size() - 1) return null;
        if (arrTasks.get(iSer) == null || index > (arrTasks.get(iSer)).size() - 1) return null;
        return arrTasks.get(iSer).get(index);
    }

    /**
     * графическое представление
     * добавляем TaskSeries в TaskSeriesCollection
     * @param iSer - индекс серии
     */
    private void addTaskSeries(int iSer) {
        TaskSeries task = new TaskSeries("");
        task.setKey(iSer);
        task.setDescription("tasks");
        for (int i = 0; i < size(iSer); i++) {
            ListTasks.ItemTask item = get(iSer, i);
            if (item != null)
           //     if (item.end > item.begin)
                    task.add(new Task(Integer.toString(item.value), new SimpleTimePeriod(item.begin, item.end)) );
        }
        if (!task.isEmpty()) {
        //    if (task.getItemCount() == 1 &&  Integer.parseInt(task.get(0).getDescription()) == 0) return;
            taskSeriesCollection.add(task);
        }
    }

    /**
     * возвращает value по x (на текущую секунду)
     * @param iSer - индекс линии
     * @param x - координата x (текущая секунда)
     * `@return
     */
    public  int getItemVal(int iSer, int x) {
        int result = -1;
        for (int i = 0; i < size(iSer); i++) {
            ListTasks.ItemTask item = get(iSer, i);
            if (item != null && x >= item.begin && x < item.end) {
                result = item.value;
                if (x == item.begin) break;
            }
        }
        return result;
    }

    /**
     * возвращаем цвет линии по умолчанию в соответствии с индексом
     * @param index - индекс линии
     * @return цвет линии по умолчанию
     */
    private Color getColorDef(int index) {
        if (isColorByIndex)
            return TaskPushKey.getColorPushKey(index);
        else
            return Color.GRAY;
    }

    /**
     * возвращаем цвет квадратика при перерисовке курсора (drawValuesCursor)
     * возвращает paint по x (на текущую секунду)
     * @param iSer - индекс линии
     * @param x - координата x (текущая секунда)
     * @return - Paint
     */
    public Paint getItemPaint(int iSer, int x) {
        Paint paint = null;
        for (int i = 0; i < size(iSer); i++) {
            ListTasks.ItemTask item = get(iSer, i);
            if (item != null && x >= item.begin && x < item.end) {
                //paint = mapPaints.get(item.value);
                paint = listPaints.get(iSer).get(item.value);
                if (x == item.begin) break;
            }
        }
        return (paint != null) ?  paint: getColorDef(iSer);
    }

    /**
     * создает набор данных для графического представления задач
     */
    public IntervalXYDataset createDataset() {
        for (int i = 0; i < arrTasks.size(); ++i) {
            if (arrTasks.get(i).size() > 0) {
                addTaskSeries(i);
                setPaint(i);
            }
        }
        XYTaskDataset dataset = new XYTaskDataset(taskSeriesCollection);
        dataset.setTransposed(true);
        dataset.setSeriesWidth(0.9);
        return dataset;
    }

    /**
     * установка цветов для диаграммы
     * @param index - индекс линии
     */
    abstract void setPaint(int index);

    /**
     * возвращает описание состояния саздт для значения в соответсвии с форматом
     * @param val - значение
     * @param key - ключ (для Series соответствует секции)
     * @return String
     */
    abstract public String getDescript(int val, int key, int loc_type);

    /**
     * переопределяем renderer
     */
    public class TasksRenderer extends XYBarRenderer {

        @Override
        public Paint getItemPaint(int row, int column) {
            Paint paint = super.getItemPaint(row, column);
            int key = (Integer) SeriesTasks.this.taskSeriesCollection.getSeries(row).getKey();
            Color colorDef = getColorDef(key);
            ListTasks.ItemTask item = SeriesTasks.this.get(key, column);
            if (item != null)
                //paint = mapPaints.get(item.value);
                paint = listPaints.get(key).get(item.value);

            if (paint == null) paint = colorDef;

            return paint;
        }
    }

    public XYBarRenderer createRenderer() {
        return new TasksRenderer();
    }

}
