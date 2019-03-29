package com.eaglesakura.armyknife.widgets.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

/**
 * Basic array-adapter.
 *
 * Supported drop-down, selection.
 */
class SupportArrayAdapter<T>(private val context: Context, @LayoutRes private val itemLayoutId: Int, @LayoutRes private val dropdownViewId: Int) :
    BaseAdapter() {
    /**
     * Convert T to title.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var titleMap: (index: Int, item: T?) -> String = { index, item -> item?.toString() ?: "$index" }

    /**
     * DropDown view settings.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var dropdownViewMap: (index: Int, item: T?, view: View) -> Unit = { index, item, view ->
        (view.findViewById(android.R.id.text1) as? TextView)?.text = titleMap(index, item)
    }

    /**
     * Selection view settings.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var selectionViewMap: (index: Int, item: T?, view: View) -> Unit = { index, item, view ->
        (view.findViewById(android.R.id.text1) as? TextView)?.text = titleMap(index, item)
    }

    /**
     * all items.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val items: MutableList<T?> = mutableListOf()

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(dropdownViewId, parent, false)!!

        @Suppress("UNCHECKED_CAST")
        dropdownViewMap(position, getItem(position), view)
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(itemLayoutId, parent, false)
        selectionViewMap(position, getItem(position), view)
        return view
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): T? {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}