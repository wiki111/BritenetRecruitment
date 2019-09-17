package readers;

import database.DBConnector;
import model.Customer;
import readers.parsers.CustomerContactsSaxParser;

import java.io.File;
import java.util.List;

public class XMLReader implements Reader {

    private DBConnector dbConnector;

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        CustomerContactsSaxParser customerContactsSaxParser = new CustomerContactsSaxParser(this);
        List<Customer> customers = customerContactsSaxParser.parseFile(file);
        dbConnector.saveCustomerContactsToDB(customers);
    }

    @Override
    public void saveBatch(List<Customer> customers) {
        dbConnector.saveCustomerContactsToDB(customers);
    }
}
