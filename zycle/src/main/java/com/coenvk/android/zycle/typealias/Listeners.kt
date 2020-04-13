package com.coenvk.android.zycle.`typealias`

import androidx.recyclerview.widget.RecyclerView

internal typealias OnAttachListener = (recyclerView: RecyclerView) -> Unit
internal typealias OnDetachListener = (recyclerView: RecyclerView) -> Unit
internal typealias OnCreateListener<VH> = VH.() -> Unit
internal typealias OnBindListener<E, VH> = VH.(item: E) -> Unit
internal typealias OnRecycleListener<VH> = VH.() -> Unit