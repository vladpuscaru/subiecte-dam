package com.example.dam_livrare_2020.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dam_livrare_2020.MainActivity;

import java.text.ParseException;
import java.util.Date;

@Entity(tableName = "livrari")
public class Livrare implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "destinatar")
    private String destinatar;
    @ColumnInfo(name = "adresa")
    private String adresa;
    @ColumnInfo(name = "data")
    private Date data;
    @ColumnInfo(name = "valoare")
    private float valoare;
    @ColumnInfo(name = "tip")
    private TipLivrare tip;

    protected Livrare(Parcel in) {
        id = in.readLong();
        destinatar = in.readString();
        adresa = in.readString();
        valoare = in.readFloat();
        try {
            data = MainActivity.dateFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tip = TipLivrare.valueOf(in.readString());
    }

    public static final Creator<Livrare> CREATOR = new Creator<Livrare>() {
        @Override
        public Livrare createFromParcel(Parcel in) {
            return new Livrare(in);
        }

        @Override
        public Livrare[] newArray(int size) {
            return new Livrare[size];
        }
    };

    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "Destinatar: " + destinatar + "\n" +
                "Adresa: " + adresa + "\n" +
                "Data: " + MainActivity.dateFormat.format(data) + "\n" +
                "Valoare: " + valoare + "\n" +
                "Tip: " + tip;
    }

    public Livrare(String destinatar, String adresa, Date data, float valoare, TipLivrare tip) {
        this.destinatar = destinatar;
        this.adresa = adresa;
        this.data = data;
        this.valoare = valoare;
        this.tip = tip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDestinatar() {
        return destinatar;
    }

    public void setDestinatar(String destinatar) {
        this.destinatar = destinatar;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getValoare() {
        return valoare;
    }

    public void setValoare(float valoare) {
        this.valoare = valoare;
    }

    public TipLivrare getTip() {
        return tip;
    }

    public void setTip(TipLivrare tip) {
        this.tip = tip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(destinatar);
        dest.writeString(adresa);
        dest.writeFloat(valoare);
        String dateStr = MainActivity.dateFormat.format(this.data);
        dest.writeString(dateStr);
        dest.writeString(tip.toString());
    }
}
