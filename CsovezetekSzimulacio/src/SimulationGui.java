import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationGui {
    private static Simulation simulation;
    private static Graph frame;

    private static void setGUIatStart() {
        simulation = new Simulation();
        frame = new Graph("A szimuláció állapota:", simulation);
        frame.setSize(1200, 900);
        frame.setVisible(true);
        frame.loadGraph();
    }

    private static void drawSimulation() {
        TimerTask repaintTask = new TimerTask() {
            public void run() {
                frame.repaint();
            }
        };
        new Timer().schedule(repaintTask, 0, 100);
        simulation.runSimulation();
        repaintTask.cancel();
    }

    private static void popUp() {
        List<String> errorMessages = simulation.getErrorMessages();
        if (!errorMessages.isEmpty())
            JOptionPane.showMessageDialog(frame, errorMessages, "Hiba!", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(frame, "Minden szállítási terv teljesíthető.", "Sikeres lefutás!", JOptionPane.INFORMATION_MESSAGE);
    }

//    public static void main(String[] args) {
//        setGUIatStart();
//        drawSimulation();
//        popUp();
//    }
}
