package com.sasfmlzr.findfilm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public class SearchAdapter extends CursorAdapter {
    private List<DiscoverMovieRequest.ResultsField> filmList;
    private TextView text;

    public SearchAdapter(Context context,
                         Cursor cursor,
                         List<DiscoverMovieRequest.ResultsField> filmList) {
        super(context, cursor, false);
        this.filmList = filmList;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (filmList!=null){
            text.setText(filmList.get(cursor.getPosition()).getTitle());
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.search_item, parent, false);
        text = view.findViewById(R.id.itemSearchTitle);
        return view;
    }
}
