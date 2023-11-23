package com.mazzampr.githubcompose.di

import android.content.Context
import com.mazzampr.githubcompose.data.UserRepository
import com.mazzampr.githubcompose.data.local.room.UsersDatabase
import com.mazzampr.githubcompose.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        return UserRepository.getInstance(apiService, dao)
    }
}