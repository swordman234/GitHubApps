package com.example.githubappsv3.Themes.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubappsv3.Themes.ThemesPreferences
import com.example.githubappsv3.ViewModel.ThemesViewModel

class ThemesViewModelFactory(private val pref: ThemesPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemesViewModel::class.java)) {
            return ThemesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}