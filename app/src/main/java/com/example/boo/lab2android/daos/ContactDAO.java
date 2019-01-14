package com.example.boo.lab2android.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.boo.lab2android.models.Contact;

import java.util.List;

@Dao
public interface ContactDAO {
    @Query("SELECT * FROM Contacts")
    LiveData<List<Contact>> getLiveContacts();

    @Insert
    void insert(Contact contact);

    @Query("DELETE FROM Contacts")
    void deleteAll();

    @Update
    void update(Contact contact);

    @Query("SELECT * FROM Contacts")
    List<Contact> getContacts();
}
