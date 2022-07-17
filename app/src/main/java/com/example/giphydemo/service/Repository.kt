package com.example.giphydemo.service

import android.content.Context
import com.example.giphydemo.model.GifResponse
import com.example.giphydemo.model.ResultResponse
import com.example.giphydemo.model.database.GifDatabase
import com.example.giphydemo.model.database.entity.FavoriteGifs

class Repository(context: Context) : BaseRepository() {
    private val service by lazy { APIClient.getRetrofitService() }
    private val gifDatabase by lazy { GifDatabase.getDatabase(context) }

    suspend fun getTrendingGifs(queryMap: Map<String, String>): ResultResponse<GifResponse> =
        safeApiCall { service.getTrendingGifs(queryMap) }

    suspend fun searchGifs(queryMap: Map<String, String>): ResultResponse<GifResponse> =
        safeApiCall { service.searchGifs(queryMap) }

    fun insertGifData(favoriteGifs: FavoriteGifs) =
        gifDatabase.gifDao().insertGifData(favoriteGifs)

    fun retrieveAllFavorites(): List<FavoriteGifs> =
        gifDatabase.gifDao().selectAllFavorites()

    fun removeFavoriteGif(id: String) = gifDatabase.gifDao().removeGifData(id)
}