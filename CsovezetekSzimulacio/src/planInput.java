import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class planInput extends JFrame{
    private JPanel mainPanel;
    private JTextField volumeField;
    private JComboBox fluidComboBox;
    private JComboBox kezdEvComboBox;
    private JComboBox kezdHonapComboBox;
    private JTextField pipeLengthField;
    private JTextField pipeDiameterField;
    private JComboBox kezdNapComboBox;
    private JSpinner kezdOraSpinner;
    private JSpinner kezdPercSpinner;
    private JPanel vegeDatumPanel;
    private JButton clearButton;
    private JButton submitButton;
    private JSpinner vegOraSpinner;
    private JSpinner vegPercSpinner;
    private JComboBox vegEvComboBox;
    private JComboBox vegHonapComboBox;
    private JComboBox vegNapComboBox;
    private JComboBox operatorIDComboBox;
    private JComboBox startDepoComboBox;
    private JComboBox endDepoComboBox;
    private JLabel operatorNameLabel;

    public planInput(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        setGUIatStart();
        Simulation s = new Simulation();
        for(Map.Entry<Integer,ArrayList<String>> entry: new DataBaseHandler().readRecords("transportationplan").entrySet()){
            s.addTransportationPlan(new TransportationPlan(entry.getValue()));
        }
        s.runSimulation();
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pipeLengthField.setText("1"); pipeDiameterField.setText("50");
                startDepoComboBox.setSelectedIndex(0); endDepoComboBox.setSelectedIndex(1); volumeField.setText("");
                kezdOraSpinner.setValue(LocalTime.now().getHour()); kezdPercSpinner.setValue(LocalTime.now().getMinute());
                vegOraSpinner.setValue(LocalTime.now().getHour()); vegPercSpinner.setValue(LocalTime.now().getMinute());
                fluidComboBox.setSelectedIndex(0);
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
                        + kezdOraSpinner.getValue().toString()+":"+ kezdPercSpinner.getValue().toString();
                String endDate = vegEvComboBox.getSelectedItem().toString()+"-"
                        +monthEnd+"-"
                        +vegNapComboBox.getSelectedItem().toString()+" "
                        + vegOraSpinner.getValue().toString()+":"+ vegPercSpinner.getValue().toString();
                Date start = new Date();
                Date end = new Date();
                try {
                    start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate);
                    end = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDate);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
//                System.out.println(start.getTime());
//                System.out.println(end.getTime());
//                if (end.after(start)){
//                    System.out.println("yeet");
//                }
//                else{
//                    System.out.println("wtf");
//                }
                if (end.after(start)) {
                    tmp.addAll(Arrays.asList(
                            operatorIDComboBox.getSelectedItem().toString() + monthStart + kezdNapComboBox.getSelectedItem().toString()
                                    + monthEnd + vegNapComboBox.getSelectedItem().toString() + startDepoComboBox.getSelectedItem().toString()
                                    + endDepoComboBox.getSelectedItem().toString(),
                            startDepoComboBox.getSelectedItem().toString(),
                            endDepoComboBox.getSelectedItem().toString(),
                            String.valueOf(fluidComboBox.getSelectedIndex() + 1),
                            volumeField.getText(),
                            startDate,
                            endDate,
                            operatorIDComboBox.getSelectedItem().toString()));
                    new DataBaseHandler().insertRecord("transportationplan", tmp);
                }else showMessageDialog(null,"A befejezési dátumnak későbbinek kell lennie a kezdődátumnál!");
            }
        });
        operatorIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               operatorNameLabel.setText(
                       new DataBaseHandler().readRecordWithCondition("operator","operatorID = '"+operatorIDComboBox.getSelectedItem().toString()+"'").get(1));
            }
        });
    }
    private void setGUIatStart() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int currentYear = localDateTime.getYear();
        int currentHour = localDateTime.getHour();
        int currentMinute = localDateTime.getMinute();

        kezdEvComboBox.addItem(currentYear);
        kezdEvComboBox.addItem(currentYear+1);
        kezdEvComboBox.setSelectedItem(currentYear);
        kezdHonapComboBox.setSelectedIndex(localDateTime.getMonth().getValue()-1);
        kezdNapComboBox.setSelectedIndex(localDateTime.getDayOfMonth()-1);
        kezdOraSpinner.setModel(new SpinnerNumberModel(currentHour, 0, 23, 1));
        kezdPercSpinner.setModel(new SpinnerNumberModel(currentMinute, 0, 59, 1));

        vegEvComboBox.addItem(currentYear);
        vegEvComboBox.addItem(currentYear+1);
        vegHonapComboBox.setSelectedIndex(localDateTime.getMonth().getValue()-1);
        vegNapComboBox.setSelectedIndex(localDateTime.getDayOfMonth()-1);
        vegOraSpinner.setModel(new SpinnerNumberModel(currentHour, 0, 23, 1));
        vegPercSpinner.setModel(new SpinnerNumberModel(currentMinute, 0, 59, 1));

        LinkedHashMap<Integer,ArrayList<String>> result = new LinkedHashMap<>();
        result = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            operatorIDComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
        startDepoComboBox.addItem(new ComboItem(""));
        endDepoComboBox.addItem(new ComboItem(""));
        operatorNameLabel.setText(result.entrySet().iterator().next().getValue().get(1));
        result = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            startDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
            endDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
        endDepoComboBox.setSelectedIndex(1);
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
