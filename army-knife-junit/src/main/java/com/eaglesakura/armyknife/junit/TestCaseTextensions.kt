package com.eaglesakura.armyknife.junit

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking

/**
 * Robolectric runtime is true.
 */
val ROBOLECTRIC: Boolean = try {
    Class.forName("org.robolectric.Robolectric")
    true
} catch (err: ClassNotFoundException) {
    false
}

fun blockingTest(dispatcher: CoroutineDispatcher = Dispatchers.Default, action: suspend () -> Unit) {
    runBlocking(dispatcher) {
        action()
    }
}
