package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.BatchSizeReachedListener;
import readers.parsers.CustomerContactsSaxParser;
import readers.parsers.Parser;

import java.io.File;
import java.util.List;

public class XMLReader implements Reader {
    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        Parser parser = new CustomerContactsSaxParser();
        parser.setBatchSizeReachedListener(customers -> {dbConnector.saveCustomerContactsToDB(customers); customers.clear();});
        List<Customer> customers = parser.getCustomersFromFile(file);
        dbConnector.saveCustomerContactsToDB(customers);
    }
}
