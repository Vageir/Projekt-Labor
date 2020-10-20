import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Thread.sleep;

//v = Q / (π · (ø / 2)²)
//        Q = Volume flow rate
//        v = Flow velocity
//        ø = Diameter
// t=s/v
public class Simulation  {
    static final double volumeFlowRate = 100; //cubic meteres/min
    static final double pipeDiameter = 1; //m
    static final int pipeLength = 10000; //m
    static final int timeSpeed = 100; // 1 ms = 1perc
    double flowVelocity; //m/min
    private int startDepoCurrentCapacity,endDepoCurrentCapacity;
    private String startDepoContainerID,endDepoContainerID;
    List <TransportationPlan> transportationPlans;

    public Simulation() {
        transportationPlans = new ArrayList<>();
        flowVelocity = volumeFlowRate/(Math.PI*Math.pow(((double)pipeDiameter/2),2));
        System.out.println("The flow velocity: "+flowVelocity);
    }
    public List<TransportationPlan> getTransportationPlans() {
        return transportationPlans;
    }
    public void addTransportationPlan(TransportationPlan tp){
        transportationPlans.add(tp);
    }
    public void setTransportationPlans(ArrayList<TransportationPlan> transportationPlans) {
//        List<TransportationPlan> tmp = new LinkedList<TransportationPlan>();
//        for (TransportationPlan t : transportationPlans){
//            tmp.add(t);
//        }
        this.transportationPlans = transportationPlans;
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
    public boolean runSimulation(){
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
             if (!startSimulation(runTransportationPlans)) return false;
            } else {
                System.out.println("All check.....Failed");
                System.out.println("Hiba! Ellenőrzie a : " + transportationPlans.get(i).getTransportationID() + " tervet");
                return false;
            }
        }
        return true;
    }


    private boolean startSimulation(List<TransportationPlan> runTransportationPlans){
        List<Thread> threads = new ArrayList<>();
        for (TransportationPlan t : runTransportationPlans){
//            if (!startSimulation(t)) {
//                System.out.println("Hiba! A "+t.getTransportationID()+" terv nem hajtható végre a megadott intervallumban");
//                return false;
//            }
            Thread thread = new Thread(new RunSimulation(t));
            threads.add(thread);
        }
        for (Thread thread:threads){
            thread.start();
        }
        for (Thread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        return true;
    }
//    private boolean startSimulation(TransportationPlan t) {
//        System.out.println("Simulation started......\n");
//        System.out.println("From: "+t.getStartDepoID()+"  To: "+ t.getEndDepoID()+"  Fuel: "+t.getFuelID()+"  Fuel Amount: "+t.getFuelAmount());
//        System.out.println("Start Hours:: "+ t.getStartHours()+"  Start Minutes: " + t.getStartMinutes());
//        System.out.println("End Hours:: "+ t.getEndHours()+"  End Minutes: " + t.getEndMinutes());
//        int movedFuelAmount = 0;
//        double time = pipeLength/flowVelocity;
//        int hours = t.getStartHours(), minutes = t.getStartMinutes();
//        while (hours < t.getEndHours()){
//            while (minutes < 60){
//                try{
//                    Thread.currentThread().sleep(timeSpeed);
//                    if (movedFuelAmount != t.getFuelAmount())
//                        movedFuelAmount+=volumeFlowRate;
//                    else
//                        time--;
//                }catch (InterruptedException ie){
//                    ie.printStackTrace();
//                }
//                minutes++;
//            }
//            minutes = 0;
//            System.out.println("Current hour:"+hours);
//            hours++;
//        }
//        if (t.getEndMinutes() > 0){
//            minutes = 0;
//            while (minutes < t.getEndMinutes()){
//                try {
//                    Thread.currentThread().sleep(timeSpeed);
//                    if (movedFuelAmount != t.getFuelAmount())
//                        movedFuelAmount+=volumeFlowRate;
//                    else
//                        time--;
//                }catch (InterruptedException ie){
//                    ie.printStackTrace();
//                }
//                minutes++;
//            }
//        }
//        if (movedFuelAmount == t.getFuelAmount() && time <= 0){
//            System.out.println("Transportation Complete.....");
//            new DataBaseHandler().updateRecord("depocontainer",
//                    "currentcapacity",String.valueOf(startDepoCurrentCapacity-t.getFuelAmount()),"containerID = '"+startDepoContainerID+"'");
////            System.out.println(String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity));
//            new DataBaseHandler().updateRecord("depocontainer",
//                    "currentcapacity",String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity),"containerID = '"+endDepoContainerID+"'");
////            System.out.println(String.valueOf(t.getFuelAmount()+endDepoCurrentCapacity));
//            return true;
//        }
//        else{
//            System.out.println("Transportation Failed.....");
//            return false;
//        }
//    }

    private class RunSimulation implements Runnable {
        TransportationPlan t;

        public RunSimulation(TransportationPlan t) {
            this.t = t;
        }

        @Override
        public void run() {
            System.out.println("Simulation started......\n");
            System.out.println("From: " + t.getStartDepoID() + "  To: " + t.getEndDepoID() + "  Fuel: " + t.getFuelID() + "  Fuel Amount: " + t.getFuelAmount());
            System.out.println("Start Hours:: " + t.getStartHours() + "  Start Minutes: " + t.getStartMinutes());
            System.out.println("End Hours:: " + t.getEndHours() + "  End Minutes: " + t.getEndMinutes());
            int movedFuelAmount = 0;
            double time = pipeLength / flowVelocity;
            int hours = t.getStartHours(), minutes = t.getStartMinutes();
            while (hours < t.getEndHours()) {
                while (minutes < 60) {
                    try {
                        Thread.currentThread().sleep(timeSpeed);
                        if (movedFuelAmount != t.getFuelAmount())
                            movedFuelAmount += volumeFlowRate;
                        else
                            time--;
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    minutes++;
                }
                minutes = 0;
                System.out.println("Current hour:("+t.getTransportationID()+")"+ hours);
                hours++;
            }
            if (t.getEndMinutes() > 0) {
                minutes = 0;
                while (minutes < t.getEndMinutes()) {
                    try {
                        Thread.currentThread().sleep(timeSpeed);
                        if (movedFuelAmount != t.getFuelAmount())
                            movedFuelAmount += volumeFlowRate;
                        else
                            time--;
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    minutes++;
                }
            }
            if (movedFuelAmount == t.getFuelAmount() && time <= 0) {
                System.out.println("Transportation Complete.....");
                new DataBaseHandler().updateRecord("depocontainer",
                        "currentcapacity", String.valueOf(startDepoCurrentCapacity - t.getFuelAmount()), "containerID = '" + startDepoContainerID + "'");
                new DataBaseHandler().updateRecord("depocontainer",
                        "currentcapacity", String.valueOf(t.getFuelAmount() + endDepoCurrentCapacity), "containerID = '" + endDepoContainerID + "'");
            } else {
                System.out.println("Transportation Failed.....");
            }
        }

        ;
    }
}
