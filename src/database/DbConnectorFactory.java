package database;

public class DbConnectorFactory {
    public static DBConnector getDbConnector(){
        return new MySQLConnector();
    }
}
