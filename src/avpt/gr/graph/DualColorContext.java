package avpt.gr.graph;

import sun.awt.image.IntegerComponentRaster;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.util.Arrays;

public class DualColorContext implements PaintContext {

    private final ColorModel xrgbmodel = new DirectColorModel(24, 0xff0000, 0x00ff00, 0x0000ff);

    private final int colorOne;
    private final int colorTwo;

    Raster savedTile;

    protected DualColorContext(int colorOne, int colorTwo, ColorModel cm) {
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
    }

    @Override
    public void dispose() {

    }

    @Override
    public ColorModel getColorModel() {
        return xrgbmodel;
    }

    @Override
    public Raster getRaster(int x, int y, int w, int h) {
        Raster t = savedTile;

        if (t == null || w > t.getWidth() || h > t.getHeight()) {
            Raster one = xrgbmodel.createCompatibleWritableRaster(w, h/2);
            Raster two = xrgbmodel.createCompatibleWritableRaster(w, h);

            IntegerComponentRaster icrOne = (IntegerComponentRaster) one;
            IntegerComponentRaster icrTwo = (IntegerComponentRaster) two;

            int[] arrayOne = icrOne.getDataStorage();
            Arrays.fill(arrayOne, colorOne);

            int[] arrayTwo = icrTwo.getDataStorage();
            Arrays.fill(arrayTwo, colorTwo);

            icrTwo.setRect(icrOne);
            t = icrTwo;
        }
        return t;
    }
}
