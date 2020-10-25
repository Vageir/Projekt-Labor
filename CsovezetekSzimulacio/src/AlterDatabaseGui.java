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
    private JTable depoTable;
    private JPanel depoPanel;
    private JPanel containerPanel;
    private JTable containerTable;
    private JTable fuelTable;
    private JPanel fuelPanel;

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
        String operatorCol[] = {"OperatorID", "OperatorName", "OperatorBirth"};
//        DefaultTableModel operatorModel = (DefaultTableModel) operatorTable.getModel();
        DefaultTableModel operatorModel = new DefaultTableModel(operatorCol, 0);
        operatorTable.setModel(operatorModel);
        operatorModel.addColumn("OperatorID");
        operatorModel.addColumn("OperatorName");
        operatorModel.addColumn("OperatorBirth");
        operatorModel.addRow(new Object[] {"OperatorID", "OperatorName", "OperatorBirth"});
        Map<Integer, ArrayList<String>> opMap = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : opMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), birth = entry.getValue().get(2);
            operatorModel.addRow(new Object[] {id, name, birth});
        }

        String depoCol[] = {"DepoID", "DepoName", "DepoLocation"};
        DefaultTableModel depoModel = new DefaultTableModel(depoCol, 0);
        depoTable.setModel(depoModel);
        depoModel.addRow(new Object[] {"DepoID", "DepoName", "DepoLocation"});
        Map<Integer, ArrayList<String>> depMap = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : depMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), location= entry.getValue().get(2);
            depoModel.addRow(new Object[] {id, name, location});
        }

        String contCol[] = {"DepoID", "containerID", "CurrentCapacity", "MaxCapacity", "fuelID"};
        DefaultTableModel contModel = new DefaultTableModel(contCol, 0);
        containerTable.setModel(contModel);
        contModel.addRow(new Object[] {"DepoID", "containerID", "CurrentCapacity", "MaxCapacity", "fuelID"});
        Map<Integer, ArrayList<String>> contMap = new DataBaseHandler().readRecords("depocontainer");
        for (Map.Entry<Integer,ArrayList<String>> entry : contMap.entrySet()){
            String did = entry.getValue().get(0), cid = entry.getValue().get(1), ccap= entry.getValue().get(2), mcap = entry.getValue().get(3), fid = entry.getValue().get(4);
            contModel.addRow(new Object[] {did, cid, ccap, mcap, fid});
        }

        String fuelCol[] = {"fuelID", "fuelName"};
        DefaultTableModel fuelModel = new DefaultTableModel(fuelCol, 0);
        fuelTable.setModel(fuelModel);
        fuelModel.addRow(new Object[] {"fuelID", "fuelName"});
        Map<Integer, ArrayList<String>> fuelMap = new DataBaseHandler().readRecords("fuel");
        for (Map.Entry<Integer,ArrayList<String>> entry : fuelMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1);
            fuelModel.addRow(new Object[] {id, name});
        }

    }
}
