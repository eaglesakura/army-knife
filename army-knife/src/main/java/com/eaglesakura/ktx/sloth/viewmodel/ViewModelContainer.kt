package com.eaglesakura.ktx.sloth.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.eaglesakura.ktx.sloth.arch.LifecycleDataStore
import kotlin.reflect.KClass

/**
 * Activity/Fragment内で利用されたViewModelをキャッシュし、必要に応じてSave/Restoreを支援する.
 */
class ViewModelContainer(private val owner: ViewModelOwner) {
    /**
     * オブジェクトで利用されたViewModelのKey
     */
    private val modelKeys: MutableSet<KClass<*>> = HashSet()

    /**
     * ViewModelを取得する
     */
    fun <T : ViewModel> getViewModel(clazz: KClass<T>): T {
        val result = owner.getViewModel(clazz.java)

        if (result is StatefulViewModel && !modelKeys.contains(clazz)) {
            modelKeys.add(clazz)
            result.attach(owner)
        }
        return result
    }

    /**
     * ViewModel状態を保存する.
     */
    fun save(): Bundle {
        val state = Bundle()

        state.putStringArrayList("classes", serializeKeys())
        modelKeys.forEach {
            @Suppress("UNCHECKED_CAST")
            val viewModel = owner.getViewModel(it.java as Class<ViewModel>)
            if (viewModel is StatefulViewModel) {
                viewModel.save()?.let { vmState ->
                    state.putBundle(it.java.name, vmState)
                }
            }
        }
        return state
    }

    /**
     * ViewModel状態を復元する.
     */
    fun restore(state: Bundle) {
        modelKeys.addAll(deserializeKeys(state.getStringArrayList("classes") ?: return))
        modelKeys.forEach {
            @Suppress("UNCHECKED_CAST")
            val viewModel = owner.getViewModel(it.java as Class<ViewModel>)
            if (viewModel is StatefulViewModel) {
                val vmState = state.getBundle(it.java.name)
                if (vmState != null) {
                    viewModel.restore(vmState)
                }
            }
        }
    }

    private fun serializeKeys(): ArrayList<String> {
        val result = ArrayList<String>()
        modelKeys.forEach {
            result.add(it.java.name)
        }
        return result
    }

    private fun deserializeKeys(value: List<String>): Set<KClass<*>> {
        val result = HashSet<KClass<*>>()
        value.forEach {
            result.add(Class.forName(it).kotlin)
        }
        return result
    }

    companion object {
        fun get(activity: FragmentActivity): ViewModelContainer? {
            return LifecycleDataStore.get<ViewModelContainer>(activity.lifecycle, ViewModelContainer::class)
        }

        fun get(fragment: Fragment): ViewModelContainer? {
            return LifecycleDataStore.get<ViewModelContainer>(fragment.lifecycle, ViewModelContainer::class)
        }

        fun getOrCreate(activity: FragmentActivity): ViewModelContainer {
            return LifecycleDataStore.get<ViewModelContainer>(activity.lifecycle, ViewModelContainer::class)
                    ?: run {
                        val container = ViewModelContainer(ViewModelOwner.from(activity))
                        LifecycleDataStore.put(activity.lifecycle, container)
                        return@run container
                    }
        }

        fun getOrCreate(fragment: Fragment): ViewModelContainer {
            return LifecycleDataStore.get<ViewModelContainer>(fragment.lifecycle, ViewModelContainer::class)
                    ?: run {
                        val container = ViewModelContainer(ViewModelOwner.from(fragment))
                        LifecycleDataStore.put(fragment.lifecycle, container)
                        return@run container
                    }
        }
    }
}
