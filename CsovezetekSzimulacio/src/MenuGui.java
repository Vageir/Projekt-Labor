import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGui extends JFrame {
    private JPanel mainPanel;
    private JButton adminButton;
    private JButton planInputButton;
    private JButton simulationButton;
    private JButton logoutButton;

    public MenuGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame admin = new AdminGui("Admin");
                admin.setVisible(true);
                MenuGui.super.dispose();
            }
        });

        planInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame planInput = new planInput("Új terv");
                planInput.setVisible(true);
                MenuGui.super.dispose();
            }
        });

        simulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Simulation s = new Simulation();
                s.runSimulation();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame login = new LoginGui("Login");
                login.setVisible(true);
                MenuGui.super.dispose();
            }
        });
    }
}
