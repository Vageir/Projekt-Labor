//import javax.swing.*;
//import java.awt.*;
//import java.awt.geom.AffineTransform;
//import java.util.*;
//import java.util.List;
//
//public class Graph extends JFrame {
//    int width;
//    int height;
//    Simulation simulation;
//    ArrayList<Fuel> fuels;
//    Map<String, DepoVertex> depoVertices;
//    Map<String, Pipe> pipes;
//
//    public Graph(String name, Simulation simulation) {
//        this.setTitle(name);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.width = 30;
//        this.height = 30;
//        this.simulation = simulation;
//        this.fuels = new ArrayList<Fuel>();
//        this.depoVertices = new HashMap<String, DepoVertex>();
//        this.pipes = new HashMap<String, Pipe>();
//        this.fuels.add(new Fuel(1,"Dízel", Color.yellow));
//        this.fuels.add(new Fuel(2,"Benzin", Color.orange));
//    }
//
//    public void loadGraph() {
//        int div = simulation.depos.size();
//        int cyc = 1;
//        for (Depo depo : simulation.depos) {
//            int a = getWidth() / 2;
//            int b = getHeight() / 2;
//            int r = Math.min(a, b) * 4 / 5;
//            double t = 2 * Math.PI * cyc / div;
//            cyc++;
//
//            int x = (int) Math.round(a + r * Math.cos(t));
//            int y = (int) Math.round(b + r * Math.sin(t));
//            addDepoVertex(depo.getDepoID(), x, y, depo.getContainers().keySet());
//        }
//        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
//            for (Depo.DepoConnection pipe : getConnectionsOfDepo(dv.getKey())) {
//                addPipe(pipe.pipeID, dv.getValue().getX(), dv.getValue().getY(),
//                        getDepoVertex(pipe.connectedDepoID).getX(), getDepoVertex(pipe.connectedDepoID).getY(),
//                        pipe.getPipeDiameter());
//            }
//        }
//    }
//
//    public Map<String, Depo.DepoContainer> getContainersOfDepo(String id) {
//        for (Depo entry : simulation.depos) {
//            if(entry.getDepoID().equals(id))
//                return entry.getContainers();
//        }
//        return null;
//    }
//
//    public List<Depo.DepoConnection> getConnectionsOfDepo(String id) {
//        for (Depo entry : simulation.depos) {
//            if(entry.getDepoID().equals(id))
//                return entry.getDepoConnections();
//        }
//        return null;
//    }
//
//    public TransportationPlan getTransportationPlan(String id) {
//        for (TransportationPlan entry : simulation.getTransportationPlans()) {
//            if(entry.getTransportationID().equals(id))
//                return entry;
//        }
//        return null;
//    }
//
//    public Depo.DepoConnection getConnectionBetween(String start, String end) {
//        for (Depo.DepoConnection entry : getConnectionsOfDepo(start)) {
//            if(entry.getConnectedDepoID().equals(end))
//                return entry;
//        }
//        return null;
//    }
//
//    class DepoVertex {
//        private final int x, y;
//        private final Map<String, ContainerVertex> containerVertices;
//
//        public DepoVertex(int x, int y, Set<String> containerNames) {
//            this.x = x;
//            this.y = y;
//            this.containerVertices = new HashMap<String, ContainerVertex>();
//
//            int div = containerNames.size();
//            int cyc = 1;
//            for (String name : containerNames) {
//                int a = 50;
//                int b = 50;
//                int r = Math.min(a, b) * 4 / 5;
//                double t = 2 * Math.PI * cyc / div;
//                cyc++;
//
//                int tmpX = (int) Math.round(a + r * Math.cos(t));
//                int tmpY = (int) Math.round(b + r * Math.sin(t));
//                this.containerVertices.put(name, new ContainerVertex(this.x + tmpX, this.y + tmpY));
//            }
//        }
//
//        public int getX() {
//            return x;
//        }
//        public int getY() {
//            return y;
//        }
//        public Map<String, ContainerVertex> getContainerVertices() {
//            return containerVertices;
//        }
//
//        class ContainerVertex {
//            private final int x, y;
//
//            public ContainerVertex(int x, int y) {
//                this.x = x;
//                this.y = y;
//            }
//
//            public int getX() {
//                return x;
//            }
//            public int getY() {
//                return y;
//            }
//        }
//    }
//
//    class Pipe {
//        private final int startX, startY, endX, endY, diameter;
//
//        public Pipe(int startX, int startY, int endX, int endY, int diameter) {
//            this.startX = startX;
//            this.startY = startY;
//            this.endX = endX;
//            this.endY = endY;
//            this.diameter = diameter;
//        }
//
//        public int getStartX() {
//            return startX;
//        }
//        public int getStartY() {
//            return startY;
//        }
//        public int getEndX() {
//            return endX;
//        }
//        public int getEndY() {
//            return endY;
//        }
//        public int getDiameter() {
//            return diameter;
//        }
//    }
//
//    class Fuel {
//        private final int id;
//        private final String name;
//        private final Color color;
//
//        public Fuel(int id, String name, Color color) {
//            this.id = id;
//            this.name = name;
//            this.color = color;
//        }
//
//        public String getName() {
//            return name;
//        }
//        public Color getColor() {
//            return color;
//        }
//    }
//
//    public void addDepoVertex(String depoID, int x, int y, Set<String> containerNames) {
//        depoVertices.put(depoID, new DepoVertex(x, y, containerNames));
//        this.repaint();
//    }
//
//    public void addPipe(String pipeID, int startX, int startY, int endX, int endY, int diameter) {
//        pipes.put(pipeID, new Pipe(startX, startY, endX, endY, diameter));
//        this.repaint();
//    }
//
//    public DepoVertex getDepoVertex(String id) {
//        return depoVertices.get(id);
//    }
//    public Pipe getPipe(String id) {
//        return pipes.get(id);
//    }
//
//    public Fuel getFuelColorAndName(int id) {
//        try {
//            return fuels.get(id);
//        } catch (IndexOutOfBoundsException e) {
//            System.err.println(e);
//        }
//        return null;
//    }
//
//    void drawPipe(Graphics g1, Pipe pipe, String name) {
//        Graphics2D g = (Graphics2D) g1.create();
//        g.setColor(Color.black);
//        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 5)));
//        //int offset;
//        g.drawLine(pipe.getStartX(), pipe.getStartY(), pipe.getEndX(), pipe.getEndY());
//    }
//
//    void drawDepo(Graphics g, Map.Entry<String, DepoVertex> depoVertex) {
//        FontMetrics f = g.getFontMetrics();
//        int nHeight = Math.max(height, f.getHeight());
//        int nWidth = Math.max(width, f.stringWidth(depoVertex.getKey()) + width / 2);
//
//        int dVX, dVY;
//        dVX = depoVertex.getValue().getX();
//        dVY = depoVertex.getValue().getY();
//
//        g.setColor(Color.white);
//        g.fillOval(dVX - nWidth / 2, dVY - nHeight / 2, nWidth, nHeight);
//        g.setColor(Color.black);
//        g.drawOval(dVX - nWidth / 2, dVY - nHeight / 2, nWidth, nHeight);
//        g.drawString(depoVertex.getKey(), dVX - f.stringWidth(depoVertex.getKey()) / 2,dVY + f.getHeight() / 2);
//
//        int div = depoVertex.getValue().getContainerVertices().size();
//        int cyc = 1;
//        for (Map.Entry<String, DepoVertex.ContainerVertex> dc : depoVertex.getValue().getContainerVertices().entrySet()) {
//            int dCX, dCY;
//            dCX = dc.getValue().getX();
//            dCY = dc.getValue().getY();
//            drawContainer(g, dCX, dCY, getContainersOfDepo(depoVertex.getKey()).get(dc.getKey()));
//        }
//    }
//
//    void drawContainer(Graphics g, int x, int y, Depo.DepoContainer depoContainer) {
//        FontMetrics f = g.getFontMetrics();
//        //System.out.println(tmp);
//        String fuelName = getFuelColorAndName(depoContainer.getFuelID()-1).getName();
//
//        int nHeight = 5 * f.getHeight();
//        int nWidth = (int) (Math.max(f.stringWidth("" + depoContainer.getMaxCapacity()), f.stringWidth(fuelName)) * 1.1);
//
//        int level = Math.round(nHeight * depoContainer.getCurrentCapacity() / depoContainer.getMaxCapacity());
//        g.setColor(Color.white);
//        g.fillRect(x - nWidth / 2, y - nHeight / 2, nWidth, nHeight - level);
//        g.setColor(getFuelColorAndName(depoContainer.getFuelID()-1).getColor());
//        g.fillRect(x - nWidth / 2, y + nHeight / 2 - level, nWidth, level);
//
//        g.setColor(Color.black);
//        g.drawRect(x - nWidth / 2, y - nHeight / 2, nWidth, nHeight);
//        g.drawString("" + depoContainer.getCurrentCapacity(),
//                x - f.stringWidth("" + depoContainer.getCurrentCapacity()) / 2, y - f.getHeight() * 3 / 2);
//        g.drawString("/", x - f.stringWidth("/") / 2, y - f.getHeight() / 2);
//        g.drawString("" + depoContainer.getMaxCapacity(),
//                x - f.stringWidth("" + depoContainer.getMaxCapacity()) / 2, y + f.getHeight() / 2);
//        g.drawString("" + depoContainer.getMaxCapacity(),
//                x - f.stringWidth("" + depoContainer.getMaxCapacity()) / 2, y + f.getHeight() / 2);
//        g.drawString(fuelName,x - f.stringWidth(fuelName) / 2, y + f.getHeight() * 2);
//    }
//
//    void drawFuel(Graphics g1, DepoVertex start, DepoVertex end, Pipe pipe, int pipeLen, double head, double tail, int fuelID) {
//        Graphics2D g = (Graphics2D) g1.create();
//
//        int startX, startY, endX, endY;
//        startX = start.getX();
//        startY = start.getY();
//        endX = end.getX();
//        endY = end.getY();
//        double dx = endX - startX, dy = endY - startY;
//        double angle = Math.atan2(dy, dx);
//        AffineTransform at = AffineTransform.getTranslateInstance(startX, startY);
//        at.concatenate(AffineTransform.getRotateInstance(angle));
//        g.transform(at);
//
//        int len = (int) Math.sqrt(dx * dx + dy * dy);
//        double headRatio = head / pipeLen;
//        double tailRatio = tail / pipeLen;
//
//        g.setStroke(new BasicStroke((float) (pipe.getDiameter() * 4)));
//        g.setColor(getFuelColorAndName(fuelID-1).getColor());
//
//        //int offset;
//        g.drawLine((int) (len * tailRatio), 0, (int) (len * headRatio), 0);
//    }
//
//    public void paint(Graphics g) {
//        for (Map.Entry<String, Pipe> pipe : pipes.entrySet()) {
//            drawPipe(g, pipe.getValue(), pipe.getKey());
//        }
//        for (Map.Entry<String, DepoVertex> dv : depoVertices.entrySet()) {
//            drawDepo(g, dv);
//        }
//        for (Map.Entry<String, List<Double>> entry : simulation.getPositionOfTheFluid().entrySet()) {
//            Double head = entry.getValue().get(0);
//            Double tail = entry.getValue().get(1);
//            TransportationPlan tPlan = getTransportationPlan(entry.getKey());
//            DepoVertex start = getDepoVertex(tPlan.getStartDepoID());
//            DepoVertex end = getDepoVertex(tPlan.getEndDepoID());
//            int fuelID = tPlan.getFuelID();
//            Depo.DepoConnection conn = getConnectionBetween(tPlan.getStartDepoID(), tPlan.getEndDepoID());
//            if(tail != head)
//                drawFuel(g, start, end, getPipe(conn.pipeID), conn.getPipeLength(), head, tail, fuelID);
//        }
//    }
//}
