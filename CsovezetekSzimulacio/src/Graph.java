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

            double ratio = 0.15;
            int distLeftX, distLeftY, distRightX, distRightY;
            distLeftX = leftX - getWidth() / 2;
            distLeftY = leftY - getHeight() / 2;
            leftX -= (int) (distLeftX * ratio);
            leftY -= (int) (distLeftY * ratio);
            distRightX = rightX - getWidth() / 2;
            distRightY = rightY - getHeight() / 2;
            rightX -= (int) (distRightX * ratio);
            rightY -= (int) (distRightY * ratio);

            addPipe(dc.getKey(),
                    leftX, leftY, rightX, rightY, pipe.getPipeDiameter(), leftDepoID, rightDepoID);
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
        private final int containersX, containersY;
        private final ArrayList<String> containerVertices;

        public DepoVertex(int x, int y, Set<String> containerNames) {
            this.x = x;
            this.y = y;
            this.containerVertices = new ArrayList<String>();
            for (String name : containerNames) {
                this.containerVertices.add(name);
            }
            double ratio = 0.33;
            int distX, distY;
            distX = this.x - getWidth() / 2;
            distY = this.y - getHeight() / 2;
            this.containersX = this.x + (int) (distX * ratio);
            this.containersY = this.y + (int) (distY * ratio);
//            System.out.println("centX: " + getWidth() / 2 + ", centY: " + getHeight() / 2);
//            System.out.println("origX: " + getX() + ", newX: " + getContainersX());
//            System.out.println("origY: " + getY() + ", newY: " + getContainersY());
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getContainersX() {
            return containersX;
        }
        public int getContainersY() {
            return containersY;
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
        this.repaint();
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

    void drawDepo(Graphics g, Map.Entry<String, DepoVertex> depoVertex) {
        g.setFont(new Font("sans", Font.PLAIN, 22));
        FontMetrics f = g.getFontMetrics();
        int nHeight = Math.max(height, f.getHeight());
        int nWidth = Math.max(width, f.stringWidth(depoVertex.getKey()) + width / 2);

        int dVX, dVY;
        dVX = depoVertex.getValue().getX();
        dVY = depoVertex.getValue().getY();

        g.setColor(Color.white);
        g.fillOval(dVX - nWidth / 2, dVY - nHeight / 2, nWidth, nHeight);
        g.setColor(Color.black);
        g.drawOval(dVX - nWidth / 2, dVY - nHeight / 2, nWidth, nHeight);
        g.drawString(depoVertex.getKey(), dVX - f.stringWidth(depoVertex.getKey()) / 2,dVY + f.getHeight() / 2);

        int longest = 0;
        for(Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            if(f.stringWidth(Integer.toString(dc.getMaxCapacity())) > longest);
                longest = f.stringWidth(Integer.toString(dc.getMaxCapacity()));
        }
        int cHeight, cWidth, offset, dCX, dCY, mOffset;
        cHeight = (int) (f.getHeight() * 1.1);
        cWidth = (int) (longest * 2.8);
        offset = 0;
        mOffset = depoVertex.getValue().getContainerVertices().size();
        dCX = depoVertex.getValue().getContainersX() - cWidth / 2;
        dCY = depoVertex.getValue().getContainersY() - (int) (mOffset / 2.0 * cHeight);

        for(Depo.DepoContainer dc : getContainersOfDepo(depoVertex.getKey()).values()) {
            String current = Integer.toString(dc.getCurrentCapacity());
            String max = Integer.toString(dc.getMaxCapacity());
            int level = Math.round(cWidth * dc.getCurrentCapacity() / dc.getMaxCapacity());

            g.setColor(Color.white);
            g.fillRect(dCX + level, dCY + cHeight * offset, cWidth - level, cHeight);
            g.setColor(getFuelType(dc.getFuelID()-1).getColor());
            g.fillRect(dCX, dCY + cHeight * offset, level, cHeight);
            g.setColor(Color.black);
            g.drawRect(dCX, dCY + cHeight * offset, cWidth, cHeight);
            g.drawString(current + " / " + max,dCX + f.stringWidth("/"), dCY  + cHeight * offset + (int) (f.getHeight() * 0.85));

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

        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 6)));
        g.setColor(getFuelType(fuelID-1).getColor());

        g.drawLine((int) (len * tailRatio), 0, (int) (len * headRatio), 0);
    }

    public void paint(Graphics g) {
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
