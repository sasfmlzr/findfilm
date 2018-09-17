package com.sasfmlzr.findfilm.fragment.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sasfmlzr.findfilm.MainActivity;
import com.sasfmlzr.findfilm.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_LANGUAGE = "language";

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
               .registerOnSharedPreferenceChangeListener(MainActivity.prefListener);
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(MainActivity.prefListener);
    }
}