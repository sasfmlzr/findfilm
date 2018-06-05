package com.sasfmlzr.findfilm;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sasfmlzr.findfilm.fragment.CurrentFilmFragment;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment.OnFilmSelectedListener;
import com.sasfmlzr.findfilm.fragment.SearchFilmFragment;

public class MainActivity extends AppCompatActivity implements OnFilmSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.containerForFragment, new DiscoverFilmFragment())
                    .commit();
        }
    }

    @Override
    public void filmClicked(int idFilm) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, CurrentFilmFragment.newInstance(idFilm))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void filmSearched(String query) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, SearchFilmFragment.newInstance(query))
                .addToBackStack(null)
                .commit();
    }
}
