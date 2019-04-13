package com.example.nastya.homework4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends Fragment {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<ListItem> itemsList = new ArrayList<>();
    private View rootView;
    private LocalDate curDay;
    private LocalDate yesDay;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        curDay = LocalDate.now();
        yesDay = curDay.minusDays(1);

        NewsDatabase db = App.getInstance().getDatabase();
        NewsDao newsDao = db.newsDao();

        mDisposable.add(newsDao.getAll()
                .map(itemNewsList -> {
                    if (itemNewsList.size() != 0) {

                        long dateInMilliseconds = itemNewsList.get(0).getDateNews();
                        itemsList.add(new ItemDateGroup(NewsListFragment.this.checkDate(dateFormat.print(dateInMilliseconds))));

                        for (ItemNews i : itemNewsList) {

                            if (i.getDateNews() == dateInMilliseconds) {
                                itemsList.add(i);
                            } else {
                                dateInMilliseconds = i.getDateNews();
                                itemsList.add(new ItemDateGroup(NewsListFragment.this.checkDate(dateFormat.print(dateInMilliseconds))));
                                itemsList.add(i);
                            }
                        }
                        return itemsList;
                    } else {
                        itemsList.clear();
                        return itemsList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listItems -> startInsert()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(itemsList, (position, news) ->
                NewsListFragment.this.startActivity(NewsContentActivity.createIntent(NewsListFragment.this.getContext(), news)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void startInsert() {
        recyclerView.setAdapter(myAdapter);
    }

    private String checkDate(String date) {

        if (date.equals(curDay.toString())) {
            return getString(R.string.today);
        }
        if (date.equals(yesDay.toString())) {
            return getString(R.string.yesterday);
        }
        return date;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
