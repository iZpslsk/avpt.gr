package avpt.gr.graph;

import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnitSource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.util.ObjectUtilities;

/**
 * форматирование значений по оси y
 * устанавливаем пространство перед tick label для выравнивания заголовков названий по одной линии
 */
public class NumberTickUnitSrcValue implements TickUnitSource, Serializable {
	
	private boolean integers;
    private int power;
    private int factor;
    private double minVal;
    
    /** The number formatter to use (an override, it can be null). */
    private NumberFormat formatter;

    /**
     * Creates a new instance.
     * @param integers  show integers only.
     */
    NumberTickUnitSrcValue(boolean integers, double minVal, double maxVal) {
        this(integers, null);
        this.minVal = minVal;
    }
    
    /**
     * Creates a new instance.
     * @param integers  show integers only?
     * @param formatter  a formatter for the axis tick labels ({@code null} 
     *         permitted).
     */
    NumberTickUnitSrcValue(boolean integers, NumberFormat formatter) {
        this.integers = integers;
        this.formatter = formatter;
        this.power = 0;
        this.factor = 1;
    }
    
    @Override
    public TickUnit getLargerTickUnit(TickUnit unit) {
        TickUnit t = getCeilingTickUnit(unit);
        if (t.equals(unit)) {
            next();
            t = new NumberTickUnit(getTickSize(), getTickLabelFormat(), 
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
        return new NumberTickUnit(getTickSize(), getTickLabelFormat(), 
                getMinorTickCount() ) {
        	
			private static final long serialVersionUID = 1L;
			//  главное ------------------------------------------------------
			@Override
        	public String valueToString(double value) {
				if ((value >= 0 && value <= 0.99) || value == minVal)
					return "     ";
				else
					return String.format("%5.0f", value);
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
    
    private DecimalFormat dfNeg4 = new DecimalFormat("0.0000");
    private DecimalFormat dfNeg3 = new DecimalFormat("0.000");
    private DecimalFormat dfNeg2 = new DecimalFormat("0.00");
    private DecimalFormat dfNeg1 = new DecimalFormat("0.0");
    private DecimalFormat df0 = new DecimalFormat("#,##0");
    private DecimalFormat df = new DecimalFormat("#.######E0");
    
    private NumberFormat getTickLabelFormat() {
        if (this.formatter != null) {
            return this.formatter;
        }
        if (power == -4) {
            return dfNeg4;
        }
        if (power == -3) {
            return dfNeg3;
        }
        if (power == -2) {
            return dfNeg2;
        }
        if (power == -1) {
            return dfNeg1;
        }
        if (power >= 0 && power <= 6) {
            return df0;
        }
        return df;
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
        NumberTickUnitSrcValue that = (NumberTickUnitSrcValue) obj;
        if (this.integers != that.integers) {
            return false;
        }
        if (!ObjectUtilities.equal(this.formatter, that.formatter)) {
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

