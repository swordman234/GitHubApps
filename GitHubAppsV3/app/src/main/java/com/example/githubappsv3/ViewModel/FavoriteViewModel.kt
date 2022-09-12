package com.example.githubappsv3.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubappsv3.Favorite.FavoriteEntity
import com.example.githubappsv3.Favorite.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllDataFavorites(): LiveData<List<FavoriteEntity>> = favoriteRepository.getAllFavorite()

}