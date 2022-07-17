package com.example.giphydemo.model

import io.mockk.mockk
import io.mockk.mockkClass
import okhttp3.Headers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ResultResponseTest {
    private val resultResponse = ResultResponse<GifResponse>()

    @Before
    fun setup() {
        resultResponse.code = 200
        resultResponse.error = mockkClass(type = ErrorEntity::class, relaxed = true)
        resultResponse.headers = mockkClass(type = Headers::class, relaxed = true)
        resultResponse.setBody(mockk())
    }

    @Test
    fun `verify code is 200`() {
        Assert.assertEquals(200, resultResponse.code)
    }

    @Test
    fun `verify code is not 401`() {
        Assert.assertNotEquals(401, resultResponse.code)
    }

    @Test
    fun `verify error is non null`() {
        Assert.assertNotNull(resultResponse.error)
    }

    @Test
    fun `verify headers is not null`() {
        Assert.assertNotNull(resultResponse.headers)
    }

    @Test
    fun `verify body is not null`() {
        Assert.assertNotNull(resultResponse.body())
    }
}