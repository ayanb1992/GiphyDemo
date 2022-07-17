package com.example.giphydemo.model

import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ErrorEntityTest {
    private lateinit var networkError: ErrorEntity.NetworkError
    private lateinit var apiError: ErrorEntity.APIError
    private lateinit var customError: ErrorEntity.CustomError
    private lateinit var databaseError: ErrorEntity.DatabaseError

    @Before
    fun setup() {
        networkError = ErrorEntity.NetworkError(mockk())
        apiError = ErrorEntity.APIError(code = 401, throwable = mockk())
        customError = ErrorEntity.CustomError(mockk())
        databaseError = ErrorEntity.DatabaseError(mockk())
    }

    @Test
    fun `verify apiError throwable is not null`() {
        Assert.assertNotNull(apiError.throwable)
    }

    @Test
    fun `verify apiError code is 401`() {
        Assert.assertEquals(401, apiError.code)
    }

    @Test
    fun `verify networkError throwable is not null`() {
        Assert.assertNotNull(networkError.throwable)
    }

    @Test
    fun `verify databaseError throwable is not null`() {
        Assert.assertNotNull(databaseError.throwable)
    }

    @Test
    fun `verify customError throwable is not null`() {
        Assert.assertNotNull(customError.throwable)
    }
}