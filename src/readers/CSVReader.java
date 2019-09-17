package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.Parser;

import java.io.File;
import java.util.List;

public class CSVReader implements Reader {

    private DBConnector dbConnector;

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        Parser parser = new CustomerContactsCSVParser(this);
        List<Customer> customers = parser.getCustomersFromFile(file);
        saveBatch(customers);
    }

    @Override
    public void saveBatch(List<Customer> customers) {
        dbConnector.saveCustomerContactsToDB(customers);
    }
}
