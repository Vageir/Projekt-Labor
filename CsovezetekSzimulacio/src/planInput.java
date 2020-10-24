import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class planInput extends JFrame {
    private JPanel mainPanel;
    private JTextField volumeField;
    private JComboBox fluidComboBox;
    private JComboBox startYearComboBox;
    private JComboBox startMonthComboBox;
    private JTextField pipeLengthField;
    private JTextField pipeDiameterField;
    private JComboBox startDayComboBox;
    private JSpinner startHourSpinner;
    private JSpinner startMinuteSpinner;
    private JPanel vegeDatumPanel;
    private JButton clearButton;
    private JButton submitButton;
    private JSpinner endHourSpinner;
    private JSpinner endMinuteSpinner;
    private JComboBox endYearComboBox;
    private JComboBox endMonthComboBox;
    private JComboBox endDayComboBox;
    private JComboBox operatorIDComboBox;
    private JComboBox startDepoComboBox;
    private JComboBox endDepoComboBox;
    private JLabel operatorNameLabel;
    private JButton backToAdminButton;
    private JButton exitButton;

    public planInput(String title) {
        super(title);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        setGUIatStart();
//        Simulation s = new Simulation();
//        s.runSimulation();

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pipeLengthField.setText("1");
                pipeDiameterField.setText("50");
                startDepoComboBox.setSelectedIndex(0);
                endDepoComboBox.setSelectedIndex(1);
                volumeField.setText("");
                startHourSpinner.setValue(LocalTime.now().getHour());
                startMinuteSpinner.setValue(LocalTime.now().getMinute());
                endHourSpinner.setValue(LocalTime.now().getHour());
                endMinuteSpinner.setValue(LocalTime.now().getMinute());
                fluidComboBox.setSelectedIndex(0);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int monthStart = startMonthComboBox.getSelectedIndex() + 1;
                int monthEnd = endMonthComboBox.getSelectedIndex() + 1;
                ArrayList<String> tmp = new ArrayList<>();
                String startDate = startYearComboBox.getSelectedItem().toString() + "-"
                        + monthStart + "-"
                        + startDayComboBox.getSelectedItem().toString() + " "
                        + startHourSpinner.getValue().toString() + ":" + startMinuteSpinner.getValue().toString();
                String endDate = endYearComboBox.getSelectedItem().toString() + "-"
                        + monthEnd + "-"
                        + endDayComboBox.getSelectedItem().toString() + " "
                        + endHourSpinner.getValue().toString() + ":" + endMinuteSpinner.getValue().toString();
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
                            operatorIDComboBox.getSelectedItem().toString() + monthStart + startDayComboBox.getSelectedItem().toString()
                                    + monthEnd + endDayComboBox.getSelectedItem().toString() + startDepoComboBox.getSelectedItem().toString()
                                    + endDepoComboBox.getSelectedItem().toString(),
                            startDepoComboBox.getSelectedItem().toString(),
                            endDepoComboBox.getSelectedItem().toString(),
                            String.valueOf(fluidComboBox.getSelectedIndex() + 1),
                            volumeField.getText(),
                            startDate,
                            endDate,
                            operatorIDComboBox.getSelectedItem().toString()));
                    new DataBaseHandler().insertRecord("transportationplan", tmp);
                } else showMessageDialog(null, "A befejezési dátumnak későbbinek kell lennie a kezdődátumnál!");
            }
        });

        operatorIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operatorNameLabel.setText(
                        new DataBaseHandler().readRecordWithCondition("operator", "operatorID = '" + operatorIDComboBox.getSelectedItem().toString() + "'").get(1));
            }
        });

        backToAdminButton.addActionListener(e -> this.dispose());
    }

    private void setGUIatStart() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int currentYear = localDateTime.getYear();
        int currentHour = localDateTime.getHour();
        int currentMinute = localDateTime.getMinute();

        startYearComboBox.addItem(currentYear);
        startYearComboBox.addItem(currentYear + 1);
        startYearComboBox.setSelectedItem(currentYear);
        startMonthComboBox.setSelectedIndex(localDateTime.getMonth().getValue() - 1);
        startDayComboBox.setSelectedIndex(localDateTime.getDayOfMonth() - 1);
        startHourSpinner.setModel(new SpinnerNumberModel(currentHour, 0, 23, 1));
        startMinuteSpinner.setModel(new SpinnerNumberModel(currentMinute, 0, 59, 1));

        endYearComboBox.addItem(currentYear);
        endYearComboBox.addItem(currentYear + 1);
        endMonthComboBox.setSelectedIndex(localDateTime.getMonth().getValue() - 1);
        endDayComboBox.setSelectedIndex(localDateTime.getDayOfMonth() - 1);
        endHourSpinner.setModel(new SpinnerNumberModel(currentHour, 0, 23, 1));
        endMinuteSpinner.setModel(new SpinnerNumberModel(currentMinute, 0, 59, 1));

        LinkedHashMap<Integer, ArrayList<String>> result = new LinkedHashMap<>();
        result = new DataBaseHandler().readRecords("operator");
        for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()) {
            operatorIDComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
        startDepoComboBox.addItem(new ComboItem(""));
        endDepoComboBox.addItem(new ComboItem(""));
        operatorNameLabel.setText(result.entrySet().iterator().next().getValue().get(1));
        result = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()) {
            startDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
            endDepoComboBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
        startDepoComboBox.setSelectedIndex(0);
        endDepoComboBox.setSelectedIndex(1);
    }

    private class ComboItem {
        private String itemName;
        ComboItem(String itemName) {this.itemName = itemName;}
        @Override
        public String toString() {
            return itemName;
        }
    }
}