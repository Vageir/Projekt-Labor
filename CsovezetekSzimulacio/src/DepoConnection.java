import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepoConnection {
    private String leftDepoID, rightDepoID;
    private int pipeLength,pipeDiameter;
    private Map<Integer, List<Double>> headAndTailOfTheFluidRelativeToLeftDepo;

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
    public void setHeadOfTheFluid(int fuelID, List<Double> location){
        headAndTailOfTheFluidRelativeToLeftDepo.put(fuelID,location);
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
        return headAndTailOfTheFluidRelativeToLeftDepo;
    }


}
