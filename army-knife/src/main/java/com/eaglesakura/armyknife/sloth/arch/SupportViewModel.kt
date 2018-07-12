package com.eaglesakura.armyknife.sloth.arch

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.eaglesakura.android.garnet.Garnet
import com.eaglesakura.android.garnet.Provider
import com.eaglesakura.armyknife.android.extensions.subscribe
import com.eaglesakura.armyknife.sloth.viewmodel.StatefulViewModel
import com.eaglesakura.armyknife.sloth.viewmodel.ViewModelOwner
import kotlin.reflect.KClass


/**
 * アプリ内で利用するViewModelの基底クラス.
 *
 * このViewModelは特定のActivity/Fragmentインスタンスに紐づくのを避ける.
 * Activity/Fragmentとはライフサイクルが異なることに留意して設計する.
 */
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

    /**
     * Garnet経由でDIを行うトップレベル関数.
     * 遅延初期化したほうが他のインスタンスの兼ね合いとして都合が良いため、Utilを提供する.
     */
    protected inline fun <T : Provider<*>, reified T2> provideLazy(provider: KClass<T>) = lazy {
        Garnet.factory(provider.java)
                .depend(Context::class.java, this.context)
                .instance(T2::class.java)
    }
}