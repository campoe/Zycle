package com.coenvk.android.zycle.adapter.observer

import androidx.recyclerview.widget.RecyclerView

internal class ForwardingRecyclerObserver(private val adapter: RecyclerView.Adapter<*>) :
    AdapterDataObserver() {

    override fun onChanged() = adapter.notifyDataSetChanged()
    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) =
        adapter.notifyItemRangeChanged(positionStart, itemCount)

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload)

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) =
        adapter.notifyItemRangeInserted(positionStart, itemCount)

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) =
        adapter.notifyItemRangeRemoved(positionStart, itemCount)

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        for (i in itemCount - 1 downTo 0) adapter.notifyItemMoved(
            fromPosition + i,
            toPosition + i
        )
    }

}