package com.example.giphydemo.model

import okhttp3.Headers

class ResultResponse<T> {
    var isSuccessFul = false
    var code = 0
    var headers: Headers? = null
    private var response: T? = null
    var error: ErrorEntity? = null

    fun setBody(response: T?) {
        this.response = response
    }

    fun body(): T? {
        return response
    }
}

sealed class ErrorEntity {
    data class NetworkError(val throwable: Throwable? = null) : ErrorEntity()
    data class DatabaseError(val throwable: Throwable? = null) : ErrorEntity()
    data class APIError(val code: Int? = null, val throwable: Throwable? = null) : ErrorEntity()
    data class CustomError(val throwable: Throwable? = null) : ErrorEntity()
}