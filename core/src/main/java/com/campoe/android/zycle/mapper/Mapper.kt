package com.campoe.android.zycle.mapper

import android.view.DragEvent
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.viewholder.IViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface TypeErasedMapper

@ZycleDsl
class Mapper<E : Any, VH : ViewHolder<E>>(
    @LayoutRes
    internal val layoutRes: Int
) :
    IMapper<E, VH>,
    TypeErasedMapper {

    internal var stableIdProvider: ((item: E, position: Int) -> Long)? = null
    internal var onBindListener: (VH.() -> Unit)? = null
    internal var onRecycleListener: (VH.() -> Unit)? = null
    internal var listeners: MutableList<IViewHolder.ViewHolderListener<VH>> = mutableListOf()

    override fun stableId(f: (item: E, position: Int) -> Long): IMapper<E, VH> =
        apply { stableIdProvider = f }

    override fun onBind(f: VH.() -> Unit): IMapper<E, VH> =
        apply { onBindListener = f }

    override fun onRecycle(f: VH.() -> Unit): IMapper<E, VH> =
        apply { onRecycleListener = f }

    override fun listen(listener: IViewHolder.ViewHolderListener<VH>): IMapper<E, VH> =
        apply { listeners.add(listener) }

    inline fun onItemClick(crossinline f: VH.() -> Unit) =
        apply {
            listen(
                object : IViewHolder.OnItemClickListener<VH> {
                    override fun onItemClick(holder: VH) = f(holder)
                }
            )
        }

    inline fun onItemLongClick(crossinline f: VH.() -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemLongClickListener<VH> {
                    override fun onItemLongClick(holder: VH): Boolean = f(holder)
                }
            )
        }

    inline fun onItemTouch(crossinline f: VH.(event: MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemTouchListener<VH> {
                    override fun onItemTouch(holder: VH, e: MotionEvent): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemDrag(crossinline f: VH.(event: DragEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemDragListener<VH> {
                    override fun onItemDrag(
                        holder: VH,
                        e: DragEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemHover(crossinline f: VH.(event: MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemHoverListener<VH> {
                    override fun onItemHover(
                        holder: VH,
                        e: MotionEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

}

fun <T : Any> mapper(
    @LayoutRes layoutRes: Int
): Mapper<T, ViewHolder<T>> =
    Mapper(layoutRes)