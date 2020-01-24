package com.example.dam_livrare_2020.database;

import androidx.room.TypeConverter;

import com.example.dam_livrare_2020.MainActivity;
import com.example.dam_livrare_2020.database.model.TipLivrare;

import java.text.ParseException;
import java.util.Date;

public class Converter {
    @TypeConverter
    public String fromDate(Date value) {
        return value == null ? null : MainActivity.dateFormat.format(value);
    }

    @TypeConverter
    public Date fromString(String value) {
        try {
            return value == null ? null : MainActivity.dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public TipLivrare fromTipString(String value) {
        return value == null ? null : TipLivrare.valueOf(value.toUpperCase());
    }

    @TypeConverter
    public String fromTip(TipLivrare tip) {
        return tip == null ? null : tip.toString();
    }
}
