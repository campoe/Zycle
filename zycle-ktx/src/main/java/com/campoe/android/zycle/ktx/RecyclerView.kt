package com.campoe.android.zycle.ktx

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

operator fun RecyclerView.Adapter<*>.plusAssign(observer: RecyclerView.AdapterDataObserver) =
    registerAdapterDataObserver(observer)

operator fun RecyclerView.Adapter<*>.minusAssign(observer: RecyclerView.AdapterDataObserver) =
    unregisterAdapterDataObserver(observer)

operator fun RecyclerView.plusAssign(decor: RecyclerView.ItemDecoration) =
    addItemDecoration(decor)

operator fun RecyclerView.minusAssign(decor: RecyclerView.ItemDecoration) =
    removeItemDecoration(decor)

operator fun RecyclerView.plusAssign(itemTouchHelper: ItemTouchHelper) =
    itemTouchHelper.attachToRecyclerView(this)

operator fun RecyclerView.plusAssign(itemAnimator: RecyclerView.ItemAnimator) {
    this.itemAnimator = itemAnimator
}

operator fun RecyclerView.plusAssign(view: View) {
    addView(view)
}