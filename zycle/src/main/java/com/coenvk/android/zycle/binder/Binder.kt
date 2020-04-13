package com.coenvk.android.zycle.binder

import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.ZycleDsl
import com.coenvk.android.zycle.`typealias`.OnBindListener
import com.coenvk.android.zycle.`typealias`.OnCreateListener
import com.coenvk.android.zycle.`typealias`.OnRecycleListener
import com.coenvk.android.zycle.`typealias`.StableIdProvider
import com.coenvk.android.zycle.eventhook.EventHook
import com.coenvk.android.zycle.eventhook.Hookable
import com.coenvk.android.zycle.ktx.requireCast
import com.coenvk.android.zycle.mapper.Mapper
import com.coenvk.android.zycle.mapper.mapperOf

internal typealias RecyclerBinder<E> = Binder<E, RecyclerView.ViewHolder>

abstract class Binder<E : Any, VH : RecyclerView.ViewHolder> : IBinder<E, VH> {

    override val viewType: Int
        get() = javaClass.name.hashCode()

    fun asMapper(): Mapper<E> = mapperOf(this.requireCast())

    override fun hasStableIds(): Boolean = false
    override fun getItemId(item: E, position: Int): Long = RecyclerView.NO_ID

    override fun onCreate(holder: VH) = Unit
    override fun onBind(holder: VH, item: E) = Unit
    override fun onRecycle(holder: VH) = Unit

    @ZycleDsl
    class Builder<E : Any> internal constructor(
        override val layoutRes: Int,
        override val viewType: Int = RecyclerView.INVALID_TYPE
    ) :
        IBinder.IBuilder<E, RecyclerView.ViewHolder>, Hookable<E, RecyclerView.ViewHolder> {

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

        override fun build(): RecyclerBinder<E> {
            return object : RecyclerBinder<E>() {
                override val viewType: Int
                    get() = if (this@Builder.viewType == RecyclerView.INVALID_TYPE) super.viewType else this@Builder.viewType

                override fun hasStableIds(): Boolean = stableIdProvider != null

                override fun getItemId(item: E, position: Int): Long {
                    return stableIdProvider?.invoke(item, position)
                        ?: super.getItemId(item, position)
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