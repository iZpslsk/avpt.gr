package avpt.gr.graph;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class DualPaint implements Paint {

    private final int[] colorOne;
    private final int[] colorTwo;

    public DualPaint(Color c1, Color c2) {
        colorOne = makeRgbArr(c1);
        colorTwo = makeRgbArr(c2);
    }

    private int[] makeRgbArr(Color c) {
        int[] arr = new int[4];
        arr[0] = c.getRed();
        arr[1] = c.getGreen();
        arr[2] = c.getBlue();
        arr[3] = c.getAlpha();
        return arr;
    }

    public PaintContext createContext(ColorModel cm,
                                      Rectangle deviceBounds,
                                      Rectangle2D userBounds,
                                      AffineTransform transform,
                                      RenderingHints hints) {
        return new Context();
    }

    public int getTransparency() {
        return java.awt.Transparency.OPAQUE;
    }

    class Context implements PaintContext {

        public void dispose() {}

        public ColorModel getColorModel() {
            return ColorModel.getRGBdefault();
        }

        public Raster getRaster(int x, int y, int w, int h) {
            int half_h = Math.max((int)Math.round(h/2.2), 1);
            WritableRaster raster_half = makeRaster(colorOne, w, half_h);
            WritableRaster raster_whole = makeRaster(colorTwo, w, h);
            raster_whole.setRect(raster_half);
            return raster_whole;
        }

        private WritableRaster makeRaster(int[] color, int w, int h) {
            WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    raster.setPixel(x, y, color);
                }
            }
            return raster;
        }

    }
}
