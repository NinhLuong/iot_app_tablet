package com.example.iot_app.webview_page;

import java.util.ArrayList;

public class Person {
    private String name;
    private String phone_number;
    private int resourceId;
    private ArrayList<Person> persons;

    public Person(String name, String phone_number, int resourceId) {
        this.name = name;
        this.phone_number = phone_number;
        this.resourceId = resourceId;
        this.persons = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public ArrayList<Person> getPersons() {
        return this.persons;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPhone_number(final String phone_number) {
        this.phone_number = phone_number;
    }

    public void setResourceId(final int resourceId) {
        this.resourceId = resourceId;
    }

    public void setPersons(final ArrayList<Person> persons) {
        this.persons = persons;
    }
}
