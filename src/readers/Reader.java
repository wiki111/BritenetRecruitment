package readers;

import database.DBConnector;

import java.io.File;

public interface Reader {
    void readAndSaveToDB(File file, DBConnector dbConnector);
}
