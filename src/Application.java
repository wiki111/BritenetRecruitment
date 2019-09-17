import database.MySQLConnector;
import model.Customer;
import readers.parsers.CustomerContactsCSVParser;
import readers.parsers.CustomerContactsSaxParser;

import java.io.File;
import java.util.ArrayList;

class Application{

    public static void main(String args[]){

        /* test code System.out.println(new MySQLConnector().getCustomersFromDB());
        CustomerContactsSaxParser parser = new CustomerContactsSaxParser();
        parser.testParsing();
        CustomerContactsCSVParser customerContactsCSVParser = new CustomerContactsCSVParser();
        ArrayList<Customer> customers = (ArrayList) customerContactsCSVParser.getCustomersFromFile(new File("D:\\ProgrammingProjects\\BritenetRecruitment\\res\\exampledata\\datacsv.txt"));
        for(Customer customer : customers){
            System.out.println(customer.toString());
        }
        */
    }

}
