import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.*;

public class Graph extends JFrame {
    int width, height, depoFontSize, legendFontSize;
    String windowName;
    Simulation simulation;
    HashMap<Integer, Fuel> fuels;
    Map<String, DepoVertex> depoVertices;
    Map<String, Pipe> pipes;

    public Graph(String windowName, Simulation simulation) {
        this.windowName = windowName;
        this.setTitle(windowName);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        this.setLocationRelativeTo(null);
        this.width = 60;
        this.height = 60;
        this.depoFontSize = 22;
        this.legendFontSize = 26;
        this.simulation = simulation;
        this.fuels = new HashMap<Integer, Fuel>();
        this.depoVertices = new HashMap<String, DepoVertex>();
        this.pipes = new HashMap<String, Pipe>();

        Color[] colors = {
                Color.decode("0xFFB300"),    // Vivid Yellow
                Color.decode("0xA6BDD7"),    // Very Light Blue
                Color.decode("0x803E75"),    // Strong Purple
                Color.decode("0xFF6800"),    // Vivid Orange
                Color.decode("0xC10020"),    // Vivid Red
                Color.decode("0xCEA262"),    // Grayish Yellow
                Color.decode("0x817066"),    // Medium Gray
                //innentől színtévesztőknek rosszabb színek, stackoverflow szerint
                Color.decode("0x007D34"),    // Vivid Green
                Color.decode("0xF6768E"),    // Strong Purplish Pink
                Color.decode("0x00538A"),    // Strong Blue
                Color.decode("0xFF7A5C"),    // Strong Yellowish Pink
                Color.decode("0x53377A"),    // Strong Violet
                Color.decode("0xFF8E00"),    // Vivid Orange Yellow
                Color.decode("0xB32851"),    // Strong Purplish Red
                Color.decode("0xF4C800"),    // Vivid Greenish Yellow
                Color.decode("0x7F180D"),    // Strong Reddish Brown
                Color.decode("0x93AA00"),    // Vivid Yellowish Green
                Color.decode("0x593315"),    // Deep Yellowish Brown
                Color.decode("0xF13A13"),    // Vivid Reddish Orange
                Color.decode("0x232C16"),    // Dark Olive Green
        };
        LinkedHashMap<Integer, ArrayList<String>> result = new DataBaseHandler().readRecords("fuel");
        for (int i = 0; i < result.size(); i++) {
            addFuelType(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), colors[i]);
        }
    }

    public void loadGraph() {
        int limit = simulation.getDepos().size();
        Map<String, Map<String, Integer>> attributes = new HashMap<>();
        int count = 1;
        for (Depo depo : simulation.getDepos()) {
            int a = getWidth() / 2;
            int b = getHeight() / 2;
            int r = Math.min(a, b) * 4 / 5;
            double t = 2 * Math.PI * count / limit;
            count++;

            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));

            List<Map.Entry<String, Depo.DepoContainer>> toSort = new ArrayList<>();
            for (Map.Entry<String, Depo.DepoContainer> stringDepoContainerEntry : depo.getContainers().entrySet()) {
                toSort.add(stringDepoContainerEntry);
            }
            toSort.sort(Comparator.comparingInt(e -> e.getValue().getFuelID()));
            LinkedHashSet<String> cont = new LinkedHashSet<>();
            for (Map.Entry<String, Depo.DepoContainer> stringDepoContainerEntry : toSort) {
                cont.add(stringDepoContainerEntry.getKey());
            }

            addDepoVertex(depo.getDepoID(), x, y, cont);
            attributes.put(depo.getDepoID(), getDepoVertexAttributes(getGraphics(), getDepoVertexEntry(depo.getDepoID())));
        }

        Set<Map<String, DepoConnection>> pipeList = getPipesBetweenSameDepos();
        for (Map<String, DepoConnection> list : pipeList) {
            count = 0;
            for (Map.Entry<String, DepoConnection> conn : list.entrySet()) {
                String leftDepo = conn.getValue().getLeftDepoID();
                String rightDepo = conn.getValue().getRightDepoID();

                Map<String, Integer> leftAttr = attributes.get(leftDepo);
                Map<String, Integer> rightAttr = attributes.get(rightDepo);

                int leftX = leftAttr.get("cCenterX"), leftY = leftAttr.get("cCenterY");
                int rightX = rightAttr.get("cCenterX"), rightY = rightAttr.get("cCenterY");
                double dx = rightX - leftX, dy = rightY - leftY;
                int step = count;
                if (count != 0 && count % 2 == 0)
                    step--;
                int offsetX = (int) (Math.pow(-1, count) * step * dy * 0.05);
                int offsetY = (int) (Math.pow(-1, count) * step * -dx * 0.05);

                leftX += offsetX;
                rightX += offsetX;
                leftY += offsetY;
                rightY += offsetY;

                int len = (int) Math.hypot(dx, dy) - (leftAttr.get("cDiameter") + rightAttr.get("cDiameter")) / 2;
                int dia = conn.getValue().getPipeDiameter();

                count++;
                addPipe(conn.getKey(), leftX, leftY, rightX, rightY, dia, len, leftDepo, rightDepo);
            }
        }

        initPaint(getGraphics());
    }

    public Map<String, Integer> getDepoVertexAttributes(Graphics g, Map.Entry<String, DepoVertex> depoVertex) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        FontMetrics f = g.getFontMetrics(new Font("sans", Font.PLAIN, depoFontSize));

        int tmp = f.stringWidth(depoVertex.getKey());
        for (Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            if (f.stringWidth(Integer.toString(dc.getMaxCapacity())) > tmp)
                tmp = f.stringWidth(Integer.toString(dc.getMaxCapacity()));
        }

        int rHeight = (int) (f.getHeight() * 1.1);
        int fWidth = (int) (tmp * 2.4);
        int mOffset = depoVertex.getValue().getContainers().size();
        int fHeight = mOffset * rHeight;
        int cDiameter = (int) Math.hypot(fHeight, fWidth);
        int cCenterX = depoVertex.getValue().getX();
        int cCenterY = depoVertex.getValue().getY();

        result.put("rHeight", rHeight);
        result.put("fWidth", fWidth);
        result.put("mOffset", mOffset);
        result.put("fHeight", fHeight);
        result.put("cDiameter", cDiameter);
        result.put("cCenterX", cCenterX);
        result.put("cCenterY", cCenterY);
        return result;
    }

    public Set<Map<String, DepoConnection>> getPipesBetweenSameDepos() {
        Set<Map<String, DepoConnection>> result = new HashSet<>();

        for (DepoConnection conn : simulation.getDepoConnections().values()) {
            Map<String, DepoConnection> tmp = new HashMap<String, DepoConnection>();
            String left, right;
            left = conn.getLeftDepoID();
            right = conn.getRightDepoID();

            for (Map.Entry<String, DepoConnection> fin : simulation.getDepoConnections().entrySet()) {
                if (fin.getValue().getLeftDepoID().equals(left) && fin.getValue().getRightDepoID().equals(right) ||
                        fin.getValue().getRightDepoID().equals(left) && fin.getValue().getLeftDepoID().equals(right)) {
                    tmp.put(fin.getKey(), fin.getValue());
                }
            }
            result.add(tmp);
        }
        return result;
    }

    public Map<String, Depo.DepoContainer> getContainersOfDepo(String id) {
        for (Depo entry : simulation.getDepos()) {
            if (entry.getDepoID().equals(id))
                return entry.getContainers();
        }
        return null;
    }

    public Depo.DepoContainer getContainer(String id) {
        for (Depo depo : simulation.getDepos()) {
            for (Map.Entry<String, Depo.DepoContainer> entry : depo.getContainers().entrySet()) {
                if (entry.getKey().equals(id))
                    return entry.getValue();
            }
        }
        return null;
    }

    public String getCurrentTime() {
        String hh, mm;
        hh = Integer.toString(simulation.getCurrentHours());
        mm = Integer.toString(simulation.getCurrentMinutes());
        if (mm.equals("60")) {
            mm = "59";
            //mm = "00";
            //hh = Integer.toString(simulation.getCurrentHours() + 1);
        }
        if (hh.length() == 1)
            hh = "0" + hh;
        if (mm.length() == 1)
            mm = "0" + mm;
        return hh + ":" + mm;
    }

    public Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

    class DepoVertex {
        private final int x, y;
        private final ArrayList<String> containers;

        public DepoVertex(int x, int y, Set<String> containerNames) {
            this.x = x;
            this.y = y;
            this.containers = new ArrayList<String>();
            for (String name : containerNames) {
                this.containers.add(name);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ArrayList<String> getContainers() {
            return containers;
        }
    }

    class Pipe {
        private final int leftX, leftY, rightX, rightY, diameter, length;
        private final String leftDepo, rightDepo;

        public Pipe(int leftX, int leftY, int rightX, int rightY, int diameter, int length, String leftDepo, String rightDepo) {
            this.leftX = leftX;
            this.leftY = leftY;
            this.rightX = rightX;
            this.rightY = rightY;
            this.diameter = diameter;
            this.length = length;
            this.leftDepo = leftDepo;
            this.rightDepo = rightDepo;
        }

        public int getLeftX() {
            return leftX;
        }

        public int getLeftY() {
            return leftY;
        }

        public int getRightX() {
            return rightX;
        }

        public int getRightY() {
            return rightY;
        }

        public int getDiameter() {
            return diameter;
        }

        public int getLength() {
            return length;
        }

        public String getLeftDepo() {
            return leftDepo;
        }

        public String getRightDepo() {
            return rightDepo;
        }
    }

    class Fuel {
        private final String name;
        private final Color color;

        public Fuel(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }

    public void addDepoVertex(String depoID, int x, int y, LinkedHashSet<String> containerNames) {
        depoVertices.put(depoID, new DepoVertex(x, y, containerNames));
        this.repaint();
    }

    public void addPipe(String pipeID, int leftX, int leftY, int rightX, int rightY, int diameter, int length, String leftDepo, String rightDepo) {
        pipes.put(pipeID, new Pipe(leftX, leftY, rightX, rightY, diameter, length, leftDepo, rightDepo));
    }

    public void addFuelType(int fuelId, String name, Color color) {
        fuels.put(fuelId, new Fuel(name, color));
    }

    public DepoVertex getDepoVertex(String id) {
        return depoVertices.get(id);
    }

    public Map.Entry<String, DepoVertex> getDepoVertexEntry(String id) {
        for (Map.Entry<String, DepoVertex> entry : depoVertices.entrySet())
            if (entry.getKey().equals(id))
                return entry;
        return null;
    }

    public Pipe getPipe(String id) {
        return pipes.get(id);
    }

    public Fuel getFuelType(int id) {
        return fuels.get(id);
    }

    void drawLegend(Graphics g, int x, int y) {
        g.setFont(new Font("sans", Font.PLAIN, legendFontSize));
        FontMetrics f = g.getFontMetrics();

        String title = "Jelmagyarázat";
        int tmp = f.stringWidth(title);
        for (Fuel fuel : fuels.values()) {
            if (f.stringWidth(fuel.getName()) > tmp)
                tmp = f.stringWidth(fuel.getName());
        }
        int cHeight, cWidth, offset;
        cHeight = (int) (f.getHeight() * 1.1);
        cWidth = (int) (tmp * 1.1);
        offset = 1;

        x -= cWidth + f.getHeight();
        y += cHeight + f.getHeight();
        g.setColor(Color.black);
        g.drawRect(x, y, cWidth, cHeight);
        g.drawString(title, x + f.stringWidth("/"), y + (int) (f.getHeight() * 0.85));

        for (Fuel fuel : fuels.values()) {
            g.setColor(fuel.getColor());
            g.fillRect(x, y + cHeight * offset, cWidth, cHeight);

            g.setColor(Color.black);
            g.drawRect(x, y + cHeight * offset, cWidth, cHeight);

            g.setColor(getContrastColor(fuel.getColor()));
            g.drawString(fuel.getName(), x + f.stringWidth("/"), y + cHeight * offset + (int) (f.getHeight() * 0.85));

            offset++;
        }
    }

    void drawDepo(Graphics g1, Map.Entry<String, DepoVertex> depoVertex) {
        Graphics2D g = (Graphics2D) g1.create();
        g.setFont(new Font("sans", Font.PLAIN, depoFontSize));
        FontMetrics f = g.getFontMetrics();

        Map<String, Integer> attr = getDepoVertexAttributes(g1, depoVertex);
        g.setColor(Color.black);
        g.drawString(depoVertex.getKey(),
                attr.get("cCenterX") - f.stringWidth(depoVertex.getKey()) / 2,
                attr.get("cCenterY") - attr.get("cDiameter") / 4);
        g.setStroke(new BasicStroke((float) 2.5));
        g.drawOval(attr.get("cCenterX") - attr.get("cDiameter") / 2,
                attr.get("cCenterY") - attr.get("cDiameter") / 2,
                attr.get("cDiameter"), attr.get("cDiameter"));

        int tmp = f.stringWidth(depoVertex.getKey());
        for (Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            if (f.stringWidth(Integer.toString(dc.getMaxCapacity())) > tmp)
                tmp = f.stringWidth(Integer.toString(dc.getMaxCapacity()));
        }

        AffineTransform at = AffineTransform.getTranslateInstance(
                depoVertex.getValue().getX() - attr.get("fWidth") / 2,
                depoVertex.getValue().getY() - attr.get("fHeight"));
        g.transform(at);
        g.setStroke(new BasicStroke((float) 1));

        int offset = 1;
        for (String dcName : depoVertex.getValue().getContainers()) {
            Depo.DepoContainer dc = getContainer(dcName);
            String current = Integer.toString(dc.getCurrentCapacity());
            String max = Integer.toString(dc.getMaxCapacity());
            int level = Math.round(attr.get("fWidth") * dc.getCurrentCapacity() / dc.getMaxCapacity());

            g.setColor(Color.gray);
            g.fillRect(level, attr.get("rHeight") * offset, attr.get("fWidth") - level, attr.get("rHeight"));
            g.setColor(getFuelType(dc.getFuelID()).getColor());
            g.fillRect(0, attr.get("rHeight") * offset, level, attr.get("rHeight"));
            g.setColor(Color.black);
            g.drawRect(0, attr.get("rHeight") * offset, attr.get("fWidth"), attr.get("rHeight"));
            g.setColor(getContrastColor(getFuelType(dc.getFuelID()).getColor()));
            g.drawString(current + " / " + max, f.stringWidth("/"),
                    attr.get("rHeight") * offset + (int) (f.getHeight() * 0.85));

            offset++;
        }

        g.dispose();
    }

    void drawFuel(Graphics g1, String start, Pipe pipe, int pipeLen, Map.Entry<Integer, List<Double>> pos) {
        Graphics2D g = (Graphics2D) g1.create();

        int leftX, leftY, rightX, rightY;
        leftX = pipe.getLeftX();
        leftY = pipe.getLeftY();
        rightX = pipe.getRightX();
        rightY = pipe.getRightY();

        if (pipe.getRightDepo().equals(start)) {
            int tmp = leftX;
            leftX = rightX;
            rightX = tmp;
            tmp = leftY;
            leftY = rightY;
            rightY = tmp;
        }

        double dx = rightX - leftX, dy = rightY - leftY;
        double angle = Math.atan2(dy, dx);

        AffineTransform at = AffineTransform.getTranslateInstance(leftX, leftY);
        at.rotate(angle);
        g.transform(at);

        double head, tail;
        head = pos.getValue().get(0);
        tail = pos.getValue().get(1);
        int fuelID = pos.getKey() % 100;

        int len = (int) (pipe.getLength() * 0.95);
        int correction = (int) (Math.hypot(dx, dy) - len) / 2;

        double headRatio = head / pipeLen;
        double tailRatio = tail / pipeLen;
        if (headRatio > 0.99)
            headRatio = 1.0;
        if (tailRatio > 0.99)
            tailRatio = 1.0;

        AffineTransform at2 = AffineTransform.getTranslateInstance(correction, 0);
        g.transform(at2);

        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 11)));
        g.setColor(getFuelType(fuelID).getColor());
        g.drawLine((int) (len * tailRatio), 0, (int) (len * headRatio), 0);

        g.setStroke(new BasicStroke((float) 2.5));
        g.setColor(getContrastColor(getFuelType(fuelID).getColor()));
        g.drawLine((int) (len * headRatio), -4, (int) (len * headRatio) + 3, 0);
        g.drawLine((int) (len * headRatio) + 3, 0, (int) (len * headRatio), 4);

        g.dispose();
    }

    public void initPaint(Graphics g) {
        drawLegend(g, getWidth(), 0);
        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
            drawDepo(g, dv);
        }

        for (Map.Entry<String, DepoConnection> depoConn : simulation.getDepoConnections().entrySet()) {
            Map<Integer, List<Double>> positions = depoConn.getValue().getHeadAndTailOfTheFluidRelativeToLeftDepo();
            int length = depoConn.getValue().getPipeLength();
            String start = depoConn.getValue().getLeftDepoID();

            for (Map.Entry<Integer, List<Double>> pos : positions.entrySet()) {
                drawFuel(g, start, getPipe(depoConn.getKey()), length, pos);
            }
        }
    }

    public void paint(Graphics g) {
        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
            drawDepo(g, dv);
        }

        for (TransportationPlan tPlan : simulation.getTransportationPlans()) {
            if(tPlan.isTimeToRun() && !tPlan.isFinished()) {
                DepoConnection depoConn = simulation.getDepoConnections().get(tPlan.getPipeID());
                Map<Integer, List<Double>> positions = depoConn.getHeadAndTailOfTheFluidRelativeToLeftDepo();
                int length = depoConn.getPipeLength();
                String start = tPlan.getStartDepoID();

                for (Map.Entry<Integer, List<Double>> pos : positions.entrySet()) {
                    if(!pos.getValue().get(0).equals(pos.getValue().get(1)))
                        drawFuel(g, start, getPipe(tPlan.getPipeID()), length, pos);
                }
            }
        }
        setTitle(windowName + ": " + getCurrentTime());
    }
}
