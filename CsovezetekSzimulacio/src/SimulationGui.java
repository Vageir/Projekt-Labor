import java.awt.*;
import java.util.List;
import java.util.Map;

public class SimulationGui {
    private static Simulation simulation;
    private static Graph frame;

    private static void setGUIatStart() {
        simulation = new Simulation();

        frame = new Graph("Depos and pipes");
        frame.setSize(700,700);
        frame.setVisible(true);
        //frame.setBackground(Color.blue);

        Dimension pos = frame.getSize();
        int div = simulation.depos.size();
        int cyc = 1;
        for (Depo entry : simulation.depos) {
            int a = pos.width / 2;
            int b = pos.height / 2;
            int r = Math.min(a, b) * 4 / 5;
            double t = 2 * Math.PI * cyc / div;
            cyc++;

            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
            frame.addDepoVertex(entry, x, y);
        }
    }

    private static void drawSimulation() {
        simulation.runSimulation();
        while(true) {
            for (Map.Entry<String, List<Double>> entry : simulation.getPositionOfTheFluid().entrySet()) {
                int width = 0, pipeLen = 0, startX = 0, startY = 0, endX = 0, endY = 0, fuelID = 0;
                Double head = entry.getValue().get(0);
                Double tail = entry.getValue().get(1);
                List<TransportationPlan> tPlans = simulation.getTransportationPlans();
                for (int i = 0; i < tPlans.size(); i++) {
                    if (tPlans.get(i) !=null && tPlans.get(i).getTransportationID().equals(entry.getKey())) {
                        TransportationPlan plan = tPlans.get(i);
                        Graph.DepoVertex start = frame.getDepoVertexById(plan.getStartDepoID());
                        Graph.DepoVertex end = frame.getDepoVertexById(plan.getEndDepoID());
                        fuelID = plan.getFuelID();
                        startX = start.getX();
                        startY = start.getY();
                        endX = end.getX();
                        endY = end.getY();
                        for(Map.Entry<String, List<Integer>> connections : start.getDepoConnections().entrySet()) {
                            if(connections.getKey().equals(end.getDepoId())) {
                                pipeLen = connections.getValue().get(0);
                                width = connections.getValue().get(1);
                            }
                        }
                    }
                }
                frame.drawFuel(frame.getGraphics(), width, startX, startY, endX, endY, pipeLen, head, tail, fuelID);
            }
        }
    }

    public static void main(String[] args) {
        setGUIatStart();
        drawSimulation();
    }
}
