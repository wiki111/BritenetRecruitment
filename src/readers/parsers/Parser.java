package readers.parsers;

import exceptions.ParserException;
import model.Customer;

import java.io.File;
import java.util.List;

public interface Parser {
    List<Customer> getCustomersFromFile(File file, String encoding) throws ParserException;
    void setBatchSizeReachedListener(BatchSizeReachedListener listener);
}
