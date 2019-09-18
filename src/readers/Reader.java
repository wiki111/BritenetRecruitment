package readers;

import exceptions.ParserException;
import database.DBConnector;
import exceptions.ReaderException;

import java.io.File;
import java.sql.SQLException;

public interface Reader {
    void readAndSaveToDB(File file, DBConnector dbConnector) throws SQLException, ParserException, ReaderException;
}
