package model;

public class Contact {
    private String contactData;
    private int type;

    public String getContactData() {
        return contactData;
    }

    public void setContactData(String contactData) {
        this.contactData = contactData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactData='" + contactData + '\'' +
                ", type=" + type +
                '}';
    }
}
