package com.eaglesakura.firearm.rx

import androidx.annotation.CheckResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.eaglesakura.armyknife.android.extensions.delay
import com.eaglesakura.armyknife.rx.toChannel
import com.eaglesakura.armyknife.rx.toLiveData
import com.eaglesakura.armyknife.rx.with
import com.eaglesakura.firearm.event.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Deprecated("Replace package-name to com.eaglesakura.firearm.event")
typealias EventStream = RxStream<Event>

/**
 * Support RxJava functions.
 *
 * RxStream use to event or snackbar-data or such one-shot data.
 */
@Deprecated("Replace package-name to com.eaglesakura.armyknife.android.reactivex.RxStream")
open class RxStream<T> private constructor(
        private val subject: Subject<T>,
        @Suppress("MemberVisibilityCanBePrivate") val observable: Observable<T>,
        private val validator: ((T) -> Boolean)
) {
    constructor(subject: Subject<T>, validator: (T) -> Boolean) : this(subject, subject.observeOn(AndroidSchedulers.mainThread()), validator)

    @Suppress("unused")
    constructor(validator: (T) -> Boolean) : this(PublishSubject.create(), validator)

    /**
     * Post new value.
     * Can run on any-thread.
     */
    open fun next(value: T) {
        if (!validator(value)) {
            throw IllegalArgumentException("Value is invalid[$value]")
        }
        subject.onNext(value)
    }

    /**
     * Make LiveData from Observable in RxJava.
     *
     * LiveData calls "dispose()" method at Inactive event.
     * You should not call Disposable.dispose() method.
     */
    fun toLiveData(): LiveData<T> {
        return observable.toLiveData()
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

    /**
     * Subscribe by reactivex.Observer
     */
    fun subscribe(observer: io.reactivex.Observer<T>) {
        return observable.subscribe(observer)
    }


    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(observer: (value: T) -> Unit): Disposable {
        return observable.subscribe {
            observer(it)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    fun subscribe(lifecycle: Lifecycle, observer: (value: T) -> Unit) {
        subscribe(observer).with(lifecycle)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun subscribe(observer: Observer<T>): Disposable {
        return observable.subscribe {
            observer.onChanged(it)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    fun subscribe(lifecycle: Lifecycle, observer: Observer<T>) {
        subscribe(observer).with(lifecycle)
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<T>) {
        subscribe(owner.lifecycle, observer)
    }

    fun subscribe(owner: LifecycleOwner, observer: (value: T) -> Unit) {
        subscribe(owner.lifecycle, Observer { observer(it) })
    }

    /**
     * RxStream build utils.
     *
     * e.g.) RxStream with Unique value.
     * RxStream.Builder<String>().apply {
     *      observableTransform = { origin ->
     *          origin.distinctUntilChanged()
     *      }
     * }.build()
     */
    class Builder<T> {

        /**
         * Optional subject.
         * If it value is null, then use "PublishSubject" observable.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        var subject: Subject<T>? = null

        /**
         * Optional observableTransform.
         */
        var observableTransform: ((Observable<T>) -> Observable<T>)? = null

        /**
         * Optional validator.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        var validator: ((T) -> Boolean)? = null

        fun build(): RxStream<T> {
            val subject = subject ?: PublishSubject.create()
            val validator = validator ?: { true }
            val observable = if (observableTransform != null) {
                observableTransform!!(subject.observeOn(AndroidSchedulers.mainThread()))
            } else {
                subject.observeOn(AndroidSchedulers.mainThread())
            }

            return RxStream(subject, observable, validator)
        }
    }

    companion object {
        @JvmStatic
        @Suppress("MemberVisibilityCanBePrivate")
        fun <T> create(): RxStream<T> {
            @Suppress("MoveLambdaOutsideParentheses")
            return RxStream(PublishSubject.create<T>(), { true })
        }

        @JvmStatic
        @Suppress("MemberVisibilityCanBePrivate", "unused")
        fun <T> withValidator(validator: (value: T) -> Boolean): RxStream<T> {
            return RxStream(PublishSubject.create<T>(), validator)
        }

        @JvmStatic
        fun <T> newObserverWithForeground(owner: LifecycleOwner, block: (value: T) -> Unit): Observer<T> {
            return newObserverWithContext(Dispatchers.Main) {
                delay(owner.lifecycle, Lifecycle.Event.ON_RESUME)
                block(it)
            }
        }

        @JvmStatic
        fun <T> newObserverWithContext(context: CoroutineContext, block: suspend (value: T) -> Unit): Observer<T> {
            return Observer { value ->
                GlobalScope.launch(context) {
                    block(value)
                }
            }
        }
    }
}