package database;

import model.Customer;

import java.sql.ResultSet;
import java.util.List;

public interface DBConnector {

    void saveCustomerContactsToDB(List<Customer> customers);

}
