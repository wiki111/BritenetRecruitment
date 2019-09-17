package database;

import model.Customer;

import java.util.List;

public interface DBConnector {

    void saveCustomerContactsToDB(List<Customer> customers);
    void setCustomerPersistedListener(ObjectProcessedListener listener);

}
