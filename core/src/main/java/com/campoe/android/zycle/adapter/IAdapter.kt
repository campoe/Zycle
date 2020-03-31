package com.campoe.android.zycle.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface IAdapter<E : Any, VH : ViewHolder<E>> {

    fun attach(view: RecyclerView): Adapter<E, VH>
    fun detach(): Adapter<E, VH>

    fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes layoutRes: Int,
        block: MapperBlock<T>? = null
    ): Adapter<E, VH>

    fun layout(f: LayoutProvider<E>): Adapter<E, VH>
    fun stableId(f: StableIdProvider<E>): Adapter<E, VH>

    //    fun onAttach(f: (RecyclerView) -> Unit): Adapter<E, VH>
//    fun onDetach(f: (RecyclerView) -> Unit): Adapter<E, VH>
    fun onBind(f: OnBindListener<VH>): Adapter<E, VH>
    fun onRecycle(f: OnRecycleListener<VH>): Adapter<E, VH>
    fun onClick(f: OnItemClickListener<VH>): Adapter<E, VH>
    fun onLongClick(f: OnItemLongClickListener<VH>): Adapter<E, VH>
    fun onTouch(f: OnItemTouchListener<VH>): Adapter<E, VH>

}