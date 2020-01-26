package com.example.dam_examen_2020.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dam_examen_2020.db.dao.ExamenDao;
import com.example.dam_examen_2020.db.model.Examen;

@Database(entities = {Examen.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DB_NAME = "db_examen";
    private static AppDatabase appDatabase;

    protected AppDatabase() {}

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
                    return appDatabase;
                }
                return appDatabase;
            }
        }
        return appDatabase;
    }

    public abstract ExamenDao examenDao();
}
