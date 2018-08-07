@file:Suppress("unused")

package com.eaglesakura

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
abstract class AndroidTestCase {
    val application: Application
        get() = InstrumentationRegistry.getTargetContext().applicationContext as Application

    /**
     * UnitTest.apk Context
     */
    val textContext: Context
        get() = InstrumentationRegistry.getContext()

    val console = fun(message: String) { Log.d(javaClass.simpleName, message) }

    @Before
    fun onSetUp() = runBlocking { setUp() }

    @After
    fun onTearDown() = runBlocking { tearDown() }

    open suspend fun setUp() {
    }

    open suspend fun tearDown() {
    }
}