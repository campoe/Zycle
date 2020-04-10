package com.campoe.android.zycle.eventhook.click

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.eventhook.EventHook

abstract class LongClickEvent<E : Any, VH : RecyclerView.ViewHolder> : EventHook<E, VH>() {

    abstract fun onLongClick(holder: VH, item: E, position: Int): Boolean

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnLongClickListener { onLongClick(holder, item, position) }
    }

}