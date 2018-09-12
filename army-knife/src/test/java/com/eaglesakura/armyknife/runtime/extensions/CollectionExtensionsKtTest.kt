package com.eaglesakura.armyknife.runtime.extensions

import com.eaglesakura.BaseTestCase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionExtensionsKtTest : BaseTestCase() {

    @Test
    fun Collection_isNullOrEmtpy() {
        mutableListOf<Unit>().let { list ->
            assertTrue(list.isNullOrEmpty())

            list.add(Unit)
            assertFalse(list.isNullOrEmpty())
        }

        val list: MutableList<Unit>? = null
        assertTrue(list.isNullOrEmpty())
    }
}