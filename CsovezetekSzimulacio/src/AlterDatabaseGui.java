import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterDatabaseGui extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel operatorPanel;
    private JButton menuButton;
    private JTable operatorTable;

    public AlterDatabaseGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        initGui();

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame menu = new MenuGui("Menü");
                menu.setVisible(true);
                AlterDatabaseGui.super.dispose();
            }
        });
    }

    private void initGui() {
        String operatorCol[] = {"ID", "Name", "Birth"};
//        DefaultTableModel operatorModel = (DefaultTableModel) operatorTable.getModel();
        DefaultTableModel operatorModel = new DefaultTableModel(operatorCol, 0);
//        operatorModel.addColumn("ID");
//        operatorModel.addColumn("Name");
//        operatorModel.addColumn("Birth");
//        operatorModel.setColumnCount(3);
        operatorTable.setModel(operatorModel);
        Object[] row = {"AD123", "Adam", "1956-10-25"};
        operatorModel.addRow(row);


    }
}
