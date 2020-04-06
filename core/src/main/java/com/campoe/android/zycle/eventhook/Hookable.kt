package com.campoe.android.zycle.eventhook

import androidx.recyclerview.widget.RecyclerView

interface Hookable<E : Any, VH : RecyclerView.ViewHolder> {

    val eventHooks: MutableList<EventHook<E, VH>>

    fun onEvent(eventHook: EventHook<E, VH>): Hookable<E, VH> =
        apply { eventHooks.add(eventHook) }

}

internal fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.attachEvents(
    holder: VH,
    item: E,
    position: Int
) {
    eventHooks.forEach { it.attach(holder, item, position) }
}