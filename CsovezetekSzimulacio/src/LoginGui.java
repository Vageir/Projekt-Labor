import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;

public class LoginGui extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGui(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = usernameField.getText();
                char [] pass = passwordField.getPassword();
                LinkedHashMap<Integer, ArrayList<String>> result = new LinkedHashMap<>();
                result = new DataBaseHandler().readRecords("user");
                for (Map.Entry<Integer,ArrayList<String>> entry : result.entrySet()){
                    if (entry.getValue().get(0).equals(name)) {
                        String pw = entry.getValue().get(1);    // kivesszük a jelszót db-ből
                        char [] ch = pw.toCharArray();  // belerakjuk karaktertömbbe
                        if (Arrays.equals(ch, pass)) {
                            showMessageDialog(null, "Sikeres bejelentkezés!");
                        } else showMessageDialog(null, "Helytelen jelszó!");
                        return;
                    }
                }
                showMessageDialog(null, "Helytelen felhasználónév!");
            }
        });
    }
}


