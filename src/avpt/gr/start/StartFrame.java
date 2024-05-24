package avpt.gr.start;

import avpt.gr.common.GBC;
import avpt.gr.dialogs.AboutDialog;
import avpt.gr.dialogs.TrainAnalysis;
import avpt.gr.dialogs.packing.PackDialog;
import avpt.gr.reports.RepDialog;
import avpt.gr.sqlite_base.CreateInsertSQLite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static avpt.gr.dialogs.AboutDialog.showAboutDialog;

@SuppressWarnings({"WeakerAccess", "unused"})
public class StartFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 520;
    public static final int DEFAULT_HEIGHT = 460;
    public static final String TITLE_ARM = "АРМ - грузовое движение";
    private static final String ARM_TITLE = "<html><h2 align=\"center\"><font color=\"white\">" +
            "Анализатор данных движения<br>грузовых электровозов</font></h2></htm1>";
    public static int sizeFont = 12;    // размер шрифтов
    private static final Color BACKGROUND_PANEL = new Color(46, 61, 141, 223);
    private static final Color BACKGROUND_PANEL_LOGO = new Color(255, 255, 255);
    private static final Color BACKGROUND_CENTER_PAN = new Color(255, 255, 255);
    private static int nextButton = 0;
    private final JPanel centralPanel;
    private static AboutDialog aboutDialog;

    public StartFrame() {
        // включить управление меню с клавиатуры
        setControlByKeyboard();
        // размер окна
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        // окно в центре экрана
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
//        if (screenSize.width < 1990 ) {
//            sizeFont = 12;
//            HexTablePan.hexWidthNumBlk = 70;
//            HexTablePan.hexWidthIdBlk = 55;
//            HexTablePan.hexWidthSecond = 70;
//            HexTablePan.hexWidthCell =30;
//        } else if (screenSize.width < 3500) {
//            sizeFont = 16;
//            HexTablePan.hexWidthNumBlk = 100;
//            HexTablePan.hexWidthIdBlk = 70;
//            HexTablePan.hexWidthSecond = 100;
//            HexTablePan.hexWidthCell =40;
//        } else {
//            sizeFont = 18;
//            HexTablePan.hexWidthNumBlk = 120;
//            HexTablePan.hexWidthIdBlk = 85;
//            HexTablePan.hexWidthSecond = 120;
//            HexTablePan.hexWidthCell = 50;
//        }

        setLocation(screenSize.width / 2 - DEFAULT_WIDTH / 2, screenSize.height / 2 - DEFAULT_HEIGHT / 2);
        // иконки окна и логотипа
        URL url = getClass().getResource("/avpt/gr/images/armlogo.png");
        assert url != null;
        setIconImage(new ImageIcon(url).getImage());
        JLabel labelLogo = new JLabel();
        JPanel panelLogo = new JPanel();
        url = getClass().getResource("/avpt/gr/images/logo_avp.png");
        assert url != null;
        labelLogo.setIcon(new ImageIcon(url));
        labelLogo.setVerticalAlignment(JLabel.BOTTOM);
        panelLogo.setBackground(BACKGROUND_PANEL_LOGO);
        panelLogo.add(labelLogo);
        // панели
        JPanel topPanel = new JPanel();
        topPanel.setBackground(BACKGROUND_PANEL);
        topPanel.add(new JLabel(ARM_TITLE, JLabel.CENTER));
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BACKGROUND_PANEL);
        centralPanel = new JPanel();
        centralPanel.setBackground(BACKGROUND_CENTER_PAN);
        centralPanel.setLayout(new GridBagLayout());
        // компановка панелей
        setLayout(new GridBagLayout());
        add(panelLogo, new GBC(0, 0).setFill(GBC.BOTH).setWeight(0, 0));
        add(topPanel, new GBC(1, 0).setFill(GBC.BOTH).setWeight(100, 0));
        add(leftPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(0, 100));
        add(centralPanel, new GBC(1, 1).setFill(GBC.BOTH).setWeight(100, 100));
        // кнопки
        addButton("Анализ файла", true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
                                    TrainAnalysis trainAnalysis = new TrainAnalysis(StartFrame.this, false, null);
                                    if (trainAnalysis.openFileTrain(StartFrame.this, true) == JFileChooser.APPROVE_OPTION ) {
                                        trainAnalysis.setVisible(true);
                                        SwingUtilities.updateComponentTreeUI(trainAnalysis);
                                    }
                            }
                        });
                    }
                });
        addButton("Пакетная обработка", CreateInsertSQLite.getConnection() != null,
                new ActionListener() {
                    private PackDialog packDialog;
                    public void actionPerformed(ActionEvent event) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                packDialog = new PackDialog(StartFrame.this);
                                packDialog.setVisible(true);
                            }
                        });
                    }
                });
        addButton("Отчеты", CreateInsertSQLite.getConnection() != null,
                new ActionListener() {
                    private RepDialog repDialog;
                    public void actionPerformed(ActionEvent event) {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (repDialog == null) {
                                    repDialog = new RepDialog(StartFrame.this);
                                }
                                repDialog.setVisible(true);
                            }
                        });
                    }
                });
        addButton("О программе", true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                    	EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
                                showAboutDialog(StartFrame.this);
							}
                    	});
                    }
                });
        addButton("Выход", true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        System.exit(0);
                    }
                });
    }

    /**
     * добавить пункт меню (кнопку)
     * @param title - текст
     * @param isEnabled - активна / неактивна
     * @param listener - событие onClick
     */
    private void addButton(final String title, final boolean isEnabled, ActionListener listener) {

        final String tag_open;
        if (isEnabled)
            tag_open = "<html><h2><font color=\"#000099\">";
        else
            tag_open = "<html><h2><font color=\"#A9A9A9\">";
        final String tag_close = "</font></h2></html>";
        final JButton button = new JButton(tag_open + title + tag_close);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(BACKGROUND_CENTER_PAN);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(DEFAULT_WIDTH / 2  , 30));
        button.setFocusPainted(true);          // откл прорисовка рмки активной кнопки
        button.setContentAreaFilled(false);     // откл визуальный отклик на нажатие
        button.setEnabled(isEnabled);
        button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
                if (isEnabled)
                    button.setText("<html><h2><font color=\"#A40000\">" + title + "</font></h2></html>");
            }
            public void mouseExited(MouseEvent event) {
                button.setText(tag_open + title + tag_close);
            }
        });
        centralPanel.add(button,  new GBC(0, nextButton++).setFill(GridBagConstraints.CENTER).
                setWeight(100, 0).setInsets(10, 0, 10, 0));
    }

    /**
     * включить управление меню с клавиатуры
     */
    private void setControlByKeyboard() {
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE); // focus enter
        // KeyEvent.VK_DOWN
        Set<AWTKeyStroke> focusTraversalKeysDown = new HashSet<AWTKeyStroke>(this.getFocusTraversalKeys(0));
        focusTraversalKeysDown.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED));
        setFocusTraversalKeys(0, focusTraversalKeysDown);
        // KeyEvent.VK_RIGHT
        Set<AWTKeyStroke> focusTraversalKeysRight = new HashSet<AWTKeyStroke>(this.getFocusTraversalKeys(0));
        focusTraversalKeysRight.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED));
        setFocusTraversalKeys(0, focusTraversalKeysRight);
        // KeyEvent.VK_UP
        Set<AWTKeyStroke> focusTraversalKeysUp = new HashSet<AWTKeyStroke>(this.getFocusTraversalKeys(1));
        focusTraversalKeysUp.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED));
        setFocusTraversalKeys(1, focusTraversalKeysUp);
        // KeyEvent.VK_LEFT
        Set<AWTKeyStroke> focusTraversalKeysLeft = new HashSet<AWTKeyStroke>(this.getFocusTraversalKeys(1));
        focusTraversalKeysLeft.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED));
        setFocusTraversalKeys(1, focusTraversalKeysLeft);
    }

    public void openFromComStr(String fileName) throws ExecutionException, InterruptedException {
        TrainAnalysis trainAnalysis = new TrainAnalysis(StartFrame.this, true, null);
        trainAnalysis.openFileTrainByName(fileName, true);
        trainAnalysis.setVisible(true);
    }
}
