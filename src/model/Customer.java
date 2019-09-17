package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String Surname;
    private int age;
    private String city;
    List<Contact> contactList;

    public Customer() {
        contactList = new ArrayList<>();
    }

    public Customer(int id, String name, String surname, int age) {
        this.id = id;
        this.name = name;
        Surname = surname;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Surname='" + Surname + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", contactList=" + contactList +
                '}';
    }
}
