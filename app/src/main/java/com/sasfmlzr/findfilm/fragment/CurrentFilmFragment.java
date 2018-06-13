package com.sasfmlzr.findfilm.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.RequestMovie;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class CurrentFilmFragment extends Fragment {
    private int idFilm;
    private ImageView posterFilm;
    private TextView nameFilm;
    private TextView description;
    private ProgressBar progressLoaderImage;

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
        posterFilm = view.findViewById(R.id.current_film_image_view);
        progressLoaderImage = view.findViewById(R.id.progressBarLoaderCurrentFilm);
        nameFilm = view.findViewById(R.id.name_current_film);
        description = view.findViewById(R.id.description_current_film);
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
            new DownloadImageTask(currentMovieRequest, imageDownloadCallback).execute();
        };

        new LoadDataFilmTask(idFilm, filmLoadCallback).execute();
    }

    static class LoadDataFilmTask extends AsyncTask<Void, Void, CurrentMovieRequest> {
        private int idFilm;
        private FilmLoaded callback;

        LoadDataFilmTask(int idFilm, FilmLoaded callback) {
            this.idFilm = idFilm;
            this.callback = callback;
        }

        @Override
        protected CurrentMovieRequest doInBackground(Void... voids) {
            try {
                RequestMovie requestMovie = new RequestMovie();
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                String json = requestMovie.viewMovie(idFilm);
                JSONObject jsonObject = new JSONObject(json);
                return jsonParserRequest.currentMovieParce(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentMovieRequest currentMovieRequest) {
            super.onPostExecute(currentMovieRequest);
            callback.isLoaded(currentMovieRequest);
        }
    }

    static class DownloadImageTask extends AsyncTask<Void, Void, CurrentMovieRequest> {
        private String url;
        CurrentMovieRequest film;
        CurrentFilmFragment.DownloadImage callback;

        DownloadImageTask(CurrentMovieRequest film, CurrentFilmFragment.DownloadImage callback) {
            if (film.getBackdropPath().equals("null")) {
                this.url = URL_IMAGE_500PX + film.getPosterPath();
            } else {
                this.url = URL_IMAGE_500PX + film.getBackdropPath();
            }
            this.film = film;
            this.callback = callback;
        }

        @Override
        protected CurrentMovieRequest doInBackground(Void... voids) {
            Bitmap bitmap = Downloader.downloadImage(url);
            film.setBackdropBitmap(bitmap);
            return film;
        }

        @Override
        protected void onPostExecute(CurrentMovieRequest film) {
            callback.isDownloaded(film);
            super.onPostExecute(film);
        }
    }
}
