package avpt.gr.dialogs;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import java.awt.event.*;
import java.io.File;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.ComponentMover;
import avpt.gr.common.ComponentResizer;
import avpt.gr.common.UtilsArmG;
import avpt.gr.common.WeightBlocks;
import avpt.gr.components.hex_tab.HexTab;
import avpt.gr.graph.ChartPanelArm;
import org.threeten.bp.LocalDateTime;

import static avpt.gr.start.StartFrame.TITLE_ARM;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TrainAnalysis extends JDialog {
    // Windows 10 64-bit  HKEY_LOCAL_MACHINE\Software\JavaSoft\Prefs
    // HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs\avpt\arm\arm_g\/Train/Analysis
    public static final Preferences PREF = UtilsArmG.getNode("TrainAnalysis");
    private final FileChooserRu fileChooser = makeFileChooser();
    private ArrBlock32 arrBlock32;
    private final JFrame owner;
    private final boolean  isShift = PREF.getBoolean("shift", true);
    private String fileName;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private boolean isTime = true;
    private boolean isMaximize = false;
    private boolean isResize = false;

    private final OpenAction openAction = new OpenAction("Открыть...", "/avpt/gr/images/menu/open16.png");
    private final OpenSlaveAction openSlaveAction = new OpenSlaveAction("Сцепка...", "/avpt/gr/images/menu/chain_16.png");
    private final ExitAction exitAction = new ExitAction("Закрыть", "/avpt/gr/images/menu/close16.png");
    private final MinimizeAction minimizeAction = new MinimizeAction("", "/avpt/gr/images/menu/minimize16.png");
    private final MaximizeAction maximizeAction = new MaximizeAction("", "/avpt/gr/images/menu/maximize16.png");
    private final NormalizeAction normalizeAction = new NormalizeAction("", "/avpt/gr/images/menu/normalize16.png");
    private final ViewCoordinateAction viewCoordinateAction = new ViewCoordinateAction("Развернуть " + VIEW_COORDINATE, "/avpt/gr/images/menu/distance16.png");
    private final ViewTimeAction viewTimeAction = new ViewTimeAction("Развернуть " + VIEW_TIME, "/avpt/gr/images/menu/time16.png");
    private final ViewDefAction viewDefAction = new ViewDefAction("Восстановить стандартный вид", "/avpt/gr/images/menu/reload16.png");
    private final ShowInfoTrainsAction showInfoTrainsAction = new ShowInfoTrainsAction("Отчеты", "/avpt/gr/images/menu/note_16.png");
    private final HelpAction helpAction = new HelpAction("Помощь...", "/avpt/gr/images/menu/help16.png");
    private final AboutAction aboutAction = new AboutAction("О программе...", "/avpt/gr/images/menu/info16.png");
    private JButton exitBtn;
    private JButton minimizeBtn;
    private JButton maximizeBtn;
    private JButton normalizeBtn;
    private JButton viewTimeBtn;
    private JButton viewCoordinateBtn;

    private JRadioButtonMenuItem itemCoordinate;
    private JRadioButtonMenuItem itemTime;
    private JMenuItem defViewItem;

    private ChartPanelArm chartPanelArm;
    private JMenuItem openItemSlave;
    private JMenuItem openItem;
    private JMenuItem showInfoTrainsItem;
    private final TrainAnalysis trainAnalysisMain; // для первого всегда = null, для второго всегда != null
    private TrainAnalysis trainAnalysisSlave; // для первого окна != null если открыто второе
    private InfoTrainsDialog infoTrainsDialog;

    private ComponentResizer componentResizer;
    private ComponentMover componentMover;

    private static final String VIEW_TIME = "по времени";
    private static final String VIEW_COORDINATE = "по координате";

    /**
     * @param owner - frame
     * @param isClose - закрываем все приложение после закрытия окна (используем при запуске из коммандной строки)
     */
    public TrainAnalysis(JFrame owner, final boolean isClose, final TrainAnalysis trainAnalysisMain) {
        super(owner,false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.owner = owner;
        this.trainAnalysisMain = trainAnalysisMain;
        init(isClose);
    }

    private void init(final boolean isClose) {
        setLayout(new BorderLayout());
        setUndecorated(true);
        WeightBlocks.loadSettings(PREF, false);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isMaximize) isResize = true;
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (isMaximize && isResize) {
                    setNormalizeWin();
                }
                isResize = false;
            }
        });

        addWindowListener(new WindowAdapter() {
            // событие закрытия окна
            public void windowClosing(WindowEvent event) {
                // сохранение состояния если не открыто два окнаviewToggleAction
                if (!isMaximize && trainAnalysisSlave == null && trainAnalysisMain == null) {
                    UtilsArmG.saveWinBound(TrainAnalysis.this, PREF);
                }
                // если закрываем из режима сцепки "save and slave", сохраняем оптимальное положение окна
                else if (!isMaximize && (trainAnalysisSlave == null || trainAnalysisMain == null)) {
                    UtilsArmG.saveWinBoundOptimal(PREF);
                }
                PREF.putBoolean("shift", isShift);
                // закрываем оба окна
                if (trainAnalysisSlave != null) {
                    trainAnalysisSlave.dispose();
                }
                //
                if (trainAnalysisMain != null) {
                    trainAnalysisMain.dispose();
                }

                if (isClose) System.exit(0);

                closeDialog();
            }
        });
        makeToolBar();
        createMenuBar();
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);

        setNormalizeWin();
        viewTimeBtn.setVisible(!isTime);
        viewCoordinateBtn.setVisible(isTime);
        setComponentMover();
        setComponentResizer();
        getRootPane().setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) );
        setTitle();
    }

    /**
     * устанавливаем возможность изменения размеров undecorated - окна
     */
    private void setComponentResizer() {
        componentResizer = new ComponentResizer();
        componentResizer.registerComponent(this);
        componentResizer.setSnapSize(new Dimension(1, 1));
    }

    /**
     * назначаем компоненты для перетаскивания undecorated - окна
     */
    private void setComponentMover() {
        componentMover = new ComponentMover(this, menuBar, toolBar);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        componentMover.setEdgeInsets(new Insets((int)dim.getHeight() * -1, (int)dim.getWidth() * -1,
                (int)dim.getHeight() * -1, (int)dim.getWidth() * -1));
    }

    /**
     * обработка закрытия окна
     */
    private void closeDialog() {
        if (WeightBlocks.isModified()) {
            int n = messClose();
            if (n == JOptionPane.YES_OPTION) {
                WeightBlocks.saveSettings(PREF);
                TrainAnalysis.this.dispose();
                WeightBlocks.setModified(false);
            } else if (n == JOptionPane.NO_OPTION) {
                TrainAnalysis.this.dispose();
                WeightBlocks.setModified(false);
            }
        }
        else
            TrainAnalysis.this.dispose();
    }

    private int messClose() {
        Object[] options = {"Да", "Нет", "Отмена"};
        int n = -1;
        if (WeightBlocks.isModified()) {
            n = JOptionPane.showOptionDialog(TrainAnalysis.this, "Закрыть окно с сохранением содержания?", "Вопрос?",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        }
        return n;
    }

    private void setTitle() {
        String title = "Анализ поездки ";
        if (trainAnalysisMain != null) title += " - второе окно";
//        setTitle(getTitle() + title);
        else {
            if (isTime) title += " - " + VIEW_TIME;
            else title += " - " + VIEW_COORDINATE;
        }
        this.setTitle(title);
    }

    /**
     * @return диалог выбора файла
     */
    private FileChooserRu makeFileChooser() {
        FileChooserRu fileChooser = new FileChooserRu();
        fileChooser.setPreferredSize(new Dimension(500, 480));
        Action details = fileChooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы поездок", "img", "dat", "bin"));
        fileChooser.setDialogTitle("Открыть файл поездки");
        fileChooser.setCurrentDirectory(new File(PREF.get("curDir", ".")));
        return fileChooser;
    }


    static class LocalAbstractAction extends AbstractAction {

        public LocalAbstractAction(String name, String nameIcon) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
            if (nameIcon != null) {
                URL url = getClass().getResource(nameIcon);
                assert url != null;
                putValue(Action.SMALL_ICON, new ImageIcon(url));
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    /**
     * действие - открыть
     */
    class OpenAction extends LocalAbstractAction {

        public OpenAction(String name, String nameIcon) {
            super(name, nameIcon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            isMaximize = false;
//            isResize = false;
            openFileTrain(isTime);
            if (trainAnalysisSlave != null) {
                trainAnalysisSlave.dispose();
                trainAnalysisSlave = null;
            }
            openSlaveAction.setEnabled(true);
            toFront();
        }
    }

    /**
     * действие - сцепка
     */
    class OpenSlaveAction extends LocalAbstractAction {

        public OpenSlaveAction(String name, String nameIcon) {
            super(name, nameIcon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            trainAnalysisSlave = new TrainAnalysis(owner, false, TrainAnalysis.this);
            if (trainAnalysisSlave.openFileTrain(isTime) == JFileChooser.APPROVE_OPTION ) {
                trainAnalysisSlave.setVisible(true);
                trainAnalysisSlave.getOpenItemSlave().setEnabled(false);
                trainAnalysisSlave.getOpenItem().setEnabled(false);
                trainAnalysisSlave.setMenuBarVisible(false);
               // trainAnalysisSlave.setToolBarVisible(false);
                openSlaveAction.setEnabled(false);
                //openItemSlave.setEnabled(false);
                trainAnalysisSlave.openAction.setEnabled(false);
                trainAnalysisSlave.openSlaveAction.setEnabled(false);
                trainAnalysisSlave.viewCoordinateAction.setEnabled(false);
                trainAnalysisSlave.viewTimeAction.setEnabled(false);

                ChartPanelArm chartPanelArmMain = trainAnalysisMain != null ? trainAnalysisMain.getChartPanelArm() : null;
                ChartPanelArm chartPanelArmSlave = trainAnalysisSlave != null ? trainAnalysisSlave.getChartPanelArm() : null;
                if (chartPanelArmSlave != null)
                    chartPanelArmSlave.setChartPanelArmMain(chartPanelArm);
                chartPanelArm.setChartPanelArmSlave(chartPanelArmSlave);
                // установка вертикального курсора на ведомый в соответсвии с ведущим
                double x = chartPanelArm.getChartArm().getXMarker();
                LocalDateTime dateTime = chartPanelArm.getChartDataset().getDateTime((int)x);
                if (chartPanelArmSlave != null) {
                    int n = chartPanelArmSlave.getChartDataset().get_xByDateTime(dateTime);
                    chartPanelArm.getChartArm().paintMarker(chartPanelArmMain, chartPanelArmSlave, chartPanelArm.getScrollBar(), n);
                }

                UtilsArmG.setWinBound(TrainAnalysis.this, trainAnalysisSlave);
                chartPanelArm.setWeightResize(TrainAnalysis.this.getHeight());
                chartPanelArm.setLabelResize(TrainAnalysis.this.getHeight());
                SwingUtilities.updateComponentTreeUI(chartPanelArm);
                SwingUtilities.updateComponentTreeUI(trainAnalysisSlave);
            }
            toFront();
        }
    }

    /**
     * действие выход
     */
    class ExitAction extends LocalAbstractAction {

        public ExitAction(String name, String nameIcon) {
            super(name, nameIcon);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
   //         closeDialog();
            dispatchEvent(new WindowEvent( TrainAnalysis.this, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * действие - минимизация окна
     */
    class MinimizeAction extends LocalAbstractAction {
        public MinimizeAction(String name, String nameIcon) {
            super(name, nameIcon);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            TrainAnalysis.this.owner.setState(Frame.ICONIFIED);
        }
    }

    /**
     * действие - максимизация окна
     */
    class MaximizeAction extends LocalAbstractAction {
        public MaximizeAction(String name, String nameIcon) {
            super(name, nameIcon);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            setMaximizeWin();
        }
    }

    /**
     * действие - нормализация окна
     */
    class NormalizeAction extends LocalAbstractAction {
        public NormalizeAction(String name, String nameIcon) {
            super(name, nameIcon);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            setNormalizeWin();
        }
    }

    /**
     * максимизация окна
     */
    private void setMaximizeWin() {
        isMaximize = true;
        if (normalizeBtn != null) normalizeBtn.setVisible(true);
        if (maximizeBtn != null) maximizeBtn.setVisible(false);
        UtilsArmG.saveWinBound(TrainAnalysis.this, PREF);
        UtilsArmG.setWinBoundMax(TrainAnalysis.this);
        if (componentResizer != null) componentResizer.deregisterComponent(this);
     //   if (componentMover != null) componentMover.deregisterComponent(menuBar, toolBar);
    }

    /**
     * нормализация окна
     */
    private void setNormalizeWin() {
        isMaximize = false;
        if (normalizeBtn != null) normalizeBtn.setVisible(false);
        if (maximizeBtn != null) maximizeBtn.setVisible(true);
        UtilsArmG.setWinBound(TrainAnalysis.this, PREF);
        if (componentResizer != null) componentResizer.registerComponent(this);
     //   if (componentMover != null) componentMover.registerComponent(menuBar, toolBar);
    }

    /**
     * действие - развертка по координате
     */
    class ViewCoordinateAction extends LocalAbstractAction {

        public ViewCoordinateAction(String name, String nameIcon) {
            super(name, nameIcon);
           // this.setEnabled(isTime);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            remakeSaveMarker(isTime = false, true, 1);
            viewTimeBtn.setVisible(true);
            viewCoordinateBtn.setVisible(false);
            itemCoordinate.setSelected(true);
            setTitle();
        }
    }

    /**
     * действие - развертка по времени
     */
    class ViewTimeAction extends  LocalAbstractAction {

        public ViewTimeAction(String name, String nameIcon) {
            super(name, nameIcon);
        //    this.setEnabled(!isTime);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            remakeSaveMarker(isTime = true, true, 1);
            viewTimeBtn.setVisible(false);
            viewCoordinateBtn.setVisible(true);
            itemTime.setSelected(true);
            setTitle();
        }
    }

    /**
     * действие - вид по умолчанию
     */
    class ViewDefAction extends  LocalAbstractAction {

        public ViewDefAction(String name, String nameIcon) {
            super(name, nameIcon);
           // this.setEnabled(false);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
          //  WeightBlocks.setModified(false);
            chartPanelArm.getChartArm().setViewLines(true);
           // viewDefAction.setEnabled(false);
            chartPanelArm.repaint();
        }
    }

    /**
     * действие - показать окно информации о поездах
     */
    class ShowInfoTrainsAction extends LocalAbstractAction {

        public ShowInfoTrainsAction(String name, String nameIcon) {
            super(name, nameIcon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (infoTrainsDialog == null)
                infoTrainsDialog = new InfoTrainsDialog(TrainAnalysis.this, chartPanelArm);
            infoTrainsDialog.setVisible(true);
        }
    }

    /**
     * действие - помощь
     */
    class HelpAction extends LocalAbstractAction {

        public HelpAction(String name, String nameIcon) {
            super(name, nameIcon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            HelpDialog.show(owner);
        }
    }

    /**
     * действие - о программе
     */
    class AboutAction extends LocalAbstractAction {

        public AboutAction(String name, String nameIcon) {
            super(name, nameIcon);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDialog.showAboutDialog(TrainAnalysis.this.owner);
        }
    }


    /**
     * @return меню файл
     */
    private JMenu makeFileMenu() {
        JMenu menu = new JMenu("Файл");
        openItem = new JMenuItem(openAction);
        openItemSlave = new JMenuItem(openSlaveAction);
        menu.add(openItem);
        menu.add(openItemSlave);
        menu.addSeparator();
        menu.add(new JMenuItem(exitAction));
        return menu;
    }

    /**
     * @return - меню вид
     */
    private JMenu makeViewMenu() {
        JMenu menu = new JMenu("Вид");
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
          //      viewDefAction.setEnabled(WeightBlocks.isModified());
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        JMenu scan_menu = new JMenu("Развертка");
        itemCoordinate = new JRadioButtonMenuItem(viewCoordinateAction);
        itemTime = new JRadioButtonMenuItem(viewTimeAction);
        defViewItem = new JMenuItem(viewDefAction);
        ButtonGroup group = new ButtonGroup();
        group.add(itemCoordinate);
        group.add(itemTime);
        itemTime.setSelected(isTime);
        scan_menu.add(itemCoordinate);
        scan_menu.add(itemTime);
        menu.add(scan_menu);
        //defViewItem = new JMenuItem();
        menu.add(defViewItem);
        return menu;
    }

    /**
     * @return меню отчеты
     */
    private JMenu makeRepMenu() {
        JMenu menu = new JMenu("Отчеты");
        showInfoTrainsItem = new JMenuItem(showInfoTrainsAction);
        menu.add(showInfoTrainsAction);
        return menu;
    }

    /**
     * @return меню помощь
     */
    private JMenu makeHelpMenu() {
        JMenu menu = new JMenu("Справка");
        JMenuItem noteMenu = new JMenuItem(helpAction);
        noteMenu.setAccelerator(KeyStroke.getKeyStroke("F1"));
        menu.add(noteMenu);
        menu.addSeparator();
        JMenuItem aboutMenu = new JMenuItem(aboutAction);
        menu.add(aboutMenu);
        return menu;
    }

    /**
     * панель меню
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.add(makeFileMenu());
        menuBar.add(makeViewMenu());
        menuBar.add(makeRepMenu());
        menuBar.add(makeHelpMenu());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(minimizeBtn);
        menuBar.add(maximizeBtn);
        menuBar.add(normalizeBtn);
        menuBar.add(exitBtn);
    }

    /**
     * панель кнопок
     */
    private void makeToolBar() {
        toolBar = new JToolBar();
        toolBar.addSeparator();
        addButton(openAction, true);
        addButton(openSlaveAction, true);
        toolBar.addSeparator();
        viewCoordinateBtn = addButton(viewCoordinateAction, true);
        viewTimeBtn = addButton(viewTimeAction, true);
        toolBar.addSeparator();
        addButton(showInfoTrainsAction, true);
        toolBar.addSeparator();
        addButton(viewDefAction, true);

        toolBar.add(Box.createHorizontalGlue());
        minimizeBtn = addButton(minimizeAction, false);
        maximizeBtn = addButton(maximizeAction, false);
        normalizeBtn = addButton(normalizeAction, false);
        exitBtn = addButton(exitAction, false);

        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        for (Component c : toolBar.getComponents()) {
            c.setFocusable(false);
        }
    }

    /**
     *  добавляем button на toolBar
     * @param action - действие
     * @param isBorder - есть рамка
     * @return button
     */
    private JButton addButton(LocalAbstractAction action, boolean isBorder) {
        JButton btn = toolBar.add(action);
        btn.setBorderPainted(isBorder);
        btn.setContentAreaFilled(isBorder);
        return btn;
    }

    /**
     * создаем или добавляем (заменяем на новый) компонент ChartPanelArm
     * @param chartDataset -
     * @param hexTablePan -
     */
    private void addChartPanelArm(ChartDataset chartDataset, JPanel hexTablePan) {
        int curVerticalSplitPos = -1; // текущая позиция сплиттера для chartPanelArm
        if (chartPanelArm != null) {
            curVerticalSplitPos = chartPanelArm.getVerticalSplitPos();
            this.remove(chartPanelArm);
        }
        chartPanelArm = new ChartPanelArm(chartDataset, hexTablePan);
        // восстановление текущей позиции сплиттера для нового chartPanelArm
        chartPanelArm.setVerticalSplitPos(curVerticalSplitPos);
        this.add(chartPanelArm, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
        chartPanelArm.initHeight();
    }

    /**
     * пересоздать по координате или времени с сохранением положения курсора
     * @param isTime- если true то по времени, если false то по координате
     * @param isAnimation - аоказывать окно анимации
     * @param precision - точность 1... .
     */
    private void remakeSaveMarker(boolean isTime, boolean isAnimation, int precision) {
        infoTrainsDialog = null;
        final int N = 10;
        ChartPanelArm chartPanelArmMain = trainAnalysisMain != null ? trainAnalysisMain.getChartPanelArm() : null;
        ChartPanelArm chartPanelArmSlave = trainAnalysisSlave != null ? trainAnalysisSlave.getChartPanelArm() : null;
        double x = getChartPanelArm().getChartArm().getXMarker();
        LocalDateTime dateTime = chartPanelArm.getChartDataset().getDateTime((int)x);
        double lower = chartPanelArm.getChartArm().getDomainAxis().getRange().getLowerBound();
        double upper = chartPanelArm.getChartArm().getDomainAxis().getRange().getUpperBound();
        double duration = !isTime ? (upper - lower) * N : (upper - lower) / N;
        double duration_slave = Double.NaN;
        if (chartPanelArmSlave != null) {
            double lower_slave = chartPanelArmSlave.getChartArm().getDomainAxis().getRange().getLowerBound();;
            double upper_slave = chartPanelArmSlave.getChartArm().getDomainAxis().getRange().getUpperBound();
            duration_slave = !isTime ? (upper_slave - lower_slave) * N : (upper_slave - lower_slave) / N;
        }
//        int k = chartPanelArm.getChartDataset().getArrBlock32().size() - 1;
//        int oldPosScroll = chartPanelArm.getScrollBar().getValue();
//        int oldSecond = chartPanelArm.getChartDataset().getArrBlock32().get(k).getSecond();

        remakeChartDataset(isTime, isAnimation, precision);
        int n = chartPanelArm.getChartDataset().get_xByDateTime(dateTime);

        if (trainAnalysisSlave != null) {
            trainAnalysisSlave.remakeChartDataset(isTime, isAnimation, precision);
            chartPanelArmSlave = trainAnalysisSlave.getChartPanelArm();
            if (chartPanelArmSlave != null)
                chartPanelArmSlave.setChartPanelArmMain(chartPanelArm);
            chartPanelArm.setChartPanelArmSlave(chartPanelArmSlave);
        }
        chartPanelArm.getChartArm().doZoomByCursor(chartPanelArm.getScrollBar(), n, duration);
        if (chartPanelArmSlave != null)
            chartPanelArmSlave.getChartArm().doZoomByCursor(chartPanelArmSlave.getScrollBar(), n, duration_slave);

        chartPanelArm.getChartArm().paintMarker(chartPanelArmMain, chartPanelArmSlave, chartPanelArm.getScrollBar(), n);
        toFront();
    }

    private void hexTabToChartPan(ChartDataset chartDataset) {
        final HexTab hexTab = new HexTab.Builder(arrBlock32).build();
        hexTab.setVisible(false);
        addChartPanelArm(chartDataset, hexTab);
    }

    /**
     * пересоздать по координате или времени
     * @param isTime - если true то по времени, если false то по координате
     * @param isAnimation - показывать окно анимации
     * @param precision - точность 1... .
     */
    private void remakeChartDataset(boolean isTime, boolean isAnimation, int precision) {
        if (arrBlock32 == null) return;
        JDialog owner = isAnimation ? this : null;
        try {
            ChartDataset chartDataset = LoadAnimate.execMakeArrBl32(owner, arrBlock32, isShift, 0, arrBlock32.size() - 1, isTime, precision);
            hexTabToChartPan(chartDataset);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * открывает файл поездки по имени файла
     */
    public void openFileTrainByName(String fileName, boolean isTime) {
        String mess_error = "Неизвестный формат поездки!";
//        if (result == JFileChooser.APPROVE_OPTION) {
            infoTrainsDialog = null;
            try {
                UtilsArmG.checkSizeFile(fileName);
                ChartDataset chartDataset = LoadAnimate.execMakeArrBl32(this, fileName, isShift, this.isTime = isTime, 1);
                arrBlock32 = chartDataset.getArrBlock32();
                hexTabToChartPan(chartDataset);
                if (chartDataset.getArrTrains().size() == 0)
                    JOptionPane.showMessageDialog(
                            this, "Поездки не сформированы!",
                            TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
            }
            catch (ExecutionException e1) {
             //   throw new FileNotFoundException("не удалось создать " + dir.toString());
                e1.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, e1.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
  //              result = JFileChooser.ERROR_OPTION;
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, e2.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
    //            result = JFileChooser.ERROR_OPTION;
            }
            catch (IOException e3) {
                e3.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, e3.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
                //            result = JFileChooser.ERROR_OPTION;
            }
            PREF.put("curDir", fileName);
//        }
        // деактивируем действие - "отчеты" если поезда не определены (нет посылок УСАВП)
        showInfoTrainsAction.setEnabled(chartPanelArm != null && chartPanelArm.getChartDataset().getArrTrains().get(0).getDateTimeStart() != null);
//        return result;
    }

    /**
     * открывает файл поездки с помощью JFileChooser
     * @return - JFileChooser.*_OPTION
     */
    public int openFileTrain(boolean isTime) {
        String mess_error = "Неизвестный формат поездки!\nВозможно данный тип локомотива не поддерживается!";
        int result = fileChooser.showOpenDialog(TrainAnalysis.this);
        if (result == JFileChooser.APPROVE_OPTION) {
          infoTrainsDialog = null;
          fileName = fileChooser.getSelectedFile().getAbsolutePath();
          try {
              UtilsArmG.checkSizeFile(fileName);
              ChartDataset chartDataset = LoadAnimate.execMakeArrBl32(this, fileName, isShift, this.isTime = isTime, 1);
              arrBlock32 = chartDataset.getArrBlock32();
              hexTabToChartPan(chartDataset);

              if (chartDataset.getArrTrains().size() == 0)
//                throw (new IOException(mess_error));
                  JOptionPane.showMessageDialog(
                         this, "Поездки не сформированы!",
                          TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
          }
//          catch (IOException e0) {
//              e0.printStackTrace();
//              JOptionPane.showMessageDialog(
//                      this, e0.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
//              result = JFileChooser.ERROR_OPTION;
//          }
          catch (ExecutionException e1) {
              e1.printStackTrace();
              JOptionPane.showMessageDialog(
                      this, mess_error, TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
              result = JFileChooser.ERROR_OPTION;
          }
          catch (InterruptedException e2) {
              e2.printStackTrace();
              JOptionPane.showMessageDialog(
                      this, e2.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
              result = JFileChooser.ERROR_OPTION;
          }
          catch (IOException e2) {
              e2.printStackTrace();
              JOptionPane.showMessageDialog(
                      this, e2.getMessage(), TITLE_ARM, JOptionPane.INFORMATION_MESSAGE);
              result = JFileChooser.ERROR_OPTION;
          }
          PREF.put("curDir", fileName);
        }
        // деактивируем действие - "отчеты" если поезда не определены (нет посылок УСАВП)
        showInfoTrainsAction.setEnabled(chartPanelArm != null && chartPanelArm.getChartDataset().getArrTrains().get(0).getDateTimeStart() != null);
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public ChartPanelArm getChartPanelArm() {
        return  chartPanelArm;
    }

    public JMenuItem getOpenItemSlave() {
        return openItemSlave;
    }

    public JMenuItem getOpenItem() {
        return openItem;
    }

    public TrainAnalysis getTrainAnalysisMain() {
        return trainAnalysisMain;
    }

    public TrainAnalysis getTrainAnalysisSlave() {
        return trainAnalysisSlave;
    }

    public void setSecondNull() {
        trainAnalysisSlave = null;
    }

    public void setMenuBarVisible(boolean isVisible) {
        menuBar.setVisible(isVisible);
    }

    public void setToolBarVisible(boolean isVisible) {
        toolBar.setVisible(isVisible);
    }
}
