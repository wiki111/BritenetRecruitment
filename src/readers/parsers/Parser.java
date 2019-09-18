package readers.parsers;

import Exceptions.ParserException;
import model.Customer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Parser {
    List<Customer> getCustomersFromFile(File file) throws ParserException;
    void setBatchSizeReachedListener(BatchSizeReachedListener listener);
}
