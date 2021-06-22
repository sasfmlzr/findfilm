package com.sasfmlzr.findfilm.fragment.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.sasfmlzr.findfilm.MainActivity
import com.sasfmlzr.findfilm.R

class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(MainActivity.prefListener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(MainActivity.prefListener)
    }

    companion object {
        const val KEY_LANGUAGE = "language"
    }
}
