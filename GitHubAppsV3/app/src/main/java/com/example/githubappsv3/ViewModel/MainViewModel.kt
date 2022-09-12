package com.example.githubappsv3.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubappsv3.GitHubService.GitHubService
import com.example.githubappsv3.Model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private val gitHubService =
        GitHubService.create()
    private val searchResponse = MutableLiveData<Model.SearchResponse>()
    private val listUserResponse = MutableLiveData<Model.ListUserResponse?>()


    fun fetchingUser(){
        gitHubService.getListUsers().enqueue(object : Callback<List<Model.UserData>> {
            override fun onFailure(call: Call<List<Model.UserData>>, t: Throwable) {
                val res = Model.ListUserResponse(t = t)
                listUserResponse.postValue(res)
            }
            override fun onResponse(
                call: Call<List<Model.UserData>>,
                response: Response<List<Model.UserData>>
            ) {
                val res = response.body()?.let{
                    Model.ListUserResponse(
                        it,
                        null
                    )
                }
                listUserResponse.postValue(res)
            }

        })
    }

    fun searchUser(query: String) {
        gitHubService.searchUser(query).enqueue(object: Callback<Model.SearchResponse> {
            override fun onFailure(call: Call<Model.SearchResponse>, t: Throwable) {
                val res = Model.SearchResponse(t = t)
                searchResponse.postValue(res)
            }

            override fun onResponse(
                call: Call<Model.SearchResponse>,
                response: Response<Model.SearchResponse>
            ) {
                searchResponse.postValue(response.body())
            }
        })
    }

    fun getResponse(): LiveData<Model.SearchResponse> = searchResponse
    fun getListUser(): MutableLiveData<Model.ListUserResponse?> = listUserResponse

}