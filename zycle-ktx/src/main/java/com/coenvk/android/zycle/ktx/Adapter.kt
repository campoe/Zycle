package com.coenvk.android.zycle.ktx

import androidx.annotation.LayoutRes
import com.coenvk.android.zycle.adapter.Adapter
import com.coenvk.android.zycle.adapter.observer.AdapterDataObserver

operator fun Adapter.plusAssign(observer: AdapterDataObserver) =
    registerAdapterDataObserver(observer)

operator fun Adapter.minusAssign(observer: AdapterDataObserver) =
    unregisterAdapterDataObserver(observer)

operator fun Adapter.plus(adapter: Adapter) =
    append(adapter)

operator fun Adapter.plus(@LayoutRes layoutRes: Int) =
    append(layoutRes)

operator fun Adapter.plus(adapters: Array<out Adapter>) =
    append(*adapters)

operator fun Adapter.plus(layouts: IntArray) =
    append(*layouts)