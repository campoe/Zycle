package com.campoe.android.zycle.click.eventhook

import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.viewholder.ViewHolder

abstract class LongClickEvent<E : Any, VH : ViewHolder> : EventHook<E, VH>() {

    abstract fun onLongClick(holder: VH, item: E, position: Int): Boolean

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnLongClickListener { onLongClick(holder, item, position) }
    }

}