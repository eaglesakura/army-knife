package com.eaglesakura.firearm.adapter

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.ArrayRes


@Deprecated("Replace to com.eaglesakura.armyknife.adapters.SpinnerAdapterBuilder")
class SpinnerAdapterBuilder<T>(private var context: Context, var spinner: Spinner) {

    @Suppress("MemberVisibilityCanBePrivate")
    val items: MutableList<T?> = mutableListOf()

    @Suppress("MemberVisibilityCanBePrivate")
    var dropdownViewMap: ((index: Int, item: T?, view: View) -> Unit)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    var selectionViewMap: ((index: Int, item: T?, view: View) -> Unit)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    var titleMap: ((index: Int, item: T?) -> String)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    var selectedAction: ((index: Int, item: T?) -> Unit)? = null

    private var selected: Int = 0

    @Suppress("unused")
    fun selection(matcher: (index: Int, item: T?) -> Boolean) {
        selected = 0
        items.forEachIndexed { index, item ->
            if (matcher(index, item)) {
                selected = index
                return
            }
        }
    }

    fun selection(obj: T): SpinnerAdapterBuilder<T> {
        selected = Math.max(items.indexOf(obj), 0)
        return this
    }

    /**
     * Adapterのみを生成する
     */
    private fun buildAdapter(): SupportArrayAdapter<T> {
        val adapter = SupportArrayAdapter<T>(context, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item)
        dropdownViewMap?.let { adapter.dropdownViewMap = it }
        selectionViewMap?.let { adapter.selectionViewMap = it }
        titleMap?.let { adapter.titleMap = it }
        adapter.items.addAll(items)
        return adapter
    }

    fun build(): SpinnerAdapterBuilder<T> {
        spinner.adapter = buildAdapter()
        spinner.setSelection(selected)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                @Suppress("UNCHECKED_CAST")
                selectedAction?.invoke(position, spinner.adapter.getItem(position) as T?)
            }
        }
        return this
    }

    companion object {
        fun <T> from(spinner: Spinner): SpinnerAdapterBuilder<T> {
            return from(spinner.context, spinner)
        }

        fun <T> from(context: Context, spinner: Spinner): SpinnerAdapterBuilder<T> {
            return SpinnerAdapterBuilder(context, spinner)
        }


        fun fromStringArray(spinner: Spinner, context: Context, @ArrayRes resId: Int): SpinnerAdapterBuilder<String> {
            return SpinnerAdapterBuilder<String>(context, spinner).also {
                it.items.addAll(context.resources.getStringArray(resId).asList())
                it.titleMap = { _, item -> item!! }
            }
        }
    }
}