package database;

import model.Contact;
import model.Customer;
import utils.ApplicationProperties;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class MySQLConnector implements DBConnector {

    private static String JDBC_URL;
    private static String DB_NAME;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String SQL_INSERT_CUSTOMER;
    private static String SQL_INSERT_CONTACT;
    private static String CUSTOMER_TABLE;
    private static String CUSTOMER_COL_NAME;
    private static String CUSTOMER_COL_SURNAME;
    private static String CUSTOMER_COL_AGE;
    private static String CONTACT_TABLE;
    private static String CONTACT_COL_ID_CUSTOMER;
    private static String CONTACT_COL_TYPE;
    private static String CONTACT_COL_CONTACT;
    private Connection connection;
    private ObjectProcessedListener objectProcessedListener;

    public MySQLConnector() throws IOException{
            initDatabaseConnectionInformation();
            initDatabaseTableInformation();
            initDatabaseInsertQueries();
    }


    private void initDatabaseConnectionInformation() throws IOException {
        JDBC_URL = ApplicationProperties.getProperty("mysql.JDBC_URL");
        DB_NAME = ApplicationProperties.getProperty("mysql.DB_NAME");
        DB_USERNAME = ApplicationProperties.getProperty("mysql.DB_USERNAME");
        DB_PASSWORD = ApplicationProperties.getProperty("mysql.DB_PASSWORD");
    }

    private void initDatabaseTableInformation() throws IOException{
        CUSTOMER_TABLE = ApplicationProperties.getProperty("mysql.customers.tablename");
        CUSTOMER_COL_NAME = ApplicationProperties.getProperty("mysql.customers.column.name");
        CUSTOMER_COL_SURNAME =  ApplicationProperties.getProperty("mysql.customers.column.surname");
        CUSTOMER_COL_AGE = ApplicationProperties.getProperty("mysql.customers.column.age");
        CONTACT_TABLE = ApplicationProperties.getProperty("mysql.contacts.tablename");
        CONTACT_COL_ID_CUSTOMER = ApplicationProperties.getProperty("mysql.contacts.column.id_customer");
        CONTACT_COL_TYPE = ApplicationProperties.getProperty("mysql.contacts.column.type");
        CONTACT_COL_CONTACT = ApplicationProperties.getProperty("mysql.contacts.column.contact");
    }

    private void initDatabaseInsertQueries(){
        SQL_INSERT_CUSTOMER = "INSERT INTO " +
                CUSTOMER_TABLE + "(" +
                CUSTOMER_COL_NAME + "," +
                CUSTOMER_COL_SURNAME + "," +
                CUSTOMER_COL_AGE + ")" +
                "VALUES (?,?,?)";
        SQL_INSERT_CONTACT = "INSERT INTO " +
                CONTACT_TABLE + "(" +
                CONTACT_COL_ID_CUSTOMER + "," +
                CONTACT_COL_TYPE + "," +
                CONTACT_COL_CONTACT + ")" +
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
                if(objectProcessedListener != null){
                    objectProcessedListener.objectProcessed(contact.toString());
                }
                contactStatement.addBatch();
            }
        }
        contactStatement.executeBatch();
    }

    private ResultSet persistCustomers(List<Customer> customers) throws SQLException{

        PreparedStatement customerStatement = connection.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

        for(Customer customer : customers){
            setUpCustomerStatement(customerStatement, customer);
            if(objectProcessedListener != null){
                objectProcessedListener.objectProcessed(customer.toString());
            }
            customerStatement.addBatch();
        }

        customerStatement.executeBatch();

        return  customerStatement.getGeneratedKeys();
    }

    private void setUpCustomerStatement(PreparedStatement customerStatement, Customer customer) throws SQLException{
        customerStatement.setString(1, customer.getName());
        customerStatement.setString(2, customer.getSurname());
        if(customer.getAge() == 0){
            customerStatement.setNull(3, Types.INTEGER);
        }else {
            customerStatement.setInt(3, customer.getAge());
        }

    }

    private void setUpContactStatement(PreparedStatement contactStatement, Contact contact, int customerId) throws SQLException{
        contactStatement.setInt(1, customerId);
        contactStatement.setInt(2, contact.getType());
        contactStatement.setString(3, contact.getContactData());
    }
}
