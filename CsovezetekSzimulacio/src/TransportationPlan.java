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
    private String highestContainerCapacityID;
    private boolean finished;
    private boolean timeToRun;
    private boolean reverse;
    private double tailOfTheFluid;
    private double headOfTheFluid;
    private double flowVelocity;
    private double startDepoMovedFuelAmount;
    private double endDepoMovedFuelAmount;

    public boolean isReverse() {
        return reverse;
    }
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
    public String getHighestContainerCapacityID() {
        return highestContainerCapacityID;
    }
    public void setHighestContainerCapacityID(String highestContainerCapacityID) {
        this.highestContainerCapacityID = highestContainerCapacityID;
    }
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
        this.finished = false;
        this.tailOfTheFluid = 0.0;
        this.headOfTheFluid = 0.0;
        this.flowVelocity = 0.0;
        this.startDepoMovedFuelAmount = 0.0;
        this.endDepoMovedFuelAmount = 0.0;
        this.timeToRun =  false;
        this.highestContainerCapacityID = null;
        this.reverse = false;
    }
    public boolean isTimeToRun() {
        return timeToRun;
    }
    public void setTimeToRun(boolean timeToRun) {
        this.timeToRun = timeToRun;
    }
    public void addStartDepoVolumeFlowRate(int volumeFlowRate){
        startDepoMovedFuelAmount+= volumeFlowRate;
    }
    public void addEndDepoVolumeFlowRate(int volumeFlowRate){
        endDepoMovedFuelAmount += volumeFlowRate;
    }
    public void setFlowVelocity(double flowVelocity) {
        this.flowVelocity = flowVelocity;
    }
    public void addHeadOfTheFluid(double flowVelocity){
        headOfTheFluid+=flowVelocity;
    }
    public void addTailOfTheFluid(double flowVelocity){
        tailOfTheFluid+=flowVelocity;
    }
    public void setTailOfTheFluid(double tailOfTheFluid) {
        this.tailOfTheFluid = tailOfTheFluid;
    }
    public void setHeadOfTheFluid(double headOfTheFluid) {
        this.headOfTheFluid = headOfTheFluid;
    }
    public double getFlowVelocity() {
        return flowVelocity;
    }
    public double getStartDepoMovedFuelAmount() {
        return startDepoMovedFuelAmount;
    }
    public double getEndDepoMovedFuelAmount() {
        return endDepoMovedFuelAmount;
    }
    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public double getTailOfTheFluid() {
        return tailOfTheFluid;
    }
    public double getHeadOfTheFluid() {
        return headOfTheFluid;
    }
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
