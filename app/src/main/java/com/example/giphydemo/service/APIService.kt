package com.example.giphydemo.service

import com.example.giphydemo.model.GifResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIService {

    @GET("gifs/trending")
    suspend fun getTrendingGifs(@QueryMap queryMap: Map<String, String>): Response<GifResponse>

    @GET("gifs/search")
    suspend fun searchGifs(@QueryMap queryMap: Map<String, String>): Response<GifResponse>
}