package com.eaglesakura.armyknife.sloth.viewmodel

import android.os.Bundle


/**
 * ViewModel with State.
 */
interface StatefulViewModel {

    /**
     * 現在のステートを保存する.
     */
    fun save(): Bundle?

    /**
     * 保存されたステートを復元する
     */
    fun restore(bundle: Bundle)

    /**
     * onAttach
     */
    fun attach(owner: ViewModelOwner)
}