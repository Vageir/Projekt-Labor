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

    public planInput(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
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
                int month = kezdHonapComboBox.getSelectedIndex()+1;
                String startDate = kezdEvComboBox.getSelectedItem().toString()+"-"
                        +month+"-"
                        +kezdNapComboBox.getSelectedItem().toString()+" "
                        +kezdOraTextField.getText()+":"+kezdPercTextField.getText();
                month = vegHonapComboBox.getSelectedIndex()+1;
                String endDate = vegEvComboBox.getSelectedItem().toString()+"-"
                        +month+"-"
                        +vegNapComboBox.getSelectedItem().toString()+" "
                        +vegOraTextField.getText()+":"+vegPercTextField.getText();
                System.out.println(endDate);
                new DataBaseHandler().insertRecord(azonTextField.getText()+honnanField.getText()+hovaField.getText()
                                +kezdHonapComboBox.getSelectedItem().toString()+kezdNapComboBox.getSelectedItem().toString()
                                +vegHonapComboBox.getSelectedItem().toString()+vegNapComboBox.getSelectedItem().toString(),
                        honnanField.getText(),hovaField.getText(),anyagComboBox.getSelectedItem().toString(),
                        Integer.parseInt(mennyisegField.getText()),startDate,endDate,azonTextField.getText(),
                        nevTextField.getText());

            }
        });
    }



}
