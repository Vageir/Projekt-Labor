import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseHandler {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/database";
    static final String USER = "pa";
    static final String PASS = "pa";

    public void insertRecord(){
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Connected database successfully...");
            /*
            stmt = conn.createStatement();
            String sql = "INSERT INTO Registration " + "VALUES (100, 'Zara', 'Ali', 18)";

            stmt.executeUpdate(sql);
            sql = "INSERT INTO Registration " + "VALUES (101, 'Mahnaz', 'Fatma', 25)";

            stmt.executeUpdate(sql);
            sql = "INSERT INTO Registration " + "VALUES (102, 'Zaid', 'Khan', 30)";

            stmt.executeUpdate(sql);
            sql = "INSERT INTO Registration " + "VALUES(103, 'Sumit', 'Mittal', 28)";

            stmt.executeUpdate(sql);
            System.out.println("Inserted records into the table...");
*/
            stmt.close();
            conn.close();
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
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
