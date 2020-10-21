import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationGui {
    public static void main(String[] args) {
        Graph frame = new Graph("Telephelyek és csövek");
        frame.setSize(500,500);
        frame.setVisible(true);

        LinkedHashMap<Integer,ArrayList<String>> result;
        result = new DataBaseHandler().readRecords("depo");
        Dimension pos = frame.getSize();
        int div = result.size();
        int cyc = 0;
        for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()){
            int x, y;
            x = pos.width * cyc / div;
            y = (int) (pos.height * 1 - Math.cos(cyc / div));
            frame.addNode(entry.getValue().get(0), x, y);
            cyc++;
        }

        result = new DataBaseHandler().readRecords("connecteddepos");
        for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()){
            frame.addEdge(frame.findNode(entry.getValue().get(0)),frame.findNode(entry.getValue().get(1)));
            //frame.addEdge(frame.findNode(entry.getValue().get(1)),frame.findNode(entry.getValue().get(0)));
        }

        //nem muxik, nemtom hogy kell a Graphics-al dolgozni
        frame.recolorEdge("BSA","DSA", Color.YELLOW);
    }
}
