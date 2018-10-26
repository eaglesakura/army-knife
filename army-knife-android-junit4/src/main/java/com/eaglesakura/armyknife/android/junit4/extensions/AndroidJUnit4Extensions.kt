package com.eaglesakura.armyknife.android.junit4.extensions

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import com.eaglesakura.armyknife.junit.ROBOLECTRIC
import com.eaglesakura.armyknife.junit.blockingTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.robolectric.shadows.ShadowLog

private const val TAG = "JUnit4"

/**
 * This instance is a test-target application.
 * Application instance by app.apk
 */
val targetApplication: Application
    get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application

/**
 * This instance is a test-target context.
 * Context instance by app.apk
 */
val targetContext: Context
    get() = InstrumentationRegistry.getInstrumentation().targetContext

/**
 * This instance is a test context.
 * Context instance by app-test.apk
 */
val testContext: Context
    get() = InstrumentationRegistry.getInstrumentation().context

/**
 * Test JVM only.
 */
fun localTest(action: () -> Unit) {
    if (ROBOLECTRIC) {
        ShadowLog.stream = System.out
        action()
    } else {
        Log.i(TAG, "skip InstrumentationTest")
    }
}

/**
 * Test JVM only with Coroutines.
 */
fun localBlockingTest(dispatcher: CoroutineDispatcher = TestDispatchers.Default, action: suspend () -> Unit) {
    if (ROBOLECTRIC) {
        blockingTest(dispatcher, action)
    } else {
        Log.i(TAG, "skip in InstrumentationTest")
    }
}

/**
 * Test Android Device(or Emulator) only.
 */
fun instrumentationTest(action: () -> Unit) {
    if (ROBOLECTRIC) {
        Log.i(TAG, "skip in LocalUnitTest")
        return
    }
    action()
}

/**
 * Test Android Device(or Emulator) only with Coroutines.
 */
fun instrumentationBlockingTest(dispatcher: CoroutineDispatcher = TestDispatchers.Default, action: suspend () -> Unit) {
    if (ROBOLECTRIC) {
        Log.i(TAG, "skip in LocalUnitTest")
        return
    }
    blockingTest(dispatcher, action)
}

/**
 * Test with suspend.
 *
 * Architecture template created by @eaglesakura
 */
fun compatibleTest(action: () -> Unit) {
    if (ROBOLECTRIC) {
        ShadowLog.stream = System.out
    }
    action()
}

/**
 * Test with suspend.
 *
 * Architecture template created by @eaglesakura
 */
fun compatibleBlockingTest(dispatcher: CoroutineDispatcher = TestDispatchers.Default, action: suspend () -> Unit) {
    if (ROBOLECTRIC) {
        ShadowLog.stream = System.out
    }
    runBlocking(dispatcher) {
        action()
    }
}
