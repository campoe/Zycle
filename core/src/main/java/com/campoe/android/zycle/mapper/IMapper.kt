package com.campoe.android.zycle.mapper

import com.campoe.android.zycle.viewholder.IViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

interface IMapper<E : Any, VH : ViewHolder<E>> {

    fun stableId(f: (item: E, position: Int) -> Long): IMapper<E, VH>
    fun onBind(f: VH.() -> Unit): IMapper<E, VH>
    fun onRecycle(f: VH.() -> Unit): IMapper<E, VH>
    fun listen(listener: IViewHolder.ViewHolderListener<VH>): IMapper<E, VH>

}