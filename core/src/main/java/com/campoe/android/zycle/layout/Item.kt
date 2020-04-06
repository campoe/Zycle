package com.campoe.android.zycle.layout

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.OnBindListener
import com.campoe.android.zycle.`typealias`.OnCreateListener
import com.campoe.android.zycle.`typealias`.OnRecycleListener
import com.campoe.android.zycle.`typealias`.StableIdProvider
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.eventhook.Hookable

internal typealias RecyclerItem<E> = Item<E, RecyclerView.ViewHolder>

abstract class Item<E : Any, VH : RecyclerView.ViewHolder> : IItem<E, VH>, Hookable<E, VH> {

    override val viewType: Int
        get() = javaClass.name.hashCode()

    override val eventHooks: MutableList<EventHook<E, VH>> = mutableListOf()

    override fun hasStableIds(): Boolean = false
    override fun getStableId(item: E, position: Int): Long = RecyclerView.NO_ID
    override fun onCreate(holder: VH) = Unit
    override fun onBind(holder: VH, item: E) = Unit
    override fun onRecycle(holder: VH) = Unit

    @ZycleDsl
    class Builder<E : Any> internal constructor(
        override val layoutRes: Int,
        override val viewType: Int
    ) :
        IItem.IBuilder<E, RecyclerView.ViewHolder>, Hookable<E, RecyclerView.ViewHolder> {

        private var stableIdProvider: StableIdProvider<E>? = null

        private var onCreateListener: OnCreateListener<RecyclerView.ViewHolder>? = null
        private var onBindListener: OnBindListener<E, RecyclerView.ViewHolder>? = null
        private var onRecycleListener: OnRecycleListener<RecyclerView.ViewHolder>? = null

        override val eventHooks: MutableList<EventHook<E, RecyclerView.ViewHolder>> =
            mutableListOf()

        override fun stableId(f: StableIdProvider<E>): Builder<E> =
            apply { stableIdProvider = f }

        override fun onCreate(f: OnCreateListener<RecyclerView.ViewHolder>): Builder<E> =
            apply { onCreateListener = f }

        override fun onBind(f: OnBindListener<E, RecyclerView.ViewHolder>): Builder<E> =
            apply { onBindListener = f }

        override fun onRecycle(f: OnRecycleListener<RecyclerView.ViewHolder>): Builder<E> =
            apply { onRecycleListener = f }

        override fun build(): RecyclerItem<E> {
            return object : RecyclerItem<E>() {
                override val viewType: Int
                    get() = this@Builder.viewType

                override fun hasStableIds(): Boolean = stableIdProvider != null

                override fun getStableId(item: E, position: Int): Long {
                    return stableIdProvider?.invoke(item, position)
                        ?: super.getStableId(item, position)
                }

                override fun onCreate(
                    holder: RecyclerView.ViewHolder
                ) {
                    onCreateListener?.invoke(holder)
                }

                override fun onBind(
                    holder: RecyclerView.ViewHolder,
                    item: E
                ) {
                    onBindListener?.invoke(holder, item)
                }

                override fun onRecycle(
                    holder: RecyclerView.ViewHolder
                ) {
                    onRecycleListener?.invoke(holder)
                }

                override val layoutRes: Int
                    get() = this@Builder.layoutRes
            }
        }

    }

}