package com.example.dam_livrare_2020.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dam_livrare_2020.database.dao.LivrareDao;
import com.example.dam_livrare_2020.database.model.Livrare;

@Database(entities = {Livrare.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static String DB_NAME = "db_examen";
    private static AppDatabase appDatabase = null;

    protected AppDatabase() {}

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
                }
                return appDatabase;
            }
        }
        return appDatabase;
    }

    public abstract LivrareDao livrareDao();
}
