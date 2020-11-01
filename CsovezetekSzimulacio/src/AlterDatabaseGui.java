import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.*;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Map;

public class AlterDatabaseGui extends JFrame {
    private JPanel mainPanel;
    private JButton menuButton;
    private JTable operatorTable;
    private JTable depoTable;
    private JTable containerTable;
    private JTable fuelTable;
    private JTable planTable;
    private JTable connectionsTable;

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

        operatorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                String id = operatorTable.getValueAt(operatorTable.getSelectedRow(), 0).toString();
                System.out.println(id);
            }
        });
    }

    private void initGui() {
        ArrayList<String> opArray = new DataBaseHandler().getTableMetaData("operator",1);
        String operatorCol[] = {opArray.get(0), opArray.get(1), opArray.get(2)};
//        DefaultTableModel operatorModel = (DefaultTableModel) operatorTable.getModel();
        DefaultTableModel operatorModel = new DefaultTableModel(operatorCol, 0);
        operatorTable.setModel(operatorModel);
//        operatorModel.addColumn("OperatorID");
//        operatorModel.addColumn("OperatorName");
//        operatorModel.addColumn("OperatorBirth");
        operatorModel.addRow(new Object[] {opArray.get(0), opArray.get(1), opArray.get(2)});
        Map<Integer, ArrayList<String>> opMap = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : opMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), birth = entry.getValue().get(2);
            operatorModel.addRow(new Object[] {id, name, birth});
        }

        ArrayList<String> depoArray = new DataBaseHandler().getTableMetaData("depo",1);
        String depoCol[] = {depoArray.get(0), depoArray.get(1), depoArray.get(2)};
        DefaultTableModel depoModel = new DefaultTableModel(depoCol, 0);
        depoTable.setModel(depoModel);
        depoModel.addRow(new Object[] {depoArray.get(0), depoArray.get(1), depoArray.get(2)});
        Map<Integer, ArrayList<String>> depMap = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : depMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), location= entry.getValue().get(2);
            depoModel.addRow(new Object[] {id, name, location});
        }

        ArrayList<String> contArray = new DataBaseHandler().getTableMetaData("depocontainer",1);
        String contCol[] = {contArray.get(0), contArray.get(1), contArray.get(2), contArray.get(3), contArray.get(4)};
        DefaultTableModel contModel = new DefaultTableModel(contCol, 0);
        containerTable.setModel(contModel);
        contModel.addRow(new Object[] {contArray.get(0), contArray.get(1), contArray.get(2), contArray.get(3), contArray.get(4)});
        Map<Integer, ArrayList<String>> contMap = new DataBaseHandler().readRecords("depocontainer");
        for (Map.Entry<Integer,ArrayList<String>> entry : contMap.entrySet()){
            String did = entry.getValue().get(0), cid = entry.getValue().get(1), ccap= entry.getValue().get(2), mcap = entry.getValue().get(3), fid = entry.getValue().get(4);
            contModel.addRow(new Object[] {did, cid, ccap, mcap, fid});
        }

        ArrayList<String> fuelArray = new DataBaseHandler().getTableMetaData("fuel",1);
        String fuelCol[] = {fuelArray.get(0), fuelArray.get(1)};
        DefaultTableModel fuelModel = new DefaultTableModel(fuelCol, 0);
        fuelTable.setModel(fuelModel);
        fuelModel.addRow(new Object[] {fuelArray.get(0), fuelArray.get(1)});
        Map<Integer, ArrayList<String>> fuelMap = new DataBaseHandler().readRecords("fuel");
        for (Map.Entry<Integer,ArrayList<String>> entry : fuelMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1);
            fuelModel.addRow(new Object[] {id, name});
        }

        ArrayList<String> planArray = new DataBaseHandler().getTableMetaData("transportationplan", 1);
        String planCol[] = {planArray.get(0), planArray.get(1), planArray.get(2), planArray.get(3), planArray.get(4), planArray.get(5), planArray.get(6), planArray.get(7)};
        DefaultTableModel planModel = new DefaultTableModel(planCol,0);
        planTable.setModel(planModel);
        planModel.addRow(new Object[] {planArray.get(0), planArray.get(1), planArray.get(2), planArray.get(3), planArray.get(4), planArray.get(5), planArray.get(6), planArray.get(7)});
        Map<Integer, ArrayList<String>> planMap = new DataBaseHandler().readRecords("transportationplan");
        for (Map.Entry<Integer,ArrayList<String>> entry : planMap.entrySet()){
            String trid = entry.getValue().get(0), sdid = entry.getValue().get(1), edid = entry.getValue().get(2), fid = entry.getValue().get(3), fam = entry.getValue().get(4);
            String sdate = entry.getValue().get(5), edate = entry.getValue().get(6), oid = entry.getValue().get(7);
            planModel.addRow(new Object[] {trid, sdid, edid, fid, fam, sdate, edate, oid});
        }

        ArrayList<String> connecArray = new DataBaseHandler().getTableMetaData("connecteddepos", 1);
        String connCol[] = {connecArray.get(0), connecArray.get(1), connecArray.get(2), connecArray.get(3), connecArray.get(4)};
        DefaultTableModel connModel = new DefaultTableModel(connCol, 0);
        connectionsTable.setModel(connModel);
        connModel.addRow(new Object[] {connecArray.get(0), connecArray.get(1), connecArray.get(2), connecArray.get(3), connecArray.get(4)});
        Map<Integer, ArrayList<String>> connMap = new DataBaseHandler().readRecords("connecteddepos");
        for (Map.Entry<Integer,ArrayList<String>> entry : connMap.entrySet()){
            String pid = entry.getValue().get(0), ldid = entry.getValue().get(1), rdid = entry.getValue().get(2), plen = entry.getValue().get(3), pdia = entry.getValue().get(4);
            connModel.addRow(new Object[] {pid, ldid, rdid, plen, pdia});
        }

    }


}