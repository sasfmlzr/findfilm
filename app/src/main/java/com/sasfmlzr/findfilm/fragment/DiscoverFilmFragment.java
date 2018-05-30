package com.sasfmlzr.findfilm.fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.request.Request;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_92PX;

public class DiscoverFilmFragment extends android.support.v4.app.Fragment {
    private RecyclerView listFilmView;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        loadRecyclerFilmView();

        DownloadImage downloadCallback = (film) -> {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).replaceImageViewFilm(film);
        };
        FilmListComplete listener = (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            for (DiscoverMovieRequest.ResultsField film : filmList) {
                new DownloadImageTask(URL_IMAGE_92PX + film.getBackdrop_path(), film, downloadCallback).execute();
            }
        };

        new RetrieveFeedTask(listener).execute();
        return view;
    }

    public interface FilmListComplete {
        void isCompleted(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    public interface DownloadImage {
        void isDownloaded(DiscoverMovieRequest.ResultsField film);
    }

    private void loadRecyclerFilmView() {
        listFilmView = view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList) {
        RecyclerView.Adapter adapter = new DiscoverRecyclerAdapter(filmList);
        listFilmView.setAdapter(adapter);
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private FilmListComplete listener;

        RetrieveFeedTask(FilmListComplete listener) {
            this.listener = listener;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            Request request = new Request();
            String json = "";
            JSONObject jsonObject;
            try {
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                json = request.discoverMovie();
                jsonObject = new JSONObject(json);
                DiscoverMovieRequest request1 = jsonParserRequest.discoverMovieParce(jsonObject);
                json = request.viewMovie(383498);
                jsonObject = new JSONObject(json);
                CurrentMovieRequest currentMovieRequest = jsonParserRequest.currentMovieParce(jsonObject);
                System.out.println();
                return request1.getResultsFields();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(json);
            return null;
        }

        @Override
        protected void onPostExecute(List<DiscoverMovieRequest.ResultsField> resultsFields) {
            listener.isCompleted(resultsFields);
            super.onPostExecute(resultsFields);
        }
    }

    static class DownloadImageTask extends AsyncTask<Void, Void, DiscoverMovieRequest.ResultsField> {
        private String url;
        DiscoverMovieRequest.ResultsField film;
        DownloadImage callback;

        DownloadImageTask(String url, DiscoverMovieRequest.ResultsField film, DownloadImage callback) {
            this.url = url;
            this.film = film;
            this.callback = callback;
        }

        @Override
        protected DiscoverMovieRequest.ResultsField doInBackground(Void... voids) {
            Bitmap bitmap = Downloader.downloadImage(url);
            film.setBackdropBitmap(bitmap);
            return film;
        }

        @Override
        protected void onPostExecute(DiscoverMovieRequest.ResultsField film) {
            callback.isDownloaded(film);
            super.onPostExecute(film);
        }
    }
}
