package com.eaglesakura.armyknife.rx

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * RxJava to LiveData.
 */
internal class RxLiveData<T>(observable: Observable<T>) : LiveData<T>() {

    private var disposable: Disposable? = null

    private val observable: Observable<T> = observable.subscribeOn(AndroidSchedulers.mainThread())

    override fun onActive() {
        super.onActive()
        disposable = observable.subscribe { newValue ->
            value = newValue
        }
    }

    override fun onInactive() {
        disposable?.dispose()
        disposable = null
        super.onInactive()
    }
}