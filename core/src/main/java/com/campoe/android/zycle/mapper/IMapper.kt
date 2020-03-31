package com.campoe.android.zycle.mapper

import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.`typealias`.OnBindListener
import com.campoe.android.zycle.`typealias`.OnItemClickListener
import com.campoe.android.zycle.`typealias`.OnItemLongClickListener
import com.campoe.android.zycle.`typealias`.OnRecycleListener
import com.campoe.android.zycle.`typealias`.StableIdProvider
import com.campoe.android.zycle.viewholder.IViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

internal interface IMapper<E : Any, VH : ViewHolder<E>> {

    fun stableId(f: StableIdProvider<E>): Mapper<E, VH>
    fun onBind(f: OnBindListener<VH>): Mapper<E, VH>
    fun onRecycle(f: OnRecycleListener<VH>): Mapper<E, VH>
    fun onClick(f: OnItemClickListener<VH>): Mapper<E, VH>
    fun onLongClick(f: OnItemLongClickListener<VH>): Mapper<E, VH>
    fun onTouch(f: OnItemTouchListener<VH>): Mapper<E, VH>

}