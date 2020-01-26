package com.example.dam_examen_2020.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dam_examen_2020.db.model.Examen;

import java.util.List;

@Dao
public interface ExamenDao {

    @Query("SELECT * FROM 'examene'")
    List<Examen> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Examen examen);
}
