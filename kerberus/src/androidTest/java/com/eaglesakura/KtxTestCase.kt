@file:Suppress("unused")

package com.eaglesakura

import android.app.Application
import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
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
    fun onSetUp() = runBlocking { setUp() }

    @After
    fun onTearDown() = runBlocking { tearDown() }

    open suspend fun setUp() {
    }

    open suspend fun tearDown() {
    }

}