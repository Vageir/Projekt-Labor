import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Depo {
    private String depoID;
    private Map<String, List<Integer>> depoConnections;
    private List<DepoContainer> containers;

    public void readDepoDatas(){
        System.out.println("DepoID: "+depoID);
        for (DepoContainer dc:containers){
            System.out.println("ContainerID: "+dc.getContainerID());
            System.out.println("Current Capcatity: "+dc.getCurrentCapacity());
            System.out.println("Max Capacity: "+dc.getMaxCapacity());
            System.out.println("Fuel ID: "+dc.getFuelID());
            System.out.println();
        }
        System.out.println("Depo Connections");
        for (Map.Entry<String,List<Integer>> entry:depoConnections.entrySet()){
            System.out.println("Connected Depo ID: "+entry.getKey());
            System.out.println("Length: "+entry.getValue().get(0)+" Diameter"+entry.getValue().get(1));
        }
        System.out.println("\n\n");
    }
    public Depo(String depoID, Map<String, List<Integer>> depoConnections, List<DepoContainer> containers) {
        this.depoID = depoID;
        this.depoConnections = depoConnections;
        this.containers = containers;
    }
    public Depo(String depoID, List<String> depoContainers, List<String> connectedDepos) {
        this.depoID = depoID;
        depoConnections = new HashMap<String, List<Integer>>();
        containers = new ArrayList<>();
        for (int i = 1; i < depoContainers.size(); i += 5){
            containers.add(new DepoContainer(depoContainers.get(i), Integer.parseInt(depoContainers.get(i+1)), Integer.parseInt(depoContainers.get(i+2)), Integer.parseInt(depoContainers.get(i+3))));
        }

        List<Integer> lengthDiameter = new ArrayList<>();
        for (int i = 0; i < connectedDepos.size(); i+=4) {
            lengthDiameter.add(Integer.parseInt(connectedDepos.get(i+2)));
            lengthDiameter.add(Integer.parseInt(connectedDepos.get(i+3)));
            if (depoID.equals(connectedDepos.get(i))){
                depoConnections.put(connectedDepos.get(i+1),lengthDiameter);
            }
            else {
                depoConnections.put(connectedDepos.get(i),lengthDiameter);
            }
        }

    }

    public String getDepoID() {
        return depoID;
    }

    public Map<String, List<Integer>> getDepoConnections() {
        return depoConnections;
    }

    public List<DepoContainer> getContainers() {
        return containers;
    }

    class DepoContainer {
        private String containerID;
        private int currentCapacity;
        private int maxCapacity;
        private int fuelID;

        public DepoContainer(String containerID, int currentCapacity, int maxCapacity, int fuelID) {
            this.containerID = containerID;
            this.currentCapacity = currentCapacity;
            this.maxCapacity = maxCapacity;
            this.fuelID = fuelID;
        }

        public String getContainerID() {
            return containerID;
        }

        public int getCurrentCapacity() {
            return currentCapacity;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public int getFuelID() {
            return fuelID;
        }
    }
}
