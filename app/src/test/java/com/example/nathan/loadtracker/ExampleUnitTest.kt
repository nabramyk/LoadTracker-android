package com.example.nathan.loadtracker

import org.junit.Assert

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @org.junit.jupiter.api.Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    @org.junit.jupiter.api.Test
    fun testCsvExporter() {

    }
}