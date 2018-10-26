package com.eaglesakura.armyknife.runtime.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectionExtensionsKtTest {

    @Test
    fun Collection_isNullOrEmtpy() = compatibleTest {
        mutableListOf<Unit>().let { list ->
            assertTrue(list.isNullOrEmpty())

            list.add(Unit)
            assertFalse(list.isNullOrEmpty())
        }

        val list: MutableList<Unit>? = null
        assertTrue(list.isNullOrEmpty())
    }
}