package com.example.iot_app.webview_page;

import java.util.ArrayList;

public class Person {
    private String name;
    private String phone_number;
    private int resourceId;
    private String imageUri;
    private String skypeId;
    public String getSkypeId() {
        return this.skypeId;
    }

    public void setSkypeId(final String skypeId) {
        this.skypeId = skypeId;
    }

    private ArrayList<Person> persons;
    private int imageResId;

    public Person(String name, String phone_number, int imageResId, String skypeId) {
        // Other initializations...
        this.name = name;
        this.phone_number = phone_number;
        this.imageResId = imageResId;
        this.skypeId = skypeId;
    }

    public Person(String name, String phone_number, String imageUri, String skypeId) {
        this.name = name;
        this.phone_number = phone_number;
        this.imageUri = imageUri;
        this.skypeId = skypeId;
        this.persons = new ArrayList<>();
    }

    // Change this method to return an int
    public int getImageResId() {
        return this.imageResId;
    }

    // Change this method to accept an int parameter
    public void setImageResId(final int imageResId) {
        this.imageResId = imageResId;
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
    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(final String imageUri) {
        this.imageUri = imageUri;
    }

    public void setPersons(final ArrayList<Person> persons) {
        this.persons = persons;
    }
}
