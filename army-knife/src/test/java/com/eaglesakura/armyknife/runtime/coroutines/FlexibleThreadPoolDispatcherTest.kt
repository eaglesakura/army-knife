package com.eaglesakura.armyknife.runtime.coroutines

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.runtime.Random
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FlexibleThreadPoolDispatcherTest {

    @Test
    fun launch_success() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        withContext(dispatcher) {
            delay(10)
            println("dispatch success")
        }
    }

    @Test
    fun launch_success_with_current() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        withContext(coroutineContext + dispatcher) {
            delay(10)
            println("dispatch success")
        }
    }

    @Ignore
    @Test
    fun launch_auto_scale() = compatibleBlockingTest {
        val dispatcher = FlexibleThreadPoolDispatcher.newDispatcher(5, 10, TimeUnit.MILLISECONDS)

        val loop = 10000
        val channel = Channel<Int>(loop)
        for (i in 0..loop) {
            GlobalScope.launch(dispatcher) {
                delay((Random.int32() % 10 + 1).toLong())
                channel.send(i)
            }
        }

        // Can't Run this test
//        while (!channel.isFull) {
//            delay(1)
//        }
    }
}