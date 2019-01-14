package com.example.boo.lab2android.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.boo.lab2android.daos.ContactDAO;
import com.example.boo.lab2android.daos.EtagDAO;
import com.example.boo.lab2android.daos.UserDAO;
import com.example.boo.lab2android.models.Contact;
import com.example.boo.lab2android.models.Etag;
import com.example.boo.lab2android.models.User;

@Database(entities = {User.class, Contact.class, Etag.class},version = 1)
public abstract class UserDB extends RoomDatabase {
    public static UserDB databaseInstance;

    public abstract UserDAO getUserDao();

    public abstract ContactDAO getContactDao();

    public abstract EtagDAO getEtagDao();

    public static UserDB getDatabase(Context context)
    {
        if(databaseInstance==null)
        {
            databaseInstance=Room.databaseBuilder(context.getApplicationContext(),UserDB.class, "user-database").allowMainThreadQueries().build();
        }
        return databaseInstance;
    }

    public static void destroyInstance()
    {
        databaseInstance = null;
    }
}
