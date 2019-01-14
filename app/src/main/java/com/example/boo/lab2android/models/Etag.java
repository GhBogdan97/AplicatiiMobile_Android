package com.example.boo.lab2android.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Etag")
public class Etag {
    @PrimaryKey
    @NonNull
    int Id;

    String Value;

    @NonNull
    public int getId() {
        return Id;
    }

    public void setId(@NonNull int id) {
        Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public Etag(@NonNull int id, String value) {

        Id = id;
        Value = value;
    }

    public Etag() {
    }
}
