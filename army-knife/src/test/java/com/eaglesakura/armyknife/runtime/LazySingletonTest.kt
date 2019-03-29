package com.eaglesakura.armyknife.runtime

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import org.junit.Assert.* // ktlint-disable no-wildcard-imports
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazySingletonTest {

    @Test
    fun get() = compatibleTest {
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