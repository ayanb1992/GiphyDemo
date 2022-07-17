package com.example.giphydemo.model

import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GifDataTest {
    private lateinit var gifData: GifData

    @Before
    fun setup() {
        gifData = GifData(
            type = "gif",
            id = "FeKIq7ysZh34Q",
            username = "test",
            title = "title",
            bitlyGifUrl = "https://giphy.com/gifs/fred-astaire-ginger-rogers-so-i-have-a-habit-of-making-gifset-whenever-find-cute-fanvid-but-some-day",
            images = mockk(),
            isFavorite = false
        )
    }

    @Test
    fun `verify type is gif`() {
        Assert.assertEquals("gif", gifData.type)
    }

    @Test
    fun `verify id is FeKIq7ysZh34Q`() {
        Assert.assertEquals("FeKIq7ysZh34Q", gifData.id)
    }

    @Test
    fun `verify username is test`() {
        Assert.assertEquals("test", gifData.username)
    }

    @Test
    fun `verify title is title`() {
        Assert.assertEquals("title", gifData.title)
    }

    @Test
    fun `verify bitlyGifUrl is proper`() {
        Assert.assertEquals(
            "https://giphy.com/gifs/fred-astaire-ginger-rogers-so-i-have-a-habit-of-making-gifset-whenever-find-cute-fanvid-but-some-day",
            gifData.bitlyGifUrl
        )
    }

    @Test
    fun `verify images is not null`() {
        Assert.assertNotNull("gif", gifData.images)
    }

    @Test
    fun `verify isFavorite is false`() {
        Assert.assertEquals(false, gifData.isFavorite)
    }
}