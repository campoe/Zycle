package com.campoe.android.zycle.ktx

import androidx.recyclerview.widget.DiffUtil
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.diff.AdapterListUpdateCallback

fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(adapter: Adapter) {
    dispatchUpdatesTo(AdapterListUpdateCallback(adapter))
}