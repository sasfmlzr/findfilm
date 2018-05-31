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
    private int countLoadedPages = 1;
    private boolean isFirstList = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        countLoadedPages = 1;
        isFirstList = true;
        loadRecyclerFilmView();

        new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println();
        super.onViewCreated(view, savedInstanceState);
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

    public interface RecyclerElementEnded {
        void isEnded();
    }

    private FilmListComplete setFilmListListener() {
        DownloadImage downloadCallback = (film) -> {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).replaceImageViewFilm(film);
        };
        return (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            countLoadedPages++;
            for (DiscoverMovieRequest.ResultsField film : filmList) {
            //    new DownloadImageTask(film, downloadCallback)
            //            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
    }

    private void loadRecyclerFilmView() {
        listFilmView = view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList) {
        if (isFirstList) {
            DiscoverFilmFragment.RecyclerElementEnded callback = () -> {
                new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            };
            RecyclerView.Adapter adapter =
                    new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
            listFilmView.setAdapter(adapter);
            isFirstList = false;
        } else {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).addElements(filmList);
        }
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private int countLoadedPages;
        private FilmListComplete listener;

        RetrieveFeedTask(int countLoadedPages, FilmListComplete listener) {
            this.countLoadedPages = countLoadedPages;
            this.listener = listener;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            Request request = new Request();
            String json = "";
            JSONObject jsonObject;
            try {
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                json = request.discoverMovie(countLoadedPages);
                jsonObject = new JSONObject(json);
                DiscoverMovieRequest movieRequest = jsonParserRequest.discoverMovieParce(jsonObject);
                return movieRequest.getResultsFields();
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
