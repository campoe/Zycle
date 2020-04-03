package com.campoe.android.zycle.eventhook

import com.campoe.android.zycle.viewholder.ViewHolder

internal interface IEventHook<E : Any, VH : ViewHolder> {

    fun attach(holder: VH, item: E, position: Int)

}