package com.eaglesakura.armyknife.runtime.coroutines

import com.eaglesakura.armyknife.junit.blockingTest
import com.eaglesakura.armyknife.runtime.Random
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.coroutineContext

class FlexibleThreadPoolDispatcherTest {

    @Test
    fun launch_success() = blockingTest {
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
    fun launch_success_with_current() = blockingTest {
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
    fun launch_auto_scale() = blockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher(5, 10, TimeUnit.MILLISECONDS, "test-dispatcher")

        val loop = 10000
        val channel = Channel<Int>(loop)
        for (i in 0..loop) {
            GlobalScope.launch(dispatcher) {
                delay(Random.int32() % 10 + 1)
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