package com.campoe.android.zycle.click.eventhook

import android.view.MotionEvent
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.viewholder.ViewHolder

abstract class TouchEvent<E : Any, VH : ViewHolder> : EventHook<E, VH>() {

    abstract fun onTouch(holder: VH, item: E, position: Int, e: MotionEvent): Boolean

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnTouchListener { _, e -> onTouch(holder, item, position, e) }
    }

}