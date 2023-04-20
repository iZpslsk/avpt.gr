package avpt.gr.components;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.blocks32.Block32;
import avpt.gr.chart_dataset.*;
import avpt.gr.chart_dataset.keysEnum.LineKeys;
import avpt.gr.common.UtilsArmG;
import avpt.gr.common.WeightBlocks;
import avpt.gr.graph.ChartArm;
import avpt.gr.graph.ChartPanelInheritor;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import avpt.gr.train.Train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

//import static avpt.gr.chart_dataset.SeriesLines.mapVisible;
import static avpt.gr.common.WeightBlocks.*;
import static avpt.gr.common.UtilsArmG.*;
import static avpt.gr.graph.ChartArm.*;
import static avpt.gr.graph.ChartPanelInheritor.COLOR_DESCRIPTION_FONT;

public class InfoPanel extends JPanel {

    private ChartArm chartArm;
    private ScrollBarArm scrollBar;
    private ChartDataset chartDataset;
    private ArrBlock32 arrBlock32;

    private int yMouseClickInfo  = -1;
    private LineKeys keySelectedLine = null; // key линии
    private int idSelectedTitle  = -1;  //
    private int idSelectedSignal = -1;
    private int idSelectedTask = -1;
    private String titleSelectedTask = null;
    private int prefSizeHeight = 10;

