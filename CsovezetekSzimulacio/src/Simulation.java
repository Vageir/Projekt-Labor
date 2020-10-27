import java.util.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
// t=s/v
public class Simulation  {
    static final int volumeFlowRate = 100; //cubic meteres/min
    static final int timeSpeed = 100; // 1 ms = 1perc
//    double flowVelocity; //m/min
    List <TransportationPlan> transportationPlans;
    List <Depo> depos;

    public Map<String, List<Double>> getPositionOfTheFluid() {
        return positionOfTheFluid;
    }

    Map<String, List<Double>> positionOfTheFluid;
    public Simulation() {
        transportationPlans = new ArrayList<>();
        depos = new ArrayList<>();
        positionOfTheFluid = new HashMap<String, List<Double>>();
        readData();
    }
    public void readData(){
        for (Map.Entry<Integer, ArrayList<String>> entry : new DataBaseHandler().readRecords("transportationplan").entrySet()) {
            transportationPlans.add(new TransportationPlan(entry.getValue()));
        }
        for (Map.Entry<Integer, ArrayList<String>> entry : new DataBaseHandler().readRecords("depo").entrySet()) {
            List<String> depocontainer = new DataBaseHandler().readRecordWithCondition("depocontainer","depoID='"+entry.getValue().get(0)+"'");
            List<String> connectedDepo = new DataBaseHandler().readRecordWithCondition("connecteddepos",
                    "LeftDepoID='"+entry.getValue().get(0)+"' OR"+" RightDepoID='"+entry.getValue().get(0)+"'");
            depos.add(new Depo(entry.getValue().get(0),depocontainer,connectedDepo));
        }

//        for (Depo d:depos){
//            d.readDepoDatas();
//        }
    }
    public List<TransportationPlan> getTransportationPlans() {
        return transportationPlans;
    }
    public void addTransportationPlan(TransportationPlan tp){
        transportationPlans.add(tp);
    }
    public void setTransportationPlans(ArrayList<TransportationPlan> transportationPlans) {
        this.transportationPlans = transportationPlans;
    }
    private boolean checkContainer(String tID){
        for(TransportationPlan t : transportationPlans){
            if (t.getTransportationID().equals(tID)){
                for (Depo d:depos){
                    if (t.getStartDepoID().equals(d.getDepoID())){
                        System.out.println("Start Depo Fuel and Container Check");
                        if (!checkStartFuelAndCapacity(t,d)){
                            return false;
                        }
                    }
                }
                for (Depo d:depos){
                    if (t.getEndDepoID().equals(d.getDepoID())){
                        System.out.println("End Depo Fuel and Container Check");
                        if (!checkEndFuelAndCapacity(t,d)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private boolean checkEndFuelAndCapacity(TransportationPlan t, Depo d) {
        for (Map.Entry<String, Depo.DepoContainer> entry : d.getContainers().entrySet()){
            if (entry.getValue().getFuelID() == t.getFuelID() && entry.getValue().getCurrentCapacity()+t.getFuelAmount() <= entry.getValue().getMaxCapacity()){
                System.out.println("End Container Fuel and Capacity.....OK");
                return true;
            }
        }
        System.out.println("End Container Fuel and Capacity.....Failed");
        return false;
    }
    private boolean checkStartFuelAndCapacity(TransportationPlan t, Depo d) {
        for (Map.Entry<String, Depo.DepoContainer> entry : d.getContainers().entrySet()){
            if (entry.getValue().getFuelID()== t.getFuelID() && entry.getValue().getCurrentCapacity() >= t.getFuelAmount()){
                System.out.println("Start Container Fuel and Capacity.....OK");
                return true;
            }
        }
        System.out.println("Start Container Fuel and Capacity.....Failed");
        return false;
    }
//    private boolean checkCapacity(TransportationPlan t, Depo d) {
////        for (int i = 2; i < ls.size(); i+=5) {
////            if (Integer.parseInt(ls.get(i))+ (t.getFuelAmount()) <= Integer.parseInt(ls.get(i+1))){
////                endDepoCurrentCapacity=Integer.parseInt(ls.get(i));
////                endDepoContainerID = ls.get(i-1);
////                endDepoCurrentCapacity=Integer.parseInt(ls.get(i));
////                endDepoContainerID = ls.get(i-1);
////                System.out.println("Container Capacity.....OK");
////                return true;
////            }
////        }
//        for (Depo.DepoContainer dc:d.getContainers()){
//            if (dc.getFuelID() == t.getFuelID() && dc.getCurrentCapacity()+t.getFuelAmount() <= dc.getMaxCapacity()){
//                System.out.println("Container Fuel and Capacity.....OK");
//                return true;
//            }
//        }
//        System.out.println("Container Capacity.....Failed");
//        return false;
//    }
    private boolean checkConnection(String tID){
        for(TransportationPlan t : transportationPlans) {
            if (t.getTransportationID().equals(tID)) {
                for (Depo d : depos){
                    if(d.getDepoID().equals(t.getStartDepoID())){
                        for (Depo.DepoConnection dc : d.getDepoConnections()){
                            if (dc.connectedDepoID.equals(t.getEndDepoID()))
                                return true;
                        }
                    }
                }
            }
        }
        System.out.println("Depo Connection.....Failed");
        return false;
    }
    public void runSimulation(){
        if (runSimulations()){
            System.out.println("Sikeres lefutás");
        }else {
            System.out.println("Hibás lefutás az adatbázis nem módosult");
        }
    }
    private boolean runSimulations(){
        for (int i = 0; i < transportationPlans.size(); i++ ) {
            System.out.println("\nTid:" + transportationPlans.get(i).getTransportationID());
            if (checkConnection(transportationPlans.get(i).getTransportationID()) && checkContainer(transportationPlans.get(i).getTransportationID())) {
                System.out.println("All check.....OK\n");
                List<TransportationPlan> runTransportationPlans = new ArrayList<>();
                int j = i + 1;
                runTransportationPlans.add(transportationPlans.get(i));
                while (j < transportationPlans.size() && transportationPlans.get(i).getStartHours() == transportationPlans.get(j).getStartHours()
                        && transportationPlans.get(i).getStartMinutes() == transportationPlans.get(j).getStartMinutes()) {
                        if (transportationPlans.get(i).getStartDepoID().equals(transportationPlans.get(j).getStartDepoID())
                                && transportationPlans.get(i).equals(transportationPlans.get(j).getEndDepoID())){
                            System.out.println("Ugyanazon az időben, ugyanabban az irányban nem mehet két terv egyszere");
                            System.out.println("Hibás tervek:"+transportationPlans.get(i).getTransportationID()
                                    + " , "+transportationPlans.get(j).getTransportationID());
                            System.out.println("whack");
                            return false;
                        }
                        else if (transportationPlans.get(i).getStartDepoID().equals(transportationPlans.get(j).getStartDepoID())
                                && !(transportationPlans.get(i).equals(transportationPlans.get(j).getEndDepoID()))){
                            System.out.println("\nTid:" + transportationPlans.get(j).getTransportationID());
                            if(checkConnection(transportationPlans.get(j).getTransportationID()) && checkContainer(transportationPlans.get(j).getTransportationID())){
                                runTransportationPlans.add(transportationPlans.get(j));
                                j++; i++;
                            }
                            else{
                                System.out.println("Hiba! Ellenőrzise a "+transportationPlans.get(j).getTransportationID()+ " tervet");
                                return false;
                            }

                        }

                }
             if (!startSimulation(runTransportationPlans)){
                 System.out.println("Hiba a szimulációban");
                 return false;
             }
            } else {
                System.out.println("All check.....Failed");
                System.out.println("Hiba! Ellenőrzie a : " + transportationPlans.get(i).getTransportationID() + " tervet");
                return false;
            }
        }
        return true;
    }

    private boolean startSimulation(List<TransportationPlan> runTransportationPlans){
        List<FutureTask> simulationTask = new ArrayList<>();
        for (TransportationPlan t : runTransportationPlans){
            Depo startDepo=null,endDepo=null;
            for (Depo d : depos){
                if (d.getDepoID().equals(t.getStartDepoID()))
                    startDepo=d;
            }
            for (Depo d : depos){
                if (d.getDepoID().equals(t.getEndDepoID()))
                    endDepo=d;
            }
            if (startDepo != null && endDepo != null){
                RunSimulation runSimulation = new RunSimulation(t, startDepo, endDepo);
                simulationTask.add(new FutureTask(runSimulation));
            }else{
                System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeet");
            }
        }

//        Thread testingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                        int i =0;
//                        while (i < 7) {
//                            Thread.currentThread().sleep(1000);
//                            for (Map.Entry<String, List<Double>> entry : positionOfTheFluid.entrySet()) {
//                                System.out.println("TID: " + entry.getKey() + " HeadOfTheFluid: " + entry.getValue().get(0) + " TailOfTheFluid: " + entry.getValue().get(1));
//                            }
//                            i++;
//                        }
//
//                } catch (InterruptedException interruptedException) {
//                    interruptedException.printStackTrace();
//                }
//            }
//        });
        for (FutureTask futureTask : simulationTask){
            Thread t = new Thread(futureTask);
            t.start();
        }
//        testingThread.start();
        for (FutureTask futureTask : simulationTask){
            try {
                if(!(Boolean)futureTask.get()){
                    return false;
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
//        try {
//            testingThread.join();
//        } catch (InterruptedException interruptedException) {
//            interruptedException.printStackTrace();
//        }
        return true;
    }

    private class RunSimulation implements Callable<Boolean> {
        TransportationPlan t;
        int pipeLength = 0, pipeDiameter = 0;
        double flowVelocity;
        double headOfTheFluid = 0;
        double tailOfTheFluid = 0;
        Depo startDepo, endDepo;
        String startDepoContainerID,endDepoContainerID;
        public RunSimulation(TransportationPlan t, Depo startDepo, Depo endDepo) {
            this.t = t;
            this.startDepo = startDepo;
            this.endDepo = endDepo;
            for (Depo.DepoConnection dc : startDepo.getDepoConnections()){
                if (dc.connectedDepoID.equals(endDepo.getDepoID())){
                    this.pipeLength = dc.getPipeLength();
                    this.pipeDiameter = dc.getPipeDiameter();
                }
            }
            flowVelocity = Simulation.volumeFlowRate / (Math.PI * Math.pow(((double) pipeDiameter / 2), 2));
            System.out.println("Flow velocity: " + flowVelocity);
            startDepoContainerID = startDepo.getContainerID(t);
            endDepoContainerID = endDepo.getContainerID(t);
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
            List ls = new ArrayList();
            ls.add(headOfTheFluid);
            ls.add(tailOfTheFluid);
            positionOfTheFluid.put(t.getTransportationID(),ls);
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
                    positionOfTheFluid.get(t.getTransportationID()).set(0,headOfTheFluid);
                    positionOfTheFluid.get(t.getTransportationID()).set(1,tailOfTheFluid);
                    minutes++;
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
                    positionOfTheFluid.get(t.getTransportationID()).set(0,headOfTheFluid);
                    positionOfTheFluid.get(t.getTransportationID()).set(1,tailOfTheFluid);
                    minutes++;
                }
            }
            positionOfTheFluid.remove(t.getTransportationID());
            if (startDepoMovedFuelAmount == t.getFuelAmount() && time <= 0) {
                System.out.println("Transportation Complete.....");
                return true;
            } else {
                System.out.println("Transportation Failed.....");
                return false;
            }
        }
    }

}
