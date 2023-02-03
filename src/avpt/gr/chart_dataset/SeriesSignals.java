package avpt.gr.chart_dataset;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.IntervalXYDataset;

import java.awt.*;
import java.util.ArrayList;

abstract public class SeriesSignals {

    private final TaskSeriesCollection taskSeriesCollection = new TaskSeriesCollection();

    protected void addTaskSeries(int key, ListSignals.ListSignal listSignal) {
        TaskSeries task = new TaskSeries("");
        String description = ListSignals.getDescriptionSygnal(key);
        task.setDescription(description);
        task.setKey(key);
        ArrayList<ListSignals.ItemSignal> list = listSignal.getList();

        for (int i = 0; i < list.size(); i++) {
            ListSignals.ItemSignal item = list.get(i);
            task.add(new Task(Integer.toString(i), new SimpleTimePeriod(item.begin, item.end)));
        }
        if (!task.isEmpty()) taskSeriesCollection.add(task);
    }

    public IntervalXYDataset getTaskSeriesCollection() {
        XYTaskDataset dataset = new XYTaskDataset(taskSeriesCollection);
        dataset.setTransposed(true);
        dataset.setSeriesWidth(0.8);
        return dataset;
    }
    abstract public Color getColorSeries(int key);
}
