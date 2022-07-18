package com.example.giphydemo.util

object Constants {
    const val BASE_URL = "https://api.giphy.com/v1/"
    const val API_TIME_OUT = 5000L
    const val DB_NAME = "gifdb.db"
    const val DB_VERSION = 1
    const val DEFAULT_SEARCH_GIF_OFFSET = 25
    const val DEFAULT_HAPTIC_DURATION = 100L
    const val API_KEY = "b7RqVS9CmSYWcWiOsNJDyecA9oOeTAyR"
    const val DB_PASSWORD = "abcd" //This s just for test purposes

    object ApiQueryParams {
        const val API_KEY = "api_key"
        const val LIMIT = "limit"
        const val RATING = "rating"
        const val QUERY = "q"
        const val OFFSET = "offset"
        const val LANG = "lang"
    }
}