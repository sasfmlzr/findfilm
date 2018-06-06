package com.sasfmlzr.findfilm.fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;

import java.util.Objects;

public class ParentFilmFragment extends Fragment implements DiscoverFilmFragment.OnFilmSelectedListener {
    filmClickedListener searchedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_fragment, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        replaceChildFragment();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void filmClicked(int idFilm) {
        searchedListener.isClicked(idFilm);
    }

    @Override
    public void filmSearched(String query) {
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_child_fragment, SearchFilmFragment.newInstance(query))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(
                getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchedListener = (filmClickedListener) getActivity();
    }

    @Override
    public void onDetach() {
        searchedListener = null;
        super.onDetach();
    }

    public interface filmClickedListener {
        void isClicked(int idFilm);
    }

    public void replaceChildFragment() {
        DiscoverFilmFragment childFragment = new DiscoverFilmFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container_child_fragment, childFragment)
                .commit();
    }
}
