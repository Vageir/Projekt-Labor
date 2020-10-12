import javax.swing.*;

public class LoginGui extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;

    public LoginGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }
}


