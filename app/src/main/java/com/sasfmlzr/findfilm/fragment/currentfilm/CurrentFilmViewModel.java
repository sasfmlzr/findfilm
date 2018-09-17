package com.sasfmlzr.findfilm.fragment.currentfilm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.android.databinding.library.baseAdapters.BR;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;
import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class CurrentFilmViewModel extends BaseObservable {


    public ObservableField<Bitmap> posterFilm = new ObservableField<>();
    public ObservableField<Bitmap> shadow = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public  ObservableField<String> voteAverage = new ObservableField<>();
    public  ObservableField<String> releaseDate = new ObservableField<>();
    public  ObservableField<String> title = new ObservableField<>();
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);


    private String materialMessage = "Buy tickets pressed";
    private String videoMessage = "Play video pressed";


    @Bindable
    public String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public void emptyButtonClicked(){
        setToastMessage(videoMessage);
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public void setTitle(ObservableField<String> title) {
        this.title = title;
    }

    private int idFilm;

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

    public void loadFilm() {
        DownloadImage imageDownloadCallback = (film) -> {
            Bitmap imageFilm = film.getBackdropBitmap();

            posterFilm.set(imageFilm);

         //   shadow.set(createShadow(imageFilm));
            dataLoading.set(true);
        };

        FilmLoaded filmLoadCallback = (currentMovieRequest) -> {
            title.set(currentMovieRequest.getTitle());
            releaseDate.set(currentMovieRequest.getReleaseDate());
            voteAverage.set(String.valueOf(currentMovieRequest.getVoteAverage()));
            description.set(currentMovieRequest.getOverview());
           //setVisibleItems(true);
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

    public void buttonBuyTicketsPressed() {

    }

    public void buttonPlayVideoPressed() {

    }

    private Drawable createShadow(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Palette palette = Palette.from(bitmap).generate();
            palette.getLightMutedSwatch();
            int color;
            Palette.Swatch swatch = palette.getLightMutedSwatch();
            if (swatch != null) {
                color = swatch.getRgb();
            } else {
                color = palette.getDominantSwatch().getRgb();
            }

            int radiusValue = 2;
            int[] colors = {color, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            shadow.setCornerRadius(radiusValue);
            shadow.setAlpha(150);
            return shadow;
        } else {
            return null;
        }
    }
}
