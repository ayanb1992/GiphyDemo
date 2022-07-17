package com.example.giphydemo.model

import org.junit.Assert
import org.junit.Test

class PaginationTest {
    private var pagination = Pagination(100, 25, 0)

    @Test
    fun `verify if totalCount is 100`() {
        Assert.assertEquals(100, pagination.totalCount)
    }

    @Test
    fun `verify if count is 25`() {
        Assert.assertEquals(25, pagination.count)
    }

    @Test
    fun `verify if offset is 0`() {
        Assert.assertEquals(0, pagination.offset)
    }
}