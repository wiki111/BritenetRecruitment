package database;

public class DbConnectorFactory {
    public static DBConnector getDbConnector(){
        //TODO : parameterize
        return new MySQLConnector();
    }
}
