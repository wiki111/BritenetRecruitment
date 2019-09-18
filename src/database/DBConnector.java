package database;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface DBConnector {

    void saveCustomerContactsToDB(List<Customer> customers) throws SQLException;
    void setCustomerPersistedListener(ObjectProcessedListener listener);

}
