package readers;

import Exceptions.ParserException;
import database.DBConnector;

import java.io.File;
import java.sql.SQLException;

public interface Reader {
    void readAndSaveToDB(File file, DBConnector dbConnector) throws SQLException, ParserException;
}
