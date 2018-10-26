package com.eaglesakura.armyknife.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.TestDispatchers
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.runtime.extensions.asCancelCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

@RunWith(AndroidJUnit4::class)
class RuntimeExtensionsKtTest {

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
        chan.receive()  // assert cancel in receive() function.

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

    @Test(expected = IOException::class)
    fun coroutine_withTimeout_withContext() = compatibleBlockingTest {
        withContext(TestDispatchers.Default) {
            val current = coroutineContext
            // Cancel this job.
            GlobalScope.launch {
                delay(TimeUnit.MILLISECONDS.toMillis(10))
                current.cancel(IOException("cancel by other thread"))
            }

            withTimeout(TimeUnit.MILLISECONDS.toMillis(1000)) {
                delay(100)
                yield()
            }
        }
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