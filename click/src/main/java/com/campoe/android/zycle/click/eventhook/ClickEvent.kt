package com.campoe.android.zycle.click.eventhook

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.eventhook.EventHook

abstract class ClickEvent<E : Any, VH : RecyclerView.ViewHolder> : EventHook<E, VH>() {

    abstract fun onClick(holder: VH, item: E, position: Int)

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnClickListener { onClick(holder, item, position) }
    }

}