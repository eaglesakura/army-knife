package com.eaglesakura.firearm.viewmodel

import android.os.Bundle


/**
 * ViewModel with State.
 */
interface StatefulViewModel {

    /**
     * Save this state or do-nothing.
     * If do nothing in this method then should returns null-object.
     */
    fun save(): Bundle?

    /**
     * Restore this state from bundle.
     * If "save()" method returns null, then not call this method.
     */
    fun restore(bundle: Bundle)

    /**
     * attach to ViewModelOwner event.
     * it call from "onCreate" or "onCreateView" event or such events.
     */
    fun attach(owner: ViewModelOwner)
}