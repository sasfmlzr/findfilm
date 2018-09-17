package com.sasfmlzr.findfilm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public class SearchAdapter extends CursorAdapter {
    private List<DiscoverMovieRequest.Result> filmList;
    private DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;

    public SearchAdapter(Context context,
                         Cursor cursor,
                         List<DiscoverMovieRequest.Result> filmList,
                         DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener) {
        super(context, cursor, false);
        this.filmList = filmList;
        this.filmSelectedListener = filmSelectedListener;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
        }
        if (filmList != null) {
            holder.text.setText(filmList.get(cursor.getPosition()).getTitle());
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return inflater.inflate(R.layout.search_item, parent, false);
    }

    private class ViewHolder implements View.OnClickListener {
        final TextView text;
        final int position = getCursor().getPosition();

        ViewHolder(View itemView) {
            text = itemView.findViewById(R.id.itemSearchTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmSelectedListener.filmClicked(filmList.get(position).getId());
        }
    }
}
