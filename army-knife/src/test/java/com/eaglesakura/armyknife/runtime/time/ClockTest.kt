package com.eaglesakura.armyknife.runtime.time

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClockTest {
    @Test
    fun plus_duration() {
        val base = Clock(1000)
        val diff = Duration(100)
        assertEquals(Clock(1100), base + diff)
    }

    @Test
    fun minus_clock() {
        val base = Clock(1000)
        val other = Clock(100)
        assertEquals(Duration(900), base - other)
    }

    @Test
    fun minus_duration() {
        val base = Clock(1000)
        val diff = Duration(200)
        assertEquals(Clock(800), base - diff)
    }
}