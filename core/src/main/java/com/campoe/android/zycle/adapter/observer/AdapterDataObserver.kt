package com.campoe.android.zycle.adapter.observer

abstract class AdapterDataObserver {

    open fun onChanged() = Unit
    open fun onItemRangeChanged(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        onItemRangeChanged(positionStart, itemCount)

    open fun onItemRangeInserted(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = Unit

}