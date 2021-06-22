package com.sasfmlzr.findfilm.fragment.currentfilm

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.sasfmlzr.findfilm.model.RetrofitSingleton
import com.sasfmlzr.findfilm.model.SystemSettings
import com.sasfmlzr.findfilm.request.CurrentMovieRequest
import com.sasfmlzr.findfilm.request.FindFilmApi
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentFilmViewModel : BaseObservable() {
    private var idFilm = 0

    val dataLoading: ObservableBoolean = ObservableBoolean(false)

    val currentMovieField = MutableLiveData<CurrentMovieRequest>()

    val poster = MutableLiveData<Bitmap>()

    /**
     * Toast message methods
     */
    @Bindable
    var toastMessage: String? = null
        private set(toastMessage) {
            field = toastMessage!!.toString()
            notifyPropertyChanged(BR.toastMessage)
        }

    interface FilmLoaded {
        fun isLoaded(currentMovieRequest: CurrentMovieRequest)
    }

    interface DownloadImage {
        fun isDownloaded(currentMovieRequest: CurrentMovieRequest)
    }

    fun start(filmId: Int) {
        idFilm = filmId
        loadFilm()
    }

    private fun loadFilm() {
        val imageDownloadCallback = object : DownloadImage {
            override fun isDownloaded(currentMovieRequest: CurrentMovieRequest) {
                poster.value =currentMovieRequest.backdropBitmap
                dataLoading.set(true)
            }
        }
        val filmLoadCallback = object : FilmLoaded {
            override fun isLoaded(currentMovieRequest: CurrentMovieRequest) {
                currentMovieField.value = currentMovieRequest
                val url: String = if (currentMovieRequest.backdropPath == null) {
                    SystemSettings.URL_IMAGE_500PX + currentMovieRequest.posterPath
                } else {
                    SystemSettings.URL_IMAGE_500PX + currentMovieRequest.backdropPath
                }
                val target: Target = object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        currentMovieRequest.backdropBitmap =bitmap
                        imageDownloadCallback.isDownloaded(currentMovieRequest)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }
                }
                Picasso.get().load(url).into(target)
            }
        }
        val findFilmApi: FindFilmApi = RetrofitSingleton.findFilmApi!!
        findFilmApi.getCurrentMovie(idFilm, SystemSettings.API_KEY, SystemSettings.LANGUAGE).enqueue(object : Callback<CurrentMovieRequest> {
                override fun onResponse(
                    call: Call<CurrentMovieRequest>,
                    response: Response<CurrentMovieRequest>
                ) {
                    filmLoadCallback.isLoaded(response.body()!!)
                }

                override fun onFailure(call: Call<CurrentMovieRequest?>, t: Throwable) {}
            })
    }

    fun materialButtonClicked() {
        toastMessage = "Buy tickets pressed"
    }

    fun videoButtonClicked() {
        toastMessage = "Play video pressed"
    }
}
