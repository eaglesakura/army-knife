package com.eaglesakura.armyknife.runtime.coroutines

import kotlinx.coroutines.experimental.ExecutorCoroutineDispatcherBase
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Dispatcher with flexible thread pool.
 *
 * Thread pools are auto scale by ThreadPoolExecutor.
 * When do not using this instance in coroutines, Thread will be shutdown by a Dalvik.
 */
class FlexibleThreadPoolDispatcher(
        private val maxThreads: Int,
        keepAliveTime: Long,
        keepAliveTimeUnit: TimeUnit,
        private val name: String
) : ExecutorCoroutineDispatcherBase() {

    private val threadPool = FlexibleThreadPoolExecutor(maxThreads, keepAliveTime, keepAliveTimeUnit)

    val aliveThreadNum: Int
        get() = threadPool.poolSize

    override val executor: Executor = threadPool

    /**
     * Closes this dispatcher -- shuts down all threads in this pool and releases resources.
     */
    override fun close() {
        threadPool.shutdown()
    }

    override fun toString(): String = "FlexibleThreadPoolDispatcher[$maxThreads, $name]"
}

private class FlexibleThreadPoolExecutor(
        maxThreads: Int,
        keepAliveTime: Long,
        keepAliveTimeUnit: TimeUnit) : ThreadPoolExecutor(0, maxThreads, keepAliveTime, keepAliveTimeUnit, LinkedBlockingDeque()) {
    override fun execute(command: Runnable?) {
        try {
            corePoolSize = maximumPoolSize
            super.execute(command)
        } finally {
            corePoolSize = 0
        }
    }
}
