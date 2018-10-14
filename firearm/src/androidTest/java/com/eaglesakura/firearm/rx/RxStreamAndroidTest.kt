package com.eaglesakura.firearm.rx

import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.extensions.assertUIThread
import com.eaglesakura.armyknife.junit.blockingTest
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxStreamAndroidTest : AndroidTestCase() {

    @Test
    fun channel_background() = blockingTest(Dispatchers.Default) {
        val stream = RxStream.create<String>()
        val channel = stream.toChannel()
        stream.next("send1")
        assertThat(channel.receive()).apply {
            isEqualTo("send1")
        }
        channel.close()
    }

    @Test
    fun channel_ui() = blockingTest(Dispatchers.Main) {
        val stream = RxStream.create<String>()
        val channel = stream.toChannel()
        stream.next("send1")
        yield()
        assertThat(channel.receive()).apply {
            isEqualTo("send1")
        }
        channel.close()
    }


    @Test
    fun channel_multi() = blockingTest(Dispatchers.Main) {
        val stream = RxStream.create<String>()
        stream.toChannel().consume {
            stream.next("send1")
            assertEquals("send1", receive())

            stream.next("send2")
            assertEquals("send2", receive())

            stream.next("send3")
            assertEquals("send3", receive())
        }
    }

    @Test
    fun subscribe() = blockingTest(Dispatchers.Default) {
        val stream = RxStream.create<String>()
        val channel = Channel<Unit>()
        val disposable = stream.subscribe {
            assertUIThread()
            assertThat(it).apply {
                startsWith("next")
            }

            if (it.endsWith("finish")) {
                GlobalScope.launch { channel.send(Unit) }
            }
        }

        stream.next("send1")
        stream.next("send2")
        stream.next("send3")
        stream.next("send_finish")

        channel.receive()  // await.
        disposable.dispose()
    }

    @Test
    fun builder_transform() = blockingTest(Dispatchers.Main) {
        val stream = RxStream.Builder<String>().apply {
            observableTransform = { origin ->
                origin.distinctUntilChanged()
            }
        }.build()

        stream.toChannel().consume {
            stream.next("value1")
            assertEquals("value1", receive())

            stream.next("value2")
            assertEquals("value2", receive())

            // Do not receive
            stream.next("value2")
            try {
                withTimeout(1, TimeUnit.SECONDS) {
                    receive()
                }
                fail("Do not receive.")
            } catch (err: TimeoutCancellationException) {
                // ok, do not receive.
            }

            // receive.
            stream.next("value1")
            assertEquals("value1", receive())
        }
    }
}