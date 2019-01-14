package com.example.boo.lab2android.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.boo.lab2android.models.Etag;

@Dao
public interface EtagDAO {
    @Query("SELECT * FROM Etag LIMIT 1")
    Etag getEtag();

    @Insert
    void insert(Etag etag);

    @Query("DELETE FROM Etag")
    void deleteAll();

    @Update
    void update(Etag etag);
}
