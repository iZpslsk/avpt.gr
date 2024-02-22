package avpt.gr.chart_dataset;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.IntervalXYDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import static avpt.gr.chart_dataset.ListSignals.*;
import static avpt.gr.chart_dataset.ListSignals.KEY_LAST;

abstract public class SeriesSignals {

    private final TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();
    private static HashSet<Integer> setSeriesSelected = new HashSet<Integer>();
    private int amountMain = 0;    // количество основных дискретных сигналов

    protected void addTaskSeries(int key, ListSignals.ListSignal listSignal) {
        TaskSeries task = new TaskSeries("");
        String description = ListSignals.getDescriptionSygnal(key);
        if (description.isEmpty()) return;
        task.setDescription(description);
        task.setKey(key);
        ArrayList<ListSignals.ItemSignal> list = listSignal.getList();

        for (int i = 0; i < list.size(); i++) {
            ListSignals.ItemSignal item = list.get(i);
            task.add(new Task(Integer.toString(i), new SimpleTimePeriod(item.begin, item.end)));
        }
        taskSeriesCollection.add(task);
        if (!task.isEmpty()) {
            setSeriesSelected.add(key);
            incCnt(key);
        }
    }

    private void incCnt(int key) {
        if (key >= KEY_FIRST && key <= KEY_LAST) {
            amountMain++;
        }
    }

    private void decCnt(int key) {
        if (key >= KEY_FIRST && key <= KEY_LAST) {
            amountMain--;
        }
        if (amountMain < 0) amountMain = 0;
    }

    public boolean isSeriesSelected(int key) {
        return setSeriesSelected.contains(key);
    }

    public void doSeriesSelected(int key, boolean doSelected) {
        if (doSelected) {
            setSeriesSelected.add(key);
            incCnt(key);
        }
        else {
            setSeriesSelected.remove(key);
            decCnt(key);
        }
    }

    /**
     * @return всего основных дискретных сигналов
     */
    public int getAmountMain() {
        return amountMain;
    }

    public IntervalXYDataset getTaskSeriesCollection() {

        final TaskSeriesCollection taskSeriesCollectionOut = new TaskSeriesCollection();

        for (int i = 0; i < taskSeriesCollection.getSeriesCount(); i++) {
            TaskSeries task = taskSeriesCollection.getSeries(i);
            int key = (Integer) task.getKey();
            if (setSeriesSelected.contains(key) || key == KEY_MAIN_CHANNEL || key == KEY_ADDITIONAL_CHANNEL) {
                taskSeriesCollectionOut.add(task);
            }
        }

        XYTaskDataset dataset = new XYTaskDataset(taskSeriesCollectionOut);
        dataset.setTransposed(true);
        dataset.setSeriesWidth(0.8);
        return dataset;
    }

    abstract public Color getColorSeries(int key);
}
