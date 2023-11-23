package com.mazzampr.githubcompose.data

import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import com.mazzampr.githubcompose.data.local.room.UsersDao
import com.mazzampr.githubcompose.data.remote.response.UserResponse
import com.mazzampr.githubcompose.data.remote.retrofit.GithubApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserRepository(
    private val api: GithubApi,
    private val dao: UsersDao
) {

    suspend fun getAllUsers(): Flow<List<UserResponse>> {
        val response = api.getAllUsers()
        return flowOf(response)
    }

    suspend fun getSearchUsers(query: String): Flow<List<UserResponse>> {
        val response = api.getSearchUsers(query)
        val items = response.items
        return flowOf(items)
    }

    suspend fun getDetailUser(username: String): Flow<UserResponse> {
        val response = api.getDetailUser(username)
        return flowOf(response)
    }

    fun getLikedUsers(): Flow<List<UsersEntity>> {
        return dao.getLikedUsers()
    }

    suspend fun insertFavorite(user: UsersEntity) {
        dao.upsert(user)
    }

    suspend fun deleteFavorite(user: UsersEntity) {
        dao.delete(user)
    }

    fun getUserFavDetail(username: String): Flow<List<UsersEntity>> {
        return dao.getUserDetail(username)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: GithubApi,
            usersDao: UsersDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                UserRepository(apiService, usersDao).apply {
                    instance = this
                }
            }
    }
}