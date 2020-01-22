package com.example.dam_c_2020_1.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dam_c_2020_1.db.model.Citat;

import java.util.List;

@Dao
public interface CitatDao {

    @Query("SELECT * FROM 'citate'")
    List<Citat> getAll();

    @Delete
    void delete(Citat c);

    @Insert
    long insert(Citat c);

    @Update
    void update(Citat c);
}
