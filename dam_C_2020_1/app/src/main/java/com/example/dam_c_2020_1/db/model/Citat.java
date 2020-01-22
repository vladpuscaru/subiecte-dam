package com.example.dam_c_2020_1.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "citate", indices = {@Index(value = {"text"}, unique = true)})
public class Citat implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "autor")
    private String autor;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "aprecieri")
    private int numarAprecieri;
    @ColumnInfo(name = "categorie")
    private Categorie categorie;

    @Override
    public String toString() {
        return "\"" + text + "\"" + "\n" +
                "Autor: " + autor + "\t\t\t" + numarAprecieri + " aprecieri" + "\n" +
                "Categorie: " + categorie + "\n" + "Id: " + id;
    }

    public Citat(String autor, String text, int numarAprecieri, Categorie categorie) {
        this.autor = autor;
        this.text = text;
        this.numarAprecieri = numarAprecieri;
        this.categorie = categorie;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumarAprecieri() {
        return numarAprecieri;
    }

    public void setNumarAprecieri(int numarAprecieri) {
        this.numarAprecieri = numarAprecieri;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
