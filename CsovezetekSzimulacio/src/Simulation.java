import java.util.ArrayList;
import java.util.Collections;
//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
public class Simulation  {
    static final double volumeFlowRate = 1; //cubic meteres/s
    static final double pipeDiameter = 0.3; //m
    static final int pipeLength = 400000; //m
    static final int timeSpeed = 100; //ms
    double flowVelocity; //m/s
    ArrayList <TransportationPlan> transportationPlans;

    public Simulation() {
        transportationPlans = new ArrayList<>();
        flowVelocity = volumeFlowRate/(Math.PI*Math.pow(((double)pipeDiameter/2),2));
        System.out.println("The flow velocity: "+flowVelocity);
    }
    public ArrayList<TransportationPlan> getTransportationPlans() {
        return transportationPlans;
    }
    public void addTransportationPlan(TransportationPlan tp){
        transportationPlans.add(tp);
    }
    public void setTransportationPlans(ArrayList<TransportationPlan> transportationPlans) {
        this.transportationPlans = transportationPlans;
    }
    private  void sortByDate(){
        Collections.sort(transportationPlans, (o1, o2) -> (o1.getStartDate().compareTo(o2.getStartDate())));
    }
    private boolean checkContainer(String tID){
        for(TransportationPlan t : transportationPlans){
            if (t.getTransportationID().equals(tID)){
               ArrayList<String> ls = new DataBaseHandler().readOneRecord("depocontainer","DepoID = '" + t.getStartDepoID()+"'");
                if (checkFuelMatch(t, ls) && checkCapacity(t, ls)) {
                    System.out.println("Container check.....OK");
                    return true;
                }else {
                    System.out.println("Conatiner check.....Failed");
                    return false;
                }
            }
        }
        return false;
    }
    private boolean checkFuelMatch(TransportationPlan t, ArrayList<String> ls) {
//        System.out.println(ls.size());
        if (Integer.parseInt(ls.get(4)) == t.getFuelID()){
            System.out.println("Container Fuel Match.....OK");
            return true;
        }
        System.out.println("Container Fuel Match.....Failed");
        return false;
    }
    private boolean checkCapacity(TransportationPlan t, ArrayList<String> ls) {
        if (Integer.parseInt(ls.get(2))+ (t.getFuelAmount()) < Integer.parseInt(ls.get(3))){
            System.out.println("Container Capacity.....OK");
            return true;
        }
        System.out.println("Container Capacity.....Failed");
        return false;
    }
    private boolean checkConnection(String tID){
        for(TransportationPlan t : transportationPlans) {
            if (t.getTransportationID().equals(tID)) {
                ArrayList<String> ls = new DataBaseHandler().readOneRecord(
                        "connecteddepos", "LeftDepoID = '"+ t.getStartDepoID()+"' and RightDepoID = '"+t.getEndDepoID()+"'");
                if (!ls.isEmpty()){
                    System.out.println("Depo Connection.....OK");
                    return true;
                } else{
                    System.out.println("Depo Connection.....Failed");
                    return false;
                }
            }
        }
        return false;
    }
    void runSimulation(){
        sortByDate();
        for (TransportationPlan t : transportationPlans){
            System.out.println("Tid:" + t.getTransportationID());
            if(checkConnection(t.getTransportationID()) && checkContainer(t.getTransportationID())){
                System.out.println("All check.....OK\n");
                startSimulation(t);
            }else {
                System.out.println("All check.....Failed");
            }
            System.out.println();
        }

    }

    private void startSimulation(TransportationPlan t) {
        System.out.println("Simulation started......\n");
        System.out.println("From: "+t.getStartDepoID()+"  To: "+ t.getEndDepoID()+"  Fuel: "+t.getFuelID()+"  Fuel Amount: "+t.getFuelAmount());
        System.out.println("Start Hours:: "+ t.getStartHours()+"  Start Minutes: " + t.getStartMinutes());
        System.out.println("End Hours:: "+ t.getEndHours()+"  End Minutes: " + t.getEndMinutes());
        TimeSimulation timeSimulation = new TimeSimulation(t);
        long startTime = System.nanoTime();
        timeSimulation.start();
        double d = 0.0;
        while (timeSimulation.isAlive()){
            try {
                Thread.sleep(1,6);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
            d += flowVelocity;
            if (d>=pipeLength/3){

            }
            if (d >= (double)pipeLength){
                System.out.println("Tranpsortation complete.....\n");
                timeSimulation.stop();
                break;
            }
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed time in seconds: "+(double)elapsedTime / 1_000_000_000.0);
        if (d< pipeLength){
            System.out.println("Transportation failed.....\n");
        }
    }
    private class TimeSimulation extends Thread {
        private TransportationPlan t;
        private int hours, minutes;
        public TimeSimulation(TransportationPlan t) {
            this.t = t;
            this.hours = t.getStartHours();
            this.minutes = t.getStartMinutes();
        }
        public void run() {
            long startTime = System.nanoTime();
            while (hours < t.getEndHours()){
                while (minutes < 60){
                    try{
                        TimeSimulation.sleep(timeSpeed);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    minutes++;
                }
                minutes = 0;
                System.out.println("Current hour:"+hours);
                hours++;
            }
            if (t.getEndMinutes() > 0){
                minutes = 0;
                while (minutes < t.getEndMinutes()){
                    try {
                        TimeSimulation.sleep(timeSpeed);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    minutes++;
                }
            }
        }
    };
}
