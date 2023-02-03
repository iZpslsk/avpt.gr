package avpt.gr.dialogs.packing;

import avpt.gr.common.UtilsArmG;
import avpt.gr.components.HexTablePan;
import avpt.gr.dialogs.FileChooserRu;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.prefs.Preferences;

public class PackHandling extends JDialog {

    private static final Preferences node_pack = UtilsArmG.getNode("package_handling");

    private JTable table;
    private FileChooserRu fileChooser;
    private Collection<File> files;
    private TableModel tableModel;

    public PackHandling(JFrame owner) {

        super(owner, "Пакетная обработка", true);
        // расположение окна
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int left = node_pack.getInt("left", 0);
        int top = node_pack.getInt("top", 0);
        int width = node_pack.getInt("width", (int)screenSize.getWidth() - (int)screenSize.getWidth() / 25);
        int height = node_pack.getInt("height", (int)screenSize.getHeight() - (int)screenSize.getHeight() / 25);
        setBounds(left, top, width, height);

        // файл поездки
        fileChooser = new FileChooserRu();
        fileChooser.setDialogTitle("Выбор папки с поездками");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent event) {
                node_pack.putInt("left", getX());
                node_pack.putInt("top", getY());
                node_pack.putInt("width", getWidth());
                node_pack.putInt("height", getHeight());
            }

            @Override
            public void windowActivated(WindowEvent e) {
                table.setModel(tableModel);
                SwingUtilities.updateComponentTreeUI(PackHandling.this);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        createTable(panel);
        add(new JScrollPane(panel), BorderLayout.CENTER);
    }

    private void createTable(JPanel panel) {
        tableModel = new PackHandling.TableModel();
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(Color.BLACK);
        table.setSelectionForeground(new Color(0xBEC7FF));
        table.setSelectionBackground(new Color(0x202563));
        final Font fontDef = new Font("Monospaced", table.getFont().getStyle(), 12);
        table.setFont(fontDef);
        HexTablePan.setPropColumn(table.getColumnModel().getColumn(0), 50, "");
        // HexTablePan.setPropColumn(table.getColumnModel().getColumn(1), 200, "файл поездки");
        //table.setBackground(Color.BLACK);
        panel.add(table, BorderLayout.CENTER);
    }

    private class TableModel extends AbstractTableModel {
        private static final int NUM_COLUMNS = 2;

        @Override
        public int getRowCount() {
            if (files != null)
                return files.size();
            else
                return 0;
        }

        @Override
        public int getColumnCount() {
            return NUM_COLUMNS;
        }

        @Override
        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0: return String.format("%,06d", row);
                case 1: return files != null ? (File)files.toArray()[row] : null;
                //     case 2: return "456";
            }
            return null;
        }
    }

}
