package avpt.gr.dialogs.packing;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartArrays;
import avpt.gr.common.UtilsArmG;
import avpt.gr.maps.Limits;
import avpt.gr.maps.Stations;
import avpt.gr.sqlite_base.CreateInsertSQLite;
import avpt.gr.train.ArrTrains;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PackWorkerFileReader  extends SwingWorker<String, Integer>  {

    private File file;
    private PackTableModel model;
    private PackDialog packDialog;
    private ArrBlock32 arrBlock32;
    private ArrTrains arrTrains = new ArrTrains();
    private ChartArrays chartArrays;
    private Stations stations;
    private Limits limits;
  //  private ChartDataset chartDataset;
    private int rowIndex;
    private int amountFiles;
    private boolean isReadError = false;

    PackWorkerFileReader(PackTableModel model, File file, int rowIndex, int amountFiles, PackDialog packDialog) {

        this.file = file;
        this.model = model;
        this.packDialog = packDialog;
        this.rowIndex = rowIndex;
        this.amountFiles = amountFiles;
        //packDialog.setBtnEnabled(PackDialog.BTN_RUN, false);
        //   packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        packDialog.setBtnEnabled(PackDialog.BTN_STOP, true);
        packDialog.setEnabledTable(false);
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("progress")) {
                    PackWorkerFileReader.this.model.updateProgress(PackWorkerFileReader.this.file,
                            (Integer)evt.getNewValue());
                }
            }
        });
    }

    @Override
    protected void process(List<Integer> chunks) {
        if (packDialog.isCanceledRead()) {
            cancel(true);
        }
        if (isCancelled()) return;
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        for (int index : chunks) {
            setProgress(index);
            packDialog.tableRepaint();
        }
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    protected String doInBackground() {

        if (file.isFile()) {
            if (file.length() > UtilsArmG.MAX_SIZE_FILE) {
                isReadError = true;
                return "Файл поездки превышает допустимый размер!";
            }
            try {
                arrBlock32 = new ArrBlock32(file.getAbsolutePath(), false);
            } catch (IOException e) {
                isReadError = true;
                return e.getLocalizedMessage();
            }
            int perc = (int)Math.round(arrBlock32.size() * 0.05 / 100);
            chartArrays = new ChartArrays(arrBlock32, arrTrains, true);
         //   chartDataset = new ChartDataset(arrBlock32, true, false);

            chartArrays.setCurTypeMove(arrBlock32.getFirstTypeMove(0));
            chartArrays.setCurTypeLoc(arrBlock32.getFirstTypeTrain(0));
            //chartArrays.setCurTypeMove(arrBlock32.getFirstTypeMove(0));
            for (int i = 0; i < arrBlock32.size(); i++) {
                chartArrays.doStep(i);
                if (i % perc == 0) {
                    int progress = 50 + (int)Math.round(((double) i / (double) arrBlock32.size()) * 50d);
                    publish(progress);
                }
            }
            chartArrays.finalStep();
            stations = new Stations(arrBlock32);
            stations.addStationsToTrains(arrTrains);
            limits = new Limits(arrBlock32);
            limits.addLimitsToTrains(arrTrains);
        }
        return file.getAbsolutePath();
    }

    @Override
    protected void done() {
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (isCancelled()) {
            packDialog.setBtnEnabled(PackDialog.BTN_OPEN, true);
            packDialog.setBtnEnabled(PackDialog.BTN_RUN, true);
            packDialog.setBtnEnabled(PackDialog.BTN_STOP, false);
            packDialog.setEnabledTable(true);
            packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        try {
            CreateInsertSQLite.insertTrains(arrTrains, arrBlock32.getFileName(), packDialog.getCodeRoad(), packDialog.getNumTch());
            CreateInsertSQLite.doDisconnect();
            CreateInsertSQLite.doConnection(UtilsArmG.SQULite_BASE_NAME);
            //     Thread.sleep(1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                CreateInsertSQLite.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        //  packDialog.setBtnEnabled(PackDialog.BTN_STOP, false);
        if (!isReadError) {
            packDialog.deselectRows(rowIndex, rowIndex);
            publish(100);
        }
        packDialog.tableRepaint();
//        try {
//            Thread.sleep(5);
//        } catch (InterruptedException e) {-*
//            e.printStackTrace();
//        }
        //   packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        //packDialog.setCursorTable(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        //packDialog.setBtnEnabled(PackDialog.BTN_STOP, false);
        // System.out.println(amountFiles + " cnt=" + packDialog.getCntFiles());
        if (amountFiles >= packDialog.getCntFiles()) {
            //  System.out.println(amountFiles + " cnt=" + packDialog.getCntFiles());
            packDialog.setBtnEnabled(PackDialog.BTN_OPEN, true);
            packDialog.setBtnEnabled(PackDialog.BTN_RUN, true);
            packDialog.setBtnEnabled(PackDialog.BTN_STOP, false);
            packDialog.setEnabledTable(true);
            packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(packDialog, "Обработка завершена!", "АРМ ТМ", JOptionPane.INFORMATION_MESSAGE);
        }
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
