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
//                for (Depo depo: depos){
//                    for (Map.Entry<String, Depo.DepoContainer> entry : depo.getContainers().entrySet()){
//                        new DataBaseHandler().
//                                updateRecord("depocontainer","currentcapacity",
//                                        String.valueOf(entry.getValue().getCurrentCapacity()),"containerID='"+entry.getKey()+"'");
//                    }
//                }
            }else {
                System.out.println("Hibás lefutás az adatbázis nem módosult");
                for (String s : errorMessages){
                    System.out.println(s);
                }
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
//                        System.out.println(currentHours+":"+currentMinutes);
                        t.setTimeToRun(true);
                        t.setHighestContainerCapacityID(getDepo(t.getStartDepoID()).getHighestCurrentCapacityContainer());
                        depoConnections.get(t.getPipeID()).setDirection(t);
                    }
                    if (t.isTimeToRun()){
                        if(!process(t)) return false;
                        if (!t.isFinished()){
                            if (t.getTailOfTheFluid() >= depoConnections.get(t.getPipeID()).getPipeLength()){
//                                System.out.println("TAIL:"+t.getTailOfTheFluid()+" PLength:"+depoConnections.get(t.getPipeID()).getPipeLength());
//                                System.out.println(t.getEndHours());
                                if (t.getEndHours() >= currentHours) {
                                    t.setFinished(true);
                                    t.setTimeToRun(false);
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
            if (!t.isFinished()) {
                errorMessages.add("Nincs elég idő a teveket lefuttatásához");
                return false;
            }
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
        double flowVelocity = t.getFlowVelocity();
//        if (t.getTransportationID().equals("2")){
//            for (Map.Entry<Integer,List<Double>> entry : depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().entrySet()) {
//                System.out.println("FUELID: " + entry.getKey());
//                System.out.println("HEAD: " + entry.getValue().get(0) + " TAIL: " + entry.getValue().get(1));
//
//            }
//            System.out.println("-------------");
//        }
        if (t.getStartDepoMovedFuelAmount() != t.getFuelAmount()) {
            if (startDepo.getContainers().get(startDepoContainerID).getCurrentCapacity()-volumeFlowRate < 0){
                errorMessages.add("Nincs elég üzemanyag az induló oldalon! Ellenőrizze a "+t.getTransportationID()+" tervet!");
                return false;
            }
            t.addStartDepoVolumeFlowRate(volumeFlowRate);
            startDepo.getContainers().get(startDepoContainerID).substractCurrentCapacity(volumeFlowRate);
        }
        else {
            if (t.getTailOfTheFluid() < (double)pipeLength){
                if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity()-volumeFlowRate) < 0) {
//                    System.out.println(t.getTransportationID()+" :lmao");
                    highestContainerCapacityID = startDepo.getHighestCurrentCapacityContainer();
                    t.setHighestContainerCapacityID(highestContainerCapacityID);
                    if (highestContainerCapacityID == null) {
                        errorMessages.add("Nincs elég üzemanyag az induló oldalon, hogy át lehessen vinni a tervben szereplő üzemanyagot. " +
                                "Ellenőrizze a "+t.getTransportationID()+" tervet!");
                        return false;
                    }
                    if ((startDepo.getContainers().get(highestContainerCapacityID).getCurrentCapacity() - volumeFlowRate) < 0) {
                        errorMessages.add("Nincs elég üzemanyag az induló oldalon, hogy át lehessen vinni a tervben szereplő üzemanyagot. " +
                                "Ellenőrizze a "+t.getTransportationID()+" tervet!");
                        System.out.println("Alulcsordulás");
                        return false;
                    }
                    depoConnections.get(pipeID).addPushFluidID(startDepo.getContainers().get(highestContainerCapacityID).getFuelID()+100);
                    List<Double> l = new ArrayList<>();
                    l.add(0.0);l.add(0.0);
                    depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID()+100,l);
                }
                t.addTailOfTheFluid(flowVelocity);
                startDepo.getContainers().get(highestContainerCapacityID).substractCurrentCapacity(volumeFlowRate);
                if (!depoConnections.get(pipeID).getPushFluidID().isEmpty()){
//                    System.out.println("yo");
                    for (int i: depoConnections.get(pipeID).getPushFluidID()) {
                        List<Double> l = new ArrayList<>();

                            l.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().
                                    get(i).get(0) + flowVelocity);
                            l.add(0.0);
                            depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(i, l);

                    }
                    if (depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().
                            get(t.getFuelID()+1000) != null) {
                        List<Double> ls = new ArrayList<>();
                        ls.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().
                                get(t.getFuelID() + 1000).get(0) + flowVelocity);
                        ls.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().
                                get(t.getFuelID() + 1000).get(1) + flowVelocity);
//                        System.out.println(ls.get(0)+";"+ls.get(1));
                        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(t.getFuelID()+1000,ls);
                    }
                }
                else {
//                    if (t.getTransportationID().equals("2")) {
//                        System.out.println("nice");
//                    }
                    List<Double> ls = new ArrayList<>();
                    ls.add(t.getTailOfTheFluid());
                    ls.add(0.0);
                    if (startDepo.getContainers().get(highestContainerCapacityID).getFuelID() == t.getFuelID()){
//                        System.out.println(ls.get(0)+";"+ls.get(1));
                        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
                                .put(t.getFuelID()+1000, ls);
                    }
                    else  depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
                                .put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID() + 100, ls);

                }
//                List<Double> ls = new ArrayList<>();
//                ls.add(t.getTailOfTheFluid());
//                ls.add((double) 0);
//                depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo()
//                        .put(startDepo.getContainers().get(highestContainerCapacityID).getFuelID() + 100, ls);

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
//            if (t.getTransportationID().equals("3")){
//                System.out.println(ls.isEmpty());
//            }
            if (!ls.isEmpty()){
                for (int i : ls) {
                    if (depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0) >= pipeLength &&
                            depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1) <
                                    depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0)) {
                        if ((endDepo.getContainers().get(endDepo.getContainerID(i)).getCurrentCapacity() + volumeFlowRate) >
                                endDepo.getContainers().get(endDepo.getContainerID(i)).getMaxCapacity()) {
                            errorMessages.add("A vételi oldalon megtelt a tartály! ");
                            System.out.println("Túlcsordulás");
                            return false;
                        }
                        endDepo.getContainers().get(endDepo.getContainerID(i)).addCurrentCapacity(volumeFlowRate);
                    }
                    List<Double> ll = new ArrayList<>();
                    if (depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1) <
                            depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0)) {
                        if (depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0) >= pipeLength){
                            ll.add((double) pipeLength);
                        }else {
                            ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(0) + flowVelocity);
                        }
                        ll.add(depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().get(i).get(1) + flowVelocity);
                        depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().put(i, ll);
                    }
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
//        if (t.getTransportationID().equals("2")) {
//            for (Map.Entry<Integer, List<Double>> entry : depoConnections.get(pipeID).getHeadAndTailOfTheFluidRelativeToLeftDepo().entrySet()) {
//                System.out.println(entry.getKey());
//                System.out.println("HEAD: "+entry.getValue().get(0)+" TAIL: "+entry.getValue().get(1));
//            }
//        }
        return true;
    }
}
