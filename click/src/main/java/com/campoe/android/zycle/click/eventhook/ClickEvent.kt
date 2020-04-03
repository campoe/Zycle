package com.campoe.android.zycle.click.eventhook

import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.viewholder.ViewHolder

abstract class ClickEvent<E : Any, VH : ViewHolder> : EventHook<E, VH>() {

    abstract fun onClick(holder: VH, item: E, position: Int)

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnClickListener { onClick(holder, item, position) }
    }

}