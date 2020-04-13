package com.coenvk.android.zycle.ktx

import androidx.recyclerview.widget.DiffUtil
import com.coenvk.android.zycle.adapter.Adapter
import com.coenvk.android.zycle.diff.AdapterListUpdateCallback

fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(adapter: Adapter) {
    dispatchUpdatesTo(AdapterListUpdateCallback(adapter))
}