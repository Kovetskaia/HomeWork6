package com.example.nastya.homework4;

import android.app.Application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.room.Room;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class App extends Application {

    public static App instance;

    private NewsDatabase database;

    NewsDao newsDao;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDatabase.class, "db")
                .allowMainThreadQueries()
                .build();

        newsDao = database.newsDao();

        if (newsDao.getCount() == 0) {
            List<ItemNews> list = new ArrayList<>();
            list.add(new ItemNews(1, getString(R.string.Title1), ((new GregorianCalendar(2019, Calendar.FEBRUARY, 2)).getTime()).getTime(), getString(R.string.Description1)));
            list.add(new ItemNews(2, getString(R.string.Title2), ((new GregorianCalendar(2019,Calendar.FEBRUARY,22)).getTime()).getTime(), getString(R.string.Description2)));
            list.add(new ItemNews(3, getString(R.string.Title3), ((new GregorianCalendar(2019,Calendar.MARCH,2)).getTime()).getTime(), getString(R.string.Description3)));
            list.add(new ItemNews(4, getString(R.string.Title4), ((new GregorianCalendar(2019,Calendar.APRIL,9)).getTime()).getTime(), getString(R.string.Description4)));
            list.add(new ItemNews(5, getString(R.string.Title5), ((new GregorianCalendar(2019,Calendar.APRIL,8)).getTime()).getTime(), getString(R.string.Description5)));
            list.add(new ItemNews(6, getString(R.string.Title6), ((new GregorianCalendar(2019,Calendar.APRIL,8)).getTime()).getTime(), getString(R.string.Description6)));
            list.add(new ItemNews(7, getString(R.string.Title7), ((new GregorianCalendar(2018,Calendar.APRIL,8)).getTime()).getTime(), getString(R.string.Description7)));
            insertNews(list);
        }
    }
    void insertNews(List<ItemNews> news) {
          newsDao.insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    public NewsDatabase getDatabase() {
        return database;
    }


}

