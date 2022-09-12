package com.example.githubapps.GitHubService

import com.example.githubapps.Model.Model
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    companion object {
        private const val baseUrl = "https://api.github.com/"
        private const val token = "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"

        private class AuthInterceptor: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .header("Authorization",
                        token
                    )
                    .build()
                return chain.proceed(request)
            }
        }

        fun create(): GitHubService {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
            val retrofit = Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(GitHubService::class.java)
        }
    }

    @GET("users")
    fun getListUsers(): Call<List<Model.UserData>>

    @GET("search/users")
    fun searchUser(@Query("q") user: String): Call<Model.SearchResponse>

    @GET("users/{user}")
    fun getDetails(@Path("user") user: String): Call<Model.UserData>

    @GET("users/{user}/followers")
    fun getFollowers(@Path("user") user: String): Call<List<Model.UserData>>

    @GET("users/{user}/following")
    fun getFollowing(@Path("user") user: String): Call<List<Model.UserData>>
}