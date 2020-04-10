package com.campoe.android.zycle.eventhook

import androidx.recyclerview.widget.RecyclerView

abstract class EventHook<E : Any, VH : RecyclerView.ViewHolder> : IEventHook<E, VH>