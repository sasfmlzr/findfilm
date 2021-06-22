package com.sasfmlzr.findfilm

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sasfmlzr.findfilm.fragment.currentfilm.CurrentFilmFragment
import com.sasfmlzr.findfilm.fragment.parentfilm.ParentFilmFragment
import com.sasfmlzr.findfilm.fragment.parentfilm.ParentFilmFragment.filmClickedListener
import com.sasfmlzr.findfilm.fragment.settings.SettingsFragment
import com.sasfmlzr.findfilm.model.SystemSettings
import com.sasfmlzr.findfilm.service.NotificationService

class MainActivity : AppCompatActivity(), filmClickedListener {
    private var startServiceOnDestroy = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startServiceOnDestroy = true
        setContentView(R.layout.activity_main)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        prefListener = OnSharedPreferenceChangeListener { prefs: SharedPreferences?, key: String ->
            if (key == SettingsFragment.KEY_LANGUAGE) {
                startServiceOnDestroy = false
                recreate()
            }
        }
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener)
        SystemSettings.LANGUAGE =
            sharedPref.getString(SettingsFragment.KEY_LANGUAGE, "en_US")
        if (savedInstanceState == null) {
            createParentFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (startServiceOnDestroy) {
            startService(Intent(this, NotificationService::class.java))
        }
    }

    override fun isClicked(idFilm: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerForFragment, CurrentFilmFragment.newInstance(idFilm))
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                setSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        for (frag in fm.fragments) {
            if (frag.isVisible) {
                val childFm = frag.childFragmentManager
                if (childFm.backStackEntryCount > 0) {
                    childFm.popBackStack()
                    return
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    private fun createParentFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.containerForFragment, ParentFilmFragment())
            .commit()
    }

    private fun setSettings() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerForFragment, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        var prefListener: OnSharedPreferenceChangeListener? = null
    }
}