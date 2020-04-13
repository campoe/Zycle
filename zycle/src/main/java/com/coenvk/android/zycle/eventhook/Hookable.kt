package com.coenvk.android.zycle.eventhook

import androidx.recyclerview.widget.RecyclerView

interface Hookable<E : Any, VH : RecyclerView.ViewHolder> {

    val eventHooks: MutableCollection<EventHook<E, VH>>

}