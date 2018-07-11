@file:Suppress("unused")

package com.eaglesakura

import android.app.Application
import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
abstract class KtxTestCase {
    val application: Application
        get() = InstrumentationRegistry.getTargetContext().applicationContext as Application

    /**
     * UnitTest.apk Context
     */
    val textContext: Context
        get() = InstrumentationRegistry.getContext()

    val LOG_TAG = javaClass.simpleName

    @Before
    open fun setUp() {
    }

}