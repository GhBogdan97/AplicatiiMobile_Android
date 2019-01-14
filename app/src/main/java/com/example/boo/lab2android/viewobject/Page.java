package com.example.boo.lab2android.viewobject;

import java.util.List;

public class Page {
    private List<ContactVO> Contacts;


    public List<ContactVO> getContactVOS() {return Contacts;}

    @Override
    public String toString(){
        return "Page{" +
                    ", contactVOS= " + Contacts +
                    "}";
    }
}
