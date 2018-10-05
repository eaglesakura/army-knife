package com.eaglesakura.firearm.event

import com.eaglesakura.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class EventIdTest : BaseTestCase() {

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