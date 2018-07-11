package com.eaglesakura.ktx.oneshotlivedata

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * This class exclude repeat and some-data callback from LiveData<T>.
 * Example)
 *
 * If use "LiveData<T>.observe()" method for Snackbar data.
 * Activity instance receive old Snackbar-data in "onResume()" method.
 *
 * @see newOneshotObserver
 * @see newOneshotObserverWithForeground
 */
open class OneshotLiveData<T> : LiveData<DataState<T>>() {

    final override fun setValue(value: DataState<T>?) {
        throw IllegalAccessError("Not supported!! Should use setValue(data:T) method")
    }

    final override fun postValue(value: DataState<T>?) {
        throw IllegalAccessError("Not supported!! Should use postValue(data:T) method")
    }

    @UiThread
    open fun setOneshot(data: T) {
        val value = DataState(data)
        value.state = DataState.State.Running
        try {
            super.setValue(value)
        } finally {
            value.state = DataState.State.Done
        }
    }

    @WorkerThread
    open fun postOneshot(data: T) {
        launch(UI) { setOneshot(data) }
    }

}
