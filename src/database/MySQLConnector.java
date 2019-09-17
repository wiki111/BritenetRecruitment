package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnector implements DBConnector {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/CustomersAndContactsDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "RK6AFJZ0t^l#";

    public String getCustomersFromDB(){
        try{
            Connection con= DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from CUSTOMERS");
            StringBuilder stringBuilder = new StringBuilder();
            while(rs.next())
                stringBuilder.append(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getInt(4));
            con.close();
            return stringBuilder.toString();
        }catch(Exception e){
            System.out.println(e);
            return "Retrieval of data from database failed";
        }
    }

    @Override
    public void saveToDB() {

    }
}
