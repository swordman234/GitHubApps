package com.example.githubappsv3.Favorite

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(app: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getInstance(app)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>> = favoriteDao.getAllFavorite()

    fun insert(favorite:FavoriteEntity) {
        executorService.execute { favoriteDao.insert(favorite) }
    }

    suspend fun findById(id:Int) : FavoriteEntity? {
        return favoriteDao.getFavorite(id)
    }

    fun delete(favorite:FavoriteEntity) {
        executorService.execute { favoriteDao.delete(favorite) }
    }

}