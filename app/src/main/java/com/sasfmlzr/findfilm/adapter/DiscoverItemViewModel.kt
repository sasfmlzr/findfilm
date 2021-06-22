package com.sasfmlzr.findfilm.adapter

import androidx.databinding.BaseObservable
import com.sasfmlzr.findfilm.model.SystemSettings
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class DiscoverItemViewModel : BaseObservable() {

    fun loadBitmap(currentFilm: DiscoverMovieRequest.Result, target: Target) {
        Picasso.get().load(setBackdropPath(currentFilm)).into(target)
    }

    private fun setBackdropPath(currentFilm: DiscoverMovieRequest.Result): String {
        return if (currentFilm.backdropPath == null) {
            SystemSettings.URL_IMAGE_500PX + currentFilm.posterPath
        } else {
            SystemSettings.URL_IMAGE_500PX + currentFilm.backdropPath
        }
    }
}
