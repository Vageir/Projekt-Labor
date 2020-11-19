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
    private JButton menuButton;
    private JTextField fluidNameField;
    private JButton addFluidButton;

    public AdminGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        setComboBoxes();

        addOperatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Date birth = new Date();
                try {
                    birth = new SimpleDateFormat("yyyy-MM-dd").parse(operatorBirthField.getText());

                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.addAll(Arrays.asList(
                            operatorIDField.getText(),
                            operatorNameField.getText(),
                            operatorBirthField.getText()));
                    new DataBaseHandler().insertRecord("operator", tmp);
                    operatorIDField.setText("");
                    operatorNameField.setText("");
                    operatorBirthField.setText("");
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Hibás dátumformátum!");
                }

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
                depoIDField.setText("");
                depoNameField.setText("");
                depoLocationField.setText("");
                setComboBoxes();
            }
        });

        addFluidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        null,
                        fluidNameField.getText()));
                new DataBaseHandler().insertRecord("fuel", tmp);
                fluidNameField.setText("");
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
                containerCapacityField.setText("");
                containerDepoBox.setSelectedIndex(0);
                containerFluidBox.setSelectedIndex(0);
            }
        });

        connectDeposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String pipeID = "" + connectDepoOneBox.getSelectedItem().toString() + connectDepoTwoBox.getSelectedItem().toString();
                int num = 0;
                LinkedHashMap<Integer,ArrayList<String>> result = new LinkedHashMap<>();
                result = new DataBaseHandler().readRecords("connecteddepos");
                for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
                    if ((entry.getValue().get(1).equals(connectDepoOneBox.getSelectedItem().toString()))
                            && (entry.getValue().get(2).equals(connectDepoTwoBox.getSelectedItem().toString()))) {
                        num++;
                    }
                }
                pipeID += String.valueOf(++num);
                ArrayList<String> tmp = new ArrayList<>();
                tmp.addAll(Arrays.asList(
                        pipeID,
                        connectDepoOneBox.getSelectedItem().toString(),
                        connectDepoTwoBox.getSelectedItem().toString(),
                        connectPipeLengthField.getText(),
                        connectPipeDiameterField.getText()));
                new DataBaseHandler().insertRecord("connecteddepos", tmp);
                connectPipeLengthField.setText("");
                connectPipeDiameterField.setText("");
                connectDepoOneBox.setSelectedIndex(0);
                connectDepoTwoBox.setSelectedIndex(0);
            }
        });

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame menu = new MenuGui("Menü");
                menu.setVisible(true);
                AdminGui.super.dispose();
            }
        });
    }

    private void setComboBoxes() {
        containerFluidBox.removeAllItems();
        containerDepoBox.removeAllItems();
        connectDepoOneBox.removeAllItems();
        connectDepoTwoBox.removeAllItems();
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
