package com.example.boo.lab2android.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.boo.lab2android.models.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM Users WHERE Users.username LIKE :username")
    LiveData<User> getUser(String username);

    @Query("SELECT * FROM Users LIMIT 1")
    LiveData<User> getLiveCurrentUser();

    @Query("SELECT * FROM Users LIMIT 1")
    User getCurrentUser();

    @Query("DELETE FROM Users")
    void deleteAll();
}
