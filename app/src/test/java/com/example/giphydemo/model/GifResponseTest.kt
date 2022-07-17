package com.example.giphydemo.model

import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class GifResponseTest {
    private var gifResponse = GifResponse(mockk(), mockk())

    @Test
    fun `verify pagination is not null`() {
        Assert.assertNotNull(gifResponse.pagination)
    }

    @Test
    fun `verify data is not null`() {
        Assert.assertNotNull(gifResponse.data)
    }
}