    public InfoPanel(ScrollBarArm scrollBar, ChartArm chartArm) {
        this.chartArm = chartArm;
        this.scrollBar = scrollBar;
        this.chartDataset = chartArm.getChartDataset();
        this.arrBlock32 = chartDataset.getArrBlock32();
       // this.chartDataset = chartDataset;
        //        doubleClickInfoPan(e);
        MouseAdapter mouseAdapter = new MouseAdapter() {

            private Point origin;
            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, InfoPanel.this);
                if (viewPort != null) {
                    Rectangle view = viewPort.getViewRect();
                    scrollRectToVisible(view);
                    SwingUtilities.updateComponentTreeUI(InfoPanel.this);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                yMouseClickInfo = e.getY();
                if (e.getClickCount() > 1) {
                    doubleClickInfoPan(e);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, InfoPanel.this);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        scrollRectToVisible(view);
                        SwingUtilities.updateComponentTreeUI(InfoPanel.this);
                    }
                }
            }

        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        setBackground(new Color(0x121212));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        double curX;
        if (chartArm != null)
            curX = chartArm.getXMarker();
        else
            curX = 0;
        final double shift = 0.00001;
        if (chartArm != null && chartArm.getStartIntervalXMarker() == chartArm.getEndIntervalXMarker())
            drawValuesCursor(curX + shift, g2);
        else
            drawInterval(curX + shift, g2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, prefSizeHeight);
    }

    private void drawInterval(double x, Graphics2D g2) {
        final String descriptionTime =      "Интервал времени: ";
        final String descriptionDistance =  "Пройденный путь:  ";
        g2.setFont(descriptFont);
        g2.setPaint(new Color(0xFEFFE7));
        int hh = (int)descriptFont.getStringBounds(chartDataset.getDateText((int)x), g2.getFontRenderContext()).getHeight();
        int hd = hh;	// hd - высота строки
        int shiftHorCur = 7;
        double startX = chartArm.getStartIntervalXMarker();
        double endX = chartArm.getEndIntervalXMarker();
        int indxStart = arrBlock32.
                searchIndexBySecond((int)chartArm.getStartIntervalXMarker(), 0, arrBlock32.size() - 1);
        int indxEnd = chartDataset.getArrBlock32().
                searchIndexBySecond((int)chartArm.getEndIntervalXMarker(), 0, arrBlock32.size() - 1);

        g2.drawString(descriptionTime + UtilsArmG.getDurationTime((long)(endX - startX)), shiftHorCur, hh);
        hh += hd;
        int distance = 0;
        Block32 block32_prev = null;
        for (int i = indxStart + 1; i <= indxEnd; i++) {
            Block32 block32 = arrBlock32.get(i - 1);
            if (block32 == null) continue;
            if (block32_prev != null) {
                int prevCoordinate = block32_prev.getCoordinate();
                int curCoordinate = block32.getCoordinate();
                int d = curCoordinate - prevCoordinate;
                if (d > 0 && d < 100) {
                    distance += d;
                }
            }
            block32_prev = block32;
        }
        g2.drawString(descriptionDistance + distance / 1000.0 + "км", shiftHorCur, hh);

        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
        List subplotsList = cdPlot.getSubplots();

        for (Object ob : subplotsList) {
            XYPlot subplot = (XYPlot) ob;
            String title = subplot.getRangeAxis().getLabel();
            XYDataset ds = subplot.getDataset(0);
            if (title.equals(CONSUMPTION_LABEL)) {
                hh += hd;
                g2.setColor(new Color(0x9BFDFCFC, true));
                g2.drawLine(0, hh, 1000, hh);
                hh += hd;
                g2.setColor(new Color(0xBDBC3B)); // цвет шрифта заголовка
                g2.setFont(titleFont);
                g2.drawString(ChartPanelInheritor.withoutShrink(title) + ":", 0, hh);
                g2.setFont(descriptFont);
                g2.setPaint(new Color(0xFEFFE7));
                for (int i = 0; i < subplot.getSeriesCount(); i++) {
                    LineKeys key = (LineKeys) subplot.getDataset().getSeriesKey(i);
                    double val_start = UtilsArmG.getVal(ds, startX, i);
                    double val_end = UtilsArmG.getVal(ds, endX, i);
                    XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
                    hh += hd;
                    String description = ChartPanelInheritor.getDescription(chartArm, ser, key, x, val_end - val_start);
                    g2.drawString(description, 0, hh);
                }
                hh += hd;
                g2.setColor(new Color(0x9BFDFCFC, true));
                g2.drawLine(0, hh, 1000, hh);
            }
        }
    }

    /**
     * рисуем информацию на инфо-панели в соответствии с позицией курсора
     * @param x - позиция
     * @param g2 - Graphics2D
     */
    private void drawValuesCursor(double x, Graphics2D g2) {
        keySelectedLine = null;
        idSelectedTitle = -1;
        idSelectedSignal = -1;
        idSelectedTask = -1;
        titleSelectedTask = null;

        g2.setFont(commonFont);
//        g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        g2.setPaint(new Color(0xFEFFE7));

        int hh = (int)commonFont.getStringBounds(chartDataset.getDateText((int)x), g2.getFontRenderContext()).getHeight();
        int hd = hh;	// hd - высота строки
        int shiftX = 7;
        // горизонтальное смещение данных курсора
        int shiftHorCur = 7;
        g2.drawString(chartDataset.getDateText((int)x), shiftHorCur, hh);
        hh += hd;
        g2.drawString(chartDataset.getTimeText((int)x), shiftHorCur, hh);
        hh += hd;
        g2.drawString(chartDataset.getRailCoordinateText((int)x), shiftHorCur, hh);
        hh += hd;
        g2.drawString(chartDataset.getLatitudeText((int)x), shiftHorCur, hh);
        hh += hd;
        g2.drawString(chartDataset.getLongitudeText((int)x), shiftHorCur, hh);

        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
        List subplotsList = cdPlot.getSubplots();
        Iterator iterator = subplotsList.iterator();
        int cntTit = 0;
        while (iterator.hasNext()) {
            XYPlot subplot = (XYPlot) iterator.next();
            String title  = subplot.getRangeAxis().getLabel();
            if (TRAIN_LABEL.equals(title)) continue;
            if (title != null && title.length() > 0) {
                hh += hd;
                if (yMouseClickInfo >= hh - hd && yMouseClickInfo < hh) {
                    idSelectedTitle = cntTit;
                }
                cntTit++;
                g2.setColor(new Color(0x525252));
                g2.drawLine(0, hh, 1000, hh);
                g2.setColor(new Color(0xBDBC3B)); // цвет шрифта заголовка
                g2.setFont(titleFont);
                // рисуем +/- перед заголовком
                int H = 6;	// длина-ширина
                int L = 3; // отступ слева
                int sizeSqr = 10;
                g2.drawLine(L, hh - sizeSqr / 2 , L + H, hh - sizeSqr / 2); // грризонтальная линия
                if (subplot.getWeight() <= 1)
                    g2.drawLine(L + H / 2, hh - sizeSqr / 2 - H / 2 ,
                            L + H / 2, hh - sizeSqr / 2 + H / 2); // вертикальная линия
                g2.drawString(ChartPanelInheritor.withoutShrink(title), shiftX + sizeSqr, hh);
                if (subplot.getWeight() <= 1) continue;
                XYDataset ds = subplot.getDataset(0);
                XYItemRenderer renderer = subplot.getRenderer();
                SeriesTasks seriesTasks = ChartPanelInheritor.getSeriesTasks(title, chartDataset);
                for (int i = 0; i < subplot.getSeriesCount(); i++) {
                    g2.setFont(descriptFont);
                    // линии
                    int dSqr = 5;
                    if (ds instanceof XYSeriesCollection) {
                        LineKeys key = (LineKeys) subplot.getDataset().getSeriesKey(i);
                        if (key == LineKeys.MAP_LINE || key == LineKeys.MAP_DIRECT) continue;
                        double val = UtilsArmG.getVal(ds, x, i);
                        XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
                        Rectangle2D rec = descriptFont.getStringBounds("_", g2.getFontRenderContext());
                        hd = (int) rec.getHeight(); // высота строки description

                        g2.setFont(descriptFont);
                        g2.setColor((Color) renderer.getSeriesPaint(i)); // квадратик рисуем в цвет текущей series
                        hh += hd;
                        String description = ChartPanelInheritor.getDescription(chartArm, ser, key, x, val);

                        if (key != LineKeys.PROFILE && key != LineKeys.PROFILE_DIRECT)
                            g2.fill(new Rectangle2D.Double(dSqr, hh - sizeSqr, sizeSqr, sizeSqr));

                        ChartPanelInheritor.setVisiblePaint(renderer, g2, i);

                        if (description.length() > 0)
                            g2.drawString(description, sizeSqr, hh);

                        if (yMouseClickInfo >= hh - hd && yMouseClickInfo < hh) {
                            keySelectedLine = (LineKeys) ((XYSeriesCollection) ds).getSeries(i).getKey();
                        }
                    }
                    else if (ds instanceof XYTaskDataset) {
                        // дискретные сигналы
                        if (SIGNALS_LABEL.equals(title) || AUTODIS_LABEL.equals(title) || SIGN_BHV_LABEL.equals(title) ||
                                SIGN_TED_LABEL.equals(title) || SIGN_TED_S5K_LABEL.equals(title) || SIGN_LINK_LABEL.equals(title)) {
                            hh += hd;
                            int key = (Integer)((XYTaskDataset) ds).getTasks().getSeries(i).getKey();
                            if (yMouseClickInfo >= hh - hd && yMouseClickInfo < hh)
                                idSelectedSignal = key;

                            g2.setColor((Color) renderer.getSeriesPaint(i));
                            g2.fill(new Rectangle2D.Double(dSqr, hh - sizeSqr, sizeSqr, sizeSqr));
                            String descript = ChartPanelInheritor.getDescriptionSignal(chartArm, g2, key, x);
                            g2.drawString(descript, sizeSqr, hh);
                        }
                        // алсн, ккм, пневматика ...
                        else {
                            hh += hd;
                            int key = (Integer)((XYTaskDataset)ds).getTasks().getSeries(i).getKey();
                            if (yMouseClickInfo >= hh - hd && yMouseClickInfo < hh) {
                                idSelectedTask = key;
                                titleSelectedTask = title;
                            }

//                            SeriesTasks seriesTasks = ChartPanelInheritor.getSeriesTasks(title, chartDataset);

                            if (seriesTasks != null) {
                                int x_ = (int)Math.round(x);
                                int val = seriesTasks.getItemVal(key, x_);
                                Train train = chartArm.getChartDataset().getArrTrains().getTrain(
                                        chartArm.getChartDataset().getArrBlock32(), x);
                                String descript = seriesTasks.getDescript(val, key, train != null ? train.getTypeLoc() : -1);
                                Paint paint = seriesTasks.getItemPaint(key, x_);
                                if (paint != null) {
                                    boolean isAsim = train != null && train.isAsim();
                                    // если кж
                                    if (!isAsim && val == TaskAlsn.ALSN_REDYELLOW && title.equals(ALSN_LABEL)) {
                                        paint = new GradientPaint(dSqr, hh - sizeSqr, Color.YELLOW, dSqr, hh, Color.RED);
                                    }
                                    if (isAsim && val == TaskAlsn.ALSN_REDYELLOW_ASIM && title.equals(ALSN_LABEL)) {
                                        paint = new GradientPaint(dSqr, hh - sizeSqr, Color.YELLOW, dSqr, hh, Color.RED);
                                    }
                                    if (val == TaskAlsnClub.ALSN_REDYELLOW && title.equals(ALSN_CLUB_LABEL)) {
                                        paint = new GradientPaint(dSqr, hh - sizeSqr, Color.YELLOW, dSqr, hh, Color.RED);
                                    }
                                    if (val == TaskAlsnBr.ALSN_REDYELLOW && (title.equals(ALSN_BR_LABEL) || title.equals(ALSN_BR_VL80_LABEL))) {
                                        paint = new GradientPaint(dSqr, hh - sizeSqr, Color.YELLOW, dSqr, hh, Color.RED);
                                    }
                                    g2.setPaint(paint);
                                    g2.fill(new Rectangle2D.Double(dSqr, hh - sizeSqr, sizeSqr, sizeSqr));
                                 }
                                if (descript != null && descript.length() > 0) g2.setFont(signalFont);
                                g2.setColor(COLOR_DESCRIPTION_FONT);
                                g2.drawString(descript, shiftX + sizeSqr, hh);
                            }
                        }
                    }
                }
            }
        }
        if (hh > prefSizeHeight) prefSizeHeight =  hh + 10;
    }

    /**
     * выполняем при двойном клике на инфо-панели
     */
    private void doubleClickInfoPan(MouseEvent event) {

        invertViewLines();

        // на дискретном сигнале перемещаемся на начало следующего
        if (/*event.isControlDown() &&*/ idSelectedSignal != -1) {
            nextSignal(idSelectedSignal);
            chartArm.setRowHexTab();
        }
        // на сигнале задач
        if (idSelectedTask != -1 && titleSelectedTask != null) {
            nextTask(idSelectedTask, titleSelectedTask);
            chartArm.setRowHexTab();
        }
        // на заголовке сворачиваем-разворачиваем линии заголовка
        if (idSelectedTitle != -1) {
            if (chartArm != null) {
                CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
                List subplotsList = cdPlot.getSubplots();
                XYPlot subplot = (XYPlot) subplotsList.get(idSelectedTitle + 1);
                String label = subplot.getRangeAxis().getLabel();
                SeriesLines seriesLines = chartDataset.getSeriesLines();
                if (label.equals(VOLTAGE_CS_LABEL)) subplot.setWeight(getWeight_voltage_cs(true));
                if (label.equals(VOLTAGE_ENGINE_LABEL)) subplot.setWeight(getWeight_voltage(true));
//                if (label.equals(VOLTAGE_LABEL_2)) subplot.setWeight(getWeight_voltage2(true));
//                if (label.equals(VOLTAGE_LABEL_3)) subplot.setWeight(getWeight_voltage3(true));
//                if (label.equals(VOLTAGE_LABEL_4)) subplot.setWeight(getWeight_voltage4(true));
                if (label.equals(AMPERAGE_COMMON_LABEL)) subplot.setWeight(getWeight_amperage_common(true));
                if (label.equals(AMPERAGE_ANCHOR_LABEL)) subplot.setWeight(getWeight_amperage_anchor(true));
                if (label.equals(AMPERAGE_EXCITATION_LABEL)) subplot.setWeight(getWeight_amperage_excitation(true));
                if (label.equals(AMPERAGE_ENGINE_LABEL)) subplot.setWeight(getWeight_amperage_engine(true));
                if (label.equals(AMPERAGE_LABEL)) subplot.setWeight(getWeight_amperage(true));
//                if (label.equals(AMPERAGE_LABEL_3)) subplot.setWeight(getWeight_amperage3(true));
//                if (label.equals(AMPERAGE_LABEL_4)) subplot.setWeight(getWeight_amperage4(true));
                if (label.equals(CONSUMPTION_LABEL)) subplot.setWeight(getWeight_consumption(true));
                if (label.equals(POWER_LABEL)) subplot.setWeight(getWeight_power(true));
                if (label.equals(TAIL_LABEL)) subplot.setWeight(getWeight_tail(true));
                //if (label.equals(MODEM_LABEL)) subplot.setWeight(subplot.getWeight() > 1 ? 1 : seriesLines.getW_modem());
                if (label.equals(PRESS_LABEL)) subplot.setWeight(getWeight_press(true));
                if (label.equals(SPEED_LABEL)) subplot.setWeight(getWeight_speed(true));
                if (label.equals(MAP_LABEL)) subplot.setWeight(getWeight_map(true));
                if (label.equals(PROF_LABEL)) subplot.setWeight(getWeight_prof(true));
                if (label.equals(POSITION_LABEL)) subplot.setWeight(getWeight_position(true));
                if (label.equals(ALSN_LABEL)) subplot.setWeight(getWeight_alsn(true));
                if (label.equals(ALSN_BR_LABEL)) subplot.setWeight(getWeight_alsn_br(true));
                if (label.equals(ALSN_BR_VL80_LABEL)) subplot.setWeight(getWeight_alsn_br(true));
                if (label.equals(ALSN_CLUB_LABEL)) subplot.setWeight(getWeight_alsn_club(true));
                if (label.equals(AUTO_LABEL)) subplot.setWeight(getWeight_auto_drive(true));
                if (label.equals(PNEUMATIC_LABEL)) subplot.setWeight(getWeight_pneumatic1(true));
                if (label.equals(PNEUMATIC2_LABEL)) subplot.setWeight(getWeight_pneumatic2(true));
                if (label.equals(PNEUMATIC_USAVP_LABEL)) subplot.setWeight(getWeight_pneumatic_usavp(true));
                if (label.equals(UATL_LABEL)) subplot.setWeight(getWeight_uatl(true));
                if (label.equals(KKM_S5K_LABEL)) subplot.setWeight(getWeight_kkm(true));
                if (label.equals(BHV_LABEL)) subplot.setWeight(getWeight_bhv(true));
                if (label.equals(KKM_S5K_2_LABEL)) subplot.setWeight(getWeight_kkm2(true));
                if (label.equals(KKM_VL10_LABEL)) subplot.setWeight(getWeight_kkm_vl10(true));
                if (label.equals(KKM_S5_LABEL)) subplot.setWeight(getWeight_kkm_s5(true));
                if (label.equals(VSC_LABEL)) subplot.setWeight(getWeight_vsc(true));
                if (label.equals(CABIN_LABEL)) subplot.setWeight(getWeight_cabin(true));
                if (label.equals(MAIN_CONTROL_LABEL)) subplot.setWeight(getWeight_main_control(true));
                if (label.equals(REV_CONTROL_LABEL)) subplot.setWeight(getWeight_rev_control(true));
                if (label.equals(SCHEMA_LABEL)) subplot.setWeight(getWeight_schema(true));
                if (label.equals(KEYS_LABEL)) subplot.setWeight(getWeight_push_key(true));
                if (label.equals(SIGNALS_LABEL)) subplot.setWeight(getWeight_signals(true));
                if (label.equals(AUTODIS_LABEL)) subplot.setWeight(getWeight_signals_autodis(true));
                if (label.equals(SIGN_BHV_LABEL)) subplot.setWeight(getWeight_signals_bhv(true));
                if (label.equals(SIGN_TED_LABEL)) subplot.setWeight(getWeight_signals_ted(true));
                if (label.equals(SIGN_TED_S5K_LABEL)) subplot.setWeight(getWeight_signals_ted_s5k(true));
                if (label.equals(SIGN_LINK_LABEL)) subplot.setWeight(getWeight_signals_link(true));
                WeightBlocks.setModified(true);
            }
            setWeightTr();
        }
    }

    /**
     * установка веса раздела avpt.gr.train
     */
    private void setWeightTr() {
        CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
        List subplotsList = cdPlot.getSubplots();
        Iterator it = subplotsList.iterator();
        int sumWeith = 0;
        while (it.hasNext()) {
            XYPlot plot  = (XYPlot)it.next();
            if (!plot.getRangeAxis().getLabel().equals(TRAIN_LABEL))
                sumWeith += plot.getWeight();
        }
        XYPlot trPlot = (XYPlot)subplotsList.get(0);
        int weightTr = sumWeith * 3 / 100;
        trPlot.setWeight(weightTr);
    }

    /**
     * инвертируем видимость линии
     */
    private void invertViewLines() {
        if (keySelectedLine != null && chartArm != null) {
            CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
            for (Object ob : cdPlot.getSubplots()) {
                XYPlot subplot = (XYPlot) ob;
                XYDataset ds = subplot.getDataset(0);
                XYItemRenderer rend = subplot.getRenderer();
                double maxY = 0;
                for (int i = 0; i < subplot.getSeriesCount(); i++) {
                    if (ds instanceof XYSeriesCollection) {
                        XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
                        LineKeys key = (LineKeys) ser.getKey();
                        if (keySelectedLine == key
                                && key != LineKeys.PROFILE
                                && key != LineKeys.PROFILE_DIRECT
                                && key != LineKeys.POSITION
                                && key != LineKeys.POSITION_S5k
                                && key != LineKeys.WEAK_FIELD) {
                                chartArm.setLimMap(subplot, key, rend.isSeriesVisible(i));
                                rend.setSeriesVisible(i, !rend.isSeriesVisible(i), true);
                                SeriesLines.getMapVisible().put(key.getName(), rend.isSeriesVisible(i));
                                WeightBlocks.setModified(true);
                        }
                        if (rend.isSeriesVisible(i)) {
                            maxY = !Double.isNaN(ser.getMaxY()) ? Math.max(ser.getMaxY(), maxY) : 0;
                        }
                    }
                }
                if (maxY > 0) {
                    ValueAxis curRange = subplot.getRangeAxis();
                    Range range = curRange.getRange();
                    curRange.setRange(range.getLowerBound(), maxY + 3);
                }
            }
        }
    }

