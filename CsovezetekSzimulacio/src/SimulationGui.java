//import java.util.Timer;
//import java.util.TimerTask;
//
//public class SimulationGui {
//    private static Simulation simulation;
//    private static Graph frame;
//
//    private static void setGUIatStart() {
//        simulation = new Simulation();
//        frame = new Graph("Depos and pipes", simulation);
//        frame.setSize(700,700);
//        frame.setVisible(true);
//        frame.loadGraph();
//    }
//
//    private static void drawSimulation() {
//        new Timer().schedule(
//        new TimerTask() {
//            public void run() {
//                frame.repaint();
//
//            }
//        }, 0, 100);
//        simulation.runSimulation();
//    }
//
//    public static void main(String[] args) {
//        setGUIatStart();
//        drawSimulation();
//    }
//}
