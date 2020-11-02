import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepoConnection {
    private String leftDepoID, rightDepoID;
    private int pipeLength,pipeDiameter;
    private Map<Integer,Double> headOfTheFluidRelativeToLeftDepo;
    private Map<Integer, Double> tailOfTheFluidRelativeToLeftDepo;
    public DepoConnection(String leftDepoID, String rightDepoID, int pipeLength, int pipeDiameter) {
        this.leftDepoID = leftDepoID;
        this.rightDepoID = rightDepoID;
        this.pipeLength = pipeLength;
        this.pipeDiameter = pipeDiameter;
        headOfTheFluidRelativeToLeftDepo = new LinkedHashMap<>();
        tailOfTheFluidRelativeToLeftDepo = new LinkedHashMap<Integer, Double>();
        headOfTheFluidRelativeToLeftDepo.put(2, Double.valueOf(pipeLength));
        tailOfTheFluidRelativeToLeftDepo.put(2,0.0);
    }
    public void setTailOfTheFluid(int fuelID, double location){
        tailOfTheFluidRelativeToLeftDepo.put(fuelID,location);
    }
    public void setHeadOfTheFluid(int fuelID, double location){
        headOfTheFluidRelativeToLeftDepo.put(fuelID,location);
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
    public Map<Integer, Double> getHeadOfTheFluidRelativeToLeftDepo() {
        return headOfTheFluidRelativeToLeftDepo;
    }
    public Map<Integer, Double> getTailOfTheFluidRelativeToLeftDepo() {
        return tailOfTheFluidRelativeToLeftDepo;
    }


}
