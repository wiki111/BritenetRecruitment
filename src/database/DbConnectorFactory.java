package database;

public class DbConnectorFactory {
    public static DBConnector getDbConnector() throws Exception{
        //TODO : parameterize
        return new MySQLConnector();
    }
}
