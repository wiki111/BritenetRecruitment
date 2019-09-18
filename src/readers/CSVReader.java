package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.BatchSizeReachedListener;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.Parser;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class CSVReader implements Reader {

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) throws SQLException {
        Parser parser = new CustomerContactsCSVParser();
        parser.setBatchSizeReachedListener(customers -> {
            dbConnector.saveCustomerContactsToDB(customers);
            customers.clear();
        });
        List<Customer> customers = parser.getCustomersFromFile(file);
        dbConnector.saveCustomerContactsToDB(customers);
    }


}
