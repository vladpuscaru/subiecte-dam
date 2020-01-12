package com.example.dam1.database;

import androidx.room.TypeConverter;

import com.example.dam1.model.PozitieFotbal;

import java.util.Date;

public class DbConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromPozitieFotbal(PozitieFotbal value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public static PozitieFotbal pozitieFotbalToString(String value) {
        return value == null ? null : PozitieFotbal.valueOf(value);
    }
}
