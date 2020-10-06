import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Simulation  {
    static final int flowRate = 1;
    ArrayList <TransportationPlan> transportationPlans;

    private  void sortByDate(){
        Comparator<? super TransportationPlan> comparator = new Comparator<TransportationPlan>() {
            @Override
            public int compare(TransportationPlan o1, TransportationPlan o2) {
                return o1.getStartDate().compareTo(o2.getEndDate()) ;
            }
        };
        transportationPlans.sort(comparator);
    }
    public Simulation() {
        transportationPlans = new ArrayList<>();
    }
    public ArrayList<TransportationPlan> getTransportationPlans() {
        return transportationPlans;
    }
    public void setTransportationPlans(ArrayList<TransportationPlan> transportationPlans) {
        this.transportationPlans = transportationPlans;
    }
    public void addTransportationPlan(TransportationPlan tp){
        transportationPlans.add(tp);
    }

    void runSimulation(){
        sortByDate();
        for (TransportationPlan t: transportationPlans){
            System.out.println("TID: "+t.getTransportationID() + "Sdate: "+ t.getStartDate()+ "Edate: " + t.getEndDate());
        }
    }

}
