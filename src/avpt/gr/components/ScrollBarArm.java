package avpt.gr.components;

import avpt.gr.common.UtilsArmG;
import avpt.gr.graph.ChartArm;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * компонент Scroll bar
 */
public class ScrollBarArm extends JScrollBar {

    private final UtilsArmG.MutableDouble boundUpper;
    private final ChartArm chartArm;
    private final double RIGHT_SPACE = 0.02;

    public ScrollBarArm(final ChartArm chartArm) {
        super(JScrollBar.HORIZONTAL);
        this.chartArm = chartArm;
        boundUpper = chartArm.getBoundUpper();
        boundUpper.set(chartArm.getDomainAxis().getUpperBound());
//        this.boundUpper = chartArm.getDomainAxis().getUpperBound();
        setMaximum(chartArm.getSecondCoordinate() - (int)Math.round(boundUpper.get() - boundUpper.get() * RIGHT_SPACE));

        getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setScroll(ScrollBarArm.this);
            }
        });
    }

    /**
     * скроллинг графики левая граница = 0
     * @param scroll	- ScrollBarArm
     */
    public void setScroll(ScrollBarArm scroll) {
        double x = scroll.getValue();
        int index = chartArm.getChartDataset().getArrTrains().getIndexFromX(chartArm.getChartDataset().getArrBlock32(), x);
        chartArm.setXMarkerTrainLabel(index, x);
        if (chartArm.getDomainAxis() != null) chartArm.getDomainAxis().setRange(0 + x, boundUpper.get() + x);
    }

    /**
     * изменение максимального положения курсора
     */
    public void changeMaximumSize() {
        setMaximum(chartArm.getSecondCoordinate() - (int)Math.round(boundUpper.get() - boundUpper.get() * RIGHT_SPACE));
    }

}
