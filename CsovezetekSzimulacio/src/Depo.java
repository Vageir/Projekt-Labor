import java.util.List;

public class Depo {
    private String depoID;
    private List<String> depoConnections;
    private List<DepoContainer> containers;

    public Depo(String depoID, List<String> depoConnections, List<DepoContainer> containers) {
        this.depoID = depoID;
        this.depoConnections = depoConnections;
        this.containers = containers;
    }

    public String getDepoID() {
        return depoID;
    }

    public List<String> getDepoConnections() {
        return depoConnections;
    }

    public List<DepoContainer> getContainers() {
        return containers;
    }

    private class DepoContainer {
        private String containerID;
        int currentCapacity;
        int maxCapacity;
        int fuelID;

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
