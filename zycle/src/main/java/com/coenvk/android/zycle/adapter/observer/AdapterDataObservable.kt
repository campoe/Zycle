package com.coenvk.android.zycle.adapter.observer

import android.database.Observable

internal class AdapterDataObservable : Observable<AdapterDataObserver>() {

    fun hasObservers(): Boolean = mObservers.isNotEmpty()

    fun notifyChanged() {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onChanged()
        }
    }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) =
        notifyItemRangeChanged(positionStart, itemCount, null)

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onItemRangeChanged(positionStart, itemCount, payload)
        }
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onItemRangeInserted(positionStart, itemCount)
        }
    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onItemRangeRemoved(positionStart, itemCount)
        }
    }

    fun notifyItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        for (i in mObservers.indices.reversed()) {
            mObservers[i].onItemRangeMoved(fromPosition, toPosition, itemCount)
        }
    }

}