package com.example.giphydemo.ui.main

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

    private val _trendingGifsResponse = MutableLiveData<GifResponse>()
    val trendingGifsResponse = _trendingGifsResponse

    fun getTrendingGifs() {
        viewModelScope.launch(Dispatchers.IO) {
            val queryMap = hashMapOf(
                "api_key" to BuildConfig.API_KEY,
                "limit" to "25",
                "rating" to "g"
            )
            repository.getTrendingGifs(queryMap).apply {
                if(isSuccessful) {
                    trendingGifsResponse.postValue(body())
                }
            }
        }
    }
}