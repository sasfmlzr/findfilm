package com.sasfmlzr.findfilm.activity;

import android.support.v4.app.FragmentManager;

import com.sasfmlzr.findfilm.BasePresenter;
import com.sasfmlzr.findfilm.BaseView;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;

import java.util.List;

public interface MainContract {

    interface View extends BaseView<Presenter>{
        void showFilms(List<DiscoverMovieRequest> filmList);
    }

    interface Presenter extends BasePresenter {
        void loadChildFragment(FragmentManager childFM);
    }
}
