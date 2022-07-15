package com.example.giphydemo.service

import com.example.giphydemo.model.GifResponse
import retrofit2.Response

class Repository {

    private val service by lazy {
        APIClient.getRetrofitService()
    }

    companion object {
        private val instance = Repository()

        fun getInstance(): Repository {
            return instance
        }
    }

    suspend fun getTrendingGifs(queryMap: Map<String, String>): Response<GifResponse> {
        return service.getTrendingGifs(queryMap)
    }
}