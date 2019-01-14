package com.example.boo.lab2android.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Contacts")
public class Contact {
    @PrimaryKey
    @NonNull
    int Id;

    String Firstname;
    String Lastname;
    String PhoneNumber;
    String WorkNumber;

    public Contact(@NonNull int id, String firstname, String lastname, String phoneNumber, String workNumber) {
        Id = id;
        Firstname = firstname;
        Lastname = lastname;
        PhoneNumber = phoneNumber;
        WorkNumber = workNumber;
    }

    public Contact() {
    }


    @NonNull
    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getWorkNumber() {
        return WorkNumber;
    }

    public void setWorkNumber(String workNumber) {
        WorkNumber = workNumber;
    }
}
