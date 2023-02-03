package avpt.gr.dialogs.packing;

import avpt.gr.common.UtilsArmG;
import avpt.gr.dialogs.FileChooserRu;
import avpt.gr.sqlite_base.CreateInsertSQLite;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class PackWorkerFileFinder  extends SwingWorker<Collection<File>, File> {

    private PackTableModel model;
    private PackDialog packDialog;
    private File dir;
    private java.util.Collection<File> lstFiles;

    PackWorkerFileFinder(PackTableModel model, PackDialog packDialog,
                         FileChooserRu fileChooser, Preferences node) throws SQLException {
        //packDialog.setCursorTable(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.model = model;
        this.packDialog = packDialog;
        lstFiles = new ArrayList<File>();
        String dirName = node.get(PackDialog.NODE_PACK, ".");
        fileChooser.setCurrentDirectory(new File(dirName));
        int result = fileChooser.showOpenDialog(packDialog);
        if (result == JFileChooser.APPROVE_OPTION) {
            model.clear();
            dir = fileChooser.getSelectedFile();
            node.put(PackDialog.NODE_PACK, dir.getPath());
        }
        // packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        hashFileNames = (HashSet<String>) QueryingDataSQLite.getSetFileNames(CreateInsertSQLite.getConnection());
        CreateInsertSQLite.fillMapFilenames();

        packDialog.setBtnEnabled(PackDialog.BTN_OPEN, false);
        packDialog.setBtnEnabled(PackDialog.BTN_RUN, false);
        packDialog.setBtnEnabled(PackDialog.BTN_STOP, true);
    }

    @Override
    protected void process(List<File> chunks) {
        if (isCancelled()) return;
        for (File file : chunks) {
            model.addFile(file);
        }
    }

    @Override
    protected Collection<File> doInBackground() throws InterruptedException {
        walk(dir);
        return lstFiles;
    }

    private boolean isFileNameExists(String fileName) {
        fileName = UtilsArmG.getShortNameFile(fileName);

        return CreateInsertSQLite.getMapFilenames().containsKey(fileName);
    }

    private void walk(File dir) throws InterruptedException {
        // регулярное выражение для расширений файлов
        final String regex = "(.*/)*.+\\.(dat|img|bin|DAT|IMG|BIN)$";
        //
        File[] list = dir.listFiles();
        if (list == null) return;
        //
        for (File file : list) {
            if (file.isDirectory()) {
                walk(file);
            }
            else {
                if (Pattern.matches(regex, file.getAbsolutePath())) {
                    if (!isFileNameExists(file.getAbsolutePath()) || packDialog.isCheckAllFiles()) {
                        lstFiles.add(file);
                        publish(file);
                    }
                }
            }
            Thread.sleep(0);
        }
    }

    @Override
    protected void done() {
        packDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        packDialog.setCursorTable(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        packDialog.setBtnEnabled(PackDialog.BTN_OPEN, true);
        packDialog.setBtnEnabled(PackDialog.BTN_STOP, false);
        packDialog.setBtnEnabled(PackDialog.BTN_RUN, true);
        if (lstFiles.size() > 0)
            packDialog.selectRows(0, lstFiles.size() - 1);
    }
}
