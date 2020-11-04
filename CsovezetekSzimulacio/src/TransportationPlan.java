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
    private String pipeID;



    public TransportationPlan(ArrayList<String> ls){
        this.transportationID = ls.get(0);
        this.startDepoID = ls.get(1);
        this.endDepoID = ls.get(2);
        this.fuelID = Integer.parseInt(ls.get(3));
        this.fuelAmount = Integer.parseInt(ls.get(4));
        this.startDate = ls.get(5);
        this.endDate = ls.get(6);
        this.operatorID = ls.get(7);
        this.pipeID = ls.get(8);
    }

//    public  ArrayList<Integer> dateComponent(String date){
//        ArrayList<Integer> ls = new ArrayList<>();
//        String[] s = date.substring(0,10).split("-");
//        for (String ss : s){
//            ls.add(Integer.parseInt(ss));
//        }
//        return ls;
//    }
    public String getPipeID() {
        return pipeID;
    }
    public int getStartHours(){
        return Integer.parseInt(startDate.substring(11,13));
    }
    public int getStartMinutes(){
        return Integer.parseInt(startDate.substring(14,16));
    }
    public int getEndHours(){
        return Integer.parseInt(endDate.substring(11,13));
    }
    public int getEndMinutes(){
        return Integer.parseInt(endDate.substring(14,16));
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getTransportationID() {
        return transportationID;
    }
    public String getStartDepoID() {
        return startDepoID;
    }
    public String getEndDepoID() {
        return endDepoID;
    }
    public int getFuelID() {
        return fuelID;
    }
    public int getFuelAmount() {
        return fuelAmount;
    }
    public String getOperatorID() {
        return operatorID;
    }

}
