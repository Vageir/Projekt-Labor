import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationGui {
    private static Simulation simulation;
    private static Graph frame;

    private static void setGUIatStart() {
        simulation = new Simulation();

        frame = new Graph("Depos and pipes");
        frame.setSize(700,700);
        frame.setVisible(true);

        Dimension pos = frame.getSize();
        int div = simulation.depos.size();
        int cyc = 1;
        for (Depo entry : simulation.depos){
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
    public static void main(String[] args) {
        setGUIatStart();
    }
}
