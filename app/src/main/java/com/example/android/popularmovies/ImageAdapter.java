package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

//This is the adapter class.
public class ImageAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List mItems;

    public ImageAdapter(Context context, List items) {
        super(context, R.layout.list_view_item, items);
        this.context = context;
        this.mItems = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateMovies(List items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);
        }

        Picasso
                .with(context)
                .load((String) mItems.get(position))
                .fit()
                .into((ImageView) convertView);

        return convertView;
    }
}

