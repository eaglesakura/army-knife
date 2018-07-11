package com.eaglesakura.ktx.android

import com.eaglesakura.ktx.BaseTestCase
import com.eaglesakura.ktx.runtime.extensions.asCancelCallback
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("TestFunctionName")
class RuntimeExtensionsKtTest : BaseTestCase() {

    @Test
    fun Coroutineのキャンセルが行える() = runBlocking {

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
    fun Coroutineの未キャンセルチェック() = runBlocking {

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