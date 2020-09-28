import java.sql.*;

public class DataBaseHandler {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "test";

    public void insertRecord(String tID, String startDepoID, String endDepoID,
                             String fuelType, int fuelAmount, String startDate, String endDate,
                             String operatorID, String operatorName){
        Connection con = null;
        Statement stmt = null;

        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Connected database successfully...");
            stmt = con.createStatement();
            String sql = "INSERT INTO TRANSPORTATIONPLAN VALUES ("+tID+","+startDepoID+","+endDepoID+","+fuelType+","+fuelAmount+","+startDate+","+endDate+","+operatorID+","+operatorName+")";
            System.out.println(sql);
//            PreparedStatement pstmt = new con.prepareStatement(sql);
//            pstmt.setString(1,tID);
//            pstmt.setString(2,startDepoID);
//            pstmt.setString(3,endDepoID);
//            pstmt.setString(4,fuelType);
//            pstmt.setInt(5,fuelAmount);
//            pstmt.setString(6,startDate);
//            pstmt.setString(7,endDate);
//            pstmt.setString(8,operatorID);
//            pstmt.setString(9,operatorName);
//            pstmt.setString(1,tID);
//            pstmt.executeUpdate();
//            pstmt.close();
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(stmt!=null) stmt.close();
            }
            catch(SQLException se2) { }
            try {
                if(con!=null) con.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
