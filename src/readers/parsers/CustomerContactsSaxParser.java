package readers.parsers;

import exceptions.ParserException;
import model.Contact;
import model.Customer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.ApplicationProperties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerContactsSaxParser extends DefaultHandler implements Parser {

    private Customer customerPlaceholder;
    private List<Customer> customers;
    private String valuePlaceholder;
    private boolean isInCustomer;
    private boolean isInContact;
    private static int batchSize;
    private BatchSizeReachedListener batchSizeReachedListener;

    public CustomerContactsSaxParser() {
        this.customers = new ArrayList<>();
        try{
            batchSize = Integer.valueOf(ApplicationProperties.getProperty("application.batchsize"));
        }catch (Exception e){
            System.out.println("Couldn't get batch size from properties file, default value used. Error message : " + e.getMessage());
        }
    }

    @Override
    public void setBatchSizeReachedListener(BatchSizeReachedListener batchSizeReachedListener) {
        this.batchSizeReachedListener = batchSizeReachedListener;
    }

    @Override
    public List<Customer> getCustomersFromFile(File file, String encoding) throws ParserException {
        return parseFile(file, encoding);
    }

    private List<Customer> parseFile(File file, String encoding) throws ParserException{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(getInputSourceForFileWithEncoding(file, encoding), this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParserException(e.getMessage());
        }
        return customers;
    }

    private InputSource getInputSourceForFileWithEncoding(File file, String encoding) throws IOException{
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
        InputSource inputSource = new InputSource(inputStreamReader);
        inputSource.setEncoding(encoding);
        return inputSource;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("person")){
            customerPlaceholder = new Customer();
            isInCustomer = true;
        }else if(qName.equalsIgnoreCase("contacts")){
            isInContact = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(isInCustomer){
            if(isInContact){
                Contact contactPlaceholder;
                if(qName.equalsIgnoreCase("phone")){
                    contactPlaceholder = new Contact();
                    contactPlaceholder.setContactData(valuePlaceholder);
                    contactPlaceholder.setType(2);
                    customerPlaceholder.getContactList().add(contactPlaceholder);
                }else if(qName.equalsIgnoreCase("email")){
                    contactPlaceholder = new Contact();
                    contactPlaceholder.setContactData(valuePlaceholder);
                    contactPlaceholder.setType(1);
                    customerPlaceholder.getContactList().add(contactPlaceholder);
                }else if(qName.equalsIgnoreCase("jabber")){
                    contactPlaceholder = new Contact();
                    contactPlaceholder.setContactData(valuePlaceholder);
                    contactPlaceholder.setType(3);
                    customerPlaceholder.getContactList().add(contactPlaceholder);
                }else if(qName.equalsIgnoreCase("contacts")){
                    isInContact = false;
                }else{
                    contactPlaceholder = new Contact();
                    contactPlaceholder.setContactData(valuePlaceholder);
                    contactPlaceholder.setType(0);
                    customerPlaceholder.getContactList().add(contactPlaceholder);
                }
            }else if(qName.equalsIgnoreCase("name")){
                customerPlaceholder.setName(valuePlaceholder);
            }else if(qName.equalsIgnoreCase("surname")){
                customerPlaceholder.setSurname(valuePlaceholder);
            }else if(qName.equalsIgnoreCase("age")){
                customerPlaceholder.setAge(Integer.valueOf(valuePlaceholder));
            }else if(qName.equalsIgnoreCase("city")){
                customerPlaceholder.setCity(valuePlaceholder);
            }else if(qName.equalsIgnoreCase("person")){
                customers.add(customerPlaceholder);
                try{
                    trySaveBatch(customers);
                }catch (SQLException e){
                    throw new SAXException(e.getMessage());
                }
                isInCustomer = false;
            }
        }
    }

    private void trySaveBatch(List<Customer> customers) throws SQLException{
        if(customers.size() > batchSize){
            if(batchSizeReachedListener != null){
                batchSizeReachedListener.trySaveBatch(customers);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        valuePlaceholder = new String(ch, start, length);
    }


}
