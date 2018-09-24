package com.eaglesakura.firearm.arch

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.eaglesakura.armyknife.android.extensions.subscribe
import com.eaglesakura.firearm.viewmodel.StatefulViewModel
import com.eaglesakura.firearm.viewmodel.ViewModelOwner


/**
 * アプリ内で利用するViewModelの基底クラス.
 *
 * このViewModelは特定のActivity/Fragmentインスタンスに紐づくのを避ける.
 * Activity/Fragmentとはライフサイクルが異なることに留意して設計する.
 */
@Deprecated("This interface is not better, Do not use this.")
abstract class SupportViewModel : ViewModel(), StatefulViewModel {

    /**
     * Lifecycle owner.
     */
    var owner: ViewModelOwner? = null

    val context: Context?
        get() = owner?.context

    override fun attach(owner: ViewModelOwner) {
        this.owner?.let {
            onDetach()
        }

        this.owner = owner
        owner.lifecycle.subscribe {
            // Set detach automatic
            if (it == Lifecycle.Event.ON_DESTROY) {
                onDetach()
                this.owner = null
            }
        }
    }

    protected open fun onDetach() {
    }

    override fun save(): Bundle? = null

    override fun restore(bundle: Bundle) {}
}