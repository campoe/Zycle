package com.campoe.android.zycle.observablelist

interface IObservableList<E : Any> {

    fun addCallback(callback: IObservableListCallback<ObservableList<E>>)

    fun removeCallback(callback: IObservableListCallback<ObservableList<E>>)

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