package readers.parsers;

import model.Contact;
import model.Customer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerContactsSaxParser extends DefaultHandler {

    private Customer customerPlaceholder;
    private Contact contactPlaceholder;
    private List<Customer> customers;
    private String valuePlaceholder;
    private boolean isInCustomer;
    private boolean isInContact;

    public CustomerContactsSaxParser() {
        this.customers = new ArrayList<>();
    }

    public void testParsing(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse("D:\\ProgrammingProjects\\BritenetRecruitment\\res\\exampledata\\dataxml.xml", this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
        for(Customer customer : customers){
            System.out.println(customer.toString());
        }
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
                isInCustomer = false;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        valuePlaceholder = new String(ch, start, length);
    }
}
