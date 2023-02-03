package avpt.gr.dialogs;

import avpt.gr.common.UtilsArmG;
import avpt.gr.components.TrainsPan;
import avpt.gr.graph.ChartPanelArm;
import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static avpt.gr.dialogs.TrainAnalysis.PREF;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class InfoTrainsDialog extends JDialog {

    public static final String BTN_BACK = "btnBack";
    public static final String BTN_PRINT = "btnPrint";
    public static final String BTN_VIRT_COUP = "btnVirtCoup";
    public static final String BTN_CONSUMP_ENERGY = "btnConsumpEn";
    public static final String BTN_CLOSE = "btnClose";
    private JToolBar toolBar;

    public InfoTrainsDialog(JDialog owner, ChartPanelArm chartPanelArm) {
        super(owner, "Отчеты", true);
        getContentPane().setBackground(new Color(0xFAF3A4));
        UtilsArmG.setWinBoundRep(this, PREF);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UtilsArmG.saveWinBoundRep(InfoTrainsDialog.this, PREF);
            }
        });
        makeToolBar();
        makePaneScroll(new TrainsPan(this, chartPanelArm));
    }

    /**
     * панель кнопок
     */
    private void makeToolBar() {
        toolBar = new JToolBar();
        // button back
        JButton btn = new JButton();
        btn.setName(BTN_BACK);
        btn.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/menu/arrow_left_32.png")));
        btn.setToolTipText("назад");
        toolBar.add(btn);
        // button print
        toolBar.addSeparator();
        btn = new JButton();
        btn.setName(BTN_PRINT);
        btn.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/menu/excel_32.png")));
        btn.setToolTipText("печать текущего отчета");
        toolBar.add(btn);

        btn = new JButton();
        btn.setName(BTN_VIRT_COUP);
        btn.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/menu/tab_virtcoup_30.png")));
        btn.setToolTipText("всц - выгрузка");
        toolBar.add(btn);

        btn = new JButton();
        btn.setName(BTN_CONSUMP_ENERGY);
        btn.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/menu/cntenergy_32.png")));
        btn.setToolTipText("расход энергии - выгрузка");
        toolBar.add(btn);

        toolBar.addSeparator();
        btn = new JButton();
        btn.setName(BTN_CLOSE);
        btn.setIcon(new ImageIcon(getClass().getResource("/avpt/gr/images/menu/close_30.png")));
        btn.setToolTipText("закрыть");
        toolBar.add(btn);

        for (Component c : toolBar.getComponents()) c.setFocusable(false);
        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * клавиши поездов
     * @param trainsPan -
     */
    private void makePaneScroll(TrainsPan trainsPan) {
        JScrollPane trainsPanScroll = new JScrollPane(trainsPan);
        trainsPanScroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        trainsPanScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        trainsPanScroll.setOpaque(false);
        trainsPanScroll.getViewport().setOpaque(false);
        add(trainsPanScroll, BorderLayout.WEST);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }
}
