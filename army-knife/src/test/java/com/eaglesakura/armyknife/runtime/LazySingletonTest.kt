package com.eaglesakura.armyknife.runtime

import com.eaglesakura.BaseTestCase
import org.junit.Assert.*
import org.junit.Test

class LazySingletonTest : BaseTestCase() {


    @Test
    fun get() {
        val singleton = LazySingleton<String>()

        val instance1 = singleton.get {
            Random.largeString()
        }

        val instance2 = singleton.get {
            fail()
            "abcdefg"
        }

        assertEquals(instance1, instance2)
        assertNotEquals("abcdefg", instance2)
    }
}