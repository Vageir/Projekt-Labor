import javax.swing.*;
import java.awt.*;
//import java.awt.geom.AffineTransform;
import java.util.*;
import java.awt.Color;
import java.util.List;

public class Graph extends JFrame {
        int width;
        int height;

        ArrayList<DepoVertex> depoVertices;

        public Graph(String name) {
            this.setTitle(name);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            depoVertices = new ArrayList<DepoVertex>();
            width = 30;
            height = 30;
        }

        class DepoVertex {
            private Depo depo;
            private final int x,y;

            public DepoVertex(Depo depo, int x, int y) {
                this.depo = depo;
                this.x = x;
                this.y = y;
            }

            public String getDepoId() {
                return depo.getDepoID();
            }
            public Map<String, Depo.DepoContainer> getDepoContainers() {
                return depo.getContainers();
            }
            public Map<String, List<Integer>> getDepoConnections() {
                return depo.getDepoConnections();
            }

            public int getX() {
                return x;
            }
            public int getY() {
                return y;
            }
        }

        public void addDepoVertex(Depo depo, int x, int y) {
            depoVertices.add(new DepoVertex(depo,x,y));
            this.repaint();
        }

        public int findDepoVertex(String id) throws Exception {
            for (int i = 0; i < depoVertices.size(); i++) {
                if (depoVertices.get(i) !=null && depoVertices.get(i).getDepoId().equals(id)) {
                    return i;
                }
            }
            throw new Exception("The following depo could not be found: \"" + id + "\"");
        }

        void drawPipe(Graphics g1, int strokeWidth, int x1, int y1, int x2, int y2) {
            Graphics2D g = (Graphics2D) g1.create();
            g.setStroke(new BasicStroke((float) (strokeWidth * 0.5)));
            //int offset;
            g.drawLine(x1, y1, x2, y2);
        }
        void drawDepo(Graphics g, DepoVertex depoVertex) {
            FontMetrics f = g.getFontMetrics();
            int nodeHeight = Math.max(height, f.getHeight());
            int nodeWidth = Math.max(width, f.stringWidth(depoVertex.getDepoId())+width/2);

            g.setColor(Color.white);
            g.fillOval(depoVertex.getX()-nodeWidth/2, depoVertex.getY()-nodeHeight/2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.black);
            g.drawOval(depoVertex.getX()-nodeWidth/2, depoVertex.getY()-nodeHeight/2,
                    nodeWidth, nodeHeight);

            g.drawString(depoVertex.getDepoId(), depoVertex.getX()-f.stringWidth(depoVertex.getDepoId())/2,
                    depoVertex.getY()+f.getHeight()/2);

            int div = depoVertex.getDepoContainers().size();
            int cyc = 1;
            for (Map.Entry<String, Depo.DepoContainer> dc : depoVertex.getDepoContainers().entrySet()) {
                int a = 50;
                int b = 50;
                int r = Math.min(a, b) * 4 / 5;
                double t = 2 * Math.PI * cyc / div;
                cyc++;
                int x = (int) Math.round(a + r * Math.cos(t));
                int y = (int) Math.round(b + r * Math.sin(t));
                drawContainer(g, depoVertex.getX() + x, depoVertex.getY() + y, dc.getValue());
            }
        }
        void drawContainer(Graphics g, int x, int y, Depo.DepoContainer depoContainer) {
            Map<String, Color> fuels = new HashMap<String, Color>() {{
                put("Dízel", Color.yellow);
                put("Benzin", Color.orange);

                put("Unused", Color.red);
            }};

            FontMetrics f = g.getFontMetrics();

            Color fuel = Color.white;
            String fuelName = "";
            int i = 0;
            for (Map.Entry<String, Color> entry : fuels.entrySet()) {
                if (i == depoContainer.getFuelID()-1) {
                    fuel = entry.getValue();
                    fuelName = entry.getKey();
                }
                i++;
            }
            //System.out.println(tmp);

            int nodeHeight = 5 * f.getHeight();
            int nodeWidth = (int) (Math.max(f.stringWidth("" + depoContainer.getMaxCapacity()), f.stringWidth(fuelName)) * 1.1);

            int level = Math.round(nodeHeight * depoContainer.getCurrentCapacity() / depoContainer.getMaxCapacity());
            g.setColor(Color.white);
            g.fillRect(x-nodeWidth/2, y-nodeHeight/2, nodeWidth, nodeHeight-level);
            g.setColor(fuel);
            g.fillRect(x-nodeWidth/2, y+nodeHeight/2-level, nodeWidth, level);

            g.setColor(Color.black);
            g.drawRect(x-nodeWidth/2, y-nodeHeight/2, nodeWidth, nodeHeight);
            g.drawString("" + depoContainer.getCurrentCapacity(),
                    x-f.stringWidth("" + depoContainer.getCurrentCapacity())/2,y-f.getHeight()*3/2);
            g.drawString("/",x-f.stringWidth("/")/2,y-f.getHeight()/2);
            g.drawString("" + depoContainer.getMaxCapacity(),
                    x-f.stringWidth("" + depoContainer.getMaxCapacity())/2,y+f.getHeight()/2);
            g.drawString("" + depoContainer.getMaxCapacity(),
                    x-f.stringWidth("" + depoContainer.getMaxCapacity())/2,y+f.getHeight()/2);
            g.drawString(fuelName,x-f.stringWidth(fuelName)/2,y+f.getHeight()*2);
        }
        void drawFuel(Graphics g1, int strokeWidth, int x1, int y1, int x2, int y2, Color fuel) {
            Graphics2D g = (Graphics2D) g1.create();

            /*double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);*/
            g.setStroke(new BasicStroke((float) (strokeWidth * 0.4)));
            g.setColor(fuel);

            //int offset;
            g.drawLine(x1, y1, x2, y2);
            //g.fillPolygon(new int[] {len, len-10, len-10, len}, new int[] {offset, -10 + offset, 10 + offset, offset}, 4);
        }

        public void paint(Graphics g) {
            for (DepoVertex dv : depoVertices) {
                dv.getDepoConnections().forEach((k, v) -> {
                    try {
                        drawPipe(g, v.get(1), dv.getX(), dv.getY(),
                                depoVertices.get(findDepoVertex(k)).getX(), depoVertices.get(findDepoVertex(k)).getY());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            for (DepoVertex dv : depoVertices) {
                drawDepo(g, dv);
            }
        }
    }
