package com.campoe.android.zycle.`typealias`

import androidx.recyclerview.widget.RecyclerView

internal typealias OnAttachListener = (recyclerView: RecyclerView) -> Unit
internal typealias OnDetachListener = (recyclerView: RecyclerView) -> Unit
internal typealias OnBindListener<E, VH> = VH.(item: E, position: Int) -> Unit
internal typealias OnRecycleListener<E, VH> = VH.(item: E, position: Int) -> Unit