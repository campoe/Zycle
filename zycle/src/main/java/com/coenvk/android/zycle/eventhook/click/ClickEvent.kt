package com.coenvk.android.zycle.eventhook.click

import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.eventhook.EventHook

abstract class ClickEvent<E : Any, VH : RecyclerView.ViewHolder> : EventHook<E, VH>() {

    abstract fun onClick(holder: VH, item: E, position: Int)

    final override fun attach(holder: VH, item: E, position: Int) {
        holder.itemView.setOnClickListener { onClick(holder, item, position) }
    }

}