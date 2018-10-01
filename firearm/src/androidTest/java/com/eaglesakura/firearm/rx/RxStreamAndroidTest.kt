package com.eaglesakura.firearm.rx

import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.extensions.assertUIThread
import com.eaglesakura.armyknife.junit.blockingTest
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consume
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
        stream.send("send1")
        assertThat(channel.receive()).apply {
            isEqualTo("send1")
        }
        channel.close()
    }

    @Test
    fun channel_ui() = blockingTest(Dispatchers.Main) {
        val stream = RxStream.create<String>()
        val channel = stream.toChannel()
        stream.send("send1")
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
            stream.send("send1")
            assertEquals("send1", receive())

            stream.send("send2")
            assertEquals("send2", receive())

            stream.send("send3")
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
                startsWith("send")
            }

            if (it.endsWith("finish")) {
                GlobalScope.launch { channel.send(Unit) }
            }
        }

        stream.send("send1")
        stream.send("send2")
        stream.send("send3")
        stream.send("send_finish")

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
            stream.send("value1")
            assertEquals("value1", receive())

            stream.send("value2")
            assertEquals("value2", receive())

            // Do not receive
            stream.send("value2")
            try {
                withTimeout(1, TimeUnit.SECONDS) {
                    receive()
                }
                fail("Do not receive.")
            } catch (err: TimeoutCancellationException) {
                // ok, do not receive.
            }

            // receive.
            stream.send("value1")
            assertEquals("value1", receive())
        }
    }
}