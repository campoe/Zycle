package com.campoe.android.zycle.pagination.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.viewholder.ViewHolder

class PaginationAdapter<E : Any, VH : ViewHolder<E>>(
    items: MutableList<E> = mutableListOf(),
    @LayoutRes private val progressLayoutRes: Int
) : Adapter<E, VH>(items) {

    private var displayProgressBar: Boolean = true

    override fun onBindViewHolder(holder: VH, position: Int) {
        super.onBindViewHolder(holder, position)
        if (isProgressViewHolder(position)) {
            (holder.itemView.rootView as ViewGroup).addView(ContentLoadingProgressBar(holder.itemView.context))
        }
    }

    override fun getItemCount(): Int =
        if (displayProgressBar) super.getItemCount() + 1 else super.getItemCount()

    override fun getItemViewType(position: Int): Int =
        if (isProgressViewHolder(position)) progressLayoutRes else super.getItemViewType(
            position
        )

    override fun getItemId(position: Int): Long =
        if (isProgressViewHolder(position)) RecyclerView.NO_ID else super.getItemId(position)

    private fun isProgressViewHolder(position: Int): Boolean {
        return displayProgressBar && position == itemCount - 1
    }

    fun displayProgressBar(displayProgressBar: Boolean) {
        if (this.displayProgressBar != displayProgressBar) {
            this.displayProgressBar = displayProgressBar
            if (displayProgressBar) notifyItemInserted(itemCount - 1)
            else notifyItemRemoved(itemCount)
        }
    }

}