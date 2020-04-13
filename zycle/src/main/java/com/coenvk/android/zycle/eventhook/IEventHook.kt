package com.coenvk.android.zycle.eventhook

import androidx.recyclerview.widget.RecyclerView

internal interface IEventHook<E : Any, VH : RecyclerView.ViewHolder> {

    fun attach(holder: VH, item: E, position: Int)

}