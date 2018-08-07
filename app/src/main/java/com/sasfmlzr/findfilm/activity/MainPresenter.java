package com.sasfmlzr.findfilm.activity;

import android.support.annotation.NonNull;

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
    public void loadChildFragment() {
    mainView.showFilms();
    }

}
