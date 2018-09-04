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
    private String query;
    private filmClickedListener searchedListener;
    private Fragment.SavedState myFragmentState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString("query");
            myFragmentState = getArguments().getParcelable("fragment");
        }
        if (myFragmentState != null) {
            setInitialSavedState(myFragmentState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_fragment, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (myFragmentState == null) {
            replaceChildFragment();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void filmClicked(int idFilm) {
        searchedListener.isClicked(idFilm);
    }

    @Override
    public void filmSearched(String query) {
        this.query = query;
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
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        if (query != null && !query.isEmpty()) {
            searchView.setQuery(query, false);
            searchView.clearFocus();
        }
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

    @Override
    public void onDestroyView() {
        assert getFragmentManager() != null;
        myFragmentState = getFragmentManager().saveFragmentInstanceState(this);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("query", query);
        outState.putParcelable("fragment", myFragmentState);
        super.onSaveInstanceState(outState);
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
