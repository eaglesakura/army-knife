package com.eaglesakura.kerberus

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main

private fun <T> dispatcherEntry(task: AsyncTaskBuilder<T>, dispatcher: CoroutineDispatcher): Job {
    return GlobalScope.launch(dispatcher) {
        try {
            task.semaphore.run {
                val value = task.onBackground(this)

                withContext(Dispatchers.Main) {
                    task.onSuccess?.invoke(value)
                    task.onFinalize?.invoke()
                }
            }
        } catch (err: Exception) {
            if (err is CancellationException) {
                when (task.onCancel) {
                    null -> throw err
                    else -> withContext(Dispatchers.Main) { task.onCancel!!(err) }
                }
            } else {
                when (task.onError) {
                    null -> throw err
                    else -> withContext(Dispatchers.Main) {
                        task.onError!!(err)
                        task.onFinalize?.invoke()
                    }
                }
            }
        }
    }
}

class AsyncTaskBuilder<T>(var semaphore: Semaphore = Semaphore.NonBlocking, var entryPoint: AsyncTaskBuilder<T>.() -> Job) {
    lateinit var onBackground: suspend (CoroutineScope.() -> T)

    /**
     * This property is finalize-handler after than "onBackground".
     * "T" is set from "onBackground" result value.
     */
    var onSuccess: ((value: T) -> Unit)? = null

    /**
     * This property is error-handler to exception throws from "onBackground" method.
     */
    var onError: ((err: Exception) -> Unit)? = null

    /**
     * This property is cancel-handler to "onBackground" method.
     */
    var onCancel: ((err: CancellationException) -> Unit)? = null

    /**
     * This function will  always called.
     */
    var onFinalize: (() -> Unit)? = null
}

/**
 * Start An async task with user-selection dispatcher.
 * "onBackground" function execute from CoroutineDispatcher thread in arguments.
 * "onSuccess", "onError", and "onCancel" functions are execute from Main-Thread.
 */
fun <T> asyncTask(semaphore: Semaphore = Semaphore.NonBlocking, dispatcher: CoroutineDispatcher = Dispatchers.Default, builder: (AsyncTaskBuilder<T>.() -> Unit)): Job {
    return asyncTask(semaphore, { dispatcherEntry(this, dispatcher) }, builder);
}

/**
 * Start An async task with user-selection "EntryPoint" object.
 */
fun <T> asyncTask(semaphore: Semaphore = Semaphore.NonBlocking, entryPoint: AsyncTaskBuilder<T>.() -> Job, builder: (AsyncTaskBuilder<T>.() -> Unit)): Job {
    val context = AsyncTaskBuilder(semaphore, entryPoint)
    builder(context)

    return context.entryPoint(context)
}
