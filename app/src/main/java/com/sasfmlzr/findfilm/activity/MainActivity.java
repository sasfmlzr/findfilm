package com.sasfmlzr.findfilm.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.fragment.CurrentFilmFragment;
import com.sasfmlzr.findfilm.fragment.SettingsFragment;
import com.sasfmlzr.findfilm.model.SystemSettings;

public class MainActivity extends AppCompatActivity implements ParentFilmFragment.filmClickedListener {
    private boolean startServiceOnDestroy;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startServiceOnDestroy = true;
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener prefListener = (prefs, key) -> {
            if (key.equals(SettingsFragment.KEY_LANGUAGE)) {
                startServiceOnDestroy = false;
                recreate();
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        SystemSettings.LANGUAGE = sharedPref.getString(SettingsFragment.KEY_LANGUAGE, "en_US");

        ParentFilmFragment parentFragment =
                (ParentFilmFragment) getSupportFragmentManager().findFragmentById(R.id.containerForFragment);
        if (parentFragment == null) {
            parentFragment = ParentFilmFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerForFragment, parentFragment)
                    .commit();
        }

        mainPresenter = new MainPresenter(parentFragment);


        //startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  if (startServiceOnDestroy) {
        //      startService(new Intent(this, NotificationService.class));
        //  }
    }

    @Override
    public void isClicked(int idFilm) {
        getSupportFragmentManager().beginTransaction()
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

    private void setSettings() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForFragment, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
