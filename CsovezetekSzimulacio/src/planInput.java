import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        new DataBaseHandler().readRecords(startDepoComboBox,endDepoComboBox,operatorIDComboBox,operatorNameLable);
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

                String startDate = kezdEvComboBox.getSelectedItem().toString()+"-"
                        +monthStart+"-"
                        +kezdNapComboBox.getSelectedItem().toString()+" "
                        +kezdOraTextField.getText()+":"+kezdPercTextField.getText();

                String endDate = vegEvComboBox.getSelectedItem().toString()+"-"
                        +monthEnd+"-"
                        +vegNapComboBox.getSelectedItem().toString()+" "
                        +vegOraTextField.getText()+":"+vegPercTextField.getText();
                System.out.println(endDate);
                new DataBaseHandler().insertRecord(operatorIDComboBox.getSelectedItem().toString()
                                +startDepoComboBox.getSelectedItem().toString()+endDepoComboBox.getSelectedItem().toString()
                                +monthStart+kezdNapComboBox.getSelectedItem().toString()
                                +monthEnd+vegNapComboBox.getSelectedItem().toString(),
                        startDepoComboBox.getSelectedItem().toString(),endDepoComboBox.getSelectedItem().toString(),anyagComboBox.getSelectedIndex(),
                        Integer.parseInt(mennyisegField.getText()),startDate,endDate,operatorIDComboBox.getSelectedItem().toString());

            }
        });

        operatorIDComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               operatorNameLable.setText(
                       new DataBaseHandler().updateGUI("operator","operatorname","operatorid",operatorIDComboBox.getSelectedItem().toString()));
            }
        });
    }
}
