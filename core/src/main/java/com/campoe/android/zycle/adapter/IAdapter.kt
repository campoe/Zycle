package com.campoe.android.zycle.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.viewholder.IViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

interface IAdapter<E : Any, VH : ViewHolder<E>> {

    fun attach(view: RecyclerView)
    fun detach()

    fun <T : E> map(
        clazz: Class<T>,
        @LayoutRes layoutRes: Int,
        block: (Mapper<T, ViewHolder<T>>.() -> Unit)? = null
    ): IAdapter<E, VH>

    fun layout(f: (item: E, position: Int) -> Int): IAdapter<E, VH>
    fun stableId(f: (item: E, position: Int) -> Long): IAdapter<E, VH>

    //    fun onAttach(f: (RecyclerView) -> Unit): IAdapter<E, VH>
//    fun onDetach(f: (RecyclerView) -> Unit): IAdapter<E, VH>
    fun onBind(f: VH.() -> Unit): IAdapter<E, VH>
    fun onRecycle(f: VH.() -> Unit): IAdapter<E, VH>
    fun listen(listener: IViewHolder.ViewHolderListener<VH>): IAdapter<E, VH>

}