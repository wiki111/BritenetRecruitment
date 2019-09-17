package model;

public class Contact {
    private int id;
    private String contactData;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        return "model.Contact{" +
                "id=" + id +
                ", contactData='" + contactData + '\'' +
                ", type=" + type +
                '}';
    }
}
