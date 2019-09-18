import database.DBConnector;
import database.MySQLConnector;
import gui.ApplicationGUI;
import model.Customer;
import readers.CSVReader;
import readers.Reader;
import readers.XMLReader;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.CustomerContactsSaxParser;

import java.io.File;
import java.util.ArrayList;

class Application{

    public static void main(String args[]){
        ApplicationGUI gui = new ApplicationGUI();
        gui.run();
    }

}
