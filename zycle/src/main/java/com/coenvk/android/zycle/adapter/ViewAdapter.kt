package com.coenvk.android.zycle.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.ktx.inflate
import com.coenvk.android.zycle.viewholder.ViewHolder

internal sealed class ViewAdapter : Adapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(layoutInflater!!, viewType))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun getLayoutRes(viewType: Int): Int {
        return viewType
    }

}

private class MultiViewAdapter(private vararg val layouts: Int) : ViewAdapter() {

    constructor(layouts: List<Int>) : this(*layouts.toIntArray())

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun getItemViewType(position: Int): Int {
        return layouts[position]
    }

}

private class SingleViewAdapter(@LayoutRes private val layoutRes: Int) : ViewAdapter() {

    override fun getItemCount(): Int = 1

    override fun getItemViewType(position: Int): Int = layoutRes

}

internal fun viewAdapterOf(vararg layouts: Int): Adapter {
    return when {
        layouts.isEmpty() -> EmptyAdapter
        layouts.size == 1 -> SingleViewAdapter(layouts[0])
        else -> MultiViewAdapter(*layouts)
    }
}

internal fun viewAdapterOf(layouts: List<Int>): Adapter {
    return when {
        layouts.isEmpty() -> EmptyAdapter
        layouts.size == 1 -> SingleViewAdapter(layouts[0])
        else -> MultiViewAdapter(layouts)
    }
}