package com.eaglesakura.armyknife.runtime

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Run only one.
 *
 * for Initializer, or such else.
 * Thinking "Lazy" or "OneshotRunner" or "object Class" or such classes.
 *
 * e.g.)
 * val initializer = OneshotRunner<Unit>()
 *
 * fun init(context: Context) {
 *      initializer.run {
 *          val app = context.applicationContext as Application
 *          // do something.
 *      }
 * }
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/army-knife
 */
class OneshotRunner<T> {
    private val lock = ReentrantLock()

    private var done: Boolean = false

    private var result: T? = null

    /**
     * Run block with oneshot.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun oneshot(block: () -> T): T {
        if (done) {
            return result!!
        }

        lock.withLock {
            if (done) {
                return result!!
            }

            result = block()
            done = true
            return result!!
        }
    }

    /**
     * Convert to Runnable.
     */
    fun toRunnable(block: () -> T): Runnable {
        return Runnable { oneshot(block) }
    }
}