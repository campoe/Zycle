package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.campoe.android.zycle.viewholder.ViewHolder

interface IAdapterOverride<E : Any, VH : ViewHolder<E>> {

    fun instantiateViewHolder(
        adapter: Adapter<E, VH>,
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): VH

    fun getItemCount(adapter: Adapter<E, VH>): Int

    fun onBindViewHolder(
        adapter: Adapter<E, VH>,
        holder: VH,
        position: Int
    )

    fun onViewRecycled(adapter: Adapter<E, VH>, holder: VH)

    fun getItemViewType(adapter: Adapter<E, VH>, position: Int): Int

    fun getItemId(adapter: Adapter<E, VH>, position: Int): Long

}