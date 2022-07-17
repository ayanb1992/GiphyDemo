package com.example.giphydemo.model

import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ImagesTest {
    private lateinit var images: Images

    @Before
    fun setup() {
        images = Images(
            original = mockk(),
            downsized = mockk(),
            downsizedLarge = mockk(),
            downsizedMedium = mockk(),
            downsizedSmall = mockk()
        )
    }

    @Test
    fun `verify original is not null`() {
        Assert.assertNotNull(images.original)
    }

    @Test
    fun `verify downsized is not null`() {
        Assert.assertNotNull(images.downsized)
    }

    @Test
    fun `verify downsizedLarge is not null`() {
        Assert.assertNotNull(images.downsizedLarge)
    }

    @Test
    fun `verify downsizedMedium is not null`() {
        Assert.assertNotNull(images.downsizedMedium)
    }

    @Test
    fun `verify downsizedSmall is not null`() {
        Assert.assertNotNull(images.downsizedSmall)
    }
}