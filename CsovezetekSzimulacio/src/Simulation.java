import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
// t=s/v
public class Simulation  {
    static final double volumeFlowRate = 1; //cubic meteres/s
    static final double pipeDiameter = 1; //m
    static final int pipeLength = 25000; //m
    static final int timeSpeed = 100; // 1 ms = 1perc
    double flowVelocity; //m/s
    private int startDepoContainerCapacity;
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
               ArrayList<String> ls = new DataBaseHandler().readRecordWithCondition("depocontainer","DepoID = '" + t.getStartDepoID()+"'");
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
//        for (String s : ls ){
//            System.out.print(s+"  ");
//        }
//        System.out.println("\n"+ls.get(9)+"\n");
        for (int i =4; i < ls.size(); i+=5){
            if (Integer.parseInt(ls.get(i)) == t.getFuelID()){
                System.out.println("Container Fuel Match.....OK");
                startDepoContainerCapacity = Integer.parseInt(ls.get(i-2));
                return true;
            }
        }
        System.out.println("Container Fuel Match.....Failed");
        return false;
    }
    private boolean checkCapacity(TransportationPlan t, ArrayList<String> ls) {
        for (int i = 2; i < ls.size(); i+=5) {
            if (Integer.parseInt(ls.get(i))+ (t.getFuelAmount()) < Integer.parseInt(ls.get(i+1))){
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
    void runSimulation(){
        sortByDate();
//        Thread timeSimulation = new Thread(new TimeSimulation(transportationPlans.get(0).getStartHours(), transportationPlans.get(0).getEndMinutes()));
//        TimeSimulation timeSimulation = new TimeSimulation(transportationPlans.get(0).getStartHours(), transportationPlans.get(0).getEndMinutes());
//        timeSimulation.start();
        for (TransportationPlan t : transportationPlans){
            System.out.println("Tid:" + t.getTransportationID());
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
        TimeSimulation timeSimulation = new TimeSimulation(t);
        timeSimulation.start();
        int movedFuelAmount = 0;
        double time = pipeLength/flowVelocity;
        while (timeSimulation.isAlive()){
            if (movedFuelAmount < t.getFuelAmount()){
                time--;
            }
            else{
                movedFuelAmount+=volumeFlowRate;
            }
            try {
                Thread.currentThread().sleep(timeSpeed);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (time <= 0){
                timeSimulation.interrupt();
                break;
            }
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
    //    private class TimeSimulation extends Thread{
//        private int hours, minutes;
//        private boolean yeet=false;
//        public TimeSimulation(int hours, int minutes) {
//            this.hours = hours;
//            this.minutes = minutes;
//        }
//        @Override
//        public void run() {
//            super.run();
//            while (hours < 11){
//                while (minutes < 60){
//                    while (yeet){}
//                    try {
//
//                        wait(timeSpeed);
//                    } catch (InterruptedException interruptedException) {
//                        interruptedException.printStackTrace();
//                    }
//                    minutes++;
//                }
//                minutes = 0;
//                System.out.println("Current hour:"+hours);
//                hours++;
//            }
//        }
//        public void setYeet(boolean y){
//            yeet = y;
//        }
//
//    };
//    private class TimeSimulation implements Runnable {
//        private TransportationPlan t;
//        private int hours, minutes;
//
//        public TimeSimulation(int hours, int minutes) {
//            this.hours = hours;
//            this.minutes = minutes;
//        }
//        public void run() {
//            while (hours < 11){
//                while (minutes < 60){
//                    try {
//                        wait(timeSpeed);
//                    } catch (InterruptedException interruptedException) {
//                        interruptedException.printStackTrace();
//                    }
//                    minutes++;
//                }
//                minutes = 0;
//                System.out.println("Current hour:"+hours);
//                hours++;
//            }
//        }
//    };
}
