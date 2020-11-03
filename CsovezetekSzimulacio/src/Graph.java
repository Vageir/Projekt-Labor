import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class Graph extends JFrame {
    int width;
    int height;
    String windowName;
    Simulation simulation;
    ArrayList<Fuel> fuels;
    Map<String, DepoVertex> depoVertices;
    Map<String, Pipe> pipes;

    public Graph(String name, Simulation simulation) {
        this.windowName = name;
        this.setTitle(windowName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.width = 60;
        this.height = 60;
        this.simulation = simulation;
        this.fuels = new ArrayList<Fuel>();
        this.depoVertices = new HashMap<String, DepoVertex>();
        this.pipes = new HashMap<String, Pipe>();
        this.fuels.add(new Fuel(1,"Dízel", Color.yellow));
        this.fuels.add(new Fuel(2,"Benzin", Color.orange));
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
            if(entry.getDepoID().equals(id))
                return entry.getContainers();
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

    void drawLegend(Graphics g1, int x, int y) {
        Graphics2D g = (Graphics2D) g1.create();
        String title = "Jelmagyarázat";

        g.setFont(new Font("sans", Font.PLAIN, 26));
        FontMetrics f = g.getFontMetrics();
        int longest = 0;
        for(Fuel fuel : fuels) {
            if(f.stringWidth(fuel.getName()) > longest);
                longest = f.stringWidth(fuel.getName());
        }
        if(f.stringWidth(title) > longest);
            longest = f.stringWidth(title);

        int cHeight, cWidth, offset, dVX, dVY, mOffset;
        cHeight = (int) (f.getHeight() * 1.1);
        cWidth = (int) (longest * 1.1);
        offset = 1;

        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        g.transform(at);

        g.setColor(Color.black);
        g.drawRect(0, 0, cWidth, cHeight);
        g.drawString(title, f.stringWidth("/"), (int) (f.getHeight() * 0.85));

        for(Fuel fuel : fuels) {
            g.setColor(fuel.getColor());
            g.fillRect(0, cHeight * offset, cWidth, cHeight);
            g.setColor(Color.black);
            g.drawRect(0, cHeight * offset, cWidth, cHeight);
            g.drawString(fuel.getName(), f.stringWidth("/"), cHeight * offset + (int) (f.getHeight() * 0.85));

            offset++;
        }
    }

    void drawDepo(Graphics g1, Map.Entry<String, DepoVertex> depoVertex) {
        Graphics2D g = (Graphics2D) g1.create();

        g.setFont(new Font("sans", Font.PLAIN, 22));
        FontMetrics f = g.getFontMetrics();
        int longest = 0;
        for(Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            if(f.stringWidth(Integer.toString(dc.getMaxCapacity())) > longest);
                longest = f.stringWidth(Integer.toString(dc.getMaxCapacity()));
        }

        int cHeight, cWidth, offset, dVX, dVY, mOffset;
        cHeight = (int) (f.getHeight() * 1.1);
        cWidth = (int) (longest * 2.8);
        offset = 1;
        mOffset = depoVertex.getValue().getContainerVertices().size();
        dVX = depoVertex.getValue().getX()- cWidth / 2;
        dVY = depoVertex.getValue().getY()- (int) ((mOffset + offset) / 2.0 * cHeight);

        AffineTransform at = AffineTransform.getTranslateInstance(dVX, dVY);
        g.transform(at);

        g.setColor(Color.black);
        g.drawRect(0, 0, cWidth, cHeight);
        g.drawString(depoVertex.getKey(), f.stringWidth("/"), (int) (f.getHeight() * 0.85));

        for(Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            String current = Integer.toString(dc.getCurrentCapacity());
            String max = Integer.toString(dc.getMaxCapacity());
            int level = Math.round(cWidth * dc.getCurrentCapacity() / dc.getMaxCapacity());

            g.setColor(Color.white);
            g.fillRect(level, cHeight * offset, cWidth - level, cHeight);
            g.setColor(getFuelType(dc.getFuelID()-1).getColor());
            g.fillRect(0, cHeight * offset, level, cHeight);
            g.setColor(Color.black);
            g.drawRect(0, cHeight * offset, cWidth, cHeight);
            g.drawString(current + " / " + max, f.stringWidth("/"), cHeight * offset + (int) (f.getHeight() * 0.85));

            offset++;
        }
    }

    void drawFuel(Graphics g1, String leftDepo, String rightDepo, Pipe pipe, int pipeLen, double head, double tail, int fuelID) {
        Graphics2D g = (Graphics2D) g1.create();

        int startX, startY, endX, endY;
        if(leftDepo == pipe.getLeftDepo() && rightDepo == pipe.getRightDepo()) {
            startX = pipe.getLeftX();
            startY = pipe.getLeftY();
            endX = pipe.getRightX();
            endY = pipe.getRightY();
        }
        else {
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
        if(headRatio > 1.0)
            headRatio = 1.0;
        if(tailRatio > 1.0)
            tailRatio = 1.0;

        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 11)));
        g.setColor(getFuelType(fuelID-1).getColor());
        g.drawLine((int) (len * tailRatio), 0, (int) (len * headRatio), 0);

        g.setStroke(new BasicStroke((float) 2.5));
        g.setColor(Color.black);
        g.drawLine((int) (len * headRatio), -4, (int) (len * headRatio) + 3, 0);
        g.drawLine((int) (len * headRatio) + 3, 0, (int) (len * headRatio), 4);

        g.setColor(Color.white);
        g.fillOval(-6, -6, 12, 12);
        g.fillOval(len-6, -6, 12, 12);
        g.setColor(Color.black);
        g.drawOval(-6, -6, 12, 12);
        g.drawOval(len-6, -6, 12, 12);
    }

    public void paint(Graphics g) {
        drawLegend(g, (int) (getWidth() * 0.825), (int) (getHeight() * 0.05));
        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
            drawDepo(g, dv);
        }
        for (Map.Entry<String, DepoConnection> dc : simulation.getDepoConnections().entrySet()) {
            Map<Integer, List<Double>> positions = dc.getValue().getHeadAndTailOfTheFluidRelativeToLeftDepo();
            String start = dc.getValue().getLeftDepoID();
            String end = dc.getValue().getRightDepoID();
            int length = dc.getValue().getPipeLength();
            for(Map.Entry<Integer, List<Double>> pos : positions.entrySet()) {
                int fuelID = pos.getKey();
                drawFuel(g, start, end, getPipe(dc.getKey()), length,
                        pos.getValue().get(0), pos.getValue().get(1), fuelID % 100);
            }
        }
        setTitle(windowName + " at " + getCurrentTime());
    }
}
