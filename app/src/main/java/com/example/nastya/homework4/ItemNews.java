package com.example.nastya.homework4;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

interface News {
    int getId();
}

@Entity
public class ItemNews implements News, ListItem, Serializable {

    @PrimaryKey
    private int id;
    @ColumnInfo
    private String titleNews;
    @ColumnInfo
    private long dateNews;
    @ColumnInfo
    private String descriptionNews;

    ItemNews(int id, String titleNews, long dateNews, String descriptionNews) {
        this.id = id;
        this.titleNews = titleNews;
        this.dateNews = dateNews;
        this.descriptionNews = descriptionNews;
    }

    String getTitleNews() {
        return titleNews;
    }

    long getDateNews() {
        return dateNews;
    }

    String getDescriptionNews() {
        return descriptionNews;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getType() {
        return TYPE_NEWS;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((News) obj).getId();
    }
}

@Entity
class FavouritesNews implements News {

    @PrimaryKey
    private int id;

    FavouritesNews(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((News) obj).getId();
    }

}