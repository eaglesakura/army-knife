package com.eaglesakura.ktx.sloth.arch

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.eaglesakura.android.garnet.Garnet
import com.eaglesakura.android.garnet.Provider
import com.eaglesakura.ktx.android.extensions.subscribe
import com.eaglesakura.ktx.sloth.viewmodel.StatefulViewModel
import com.eaglesakura.ktx.sloth.viewmodel.ViewModelOwner
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

    /**
     * 現在バインドされているContext.
     * 所有者がいないならば、nullを返却する.
     */
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

    /**
     * 現在のステートを保存する.
     */
    override fun save(): Bundle? = null

    /**
     * 保存されたステートを復元する
     */
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