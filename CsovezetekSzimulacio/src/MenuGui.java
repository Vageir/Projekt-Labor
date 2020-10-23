import javax.swing.*;

public class MenuGui extends JFrame {
    private JPanel mainPanel;
    private JButton button1;

    public MenuGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
