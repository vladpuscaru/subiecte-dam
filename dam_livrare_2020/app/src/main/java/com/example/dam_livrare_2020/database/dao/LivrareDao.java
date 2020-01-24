package com.example.dam_livrare_2020.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dam_livrare_2020.database.model.Livrare;

import java.util.List;

@Dao
public interface LivrareDao {

    @Query("SELECT * FROM 'livrari'")
    List<Livrare> getAll();

    @Insert
    long insert(Livrare livrare);

    @Delete
    void delete(Livrare livrare);

    @Update
    void update(Livrare livrare);
}
