package com.sasfmlzr.findfilm.fragment;

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

import butterknife.ButterKnife;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedState = null;
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        isFirstList = true;
        unbinder = ButterKnife.bind(this, view);
        loadRecyclerFilmView();

        SearchCallback callback = this::setAdapterDiscoverFilm;
        runSearchRequestFilm(querySearch, callback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.Result> filmList) {
        DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                Log.d("ListEnded", "setAdapterDiscoverFilm()");
        RecyclerView.Adapter adapter =
                new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
        listFilmView.setAdapter(adapter);
    }
}
