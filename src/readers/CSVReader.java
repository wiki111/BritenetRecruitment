package readers;

import exceptions.ParserException;
import database.DBConnector;
import exceptions.ReaderException;
import model.Customer;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.Parser;
import utils.ApplicationProperties;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class CSVReader implements Reader {

    @Override
    public void readAndSaveToDB(File file, DBConnector dbConnector) throws ReaderException {
        try{
            Parser parser = new CustomerContactsCSVParser();
            parser.setBatchSizeReachedListener(customers -> {
                dbConnector.saveCustomerContactsToDB(customers);
                customers.clear();
            });
            List<Customer> customers = parser.getCustomersFromFile(file,  ApplicationProperties.getProperty("application.encoding"));
            dbConnector.saveCustomerContactsToDB(customers);
        }catch (Exception e){
            throw new ReaderException(e.getMessage());
        }

    }


}
