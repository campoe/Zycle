package com.campoe.android.zycle.viewholder

import androidx.recyclerview.widget.RecyclerView

internal interface IViewHolder {

    val itemPosition: Int

    fun onCreateViewHolder(recyclerView: RecyclerView)
    fun onBindViewHolder()
    fun onViewRecycled(recyclerView: RecyclerView)

}