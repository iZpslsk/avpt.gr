package avpt.gr.components;

import avpt.gr.dialogs.InfoTrainsDialog;
import avpt.gr.graph.ChartPanelArm;
import avpt.gr.train.ArrTrains;
import avpt.gr.train.Train;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static avpt.gr.common.UtilsArmG.formatTime;
import static avpt.gr.dialogs.InfoTrainsDialog.*;
import static avpt.gr.train.Train.*;

public class TrainsPan extends JPanel {

    private final JToggleButton[] buttons;
    private final JToggleButton btn_whole;
    private final InfoTrainPan[] infoTrainPans;
    private JScrollPane scrollInfoPane;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JDialog owner;
    private final ChartPanelArm chartPanelArm;
    private final ArrTrains trains;
    private int curIndex;
    private JButton btnBack;
    private JButton btnPrint;
    private JButton btnVirtCoup;
    private JButton btnConsumpEnergy;
    private JButton btnClose;

    public TrainsPan(final JDialog owner, ChartPanelArm chartPanelArm) {
        this.owner = owner;
        this.chartPanelArm = chartPanelArm;

        initToolButtons(((InfoTrainsDialog)owner).getToolBar());
     //   if (btnBack != null) System.out.println(btnBack.getName());

        trains = chartPanelArm.getChartDataset().getArrTrains();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 3, 2, 3));
        JPanel layout = new JPanel(new GridBagLayout());
        layout.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel btnPanel = new JPanel(new GridLayout(trains.size() + 1, 1, 10, 5));

        btn_whole = new JToggleButton("Все поездки");
        btn_whole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInfoTrain(0);
            }
        });
        btn_whole.setSelected(true);

        buttonGroup.add(btn_whole);
        buttons = new JToggleButton[trains.size()];
        infoTrainPans = new InfoTrainPan[trains.size() + 1];    // 0-й индекс - в целом

        btnPanel.add(btn_whole);
        addButtons(btnPanel, trains);

        layout.add(btnPanel);
        add(layout, BorderLayout.NORTH);
        setInfoTrain(0);
        setKeyEventMove(this, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * создание (если не существует) и отображение панели
     * @param index_pan - индекс панели; 0 - все, 1 - 1-й поезд, 2 - 2-й поезд...
     */
    private void setInfoTrain(int index_pan) {
        btnBack.setEnabled(index_pan > 0);
        btnPrint.setEnabled(index_pan > 0);
        int typeLoc = index_pan > 0 ? trains.get(index_pan - 1).getTypeLoc() : trains.get(0).getTypeLoc();  // тип локомотива для всех поездок берем из 1-й поездки
        btnVirtCoup.setEnabled(typeLoc == S5K || typeLoc == S5K_2 || typeLoc == S6);
        btnConsumpEnergy.setEnabled(typeLoc != S6);
       // btnConsumpEnergy.setEnabled(index_pan > 0);

        curIndex = index_pan;
        if (infoTrainPans[index_pan] == null) {
            infoTrainPans[index_pan] = index_pan > 0 ?
                    new InfoTrainPan(chartPanelArm, index_pan - 1) : new InfoTrainPan(chartPanelArm, -1);
            setKeyEventMove(infoTrainPans[index_pan], JComponent.WHEN_FOCUSED);
        }
        if (scrollInfoPane != null) owner.remove(scrollInfoPane);
        infoTrainPans[index_pan].setActionBtnBack(btnBack);
        infoTrainPans[index_pan].setActionBtnPrint(btnPrint);
        infoTrainPans[index_pan].setActionBtnVirtCoup(btnVirtCoup);
        infoTrainPans[index_pan].setActionBtnConsumpEnergy(btnConsumpEnergy);
        infoTrainPans[index_pan].setActionBtnClose(btnClose);
        scrollInfoPane = new JScrollPane(infoTrainPans[index_pan]);
        scrollInfoPane.setOpaque(false);
        scrollInfoPane.getViewport().setOpaque(false);
        owner.add(scrollInfoPane);
        SwingUtilities.updateComponentTreeUI(owner);
    }

    /**
     * добавить кнопку в массив buttons[] для поезда; 0 - 1-й, 1 - 2-й, 2 - 3-й
     * @param btnPanel -
     * @param trains -
     */
    private void addButtons(Container btnPanel, ArrTrains trains) {
        for (int i = 0; i < trains.size(); i++) {
            Train train = trains.get(i);
            buttons[i] = new JToggleButton((i + 1) + ") " + train.getDateTimeStart().format(formatTime) + "  №" + train.getNumTrain());
            buttons[i].addActionListener(new ListenerBtnTrain(i));
            buttons[i].setHorizontalAlignment(SwingConstants.LEFT);
            buttonGroup.add(buttons[i]);
            btnPanel.add(buttons[i]);
        }
    }

    /**
     * действие клавиши выбора поезда
     */
    private class ListenerBtnTrain implements ActionListener {
        private final int index;

        public ListenerBtnTrain(int index) {
           this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setInfoTrain(index + 1);
        }
    }

    /**
     * установка горячих клавиш (up, down) перемещения вертикольного курсора
     */
    @SuppressWarnings({ "unused", "serial" })
    public void setKeyEventMove(JComponent component, int condition) {

        InputMap imap = component.getInputMap(condition);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        ActionMap amap = component.getActionMap();

        amap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (curIndex <= 1) {
                    btn_whole.setSelected(true);
                    setInfoTrain(0);
                }
                else {
                    buttons[curIndex - 2].setSelected(true);
                    setInfoTrain(curIndex - 1);
                }
            }
        });

        amap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (curIndex < infoTrainPans.length - 1) {
                    buttons[curIndex].setSelected(true);
                    setInfoTrain(curIndex + 1);
                }
            }
        });
    }

    /**
     * инициализация кнопок для toolbar
     * @param toolBar -
     */
    private void initToolButtons(JToolBar toolBar) {
        for (Component c : toolBar.getComponents()) {
            if (c instanceof JButton) {
                if (c.getName().equals(BTN_BACK)) btnBack = (JButton) c;
                if (c.getName().equals(BTN_PRINT)) btnPrint = (JButton) c;
                if (c.getName().equals(BTN_CLOSE)) btnClose = (JButton) c;
                if (c.getName().equals(BTN_VIRT_COUP)) btnVirtCoup = (JButton) c;
                if (c.getName().equals(BTN_CONSUMP_ENERGY)) btnConsumpEnergy = (JButton) c;
            }
        };
    }
}
