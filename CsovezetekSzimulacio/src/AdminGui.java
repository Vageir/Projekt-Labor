import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminGui extends JFrame {
    private JPanel mainPanel;
    private JTextField operatorNameField;
    private JTextField operatorIDField;
    private JTextField operatorBirthField;
    private JButton addOperatorButton;
    private JTextField depoNameField;
    private JTextField depoIDField;
    private JTextField depoLocationField;
    private JButton addDepoButton;
    private JComboBox containerFluidBox;
    private JTextField containerCapacityField;

    private JComboBox containerDepoBox;
    private JButton addContainerButton;
    private JTextField connectPipeLengthField;
    private JTextField connectPipeDiameterField;
    private JComboBox connectDepoOneBox;
    private JComboBox connectDepoTwoBox;
    private JButton connectDeposButton;
    private JButton exitButton;
    private JButton newPlanButton;

    public AdminGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        setComboBoxes();

        addOperatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Date birth = new Date();
                try {
                    birth = new SimpleDateFormat("yyyy-MM-dd").parse(operatorBirthField.getText());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        operatorIDField.getText(),
                        operatorNameField.getText(),
                        operatorBirthField.getText()));
                new DataBaseHandler().insertRecord("operator", tmp);
            }
        });

        addDepoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        depoIDField.getText(),
                        depoNameField.getText(),
                        depoLocationField.getText()));
                new DataBaseHandler().insertRecord("depo", tmp);
            }
        });

        addContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        containerDepoBox.getSelectedItem().toString(),
                        containerDepoBox.getSelectedItem().toString() +
                                String.valueOf(containerFluidBox.getSelectedIndex() + 1) +
                                containerCapacityField.getText(),
                        containerCapacityField.getText(),   // current capacity = max capacity, because we assume its empty
                        containerCapacityField.getText(),   // max capacity
                        String.valueOf(containerFluidBox.getSelectedIndex() + 1)));
                new DataBaseHandler().insertRecord("depocontainer", tmp);
            }
        });

        connectDeposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        connectDepoOneBox.getSelectedItem().toString(),
                        connectDepoTwoBox.getSelectedItem().toString(),
                        connectPipeLengthField.getText(),
                        connectPipeDiameterField.getText()));
                new DataBaseHandler().insertRecord("connecteddepos", tmp);
            }
        });

        newPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame plan = new planInput("Plan input");
                plan.setVisible(true);
            }
        });
    }

    private void setComboBoxes() {
        LinkedHashMap<Integer,ArrayList<String>> result = new LinkedHashMap<>();
        result = new DataBaseHandler().readRecords("fuel");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            containerFluidBox.addItem(new ComboItem(entry.getValue().get(1)));
        }
        result = new DataBaseHandler().readRecords("depo");
        for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
            containerDepoBox.addItem(new ComboItem(entry.getValue().get(0)));
            connectDepoOneBox.addItem(new ComboItem(entry.getValue().get(0)));
            connectDepoTwoBox.addItem(new ComboItem(entry.getValue().get(0)));
        }
    }

    private class ComboItem {
        private String itemName;
        ComboItem(String itemName){this.itemName=itemName;}
        @Override
        public String toString() {
            return itemName;
        }
    }
}