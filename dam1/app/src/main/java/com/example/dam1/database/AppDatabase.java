package com.example.dam1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dam1.database.dao.JucatorDao;
import com.example.dam1.model.Jucator;

@Database(entities = {Jucator.class}, version = 2, exportSchema = false)
@TypeConverters({DbConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "dam1_db";
    private static AppDatabase appDatabase = null;

    protected AppDatabase() {
    }

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    return appDatabase;
                }
            }
        }
        return appDatabase;
    }

    public abstract JucatorDao jucatorDao();
}
