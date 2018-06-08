package com.sasfmlzr.findfilm.fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sasfmlzr.findfilm.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_LANGUAGE = "language";

    /* @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         addPreferencesFromResource(R.xml.settings);
     }
 */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onStop() {
        Objects.requireNonNull(getActivity()).recreate();
        super.onStop();
    }
}
