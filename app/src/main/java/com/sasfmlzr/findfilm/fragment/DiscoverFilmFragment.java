package com.sasfmlzr.findfilm.fragment;
import android.content.Context;
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
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.Request;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_92PX;

public class DiscoverFilmFragment extends android.support.v4.app.Fragment {

    private DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    private RecyclerView listFilmView;
    private View view;
    private int countLoadedPages=1;

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
                new DownloadImageTask(film, downloadCallback).execute();
            }
        };

        new RetrieveFeedTask(listener).execute();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            filmSelectedListener = (OnFilmSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " OnFilmSelectedListener not attached");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filmSelectedListener = null;
    }

    public interface OnFilmSelectedListener {
        void filmClicked(int idFilm);
    }

    public interface FilmListComplete {
        void isCompleted(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    public interface DownloadImage {
        void isDownloaded(DiscoverMovieRequest.ResultsField film);
    }

    public interface RecyclerElementEnded{
        void isEnded();
    }

    private void loadRecyclerFilmView() {
        listFilmView = view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList) {
        DiscoverFilmFragment.RecyclerElementEnded callback = () -> {
            
        };
        RecyclerView.Adapter adapter =
                new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
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
                DiscoverMovieRequest movieiRequest = jsonParserRequest.discoverMovieParce(jsonObject);
                return movieiRequest.getResultsFields();
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
        private DiscoverMovieRequest.ResultsField film;
        private DownloadImage callback;
        private String url;

        DownloadImageTask(DiscoverMovieRequest.ResultsField film, DownloadImage callback) {
            this.url = URL_IMAGE_92PX + film.getBackdrop_path();
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
