package com.campoe.android.zycle.adapter.observer

import android.database.Observable

internal class AdapterDataObservable : Observable<AdapterDataObserver>() {

    fun hasObservers(): Boolean = mObservers.isNotEmpty()

    fun notifyChanged() = mObservers.reversed().forEach { it.onChanged() }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) =
        notifyItemRangeChanged(positionStart, itemCount, null)

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        mObservers.reversed().forEach { it.onItemRangeChanged(positionStart, itemCount, payload) }

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) =
        mObservers.reversed().forEach { it.onItemRangeInserted(positionStart, itemCount) }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) =
        mObservers.reversed().forEach { it.onItemRangeRemoved(positionStart, itemCount) }

    fun notifyItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) =
        mObservers.reversed().forEach { it.onItemRangeMoved(fromPosition, toPosition, itemCount) }

}