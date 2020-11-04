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
    public String getPipeID(String startDepoID, String endDepoID){
        for (Map.Entry<String,DepoConnection> entry : depoConnections.entrySet()){
            if (entry.getValue().getLeftDepoID().equals(startDepoID) && entry.getValue().getRightDepoID().equals(endDepoID)) return entry.getKey();
            else if(entry.getValue().getLeftDepoID().equals(endDepoID) && entry.getValue().getRightDepoID().equals(startDepoID)) return entry.getKey();

        }
        return null;
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
        if (runSimulations()){
            System.out.println("Sikeres lefutás");
            //database update
        }else {
            System.out.println("Hibás lefutás az adatbázis nem módosult");
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
//        for (Depo d:depos){
//            d.readDepoDatas();
//        }
    }
    private boolean checkContainer(String tID){
        for(TransportationPlan t : transportationPlans){
            if (t.getTransportationID().equals(tID)){
                for (Depo d:depos){
                    if (t.getStartDepoID().equals(d.getDepoID())){
//                        System.out.println("Start Depo Fuel and Container Check");
                        if (!checkStartFuelAndCapacity(t,d)){
                            return false;
                        }
                    }
                }
                for (Depo d:depos){
                    if (t.getEndDepoID().equals(d.getDepoID())){
//                        System.out.println("End Depo Fuel and Container Check");
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
//                System.out.println("End Container Fuel and Capacity.....OK");
                return true;
            }
        }
//        System.out.println("End Container Fuel and Capacity.....Failed");
        errorMessages.add("Nincs elég kapacitás/nem létezik olyan tipusú tartály a vételi oldalon. Ellenőrizze a "+t.getTransportationID()+" tervet!");
        return false;
    }
    private boolean checkStartFuelAndCapacity(TransportationPlan t, Depo d) {
        for (Map.Entry<String, Depo.DepoContainer> entry : d.getContainers().entrySet()){
            if (entry.getValue().getFuelID()== t.getFuelID() && entry.getValue().getCurrentCapacity() >= t.getFuelAmount()){
//                System.out.println("Start Container Fuel and Capacity.....OK");
                return true;
            }
        }
//        System.out.println("Start Container Fuel and Capacity.....Failed");
        errorMessages.add("Nincs elég kapacitás/nem létezik olyan tipusú tartály a küldő oldalon. Ellenőrizze a "+t.getTransportationID()+" tervet!");
        return false;
    }
    private boolean checkConnection(String tID){
//        System.out.println("Depo Connection Check");
        for (TransportationPlan t : transportationPlans){
            if (t.getTransportationID().equals(tID)){
                if (getPipeID(t.getStartDepoID(),t.getEndDepoID()) != null) return true;
            }
        }
//        System.out.println("Depo Connection.....Failed");
        errorMessages.add("A küldő és vételi oldal között nincs összeköttetés. Ellenőrizze a "+ tID+" tervet!");
        return false;
    }
    private boolean runSimulations(){
        for (int i = 0; i < transportationPlans.size(); i++ ) {
            System.out.println("\nTid:" + transportationPlans.get(i).getTransportationID());
            if (checkConnection(transportationPlans.get(i).getTransportationID()) && checkContainer(transportationPlans.get(i).getTransportationID())) {
//                System.out.println("All check.....OK\n");
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
                            errorMessages.add("Ugyanazon időpillantban egyszerre nem mehet két terv. Ellenőrizze a "
                                    +transportationPlans.get(i).getTransportationID()
                                    + " és a "+transportationPlans.get(j).getTransportationID()+" terveket!");
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
                                System.out.println("Hiba! Ellenőrizze a "+transportationPlans.get(j).getTransportationID()+ " tervet");
                                return false;
                            }

                        }else break;

                }
                int finalI = i;
                int finalJ = j;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (transportationPlans.size() > finalJ){
                            if (transportationPlans.get(finalJ).getStartHours() < transportationPlans.get(finalI).getEndHours()
                                    ||(transportationPlans.get(finalJ).getStartHours() == transportationPlans.get(finalI).getEndHours()
                                    && transportationPlans.get(finalJ).getStartMinutes() < transportationPlans.get(finalI).getEndMinutes()))
                            {
                                System.out.println(transportationPlans.get(finalJ).getStartHours()+":"+transportationPlans.get(finalJ).getStartMinutes() );
                                while(currentHours != transportationPlans.get(finalJ).getStartHours()
                                        || currentMinutes != transportationPlans.get(finalJ).getStartMinutes()){}
                                executor.shutdownNow();
                                errorMessages.add("A terv nem indulhat el amig a másik be nem fejeződött");
                            }
                        }
                    }
                });
                t.start();
                if (!startSimulation(runTransportationPlans)){
//                 System.out.println("Hiba a szimulációban");
                 return false;
             }
            }
            else {
//                System.out.println("All check.....Failed");
//                System.out.println("Hiba! Ellenőrzie a : " + transportationPlans.get(i).getTransportationID() + " tervet");
                return false;
            }

        }
        return true;
    }
    private boolean startSimulation(List<TransportationPlan> runTransportationPlans){
        executor = Executors.newFixedThreadPool(runTransportationPlans.size());
        futureList = new ArrayList<Future<Boolean>>();
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
                Future<Boolean> future = executor.submit(runSimulation);
                futureList.add(future);
            }else{
                System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeet");
            }
        }
        for (Future future : futureList){
            try {
                if(!(Boolean)future.get()){
                    return false;
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return true;
    }


    private class RunSimulation implements Callable<Boolean> {
        TransportationPlan t;
        int pipeLength = 0, pipeDiameter = 0;
        double startDepoMovedFuelAmount = 0.0, endDepoMoveFuelAmount = 0.0;
        double flowVelocity = 0.0;
        double tailOfTheFluid = 0.0;
        Depo startDepo, endDepo;
        String startDepoContainerID,endDepoContainerID, pipeID;
        String highestContainerCapacityID;
        private double headOfTheFluid;


        public RunSimulation(TransportationPlan t, Depo startDepo, Depo endDepo) {
            this.t = t;
            this.startDepo = startDepo;
            this.endDepo = endDepo;
            pipeID = getPipeID(startDepo.getDepoID(),endDepo.getDepoID());
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
            Thread.currentThread().sleep(Simulation.timeSpeed);
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
