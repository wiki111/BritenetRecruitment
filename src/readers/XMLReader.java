package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.BatchSizeReachedListener;
import readers.parsers.CustomerContactsSaxParser;
import readers.parsers.Parser;

import java.io.File;
import java.util.List;

public class XMLReader implements Reader {

    private DBConnector dbConnector;

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        Parser parser = new CustomerContactsSaxParser();
        parser.setBatchSizeReachedListener(customers -> dbConnector.saveCustomerContactsToDB(customers));
        List<Customer> customers = parser.getCustomersFromFile(file);
        dbConnector.saveCustomerContactsToDB(customers);
    }
}
