package com.example.dam1.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dam1.model.Jucator;

import java.util.List;

@Dao
public interface JucatorDao {

    @Query("SELECT * FROM jucatori")
    List<Jucator> getAll();

    @Insert
    void insertAll(List<Jucator> jucatori);
}