//    /**
//     * переключение видимости линий
//     * @param isSingleSect - по одной или по всем секциям
//     */
//    @SuppressWarnings("unchecked")
//    private void changeViewSer(boolean isSingleSect) {
//        if (keySelectedLine != null && chartArm != null) {
//          //  int key = UtilsArmTm.getNumSectFromKey(keySelectedLine);
//          //  ChartArm.setModified(true);
//            CombinedDomainXYPlot cdPlot = (CombinedDomainXYPlot)chartArm.getXYPlot();
//            List subplotsList = cdPlot.getSubplots();
//            for (Object ob : subplotsList) {
//                XYPlot subplot = (XYPlot) ob;
//                XYDataset ds = subplot.getDataset(0);
//                XYItemRenderer rend = subplot.getRenderer();
//                for (int i = 0; i < subplot.getSeriesCount(); i++) {
//                    if (ds instanceof XYSeriesCollection) {
//                        XYSeries ser = ((XYSeriesCollection) ds).getSeries(i);
//                        boolean is_on = !rend.isSeriesVisible(i);
//                        if (keySelectedLine.equals(ser.getKey())) {
//                            for (int j = 0; j < subplot.getSeriesCount(); j++) {
//                                XYSeries ser2 = ((XYSeriesCollection) ds).getSeries(j);
//                             //   int curKey = UtilsArmTm.getNumSectFromKey(ser2.getKey().toString());
//                             //   if (!isSingleSect || curKey == key) {
//                                    rend.setSeriesVisible(j, is_on, true);
//                              //      mapViewLines.put(ser2.getKey(), rend.isSeriesVisible(j));
//                             //   }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    /**
//     * Если координата y click mouse попадает в диапазон на infoPanel
//     * определяем key для идентификации выбранного по клику сигнала
//     * @param hh -
//     * @param hd -
//     * @param key - номер секции (Hi) и id 1-40 (Lo)
//     */
//    private void setIdSelectedSignal(int hh, int hd, int key) {
//        if (yMouseClickInfo >= hh - hd && yMouseClickInfo < hh)
//            idSelectedSignal = key;
//    }

    private void nextTask(int key, String title) {
        SeriesTasks seriesTasks = ChartPanelInheritor.getSeriesTasks(title, chartDataset);
        int size = seriesTasks.size(key);
        double x = chartArm.getXMarker();
        int next = 0;
        int val_empty = title.equals(KEYS_LABEL) ? -1 : 0;
        for (int i = 0; i < size; i++) {
            ListTasks.ItemTask item = seriesTasks.get(key, i);
            next = item.getBegin();
            if (next > x && item.getValue() != val_empty) {
                break;
            }
            else
                next = seriesTasks.get(key, 0).getBegin();
        }
        chartArm.paintMarker(null, null, scrollBar,  next);
    }

    /**
     * позиция курсора на следующий дискретный сигнал
     * @param key - вид сигнала
     */
    private void nextSignal(int key) {
        ListSignals signals = chartArm.getChartDataset().getChartArrays().getListSignals();
        ListSignals.ListSignal signal = signals.getListByKey(key);
        double x = chartArm.getXMarker();
        int next = 0;
        for (int i = 0; i < signal.getList().size(); i++) {
            ListSignals.ItemSignal item = signal.getList().get(i);
            next = item.getBegin();
            if (next > x)
                break;
            else {
                next = signal.getList().get(0).getBegin();
            }
        }
        chartArm.paintMarker(null, null, scrollBar,  next);
    }
}
