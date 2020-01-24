package com.example.dam_2020_revizii.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.dam_2020_revizii.MainActivity;

import java.text.ParseException;
import java.util.Date;

public class Revizie implements Parcelable {
    private String numarAuto;
    private int numarKm;
    private Date data;
    private double cost;
    private TipRevizie tip;

    protected Revizie(Parcel in) {
        numarAuto = in.readString();
        numarKm = in.readInt();
        cost = in.readDouble();
        try {
            data = MainActivity.dateFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tip = TipRevizie.valueOf(in.readString());
    }

    public static final Creator<Revizie> CREATOR = new Creator<Revizie>() {
        @Override
        public Revizie createFromParcel(Parcel in) {
            return new Revizie(in);
        }

        @Override
        public Revizie[] newArray(int size) {
            return new Revizie[size];
        }
    };

    @Override
    public String toString() {
        return "Revizie{" +
                "numarAuto='" + numarAuto + '\'' +
                ", numarKm=" + numarKm +
                ", data=" + data +
                ", cost=" + cost +
                ", tip=" + tip +
                '}';
    }

    public Revizie(String numarAuto, int numarKm, Date data, double cost, TipRevizie tip) {
        this.numarAuto = numarAuto;
        this.numarKm = numarKm;
        this.data = data;
        this.cost = cost;
        this.tip = tip;
    }

    public String getNumarAuto() {
        return numarAuto;
    }

    public void setNumarAuto(String numarAuto) {
        this.numarAuto = numarAuto;
    }

    public int getNumarKm() {
        return numarKm;
    }

    public void setNumarKm(int numarKm) {
        this.numarKm = numarKm;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public TipRevizie getTip() {
        return tip;
    }

    public void setTip(TipRevizie tip) {
        this.tip = tip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numarAuto);
        dest.writeInt(numarKm);
        dest.writeDouble(cost);
        String dateStr = MainActivity.dateFormat.format(data);
        dest.writeString(dateStr);
        dest.writeString(tip.toString());
    }
}
