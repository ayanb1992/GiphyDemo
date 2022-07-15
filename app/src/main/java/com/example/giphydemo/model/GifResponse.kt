package com.example.giphydemo.model

import com.google.gson.annotations.SerializedName

data class GifResponse(val data: List<GifData>)

data class GifData(
    val type: String,
    val id: String,
    val username: String,
    val title: String,
    @SerializedName("bitly_gif_url") val bitlyGifUrl: String
)