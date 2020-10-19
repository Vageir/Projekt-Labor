import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
// t=s/v
public class Simulation  {
    static final double volumeFlowRate = 1; //cubic meteres/min
    static final double pipeDiameter = 1; //m
    static final int pipeLength = 10000; //m
    static final int timeSpeed = 100; // 1 ms = 1perc
    double flowVelocity; //m/min
    private int startDepoCurrentCapacity,endDepoCurrentCapacity;
    private String startDepoContainerID,endDepoContainerID;
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
                ArrayList<String> startDepoContainer = new DataBaseHandler().readRecordWithCondition("depocontainer","DepoID = '" + t.getStartDepoID()+"'");
                ArrayList<String> endDepoContainer = new DataBaseHandler().readRecordWithCondition("depocontainer","DepoID = '" + t.getEndDepoID()+"'");
               System.out.println("Start Depo Fuel and Container Check");
               if (!checkEnoughFuel(t,startDepoContainer)) {
                   System.out.println("Container check.....Failed");
                   System.out.println("Start Depo Fuel and Container Check.....Failed");
                   return false;
               }
               System.out.println("Container check.....OK");
                System.out.println("Start Depo Fuel and Container Check.....OK\n");
               System.out.println("End Depo Fuel and Container Check");
               if (checkFuelAndCapacity(t, endDepoContainer)) {
                    System.out.println("Container check.....OK");
                   System.out.println("End Depo Fuel and Container Check.....OK");
                    return true;
               }else {
                    System.out.println("Conatiner check.....Failed");
                   System.out.println("End Depo Fuel and Container Check.....Failed");
                    return false;
               }
            }
        }
        return false;
    }
    private boolean checkFuelAndCapacity(TransportationPlan t, ArrayList<String> ls) {
        for (int i =4; i < ls.size(); i+=5){
            if (Integer.parseInt(ls.get(i)) == t.getFuelID() && (Integer.parseInt(ls.get(i-2))+ (t.getFuelAmount()) <= Integer.parseInt(ls.get(i-1)))){
                System.out.println("Container Fuel and Capacity.....OK");
                endDepoCurrentCapacity = Integer.parseInt(ls.get(i - 2));
                endDepoContainerID = ls.get(i - 3);
                return true;
            }
        }
        System.out.println("Container Fuel and Capacity.....Failed");
        return false;
    }
    private boolean checkEnoughFuel(TransportationPlan t, ArrayList<String> ls) {
        for (int i =4; i < ls.size(); i+=5){
            if (Integer.parseInt(ls.get(i)) == t.getFuelID() && Integer.parseInt(ls.get(i-2))>=t.getFuelAmount()){
                System.out.println("Container Fuel and Capacity.....OK");
                startDepoCurrentCapacity = Integer.parseInt(ls.get(i - 2));
                startDepoContainerID = ls.get(i - 3);
                return true;
            }
        }
        System.out.println("Container Fuel and Capacity.....Failed");
        return false;
    }
    private boolean checkCapacity(TransportationPlan t, ArrayList<String> ls) {
        for (int i = 2; i < ls.size(); i+=5) {
            if (Integer.parseInt(ls.get(i))+ (t.getFuelAmount()) <= Integer.parseInt(ls.get(i+1))){
                endDepoCurrentCapacity=Integer.parseInt(ls.get(i));
                endDepoContainerID = ls.get(i-1);
                endDepoCurrentCapacity=Integer.parseInt(ls.get(i));
                endDepoContainerID = ls.get(i-1);
                System.out.println("Container Capacity.....OK");
                return true;
            }
        }
        System.out.println("Container Capacity.....Failed");
        return false;
    }
    private boolean checkConnection(String tID){
        for(TransportationPlan t : transportationPlans) {
            if (t.getTransportationID().equals(tID)) {
                if(!new DataBaseHandler().readRecordWithCondition(
                        "connecteddepos", "LeftDepoID = '"+ t.getStartDepoID()+"' and RightDepoID = '"+t.getEndDepoID()+"'").isEmpty()
                    || !new DataBaseHandler().readRecordWithCondition(
                        "connecteddepos", "LeftDepoID = '"+ t.getEndDepoID()+"' and RightDepoID = '"+t.getStartDepoID()+"'").isEmpty()){
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
    public void runSimulation(){
        sortByDate();
        for (TransportationPlan t : transportationPlans){
            System.out.println("\nTid:" + t.getTransportationID());
            if(checkConnection(t.getTransportationID()) && checkContainer(t.getTransportationID())){
                System.out.println("All check.....OK\n");
                startSimulation(t);
            }
            else {
                System.out.println("All check.....Failed");
            }
        }


    }
    private void startSimulation(TransportationPlan t) {
        System.out.println("Simulation started......\n");
        System.out.println("From: "+t.getStartDepoID()+"  To: "+ t.getEndDepoID()+"  Fuel: "+t.getFuelID()+"  Fuel Amount: "+t.getFuelAmount());
        System.out.println("Start Hours:: "+ t.getStartHours()+"  Start Minutes: " + t.getStartMinutes());
        System.out.println("End Hours:: "+ t.getEndHours()+"  End Minutes: " + t.getEndMinutes());
        int movedFuelAmount = 0;
        double time = pipeLength/flowVelocity;
        int hours = t.getStartHours(), minutes = t.getStartMinutes();
        while (hours < t.getEndHours()){
            while (minutes < 60){
                try{
                    Thread.currentThread().sleep(timeSpeed);
                    if (movedFuelAmount != t.getFuelAmount())
                        movedFuelAmount+=volumeFlowRate;
                    else
                        time--;
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
                    Thread.currentThread().sleep(timeSpeed);
                    if (movedFuelAmount != t.getFuelAmount())
                        movedFuelAmount+=volumeFlowRate;
                    else
                        time--;
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                minutes++;
            }
        }
        if (movedFuelAmount == t.getFuelAmount() && time <= 0){
            System.out.println("Transportation Complete.....");
            new DataBaseHandler().updateRecord("depocontainer",
                    "currentcapacity",String.valueOf(startDepoCurrentCapacity-t.getFuelAmount()),"containerID = '"+startDepoContainerID+"'");
            System.out.println(String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity));
            new DataBaseHandler().updateRecord("depocontainer",
                    "currentcapacity",String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity),"containerID = '"+endDepoContainerID+"'");
            System.out.println(String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity));
        }
        else{
            System.out.println("Transportation Failed.....");
        }
    }
//    private class TimeSimulation extends Thread {
//        private TransportationPlan t;
//        private int hours, minutes;
//        public TimeSimulation(TransportationPlan t) {
//            this.t = t;
//            this.hours = t.getStartHours();
//            this.minutes = t.getStartMinutes();
//        }
//        public void run() {
//            while (hours < t.getEndHours()){
//                while (minutes < 60){
//                    try{
//                        TimeSimulation.sleep(timeSpeed);
//                    }catch (InterruptedException ie){
//                        ie.printStackTrace();
//                    }
//                    minutes++;
//                }
//                minutes = 0;
//                System.out.println("Current hour:"+hours);
//                hours++;
//            }
//            if (t.getEndMinutes() > 0){
//                minutes = 0;
//                while (minutes < t.getEndMinutes()){
//                    try {
//                        TimeSimulation.sleep(timeSpeed);
//                    }catch (InterruptedException ie){
//                        ie.printStackTrace();
//                    }
//                    minutes++;
//                }
//            }
//        }
//    };

}
