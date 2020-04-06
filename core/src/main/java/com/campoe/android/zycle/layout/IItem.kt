package com.campoe.android.zycle.layout

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.OnBindListener
import com.campoe.android.zycle.`typealias`.OnCreateListener
import com.campoe.android.zycle.`typealias`.OnRecycleListener
import com.campoe.android.zycle.`typealias`.StableIdProvider
import com.campoe.android.zycle.util.Builder

internal interface IItem<E : Any, VH : RecyclerView.ViewHolder> {

    @get:IdRes
    val viewType: Int

    @get:LayoutRes
    val layoutRes: Int

    fun hasStableIds(): Boolean
    fun getStableId(item: E, position: Int): Long

    fun onCreate(holder: VH)
    fun onBind(holder: VH, item: E)
    fun onRecycle(holder: VH)

    interface IBuilder<E : Any, VH : RecyclerView.ViewHolder> :
        Builder<Item<E, VH>> {

        @get:IdRes
        val viewType: Int

        @get:LayoutRes
        val layoutRes: Int

        fun stableId(f: StableIdProvider<E>): Item.Builder<E>

        fun onCreate(f: OnCreateListener<VH>): Item.Builder<E>
        fun onBind(f: OnBindListener<E, VH>): Item.Builder<E>
        fun onRecycle(f: OnRecycleListener<VH>): Item.Builder<E>

    }

}