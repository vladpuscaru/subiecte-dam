package com.example.dam_c_2020_1.db;

import androidx.room.TypeConverter;

import com.example.dam_c_2020_1.db.model.Categorie;

public class Converter {
    @TypeConverter
    public static String fromCategorie(Categorie value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public static Categorie fromString(String value) {
        return value == null ? null : Categorie.valueOf(value);
    }
}
