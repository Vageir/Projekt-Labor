import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.Color;

public class Graph extends JFrame {
        int width;
        int height;

        ArrayList<Node> nodes;
        ArrayList<edge> edges;

        public Graph(String name) {
            this.setTitle(name);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            nodes = new ArrayList<Node>();
            edges = new ArrayList<edge>();
            width = 30;
            height = 30;
        }

        class Node {
            int x, y;
            String name;

            public Node(String myName, int myX, int myY) {
                x = myX;
                y = myY;
                name = myName;
            }
        }

        class edge {
            int i,j;

            public edge(int ii, int jj) {
                i = ii;
                j = jj;
            }
        }

        public void addNode(String name, int x, int y) {
            nodes.add(new Node(name,x,y));
            this.repaint();
        }
        public void addEdge(int i, int j) {
            edges.add(new edge(i,j));
            this.repaint();
        }

        public int findNode(String name) {
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i) !=null && nodes.get(i).name.equals(name)) {
                    return i;
                }
            }
            return -1;
        }
        public int findEdge(String start, String end) {
            int startIndex = findNode(start);
            int endIndex = findNode(end);
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i) !=null && edges.get(i).i == startIndex && edges.get(i).j == endIndex) {
                    return i;
                }
            }
            return -1;
        }

        public void recolorEdge(String start, String end, Color state) {
            try {
                edge e = edges.get(findEdge(start, end));
                Graphics g = getGraphics();
                g.setColor(state);
                drawArrow(g, nodes.get(e.i).x, nodes.get(e.i).y, nodes.get(e.j).x, nodes.get(e.j).y);
            }
            catch (IndexOutOfBoundsException error) {
                System.err.println("The edge could not be found");
            }
        }
        void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
            Graphics2D g = (Graphics2D) g1.create();

            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);

            // Draw horizontal arrow starting in (0, 0)
            g.drawLine(0, 0, len, 0);
            g.fillPolygon(new int[] {len, len-4, len-4, len},
                    new int[] {0, -4, 4, 0}, 4);
        }

        public void paint(Graphics g) {
            FontMetrics f = g.getFontMetrics();
            int nodeHeight = Math.max(height, f.getHeight());

            g.setColor(Color.black);
            for (edge e : edges) {
                //g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
                        //nodes.get(e.j).x, nodes.get(e.j).y);
                drawArrow(g, nodes.get(e.i).x, nodes.get(e.i).y,
                        nodes.get(e.j).x, nodes.get(e.j).y);
            }

            for (Node n : nodes) {
                int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
                g.setColor(Color.white);
                g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                        nodeWidth, nodeHeight);
                g.setColor(Color.black);
                g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                        nodeWidth, nodeHeight);

                g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
                        n.y+f.getHeight()/2);
            }
        }
    }
