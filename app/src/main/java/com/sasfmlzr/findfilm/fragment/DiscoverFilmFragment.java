package com.sasfmlzr.findfilm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.adapter.VerticalItemDecoration;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
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
        unbinder = ButterKnife.bind(this, view);
        loadRecyclerFilmView();

        runRequestFilm(filmListListener());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
        unbinder.unbind();
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

    private FilmListComplete filmListListener() {
        return (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            countLoadedPages++;
        };
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.Result> filmList) {
        if (isFirstList) {
            DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                    runRequestFilm(filmListListener());
            RecyclerView.Adapter adapter =
                    new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
            listFilmView.setAdapter(adapter);
            isFirstList = false;
        } else {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).addElements(filmList);
        }
    }

    private void runRequestFilm(FilmListComplete callback) {
        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getDiscoverMovie(API_KEY, LANGUAGE, countLoadedPages)
                .enqueue(new Callback<DiscoverMovieRequest>() {
                    @Override
                    public void onResponse(Call<DiscoverMovieRequest> call, Response<DiscoverMovieRequest> response) {
                        callback.isCompleted(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<DiscoverMovieRequest> call, Throwable t) {

                    }
                });
    }
}
