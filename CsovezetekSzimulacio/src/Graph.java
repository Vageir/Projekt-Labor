import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.*;

public class Graph extends JFrame {
    int width;
    int height;
    String windowName;
    Simulation simulation;
    ArrayList<Fuel> fuels;
    Map<String, DepoVertex> depoVertices;
    Map<String, Pipe> pipes;

    public Graph(String windowName, Simulation simulation) {
        this.windowName = windowName;
        this.setTitle(windowName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.width = 60;
        this.height = 60;
        this.simulation = simulation;
        this.fuels = new ArrayList<Fuel>();
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
                //innentől színtévesztőknek rosszabb színek
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
            this.fuels.add(new Fuel(Integer.parseInt(result.get(i).get(0)), result.get(i).get(1), colors[i]));
        }
    }

    public void loadGraph() {
        int div = simulation.getDepos().size();
        int cyc = 1;
        for (Depo depo : simulation.getDepos()) {
            int a = getWidth() / 2;
            int b = getHeight() / 2;
            int r = Math.min(a, b) * 4 / 5;
            double t = 2 * Math.PI * cyc / div;
            cyc++;

            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
            addDepoVertex(depo.getDepoID(), x, y, depo.getContainers().keySet());
        }

        for (Map.Entry<String, DepoConnection> dc : simulation.getDepoConnections().entrySet()) {
            DepoConnection pipe = dc.getValue();
            String leftDepoID = pipe.getLeftDepoID();
            String rightDepoID = pipe.getRightDepoID();
            int leftX = getDepoVertex(leftDepoID).getX();
            int leftY = getDepoVertex(leftDepoID).getY();
            int rightX = getDepoVertex(rightDepoID).getX();
            int rightY = getDepoVertex(rightDepoID).getY();

            double ratio = 0.25;
            int distLeftX, distLeftY, distRightX, distRightY;
            distLeftX = leftX - getWidth() / 2;
            distLeftY = leftY - getHeight() / 2;
            leftX -= (int) (distLeftX * ratio);
            leftY -= (int) (distLeftY * ratio);
            distRightX = rightX - getWidth() / 2;
            distRightY = rightY - getHeight() / 2;
            rightX -= (int) (distRightX * ratio);
            rightY -= (int) (distRightY * ratio);

            addPipe(dc.getKey(), leftX, leftY, rightX, rightY, pipe.getPipeDiameter(), leftDepoID, rightDepoID);
        }
    }

    public Map<String, Depo.DepoContainer> getContainersOfDepo(String id) {
        for (Depo entry : simulation.getDepos()) {
            if (entry.getDepoID().equals(id))
                return entry.getContainers();
        }
        return null;
    }

    public Depo getDepo(String id) {
        for (Depo entry : simulation.getDepos()) {
            if (entry.getDepoID().equals(id))
                return entry;
        }
        return null;
    }

