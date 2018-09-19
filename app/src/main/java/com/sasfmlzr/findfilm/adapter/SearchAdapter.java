package com.sasfmlzr.findfilm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.databinding.SearchItemBinding;
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
        SearchItemBinding binding = DataBindingUtil.bind(view);
        assert binding != null;
        ViewHolder holder = new ViewHolder(binding);
        if (filmList != null) {
            holder.binding.itemSearchTitle.setText(filmList.get(cursor.getPosition()).getTitle());
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return inflater.inflate(R.layout.search_item, parent, false);
    }

    private class ViewHolder implements View.OnClickListener {
        private SearchItemBinding binding;
        final int position = getCursor().getPosition();

        ViewHolder(SearchItemBinding binding) {
            this.binding = binding;
            binding.itemSearchTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmSelectedListener.filmClicked(filmList.get(position).getId());
        }
    }
}
