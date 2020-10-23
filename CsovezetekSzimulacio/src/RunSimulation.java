import java.util.concurrent.Callable;

public class RunSimulation implements Callable<Boolean> {
    TransportationPlan t;
    int pipeLength = 0, pipeDiameter = 0;
    double flowVelocity;
    double headOfTheFluid = 0;
    double tailOfTheFluid = 0;
    Depo startDepo, endDepo;
    String startDepoContainerID,endDepoContainerID;
    Test test;
    public RunSimulation(TransportationPlan t, Depo startDepo, Depo endDepo) {
        this.t = t;
        this.startDepo = startDepo;
        this.endDepo = endDepo;
        this.pipeLength = startDepo.getDepoConnections().get(endDepo.getDepoID()).get(0);
        this.pipeDiameter = startDepo.getDepoConnections().get(endDepo.getDepoID()).get(1);
        flowVelocity = Simulation.volumeFlowRate / (Math.PI * Math.pow(((double) pipeDiameter / 2), 2));
        System.out.println("Flow velocity: " + flowVelocity);
        startDepoContainerID = startDepo.getContainerID(t);
        endDepoContainerID = endDepo.getContainerID(t);
        test = new Test(tailOfTheFluid);
    }

    public double getTailOfTheFluid() {
        return tailOfTheFluid;
    }

    public double getHeadOfTheFluid() {
        return headOfTheFluid;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println("Simulation started......\n");
        System.out.println("From: " + t.getStartDepoID() + "  To: " + t.getEndDepoID() + "  Fuel: " + t.getFuelID() + "  Fuel Amount: " + t.getFuelAmount());
        System.out.println("Start Hours:: " + t.getStartHours() + "  Start Minutes: " + t.getStartMinutes());
        System.out.println("End Hours:: " + t.getEndHours() + "  End Minutes: " + t.getEndMinutes());
        int startDepoMovedFuelAmount = 0, endDepoMoveFuelAmount = 0;
        double time = pipeLength / flowVelocity;
        int hours = t.getStartHours(), minutes = t.getStartMinutes();
        while (hours < t.getEndHours()) {
            while (minutes < 60) {
                try {
                    Thread.currentThread().sleep(Simulation.timeSpeed);
                    if (headOfTheFluid < pipeLength) {
                        headOfTheFluid += flowVelocity;
                    }else if(headOfTheFluid >= pipeLength && endDepoMoveFuelAmount != t.getFuelAmount()){
                        endDepoMoveFuelAmount+=Simulation.volumeFlowRate;
                        endDepo.getContainers().get(endDepoContainerID).addCurrentCapacity(Simulation.volumeFlowRate);
                    }
                    if (startDepoMovedFuelAmount != t.getFuelAmount()) {
                        startDepoMovedFuelAmount += Simulation.volumeFlowRate;
                        startDepo.getContainers().get(startDepoContainerID).substractCurrentCapacity(Simulation.volumeFlowRate);
                    }
                    else if (tailOfTheFluid < pipeLength) {
                        time--;
                        tailOfTheFluid += flowVelocity;
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                minutes++;
                test.tail = headOfTheFluid;
            }
            minutes = 0;
            System.out.println("Current hour:(" + t.getTransportationID() + ")" + hours);
            hours++;
        }
        if (t.getEndMinutes() > 0) {
            minutes = 0;
            while (minutes < t.getEndMinutes()) {
                try {
                    Thread.currentThread().sleep(Simulation.timeSpeed);
                    if (headOfTheFluid < pipeLength) {
                        headOfTheFluid += flowVelocity;
                    }
                    if (startDepoMovedFuelAmount != t.getFuelAmount())
                        startDepoMovedFuelAmount += Simulation.volumeFlowRate;
                    else {
                        time--;

                        tailOfTheFluid += flowVelocity;
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                minutes++;
            }
        }
        if (startDepoMovedFuelAmount == t.getFuelAmount() && time <= 0) {
            System.out.println("Transportation Complete.....");
//                new DataBaseHandler().updateRecord("depocontainer",
//                        "currentcapacity", String.valueOf(startDepoCurrentCapacity - t.getFuelAmount()), "containerID = '" + startDepoContainerID + "'");
//                new DataBaseHandler().updateRecord("depocontainer",
//                        "currentcapacity", String.valueOf(t.getFuelAmount() + endDepoCurrentCapacity), "containerID = '" + endDepoContainerID + "'");
            return true;
        } else {
            System.out.println("Transportation Failed.....");
            return false;
        }
    }
    static class Test{
        private double tail;

        public Test() {
        }

        public Test(double tail) {
            this.tail = tail;
        }

        public double getTail() {
            return tail;
        }
    }
}
