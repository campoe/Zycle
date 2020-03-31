package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.campoe.android.zycle.`typealias`.*
import com.campoe.android.zycle.viewholder.ViewHolder

class AdapterOverride<E : Any, VH : ViewHolder<E>>(internal val items: MutableList<E> = mutableListOf()) :
    IAdapterOverride<E, VH> {

    private var onBindListener: OnBindListener<VH>? = null
    private var onRecycleListener: OnRecycleListener<VH>? = null

    private var onClickListener: OnItemClickListener<VH>? = null
    private var onLongClickListener: OnItemLongClickListener<VH>? = null
    private var onTouchListener: OnItemTouchListener<VH>? = null

    @Suppress("UNCHECKED_CAST")
    override fun instantiateViewHolder(
        adapter: Adapter<E, VH>,
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): VH {
        val itemView = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder<E>(itemView) as VH
    }

    override fun getItemCount(adapter: Adapter<E, VH>): Int {
        return items.size
    }

    override fun onBindViewHolder(
        adapter: Adapter<E, VH>,
        holder: VH,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onViewRecycled(
        adapter: Adapter<E, VH>,
        holder: VH
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(
        adapter: Adapter<E, VH>,
        position: Int
    ): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(
        adapter: Adapter<E, VH>,
        position: Int
    ): Long {
        TODO("Not yet implemented")
    }

    protected fun get(position: Int): E {
        return items[position]
    }

    private fun doOnClick(holder: VH) {
        onClickListener?.invoke(holder)
    }

    private fun doOnLongClick(holder: VH): Boolean {
        return onLongClickListener?.invoke(holder) ?: false
    }

    private fun doOnTouch(holder: VH, e: MotionEvent): Boolean {
        return onTouchListener?.invoke(holder, e) ?: false
    }

}