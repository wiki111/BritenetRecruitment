package readers;

import database.DBConnector;
import model.Customer;

import java.io.File;
import java.util.Currency;
import java.util.List;

public interface Reader {
    void readAndSaveToDB(File file, DBConnector dbConnector);
    void saveBatch(List<Customer> customers);
}
