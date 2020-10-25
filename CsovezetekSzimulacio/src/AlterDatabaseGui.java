import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

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
        Map<Integer, ArrayList<String>> result = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            System.out.println("ID:"+entry.getValue().get(0));
            System.out.println("Name:"+entry.getValue().get(1));
            System.out.println("Birth:"+entry.getValue().get(2));
        }
        operatorModel.addRow(row);


    }
}
