package com.example.nastya.homework4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsContentActivity extends AppCompatActivity {
    private FavouritesNewsDao favouritesNewsDao;
    private FavouritesNews currentNews;
    private int id;
    private Boolean starVisible = false;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MMMM-dd");

    public static Intent createIntent(Context context, ItemNews news) {
        return new Intent(context, NewsContentActivity.class)
                .putExtra(ItemNews.class.getSimpleName(), news);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        TextView contentDate = findViewById(R.id.contentDate);
        TextView contentDescription = findViewById(R.id.contentDescription);

        ItemNews itemNews = (ItemNews) getIntent().getSerializableExtra(ItemNews.class.getSimpleName());
        id = itemNews.getId();

        setTitle(itemNews.getTitleNews());
        contentDate.setText(dateFormat.print(itemNews.getDateNews()));
        contentDescription.setText(itemNews.getDescriptionNews());
    }

    @Override
    protected void onStart() {
        super.onStart();
        NewsDatabase db = App.getInstance().getDatabase();
        favouritesNewsDao = db.favouritesNewsDao();

        mDisposable.add(favouritesNewsDao.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(new DisposableSingleObserver<FavouritesNews>() {
                    @Override
                    public void onSuccess(FavouritesNews favouritesNews) {
                        starVisible = true;
                        currentNews = favouritesNews;
                    }

                    @Override
                    public void onError(Throwable e) {
                        starVisible = false;
                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem favourites = menu.findItem(R.id.favourites);
        if (starVisible) {
            favourites.setIcon(R.drawable.added);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.favourites) {
            if (!starVisible) {

                currentNews = new FavouritesNews(id);
                mDisposable.add(favouritesNewsDao.insert(currentNews)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Toast.makeText(NewsContentActivity.this, getString(R.string.addedToFavourites), Toast.LENGTH_LONG).show();
                                menuItem.setIcon(R.drawable.added);
                                starVisible = true;
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(NewsContentActivity.this, getString(R.string.notAddedToFavourites), Toast.LENGTH_LONG).show();
                            }
                        }));
            } else {

                mDisposable.add(favouritesNewsDao.delete(currentNews)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Toast.makeText(NewsContentActivity.this, getString(R.string.deletedFromFavourites), Toast.LENGTH_LONG).show();
                                menuItem.setIcon(R.drawable.not_added);
                                starVisible = false;
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(NewsContentActivity.this, getString(R.string.notDeletedFromFavourites), Toast.LENGTH_LONG).show();
                            }
                        }));

            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
