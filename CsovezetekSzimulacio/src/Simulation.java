import java.util.ArrayList;
import java.util.Collections;

public class Simulation  {
    static final int flowRate = 1;
    ArrayList <TransportationPlan> transportationPlans;

    public Simulation() {
        transportationPlans = new ArrayList<>();
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
                System.out.println("All check.....OK");
            }else {
                System.out.println("All check.....Failed");
            }
            System.out.println();
        }

    }
}
