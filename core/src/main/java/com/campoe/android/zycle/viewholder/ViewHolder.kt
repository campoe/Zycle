package com.campoe.android.zycle.viewholder

import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl

@ZycleDsl
class ViewHolder<E : Any>(itemView: View) : RecyclerView.ViewHolder(itemView), IViewHolder<E> {

    var item: E? = null
        protected set

    @CallSuper
    override fun bind(
        item: E,
        listeners: List<IViewHolder.ViewHolderListener<IViewHolder<E>>>
    ) {
        this.item = item
        listeners.forEach {
            listen(it)
        }
    }

    @CallSuper
    override fun bind(
        item: E,
        vararg listeners: IViewHolder.ViewHolderListener<IViewHolder<E>>
    ) {
        this.item = item
        listeners.forEach {
            listen(it)
        }
    }

    @CallSuper
    override fun unbind() {
        itemView.setOnClickListener(null)
        itemView.setOnLongClickListener(null)
        itemView.setOnTouchListener(null)
        item = null
    }

    override fun listen(listener: IViewHolder.ViewHolderListener<IViewHolder<E>>) {
        when (listener) {
            is IViewHolder.OnItemClickListener ->
                this.itemView.setOnClickListener {
                    listener.onItemClick(this)
                }
            is IViewHolder.OnItemLongClickListener ->
                this.itemView.setOnLongClickListener {
                    listener.onItemLongClick(this)
                }
            is IViewHolder.OnItemTouchListener ->
                this.itemView.setOnTouchListener { _, e ->
                    listener.onItemTouch(this, e)
                }
            is IViewHolder.OnItemDragListener ->
                this.itemView.setOnDragListener { _, e ->
                    listener.onItemDrag(this, e)
                }
            is IViewHolder.OnItemHoverListener ->
                this.itemView.setOnHoverListener { _, e ->
                    listener.onItemHover(this, e)
                }
        }
    }

    inline fun onItemClick(crossinline f: (IViewHolder<E>) -> Unit) =
        apply {
            listen(
                object : IViewHolder.OnItemClickListener<IViewHolder<E>> {
                    override fun onItemClick(holder: IViewHolder<E>) = f(holder)
                }
            )
        }

    inline fun onItemLongClick(crossinline f: (IViewHolder<E>) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemLongClickListener<IViewHolder<E>> {
                    override fun onItemLongClick(holder: IViewHolder<E>): Boolean = f(holder)
                }
            )
        }

    inline fun onItemTouch(crossinline f: (IViewHolder<E>, MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemTouchListener<IViewHolder<E>> {
                    override fun onItemTouch(holder: IViewHolder<E>, e: MotionEvent): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemDrag(crossinline f: (IViewHolder<E>, DragEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemDragListener<IViewHolder<E>> {
                    override fun onItemDrag(
                        holder: IViewHolder<E>,
                        e: DragEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

    inline fun onItemHover(crossinline f: (IViewHolder<E>, MotionEvent) -> Boolean) =
        apply {
            listen(
                object : IViewHolder.OnItemHoverListener<IViewHolder<E>> {
                    override fun onItemHover(
                        holder: IViewHolder<E>,
                        e: MotionEvent
                    ): Boolean =
                        f(holder, e)
                }
            )
        }

}