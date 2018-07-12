package com.eaglesakura.armyknife.sloth.arch

import androidx.lifecycle.Lifecycle


/**
 * This is optional interface for LifecycleDataStore.
 * If this interface implements to data object, Call "onDestroy()" method on lifecycle destroy time.
 */
interface LifecycleData {
    /**
     * onDestroy object.
     */
    fun onDestroy(owner: Lifecycle) {}
}