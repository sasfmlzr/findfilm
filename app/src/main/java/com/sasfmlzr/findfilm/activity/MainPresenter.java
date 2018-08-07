package com.sasfmlzr.findfilm.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mainView;


    public MainPresenter(@NonNull MainContract.View mainView) {
        this.mainView = mainView;
        this.mainView.setPresenter(this);
    }

    @Override
    public void start() {
    }


    @Override
    public void loadChildFragment(@NonNull FragmentManager childFM) {
        DiscoverFilmFragment childFragment = new DiscoverFilmFragment();
        childFM.beginTransaction()
                .replace(R.id.container_child_fragment, childFragment)
                .commit();
    }
}
