package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.CustomerContactsCSVParser;

import java.io.File;
import java.util.List;

public class CSVReader implements Reader {

    private DBConnector dbConnector;

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        CustomerContactsCSVParser customerContactsCSVParser = new CustomerContactsCSVParser(this);
        List<Customer> customers = customerContactsCSVParser.getCustomersFromFile(file);

    }

    @Override
    public void saveBatch(List<Customer> customers) {
        dbConnector.saveCustomerContactsToDB(customers);
    }
}
