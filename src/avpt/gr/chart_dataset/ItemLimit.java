package avpt.gr.chart_dataset;

import avpt.gr.blocks32.overall.Block32_21_3;

import java.util.ArrayList;

/**
 * позволяет получить величину временного ограничения по текущей координате - getTempLimit
 * содержит статический список временных ограничений, состоящий из актуальных на данный момент фреймов из посылки 0x21_3
 */
public class ItemLimit {
    private final int val;     // значение ограничения
    private final int start;  // координата начала
    private final int end;    // координата конца
    private static final ArrayList<ItemLimit> list = new ArrayList<ItemLimit>(); // список временных ограничений

    // приватный конструктор
    private ItemLimit(int val, int start, int end) {
        this.val = val;
        this.start = start;
        this.end = end;
    }

    /**
     * добавляем данные о текущем временном ограничении из посылки 0x21_3
     * @param block32_21_3 - посылка с временными ограничениями
     */
    public static void add(Block32_21_3 block32_21_3) {
        if (block32_21_3.getNumFrame() == 1) list.clear(); // если 1-й фрейм - стираем предыдущие фреймы
        list.add(new ItemLimit(block32_21_3.getValue1(), block32_21_3.getBegin1(), block32_21_3.getEnd1()));
        list.add(new ItemLimit(block32_21_3.getValue2(), block32_21_3.getBegin2(), block32_21_3.getEnd2()));
        list.add(new ItemLimit(block32_21_3.getValue3(), block32_21_3.getBegin3(), block32_21_3.getEnd3()));
        list.add(new ItemLimit(block32_21_3.getValue4(), block32_21_3.getBegin4(), block32_21_3.getEnd4()));
        list.add(new ItemLimit(block32_21_3.getValue5(), block32_21_3.getBegin5(), block32_21_3.getEnd5()));
    }

    /**
     * определяем ограничеие из актуальных фреймов собранных из посылок в соответствии с текущей координатой
     * @param coordinate - координата
     * @param itemLines - ArrayList<ListLines.ItemLine> необходим для получения предыдущего значения
     * @return - текущее значение временного ограничения
     */
    static double getTempLimit(int coordinate, ArrayList<ListLines.ItemLine> itemLines) {
        double result = Double.NaN;
        for (ItemLimit item : list) {
            if (coordinate >= item.start && coordinate < item.end &&
                    item.val > 1 && (Double.isNaN(result) || item.val > result)) {
                result = item.val;
            }
        }
        double pre_val = Double.NaN;
        if (itemLines.size() > 0)
            pre_val = itemLines.get(itemLines.size() - 1).getValue();
        if (pre_val != result && !Double.isNaN(pre_val) && !Double.isNaN(result))
            result = Double.NaN;
        return result;
    }
}
