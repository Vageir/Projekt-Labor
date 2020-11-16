import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGui extends JFrame {
    private JPanel mainPanel;
    private JButton newDataButton;
    private JButton planInputButton;
    private JButton simulationButton;
    private JButton logoutButton;
    private JButton alterDatabaseButton;

    public MenuGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        newDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame admin = new AdminGui("Új adat");
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
                SimulationGui simulationGui = new SimulationGui();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        simulationGui.setGUIatStart();
                        simulationGui.drawSimulation();
                        simulationGui.popUp();
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
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

        alterDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame alter = new AlterDatabaseGui("Adatok");
                alter.setVisible(true);
                MenuGui.super.dispose();
            }
        });
    }
}
