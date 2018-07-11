package com.eaglesakura.ktx.junit

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.runBlocking

fun blockingTest(dispatcher: CoroutineDispatcher = CommonPool, action: suspend () -> Unit): Unit = runBlocking(dispatcher) {
    action()
}