package com.campoe.android.zycle.mapper

import androidx.annotation.LayoutRes
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface TypeErasedMapper

@ZycleDsl
open class Mapper<E : Any, VH : ViewHolder<E>>(
    @LayoutRes
    internal val layoutRes: Int
) :
    IMapper<E, VH>,
    TypeErasedMapper {

    internal var stableIdProvider: StableIdProvider<E>? = null
        private set

    internal var internalOnBindListener: OnBindListener<VH>? = null
        private set
    protected open val onBindListener: OnBindListener<VH>?
        get() = internalOnBindListener
    internal var onRecycleListener: OnRecycleListener<VH>? = null
        private set

    internal var onClickListener: OnItemClickListener<VH>? = null
    internal var onLongClickListener: OnItemLongClickListener<VH>? = null
    internal var onTouchListener: OnItemTouchListener<VH>? = null

    override fun stableId(f: StableIdProvider<E>): Mapper<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: OnBindListener<VH>): Mapper<E, VH> =
        apply { internalOnBindListener = f }

    override fun onRecycle(f: OnRecycleListener<VH>): Mapper<E, VH> =
        apply { onRecycleListener = f }

    override fun onClick(f: OnItemClickListener<VH>) =
        apply { onClickListener = f }

    override fun onLongClick(f: OnItemLongClickListener<VH>) =
        apply { onLongClickListener = f }

    override fun onTouch(f: OnItemTouchListener<VH>) =
        apply { onTouchListener = f }

}

internal fun <T : Any> mapper(
    @LayoutRes layoutRes: Int
): Mapper<T, ViewHolder<T>> =
    Mapper(layoutRes)