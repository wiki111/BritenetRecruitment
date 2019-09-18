package readers.parsers;

import model.Customer;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface Parser {
    List<Customer> getCustomersFromFile(File file) throws SQLException;
    void setBatchSizeReachedListener(BatchSizeReachedListener listener);
}
