package com.example.giphydemo.util

object Constants {
    const val BASE_URL = "https://api.giphy.com/v1/"
    const val API_TIME_OUT = 5000L
    const val DB_NAME = "gifdb.db"
    const val DB_VERSION = 1

    object ApiQueryParams {
        const val API_KEY = "api_key"
        const val LIMIT = "limit"
        const val RATING = "rating"
        const val QUERY = "q"
        const val OFFSET = "offset"
        const val LANG = "lang"
    }
}