package com.mazzampr.githubcompose.data.remote.retrofit

import com.mazzampr.githubcompose.data.remote.response.GithubResponse
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users")
    suspend fun getAllUsers(
    ): List<UserResponse>

    @GET("search/users")
    suspend fun getSearchUsers(
        @Query("q") query: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): UserResponse
}