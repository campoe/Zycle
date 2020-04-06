package com.campoe.android.zycle.viewholder

import androidx.recyclerview.widget.RecyclerView

internal interface IViewHolder {

    fun onCreateViewHolder(recyclerView: RecyclerView)
    fun onBindViewHolder()
    fun onViewRecycled(recyclerView: RecyclerView)

}