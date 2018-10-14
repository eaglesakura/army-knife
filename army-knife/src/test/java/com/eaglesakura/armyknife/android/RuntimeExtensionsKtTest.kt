package com.eaglesakura.armyknife.android

import com.eaglesakura.BaseTestCase
import com.eaglesakura.armyknife.junit.blockingTest
import com.eaglesakura.armyknife.runtime.extensions.asCancelCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

@Suppress("TestFunctionName")
class RuntimeExtensionsKtTest : BaseTestCase() {

    @Test(expected = CancellationException::class)
    fun Channel_cancel_in_poll() = blockingTest {
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
    fun Channel_cancel_in_receive() = blockingTest {
        val chan = Channel<Unit>()
        GlobalScope.launch { chan.close(CancellationException()) }
        chan.receive()  // assert cancel in receive() function.

        // do not it.
        fail()
    }

    @Test
    fun coroutine_cancel() = blockingTest {

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
    fun coroutine_not_cancel() = blockingTest {
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
    fun coroutine_withTimeout() = blockingTest {
        withTimeout(100, TimeUnit.MILLISECONDS) {
            delay(1000)
        }

        fail()
    }

    @Test(expected = IOException::class)
    fun coroutine_withTimeout_withContext() = blockingTest {
        val channel = Channel<Unit>()
        GlobalScope.launch {
            val current = coroutineContext
            // Cancel this job.
            launch {
                delay(10, TimeUnit.MILLISECONDS)
                current.cancel(IOException("cancel in other thread"))
            }

            try {
                withTimeout(1000, TimeUnit.MILLISECONDS) {
                    delay(100)
                    yield()
                }
                channel.cancel(IllegalStateException("not canceled"))
            } catch (e: JobCancellationException) {
                e.cause!!.printStackTrace()
                withContext(NonCancellable) {
                    channel.cancel(e.cause)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                channel.cancel(e)
            }
        }

        channel.receive()
    }

    @Test(expected = CancellationException::class)
    fun withContext_cancel() = blockingTest {
        val topLevel = coroutineContext
        GlobalScope.launch {
            delay(1, TimeUnit.SECONDS)
            topLevel.cancel()
            yield()
        }

        yield()

        // Blocking include top level.
        withContext(Dispatchers.Default) {
            delay(2, TimeUnit.SECONDS)
            fail()
        }
        fail()
    }

}