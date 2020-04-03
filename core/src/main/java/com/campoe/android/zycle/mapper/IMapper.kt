package com.campoe.android.zycle.mapper

import com.campoe.android.zycle.`typealias`.OnBindListener
import com.campoe.android.zycle.`typealias`.OnRecycleListener
import com.campoe.android.zycle.`typealias`.StableIdProvider
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.eventhook.EventListener
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface IMapper<E : Any, VH : ViewHolder> : EventListener<E, VH> {

    fun stableId(f: StableIdProvider<E>): Mapper<E, VH>
    fun onBind(f: OnBindListener<E, VH>): Mapper<E, VH>
    fun onRecycle(f: OnRecycleListener<E, VH>): Mapper<E, VH>

}