package com.example.giphydemo.service

import com.example.giphydemo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {
    private var okHttpClient: OkHttpClient

    init {
        okHttpClient = getOkHttpClient().build()
    }

    private fun getOkHttpClient(): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(getLoggingInterceptor())
                connectTimeout(BuildConfig.TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            }
        return okHttpClientBuilder
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    fun getRetrofitService(): APIService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build().create(APIService::class.java)
    }
}