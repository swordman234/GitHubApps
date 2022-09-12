package com.example.githubapps.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapps.GitHubService.GitHubService
import com.example.githubapps.Model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val gitHubService =
        GitHubService.create()
    private val followingResponse = MutableLiveData<Model.FollowResponse>()

    fun fetchFollowing(user: String) {
        gitHubService.getFollowing(user).enqueue(object : Callback<List<Model.UserData>> {
            override fun onFailure(call: Call<List<Model.UserData>>, t: Throwable) {
                val res = Model.FollowResponse(t = t)
                followingResponse.postValue(res)
            }

            override fun onResponse(
                call: Call<List<Model.UserData>>,
                response: Response<List<Model.UserData>>
            ) {
                val res = response.body()?.let {
                    Model.FollowResponse(
                        it,
                        null
                    )
                }
                followingResponse.postValue(res)
            }
        })
    }

    fun getFollowing(): LiveData<Model.FollowResponse> = followingResponse
}