package database;

import model.Contact;
import model.Customer;

import java.sql.*;
import java.util.List;

public class MySQLConnector implements DBConnector {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/CustomersAndContactsDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_NAME = "customersandcontactsdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "RK6AFJZ0t^l#";
    private static final String SQL_GET_NEXT_CUSTOMER_ID_QUERY = "SELECT AUTO_INCREMENT\n" +
            "FROM information_schema.TABLES" +
            "WHERE TABLE_SCHEMA = " + DB_NAME +
            "AND TABLE_NAME = " + "customers";
    private static final String SQL_INSERT_CUSTOMER = "INSERT INTO customers (NAME, SURNAME, AGE, CITY) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_CONTACT = "INSERT INTO contacts (ID_CUSTOMER, TYPE, CONTACT) VALUES (?, ?, ?)";
    private Connection connection;


    private void initConnection() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    }

    private void closeConnection() {
        try{
            this.connection.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveToDB(List<Customer> customers) {
        try{
           initConnection();
            for(Customer customer : customers){
                PreparedStatement customerStatement = connection.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
                customerStatement.setString(1, customer.getName());
                customerStatement.setString(2, customer.getSurname());
                customerStatement.setInt(3, customer.getAge());
                customerStatement.setString(4, customer.getCity());
                int customerId = customerStatement.executeUpdate();
                for(Contact contact : customer.getContactList()){
                    PreparedStatement contactStatement = connection.prepareStatement(SQL_INSERT_CONTACT);
                    contactStatement.setInt(1, customerId);
                    contactStatement.setInt(2, contact.getType());
                    contactStatement.setString(3, contact.getContactData());
                    contactStatement.executeUpdate();
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }
}
