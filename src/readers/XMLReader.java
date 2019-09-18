package readers;

import Exceptions.ParserException;
import database.DBConnector;
import model.Customer;
import readers.parsers.BatchSizeReachedListener;
import readers.parsers.CustomerContactsSaxParser;
import readers.parsers.Parser;
import utils.ThrowingConsumer;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class XMLReader implements Reader {
    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) throws SQLException, ParserException {
        Parser parser = new CustomerContactsSaxParser();
        parser.setBatchSizeReachedListener(customers -> {
            dbConnector.saveCustomerContactsToDB(customers);
            customers.clear();
        });
        List<Customer> customers = parser.getCustomersFromFile(file);
        dbConnector.saveCustomerContactsToDB(customers);
    }


}
