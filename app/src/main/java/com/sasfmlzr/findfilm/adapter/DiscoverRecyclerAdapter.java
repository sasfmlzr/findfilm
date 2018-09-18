package com.sasfmlzr.findfilm.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.databinding.DiscoverFilmItemBinding;
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

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
        DiscoverFilmItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.discover_film_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscoverMovieRequest.Result currentFilm = filmList.get(position);
        DiscoverFilmItemBinding binding = holder.binding;

        binding.nameFilm.setText(currentFilm.getTitle());
        binding.scoreFilm.setText(String.valueOf(currentFilm.getVoteAverage()));
        String url;
        if (currentFilm.getBackdropPath() == null) {
            url = URL_IMAGE_500PX + currentFilm.getPosterPath();
        } else {
            url = URL_IMAGE_500PX + currentFilm.getBackdropPath();
        }
        Picasso.get().load(url).into(holder.target);

        if (position == filmList.size() - 1) {
            elementEndedCallback.isEnded();
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public void addElements(List<DiscoverMovieRequest.Result> filmList) {
        this.filmList.addAll(filmList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DiscoverFilmItemBinding binding;

        final Target target;

        ViewHolder(DiscoverFilmItemBinding binding) {
            super(binding.itemCardView);

            this.binding = binding;
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    binding.previewFilmImageView.setImageBitmap(bitmap);
                    binding.progressBarLoaderImage.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            filmSelectedListener.filmClicked(filmList.get(getAdapterPosition()).getId());
        }
    }
}
