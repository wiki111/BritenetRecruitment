package database;

import model.Contact;
import model.Customer;

import java.sql.*;
import java.util.List;

public class MySQLConnector implements DBConnector {

    //TODO : parameterize
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/CustomersAndContactsDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_NAME = "customersandcontactsdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "RK6AFJZ0t^l#";
    private static final String SQL_INSERT_CUSTOMER = "INSERT INTO customers (NAME, SURNAME, AGE, CITY) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_CONTACT = "INSERT INTO contacts (ID_CUSTOMER, TYPE, CONTACT) VALUES (?, ?, ?)";
    private Connection connection;
    private ObjectProcessedListener objectProcessedListener;

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
    public void saveCustomerContactsToDB(List<Customer> customers) {
        try{
           initConnection();
            ResultSet generatedKeys = persistCustomers(customers);
            persistContacts(customers, generatedKeys);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }

    @Override
    public void setCustomerPersistedListener(ObjectProcessedListener listener) {
        this.objectProcessedListener = listener;
    }

    private void persistContacts(List<Customer> customers, ResultSet generatedKeys) throws SQLException{
        PreparedStatement contactStatement = connection.prepareStatement(SQL_INSERT_CONTACT);
        for(Customer customer : customers){
            generatedKeys.next();
            int customerId = generatedKeys.getInt(1);
            for(Contact contact : customer.getContactList()){
                setUpContactStatement(contactStatement, contact, customerId);
                objectProcessedListener.objectProcessed(contact.toString());
                System.out.println(contact.toString());
                contactStatement.addBatch();
            }
        }
        contactStatement.executeBatch();
    }

    private ResultSet persistCustomers(List<Customer> customers) throws SQLException{

        PreparedStatement customerStatement = connection.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

        for(Customer customer : customers){
            setUpCustomerStatement(customerStatement, customer);
            objectProcessedListener.objectProcessed(customer.toString());
            System.out.println(customer.toString());
            customerStatement.addBatch();
        }

        customerStatement.executeBatch();

        return  customerStatement.getGeneratedKeys();
    }

    private void setUpCustomerStatement(PreparedStatement customerStatement, Customer customer) throws SQLException{
        customerStatement.setString(1, customer.getName());
        customerStatement.setString(2, customer.getSurname());
        customerStatement.setInt(3, customer.getAge());
        customerStatement.setString(4, customer.getCity());
    }

    private void setUpContactStatement(PreparedStatement contactStatement, Contact contact, int customerId) throws SQLException{
        contactStatement.setInt(1, customerId);
        contactStatement.setInt(2, contact.getType());
        contactStatement.setString(3, contact.getContactData());
    }
}
