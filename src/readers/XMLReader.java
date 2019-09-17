package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.CustomerContactsSaxParser;

import java.io.File;
import java.util.List;

public class XMLReader implements Reader {
    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        CustomerContactsSaxParser customerContactsSaxParser = new CustomerContactsSaxParser();
        List<Customer> customers = customerContactsSaxParser.parseFile(file);
        dbConnector.saveToDB(customers);
    }
}
