package com.example.githubappsv3.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubappsv3.R
import com.example.githubappsv3.Themes.ThemesPreferences
import com.example.githubappsv3.Themes.Factory.ThemesViewModelFactory
import com.example.githubappsv3.ViewModel.ThemesViewModel
import com.example.githubappsv3.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.cvLanguage.setOnClickListener(this)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = ThemesPreferences.getInstance(dataStore)
        val themesViewModel = ViewModelProvider(this, ThemesViewModelFactory(pref)).get(
            ThemesViewModel::class.java
        )
        themesViewModel.getThemeSettings().observe(this,)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themesViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.cvLanguage.id -> {
                val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(i)
            }
        }
    }
}