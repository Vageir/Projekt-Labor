import java.util.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
// t=s/v
public class Simulation  {
    static final int volumeFlowRate = 100; //cubic metres/min
    static final int timeSpeed = 100; // 1 ms = 1min
//    double flowVelocity; //m/min
    private List <TransportationPlan> transportationPlans;
    private List <Depo> depos;
    private Map<String, DepoConnection> depoConnections;
    private int currentHours, currentMinutes;
    private List<String> errorMessages;
    private ExecutorService executor;
    private List<Future<Boolean>> futureList;

    public Simulation() {
        transportationPlans = new ArrayList<>();
        depos = new ArrayList<>();
        depoConnections = new LinkedHashMap<>();
        errorMessages = new ArrayList<>();
        readData();
        setVelocity();
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
    public Map<String, DepoConnection> getDepoConnections() {
        return depoConnections;
    }
    public int getCurrentHours() {
        return currentHours;
    }
    public int getCurrentMinutes() {
        return currentMinutes;
    }
    public List<TransportationPlan> getTransportationPlans() {
        return transportationPlans;
    }
    public List<Depo> getDepos() {
        return depos;
    }
    public void runSimulation(){
//        try {
//            Thread.currentThread().sleep(2000);
//        } catch (InterruptedException interruptedException) {
//            interruptedException.printStackTrace();
//        }
        try {
            if (runSimulations()){
                System.out.println("Sikeres lefutás");
                //database update
            }else {
                System.out.println("Hibás lefutás az adatbázis nem módosult");
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
    private void readData(){
        for (Map.Entry<Integer, ArrayList<String>> entry : new DataBaseHandler().readRecords("transportationplan").entrySet()) {
            transportationPlans.add(new TransportationPlan(entry.getValue()));
        }
        for (Map.Entry<Integer, ArrayList<String>> entry : new DataBaseHandler().readRecords("depo").entrySet()) {
            List<String> depocontainer = new DataBaseHandler().readRecordWithCondition("depocontainer","depoID='"+entry.getValue().get(0)+"'");
            depos.add(new Depo(entry.getValue().get(0),depocontainer));
        }
        for (Map.Entry<Integer, ArrayList<String>> entry : new DataBaseHandler().readRecords("connecteddepos").entrySet()) {
            List<String> ls = entry.getValue();
            DepoConnection dc = new DepoConnection(ls.get(1),ls.get(2),Integer.parseInt(ls.get(3)),Integer.parseInt(ls.get(4)));
            depoConnections.put(ls.get(0),dc);
        }
    }
//    private boolean checkContainer(String tID){
//        for(TransportationPlan t : transportationPlans){
//            if (t.getTransportationID().equals(tID)){
//                for (Depo d:depos){
//                    if (t.getStartDepoID().equals(d.getDepoID())){
////                        System.out.println("Start Depo Fuel and Container Check");
//                        if (!checkStartFuelAndCapacity(t,d)){
//                            return false;
//                        }
//                    }
//                }
//                for (Depo d:depos){
//                    if (t.getEndDepoID().equals(d.getDepoID())){
////                        System.out.println("End Depo Fuel and Container Check");
//                        if (!checkEndFuelAndCapacity(t,d)){
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }
//    private boolean checkEndFuelAndCapacity(TransportationPlan t, Depo d) {
//        for (Map.Entry<String, Depo.DepoContainer> entry : d.getContainers().entrySet()){
//            if (entry.getValue().getFuelID() == t.getFuelID() && entry.getValue().getCurrentCapacity()+t.getFuelAmount() <= entry.getValue().getMaxCapacity()){
////                System.out.println("End Container Fuel and Capacity.....OK");
//                return true;
//            }
//        }
////        System.out.println("End Container Fuel and Capacity.....Failed");
//        errorMessages.add("Nincs elég kapacitás/nem létezik olyan tipusú tartály a vételi oldalon. Ellenőrizze a "+t.getTransportationID()+" tervet!");
//        return false;
//    }
//    private boolean checkStartFuelAndCapacity(TransportationPlan t, Depo d) {
//        for (Map.Entry<String, Depo.DepoContainer> entry : d.getContainers().entrySet()){
//            if (entry.getValue().getFuelID()== t.getFuelID() && entry.getValue().getCurrentCapacity() >= t.getFuelAmount()){
////                System.out.println("Start Container Fuel and Capacity.....OK");
//                return true;
//            }
//        }
////        System.out.println("Start Container Fuel and Capacity.....Failed");
//        errorMessages.add("Nincs elég kapacitás/nem létezik olyan tipusú tartály a küldő oldalon. Ellenőrizze a "+t.getTransportationID()+" tervet!");
//        return false;
//    }
    private void setVelocity() {
        for (TransportationPlan t : transportationPlans){
            double flowVelocity = 0.0;
            flowVelocity = Simulation.volumeFlowRate / (Math.PI * Math.pow(((double) depoConnections.get(t.getPipeID()).getPipeDiameter() / 2), 2));
            t.setFlowVelocity(flowVelocity);
        }
    }
    private Depo getDepo(String depoID){
        for (Depo d : depos){
            if (d.getDepoID().equals(depoID))
                return d;
        }
        return null;
    }
    private boolean runSimulations() throws InterruptedException {
        currentHours = transportationPlans.get(0).getStartHours();
        currentMinutes = transportationPlans.get(0).getStartMinutes();
        while (currentHours < transportationPlans.get(transportationPlans.size()-1).getEndHours()){
//            System.out.println("Current Hours: "+currentHours);
            currentMinutes = 0;
            while (currentMinutes < 60){
                sleep(timeSpeed);
                for (TransportationPlan t : transportationPlans){
                    if (t.getStartHours()== currentHours && t.getStartMinutes() == currentMinutes){
                        t.setTimeToRun(true);
                        t.setHighestContainerCapacityID(getDepo(t.getStartDepoID()).getHighestCurrentCapacityContainer());
                    }
                    if (t.isTimeToRun()){
                        if(!process(t)) return false;
                        if (!t.isFinished()){
                            if (t.getTailOfTheFluid() >= depoConnections.get(t.getPipeID()).getPipeLength()){
//                                System.out.println("TAIL:"+t.getTailOfTheFluid()+" PLength:"+depoConnections.get(t.getPipeID()).getPipeLength());
//                                System.out.println(t.getEndHours());
                                if (t.getEndHours() >= currentHours) {
                                    t.setFinished(true);
//                                    System.out.println("TID:"+t.getTransportationID());
                                    depoConnections.get(t.getPipeID()).setCurrentFuelID();
                                }
                                else{
                                    System.out.println("Nem volt elég idő");
                                    errorMessages.add("Nincs elég idő a(z) "+t.getTransportationID()+" terv végerhajtására.");
                                    return false;
                                }
                            }
                        }
                    }
                }
                currentMinutes++;
            }
            currentHours++;
        }
        for (TransportationPlan t: transportationPlans){
            if (!t.isFinished()) return false;
        }
        return true;
    }
    private boolean process(TransportationPlan t){
        int pipeLength = depoConnections.get(t.getPipeID()).getPipeLength();
        Depo startDepo = getDepo(t.getStartDepoID());
        Depo endDepo = getDepo(t.getEndDepoID());
        String startDepoContainerID = startDepo.getContainerID(t);
        String endDepoContainerID = endDepo.getContainerID(t);
        String highestContainerCapacityID = t.getHighestContainerCapacityID();
        String pipeID = t.getPipeID();
//        System.out.println("TID:"+t.getTransportationID()+" STARtDEPO: "+startDepo.getDepoID());
        double flowVelocity = t.getFlowVelocity();
        if (t.getStartDepoMovedFuelAmount() != t.getFuelAmount()) {
            t.addStartDepoVolumeFlowRate(volumeFlowRate);
            startDepo.getContainers().get(startDepoContainerID).substractCurrentCapacity(volumeFlowRate);
        }
        else {
            if (t.getTailOfTheFluid() < (double)pipeLength){
                if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity()-volumeFlowRate) < 0) {
                    highestContainerCapacityID = startDepo.getHighestCurrentCapacityContainer();
                    t.setHighestContainerCapacityID(highestContainerCapacityID);
                    if (highestContainerCapacityID == null)
                        return false;
                    if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity() - volumeFlowRate) < 0) {
                        errorMessages.add("Nincs elég üzemanyag az induló oldalon, hogy át lehessen vinni a tervben szereplő üzemanyagot. " +
                                "Ellenőrizze a "+t.getTransportationID()+" tervet!");
                        System.out.println("Alulcsordulás");
                        return false;
                    }
                }
                t.addTailOfTheFluid(flowVelocity);
//                System.out.println("BEFORE MOVE: "+startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity());
                startDepo.getContainers().get(highestContainerCapacityID).substractCurrentCapacity(volumeFlowRate);
//                System.out.println("AFTER MOVE: "+startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity());
                List<Double> ls = new ArrayList<>();
                ls.add(t.getTailOfTheFluid());
                ls.add((double) 0);
                depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
                        .put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID()+100,ls);
                if (t.getTransportationID().equals("3")) {
                    System.out.println("CONTAINERID: " + highestContainerCapacityID);
                    System.out.println("FUELID:" + (startDepo.getContainers().get(highestContainerCapacityID).getFuelID() + 100) + " HEAD: " + depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(
                            startDepo.getContainers().get(highestContainerCapacityID).getFuelID() + 100).get(0)
                            + " TAIL: " + depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(
                            startDepo.getContainers().get(highestContainerCapacityID).getFuelID() + 100).get(1));
                }
            }
            else {
                t.setTailOfTheFluid(pipeLength);
                List<Double> ls = new ArrayList<>();
                ls.add(t.getTailOfTheFluid());
                ls.add((double) 0);
                depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
                        .put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID()+100,ls);
            }
        }
        if (t.getHeadOfTheFluid() < pipeLength) {
            t.addHeadOfTheFluid(flowVelocity);
            List<Integer> ls = depoConnections.get(pipeID).getFuelIDBefore();
            if (!ls.isEmpty()){
                for (int i : ls){
//                    System.out.println("END DEPO CONTAINER ID:"+endDepo.getContainerID(i));
//                    System.out.println("CURRENT CAPACITY:"+endDepo.getContainers().get(endDepo.getContainerID(i)).getCurrentCapacity());
//                    System.out.println("MAX CAPACITY"+endDepo.getContainers().get(endDepo.getContainerID(i)).getMaxCapacity());
                    if ((endDepo.getContainers().get(endDepo.getContainerID(i)).getCurrentCapacity()+volumeFlowRate) >
                            endDepo.getContainers().get(endDepo.getContainerID(i)).getMaxCapacity()) {
                        errorMessages.add("A vételi oldalon megtelt a tartály! ");
                        System.out.println("Túlcsordulás");
                        return false;
                    }
                    endDepo.getContainers().get(endDepo.getContainerID(i)).addCurrentCapacity(volumeFlowRate);
                    List<Double> ll = new ArrayList<>();
                    ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0));
                    ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1)+flowVelocity);
