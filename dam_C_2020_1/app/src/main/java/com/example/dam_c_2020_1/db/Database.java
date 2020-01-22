package com.example.dam_c_2020_1.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dam_c_2020_1.db.dao.CitatDao;
import com.example.dam_c_2020_1.db.model.Citat;

@androidx.room.Database(entities = {Citat.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "app_db";
    private static Database database = null;

    protected Database() {}

    public static Database getInstance(Context context) {
        if (database == null) {
            synchronized (Database.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context, Database.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    return database;
                }
                return database;
            }
        }
        return database;
    }

    public abstract CitatDao citatDao();
}
