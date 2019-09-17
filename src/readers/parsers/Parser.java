package readers.parsers;

import model.Customer;

import java.io.File;
import java.util.List;

public interface Parser {
    List<Customer> getCustomersFromFile(File file);
}
