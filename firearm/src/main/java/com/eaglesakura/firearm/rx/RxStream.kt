package com.eaglesakura.firearm.rx

import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.eaglesakura.armyknife.rx.with
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel

/**
 * Support RxJava functions.
 */
open class RxStream<T>(
        subject: Subject<T>,
        validator: ((T) -> Boolean)
) {
    private val subject: Subject<T> = subject

    private val observable: Observable<T> = subject
            .observeOn(AndroidSchedulers.mainThread())

    private val validator: ((T) -> Boolean) = validator

    /**
     * Post new value.
     * Can run on any-thread.
     */
    open fun send(value: T) {
        if (!validator(value)) {
            throw IllegalArgumentException("Value is invalid[$value]")
        }
        subject.onNext(value)
    }

    /**
     * Make Channel from Observable in RxJava.
     * CAUTION!! Make a promise, You will call "Channel.close()" or "Channel.cancel()" method.
     *
     * Channel calls "dispose()" method at Channel.close() or Channel.cancel().
     * You should not call Disposable.dispose() method.
     */
    @CheckResult
    fun toChannel(dispatcher: CoroutineDispatcher = Dispatchers.Main): Channel<T> {
        return observable.toChannel(dispatcher)
    }


    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(observer: (value: T) -> Unit): Disposable {
        return observable.subscribe {
            observer(it)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(lifecycle: Lifecycle, observer: (value: T) -> Unit) {
        subscribe(observer).with(lifecycle)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(observer: Observer<T>): Disposable {
        return observable.subscribe {
            observer.onChanged(it)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(lifecycle: Lifecycle, observer: Observer<T>) {
        subscribe(observer).with(lifecycle)
    }

    companion object {
        @JvmStatic
        @Suppress("MemberVisibilityCanBePrivate")
        fun <T> create(): RxStream<T> {
            @Suppress("MoveLambdaOutsideParentheses")
            return RxStream(PublishSubject.create<T>(), { true })
        }

        @JvmStatic
        @Suppress("MemberVisibilityCanBePrivate")
        fun <T> withValidator(validator: (value: T) -> Boolean): RxStream<T> {
            return RxStream(PublishSubject.create<T>(), validator)
        }
    }
}