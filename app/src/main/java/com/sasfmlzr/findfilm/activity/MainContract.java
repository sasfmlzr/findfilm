package com.sasfmlzr.findfilm.activity;

import com.sasfmlzr.findfilm.BasePresenter;
import com.sasfmlzr.findfilm.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter>{
        void showFilms();
    }

    interface Presenter extends BasePresenter {
        void loadChildFragment();
    }
}
