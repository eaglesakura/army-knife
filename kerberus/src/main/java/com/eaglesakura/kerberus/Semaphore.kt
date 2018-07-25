@file:Suppress("unused")

package com.eaglesakura.kerberus

import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

/**
 * TaskQueueの制御等に用いるセマフォ.
 *
 * ThreadPoolのような順番待ち処理を適切に行う.
 */
interface Semaphore {
    @Throws(CancellationException::class)
    suspend fun <R> run(block: suspend () -> R): R

    companion object {
        val NonBlocking: Semaphore = object : Semaphore {
            override suspend fun <R> run(block: suspend () -> R): R {
                return block()
            }
        }

        /**
         * For network input/output semaphore.
         */
        val Network: Semaphore = SemaphoreImpl(Runtime.getRuntime().availableProcessors() * 2 + 1)

        /**
         * For storage input/output semaphore.
         */
        val IO: Semaphore = SemaphoreImpl(Runtime.getRuntime().availableProcessors() * 2 + 1)

        /**
         * Global queue.
         */
        val Queue: Semaphore = SemaphoreImpl(1)

        /**
         * キューイング用のセマフォを生成する.
         */
        fun newQueue(): Semaphore {
            return newInstance(1)
        }

        /**
         * 制御用のセマフォを生成する
         */
        @Suppress("MemberVisibilityCanBePrivate")
        fun newInstance(maxParallel: Int): Semaphore {
            return SemaphoreImpl(maxParallel)
        }
    }


    private class SemaphoreImpl(maxParallel: Int) : Semaphore {
        private val channel: Channel<Unit> = Channel(maxParallel)

        override suspend fun <R> run(block: suspend () -> R): R {
            channel.send(Unit)
            try {
                return block()
            } finally {
                withContext(NonCancellable) {
                    channel.receive()
                }
            }
        }
    }
}

fun Semaphore.launch(context: CoroutineContext, block: suspend CoroutineScope.() -> Unit) {
    kotlinx.coroutines.experimental.launch(context) {
        block()
    }
}
