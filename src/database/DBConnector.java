package database;

import model.Customer;

import java.sql.ResultSet;
import java.util.List;

public interface DBConnector {

    void saveToDB(List<Customer> customers);

}
