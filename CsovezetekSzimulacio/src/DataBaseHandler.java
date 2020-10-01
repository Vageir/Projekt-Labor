import javax.swing.*;
import java.sql.*;

public class DataBaseHandler {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/projektlabor?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "test";

    public void insertRecord(String tID, String startDepoID, String endDepoID,
                             int fuelID, int fuelAmount, String startDate, String endDate,
                             String operatorID){
        Connection con = null;
        Statement stmt = null;

        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Connected database successfully...");
            stmt = con.createStatement();
            String sql = "INSERT INTO TRANSPORTATIONPLAN VALUES ('"+tID+"','"+startDepoID+"','"+endDepoID+"',"+fuelID+","+fuelAmount+",'"+startDate+"','"+endDate+"','"+operatorID+"')";
            System.out.println(sql);
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
    public void readRecords(JComboBox starDepo, JComboBox endDepo, JComboBox operatorID, JLabel operatorName){
            Connection con = null;
            Statement stmt = null;
            try{
                Class.forName(JDBC_DRIVER);
                System.out.println("Connecting to a selected database...");
                con = DriverManager.getConnection(DB_URL,USER,PASS);
                System.out.println("Connected database successfully...");
                stmt = con.createStatement();
                String sql = "Select DepoID from depo";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()){
                    String item = rs.getString("DepoID");
                    starDepo.addItem(new ComboItem(item));
                    endDepo.addItem(new ComboItem(item));
                }
                sql = "Select OperatorID,OperatorName from operator";
                rs = stmt.executeQuery(sql);
                int i = 0;
                while (rs.next()){
                   operatorID.addItem(new ComboItem(rs.getString("operatorID")));
                   if(i==0)
                    operatorName.setText(rs.getString("operatorName"));
                   i++;
                }


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
    public String updateGUI(String tableName, String valueColumnName,String column, String value){
        Connection con = null;
        Statement stmt = null;
        String result = null;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Connected database successfully...");
            stmt = con.createStatement();
//            select OperatorName from operator where OperatorID = "JSP23"
            String sql = "Select "+valueColumnName+" from "+tableName+" where "+column+"='"+value+"'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            result = rs.getString(valueColumnName);
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
        return result;
    }
        //        starDepo.addItem(new ComboItem("ASD"));
//        endDepo.addItem(new ComboItem("yo"));
//        endDepo.addItem(new ComboItem("yes"));
//        operatorID.addItem(new ComboItem("yeet"));
//        operatorName.setText("fossssssss");
    private class ComboItem {
        private String itemName;
        ComboItem(String itemName){
            this.itemName=itemName;
        }
        @Override
        public String toString() {
            return itemName;
        }
    }
}
