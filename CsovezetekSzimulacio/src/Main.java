import javax.swing.*;

public class Main {

    public static void main(String[] args) {
//        JFrame frame = new LoginGui("Login");
//        JFrame frame = new MenuGui("Menü");
//        JFrame frame = new AdminGui("Admin");
        JFrame frame = new AlterDatabaseGui("Módosítás");
//        JFrame frame = new planInput("Plan input");
        frame.setVisible(true);
    }
}
