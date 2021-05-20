package com.more.sandboxapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun symbolParser() {
        val sampleTest = "T {W/G}{G}{W} sample text {B} dad"
        sampleTest.indexOf("{W/G}")
        println(sampleTest.indexOf("{W/G}"))
        var index = sampleTest.indexOfAny(listOf("{W}","{G}","{B}", "{W/G}}"))
        var start = 0
        val indexList = mutableListOf(index)
        while (index != -1) {
            print(sampleTest.substring(start, index))
            println("IMG(${sampleTest.substring(index, index+3)})")
            start = index+3
            index = sampleTest.indexOfAny(listOf("{W}","{G}","{B}", "{W/G}}"), index+2)
            if (index != -1) {
                indexList.add(index)
            }
        }
        indexList.map { s -> s to s + 3 }.forEach {
            println(sampleTest.substring(it.first,it.second))
        }
    }

    @Test
    fun symbolParser2() {
        val sampleTest = "T {W/G} {G}{W} sample text {B} dad"

        val match = sampleTest.findAnyOf(listOf("{W}","{G}","{B}", "{W/G}"))
        while (match != null) {

        }

        println(pair?.first)
        println(pair?.second)
    }
}