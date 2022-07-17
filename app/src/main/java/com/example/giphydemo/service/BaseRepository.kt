package com.example.giphydemo.service

import com.example.giphydemo.model.ErrorEntity
import com.example.giphydemo.model.ResultResponse
import retrofit2.Response

open class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultResponse<T> {
        val response = ResultResponse<T>()
        try {
            val res = apiCall.invoke()
            response.setBody(res.body())
            response.isSuccessFul = res.isSuccessful
            response.code = res.code()
            response.headers = res.headers()
            if (!response.isSuccessFul) {
                response.error =
                    ErrorEntity.APIError(code = response.code, throwable = Throwable(res.message()))
            }
        } catch (e: Throwable) {
            response.error = ErrorEntity.NetworkError(e)
        }
        return response
    }
}