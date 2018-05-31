package com.sasfmlzr.findfilm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    private List<DiscoverMovieRequest.ResultsField> filmList;
    private DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    private DiscoverFilmFragment.RecyclerElementEnded elementEndedCallback;

    public DiscoverRecyclerAdapter(List<DiscoverMovieRequest.ResultsField> filmList,
                                   DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener,
                                   DiscoverFilmFragment.RecyclerElementEnded elementEndedCallback) {
        this.filmList = filmList;
        this.filmSelectedListener = filmSelectedListener;
        this.elementEndedCallback = elementEndedCallback;
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
        if (overview.length() >= 97) {
            overview = overview.substring(0, 97) + "...";
        }
        holder.descriptionFilm.setText(overview);
        holder.imageFilmView.setImageBitmap(currentFilm.getBackdropBitmap());
        if(position==filmList.size()-1){
            elementEndedCallback.isEnded();
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public void replaceImageViewFilm(DiscoverMovieRequest.ResultsField film) {
        for (int pos = 0; pos < filmList.size(); pos++) {
            DiscoverMovieRequest.ResultsField currentFilm = filmList.get(pos);
            if (currentFilm.getBackdrop_path().equals(film.getBackdrop_path())) {
                filmList.set(pos, film);
                notifyItemChanged(pos);
                return;
            }
        }
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmSelectedListener.filmClicked(filmList.get(getAdapterPosition()).getId());
        }
    }
}
