package com.campoe.android.zycle.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ViewHolder(
    itemView: View
) : IViewHolder, RecyclerView.ViewHolder(itemView) {

    override fun isDraggable(): Boolean = false
    override fun isSwipeable(): Boolean = false

}