package com.sasfmlzr.findfilm.fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.Request;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_154PX;

public abstract class AbstractFilmFragment extends android.support.v4.app.Fragment {
    public DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;

    public boolean isFirstList = true;
    public RecyclerView listFilmView;
    public View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            filmSelectedListener = (DiscoverFilmFragment.OnFilmSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " OnFilmSelectedListener not attached");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filmSelectedListener = null;
    }

    public interface RecyclerElementEnded {
        void isEnded();
    }

    public interface SearchCallback {
        void isFind(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    public DiscoverFilmFragment.DownloadImage downloadCallback = (film) -> {
        DiscoverRecyclerAdapter adapter = (DiscoverRecyclerAdapter) listFilmView.getAdapter();
        if (adapter != null) {
            adapter.replaceImageViewFilm(film);
        }
    };

    public void loadRecyclerFilmView() {
        listFilmView = view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    static class SearchFilmTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private String query;
        private SearchCallback callback;

        SearchFilmTask(String query, SearchCallback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            Request request = new Request();
            try {
                JSONObject jsonObject = new JSONObject(request.searchMovie(query));
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                DiscoverMovieRequest result = jsonParserRequest.searchMovieParce(jsonObject);
                return result.getResultsFields();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DiscoverMovieRequest.ResultsField> resultsFields) {
            callback.isFind(resultsFields);
            super.onPostExecute(resultsFields);
        }
    }

    static class DownloadImageTask extends AsyncTask<Void, Void, DiscoverMovieRequest.ResultsField> {
        private DiscoverMovieRequest.ResultsField film;
        private DiscoverFilmFragment.DownloadImage callback;
        private String url;

        DownloadImageTask(DiscoverMovieRequest.ResultsField film, DiscoverFilmFragment.DownloadImage callback) {
            if (film.getBackdrop_path().equals("null")) {
                this.url = URL_IMAGE_154PX + film.getPoster_path();
            } else {
                this.url = URL_IMAGE_154PX + film.getBackdrop_path();
            }
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
