package database;

public class DbConnectorFactory {
    public static DBConnector getDbConnector() throws Exception{
        return new MySQLConnector();
    }
}
