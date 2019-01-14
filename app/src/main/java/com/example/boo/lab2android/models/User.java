package com.example.boo.lab2android.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String token;


    public User(@NonNull String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;

    }

    public User() {
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
