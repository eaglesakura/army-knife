package com.eaglesakura

import android.app.Application
import android.os.Build
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
abstract class BaseTestCase {

    @Suppress("MemberVisibilityCanBePrivate")
    val application: Application
        get() = RuntimeEnvironment.application

    @Before
    fun onSetUp() = runBlocking { setUp() }

    @After
    fun onTearDown() = runBlocking { tearDown() }

    open suspend fun setUp() {
    }

    open suspend fun tearDown() {
    }
}