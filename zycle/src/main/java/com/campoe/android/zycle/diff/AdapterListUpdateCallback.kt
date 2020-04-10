package com.campoe.android.zycle.diff

import androidx.recyclerview.widget.ListUpdateCallback
import com.campoe.android.zycle.adapter.Adapter

class AdapterListUpdateCallback(private val adapter: Adapter) : ListUpdateCallback {

    override fun onChanged(position: Int, count: Int, payload: Any?) =
        adapter.notifyItemRangeChanged(position, count, payload)

    override fun onMoved(fromPosition: Int, toPosition: Int) =
        adapter.notifyItemMoved(fromPosition, toPosition)

    override fun onInserted(position: Int, count: Int) =
        adapter.notifyItemRangeInserted(position, count)

    override fun onRemoved(position: Int, count: Int) =
        adapter.notifyItemRangeRemoved(position, count)

}