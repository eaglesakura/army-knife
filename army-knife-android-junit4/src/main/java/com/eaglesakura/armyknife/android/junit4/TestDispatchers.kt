package com.eaglesakura.armyknife.android.junit4

import com.eaglesakura.armyknife.junit.ROBOLECTRIC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

/**
 * Dispatchers for UnitTest.
 */
object TestDispatchers {
    val Default = Dispatchers.Default

    /**
     * in Instrumentation test, then returns "Dispatchers.Main".
     * in Local Unit Test, then returns a dummy single-thread dispatcher.
     */
    val Main: CoroutineDispatcher by lazy {
        if (ROBOLECTRIC) {
            newSingleThreadContext("junit4-main")
        } else {
            Dispatchers.Main
        }
    }
}