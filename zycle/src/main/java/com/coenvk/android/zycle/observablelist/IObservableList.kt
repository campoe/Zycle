package com.coenvk.android.zycle.observablelist

internal interface IObservableList<E : Any> {

    fun swap(i: Int, j: Int)

    fun move(fromIndex: Int, toIndex: Int)

    fun addCallback(callback: ObservableList.ObservableListCallback<E>)

    fun removeCallback(callback: ObservableList.ObservableListCallback<E>)

    fun removeAllCallbacks()

    interface IObservableListCallback<T : IObservableList<*>> {

        fun onChanged(sender: T)

        fun onItemRangeChanged(sender: T, positionStart: Int, itemCount: Int)

        fun onItemRangeInserted(sender: T, positionStart: Int, itemCount: Int)

        fun onItemRangeMoved(
            sender: T, fromPosition: Int, toPosition: Int,
            itemCount: Int
        )

        fun onItemRangeRemoved(sender: T, positionStart: Int, itemCount: Int)
    }

}