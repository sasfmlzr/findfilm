package com.sasfmlzr.findfilm.model.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.model.request.DiscoverMovieRequest;

import java.util.List;

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    List<DiscoverMovieRequest.ResultsField> filmList;

    public DiscoverRecyclerAdapter(List<DiscoverMovieRequest.ResultsField> filmList) {
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_film_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscoverMovieRequest.ResultsField currentFilm = filmList.get(position);
        holder.nameFilm.setText(currentFilm.getTitle());
        String overview = currentFilm.getOverview();
        if(overview.length()>=97) {
            overview = overview.substring(0,97) + "...";
        }
        holder.descriptionFilm.setText(overview);
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameFilm;
        final TextView descriptionFilm;
        final ImageView imageFilmView;

        ViewHolder(View itemView) {
            super(itemView);
            nameFilm = itemView.findViewById(R.id.nameFilm);
            descriptionFilm = itemView.findViewById(R.id.descriptionFilm);
            imageFilmView = itemView.findViewById(R.id.previewFilmImageView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
