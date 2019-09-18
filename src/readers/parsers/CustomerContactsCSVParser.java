package readers.parsers;

import exceptions.ParserException;
import model.Contact;
import model.Customer;
import utils.ApplicationProperties;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerContactsCSVParser implements Parser {

    private List<Customer> customers;
    private static final String COMMA_DELIMITER = ",";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_JABBER_ID = Pattern.compile("^[A-Za-z0-9._%+-]+@jabber.org$");
    private static final Pattern VALID_PHONE = Pattern.compile("^(\\+([0-9]){2}[\\s]?)?[1-9]{1}[0-9]{2}(([\\s\\-])?[0-9]{3}){2}$");
    private static int batchSize = 100;
    private BatchSizeReachedListener batchSizeReachedListener;

    public CustomerContactsCSVParser() {
        this.customers = new ArrayList<>();
        try{
            batchSize = Integer.valueOf(ApplicationProperties.getProperty("application.batchsize"));
        }catch (Exception e){
            System.out.println("Couldnt get batch size from properties file, default value used. Error message : " + e.getMessage());
        }
    }

    @Override
    public void setBatchSizeReachedListener(BatchSizeReachedListener batchSizeReachedListener) {
        this.batchSizeReachedListener = batchSizeReachedListener;
    }

    @Override
    public List<Customer> getCustomersFromFile(File file, String encoding) throws ParserException {
        this.customers.clear();
        try{
            Scanner rowScanner = new Scanner(file, encoding);
            while (rowScanner.hasNextLine()) {
                customers.add(getCustomerData(rowScanner.nextLine()));
                trySaveBatch(customers);
            }
            return customers;
        }catch (IOException | SQLException e){
            throw new ParserException(e.getMessage());
        }
    }

    private void trySaveBatch(List<Customer> customers) throws SQLException{
        if(customers.size() >= batchSize){
            if(batchSizeReachedListener != null){
                batchSizeReachedListener.trySaveBatch(customers);
            }
        }
    }

    private Customer getCustomerData(String readLine){
        ArrayList<String> values = new ArrayList<>();
        Scanner lineScanner = new Scanner(readLine);
        lineScanner.useDelimiter(COMMA_DELIMITER);
        while (lineScanner.hasNext()){
            values.add(lineScanner.next());
        }
        return createCustomerFromData(values);
    }

    private Customer createCustomerFromData(ArrayList<String> data){
        Customer customer = new Customer();
        customer.setName(data.get(0));
        customer.setSurname(data.get(1));
        try{
            customer.setAge(Integer.valueOf(data.get(2)));
        }catch (NumberFormatException e){
            System.out.println("Couldn't read age for customer " + customer.getName() + " " + customer.getSurname());
        }
        customer.setCity(data.get(3));
        ListIterator<String> iterator = data.listIterator(4);
        while (iterator.hasNext()){
            String contactData = iterator.next();
            Contact contact = new Contact();
            contact.setContactData(contactData);
            contact.setType(getContactType(contactData));
            customer.getContactList().add(contact);
        }
        return customer;
    }

    private int getContactType(String contactData){
        Matcher jabberMatcher = VALID_JABBER_ID.matcher(contactData);
        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(contactData);
        Matcher phoneMatcher = VALID_PHONE.matcher(contactData);

        if(jabberMatcher.find()){
            return 3;
        }else if(emailMatcher.find()){
            return 1;
        }else if(phoneMatcher.find()){
            return 2;
        }else {
            return 0;
        }

    }

}
