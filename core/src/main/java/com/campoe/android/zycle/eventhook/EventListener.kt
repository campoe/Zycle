package com.campoe.android.zycle.eventhook

import com.campoe.android.zycle.viewholder.ViewHolder

interface EventListener<E : Any, VH : ViewHolder> {

    fun onEvent(eventHook: EventHook<E, VH>): EventListener<E, VH>

}