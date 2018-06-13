package com.sasfmlzr.findfilm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sasfmlzr.findfilm.fragment.CurrentFilmFragment;
import com.sasfmlzr.findfilm.fragment.ParentFilmFragment;
import com.sasfmlzr.findfilm.fragment.SettingsFragment;
import com.sasfmlzr.findfilm.model.SystemSettings;
import com.sasfmlzr.findfilm.service.NotificationService;

public class MainActivity extends AppCompatActivity implements ParentFilmFragment.filmClickedListener {
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private boolean startServiceOnDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startServiceOnDestroy = true;
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        prefListener = (prefs, key) -> {
            if (key.equals(SettingsFragment.KEY_LANGUAGE)) {
                startServiceOnDestroy = false;
                recreate();
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        SystemSettings.LANGUAGE = sharedPref.getString(SettingsFragment.KEY_LANGUAGE, "en_US");
        if (savedInstanceState == null) {
            createParentFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (startServiceOnDestroy) {
            startService(new Intent(this, NotificationService.class));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                setSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // https://alexfu.github.io/android/2013/12/09/managing-fragment-states-manually.html
        // http://android.joao.jp/2013/09/back-stack-with-nested-fragments-back.html
        // http://d.hatena.ne.jp/yohpapa/20130317/1363509114
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    private void createParentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.containerForFragment, new ParentFilmFragment())
                .commit();
    }

    private void setSettings() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.containerForFragment, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
