package avpt.gr.graph;

import avpt.gr.chart_dataset.ChartDataset;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnitSource;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.threeten.bp.LocalTime;
//import java.time.LocalTime;

/**
 * форматирование значений по оси x (время)
 * преобразуем секунды в трабуемый формат
 */
public class NumberTickUnitSrcTime implements TickUnitSource, Serializable {

	private boolean integers;
    private int power;
    private int factor;
    private ChartDataset datasets;

    /**
     * Creates a new instance.
     */
    public NumberTickUnitSrcTime(ChartDataset datasets) {

        integers = false;
        this.power = 0;
        this.factor = 1;
    	this.datasets = datasets;
    }
    
    @Override
    public TickUnit getLargerTickUnit(TickUnit unit) {
        TickUnit t = getCeilingTickUnit(unit);
        if (t.equals(unit)) {
            next();
            t = new NumberTickUnit(getTickSize(), new DecimalFormat(), 
                    getMinorTickCount());
        }
        return t; 
    }

    @Override
    public TickUnit getCeilingTickUnit(TickUnit unit) {
        return getCeilingTickUnit(unit.getSize());
    }

    @Override
    public TickUnit getCeilingTickUnit(double size) {
        if (Double.isInfinite(size)) {
            throw new IllegalArgumentException("Must be finite.");
        }
        this.power = (int) Math.ceil(Math.log10(size));
        if (this.integers) {
            power = Math.max(this.power, 0);
        }
        this.factor = 1;
        boolean done = false;
        // step down in size until the current size is too small or there are 
        // no more units
        while (!done) {
            done = !previous();
            if (getTickSize() < size) {
                next();
                done = true;
            }
        }
        return new NumberTickUnit(getTickSize(), new DecimalFormat(), getMinorTickCount() ) {
        	
			private static final long serialVersionUID = 1L;
			
			// ------------------------------------------------------
			@Override
        	public String valueToString(double value) {
				// установка текущего времени на оси x
				LocalTime time = datasets.getTime((int)value);
				if (time != null && time != LocalTime.ofSecondOfDay(0))
				  return time.toString(); 
				else
				  return "";
        	}
        };
    }
    
    private boolean next() {
        if (factor == 1) {
            factor = 2;
            return true;
        }
        if (factor == 2) {
            factor = 5;
            return true;
        }
        if (factor == 5) {
            if (power == 300) {
                return false;
            }
            power++;
            factor = 1;
            return true;
        }
        throw new IllegalStateException("We should never get here.");
    }

    private boolean previous() {
        if (factor == 1) {
            if (this.integers && power == 0 || power == -300) {
                return false;
            }
            factor = 5;
            power--;
            return true;
        } 
        if (factor == 2) {
            factor = 1;
            return true;
        }
        if (factor == 5) {
            factor = 2;
            return true;
        } 
        throw new IllegalStateException("We should never get here.");
    }

    private double getTickSize() {
        return this.factor * Math.pow(10.0, this.power);
    }
    
    private int getMinorTickCount() {
        if (factor == 1) {
            return 10;
        } else if (factor == 5) {
            return 5;
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberTickUnitSource)) {
            return false;
        }
        NumberTickUnitSrcTime that = (NumberTickUnitSrcTime) obj;
        if (this.integers != that.integers) {
            return false;
        }
        if (this.power != that.power) {
            return false;
        }
        if (this.factor != that.factor) {
            return false;
        }
        return true;
    }
}

