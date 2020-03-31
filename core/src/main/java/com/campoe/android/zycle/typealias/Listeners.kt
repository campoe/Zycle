package com.campoe.android.zycle.`typealias`

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

internal typealias OnAttachListener = (RecyclerView) -> Unit
internal typealias OnDetachListener = (RecyclerView) -> Unit
internal typealias OnBindListener<VH> = VH.() -> Unit
internal typealias OnRecycleListener<VH> = VH.() -> Unit
internal typealias OnItemClickListener<VH> = VH.() -> Unit
internal typealias OnItemLongClickListener<VH> = VH.() -> Boolean
internal typealias OnItemTouchListener<VH> = VH.(MotionEvent) -> Boolean