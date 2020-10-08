import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.ZoneOffset.UTC;
import static javax.swing.JOptionPane.showMessageDialog;

public class planInput extends JFrame{
    private JPanel mainPanel;
    private JPanel topLeftPanel;
    private JPanel kezdoDatumPanel;
    private JPanel bottomLeftPanel;
    private JPanel bottomRightPanel;
    private JTextField hovaField;
    private JTextField honnanField;
    private JTextField mennyisegField;
    private JComboBox anyagComboBox;
    private JComboBox kezdEvComboBox;
    private JComboBox kezdHonapComboBox;
    private JTextField csohosszTextField;
    private JTextField atmeroTextField;
    private JComboBox kezdNapComboBox;
    private JTextField kezdOraTextField;
    private JTextField kezdPercTextField;
    private JPanel vegeDatumPanel;
    private JTextField nevTextField;
    private JTextField azonTextField;
    private JButton clearButton;
    private JButton submitButton;
    private JTextField vegOraTextField;
    private JTextField vegPercTextField;
    private JComboBox vegEvComboBox;
    private JComboBox vegHonapComboBox;
    private JComboBox vegNapComboBox;
    private JComboBox operatorIDComboBox;
    private JComboBox startDepoComboBox;
    private JComboBox endDepoComboBox;
    private JLabel operatorNameLable;

    public planInput(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        setGUIatStart();
        Simulation s = new Simulation();
//        for(Map.Entry<Integer,ArrayList<String>> entry: new DataBaseHandler().readRecords("transportationplan").entrySet()){
//            System.out.println("Yeet: ");
//            for (String ss :entry.getValue()){
//                System.out.print();
//            }
//            System.out.println();
//        }
        for(Map.Entry<Integer,ArrayList<String>> entry: new DataBaseHandler().readRecords("transportationplan").entrySet()){
            s.addTransportationPlan(new TransportationPlan(entry.getValue()));
        }
        s.runSimulation();
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csohosszTextField.setText("500"); atmeroTextField.setText("50");
                honnanField.setText(""); hovaField.setText(""); mennyisegField.setText(""); kezdOraTextField.setText(""); kezdPercTextField.setText("");
                vegOraTextField.setText(""); vegPercTextField.setText(""); nevTextField.setText(""); azonTextField.setText("");
                anyagComboBox.setSelectedIndex(0);
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int monthStart = kezdHonapComboBox.getSelectedIndex()+1;
                int monthEnd = vegHonapComboBox.getSelectedIndex()+1;
                ArrayList<String> tmp = new ArrayList<>();
                String startDate = kezdEvComboBox.getSelectedItem().toString()+"-"
                        +monthStart+"-"
                        +kezdNapComboBox.getSelectedItem().toString()+" "
                        +kezdOraTextField.getText()+":"+kezdPercTextField.getText();
                String endDate = vegEvComboBox.getSelectedItem().toString()+"-"
                        +monthEnd+"-"
                        +vegNapComboBox.getSelectedItem().toString()+" "
                        +vegOraTextField.getText()+":"+vegPercTextField.getText();
                Date start = new Date();
                Date end = new Date();
                try {
                    start = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(startDate);
                    end = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(endDate);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                if (end.after(start) && (end.getHours() > start.getHours() ||(end.getMinutes() > start.getMinutes()))) {
                    tmp.addAll(Arrays.asList(
                            operatorIDComboBox.getSelectedItem().toString() + monthStart + kezdNapComboBox.getSelectedItem().toString()
                                    + monthEnd + vegNapComboBox.getSelectedItem().toString() + startDepoComboBox.getSelectedItem().toString()
                                    + endDepoComboBox.getSelectedItem().toString(),
                            startDepoComboBox.getSelectedItem().toString(),
                            endDepoComboBox.getSelectedItem().toString(),
                            String.valueOf(anyagComboBox.getSelectedIndex() + 1),
                            mennyisegField.getText(),
                            startDate,
                            endDate,
                            operatorIDComboBox.getSelectedItem().toString()));
                    new DataBaseHandler().insertRecord("transportationplan", tmp);
                }else showMessageDialog(null,"A vég dátum nem lehet előrébb mint a kezdődátum");
            }
        });
        operatorIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               operatorNameLable.setText(
                       new DataBaseHandler().readOneRecord("operator","operatorID = '"+operatorIDComboBox.getSelectedItem().toString()+"'").get(1));
            }
        });
    }
    private void setGUIatStart() {
        LinkedHashMap<Integer,ArrayList<String>> result = new LinkedHashMap<>();
        result = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            operatorIDComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
        operatorNameLable.setText(result.entrySet().iterator().next().getValue().get(1));
        result = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            startDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
            endDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }


    }
    private class ComboItem {
        private String itemName;
        ComboItem(String itemName){
            this.itemName=itemName;
        }
        @Override
        public String toString() {
            return itemName;
        }
    }
}
