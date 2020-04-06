package com.campoe.android.zycle.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.extension.Extendable
import com.campoe.android.zycle.util.Builder

internal interface IAdapter<E : Any> {

    fun attach(view: RecyclerView): Adapter<E>
    fun detach(): Adapter<E>

    fun getLayoutRes(viewType: Int): Int
    fun getStableId(item: E, position: Int): Long

    interface IBuilder<E : Any> : Builder<Adapter<E>> {

        fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes layoutRes: Int,
            block: ItemBuilderBlock<T>? = null
        ): Adapter.Builder<E>

        fun <T : E> map(
            clazz: Class<T>,
            @LayoutRes layoutRes: Int,
            viewType: Int,
            block: ItemBuilderBlock<T>? = null
        ): Adapter.Builder<E>

        fun viewType(f: ViewTypeProvider<E>): Adapter.Builder<E>
        fun layout(f: LayoutProvider): Adapter.Builder<E>
        fun stableId(f: StableIdProvider<E>): Adapter.Builder<E>

        fun onCreate(f: OnCreateListener<RecyclerView.ViewHolder>): Adapter.Builder<E>
        fun onBind(f: OnBindListener<E, RecyclerView.ViewHolder>): Adapter.Builder<E>
        fun onRecycle(f: OnRecycleListener<RecyclerView.ViewHolder>): Adapter.Builder<E>

    }

}