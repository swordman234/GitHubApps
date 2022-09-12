package com.example.githubappsv3.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubappsv3.Favorite.FavoriteEntity
import com.example.githubappsv3.Favorite.FavoriteRepository
import com.example.githubappsv3.GitHubService.GitHubService
import com.example.githubappsv3.Model.Model
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val gitHubService = GitHubService.create()
    private val userData = MutableLiveData<Model.UserData>()
    private val favoriteRepository : FavoriteRepository = FavoriteRepository(getApplication())
    private val isFavorite = MutableLiveData<Boolean>()

    fun fetchUser(user: String) {
        gitHubService.getDetails(user).enqueue(object : Callback<Model.UserData> {
            override fun onFailure(call: Call<Model.UserData>, t: Throwable) {
                val res = Model.UserData(t = t)
                userData.postValue(res)
            }

            override fun onResponse(
                call: Call<Model.UserData>,
                response: Response<Model.UserData>
            ) {
                userData.postValue(response.body())
            }
        })
    }

    fun getUser(): LiveData<Model.UserData> = userData

    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun checkFavorite(){
        viewModelScope.launch {
            if (favoriteRepository.findById(userData.value!!.id)==null) isFavorite.postValue(false)
            else isFavorite.postValue(true)
            }
        }

    fun addToFavorite(){
        viewModelScope.launch {
            if(favoriteRepository.findById(userData.value!!.id)==null) {
                favoriteRepository.insert(
                    FavoriteEntity(
                    userData.value!!.id,
                    userData.value!!.login,
                    userData.value!!.avatar
                ))
                isFavorite.postValue(true)
            }
            else{
                favoriteRepository.delete(FavoriteEntity(
                    userData.value!!.id,
                    userData.value!!.login,
                    userData.value!!.avatar
                ))
                isFavorite.postValue(false)
            }
        }
    }
}