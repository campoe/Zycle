package com.campoe.android.zycle.viewholder

import android.view.DragEvent
import android.view.MotionEvent

interface IViewHolder<E : Any> {

    fun bind(
        item: E,
        listeners: List<ViewHolderListener<IViewHolder<E>>>
    )

    fun bind(
        item: E,
        vararg listeners: ViewHolderListener<IViewHolder<E>>
    )

    fun listen(
        listener: ViewHolderListener<IViewHolder<E>>
    )

    fun unbind()

    interface ViewHolderListener<VH : IViewHolder<*>>

    interface OnItemClickListener<VH : IViewHolder<*>>
        : ViewHolderListener<VH> {
        fun onItemClick(holder: VH)
    }

    interface OnItemLongClickListener<VH : IViewHolder<*>> :
        ViewHolderListener<VH> {
        fun onItemLongClick(holder: VH): Boolean
    }

    interface OnItemTouchListener<VH : IViewHolder<*>> :
        ViewHolderListener<VH> {
        fun onItemTouch(holder: VH, e: MotionEvent): Boolean
    }

    interface OnItemDragListener<VH : IViewHolder<*>> :
        ViewHolderListener<VH> {
        fun onItemDrag(holder: VH, e: DragEvent): Boolean
    }

    interface OnItemHoverListener<VH : IViewHolder<*>> :
        ViewHolderListener<VH> {
        fun onItemHover(holder: VH, e: MotionEvent): Boolean
    }

}