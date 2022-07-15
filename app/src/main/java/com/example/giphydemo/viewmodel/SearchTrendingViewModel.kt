package com.example.giphydemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphydemo.BuildConfig
import com.example.giphydemo.model.GifResponse
import com.example.giphydemo.service.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTrendingViewModel : ViewModel() {

    private val repository = Repository.getInstance()

    private val _gifsResponse = MutableLiveData<GifResponse>()
    val gifsResponse = _gifsResponse

    fun getTrendingGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = QueryFactory.getTrendingGifsQuery(
                apiKey = BuildConfig.API_KEY,
                limit = 25,
                rating = "g"
            )
            repository.getTrendingGifs(queryMap).apply {
                if(isSuccessful) {
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
                if(isSuccessful) {
                    _gifsResponse.postValue(body())
                }
            }
        }
    }
}