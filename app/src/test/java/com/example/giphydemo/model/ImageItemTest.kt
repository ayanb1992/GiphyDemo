package com.example.giphydemo.model

import org.junit.Assert
import org.junit.Test

class ImageItemTest {
    private var imageItem = ImageItem(
        height = "320",
        width = "320",
        size = "1024",
        url = "https://www.google.com",
        mp4Size = "1024",
        mp4 = "https://www.google.com",
        webpSize = "1024",
        webp = "https://www.google.com",
        frames = "30",
        hash = "h8hjs8snf8sn83b#cc"
    )

    @Test
    fun `verify height is 320`() {
        Assert.assertEquals("320", imageItem.height)
    }

    @Test
    fun `verify width is 320`() {
        Assert.assertEquals("320", imageItem.width)
    }

    @Test
    fun `verify size is 1024`() {
        Assert.assertEquals("1024", imageItem.size)
    }

    @Test
    fun `verify url is proper`() {
        Assert.assertEquals("https://www.google.com", imageItem.url)
    }

    @Test
    fun `verify mp4Size is 1024`() {
        Assert.assertEquals("1024", imageItem.mp4Size)
    }

    @Test
    fun `verify mp4 is proper`() {
        Assert.assertEquals("https://www.google.com", imageItem.mp4)
    }

    @Test
    fun `verify webpSize is 1024`() {
        Assert.assertEquals("1024", imageItem.webpSize)
    }

    @Test
    fun `verify webp is proper`() {
        Assert.assertEquals("https://www.google.com", imageItem.webp)
    }

    @Test
    fun `verify frames is 30`() {
        Assert.assertEquals("30", imageItem.frames)
    }

    @Test
    fun `verify hash is proper`() {
        Assert.assertEquals("h8hjs8snf8sn83b#cc", imageItem.hash)
    }
}