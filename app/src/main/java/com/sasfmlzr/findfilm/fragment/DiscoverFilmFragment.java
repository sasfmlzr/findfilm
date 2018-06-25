package com.sasfmlzr.findfilm.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.model.FindFilmSingleton;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;

public class DiscoverFilmFragment extends AbstractFilmFragment {
    private int countLoadedPages = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        savedState = null;
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        setHasOptionsMenu(true);
        countLoadedPages = 1;
        isFirstList = true;
        loadRecyclerFilmView();

        new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchView != null) {
            outState.putString("currentSearchQuery", searchView.getQuery().toString());
        }
    }

    public interface OnFilmSelectedListener {
        void filmClicked(int idFilm);

        void filmSearched(String query);
    }

    public interface FilmListComplete {
        void isCompleted(List<DiscoverMovieRequest.Result> filmList);
    }

    public interface DownloadImage {
        void isDownloaded(DiscoverMovieRequest.Result film);
    }

    private FilmListComplete setFilmListListener() {
        return (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            countLoadedPages++;
            for (DiscoverMovieRequest.Result film : filmList) {
                new DownloadImageTask(film, downloadCallback)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.Result> filmList) {
        if (isFirstList) {
            DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                    new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            RecyclerView.Adapter adapter =
                    new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
            listFilmView.setAdapter(adapter);
            isFirstList = false;
        } else {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).addElements(filmList);
        }
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.Result>> {
        private int countLoadedPages;
        private FilmListComplete listener;

        RetrieveFeedTask(int countLoadedPages, FilmListComplete listener) {
            this.countLoadedPages = countLoadedPages;
            this.listener = listener;
        }

        @Override
        protected List<DiscoverMovieRequest.Result> doInBackground(Void... voids) {
            FindFilmApi findFilmApi = FindFilmSingleton.getFindFilmApi();
            Response response;
            try {
                response = findFilmApi.getDiscoverMovie(API_KEY, LANGUAGE, countLoadedPages)
                        .execute();
                DiscoverMovieRequest discoverMovieRequest = (DiscoverMovieRequest) response.body();
                return discoverMovieRequest.getResults();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DiscoverMovieRequest.Result> Results) {
            listener.isCompleted(Results);
            super.onPostExecute(Results);
        }
    }
}
