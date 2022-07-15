package com.example.giphydemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.giphydemo.BuildConfig
import com.example.giphydemo.model.GifData
import com.example.giphydemo.model.GifResponse
import com.example.giphydemo.model.database.entity.FavoriteGifs
import com.example.giphydemo.service.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTrendingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy { Repository(application) }

    private val _gifsResponse = MutableLiveData<GifResponse>()
    val gifsResponse: LiveData<GifResponse> = _gifsResponse

    private val _insertComplete = MutableLiveData<Pair<Boolean, String>>()
    val insertComplete: LiveData<Pair<Boolean, String>> = _insertComplete

    private val _removeComplete = MutableLiveData<Pair<Boolean, String>>()
    val removeComplete: LiveData<Pair<Boolean, String>> = _removeComplete

    fun insertFavoriteGif(gifData: GifData) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataToBeInserted =
                FavoriteGifs(gifData.id, gifData.images.downsizedMedium?.url ?: "", gifData.title)
            repository.insertGifData(dataToBeInserted).also {
                _insertComplete.postValue(true to gifData.id)
            }
        }
    }

    fun removeFavoriteGif(gifData: GifData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavoriteGif(gifData.id).also {
                _removeComplete.postValue(true to gifData.id)
            }
        }
    }

    fun getTrendingGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = QueryFactory.getTrendingGifsQuery(
                apiKey = BuildConfig.API_KEY,
                limit = 25,
                rating = "g"
            )
            repository.getTrendingGifs(queryMap).apply {
                if (isSuccessful) {
                    _gifsResponse.postValue(body())
                }
            }
        }
    }

    fun searchGifs(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = QueryFactory.getSearchQueryParams(
                apiKey = BuildConfig.API_KEY,
                limit = 25,
                query = query,
                rating = "g",
                offset = 0,
                lang = "en"
            )
            repository.searchGifs(queryMap).apply {
                if (isSuccessful) {
                    _gifsResponse.postValue(body())
                }
            }
        }
    }
}