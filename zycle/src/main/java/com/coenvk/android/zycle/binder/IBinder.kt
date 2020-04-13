package com.coenvk.android.zycle.binder

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.`typealias`.OnBindListener
import com.coenvk.android.zycle.`typealias`.OnCreateListener
import com.coenvk.android.zycle.`typealias`.OnRecycleListener
import com.coenvk.android.zycle.`typealias`.StableIdProvider
import com.coenvk.android.zycle.util.Builder

internal interface IBinder<E : Any, VH : RecyclerView.ViewHolder> {

    @get:IdRes
    val viewType: Int

    @get:LayoutRes
    val layoutRes: Int

    fun hasStableIds(): Boolean
    fun getItemId(item: E, position: Int): Long

    fun onCreate(holder: VH)
    fun onBind(holder: VH, item: E)
    fun onRecycle(holder: VH)

    interface IBuilder<E : Any, VH : RecyclerView.ViewHolder> :
        Builder<Binder<E, VH>> {

        @get:IdRes
        val viewType: Int

        @get:LayoutRes
        val layoutRes: Int

        fun stableId(f: StableIdProvider<E>): Binder.Builder<E>

        fun onCreate(f: OnCreateListener<VH>): Binder.Builder<E>
        fun onBind(f: OnBindListener<E, VH>): Binder.Builder<E>
        fun onRecycle(f: OnRecycleListener<VH>): Binder.Builder<E>

    }

}