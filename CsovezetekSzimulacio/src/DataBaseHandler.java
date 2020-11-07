import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DataBaseHandler {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/projektlabor?useLegacyDatetimeCode=false&amp&useSSL=false&serverTimezone=Europe/Budapest";
    static final String USER = "root";
    static final String PASS = "test";
    Connection con = null;
    Statement stmt = null;

    private boolean setConnection(){
        try {
            Class.forName(JDBC_DRIVER);
//            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection(DB_URL,USER,PASS);
//            System.out.println("Connected database successfully...");
            stmt = con.createStatement();
            return true;
        }
        catch (SQLException se){
            se.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void closeConnection(){
        try {
            stmt.close();
            con.close();
        }
        catch (SQLException se){
            se.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getTableMetaData(String tableName, int typeOrNameOrColumns){
        //0 = columnTypeName, 1 = columnName, 2 = columnCount
        setConnection();
        ArrayList<String> tableData = new ArrayList<>();
        try {
            ResultSet resultSet = stmt.executeQuery("Select * From "+tableName);
            ResultSetMetaData md = resultSet.getMetaData();
            if (typeOrNameOrColumns == 0){
                for (int i=1; i<=md.getColumnCount(); i++)
                {
                    tableData.add(md.getColumnTypeName(i));
                }
            }else if (typeOrNameOrColumns == 1){
                for (int i=1; i<=md.getColumnCount(); i++)
                {
                    tableData.add(md.getColumnName(i));
                }
            } else if (typeOrNameOrColumns == 2){
                tableData.add(String.valueOf(md.getColumnCount()));
            }
        }
        catch (SQLException se){
            se.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  tableData;
    }
    public void insertRecord(String tableName, ArrayList<String> values){
        if (!setConnection()) {
            return;
        }
        try {
            ArrayList<String> tableData = new ArrayList<>();
            tableData = getTableMetaData(tableName,0);
            String sql = "INSERT INTO "+ tableName+" Values (";
            for (int i = 0; i < tableData.size();i++){
                if (tableData.get(i).equals("INT")){
                    sql+=values.get(i);
                }
                else {
                    sql+="'"+ values.get(i)+"'";
                }
                sql+=",";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+=")";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (con != null) con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    public LinkedHashMap<Integer,ArrayList<String>> readRecords(String tableName){
        //0 = columnTypeName, 1 = columnName, 2 = columnCount
        if(!setConnection()){
            return null;
        }
        ArrayList<String> columName = new ArrayList<>();
        ArrayList<String> columnType = new ArrayList<>();
        ArrayList<String> columnCount = new ArrayList<>();
        columName = getTableMetaData(tableName,1);
        columnType = getTableMetaData(tableName,0);
        columnCount = getTableMetaData(tableName,2);
        LinkedHashMap<Integer,ArrayList<String>> result = new LinkedHashMap<>();
        ResultSet resultSet = null;
        try{
            String sql = null;
            if (tableName.equals("transportationplan")) {
                sql = "select  * from " + tableName + " ORDER BY startdate ASC";
            }else{
                sql = "select  * from " + tableName;
            }
//            System.out.println(sql);
            resultSet = stmt.executeQuery(sql);
            Integer i = 0;
            while (resultSet.next()){
                ArrayList<String> tmp = new ArrayList<>();
                for (int j = 0; j < Integer.parseInt(columnCount.get(0)); j++) {
                    //System.out.println("in the foooooooooooooor");
                    if (columnType.get(j).equals("INT")) {
                        // System.out.println("yeet we are int");
                        tmp.add(String.valueOf(resultSet.getInt(columName.get(j))));
                    } else if (columnType.get(j).equals("VARCHAR")) {
                        tmp.add(resultSet.getString(columName.get(j)));
                    } else if (columnType.get(j).equals("DATETIME")) {
//                        System.out.println(resultSet.getTimestamp(columName.get(j)));
                        tmp.add(String.valueOf(resultSet.getTimestamp(columName.get(j))));
                    } else if (columnType.get(j).equals("DATE")) {
//                        System.out.println(resultSet.getTimestamp(columName.get(j)));
                        tmp.add(String.valueOf(resultSet.getString(columName.get(j))));
                    }
                }
//                System.out.println(tmp);
                result.put(i,tmp);
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
        return result;
    }
    public ArrayList<String> readRecordWithCondition(String tableName, String condition){
        if (!setConnection()){
            return null;
        }
        ArrayList<String> record = new ArrayList<>();
        try{
            String sql = "select  * from " + tableName + " Where "+ condition;
            ArrayList<String> columName = new ArrayList<>();
            ArrayList<String> columnType = new ArrayList<>();
            ArrayList<String> columnCount = new ArrayList<>();
            columName = getTableMetaData(tableName,1);
            columnType = getTableMetaData(tableName,0);
            columnCount = getTableMetaData(tableName,2);
//             System.out.println(sql);
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()){
                for (int j = 0; j < Integer.parseInt(columnCount.get(0)); j++) {
                    //System.out.println("in the foooooooooooooor");
                    if (columnType.get(j).equals("INT")) {
                        // System.out.println("yeet we are int");
                        record.add(String.valueOf(resultSet.getInt(columName.get(j))));
                    } else if (columnType.get(j).equals("VARCHAR")) {
                        record.add(resultSet.getString(columName.get(j)));
                    } else if (columnType.get(j).equals("DATETIME")) {
                        record.add(String.valueOf(resultSet.getTimestamp(columName.get(j))));
                    } else if (columnType.get(j).equals("DATE")) {
                        record.add(String.valueOf(resultSet.getString(columName.get(j))));
                    }
                }
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

        return record;
    }
    public void updateRecord(String tableName, String columName, String value, String condition){
        if (!setConnection()) {
            return;
        }
        try {
            String sql = "UPDATE "+tableName+" SET "+columName+" = '"+value+"' WHERE "+condition;
//            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (con != null) con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    public void deleteRecord(String tableName, String condition){
        if (!setConnection()) {
            return;
        }
        try {
            String sql = "Delete FROM "+tableName+" Where "+condition;
            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (con != null) con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
