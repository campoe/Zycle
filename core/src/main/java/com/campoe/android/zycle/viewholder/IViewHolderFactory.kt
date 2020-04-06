package com.campoe.android.zycle.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface IViewHolderFactory {

    fun getViewHolder(itemView: View): RecyclerView.ViewHolder // TODO: figure out pattern

}