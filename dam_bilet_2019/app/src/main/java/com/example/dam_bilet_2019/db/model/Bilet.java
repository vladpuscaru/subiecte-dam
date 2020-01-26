package com.example.dam_bilet_2019.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bilete")
public class Bilet implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ColumnInfo(name = "numar")
    private int numar;
    @ColumnInfo(name = "plecare")
    private String plecare;
    @ColumnInfo(name = "destinatie")
    private String destinatie;
    @ColumnInfo(name = "tarif_redus")
    private boolean tarifRedus;
    @ColumnInfo(name = "pret")
    private double pret;

    public Bilet(int numar, String plecare, String destinatie, boolean tarifRedus, double pret) {
        this.numar = numar;
        this.plecare = plecare;
        this.destinatie = destinatie;
        this.tarifRedus = tarifRedus;
        this.pret = pret;
    }

    protected Bilet(Parcel in) {
        id = in.readLong();
        numar = in.readInt();
        plecare = in.readString();
        destinatie = in.readString();
        tarifRedus = in.readByte() != 0;
        pret = in.readDouble();
    }

    public static final Creator<Bilet> CREATOR = new Creator<Bilet>() {
        @Override
        public Bilet createFromParcel(Parcel in) {
            return new Bilet(in);
        }

        @Override
        public Bilet[] newArray(int size) {
            return new Bilet[size];
        }
    };

    public int getNumar() {
        return numar;
    }

    public void setNumar(int numar) {
        this.numar = numar;
    }

    public String getPlecare() {
        return plecare;
    }

    public void setPlecare(String plecare) {
        this.plecare = plecare;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public void setDestinatie(String destinatie) {
        this.destinatie = destinatie;
    }

    public boolean isTarifRedus() {
        return tarifRedus;
    }

    public void setTarifRedus(boolean tarifRedus) {
        this.tarifRedus = tarifRedus;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "numar=" + numar +
                ", plecare='" + plecare + '\'' +
                ", destinatie='" + destinatie + '\'' +
                ", tarifRedus=" + tarifRedus +
                ", pret=" + pret +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(numar);
        dest.writeString(plecare);
        dest.writeString(destinatie);
        dest.writeByte((byte) (tarifRedus ? 1 : 0));
        dest.writeDouble(pret);
    }
}