    public String getCurrentTime() {
        String hh, mm;
        hh = Integer.toString(simulation.getCurrentHours());
        mm = Integer.toString(simulation.getCurrentMinutes());
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
        private final ArrayList<String> containerVertices;

        public DepoVertex(int x, int y, Set<String> containerNames) {
            this.x = x;
            this.y = y;
            this.containerVertices = new ArrayList<String>();
            for (String name : containerNames) {
                this.containerVertices.add(name);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ArrayList<String> getContainerVertices() {
            return containerVertices;
        }
    }

    class Pipe {
        private final int leftX, leftY, rightX, rightY, diameter;
        private final String leftDepo, rightDepo;

        public Pipe(int leftX, int leftY, int rightX, int rightY, int diameter, String leftDepo, String rightDepo) {
            this.leftX = leftX;
            this.leftY = leftY;
            this.rightX = rightX;
            this.rightY = rightY;
            this.diameter = diameter;
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

        public String getLeftDepo() {
            return leftDepo;
        }

        public String getRightDepo() {
            return rightDepo;
        }
    }

    class Fuel {
        private final int id;
        private final String name;
        private final Color color;

        public Fuel(int id, String name, Color color) {
            this.id = id;
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

    public void addDepoVertex(String depoID, int x, int y, Set<String> containerNames) {
        depoVertices.put(depoID, new DepoVertex(x, y, containerNames));
        this.repaint();
    }

    public void addPipe(String pipeID, int leftX, int leftY, int rightX, int rightY, int diameter, String leftDepo, String rightDepo) {
        pipes.put(pipeID, new Pipe(leftX, leftY, rightX, rightY, diameter, leftDepo, rightDepo));
    }

    public DepoVertex getDepoVertex(String id) {
        return depoVertices.get(id);
    }

    public Pipe getPipe(String id) {
        return pipes.get(id);
    }

    public Fuel getFuelType(int id) {
        try {
            return fuels.get(id);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e);
        }
        return null;
    }

    void drawLegend(Graphics g, int x, int y) {
        g.setFont(new Font("sans", Font.PLAIN, 26));
        FontMetrics f = g.getFontMetrics();

        String title = "Jelmagyarázat";
        int tmp = f.stringWidth(title);
        for (Fuel fuel : fuels) {
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

        for (Fuel fuel : fuels) {
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

        g.setFont(new Font("sans", Font.PLAIN, 22));
        FontMetrics f = g.getFontMetrics();

        int tmp = f.stringWidth(depoVertex.getKey());
        for (Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            if (f.stringWidth(Integer.toString(dc.getMaxCapacity())) > tmp)
                tmp = f.stringWidth(Integer.toString(dc.getMaxCapacity()));
        }
        int cHeight, cWidth, offset, mOffset;
        cHeight = (int) (f.getHeight() * 1.1);
        cWidth = (int) (tmp * 2.4);
        offset = 1;
        mOffset = depoVertex.getValue().getContainerVertices().size();

        int fHeight = mOffset * cHeight;

        AffineTransform at = AffineTransform.getTranslateInstance(
                depoVertex.getValue().getX() - cWidth / 2, depoVertex.getValue().getY() - fHeight);
        g.transform(at);

        for (Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            String current = Integer.toString(dc.getCurrentCapacity());
            String max = Integer.toString(dc.getMaxCapacity());
            int level = Math.round(cWidth * dc.getCurrentCapacity() / dc.getMaxCapacity());

            g.setColor(Color.gray);
            g.fillRect(level, cHeight * offset, cWidth - level, cHeight);
            g.setColor(getFuelType(dc.getFuelID() - 1).getColor());
            g.fillRect(0, cHeight * offset, level, cHeight);
            g.setColor(Color.black);
            g.drawRect(0, cHeight * offset, cWidth, cHeight);
            g.setColor(getContrastColor(getFuelType(dc.getFuelID() - 1).getColor()));
            g.drawString(current + " / " + max, f.stringWidth("/"), cHeight * offset + (int) (f.getHeight() * 0.85));

            offset++;
        }

        g.setColor(Color.black);
        g.drawString(depoVertex.getKey(), (cWidth - f.stringWidth(depoVertex.getKey())) / 2, (int) (f.getHeight() * 0.85));
        int cSide = (int) Math.sqrt(Math.pow(fHeight, 2) + Math.pow(cWidth, 2));
        g.setStroke(new BasicStroke((float) 2.5));
        g.drawOval((cWidth - cSide) / 2, (cHeight * ++offset - cSide) / 2, cSide, cSide);

        g.dispose();
    }

    void drawFuel(Graphics g1, String leftDepo, String rightDepo, Pipe pipe, int pipeLen, Map.Entry<Integer, List<Double>> pos) {
        Graphics2D g = (Graphics2D) g1.create();

        int fuelID = pos.getKey() % 100;
        double head, tail;
        head = pos.getValue().get(0);
        tail = pos.getValue().get(1);

        int startX, startY, endX, endY;
        if (leftDepo == pipe.getLeftDepo() && rightDepo == pipe.getRightDepo()) {
            startX = pipe.getLeftX();
            startY = pipe.getLeftY();
            endX = pipe.getRightX();
            endY = pipe.getRightY();
        } else {
            startX = pipe.getRightX();
            startY = pipe.getRightY();
            endX = pipe.getLeftX();
            endY = pipe.getLeftY();
        }
        double dx = endX - startX, dy = endY - startY;
        double angle = Math.atan2(dy, dx);
        AffineTransform at = AffineTransform.getTranslateInstance(startX, startY);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        int len = (int) Math.sqrt(dx * dx + dy * dy);
        double headRatio = head / pipeLen;
        double tailRatio = tail / pipeLen;
        if (headRatio > 1.0)
            headRatio = 1.0;
        if (tailRatio > 1.0)
            tailRatio = 1.0;


        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 11)));
        g.setColor(getFuelType(fuelID - 1).getColor());
        g.drawLine((int) (len * tailRatio), 0, (int) (len * headRatio), 0);

        g.setStroke(new BasicStroke((float) 2.5));
        g.setColor(getContrastColor(getFuelType(fuelID - 1).getColor()));
        g.drawLine((int) (len * headRatio), -4, (int) (len * headRatio) + 3, 0);
        g.drawLine((int) (len * headRatio) + 3, 0, (int) (len * headRatio), 4);

        g.setColor(Color.white);
        g.fillOval(-6, -6, 12, 12);
        g.fillOval(len - 6, -6, 12, 12);
        g.setColor(Color.black);
        g.drawOval(-6, -6, 12, 12);
        g.drawOval(len - 6, -6, 12, 12);

        g.dispose();
    }

    public void paint(Graphics g) {
        drawLegend(g, getWidth(), 0);
        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
            drawDepo(g, dv);
        }
        for (Map.Entry<String, DepoConnection> dc : simulation.getDepoConnections().entrySet()) {
            Map<Integer, List<Double>> positions = dc.getValue().getHeadAndTailOfTheFluidRelativeToLeftDepo();
            String start = dc.getValue().getLeftDepoID();
            String end = dc.getValue().getRightDepoID();
            int length = dc.getValue().getPipeLength();
            for (Map.Entry<Integer, List<Double>> pos : positions.entrySet()) {
                drawFuel(g, start, end, getPipe(dc.getKey()), length, pos);
            }
        }
        setTitle(windowName + ": " + getCurrentTime());
    }
}
