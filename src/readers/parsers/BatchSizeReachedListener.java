package readers.parsers;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface BatchSizeReachedListener {
    void trySaveBatch(List<Customer> customers) throws SQLException;
}
