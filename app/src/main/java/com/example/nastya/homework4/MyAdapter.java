package com.example.nastya.homework4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

interface Listener {
    void onUserClick(int position, ItemNews n);
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ListItem> newsList;
    private Listener listener;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MMMM-dd");

    MyAdapter(List<ListItem> news, Listener listener) {
        this.newsList = news;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_DATE: {
                view = inflater.inflate(R.layout.item_date_group, parent, false);
                return new DataGroupViewHolder(view);
            }
            case ListItem.TYPE_NEWS: {
                view = inflater.inflate(R.layout.item_news, parent, false);
                NewsViewHolder holder = new NewsViewHolder(view);
                view.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onUserClick(pos, (ItemNews) newsList.get(pos));
                    }
                });

                return holder;
            }
            default:
                throw new IllegalArgumentException("unknown viewType=" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_DATE: {
                ItemDateGroup itemDateGroup = (ItemDateGroup) newsList.get(position);
                ((DataGroupViewHolder) holder).textTitle.setText(itemDateGroup.getDate());
                break;
            }
            case ListItem.TYPE_NEWS: {
                ItemNews itemNews = (ItemNews) newsList.get(position);
                ((NewsViewHolder) holder).titleNews.setText(itemNews.getTitleNews());
                ((NewsViewHolder) holder).dateNews.setText(dateFormat.print(itemNews.getDateNews()));
                ((NewsViewHolder) holder).descriptionNews.setText(itemNews.getDescriptionNews());
                break;
            }
            default:
                throw new IllegalArgumentException("unknown viewType=" + viewType);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return newsList.get(position).getType();
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {
        final TextView titleNews;
        final TextView dateNews;
        final TextView descriptionNews;

        NewsViewHolder(View itemView) {
            super(itemView);
            titleNews = itemView.findViewById(R.id.titleNews);
            dateNews = itemView.findViewById(R.id.dateNews);
            descriptionNews = itemView.findViewById(R.id.descriptionNews);
        }
    }

    private class DataGroupViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;

        DataGroupViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.itemDateGroup);
        }
    }

}
