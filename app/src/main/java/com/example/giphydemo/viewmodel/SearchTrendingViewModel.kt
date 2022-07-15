package com.example.giphydemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphydemo.BuildConfig
import com.example.giphydemo.model.GifResponse
import com.example.giphydemo.service.Repository
import com.example.giphydemo.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTrendingViewModel : ViewModel() {

    private val repository = Repository.getInstance()

    private val _gifsResponse = MutableLiveData<GifResponse>()
    val gifsResponse = _gifsResponse

    fun getTrendingGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = hashMapOf(
                Constants.ApiQueryParams.API_KEY to BuildConfig.API_KEY,
                Constants.ApiQueryParams.LIMIT to "25",
                Constants.ApiQueryParams.RATING to "g"
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
            val queryMap = hashMapOf(
                Constants.ApiQueryParams.API_KEY to BuildConfig.API_KEY,
                Constants.ApiQueryParams.QUERY to query,
                Constants.ApiQueryParams.LIMIT to "25",
                Constants.ApiQueryParams.RATING to "g",
                Constants.ApiQueryParams.OFFSET to "0",
                Constants.ApiQueryParams.LANG to "en"
            )
            repository.searchGifs(queryMap).apply {
                if(isSuccessful) {
                    _gifsResponse.postValue(body())
                }
            }
        }
    }
}