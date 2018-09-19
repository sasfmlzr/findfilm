package com.sasfmlzr.findfilm.fragment.discoverfilm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.android.databinding.library.baseAdapters.BR;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;

public class DiscoverFilmViewModel extends BaseObservable {
    @Bindable
    private RecyclerView.Adapter recyclerViewAdapter;
    private DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    private String querySearch;
    private int countLoadedPages = 1;
    private boolean isFirstList = true;

    DiscoverFilmViewModel(DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener) {
        this.filmSelectedListener = filmSelectedListener;
    }

    public void runRequest(String querySearch) {
        isFirstList = true;
        this.querySearch = querySearch;

        if (querySearch == null) {
            runRequestFilm(1, filmListListener());
        } else {
            DiscoverFilmFragment.SearchCallback callback = this::setAdapterDiscoverFilm;
            runSearchRequestFilm(querySearch, callback);
        }
    }

    public void runSearchRequestFilm(String query, DiscoverFilmFragment.SearchCallback callback) {
        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getSearchMovie(API_KEY, LANGUAGE, query, 1)
                .enqueue(new Callback<DiscoverMovieRequest>() {
                    @Override
                    public void onResponse(Call<DiscoverMovieRequest> call, Response<DiscoverMovieRequest> response) {
                        DiscoverMovieRequest discoverMovieRequest = response.body();
                        callback.isFind(discoverMovieRequest.getResults());
                    }

                    @Override
                    public void onFailure(Call<DiscoverMovieRequest> call, Throwable t) {
                    }
                });
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    private void setRecyclerViewAdapter(RecyclerView.Adapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
        notifyPropertyChanged(BR.recyclerViewAdapter);
    }

    private void runRequestFilm(int countLoadedPages, DiscoverFilmFragment.FilmListComplete callback) {
        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getDiscoverMovie(API_KEY, LANGUAGE, countLoadedPages)
                .enqueue(new Callback<DiscoverMovieRequest>() {
                    @Override
                    public void onResponse(Call<DiscoverMovieRequest> call,
                                           Response<DiscoverMovieRequest> response) {
                        callback.isCompleted(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<DiscoverMovieRequest> call, Throwable t) {

                    }
                });
    }

    private DiscoverFilmFragment.FilmListComplete filmListListener() {
        return (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            countLoadedPages++;
        };
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.Result> filmList) {
        if (isFirstList) {
            DiscoverFilmFragment.RecyclerElementEnded callback = () -> {
                if (querySearch == null) {
                    runRequestFilm(countLoadedPages, filmListListener());
                }
            };
            RecyclerView.Adapter adapter =
                    new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);

            setRecyclerViewAdapter(adapter);
            isFirstList = false;
        } else {
            ((DiscoverRecyclerAdapter) Objects.requireNonNull(
                    getRecyclerViewAdapter()))
                    .addElements(filmList);
        }
    }
}
