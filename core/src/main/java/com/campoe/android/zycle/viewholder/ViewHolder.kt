package com.campoe.android.zycle.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ViewHolder(
    itemView: View
) :
    RecyclerView.ViewHolder(itemView),
    IViewHolder {

    override fun onCreateViewHolder(recyclerView: RecyclerView) = Unit
    override fun onBindViewHolder() = Unit
    override fun onViewRecycled(recyclerView: RecyclerView) = Unit

}