package com.sasfmlzr.findfilm.adapter;

import android.databinding.BaseObservable;

import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class ItemViewModel extends BaseObservable {

    public void loadBitmap(DiscoverMovieRequest.Result currentFilm, Target target){
        Picasso.get().load(setBackdropPath(currentFilm)).into(target);
    }

    private String setBackdropPath(DiscoverMovieRequest.Result currentFilm) {
        if (currentFilm.getBackdropPath() == null) {
            return URL_IMAGE_500PX + currentFilm.getPosterPath();
        } else {
            return URL_IMAGE_500PX + currentFilm.getBackdropPath();
        }
    }


}
