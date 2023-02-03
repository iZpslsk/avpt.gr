package avpt.gr.chart_dataset;

import avpt.gr.blocks32.overall.Block32_21_2;

import java.util.ArrayList;

public class ItemWag {

    private final int type;     // тип вагона
    private final int num;      // номер вагона
    private final int weight;   // вес вагона
    private static final ArrayList<ItemWag> list_tmp = new ArrayList<ItemWag>(); // список вагонов
    private static final ArrayList<ItemWag> wags = new ArrayList<ItemWag>();

    // приватный конструктор
    private ItemWag(int type, int num, int weight) {
        this.type = type;
        this.num = num;
        this.weight = weight;
    }

    public static void add(Block32_21_2 block32_21_2) {
        if (block32_21_2.getNumFrame() == 1) list_tmp.clear(); // если 1-й фрейм - стираем предыдущие фреймы
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag1(), block32_21_2.getNumWag1(), block32_21_2.getWeightWag1()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag2(), block32_21_2.getNumWag2(), block32_21_2.getWeightWag2()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag3(), block32_21_2.getNumWag3(), block32_21_2.getWeightWag3()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag4(), block32_21_2.getNumWag4(), block32_21_2.getWeightWag4()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag5(), block32_21_2.getNumWag5(), block32_21_2.getWeightWag5()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag6(), block32_21_2.getNumWag6(), block32_21_2.getWeightWag6()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag7(), block32_21_2.getNumWag7(), block32_21_2.getWeightWag7()));
        list_tmp.add(new ItemWag(block32_21_2.getTypeWag8(), block32_21_2.getNumWag8(), block32_21_2.getWeightWag8()));

        if (block32_21_2.getNumFrame() == block32_21_2.getCountFrames()) {
            wags.clear();
            for (ItemWag item : list_tmp) {
                if (item.num > 0 && item.num <= block32_21_2.getCountWags())
                    wags.add(item);
            }
        }
    }

    public static ArrayList<ItemWag> getWags() {
        return wags;
    }

    public int getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public int getWeight() {
        return weight;
    }
}
