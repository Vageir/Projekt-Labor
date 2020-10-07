import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransportationPlan {
    private String transportationID;
    private String startDepoID;
    private String endDepoID;
    private int fuelID;
    private int fuelAmount;
    private String startDate;
    private String endDate;
    private String operatorID;

    public TransportationPlan(ArrayList<String> ls){
        this.transportationID = ls.get(0);
        this.startDepoID = ls.get(1);
        this.endDepoID = ls.get(2);
        this.fuelID = Integer.parseInt(ls.get(3));
        this.fuelAmount = Integer.parseInt(ls.get(4));
        this.startDate = ls.get(5);
        this.endDate = ls.get(6);
        this.operatorID = ls.get(7);
    }

//    public  ArrayList<Integer> dateComponent(String date){
//        ArrayList<Integer> ls = new ArrayList<>();
//        String[] s = date.substring(0,10).split("-");
//        for (String ss : s){
//            ls.add(Integer.parseInt(ss));
//        }
//        return ls;
//    }


    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getTransportationID() {
        return transportationID;
    }
    public void setTransportationID(String transportationID) {
        this.transportationID = transportationID;
    }
    public String getStartDepoID() {
        return startDepoID;
    }
    public void setStartDepoID(String startDepoID) {
        this.startDepoID = startDepoID;
    }
    public String getEndDepoID() {
        return endDepoID;
    }
    public void setEndDepoID(String endDepoID) {
        this.endDepoID = endDepoID;
    }
    public int getFuelID() {
        return fuelID;
    }
    public void setFuelID(int fuelID) {
        this.fuelID = fuelID;
    }
    public int getFuelAmount() {
        return fuelAmount;
    }
    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }
    public String getOperatorID() {
        return operatorID;
    }
    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }
}
