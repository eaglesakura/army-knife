package com.eaglesakura.armyknife.runtime.coroutines

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.runtime.Random
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

@RunWith(AndroidJUnit4::class)
class FlexibleThreadPoolDispatcherTest {

    @Test
    fun launch_success() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher(5, 10, TimeUnit.MILLISECONDS, "test-dispatcher")
        assertEquals(0, dispatcher.aliveThreadNum)

        withContext(dispatcher) {
            assertEquals(1, dispatcher.aliveThreadNum)
            delay(10)
            println("dispatch success")
        }

        assertThat(dispatcher.aliveThreadNum).apply {
            isGreaterThan(0)
            isLessThanOrEqualTo(5)
        }

        delay(30)
        assertEquals(0, dispatcher.aliveThreadNum)
    }

    @Test
    fun launch_success_with_current() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher(5, 10, TimeUnit.MILLISECONDS, "test-dispatcher")
        assertEquals(0, dispatcher.aliveThreadNum)

        withContext(coroutineContext + dispatcher) {
            assertEquals(1, dispatcher.aliveThreadNum)
            delay(10)
            println("dispatch success")
        }
        assertThat(dispatcher.aliveThreadNum).apply {
            isGreaterThan(0)
            isLessThanOrEqualTo(5)
        }

        delay(30)
        assertEquals(0, dispatcher.aliveThreadNum)
    }

    @Test
    fun launch_auto_scale() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher(5, 10, TimeUnit.MILLISECONDS, "test-dispatcher")

        val loop = 10000
        val channel = Channel<Int>(loop)
        for (i in 0..loop) {
            GlobalScope.launch(dispatcher) {
                delay((Random.int32() % 10 + 1).toLong())
                channel.send(i)
            }
        }

        while (!channel.isFull) {
            delay(1)
        }
        assertEquals(5, dispatcher.aliveThreadNum)

        delay(20)
        assertEquals(0, dispatcher.aliveThreadNum)
    }
}