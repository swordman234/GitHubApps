package com.example.githubapps.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubapps.GitHubService.GitHubService
import com.example.githubapps.Model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val gitHubService = GitHubService.create()
    private val userData = MutableLiveData<Model.UserData>()

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


}