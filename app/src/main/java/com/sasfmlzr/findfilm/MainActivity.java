package com.sasfmlzr.findfilm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.sasfmlzr.findfilm.fragment.CurrentFilmFragment;
import com.sasfmlzr.findfilm.fragment.ParentFilmFragment;

public class MainActivity extends AppCompatActivity implements ParentFilmFragment.filmClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            createParentFragment();
        }
    }

    @Override
    public void isClicked(int idFilm) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, CurrentFilmFragment.newInstance(idFilm))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        // https://alexfu.github.io/android/2013/12/09/managing-fragment-states-manually.html
        // http://android.joao.jp/2013/09/back-stack-with-nested-fragments-back.html
        // http://d.hatena.ne.jp/yohpapa/20130317/1363509114
        super.onBackPressed();
    }

    private void createParentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.containerForFragment, new ParentFilmFragment())
                .commit();
    }
}
