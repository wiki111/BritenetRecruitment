package database;

import model.Contact;
import model.Customer;
import utils.ApplicationProperties;

import java.sql.*;
import java.util.List;

public class MySQLConnector implements DBConnector {

    private static String JDBC_URL;
    private static String DB_NAME;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String SQL_INSERT_CUSTOMER;
    private static String SQL_INSERT_CONTACT;
    private Connection connection;
    private ObjectProcessedListener objectProcessedListener;

    public MySQLConnector() throws Exception{
            JDBC_URL = ApplicationProperties.getProperty("mysql.JDBC_URL");
            DB_NAME = ApplicationProperties.getProperty("mysql.DB_NAME");
            DB_USERNAME = ApplicationProperties.getProperty("mysql.DB_USERNAME");
            DB_PASSWORD = ApplicationProperties.getProperty("mysql.DB_PASSWORD");
            SQL_INSERT_CUSTOMER = "INSERT INTO " +
                    ApplicationProperties.getProperty("mysql.customers.tablename") + "(" +
                    ApplicationProperties.getProperty("mysql.customers.column.name") + "," +
                    ApplicationProperties.getProperty("mysql.customers.column.surname") + "," +
                    ApplicationProperties.getProperty("mysql.customers.column.age") + "," +
                    ApplicationProperties.getProperty("mysql.customers.column.city") + ")" +
                    "VALUES (?,?,?,?)";
            SQL_INSERT_CONTACT = "INSERT INTO " +
                    ApplicationProperties.getProperty("mysql.contacts.tablename") + "(" +
                    ApplicationProperties.getProperty("mysql.contacts.column.id_customer") + "," +
                    ApplicationProperties.getProperty("mysql.contacts.column.type") + "," +
                    ApplicationProperties.getProperty("mysql.contacts.column.contact") + ")" +
                    "VALUES (?,?,?)";
    }

    private void initConnection() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    }

    private void closeConnection() throws SQLException{
            this.connection.close();
    }

    @Override
    public void saveCustomerContactsToDB(List<Customer> customers) throws SQLException{
        initConnection();
        ResultSet generatedKeys = persistCustomers(customers);
        persistContacts(customers, generatedKeys);
        closeConnection();
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
