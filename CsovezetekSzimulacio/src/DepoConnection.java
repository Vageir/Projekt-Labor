import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepoConnection {
    private String leftDepoID, rightDepoID;
    private int pipeLength,pipeDiameter;
    private Map<Integer, List<Double>> headAndTailOfTheFluidRelativeToLeftDepo;//List:head,tail
    private List<Integer> pushFluidID;

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
        pushFluidID = new ArrayList<>();
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
//            System.out.println("FUELID: "+entry.getKey());
//            System.out.println("HEAD: "+entry.getValue().get(0)+" TAIL:"+entry.getValue().get(1));
        }
        return ls;
    }
    public void setCurrentFuelID() {
        List<Integer> ls = new ArrayList<>();
        List<Integer> ll = new ArrayList<>();
//        System.out.println("BEFORE");
//        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
//            System.out.println("FUELID: " + entry.getKey() + " HEAD: " + entry.getValue().get(0) + " TAIL:" + entry.getValue().get(1));
//        }
        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
            if (entry.getKey() > 100)
                ls.add(entry.getKey());
            List<Double> list = new ArrayList<>();
            if (entry.getValue().get(0) > pipeLength ) {
                list.add((double) pipeLength);
            } else list.add(entry.getValue().get(0));
            if (entry.getValue().get(1) > pipeLength){
                list.add(Double.valueOf(pipeLength));
            }list.add(entry.getValue().get(1));
            entry.setValue(list);
        }
        for (int i : ls) {
            if (i > 1000){
                headAndTailOfTheFluidRelativeToLeftDepo.put(i - 1000, headAndTailOfTheFluidRelativeToLeftDepo.get(i));
                headAndTailOfTheFluidRelativeToLeftDepo.remove(i);
            }
            else {
                headAndTailOfTheFluidRelativeToLeftDepo.put(i - 100, headAndTailOfTheFluidRelativeToLeftDepo.get(i));
                headAndTailOfTheFluidRelativeToLeftDepo.remove(i);
            }

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
        pushFluidID.clear();
//        System.out.println("AFTER");
//        for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
//            System.out.println("FUELID: " + entry.getKey() + " HEAD: " + entry.getValue().get(0) + " TAIL:" + entry.getValue().get(1));
//        }
//        System.out.println(".........");
    }
    public void addPushFluidID(int fuelID){
        pushFluidID.add(fuelID);
    }
    public List<Integer> getPushFluidID() {
        return pushFluidID;
    }
    public void setDirection(TransportationPlan t) {
        if (!t.getStartDepoID().equals(leftDepoID)) {
            for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
                List<Double> list = new ArrayList<>();
                if (entry.getValue().get(1) == 0.0){
                    list.add((double) pipeLength);
                }
                else if (entry.getValue().get(1) > 0.0){
                    list.add(pipeLength-entry.getValue().get(1));
                }
                if (entry.getValue().get(0) >= pipeLength){
                    list.add(0.0);
                }
                else if (entry.getValue().get(0) > 0.0){
                    list.add(pipeLength-entry.getValue().get(0));

                }
                entry.setValue(list);
            }
//            System.out.println("AFTER THE SWAP");
//            for (Map.Entry<Integer, List<Double>> entry : headAndTailOfTheFluidRelativeToLeftDepo.entrySet()) {
//                System.out.println("FUELID: " + entry.getKey() + " HEAD: " + entry.getValue().get(0) + " TAIL:" + entry.getValue().get(1));
//            }
//            System.out.println(".........");
        }

    }
}
