package avpt.gr.reports;

import avpt.gr.sqlite_base.CreateInsertSQLite;
import avpt.gr.train.Train;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoadTchPan extends JPanel {

    private JComboBox comboRoads = new JComboBox();
    private JComboBox comboLocomotives = new JComboBox();
    private JSpinner spinTch;
    private ArrayList<CreateInsertSQLite.ItemRoad> roads;
    ArrayList<CreateInsertSQLite.ItemLocomotive>locomotives;
    private int indexRoad;
    private int indexLoc;
    private int typeLoc;
    private int codeAsoup;
    private int numLoc = -1;
    private int codeRoad;
    private JComponent component;

    public RoadTchPan(int indexRoad, int numTch) throws SQLException {
        makeComboRoads();
        makeSpinTch();
        comboRoads.setSelectedIndex(indexRoad);
        spinTch.setValue(numTch);
        makeComboLocomotives();
        setComboFnt(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(comboLocomotives);
        add(comboRoads);
        add(spinTch);
    }

    private void setComboFnt(Font font) {
        super.setFont(font);
        comboRoads.setFont(font);
        spinTch.setFont(font);
        comboLocomotives.setFont(font);
    }

    private void makeSpinTch() {
        final int width = 40;
        final int height = 20;
        int numTch = 0;
        SpinnerModel model = new SpinnerNumberModel(numTch, 0, 1000, 1);
        spinTch = new JSpinner(model);
        spinTch.setMinimumSize(new Dimension(width, height));
        spinTch.setPreferredSize(new Dimension(width, height));
        spinTch.setMaximumSize(new Dimension(width, height));
        spinTch.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    fillComboLocomotives();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void makeComboRoads() throws SQLException {
        final int width = 150;
        final int height = 20;
        comboRoads.setMinimumSize(new Dimension(width, height));
        comboRoads.setPreferredSize(new Dimension(width, height));
        comboRoads.setMaximumSize(new Dimension(width, height));
        roads = CreateInsertSQLite.getArrRoads();
        for (CreateInsertSQLite.ItemRoad item : roads) {
            comboRoads.addItem(item.getName());
        }
        comboRoads.setSelectedIndex(indexRoad);
        comboRoads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexRoad = comboRoads.getSelectedIndex();
                codeRoad = roads.get(indexRoad).getCode();
                if (indexRoad == 0)
                    spinTch.setValue(0);
                try {
                    fillComboLocomotives();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //     System.out.println(indexRoad + "-" + codeRoad);
            }
        });
    }

    public void fillComboLocomotives() throws SQLException {
        String str = (String) comboLocomotives.getItemAt(comboLocomotives.getSelectedIndex());
        comboLocomotives.removeAllItems();
        locomotives = CreateInsertSQLite.getArrLocomotives(codeRoad, (Integer) spinTch.getValue());
        comboLocomotives.addItem("Все локомотивы");
        for (CreateInsertSQLite.ItemLocomotive item : locomotives) {
            String txt = String.format("№%-5d%s", item.getNum(), Train.getNameTypeLoc(item.getCodeType(), item.getCodeAsoup()));
            comboLocomotives.addItem(txt);
        }
        if (comboLocomotives.getItemCount() < 2) {
            comboLocomotives.setEnabled(false);
            if (component != null) component.setEnabled(false);
        }
        else {
            comboLocomotives.setEnabled(true);
            if (component != null) component.setEnabled(true);
        }
        comboLocomotives.setSelectedItem(str);
        int index = comboLocomotives.getSelectedIndex();
        if (index == -1)
            comboLocomotives.setSelectedIndex(0);
    }

    private void makeComboLocomotives() throws SQLException {
        final int width = 150;
        final int height = 20;
        comboLocomotives.setMinimumSize(new Dimension(width, height));
        comboLocomotives.setPreferredSize(new Dimension(width, height));
        comboLocomotives.setMaximumSize(new Dimension(width, height));
        fillComboLocomotives();
        comboLocomotives.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexLoc = comboLocomotives.getSelectedIndex() - 1;
                if (indexLoc < 0 || indexLoc >= locomotives.size()) {
                    typeLoc = 0;
                    codeAsoup = 0;
                    numLoc = -1;
                }
                else {
                    typeLoc = locomotives.get(indexLoc).getCodeType();
                    codeAsoup = locomotives.get(indexLoc).getCodeAsoup();
                    numLoc = locomotives.get(indexLoc).getNum();
                }
            }
        });
    }

    public int getIndexRoad() {
        return indexRoad;
    }

    public int getCodeRoad() {
        return codeRoad;
    }

    public int getNumTch() {
        return (Integer) spinTch.getValue();
    }

    public void setNumTch(int val) {
        spinTch.setValue(val);
    }

    public int getTypeLoc() {
        return typeLoc;
    }

    public int getCodeAsoup() {
        return codeAsoup;
    }

    public int getNumLoc() {
        return numLoc;
    }

    public void setComponentEnabled(JComponent component) {
        if (comboLocomotives == null) return;
        this.component = component;

        if (comboLocomotives.getItemCount() < 2) {
            comboLocomotives.setEnabled(false);
            if (component != null) component.setEnabled(false);
        }
        else {
            comboLocomotives.setEnabled(true);
            if (component != null) component.setEnabled(true);
        }
    }

}
