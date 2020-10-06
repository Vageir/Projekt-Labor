import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class TransportationPlan {
    private String transportationID;
    private String startDepoID;
    private String endDepoID;
    private int fuelID;
    private int fuelAmount;
    private Calendar startDate;
    private Calendar endDate;
    private String operatorID;

    public TransportationPlan(String transportationID, String startDepoID, String endDepoID, int fuelID, int fuelAmount, Calendar startDate, Calendar endDate, String operatorID) {
        this.transportationID = transportationID;
        this.startDepoID = startDepoID;
        this.endDepoID = endDepoID;
        this.fuelID = fuelID;
        this.fuelAmount = fuelAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.operatorID = operatorID;
    }
    public TransportationPlan(ArrayList<String> ls){
        int[] StartYearsMonthsDays = Arrays.asList(ls.get(5).substring(0,10).split("-")).stream().mapToInt(Integer::parseInt).toArray();
        int[] startHoursMins = Arrays.asList(ls.get(5).substring(11,16).split(":")).stream().mapToInt(Integer::parseInt).toArray();
        int[] endYearsMonthsDays = Arrays.asList(ls.get(6).substring(0,10).split("-")).stream().mapToInt(Integer::parseInt).toArray();
        int[] endhoursMins = Arrays.asList(ls.get(6).substring(11,16).split(":")).stream().mapToInt(Integer::parseInt).toArray();
        this.transportationID = ls.get(0);
        this.startDepoID = ls.get(1);
        this.endDepoID = ls.get(2);
        this.fuelID = Integer.parseInt(ls.get(3));
        this.fuelAmount = Integer.parseInt(ls.get(4));
        this.startDate = Calendar.getInstance();
        this.endDate = Calendar.getInstance();
        startDate.set(StartYearsMonthsDays[0],StartYearsMonthsDays[1],StartYearsMonthsDays[2],startHoursMins[0],startHoursMins[1]);
        endDate.set(endYearsMonthsDays[0],endYearsMonthsDays[1],endYearsMonthsDays[2],endhoursMins[0],endhoursMins[1]);
        this.operatorID = ls.get(7);

    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
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
