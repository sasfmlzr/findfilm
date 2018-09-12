package com.sasfmlzr.findfilm.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.shape.MaterialShapeDrawable;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.design.shape.ShapePathModel;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sasfmlzr.findfilm.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParentFilmFragment extends Fragment implements DiscoverFilmFragment.OnFilmSelectedListener {
    private String query;
    private filmClickedListener searchedListener;
    private Fragment.SavedState myFragmentState;
    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.button_now)
    MaterialButton buttonNow;
    @BindView(R.id.button_soon)
    MaterialButton buttonSoon;

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
        unbinder = ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            toolbar.setTitle("TMDB");
            activity.setSupportActionBar(toolbar);
        }
        configureBottomNavigation();
        configureTopButtons();

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

        View viewSearch = menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(
                getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) viewSearch;
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
        unbinder.unbind();
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

    private void configureBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_child:
                            Toast.makeText(getContext(),
                                    "Child pushed", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_favorites:
                            Toast.makeText(getContext(),
                                    "Favorites pushed", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_invalid:
                            Toast.makeText(getContext(),
                                    "Invalid pushed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                });
    }

    private void configureTopButtons() {
        buttonNow.setBackground(configureLeftButton(255));

        buttonSoon.setBackground(configureRightButton(128));
        buttonSoon.setTextColor(getResources().getColor(R.color.colorWhite));

        buttonNow.setOnClickListener(item -> {
            Toast.makeText(getContext(), "Now clicked", Toast.LENGTH_SHORT).show();
            buttonNow.setBackground(configureLeftButton(255));
            buttonSoon.setBackground(configureRightButton(128));
            buttonNow.setTextColor(getResources().getColor(R.color.colorBlack));
            buttonSoon.setTextColor(getResources().getColor(R.color.colorWhite));
        });

        buttonSoon.setOnClickListener(item -> {
            Toast.makeText(getContext(), "Soon clicked", Toast.LENGTH_SHORT).show();
            buttonNow.setBackground(configureLeftButton(128));
            buttonSoon.setBackground(configureRightButton(255));
            buttonSoon.setTextColor(getResources().getColor(R.color.colorBlack));
            buttonNow.setTextColor(getResources().getColor(R.color.colorWhite));
        });
    }

    private MaterialShapeDrawable configureLeftButton(@IntRange(from = 0L, to = 255L) int opacity) {
        ShapePathModel leftShapePathModel = new ShapePathModel();
        leftShapePathModel.setBottomLeftCorner(new RoundedCornerTreatment(40));
        leftShapePathModel.setTopLeftCorner(new RoundedCornerTreatment(40));
        MaterialShapeDrawable leftRoundedMaterialShape = new MaterialShapeDrawable(leftShapePathModel);
        leftRoundedMaterialShape.setTint(getResources().getColor(R.color.colorPrimary));
        leftRoundedMaterialShape.setAlpha(opacity);
        return leftRoundedMaterialShape;
    }

    private MaterialShapeDrawable configureRightButton(@IntRange(from = 0L, to = 255L) int opacity) {
        ShapePathModel rightShapePathModel = new ShapePathModel();
        rightShapePathModel.setBottomRightCorner(new RoundedCornerTreatment(40));
        rightShapePathModel.setTopRightCorner(new RoundedCornerTreatment(40));
        MaterialShapeDrawable rightRoundedMaterialShape = new MaterialShapeDrawable(rightShapePathModel);
        rightRoundedMaterialShape.setTint(getResources().getColor(R.color.colorPrimary));
        rightRoundedMaterialShape.setAlpha(opacity);
        return rightRoundedMaterialShape;
    }
}
