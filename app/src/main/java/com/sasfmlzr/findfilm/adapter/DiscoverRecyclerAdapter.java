package com.sasfmlzr.findfilm.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    private List<DiscoverMovieRequest.Result> filmList;
    private DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    private DiscoverFilmFragment.RecyclerElementEnded elementEndedCallback;

    public DiscoverRecyclerAdapter(List<DiscoverMovieRequest.Result> filmList,
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
        DiscoverMovieRequest.Result currentFilm = filmList.get(position);
        holder.nameFilm.setText(currentFilm.getTitle());
        String overview = currentFilm.getOverview();
        if (overview.length() >= 97) {
            overview = overview.substring(0, 97) + "...";
        }
        holder.descriptionFilm.setText(overview);
        Bitmap bitmap = currentFilm.getBackdropBitmap();
        holder.imageFilmView.setImageBitmap(bitmap);
        if (bitmap != null) {
            holder.progressLoaderImage.setVisibility(View.INVISIBLE);
        }
        if (position == filmList.size() - 1) {
            elementEndedCallback.isEnded();
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public void replaceImageViewFilm(DiscoverMovieRequest.Result film) {
        for (int pos = 0; pos < filmList.size(); pos++) {
            DiscoverMovieRequest.Result currentFilm = filmList.get(pos);
            Boolean equalsImageFilm;
            if (currentFilm.getBackdropPath() == null) {
                equalsImageFilm = currentFilm.getPosterPath().equals(film.getBackdropPath());
            } else {
                equalsImageFilm = currentFilm.getBackdropPath().equals(film.getBackdropPath());
            }
            if (equalsImageFilm) {
                filmList.set(pos, film);
                notifyItemChanged(pos);
                return;
            }
        }
    }

    public void addElements(List<DiscoverMovieRequest.Result> filmList) {
        this.filmList.addAll(filmList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameFilm;
        final TextView descriptionFilm;
        final ImageView imageFilmView;
        final ProgressBar progressLoaderImage;

        ViewHolder(View itemView) {
            super(itemView);
            nameFilm = itemView.findViewById(R.id.nameFilm);
            descriptionFilm = itemView.findViewById(R.id.descriptionFilm);
            imageFilmView = itemView.findViewById(R.id.previewFilmImageView);
            progressLoaderImage = itemView.findViewById(R.id.progressBarLoaderImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmSelectedListener.filmClicked(filmList.get(getAdapterPosition()).getId());
        }
    }
}
