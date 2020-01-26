package com.example.dam_bilet_2019.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dam_bilet_2019.db.model.Bilet;

import java.util.List;

@Dao
public interface BiletDao {

    @Query("SELECT * FROM 'bilete'")
    List<Bilet> getAll();

    @Insert
    long insert(Bilet bilet);
}
