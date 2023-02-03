package avpt.gr.dialogs.packing;

import avpt.gr.common.UtilsArmG;
import avpt.gr.dialogs.FileChooserRu;
import avpt.gr.sqlite_base.CreateInsertSQLite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

public class PackDialog extends JDialog {

    private PackWorkerFileFinder packWorkerFileFinder;
    static final String NODE_PACK = "pack_dialog";
    static final String NODE_KEY_ROAD = "index_road";
    private static final Preferences node_pack_dialog = UtilsArmG.getNode(NODE_PACK);
    // id кнопок панели инструментов
    static final int BTN_OPEN = 0;
    static final int BTN_RUN = 1;
    //public static final int BTN_PAUSE = 2;
    static final int BTN_STOP = 3;
    private FileChooserRu fileChooser;
    private PackTableModel model;
    private JTable table;

    private JButton btnOpen;
    private JButton btnRun;
    //JButton btnPause;
    private JButton btnStop;
    private JCheckBox checkAllFiles;
    private int cntFiles;
    private boolean isCanceledRead;
    private ArrayList<CreateInsertSQLite.ItemRoad> roads;
    private JSpinner spinTch;
    private int indexRoad;
    private int codeRoad;
    private String nameRoad;
    ExecutorService threadPool;

    public PackDialog(JFrame owner) {

        super (owner, "Пакетная обработка", true);
        try {
            roads = CreateInsertSQLite.getArrRoads();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        indexRoad = node_pack_dialog.getInt(NODE_KEY_ROAD, 0);
        codeRoad = roads.get(indexRoad).getCode();
        nameRoad = roads.get(indexRoad).getName();

        // расположение окна
        UtilsArmG.setWinBound(this, node_pack_dialog);

        // обработчик событий окна
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                node_pack_dialog.putInt("left", getX());
                node_pack_dialog.putInt("top", getY());
                node_pack_dialog.putInt("width", getWidth());
                node_pack_dialog.putInt("height", getHeight());
                node_pack_dialog.putInt(NODE_KEY_ROAD, indexRoad);
                if (spinTch != null)
                    node_pack_dialog.putInt("num_tch", (Integer) spinTch.getValue());
            }
        });
        makeToolBar();
        model = new PackTableModel();
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getColumn(PackTableModel.ORD_PROGRESS).setCellRenderer(new PackTableModel.ProgressCellRender());
        table.getColumnModel().getColumn(PackTableModel.ORD_CHECKBOX).setCellRenderer(new PackTableModel.CheckCellRender());

        PackTableModel.setColumnsWidth(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        fileChooser = new FileChooserRu();
        fileChooser.setDialogTitle("Выбор папки с поездками");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

    }

    private class BtnAction extends AbstractAction {

        private final int btnKind;

        private BtnAction(int btnKind) {

            this.btnKind = btnKind;
            String description = "???";
            Icon icon = null;
            switch (btnKind) {
                case BTN_OPEN:
                    description = "выбрать папку";
                    icon = new ImageIcon(getClass().getResource("/avpt/gr/dialogs/images/opened-folder-16.png"));
                    break;
                case BTN_RUN:
                    description = "пуск";
                    icon = new ImageIcon(getClass().getResource("/avpt/gr/dialogs/images/play-16.png")); setEnabled(false);
                    break;
//                case BTN_PAUSE:
//                    description = "пауза";
//                    icon = new ImageIcon(getClass().getResource("../avpt.gr.images/pause-16.png")); setEnabled(false);
//                break;
                case BTN_STOP:
                    description = "стоп";
                    icon = new ImageIcon(getClass().getResource("/avpt/gr/dialogs/images/stop-16.png")); setEnabled(false);
            }
            putValue(Action.SHORT_DESCRIPTION, description);
            putValue(Action.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (btnKind) {
                case BTN_OPEN:
                    setEnabledTable(true);
                    //table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        packWorkerFileFinder = new PackWorkerFileFinder(
                                model, PackDialog.this, fileChooser, node_pack_dialog);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    packWorkerFileFinder.execute();
                    break;
                case BTN_RUN:
                    isCanceledRead = false;
                    //table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//                    setEnabledTable(false);
//                    btnRun.setEnabled(true);
                    // setBtnEnabled(BTN_RUN, true);
                    if (packWorkerFileFinder == null) return;
                    try {
                        Collection<File> files = packWorkerFileFinder.get();
                        cntFiles = files.size();
                        if (table.getSelectedRowCount() > 0) {
                            setEnabledTable(false);
                            setBtnEnabled(PackDialog.BTN_RUN, false);
                            setBtnEnabled(PackDialog.BTN_OPEN, false);
                        }
                        int i = 0;
                        final int nThread = 1;
                        threadPool = Executors.newFixedThreadPool(nThread);
                        for (File file : files) {
                            if (table.isRowSelected(i)) {
                                PackWorkerFileReader packWorkerFileReader = new PackWorkerFileReader(
                                        model, file, i, ++cntFiles, PackDialog.this);
                                threadPool.submit(packWorkerFileReader);
//                                new PackWorkerFileReader(model, file, i, ++cntFiles, PackDialog.this).execute();
                            }
                            i++;
                        }
                        if (cntFiles == 0) {
                            setBtnEnabled(PackDialog.BTN_RUN, true);
                            setBtnEnabled(PackDialog.BTN_OPEN, true);
                            setEnabledTable(true);
                        }
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    break;
                case BTN_STOP:
                    packWorkerFileFinder.cancel(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    setBtnEnabled(BTN_STOP, false);
                    setEnabledTable(true);
//                    btnStop.setEnabled(false);
                    isCanceledRead = true;
                    if (threadPool != null) threadPool.shutdownNow();
            }
        }
    }

    /**
     * создаем spinTch
     * @param toolBar -
     * @param width -
     * @param height -
     */
    private void makeSpinTch(JToolBar toolBar, int width, int height) {
        int numTch = node_pack_dialog.getInt("num_tch", 0);
        SpinnerModel model = new SpinnerNumberModel(numTch, 0, 1000, 1);
        spinTch = new JSpinner(model);
        spinTch.setMinimumSize(new Dimension(width, height));
        spinTch.setPreferredSize(new Dimension(width, height));
        spinTch.setMaximumSize(new Dimension(width, height));
        toolBar.add(spinTch);
    }

    /**
     * создаем comboRoads - список дорог (коды по индексу в ArrayList roads)
     * @param toolBar -
     * @param width -
     * @param height -
     */
    private void makeComboRoads(JToolBar toolBar, int width, int height) {
        final JComboBox comboRoads = new JComboBox();
        comboRoads.setMinimumSize(new Dimension(width, height));
        comboRoads.setPreferredSize(new Dimension(width, height));
        comboRoads.setMaximumSize(new Dimension(width, height));
        for (CreateInsertSQLite.ItemRoad item : roads) {
            comboRoads.addItem(item.getName());
        }
        comboRoads.setSelectedIndex(indexRoad);
        comboRoads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexRoad = comboRoads.getSelectedIndex();
                codeRoad = roads.get(indexRoad).getCode();
                nameRoad = roads.get(indexRoad).getName();
                if (indexRoad == 0)
                    spinTch.setValue(0);
            }
        });
        toolBar.add(comboRoads);
    }

    private void makeToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(btnOpen = new JButton(new BtnAction(BTN_OPEN)));
        btnOpen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolBar.addSeparator();
        toolBar.add(btnRun = new JButton(new BtnAction(BTN_RUN)));
        btnRun.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolBar.add(btnStop = new JButton(new BtnAction(BTN_STOP)));
        btnStop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolBar.addSeparator();
        toolBar.add(checkAllFiles = new JCheckBox("успешно расшифрованные"));
        makeComboRoads(toolBar, 150, 25);
        makeSpinTch(toolBar, 40, 25);
        add(toolBar, BorderLayout.NORTH);
    }

    public void selectRows(int i1, int i2) {

        table.setRowSelectionInterval(i1, i2);
        table.setColumnSelectionInterval(2, 2);
        table.setEditingColumn(PackTableModel.ORD_CHECKBOX);
    }

    public void deselectRows(int i1, int i2) {
        if (i1 >= 0 && i1 < table.getRowCount() )
            table.changeSelection(i1, 0, true, false);

        //table.changeSelection(table.getRowCount() - 1, 1, false, true);
        //table.removeRowSelectionInterval(i1, i2);
    }

    public void tableRepaint() {
        table.repaint();
    }

    public void setCursorTable(Cursor cursor) {
        table.setCursor(cursor);
    }

    public void setBtnEnabled(int btnKind, boolean isEnabled) {
        switch (btnKind) {
            case BTN_OPEN:
                if (btnOpen != null) btnOpen.setEnabled(isEnabled);
                break;
            case BTN_RUN:
                if (btnRun != null) btnRun.setEnabled(isEnabled);
                break;
//            case BTN_PAUSE:
//                if (btnPause != null) btnPause.setEnabled(isEnabled);
//                break;
            case BTN_STOP:
                if (btnStop != null) btnStop.setEnabled(isEnabled);
                break;
        }
    }

    public void setEnabledTable(boolean isEnabled) {
        table.setEnabled(isEnabled);
    }

    public int getCntFiles() {
        return cntFiles;
    }

    public boolean isCanceledRead() {
        return isCanceledRead;
    }

    public boolean isCheckAllFiles() {
        return checkAllFiles.isSelected();
    }

    public int getNumTch() {
        return (Integer) spinTch.getValue();
    }

    public int getCodeRoad() {
        return codeRoad;
    }

    public String getNameRoad() {
        return nameRoad;
    }
}
