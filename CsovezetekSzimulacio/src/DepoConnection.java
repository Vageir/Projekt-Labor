import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepoConnection {
    private String leftDepoID, rightDepoID;
    private int pipeLength,pipeDiameter;
    private Map<Integer, List<Double>> headAndTailOfTheFluidRelativeToLeftDepo;//List:head,tail

    public DepoConnection(String leftDepoID, String rightDepoID, int pipeLength, int pipeDiameter) {
        this.leftDepoID = leftDepoID;
        this.rightDepoID = rightDepoID;
        this.pipeLength = pipeLength;
        this.pipeDiameter = pipeDiameter;
        headAndTailOfTheFluidRelativeToLeftDepo = new LinkedHashMap<Integer, List<Double>>();
        List<Double> ls = new ArrayList();
        ls.add((double) pipeLength);
        ls.add((double) 0);
        headAndTailOfTheFluidRelativeToLeftDepo.put(2,ls);
    }
    public String getLeftDepoID() {
        return leftDepoID;
    }
    public String getRightDepoID() {
        return rightDepoID;
    }
    public int getPipeLength() {
        return pipeLength;
    }
    public int getPipeDiameter() {
        return pipeDiameter;
    }
    public Map<Integer, List<Double>> getHeadAndTailOfTheFluidRelativeToLeftDepo() {
        return headAndTailOfTheFluidRelativeToLeftDepo;    }
    public List<Integer> getFuelIDBefore(){
        List<Integer> ls = new ArrayList<>();
        for (Map.Entry<Integer,List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()){
            if (entry.getKey() < 100)
                ls.add(entry.getKey());
        }
        return ls;
    }
    public void setCurrentFuelID() {
        List<Integer> ls = new ArrayList<>();
        List<Integer> ll = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
            if (entry.getKey() > 100)
                ls.add(entry.getKey());
        }
        for (int i : ls) {
            headAndTailOfTheFluidRelativeToLeftDepo.put(i - 100, headAndTailOfTheFluidRelativeToLeftDepo.get(i));
            headAndTailOfTheFluidRelativeToLeftDepo.remove(i);
        }
        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
            Double head = entry.getValue().get(0);
            Double tail = entry.getValue().get(1);
            if (head.equals(tail) || tail.compareTo(head) > 0) {
                ll.add(entry.getKey());
            }

        }
        for (int i : ll) {
            headAndTailOfTheFluidRelativeToLeftDepo.remove(i);
        }
        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
            System.out.println("FUELID: " + entry.getKey() + " HEAD: " + entry.getValue().get(0) + " TAIL:" + entry.getValue().get(1));
        }
    }
}
