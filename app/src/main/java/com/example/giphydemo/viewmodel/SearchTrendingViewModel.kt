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

    companion object {
        private const val LIMIT = 25
        private const val RATING = "g"
        private const val OFFSET = 0
        private const val LANG = "en"
    }

    private val repository by lazy { Repository(application) }

    private val _gifsResponse = MutableLiveData<GifResponse>()
    val gifsResponse: LiveData<GifResponse> = _gifsResponse

    private val _insertComplete = MutableLiveData<Pair<Boolean, String>>()
    val insertComplete: LiveData<Pair<Boolean, String>> = _insertComplete

    private val _removeComplete = MutableLiveData<Pair<Boolean, String>>()
    val removeComplete: LiveData<Pair<Boolean, String>> = _removeComplete

    private val _allFavoriteGifs = MutableLiveData<ArrayList<FavoriteGifs>>()
    val allFavoriteGifs: LiveData<ArrayList<FavoriteGifs>> = _allFavoriteGifs

    fun insertFavoriteGif(gifData: GifData) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataToBeInserted =
                FavoriteGifs(gifData.id, gifData.images.downsizedMedium?.url ?: "", gifData.title)
            repository.insertGifData(dataToBeInserted).also {
                _insertComplete.postValue(true to gifData.id)
            }
        }
    }

    fun removeFavoriteGif(gifId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavoriteGif(gifId).also {
                _removeComplete.postValue(true to gifId)
            }
        }
    }

    fun fetchAllFavoriteGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.retrieveAllFavorites().also {
                _allFavoriteGifs.postValue(it as ArrayList)
            }
        }
    }

    fun getTrendingGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = QueryFactory.getTrendingGifsQuery(
                apiKey = BuildConfig.API_KEY,
                limit = LIMIT,
                rating = RATING
            )
            repository.getTrendingGifs(queryMap).apply {
                if (isSuccessful) {
                    val favoriteGifs = repository.retrieveAllFavorites()
                    (body() as GifResponse).data.forEach {
                        favoriteGifs.find { favGif -> favGif.id == it.id }?.let { _ ->
                            it.isFavorite = true
                        }
                    }

                    _gifsResponse.postValue(body())
                }
            }
        }
    }

    fun searchGifs(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = QueryFactory.getSearchQueryParams(
                apiKey = BuildConfig.API_KEY,
                limit = LIMIT,
                query = query,
                rating = RATING,
                offset = OFFSET,
                lang = LANG
            )
            repository.searchGifs(queryMap).apply {
                if (isSuccessful) {
                    val favoriteGifs = repository.retrieveAllFavorites()
                    (body() as GifResponse).data.forEach {
                        favoriteGifs.find { favGif -> favGif.id == it.id }?.let { _ ->
                            it.isFavorite = true
                        }
                    }
                    _gifsResponse.postValue(body())
                }
            }
        }
    }
}