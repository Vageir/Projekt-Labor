import javax.swing.*;

public class planInput extends JFrame{
    private JPanel mainPanel;
    private JPanel topLeftPanel;
    private JPanel kezdoDatumPanel;
    private JPanel bottomLeftPanel;
    private JPanel bottomRightPanel;
    private JTextField hovaField;
    private JTextField honnanField;
    private JTextField mennyisegField;
    private JComboBox anyagBox;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField a500TextField;
    private JTextField a50TextField;
    private JComboBox comboBox3;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel vegeDatumPanel;
    private JTextField textField5;
    private JTextField textField6;
    private JButton newPlanButton;
    private JButton submitButton;

    public planInput(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }


}
