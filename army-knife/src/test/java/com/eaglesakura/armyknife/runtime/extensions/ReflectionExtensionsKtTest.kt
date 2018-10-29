package com.eaglesakura.armyknife.runtime.extensions

import android.app.Activity
import android.graphics.Rect
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReflectionExtensionsKtTest {

    @Test
    fun instanceOf() {
        val item = Rect()
        assertTrue(item.instanceOf(Rect::class))
        assertTrue(item.instanceOf(Parcelable::class))
        assertFalse(item.instanceOf(Activity::class))
    }
}