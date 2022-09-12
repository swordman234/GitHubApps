package com.example.githubapps.Model

import com.google.gson.annotations.SerializedName

object Model {
    data class UserData (
        var login: String = "",
        var id: Int = 0,
        @SerializedName("avatar_url")
        var avatar: String = "",
        var name: String = "",
        var company: String? = "",
        var blog: String = "",
        var location: String? = "",
        var email: String = "",
        var bio: String = "",
        @SerializedName("twitter_username")
        var twitter: String = "",
        @SerializedName("public_repos")
        var repos: Int = 0,
        var followers: Int = 0,
        var following: Int = 0,
        var t: Throwable
    )
    data class ListUserResponse(
        var userList: List<UserData> = listOf(),
        var t: Throwable?
    )
    data class SearchResponse (
        @SerializedName("items")
        var userList: List<UserData> = listOf(),
        var t: Throwable
    )
    data class FollowResponse (
        var userList: List<UserData> = listOf(),
        var t: Throwable?
    )
}