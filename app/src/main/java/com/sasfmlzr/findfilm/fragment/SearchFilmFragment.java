package com.sasfmlzr.findfilm.fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public class SearchFilmFragment extends AbstractFilmFragment {
    private String querySearch;

    public static SearchFilmFragment newInstance(String querySearch) {
        Bundle args = new Bundle();
        args.putString("querySearch", querySearch);
        SearchFilmFragment fragment = new SearchFilmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            querySearch = getArguments().getString("querySearch");
            Log.d("asdas", "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
            currentSearchQuery = querySearch;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedState != null) {
            currentSearchQuery = savedState.getString("currentSearchQuery");
        }
        savedState = null;
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        isFirstList = true;
        //setHasOptionsMenu(true);
        loadRecyclerFilmView();

        SearchCallback callback = filmList -> {
            setAdapterDiscoverFilm(filmList);
            for (DiscoverMovieRequest.ResultsField film : filmList) {
                new DownloadImageTask(film, downloadCallback)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        new SearchFilmTask(querySearch, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList) {
        DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                Log.d("ListEnded", "setAdapterDiscoverFilm()");
        RecyclerView.Adapter adapter =
                new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
        listFilmView.setAdapter(adapter);
    }
}
