package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private String Surname;
    private int age;
    List<Contact> contactList;

    public Customer() {
        contactList = new ArrayList<>();
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

    public List<Contact> getContactList() {
        return contactList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", Surname='" + Surname + '\'' +
                ", age=" + age +
                ", contactList=" + contactList +
                '}';
    }

}
