package com.mazzampr.githubcompose.data.remote.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @field:SerializedName("items")
    val items: List<UserResponse>
)

data class UserResponse(

    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,
)