package avpt.gr.graph;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class DualColor implements Paint {
    private final Color colorOne;
    private final Color colorTwo;

    public DualColor(Color colorOne, Color colorTwo) {
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
    }

    public int getRGBOne() {
        return colorOne.getRGB();
    }
    public int getRGBTwo() {
        return colorTwo.getRGB();
    }
    transient private PaintContext theContext;

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        PaintContext pc = theContext;
        if (pc == null) {
            pc = new DualColorContext(getRGBOne(), getRGBTwo(), cm);
            theContext = pc;
        }
        return pc;
    }

    @Override
    public int getTransparency() {
        int a1 = colorOne.getAlpha();
        int a2 = colorTwo.getAlpha();
        return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
}
