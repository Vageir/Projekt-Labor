import java.util.Timer;
import java.util.TimerTask;

public class SimulationGui {
    private static Simulation simulation;
    private static Graph frame;

    private static void setGUIatStart() {
        simulation = new Simulation();
        frame = new Graph("Depos and pipes", simulation);
        frame.setSize(1200,900);
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

    public static void main(String[] args) {
        setGUIatStart();
        drawSimulation();
    }
}