//                    System.out.println(ll.get(1));
                    depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(i,ll);
                }
            }
        }
        else {
            if (t.getEndDepoMovedFuelAmount() != t.getFuelAmount()){
                if (endDepo.getContainers().get(endDepoContainerID).getCurrentCapacity()+volumeFlowRate
                        <= endDepo.getContainers().get(endDepoContainerID).getMaxCapacity()) {
                    endDepo.getContainers().get(endDepoContainerID).addCurrentCapacity(volumeFlowRate);
                }
                else{
                    errorMessages.add("A vételi oldalon megtelt a tartály!");
                    return false;
                }
                t.addEndDepoVolumeFlowRate(volumeFlowRate);
            }
            t.setHeadOfTheFluid(pipeLength);
            List<Integer> ls = depoConnections.get(pipeID).getFuelIDBefore();
            if (!ls.isEmpty()){
                for (int i : ls){
                    depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().remove(i);
                }
            }
        }
        List<Double> ls = new ArrayList<>();
        ls.add(t.getHeadOfTheFluid());
        ls.add(t.getTailOfTheFluid());
        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(t.getFuelID()+100,ls);
//        System.out.println(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(t.getFuelID()+100).get(0)+" ; "
//                            +depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(t.getFuelID()+100).get(1));
//        List<Integer> test = depoConnections.get(pipeID).getFuelIDBefore();
//        for (int i : test) {
//            System.out.println("TID: " + t.getTransportationID()
//                    +" FUELID: "+i+" HEAD: " + depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0)
//                    + " TAIL: " + depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1));
//        }
        return true;
    }


    private class RunSimulation implements Callable<Boolean> {
        TransportationPlan t;
        int pipeLength, pipeDiameter;
        double startDepoMovedFuelAmount = 0.0, endDepoMoveFuelAmount = 0.0;
        double flowVelocity;
        double tailOfTheFluid = 0.0;
        Depo startDepo, endDepo;
        String startDepoContainerID,endDepoContainerID, pipeID;
        String highestContainerCapacityID;
        private double headOfTheFluid;


        public RunSimulation(TransportationPlan t, Depo startDepo, Depo endDepo) {
            this.t = t;
            this.startDepo = startDepo;
            this.endDepo = endDepo;
            pipeID = t.getPipeID();
            pipeLength = depoConnections.get(pipeID).getPipeLength();
            pipeDiameter = depoConnections.get(pipeID).getPipeDiameter();
            flowVelocity = Simulation.volumeFlowRate / (Math.PI * Math.pow(((double) pipeDiameter / 2), 2));
//            System.out.println("Flow velocity: " + flowVelocity);
            startDepoContainerID = startDepo.getContainerID(t);
            endDepoContainerID = endDepo.getContainerID(t);
            highestContainerCapacityID = startDepo.getHighestCurrentCapacityContainer();
            currentHours = t.getStartHours();
            currentMinutes = t.getStartMinutes();
            for (Map.Entry<Integer, List<Double>> entry : depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().entrySet()) {
                System.out.println("Fuel ID: "+entry.getKey()+"   "+entry.getValue().get(0)+";"+entry.getValue().get(1));
            }
        }
        @Override
        public Boolean call()  {
            System.out.println("Simulation started......\n");
            System.out.println("PipeID:"+pipeID+"  From: " + t.getStartDepoID() + "  To: " + t.getEndDepoID() + "  Fuel: " + t.getFuelID() + "  Fuel Amount: " + t.getFuelAmount());
            System.out.println("Start Hours:: " + t.getStartHours() + "  Start Minutes: " + t.getStartMinutes());
            System.out.println("End Hours:: " + t.getEndHours() + "  End Minutes: " + t.getEndMinutes());
            int hours = t.getStartHours();
            while(hours < t.getEndHours()){
                int minutes = 0;
                while (minutes < 60){
                    try {
                        if (Thread.currentThread().isInterrupted()) return false;
                        if (!process()) return false;
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                    currentMinutes = minutes;
                    minutes++;
//                    for (Map.Entry<Integer, List<Double>> entry : depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().entrySet()) {
//                        System.out.println("Fuel ID: "+entry.getKey()+"   "+entry.getValue().get(0)+";"+entry.getValue().get(1));
//                    }
                }
                hours++;
                currentHours = hours;
                System.out.println("Current Hour("+t.getTransportationID()+"):" + hours);
            }
            int minutes = t.getStartMinutes();
            while(minutes < t.getEndMinutes()){
                try {
                    if (!process()) return false;
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                minutes++;
                currentMinutes = minutes;
            }
            if (tailOfTheFluid >= pipeLength){
                System.out.println("Transportation Complete");
                depoConnections.get(pipeID).setCurrentFuelID();
                return true;
            }
            else{
                errorMessages.add("A megadott idő intervallumban nem teljesíthető a "+t.getTransportationID()+" terv. " +
                        "Növelje az időintervallumot vagy csökkentse az átvinni kivánt anyagmennyiséget");
                System.out.println("Transportation Failed");
                return false;
            }

        }
        private boolean process() throws InterruptedException {
            sleep(Simulation.timeSpeed);
            if (startDepoMovedFuelAmount != t.getFuelAmount()) {
                startDepoMovedFuelAmount += volumeFlowRate;
                startDepo.getContainers().get(startDepoContainerID).substractCurrentCapacity(volumeFlowRate);
            }
            else {
                if (tailOfTheFluid < pipeLength){
                    if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity()-volumeFlowRate) < 0) {
                        highestContainerCapacityID = startDepo.getHighestCurrentCapacityContainer();
                        if (highestContainerCapacityID == null)
                            return false;
                        if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity() - volumeFlowRate) < 0) {
                            errorMessages.add("Nincs elég üzemanyag a vételi oldalon, hogy át lehessen vinni a tervben szereplő üzemanyagot. " +
                                    "Ellenőrizze a "+t.getTransportationID()+" tervet!");
                            System.out.println("Alulcsordulás");
                            return false;
                        }
                    }
                    tailOfTheFluid += flowVelocity;
                    startDepo.getContainers().get(highestContainerCapacityID).substractCurrentCapacity(volumeFlowRate);
                    List<Double> ls = new ArrayList<>();
                    ls.add(tailOfTheFluid);
                    ls.add((double) 0);
                    depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
                            .put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID()+100,ls);
                }
                else tailOfTheFluid = pipeLength;
            }
            if (headOfTheFluid < pipeLength) {
                headOfTheFluid += flowVelocity;
                List<Integer> ls = depoConnections.get(pipeID).getFuelIDBefore();
                if (!ls.isEmpty()){

                    for (int i : ls){
                        if ((endDepo.getContainers().get(endDepo.getContainerID(i)).getCurrentCapacity()+volumeFlowRate) >
                                endDepo.getContainers().get(endDepo.getContainerID(i)).getMaxCapacity()) {
                            errorMessages.add("A vételi oldalon megtelt a tartály! ");
                            System.out.println("Túlcsordulás");
                            return false;
                        }
                        endDepo.getContainers().get(endDepo.getContainerID(i)).addCurrentCapacity(volumeFlowRate);
                        List<Double> ll = new ArrayList<>();
                        ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0));
                        ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1)+volumeFlowRate);
                        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(i,ll);
                    }
                }
            }
            else {
                if (endDepoMoveFuelAmount != t.getFuelAmount()){
                    endDepo.getContainers().get(endDepoContainerID).addCurrentCapacity(volumeFlowRate);
                    endDepoMoveFuelAmount+=volumeFlowRate;
                }
                headOfTheFluid = pipeLength;
                List<Integer> ls = depoConnections.get(pipeID).getFuelIDBefore();
                if (!ls.isEmpty()){
                    for (int i : ls){
                        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().remove(i);
                    }
                }
            }
            List<Double> ls = new ArrayList<>();
            ls.add(headOfTheFluid);
            ls.add(tailOfTheFluid);
            depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(t.getFuelID()+100,ls);
            return true;
        }

    }

}
