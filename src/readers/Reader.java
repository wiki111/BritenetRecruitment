package readers;

import Exceptions.ParserException;
import database.DBConnector;
import model.Customer;
import utils.ThrowingConsumer;

import java.io.File;
import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.function.Consumer;

public interface Reader {
    void readAndSaveToDB(File file, DBConnector dbConnector) throws SQLException, ParserException;
}
