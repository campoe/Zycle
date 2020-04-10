package com.campoe.android.zycle.adapter.util

import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.composite.CompositeAdapter
import com.campoe.android.zycle.ktx.cast

class AdapterPositionLookup(adapter: Adapter) {

    private val compositeAdapter: CompositeAdapter? by lazy {
        adapter.cast<CompositeAdapter>()
    }

    fun innerPosition(adapterPosition: Int): Int =
        compositeAdapter?.innerPosition(adapterPosition) ?: adapterPosition

}