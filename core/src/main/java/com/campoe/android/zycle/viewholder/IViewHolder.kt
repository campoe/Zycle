package com.campoe.android.zycle.viewholder

import android.view.MotionEvent
import com.campoe.android.zycle.`typealias`.OnItemClickListener
import com.campoe.android.zycle.`typealias`.OnItemLongClickListener
import com.campoe.android.zycle.`typealias`.OnItemTouchListener

internal interface IViewHolder<E : Any> {

    fun bind(
        item: E,
        onClick: OnItemClickListener<ViewHolder<E>>? = null,
        onLongClick: OnItemLongClickListener<ViewHolder<E>>? = null,
        onTouch: OnItemTouchListener<ViewHolder<E>>? = null
    )

    fun unbind()

    interface ViewListener<VH : IViewHolder<*>>

    interface OnClickListener<VH : IViewHolder<*>>
        : ViewListener<VH> {
        fun onItemClick(holder: VH)
    }

    interface OnLongClickListener<VH : IViewHolder<*>> :
        ViewListener<VH> {
        fun onItemLongClick(holder: VH): Boolean
    }

    interface OnTouchListener<VH : IViewHolder<*>> :
        ViewListener<VH> {
        fun onItemTouch(holder: VH, e: MotionEvent): Boolean
    }

}