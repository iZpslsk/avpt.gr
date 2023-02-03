package avpt.gr.dialogs;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * диалог анимации при загрузке файла поездки
 */
public class LoadAnimate extends JDialog {

    //private static Worker worker;

    private static class Worker extends SwingWorker<ChartDataset, Void> {

        private String fileName;
        private JDialog load;
        private boolean isShift;
        private JDialog owner;
        private boolean isVisible = true;
        private ArrBlock32 arrBlock32;
        private int nBlStart;
        private int nBlEnd;
        private boolean isTime;

        /**
         * конструктор по имени файла
         * nBLStart и nBlEnd инициализируются после создания ArrBlock32 от 0 до arrBlock32.size() - 1
         * @param owner - диалоговое окно - владелец
         * @param fileName - имя файла поездки
         * @param load - диалог LoadAnimate
         * @param isShift - есть ли сдвижка
         */
        public Worker(JDialog owner, String fileName, JDialog load, boolean isShift, boolean isTime) {
            SwingUtilities.updateComponentTreeUI(load);
            this.fileName = fileName;
            this.load = load;
            this.isShift = isShift;
            this.isTime = isTime;
            this.owner = owner;
        }

        /**
         * конструктор по ArrBlock32 (если ArrBlock32 уже существует)
         * @param owner - диалоговое окно - владелец
         * @param arrBlock32 - массив 32-х байтных посылок
         * @param load - диалог LoadAnimate
         * @param isShift - есть ли сдвижка
         */
        public Worker(JDialog owner, ArrBlock32 arrBlock32, JDialog load, boolean isShift, int nBlStart, int nBlEnd, boolean isTime) {
            SwingUtilities.updateComponentTreeUI(load);
            this.load = load;
            this.isShift = isShift;
            this.owner = owner;
            this.arrBlock32 = arrBlock32;
            this.nBlStart = nBlStart;
            this.nBlEnd = nBlEnd;
            this.isTime = isTime;
        }

        @Override
        protected ChartDataset doInBackground() throws IOException {
            ChartDataset chartDataset;
            try {
                // если строим по имени файла необходима инициализация nBlStart и nBlEnd
                if (arrBlock32 == null) {
                    arrBlock32 = new ArrBlock32(fileName, isShift);
                    nBlStart = 0;
                    nBlEnd = arrBlock32.size() - 1;
                    if (nBlEnd < 0) nBlEnd = 0;
                }
                chartDataset = new ChartDataset(arrBlock32, isTime, true);
                chartDataset.setSeriesLines(0, arrBlock32.get(arrBlock32.size() - 1).getSecond(), 1);
//                chartDataset.setSeriesLines(10000, 15000, 1);
                chartDataset.setSeriesSignalsDiscrete();
                chartDataset.setSeriesSignalsAutodisp();
                chartDataset.setSeriesSignalsBHV();;
                chartDataset.setSeriesSignalsTed();
                chartDataset.setSeriesSignalsTed_s5k();
                chartDataset.setSeriesSignalsLink();
                chartDataset.setSeriesAlsn();
                chartDataset.setSeriesAlsnClub();
                chartDataset.setSeriesAlsnBr_vl10();
                chartDataset.setSeriesAlsnBr_vl80();
                chartDataset.setSeriesUatl();
                chartDataset.setSeriesAuto();
                chartDataset.setSeriesPneumatic();
                chartDataset.setSeriesBhv();
  //              chartDataset.setSeriesPneumatic2();
                chartDataset.setSeriesPneumaticUsavp();;
                chartDataset.setSeriesKKM_s5k();
                chartDataset.setSeriesKKM_s5k_2();
                chartDataset.setSeriesKKM_vl10();
                chartDataset.setSeriesKKM_s5();
                chartDataset.setSeriesPushKey();
                chartDataset.setSeriesVsc();
                chartDataset.setSeriesCabin();
                chartDataset.setSeriesMainControl();
                chartDataset.setSeriesRevControl();
                chartDataset.setSeriesSchema();
            } catch (IOException e) {
                isVisible = true;
                throw new IOException(e.getMessage());
            }
            return chartDataset;
        }

        @Override
        protected void done() {
            load.dispose();
            owner.getOwner().setVisible(isVisible);
        }
    }

    private LoadAnimate(JDialog owner, String caption) {
        super(owner, true);
        setResizable(false);
        JLabel gifLabel = new JLabel();
        JLabel capLabel = new JLabel(caption, JLabel.CENTER);
        capLabel.setText(caption);
        gifLabel.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/animate/circle1.gif")));
        capLabel.setOpaque(true);
        capLabel.setBackground(new Color(11, 11, 11, 225));
        capLabel.setForeground(Color.WHITE);
        capLabel.repaint();
        add(gifLabel, BorderLayout.CENTER);
        add(capLabel, BorderLayout.NORTH);
        setUndecorated(true);
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    /**
     * @param owner - диалоговое окно (владелец)
     * @param fileName - имя файла поездки
     * @param isShift - есть ли сдвижка
     * @return - ChartDataset
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    public static ChartDataset execMakeArrBl32(JDialog owner, String fileName, boolean isShift, boolean isTime)
            throws ExecutionException, InterruptedException {
        LoadAnimate dialog = new LoadAnimate(owner, "файл: " + UtilsArmG.getShortNameFile(fileName));
        Worker worker = new Worker(owner, fileName, dialog, isShift, isTime);
        worker.execute();
        SwingUtilities.updateComponentTreeUI(dialog);
        dialog.setVisible(true);
        return worker.get();
    }

    /**
     * @param owner - диалоговое окно (владелец)
     * @param arrBlock32 - массив 32-х байтных посылок
     * @param isShift - есть ли сдвижка
     * @return - ChartDataset
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    public static ChartDataset execMakeArrBl32(JDialog owner, ArrBlock32 arrBlock32, boolean isShift, int nBlStart, int nBlEnd, boolean isTime)
            throws ExecutionException, InterruptedException {
        LoadAnimate dialog = new LoadAnimate(owner, "файл: " + UtilsArmG.getShortNameFile(arrBlock32.getFileName()));
        Worker worker = new Worker(owner, arrBlock32, dialog, isShift, nBlStart, nBlEnd, isTime);
        worker.execute();
        SwingUtilities.updateComponentTreeUI(dialog);
        dialog.setVisible(true);
        return worker.get();
    }
}
