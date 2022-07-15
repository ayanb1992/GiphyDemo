package com.example.giphydemo.model

import com.google.gson.annotations.SerializedName

data class GifResponse(val data: List<GifData>)

data class GifData(
    val type: String,
    val id: String,
    val username: String,
    val title: String,
    @SerializedName("bitly_gif_url") val bitlyGifUrl: String,
    val images: Images
)

data class Images(
    val original: ImageItem?,
    val downsized: ImageItem?,
    @SerializedName("downsized_large") val downsizedLarge: ImageItem?,
    @SerializedName("downsized_medium") val downsizedMedium: ImageItem?,
    @SerializedName("downsized_small") val downsizedSmall: ImageItem?,

)

data class ImageItem(
    val height: String?,
    val width: String?,
    val size: String?,
    val url: String?,
    @SerializedName("mp4_size") val mp4Size: String?,
    val mp4: String?,
    @SerializedName("webp_size")val webpSize: String?,
    val webp: String?,
    val frames: String?,
    val hash: String?
)