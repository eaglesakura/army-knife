package com.eaglesakura.ktx.oneshotlivedata

import com.eaglesakura.ktx.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class EventStreamTest : BaseTestCase() {

    @Test
    fun emptyIdTest() {
        val empty = DataState.EMPTY_DATA
        val id = EventId("EVENT")
        assertEquals(empty.toString(), "EMPTY_DATA")
        assertNotEquals(empty, id)
        assertNotEquals(empty, empty)
        assertFalse(empty == empty)
        assertTrue(empty === empty)
    }

    @Test
    fun eventIdTest() {
        val id0 = EventId("EVENT")
        val id1 = EventId("EVENT")
        assertEquals(id0.toString(), "EVENT")
        assertEquals(id1.toString(), "EVENT")
        assertTrue(id0 == id0)
        assertEquals(id0, id0)
        assertNotEquals(id0, id1)
    }
}