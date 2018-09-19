package com.sasfmlzr.findfilm.fragment.currentfilm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.android.databinding.library.baseAdapters.BR;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;
import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class CurrentFilmViewModel extends BaseObservable {
    private static final ObservableField<String> materialMessage = new ObservableField<>("Buy tickets pressed");
    private static final ObservableField<String> videoMessage = new ObservableField<>("Play video pressed");

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    private int idFilm;

    @Bindable
    private CurrentMovieRequest currentMovieField;

    @Bindable
    private String toastMessage = null;

    public interface FilmLoaded {
        void isLoaded(CurrentMovieRequest currentMovieRequest);
    }

    public interface DownloadImage {
        void isDownloaded(CurrentMovieRequest currentMovieRequest);
    }

    public void start(int filmId) {
        this.idFilm = filmId;
        loadFilm();
    }

    public CurrentMovieRequest getCurrentMovieField() {
        return currentMovieField;
    }

    private void setCurrentMovieField(CurrentMovieRequest currentMovieField) {
        this.currentMovieField = currentMovieField;
        notifyPropertyChanged(BR.currentMovieField);
    }

    private void loadFilm() {
        DownloadImage imageDownloadCallback = (film) -> {
            currentMovieField.setBackdropBitmap(film.getBackdropBitmap());
            dataLoading.set(true);
        };

        FilmLoaded filmLoadCallback = (currentMovieRequest) -> {
            setCurrentMovieField(currentMovieRequest);

            String url;
            if (currentMovieRequest.getBackdropPath() == null) {
                url = URL_IMAGE_500PX + currentMovieRequest.getPosterPath();
            } else {
                url = URL_IMAGE_500PX + currentMovieRequest.getBackdropPath();
            }
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    currentMovieRequest.setBackdropBitmap(bitmap);
                    imageDownloadCallback.isDownloaded(currentMovieRequest);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(url).into(target);
        };

        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getCurrentMovie(idFilm, API_KEY, LANGUAGE)
                .enqueue(new Callback<CurrentMovieRequest>() {
                    @Override
                    public void onResponse(Call<CurrentMovieRequest> call, Response<CurrentMovieRequest> response) {
                        filmLoadCallback.isLoaded(response.body());
                    }

                    @Override
                    public void onFailure(Call<CurrentMovieRequest> call, Throwable t) {

                    }
                });
    }
    
    /**
     * Toast message methods
     */
    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(ObservableField<String> toastMessage) {
        this.toastMessage = toastMessage.get();
        notifyPropertyChanged(BR.toastMessage);
    }

    public void materialButtonClicked() {
        setToastMessage(materialMessage);
    }

    public void videoButtonClicked() {
        setToastMessage(videoMessage);
    }
}
