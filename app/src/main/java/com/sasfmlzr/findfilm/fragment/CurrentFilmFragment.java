package com.sasfmlzr.findfilm.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;
import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class CurrentFilmFragment extends Fragment {
    private int idFilm;
    private Unbinder unbinder;

    @BindView(R.id.current_film_image_view)
    ImageView posterFilm;
    @BindView(R.id.name_current_film)
    TextView nameFilm;
    @BindView(R.id.description_current_film)
    TextView description;
    @BindView(R.id.progressBarLoaderCurrentFilm)
    ProgressBar progressLoaderImage;

    public static CurrentFilmFragment newInstance(int idFilm) {
        Bundle args = new Bundle();
        CurrentFilmFragment fragment = new CurrentFilmFragment();
        args.putInt("idFilm", idFilm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idFilm = getArguments().getInt("idFilm");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_film_fragment, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFilm();
    }

    public interface FilmLoaded {
        void isLoaded(CurrentMovieRequest currentMovieRequest);
    }

    public interface DownloadImage {
        void isDownloaded(CurrentMovieRequest currentMovieRequest);
    }

    public void loadFilm() {
        DownloadImage imageDownloadCallback = (film) -> {
            posterFilm.setImageBitmap(film.getBackdropBitmap());
            progressLoaderImage.setVisibility(View.INVISIBLE);
        };

        FilmLoaded filmLoadCallback = (currentMovieRequest) -> {
            nameFilm.setText(currentMovieRequest.getTitle());
            description.setText(currentMovieRequest.getOverview());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
