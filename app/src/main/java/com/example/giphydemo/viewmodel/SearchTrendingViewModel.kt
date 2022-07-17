package com.example.giphydemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.giphydemo.BuildConfig
import com.example.giphydemo.model.ErrorEntity
import com.example.giphydemo.model.GifData
import com.example.giphydemo.model.GifResponse
import com.example.giphydemo.model.database.entity.FavoriteGifs
import com.example.giphydemo.service.Repository
import com.example.giphydemo.util.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTrendingViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LIMIT = 25
        private const val RATING = "g"
        private const val OFFSET = 0
        private const val LANG = "en"
    }

    private val context = application

    private val repository by lazy { Repository(application) }

    private val _gifsResponse = MutableLiveData<Pair<Boolean, GifResponse>>()
    val gifsResponse: LiveData<Pair<Boolean, GifResponse>> = _gifsResponse

    private val _insertComplete = MutableLiveData<Pair<Boolean, String>>()
    val insertComplete: LiveData<Pair<Boolean, String>> = _insertComplete

    private val _removeComplete = MutableLiveData<Pair<Boolean, String>>()
    val removeComplete: LiveData<Pair<Boolean, String>> = _removeComplete

    private val _allFavoriteGifs = MutableLiveData<ArrayList<FavoriteGifs>>()
    val allFavoriteGifs: LiveData<ArrayList<FavoriteGifs>> = _allFavoriteGifs

    val networkError = MutableLiveData<ErrorEntity?>()

    val dbError = MutableLiveData<ErrorEntity?>()

    var queryString: String = ""
    val dataList: ArrayList<GifData> = ArrayList()

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var totalPages: Int = 0
    var currentPage: Int = 1

    fun insertFavoriteGif(gifData: GifData) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataToBeInserted =
                FavoriteGifs(gifData.id, gifData.images?.downsizedMedium?.url ?: "", gifData.title)
            try {
                repository.insertGifData(dataToBeInserted).also {
                    _insertComplete.postValue(true to gifData.id)
                }
            } catch (e: Exception) {
                dbError.postValue(ErrorEntity.DatabaseError(e))
            }
        }
    }

    fun removeFavoriteGif(gifId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeFavoriteGif(gifId).also {
                    _removeComplete.postValue(true to gifId)
                }
            } catch (e: Exception) {
                dbError.postValue(ErrorEntity.DatabaseError(e))
            }
        }
    }

    fun fetchAllFavoriteGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.retrieveAllFavorites().also {
                    _allFavoriteGifs.postValue(it as ArrayList)
                }
            } catch (e: Exception) {
                dbError.postValue(ErrorEntity.DatabaseError(e))
            }
        }
    }

    fun getTrendingGifs() {
        if (context.isNetworkAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                val queryMap = QueryFactory.getTrendingGifsQuery(
                    apiKey = BuildConfig.API_KEY,
                    limit = LIMIT,
                    rating = RATING
                )
                repository.getTrendingGifs(queryMap).apply {
                    if (isSuccessFul) {
                        try {
                            val favoriteGifs = repository.retrieveAllFavorites()
                            (body() as GifResponse).data.forEach {
                                favoriteGifs.find { favGif -> favGif.id == it.id }?.let { _ ->
                                    it.isFavorite = true
                                }
                            }
                        } catch (e: Exception) {
                            dbError.postValue(ErrorEntity.DatabaseError(e))
                        }
                        body()?.let { _gifsResponse.postValue(false to it) }
                    } else {
                        networkError.postValue(this.error)
                    }
                }
            }
        } else {
            networkError.postValue(ErrorEntity.CustomError(Throwable("Please turn on network")))
        }
    }

    fun searchGifs(query: String) {
        if (context.isNetworkAvailable()) {
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
                    if (isSuccessFul) {
                        try {
                            val favoriteGifs = repository.retrieveAllFavorites()
                            (body() as GifResponse).data.forEach {
                                favoriteGifs.find { favGif -> favGif.id == it.id }?.let { _ ->
                                    it.isFavorite = true
                                }
                            }
                        } catch (e: Exception) {
                            dbError.postValue(ErrorEntity.DatabaseError(e))
                        }
                        body()?.let { _gifsResponse.postValue(true to it) }
                    } else {
                        networkError.postValue(this.error)
                    }
                }
            }
        } else {
            networkError.postValue(ErrorEntity.CustomError(Throwable("Please turn on network")))
        }
    }

    fun searchGifs(query: String, offset: Int) {
        if (context.isNetworkAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                val queryMap = QueryFactory.getSearchQueryParams(
                    apiKey = BuildConfig.API_KEY,
                    limit = LIMIT,
                    query = query,
                    rating = RATING,
                    offset = offset,
                    lang = LANG
                )
                repository.searchGifs(queryMap).apply {
                    if (isSuccessFul) {
                        try {
                            val favoriteGifs = repository.retrieveAllFavorites()
                            (body() as GifResponse).data.forEach {
                                favoriteGifs.find { favGif -> favGif.id == it.id }?.let { _ ->
                                    it.isFavorite = true
                                }
                            }
                        } catch (e: Exception) {
                            dbError.postValue(ErrorEntity.DatabaseError(e))
                        }
                        body()?.let { _gifsResponse.postValue(true to it) }
                    } else {
                        networkError.postValue(this.error)
                    }
                }
            }
        } else {
            networkError.postValue(ErrorEntity.CustomError(Throwable("Please turn on network")))
        }
    }
}