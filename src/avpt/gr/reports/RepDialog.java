package avpt.gr.reports;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.LocalDateTime;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.prefs.Preferences;

public class RepDialog extends JDialog {
    private static final Preferences node_rep_dialog = UtilsArmG.getNode("rep_dialog");
    private Calendar calendarBegin = new GregorianCalendar(2000, Calendar.JANUARY, 1);;
    private Calendar calendarEnd  = new GregorianCalendar();
    private RoadTchPan roadTchPan;
    private JList listRep;

    public RepDialog(JFrame owner) {
        super (owner, "Отчеты", true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                super.windowDeactivated(e);
                node_rep_dialog.putInt("index_road", roadTchPan.getIndexRoad());
                node_rep_dialog.putInt("num_tch", roadTchPan.getNumTch());
            }

            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                try {
                    roadTchPan.fillComboLocomotives();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel_north = new JPanel();
        JPanel panel_south = new JPanel();
        try {
            roadTchPan = new RoadTchPan(
                    node_rep_dialog.getInt("index_road", 0), node_rep_dialog.getInt("num_tch", 0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        panel_north.add(roadTchPan);
        makeDateSpinner(panel_north);
        makeListReports();
        roadTchPan.setComponentEnabled(listRep);
        addBtnClose(panel_south);
        add(panel_north, BorderLayout.NORTH);
        add(panel_south, BorderLayout.SOUTH);
        pack();
        listRep.requestFocus();
        setLocationRelativeTo(owner);
    }

    private void addBtnClose(JComponent component) {
        JButton btn = new JButton("Закрыть");
        Icon icon = new ImageIcon(getClass().getResource("/avpt/gr/dialogs/images/close.png"));
        btn.setIcon(icon);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RepDialog.this.setVisible(false);
            }
        });
        component.add(btn);
    }

    private void doReport(int index) {
        if (!listRep.isEnabled()) return;
        RepDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ExcelReports.KindReport kind = null;
        try {
            switch (index) {
                case 0:
                    kind = ExcelReports.KindReport.trains_query;
                    break;
                case 1:
                    kind = ExcelReports.KindReport.diagn_vsc_query;
                    break;
            }
            new ExcelReports(getLocalDateTime(calendarBegin), getLocalDateTime(calendarEnd), kind, roadTchPan);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            RepDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void makeDateSpinner(JComponent component) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        final JSpinner spinnerBegin = new JSpinner(new SpinnerDateModel());
        spinnerBegin.setFont(font);
        final JSpinner spinnerEnd = new JSpinner(new SpinnerDateModel());
        spinnerEnd.setFont(font);

        spinnerBegin.setValue(calendarBegin.getTime());
        spinnerEnd.setValue(calendarEnd.getTime());

        spinnerBegin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calendarBegin.setTime((Date)spinnerBegin.getValue());
            }
        });

        spinnerEnd.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calendarEnd.setTime((Date)spinnerEnd.getValue());
            }
        });

        component.add(spinnerBegin);
        component.add(new JLabel(" - "));
        component.add(spinnerEnd);
    }

    private void makeListReports() {
        final String[] reports = {"Поездки", "Диагностика ВСЦ"/*, "Тех. состояние", "Диагностика", "Диагностика 75"*/};
        listRep = new JList(reports);
        listRep.setSelectedIndex(0);
        listRep.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                //super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doReport(listRep.getSelectedIndex());
                }
            }

//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    doReport(listRep.getSelectedIndex());
//                }
//            }
        });
        listRep.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    doReport(listRep.getSelectedIndex());
                }
            }
        });
        add(listRep);
    }

    private LocalDateTime getLocalDateTime(Calendar calendar) {
        LocalDateTime ldt = null;
        if (calendar != null) {
            ldt = LocalDateTime.of(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
        }
        return ldt;
    }


}
