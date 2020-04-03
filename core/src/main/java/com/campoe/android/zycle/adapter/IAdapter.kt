package com.campoe.android.zycle.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.eventhook.EventListener
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface IAdapter<E : Any, VH : ViewHolder> : EventListener<E, VH> {

    fun attach(view: RecyclerView): Adapter<E, VH>
    fun detach(): Adapter<E, VH>

    fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes layoutRes: Int,
        block: MapperBlock<T, VH>? = null
    ): Adapter<E, VH>

    fun layout(f: LayoutProvider<E>): Adapter<E, VH>
    fun stableId(f: StableIdProvider<E>): Adapter<E, VH>

    fun onBind(f: OnBindListener<E, VH>): Adapter<E, VH>
    fun onRecycle(f: OnRecycleListener<E, VH>): Adapter<E, VH>

}