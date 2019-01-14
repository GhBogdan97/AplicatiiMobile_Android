package com.example.boo.lab2android.viewobject;

public class ContactVO {
    private int Id;
    private String Firstname;
    private String Lastname;
    private String PhoneNumber;
    private String WorkNumber;


    public String getFirstname() {
        return Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getWorkNumber() {
        return WorkNumber;
    }

    public Integer getId() {
        return Id;
    }


    @Override
    public String toString() {
        return "Contacts{" +
                "id='" + Id + '\'' +
                ", text='" + Firstname + '\'' +
                '}';
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setWorkNumber(String workNumber) {
        WorkNumber = workNumber;
    }
}
