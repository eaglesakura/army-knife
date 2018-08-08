package com.eaglesakura.armyknife.android

import com.eaglesakura.BaseTestCase
import com.eaglesakura.armyknife.runtime.extensions.asCancelCallback
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import org.junit.Assert.*
import org.junit.Test
import kotlin.coroutines.experimental.coroutineContext

@Suppress("TestFunctionName")
class RuntimeExtensionsKtTest : BaseTestCase() {

    @Test(expected = CancellationException::class)
    fun Channel_cancel_in_receive() = runBlocking {
        val chan = Channel<Unit>()
        launch { chan.close(CancellationException()) }
        chan.receive()  // assert cancel in receive() function.

        // do not it.
        fail()
    }

    @Test
    fun coroutine_cancel() = runBlocking {

        val channel = Channel<Boolean>()
        val job = async {
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
    fun coroutine_not_cancel() = runBlocking {

        val channel = Channel<Boolean>()
        async {
            Thread.sleep(100)
            val callback = coroutineContext.asCancelCallback()
            withContext(NonCancellable) {
                channel.send(callback())
            }
        }
        // 未キャンセル
        assertFalse(channel.receive())
    }
}