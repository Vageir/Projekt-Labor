import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
        initTables();
        fillTables();

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
                if (!listSelectionEvent.getValueIsAdjusting() && operatorTable.getSelectedRow() != -1) {
                    String col1 = operatorTable.getColumnName(0);
                    String val1 = operatorTable.getValueAt(operatorTable.getSelectedRow(),0).toString();
                    String col2 = operatorTable.getColumnName(1);
                    String val2 = operatorTable.getValueAt(operatorTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("operator", col1, val1, col2 ,val2);
                    operatorTable.clearSelection();
                }
            }
        });

        depoTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting() && depoTable.getSelectedRow() != -1) {
                    String col1 = depoTable.getColumnName(0);
                    String val1 = depoTable.getValueAt(depoTable.getSelectedRow(),0).toString();
                    String col2 = depoTable.getColumnName(1);
                    String val2 = depoTable.getValueAt(depoTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("depo", col1, val1, col2 ,val2);
                    depoTable.clearSelection();
                }
            }
        });

        containerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting() && containerTable.getSelectedRow() != -1) {
                    String col1 = containerTable.getColumnName(0);
                    String val1 = containerTable.getValueAt(containerTable.getSelectedRow(),0).toString();
                    String col2 = containerTable.getColumnName(1);
                    String val2 = containerTable.getValueAt(containerTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("depocontainer", col1, val1, col2 ,val2);
                    containerTable.clearSelection();
                }
            }
        });

        connectionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting() && connectionsTable.getSelectedRow() != -1) {
                    String col1 = connectionsTable.getColumnName(0);
                    String val1 = connectionsTable.getValueAt(connectionsTable.getSelectedRow(),0).toString();
                    String col2 = connectionsTable.getColumnName(1);
                    String val2 = connectionsTable.getValueAt(connectionsTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("connecteddepos", col1, val1, col2 ,val2);
                    connectionsTable.clearSelection();
                }
            }
        });

        fuelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting() && fuelTable.getSelectedRow() != -1) {
                    String col1 = fuelTable.getColumnName(0);
                    String val1 = fuelTable.getValueAt(fuelTable.getSelectedRow(),0).toString();
                    String col2 = fuelTable.getColumnName(1);
                    String val2 = fuelTable.getValueAt(fuelTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("fuel", col1, val1, col2 ,val2);
                    fuelTable.clearSelection();
                }
            }
        });

        planTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting() && planTable.getSelectedRow() != -1) {
                    String col1 = planTable.getColumnName(0);
                    String val1 = planTable.getValueAt(planTable.getSelectedRow(),0).toString();
                    String col2 = planTable.getColumnName(1);
                    String val2 = planTable.getValueAt(planTable.getSelectedRow(),1).toString();
//                    System.out.println(col1); System.out.println(val1); System.out.println(col2); System.out.println(val2);
                    deleteOrAlterPopup("transportationplan", col1, val1, col2 ,val2);
                    planTable.clearSelection();
                }
            }
        });

    }

    private void initTables() {

        ArrayList<String> opArray = new DataBaseHandler().getTableMetaData("operator",1);
        String operatorCol[] = {opArray.get(0), opArray.get(1), opArray.get(2)};
//        DefaultTableModel operatorModel = (DefaultTableModel) operatorTable.getModel();
        DefaultTableModel operatorModel = new DefaultTableModel(operatorCol, 0);
        operatorTable.setModel(operatorModel);
//        operatorModel.addColumn("OperatorID");
//        operatorModel.addColumn("OperatorName");
//        operatorModel.addColumn("OperatorBirth");
//        Map<Integer, ArrayList<String>> opMap = new DataBaseHandler().readRecords("operator");
//        for (Map.Entry<Integer,ArrayList<String>> entry : opMap.entrySet()){
//            String id = entry.getValue().get(0), name = entry.getValue().get(1), birth = entry.getValue().get(2);
//            operatorModel.addRow(new Object[] {id, name, birth});
//        }

        ArrayList<String> depoArray = new DataBaseHandler().getTableMetaData("depo",1);
        String depoCol[] = {depoArray.get(0), depoArray.get(1), depoArray.get(2)};
        DefaultTableModel depoModel = new DefaultTableModel(depoCol, 0);
        depoTable.setModel(depoModel);
//        Map<Integer, ArrayList<String>> depMap = new DataBaseHandler().readRecords("depo");
//        for (Map.Entry<Integer,ArrayList<String>> entry : depMap.entrySet()){
//            String id = entry.getValue().get(0), name = entry.getValue().get(1), location= entry.getValue().get(2);
//            depoModel.addRow(new Object[] {id, name, location});
//        }

        ArrayList<String> contArray = new DataBaseHandler().getTableMetaData("depocontainer",1);
        String contCol[] = {contArray.get(0), contArray.get(1), contArray.get(2), contArray.get(3), contArray.get(4)};
        DefaultTableModel contModel = new DefaultTableModel(contCol, 0);
        containerTable.setModel(contModel);
//        Map<Integer, ArrayList<String>> contMap = new DataBaseHandler().readRecords("depocontainer");
//        for (Map.Entry<Integer,ArrayList<String>> entry : contMap.entrySet()){
//            String did = entry.getValue().get(0), cid = entry.getValue().get(1), ccap= entry.getValue().get(2), mcap = entry.getValue().get(3), fid = entry.getValue().get(4);
//            contModel.addRow(new Object[] {did, cid, ccap, mcap, fid});
//        }

        ArrayList<String> fuelArray = new DataBaseHandler().getTableMetaData("fuel",1);
        String fuelCol[] = {fuelArray.get(0), fuelArray.get(1)};
        DefaultTableModel fuelModel = new DefaultTableModel(fuelCol, 0);
        fuelTable.setModel(fuelModel);
//        Map<Integer, ArrayList<String>> fuelMap = new DataBaseHandler().readRecords("fuel");
//        for (Map.Entry<Integer,ArrayList<String>> entry : fuelMap.entrySet()){
//            String id = entry.getValue().get(0), name = entry.getValue().get(1);
//            fuelModel.addRow(new Object[] {id, name});
//        }

        ArrayList<String> planArray = new DataBaseHandler().getTableMetaData("transportationplan", 1);
        String planCol[] = {planArray.get(0), planArray.get(1), planArray.get(2), planArray.get(3), planArray.get(4), planArray.get(5), planArray.get(6), planArray.get(7)};
        DefaultTableModel planModel = new DefaultTableModel(planCol,0);
        planTable.setModel(planModel);
//        Map<Integer, ArrayList<String>> planMap = new DataBaseHandler().readRecords("transportationplan");
//        for (Map.Entry<Integer,ArrayList<String>> entry : planMap.entrySet()){
//            String trid = entry.getValue().get(0), sdid = entry.getValue().get(1), edid = entry.getValue().get(2), fid = entry.getValue().get(3), fam = entry.getValue().get(4);
//            String sdate = entry.getValue().get(5), edate = entry.getValue().get(6), oid = entry.getValue().get(7);
//            planModel.addRow(new Object[] {trid, sdid, edid, fid, fam, sdate, edate, oid});
//        }

        ArrayList<String> connecArray = new DataBaseHandler().getTableMetaData("connecteddepos", 1);
        String connCol[] = {connecArray.get(0), connecArray.get(1), connecArray.get(2), connecArray.get(3), connecArray.get(4)};
        DefaultTableModel connModel = new DefaultTableModel(connCol, 0);
        connectionsTable.setModel(connModel);
//        Map<Integer, ArrayList<String>> connMap = new DataBaseHandler().readRecords("connecteddepos");
//        for (Map.Entry<Integer,ArrayList<String>> entry : connMap.entrySet()){
//            String pid = entry.getValue().get(0), ldid = entry.getValue().get(1), rdid = entry.getValue().get(2), plen = entry.getValue().get(3), pdia = entry.getValue().get(4);
//            connModel.addRow(new Object[] {pid, ldid, rdid, plen, pdia});
//        }

    }

    public void fillTables() {
        DefaultTableModel operatorModel = (DefaultTableModel) operatorTable.getModel();
        operatorModel.setRowCount(0);
        operatorTable.setModel(operatorModel);
        Map<Integer, ArrayList<String>> opMap = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : opMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), birth = entry.getValue().get(2);
            operatorModel.addRow(new Object[] {id, name, birth});
        }

        DefaultTableModel depoModel = (DefaultTableModel) depoTable.getModel();
        depoModel.setRowCount(0);
        depoTable.setModel(depoModel);
        Map<Integer, ArrayList<String>> depMap = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : depMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1), location= entry.getValue().get(2);
            depoModel.addRow(new Object[] {id, name, location});
        }

        DefaultTableModel contModel = (DefaultTableModel) containerTable.getModel();
        contModel.setRowCount(0);
        containerTable.setModel(contModel);
        Map<Integer, ArrayList<String>> contMap = new DataBaseHandler().readRecords("depocontainer");
        for (Map.Entry<Integer,ArrayList<String>> entry : contMap.entrySet()){
            String did = entry.getValue().get(0), cid = entry.getValue().get(1), ccap= entry.getValue().get(2), mcap = entry.getValue().get(3), fid = entry.getValue().get(4);
            contModel.addRow(new Object[] {did, cid, ccap, mcap, fid});
        }

        DefaultTableModel fuelModel = (DefaultTableModel) fuelTable.getModel();
        fuelModel.setRowCount(0);
        fuelTable.setModel(fuelModel);
        Map<Integer, ArrayList<String>> fuelMap = new DataBaseHandler().readRecords("fuel");
        for (Map.Entry<Integer,ArrayList<String>> entry : fuelMap.entrySet()){
            String id = entry.getValue().get(0), name = entry.getValue().get(1);
            fuelModel.addRow(new Object[] {id, name});
        }

        DefaultTableModel planModel = (DefaultTableModel) planTable.getModel();
        planModel.setRowCount(0);
        planTable.setModel(planModel);
        Map<Integer, ArrayList<String>> planMap = new DataBaseHandler().readRecords("transportationplan");
        for (Map.Entry<Integer,ArrayList<String>> entry : planMap.entrySet()){
            String trid = entry.getValue().get(0), sdid = entry.getValue().get(1), edid = entry.getValue().get(2), fid = entry.getValue().get(3), fam = entry.getValue().get(4);
            String sdate = entry.getValue().get(5), edate = entry.getValue().get(6), oid = entry.getValue().get(7);
            planModel.addRow(new Object[] {trid, sdid, edid, fid, fam, sdate, edate, oid});
        }

        DefaultTableModel connModel = (DefaultTableModel) connectionsTable.getModel();
        connModel.setRowCount(0);
        connectionsTable.setModel(connModel);
        Map<Integer, ArrayList<String>> connMap = new DataBaseHandler().readRecords("connecteddepos");
        for (Map.Entry<Integer,ArrayList<String>> entry : connMap.entrySet()){
            String pid = entry.getValue().get(0), ldid = entry.getValue().get(1), rdid = entry.getValue().get(2), plen = entry.getValue().get(3), pdia = entry.getValue().get(4);
            connModel.addRow(new Object[] {pid, ldid, rdid, plen, pdia});
        }

    }

    public void deleteOrAlterPopup(String tableName, String col1, String value1, String col2, String value2) {
        JDialog dialog = new JDialog();
        dialog.setSize(355,175);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);
        dialog.setTitle("Na most mit akarsz?");

        JLabel messageLabel = new JLabel("Mit kíván tenni a rekorddal?");
        messageLabel.setBounds(95,30,300,25);
        dialog.add(messageLabel);

        JButton deleteButton = new JButton("Töröl");
        deleteButton.setBounds(10,90,100,25);
        dialog.add(deleteButton);

        JButton alterButton = new JButton("Módosít");
        alterButton.setBounds(120,90,100,25);
        dialog.add(alterButton);

        JButton backButton = new JButton("Mégse");
        backButton.setBounds(230,90,100,25);
        dialog.add(backButton);

        String condition;
        if (!tableName.equals("depocontainer")) {
            condition = col1 + " = '" + value1 + "'";
        } else {
            condition = col1 + " = '" + value1 + "' AND " + col2 + " = '" + value2 + "'";
        }

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = JOptionPane.showConfirmDialog(null,
                                                                "Biztos hogy törli a rekordot?", "Biztos?",
                                                                        JOptionPane.YES_NO_OPTION,
                                                                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
//                    System.out.println("YES");
                    new DataBaseHandler().deleteRecord(tableName, condition);
                    fillTables();
                    dialog.dispose();
                } else { /*System.out.println("NO"); */}
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispose();
            }
        });

        alterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                alterOperator(condition);
                switch (tableName) {
                    case "operator":
                        alterOperator(condition);
                        break;
                    case "depo":
                        alterDepo(condition);
                        break;
                    case "depocontainer":
                        alterContainer(condition);
                        break;
                    case "connecteddepos":
                        alterPipe(condition);
                        break;
                    case "fuel":
                        alterFuel(condition);
                        break;
                    case "transportationplan":
                        alterPlan(condition);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"ERROR: Hibás táblanév!");
                }


                dialog.dispose();
            }
        });

        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setVisible(true);
    }

    public void alterOperator(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("operator",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("operator", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,220);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);
        JLabel label3 = new JLabel(columns.get(2) + ":"); label3.setBounds(10,90,100,25); alterDia.add(label3);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);
        JTextField field3 = new JTextField(record.get(2)); field3.setBounds(120,90,200,25); alterDia.add(field3);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,135,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,135,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals("")) && !(field3.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1))) || !(field3.getText().equals(record.get(2)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                                                                JOptionPane.YES_NO_OPTION,
                                                                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "'";
                            new DataBaseHandler().updateRecord("operator", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("operator", columns.get(1), field2.getText(), alterCond);
                            new DataBaseHandler().updateRecord("operator", columns.get(2), String.valueOf(field3.getText()), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

    public void alterDepo(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("depo",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("depo", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,220);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);
        JLabel label3 = new JLabel(columns.get(2) + ":"); label3.setBounds(10,90,100,25); alterDia.add(label3);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);
        JTextField field3 = new JTextField(record.get(2)); field3.setBounds(120,90,200,25); alterDia.add(field3);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,135,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,135,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals("")) && !(field3.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1))) || !(field3.getText().equals(record.get(2)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                                                                JOptionPane.YES_NO_OPTION,
                                                                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "'";
                            new DataBaseHandler().updateRecord("depo", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depo", columns.get(1), field2.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depo", columns.get(2), field3.getText(), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

    public void alterContainer(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("depocontainer",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("depocontainer", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,290);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);
        JLabel label3 = new JLabel(columns.get(2) + ":"); label3.setBounds(10,90,100,25); alterDia.add(label3);
        JLabel label4 = new JLabel(columns.get(3) + ":"); label4.setBounds(10,125,100,25); alterDia.add(label4);
        JLabel label5 = new JLabel(columns.get(4) + ":"); label5.setBounds(10,160,100,25); alterDia.add(label5);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);
        JTextField field3 = new JTextField(record.get(2)); field3.setBounds(120,90,200,25); alterDia.add(field3);
        JTextField field4 = new JTextField(record.get(3)); field4.setBounds(120,125,200,25); alterDia.add(field4);
        JTextField field5 = new JTextField(record.get(4)); field5.setBounds(120,160,200,25); alterDia.add(field5);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,205,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,205,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals("")) && !(field3.getText().equals(""))
                        && !(field4.getText().equals("")) && !(field5.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1))) || !(field3.getText().equals(record.get(2)))
                            || !(field4.getText().equals(record.get(3))) || !(field5.getText().equals(record.get(4)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                                                                JOptionPane.YES_NO_OPTION,
                                                                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "' AND " + columns.get(1) + " = '" + record.get(1) + "'";
                            new DataBaseHandler().updateRecord("depocontainer", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depocontainer", columns.get(1), field2.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depocontainer", columns.get(2), field3.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depocontainer", columns.get(3), field4.getText(), alterCond);
                            new DataBaseHandler().updateRecord("depocontainer", columns.get(4), field5.getText(), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

    public void alterPipe(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("connecteddepos",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("connecteddepos", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,290);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);
        JLabel label3 = new JLabel(columns.get(2) + ":"); label3.setBounds(10,90,100,25); alterDia.add(label3);
        JLabel label4 = new JLabel(columns.get(3) + ":"); label4.setBounds(10,125,100,25); alterDia.add(label4);
        JLabel label5 = new JLabel(columns.get(4) + ":"); label5.setBounds(10,160,100,25); alterDia.add(label5);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);
        JTextField field3 = new JTextField(record.get(2)); field3.setBounds(120,90,200,25); alterDia.add(field3);
        JTextField field4 = new JTextField(record.get(3)); field4.setBounds(120,125,200,25); alterDia.add(field4);
        JTextField field5 = new JTextField(record.get(4)); field5.setBounds(120,160,200,25); alterDia.add(field5);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,205,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,205,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals("")) && !(field3.getText().equals(""))
                        && !(field4.getText().equals("")) && !(field5.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1))) || !(field3.getText().equals(record.get(2)))
                            || !(field4.getText().equals(record.get(3))) || !(field5.getText().equals(record.get(4)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                                                                JOptionPane.YES_NO_OPTION,
                                                                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "'";
                            new DataBaseHandler().updateRecord("connecteddepos", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("connecteddepos", columns.get(1), field2.getText(), alterCond);
                            new DataBaseHandler().updateRecord("connecteddepos", columns.get(2), field3.getText(), alterCond);
                            new DataBaseHandler().updateRecord("connecteddepos", columns.get(3), field4.getText(), alterCond);
                            new DataBaseHandler().updateRecord("connecteddepos", columns.get(4), field5.getText(), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

    public void alterFuel(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("fuel",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("fuel", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,220);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,135,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,135,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "'";
                            new DataBaseHandler().updateRecord("fuel", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("fuel", columns.get(1), field2.getText(), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

    public void alterPlan(String condition) {
        ArrayList<String> columns = new DataBaseHandler().getTableMetaData("transportationplan",1);
        ArrayList<String> record = new DataBaseHandler().readRecordWithCondition("transportationplan", condition);

        JDialog alterDia = new JDialog();
        alterDia.setSize(355,430);
        alterDia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alterDia.setLocationRelativeTo(null);
        alterDia.setLayout(null);

        JLabel label1 = new JLabel(columns.get(0) + ":"); label1.setBounds(10,20,100,25); alterDia.add(label1);
        JLabel label2 = new JLabel(columns.get(1) + ":"); label2.setBounds(10,55,100,25); alterDia.add(label2);
        JLabel label3 = new JLabel(columns.get(2) + ":"); label3.setBounds(10,90,100,25); alterDia.add(label3);
        JLabel label4 = new JLabel(columns.get(3) + ":"); label4.setBounds(10,125,100,25); alterDia.add(label4);
        JLabel label5 = new JLabel(columns.get(4) + ":"); label5.setBounds(10,160,100,25); alterDia.add(label5);
        JLabel label6 = new JLabel(columns.get(5) + ":"); label6.setBounds(10,195,100,25); alterDia.add(label6);
        JLabel label7 = new JLabel(columns.get(6) + ":"); label7.setBounds(10,230,100,25); alterDia.add(label7);
        JLabel label8 = new JLabel(columns.get(7) + ":"); label8.setBounds(10,265,100,25); alterDia.add(label8);
        JLabel label9 = new JLabel(columns.get(8) + ":"); label9.setBounds(10,300,100,25); alterDia.add(label9);

        JTextField field1 = new JTextField(record.get(0)); field1.setBounds(120,20,200,25); alterDia.add(field1);
        JTextField field2 = new JTextField(record.get(1)); field2.setBounds(120,55,200,25); alterDia.add(field2);
        JTextField field3 = new JTextField(record.get(2)); field3.setBounds(120,90,200,25); alterDia.add(field3);
        JTextField field4 = new JTextField(record.get(3)); field4.setBounds(120,125,200,25); alterDia.add(field4);
        JTextField field5 = new JTextField(record.get(4)); field5.setBounds(120,160,200,25); alterDia.add(field5);
        JTextField field6 = new JTextField(record.get(5)); field6.setBounds(120,195,200,25); alterDia.add(field6);
        JTextField field7 = new JTextField(record.get(6)); field7.setBounds(120,230,200,25); alterDia.add(field7);
        JTextField field8 = new JTextField(record.get(7)); field8.setBounds(120,265,200,25); alterDia.add(field8);
        JTextField field9 = new JTextField(record.get(8)); field9.setBounds(120,300,200,25); alterDia.add(field9);

        JButton confirmButton = new JButton("Módosít"); confirmButton.setBounds(65,345,100,25); alterDia.add(confirmButton);
        JButton backButton = new JButton("Mégse"); backButton.setBounds(175,345,100,25); alterDia.add(backButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(field1.getText().equals("")) && !(field2.getText().equals("")) && !(field3.getText().equals(""))
                        && !(field4.getText().equals("")) && !(field5.getText().equals("")) && !(field6.getText().equals(""))
                        && !(field7.getText().equals("")) && !(field8.getText().equals("")) && !(field9.getText().equals(""))) {
                    // ha egyik mező sem üres
                    if (!(field1.getText().equals(record.get(0))) || !(field2.getText().equals(record.get(1))) || !(field3.getText().equals(record.get(2)))
                            || !(field4.getText().equals(record.get(3))) || !(field5.getText().equals(record.get(4))) || !(field6.getText().equals(record.get(5)))
                            || !(field7.getText().equals(record.get(6))) || !(field8.getText().equals(record.get(7))) || !(field9.getText().equals(record.get(8)))) {
                        // ha bármelyik adat változott
                        int result = JOptionPane.showConfirmDialog(null, "Biztos hogy módosítja a rekordot?", "Biztos?",
                                                                                JOptionPane.YES_NO_OPTION,
                                                                                JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
//                          System.out.println("YES");
                            String alterCond = columns.get(0) + " = '" + record.get(0) + "'";
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(0), field1.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(1), field2.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(2), field3.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(3), field4.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(4), field5.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(5), field6.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(6), field7.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(7), field8.getText(), alterCond);
                            new DataBaseHandler().updateRecord("transportationplan", columns.get(8), field9.getText(), alterCond);
                            fillTables();
                            alterDia.dispose();
                        } else { /*System.out.println("NO"); */}
                    } else {
                        JOptionPane.showMessageDialog(alterDia, "A rekord nem módosult");
                        alterDia.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Egy mezőt sem hagyhat üresen!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alterDia.dispose();
            }
        });

        alterDia.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        alterDia.setVisible(true);
    }

}