import javax.swing.*;

public class AdminGui extends JFrame {
    private JPanel mainPanel;
    private JTextField operatorNameField;
    private JTextField operatorIDField;
    private JTextField operatorBirthField;
    private JButton addOperatorButton;
    private JTextField depoNameField;
    private JTextField depoIDField;
    private JButton addDepoButton;
    private JTextField containerFluidField;
    private JTextField containerCapacityField;
    private JTextField containerIDField;
    private JTextField containerDepoField;
    private JButton addContainerButton;
    private JTextField connectDepoOneField;
    private JTextField connectDepoTwoField;
    private JTextField connectPipeLengthField;
    private JButton connectDeposButton;
    private JButton exitButton;
    private JButton newPlanButton;
    private JTextField connectPipeDiameterField;

    public AdminGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }
}
