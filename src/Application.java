import database.DBConnector;
import database.DbConnectorFactory;
import database.MySQLConnector;
import gui.ApplicationGUI;
import model.Customer;
import readers.CSVReader;
import readers.Reader;
import readers.ReaderFactory;
import readers.XMLReader;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.CustomerContactsSaxParser;
import utils.ApplicationProperties;
import utils.Utils;

import java.io.File;
import java.util.ArrayList;

class Application{

    private static final String GUI_MODE = "gui";
    private static final String CRON_MODE = "cronmode"

    public static void main(String args[]){
        if(args.length > 0){
            try{
                String fileLocation = args[0];
                executeApplication(fileLocation);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else {
            try{
                String application_mode = ApplicationProperties.getProperty("application.mode");
                if(application_mode.equals(CRON_MODE)){
                    executeApplication(ApplicationProperties.getProperty("application.defaultFilePath"));
                }else if(application_mode.equals(GUI_MODE)){
                    ApplicationGUI gui = new ApplicationGUI();
                    gui.run();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    private static void executeApplication(String filePath) throws Exception{
        File file = new File(filePath);
        DBConnector connector = DbConnectorFactory.getDbConnector();
        //Read from file and save to DB
        Reader reader = ReaderFactory.getReaderForFileType(Utils.getExtension(file));
        reader.readAndSaveToDB(file, connector);
    }

}
