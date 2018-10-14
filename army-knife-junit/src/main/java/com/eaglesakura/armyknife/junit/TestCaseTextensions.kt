package com.eaglesakura.armyknife.junit

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.SoftAssertions

/**
 * Robolectric runtime is true.
 */
val ROBOLECTRIC: Boolean = try {
    Class.forName("org.robolectric.Robolectric")
    true
} catch (err: ClassNotFoundException) {
    false
}

/**
 * Test with suspend.
 *
 * Architecture template created by @eaglesakura
 */
fun blockingTest(dispatcher: CoroutineDispatcher = Dispatchers.Default, action: suspend () -> Unit) {
    runBlocking(dispatcher) {
        action()
    }
}

/**
 * Values validation with AssertJ
 *
 * Architecture template created by @eaglesakura
 */
fun softAssertions(block: SoftAssertions.() -> Unit) {
    SoftAssertions().apply {
        block(this)
        assertAll()
    }
}
