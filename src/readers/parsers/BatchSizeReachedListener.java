package readers.parsers;

import model.Customer;

import java.util.List;

public interface BatchSizeReachedListener {
    void trySaveBatch(List<Customer> customers);
}
