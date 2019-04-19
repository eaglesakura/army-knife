package com.eaglesakura.armyknife.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.runtime.extensions.asCancelCallback
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RuntimeExtensionsKtTest {

    @Test
    fun Channel_close() = compatibleBlockingTest(TestDispatchers.Default) {
        val chan = Channel<Unit>()
        chan.close()
        GlobalScope.async {
            chan.close(CancellationException())
        }.await()
//        chan.receive()
    }

    @Test(expected = CancellationException::class)
    fun Channel_cancel_in_poll() = compatibleBlockingTest {
        val chan = Channel<Unit>()
        GlobalScope.launch {
            delay(500)
            chan.cancel(CancellationException())
        }

        while (true) {
            // throws in function.
            assertNull(chan.poll())
            yield()
        }
    }

    @Test(expected = CancellationException::class)
    fun Channel_cancel_in_receive() = compatibleBlockingTest {
        val chan = Channel<Unit>()
        GlobalScope.launch { chan.close(CancellationException()) }
        chan.receive() // assert cancel in receive() function.

        // do not it.
        fail()
    }

    @Test
    fun coroutine_cancel() = compatibleBlockingTest {

        val channel = Channel<Boolean>()
        val job = GlobalScope.async {
            Thread.sleep(100)
            val callback = coroutineContext.asCancelCallback()
            withContext(NonCancellable) {
                channel.send(callback())
            }
        }
        delay(10)
        job.cancel()

        // キャンセル済みとなる
        assertTrue(channel.receive())
    }

    @Test
    fun coroutine_not_cancel() = compatibleBlockingTest {
        val channel = Channel<Boolean>()
        GlobalScope.async {
            Thread.sleep(100)
            val callback = coroutineContext.asCancelCallback()
            withContext(NonCancellable) {
                channel.send(callback())
            }
        }
        // 未キャンセル
        assertFalse(channel.receive())
    }

    @Test(expected = TimeoutCancellationException::class)
    fun coroutine_withTimeout() = compatibleBlockingTest {
        withTimeout(TimeUnit.MILLISECONDS.toMillis(100)) {
            delay(1000)
        }

        fail()
    }

    @Test(expected = CancellationException::class)
    fun withContext_cancel() = compatibleBlockingTest {
        val topLevel = coroutineContext
        GlobalScope.launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            topLevel.cancel()
            yield()
        }

        yield()

        // Blocking include top level.
        withContext(Dispatchers.Default) {
            delay(TimeUnit.SECONDS.toMillis(2))
            fail()
        }
        fail()
    }
}