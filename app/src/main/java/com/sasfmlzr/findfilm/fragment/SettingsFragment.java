package com.sasfmlzr.findfilm.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sasfmlzr.findfilm.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_LANGUAGE = "language";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
