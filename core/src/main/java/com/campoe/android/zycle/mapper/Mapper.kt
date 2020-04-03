package com.campoe.android.zycle.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.OnBindListener
import com.campoe.android.zycle.`typealias`.OnRecycleListener
import com.campoe.android.zycle.`typealias`.StableIdProvider
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface TypeErasedMapper

@ZycleDsl
open class Mapper<E : Any, VH : ViewHolder>(
    @LayoutRes
    internal val layoutRes: Int
) :
    IMapper<E, VH>,
    TypeErasedMapper {

    internal var stableIdProvider: StableIdProvider<E>? = null
        private set

    internal var onBindListener: OnBindListener<E, VH>? = null
        private set
    internal var onRecycleListener: OnRecycleListener<E, VH>? = null
        private set

    internal val eventHooks: MutableList<EventHook<E, VH>> = mutableListOf()

    override fun stableId(f: StableIdProvider<E>): Mapper<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: OnBindListener<E, VH>): Mapper<E, VH> =
        apply { onBindListener = f }

    override fun onRecycle(f: OnRecycleListener<E, VH>): Mapper<E, VH> =
        apply { onRecycleListener = f }

    override fun onEvent(eventHook: EventHook<E, VH>): Mapper<E, VH> =
        apply { eventHooks.add(eventHook) }

}

internal fun <T : Any, VH : ViewHolder> mapper(
    @LayoutRes layoutRes: Int
): Mapper<T, VH> =
    Mapper(layoutRes)