package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.CustomerContactsCSVParser;

import java.io.File;
import java.util.List;

public class CSVReader implements Reader {
    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        CustomerContactsCSVParser customerContactsCSVParser = new CustomerContactsCSVParser();
        List<Customer> customers = customerContactsCSVParser.getCustomersFromFile(file);
        dbConnector.saveToDB(customers);
    }
}
