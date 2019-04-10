package com.example.nastya.homework4;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
interface NewsDao {
    @Query("SELECT * FROM itemnews ORDER BY dateNews DESC")
    Flowable<List<ItemNews>> getAll();

    @Query("SELECT * FROM itemnews WHERE id IN (:ids)")
    Single<List<ItemNews>> getByIds(List<Long> ids);

    @Query("SELECT COUNT(*) FROM itemnews")
    int getCount();

    @Insert
    Completable insert(List<ItemNews> news);
}

@Dao
interface FavouritesNewsDao {
    @Query("SELECT * FROM favouritesnews")
    Flowable<List<FavouritesNews>> getAll();

    @Query("SELECT * FROM favouritesnews WHERE id = :id")
    Single<FavouritesNews> getById(long id);

    @Insert
    Completable insert(FavouritesNews favouritesNews);

    @Delete
    Completable delete(FavouritesNews favouritesNews);

}

@Database(entities = {ItemNews.class, FavouritesNews.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public abstract FavouritesNewsDao favouritesNewsDao();
}

