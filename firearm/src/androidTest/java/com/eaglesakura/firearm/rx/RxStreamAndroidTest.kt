package com.eaglesakura.firearm.rx

import com.eaglesakura.AndroidTestCase
import com.eaglesakura.armyknife.android.extensions.assertUIThread
import com.eaglesakura.armyknife.junit.blockingTest
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consume
import kotlinx.coroutines.experimental.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun subscribe() = blockingTest {
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
}