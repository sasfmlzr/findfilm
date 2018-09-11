package com.sasfmlzr.findfilm.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sasfmlzr.findfilm.MainActivity;
import com.sasfmlzr.findfilm.R;

/**
 * shows the settings option for choosing the movie categories in ListPreference.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_LANGUAGE = "language";
    private static final String TAG = SettingsFragment.class.getSimpleName();

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        //add xml
        setPreferencesFromResource(R.xml.settings, rootKey);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

       // onSharedPreferenceChanged(sharedPreferences, getString(R.string.movies_categories_key));
    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
               .registerOnSharedPreferenceChangeListener(MainActivity.prefListener);
    }


    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(MainActivity.prefListener);
    }
}