package com.coenvk.android.zycle.eventhook.touch

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.eventhook.EventHook

abstract class TouchEvent<E : Any, VH : RecyclerView.ViewHolder> : EventHook<E, VH>() {

    abstract fun onTouch(holder: VH, item: E, position: Int, e: MotionEvent): Boolean

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnTouchListener { _, e -> onTouch(holder, item, position, e) }
    }

}