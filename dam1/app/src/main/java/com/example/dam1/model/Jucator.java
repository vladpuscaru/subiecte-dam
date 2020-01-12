package com.example.dam1.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.dam1.MainActivity;

import java.text.ParseException;
import java.util.Date;

@Entity (tableName = "jucatori")
public class Jucator implements Parcelable {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo(name = "numar")
    private int numar;
    @ColumnInfo(name = "nume")
    private String nume;
    @ColumnInfo(name = "data_nasterii")
    private Date dataNasterii;
    @ColumnInfo(name = "pozitie")
    private PozitieFotbal pozitie;

    @Ignore
    protected Jucator(Parcel in) {
        numar = in.readInt();
        nume = in.readString();
        try {
            dataNasterii = MainActivity.dateFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pozitie = PozitieFotbal.valueOf(in.readString());
    }

    @Ignore
    public static final Creator<Jucator> CREATOR = new Creator<Jucator>() {
        @Override
        public Jucator createFromParcel(Parcel in) {
            return new Jucator(in);
        }

        @Override
        public Jucator[] newArray(int size) {
            return new Jucator[size];
        }
    };

    @Override
    public String toString() {
        return "Jucator{" +
                "numar=" + numar +
                ", nume='" + nume + '\'' +
                ", dataNasterii=" + dataNasterii +
                ", pozitie=" + pozitie +
                '}';
    }

    @Ignore
    public Jucator() {
    }

    public Jucator(int numar, String nume, Date dataNasterii, PozitieFotbal pozitie) {
        this.numar = numar;
        this.nume = nume;
        this.dataNasterii = dataNasterii;
        this.pozitie = pozitie;
    }

    public int getNumar() {
        return numar;
    }

    public void setNumar(int numar) {
        this.numar = numar;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Date getDataNasterii() {
        return dataNasterii;
    }

    public void setDataNasterii(Date dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public PozitieFotbal getPozitie() {
        return pozitie;
    }

    public void setPozitie(PozitieFotbal pozitie) {
        this.pozitie = pozitie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numar);
        dest.writeString(nume);
        String dateStr = MainActivity.dateFormat.format(this.dataNasterii);
        dest.writeString(dateStr);
        dest.writeString(pozitie.toString());
    }
}
