package com.mazzampr.githubcompose.data.remote.retrofit

import com.google.gson.GsonBuilder
import com.mazzampr.githubcompose.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): GithubApi {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "token ${BuildConfig.TOKEN_API}")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder().also {
                it.addInterceptor(loggingInterceptor)
                it.addInterceptor(authInterceptor)
            }.build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
            return retrofit.create(GithubApi::class.java)
        }
    }
}