package com.example.giphydemo.model.database.entity

import org.junit.Assert
import org.junit.Test


class FavoriteGifsTest {

    private val favoriteGifs = FavoriteGifs("id", "https://www.google.com", "title", true)

    @Test
    fun `verify id value`() {
        Assert.assertEquals("id", favoriteGifs.id)
    }

    @Test
    fun `verify url value`() {
        Assert.assertEquals("https://www.google.com", favoriteGifs.url)
    }

    @Test
    fun `verify title value`() {
        Assert.assertEquals("title", favoriteGifs.title)
    }

    @Test
    fun `verify isFavorite value`() {
        Assert.assertEquals(true, favoriteGifs.isFavorite)
    }
}