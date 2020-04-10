package com.campoe.android.zycle.adapter.util

import android.util.SparseIntArray
import com.campoe.android.zycle.adapter.composite.CompositeAdapter

internal class AdapterLut {

    private val positionToIndex: SparseIntArray = SparseIntArray()

    fun rebuild(entries: Array<out CompositeAdapter.OffsetAdapterEntry>): Int {
        positionToIndex.clear()
        var offset = 0
        val adapterCount = entries.size
        for (i in 0 until adapterCount) {
            val entry = entries[i]
            entry.offset = offset
            val adapterItemCount = entry.adapter.getItemCount()
            if (adapterItemCount > 0) {
                positionToIndex.put(offset, i)
            }
            offset += adapterItemCount
        }
        return offset
    }

    fun rebuild(entries: List<CompositeAdapter.OffsetAdapterEntry>): Int {
        positionToIndex.clear()
        var offset = 0
        val adapterCount = entries.size
        for (i in 0 until adapterCount) {
            val entry = entries[i]
            entry.offset = offset
            val adapterItemCount = entry.adapter.getItemCount()
            if (adapterItemCount > 0) {
                positionToIndex.put(offset, i)
            }
            offset += adapterItemCount
        }
        return offset
    }

    fun getAdapterIndex(parentPosition: Int): Int {
        val index = positionToIndex.indexOfKey(parentPosition)
        if (index >= 0) {
            return positionToIndex.valueAt(index)
        }
        return positionToIndex.valueAt(-index - 2)
    }

}