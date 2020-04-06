package com.campoe.android.zycle.observablelist

import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.ktx.dispatchUpdatesTo
import com.campoe.android.zycle.util.DiffUtilCallback
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

class ObservableList<E : Any>(private val items: MutableList<E> = mutableListOf()) :
    IObservableList<E>,
    MutableList<E> by items {

    constructor(items: Array<out E>) : this(items.toMutableList())

    private val callbacks: MutableList<ObservableListCallback<E>> =
        mutableListOf()

    override fun move(fromIndex: Int, toIndex: Int) {
        Collections.swap(items, fromIndex, toIndex)
        callbacks.forEach { it.onItemRangeMoved(this, fromIndex, toIndex, 1) }
    }

    override fun add(element: E): Boolean {
        val ret = items.add(element)
        if (ret) callbacks.forEach { it.onItemRangeInserted(this, size - 1, 1) }
        return ret
    }

    override fun add(index: Int, element: E) {
        items.add(index, element)
        callbacks.forEach { it.onItemRangeInserted(this, index, 1) }
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val ret = items.addAll(index, elements)
        if (ret) callbacks.forEach { it.onItemRangeInserted(this, index, elements.size) }
        return ret
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val startRange = size
        val ret = items.addAll(elements)
        if (ret) callbacks.forEach { it.onItemRangeInserted(this, startRange, elements.size) }
        return ret
    }

    override fun remove(element: E): Boolean {
        val index = indexOf(element)
        val ret = items.remove(element)
        if (ret) callbacks.forEach { it.onItemRangeRemoved(this, index, 1) }
        return ret
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        if (elements.size == 1) return remove(elements.single())
        val oldItems = this.toMutableList()
        val ret = items.removeAll(elements)
        if (ret) DiffUtil.calculateDiff(DiffUtilCallback(oldItems, this))
            .dispatchUpdatesTo(callbacks)
        return ret
    }

    override fun removeAt(index: Int): E {
        val item = items.removeAt(index)
        callbacks.forEach { it.onItemRangeRemoved(this, index, 1) }
        return item
    }

    override fun set(index: Int, element: E): E {
        val item = items.set(index, element)
        callbacks.forEach { it.onItemRangeChanged(this, index, 1) }
        return item
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val oldItems = this.toMutableList()
        val ret = items.retainAll(elements)
        if (ret) DiffUtil.calculateDiff(DiffUtilCallback(oldItems, this))
            .dispatchUpdatesTo(callbacks)
        return ret
    }

    override fun clear() {
        items.clear()
        callbacks.forEach { it.onChanged(this) }
    }

    override fun addCallback(callback: ObservableListCallback<E>) {
        callbacks.add(callback)
    }

    override fun removeCallback(callback: ObservableListCallback<E>) {
        callbacks.remove(callback)
    }

    override fun removeAllCallbacks() {
        callbacks.clear()
    }

    class ObservableListCallback<E : Any>(adapter: Adapter<E>) :
        IObservableList.IObservableListCallback<ObservableList<E>> {

        private val reference: Reference<Adapter<E>> = WeakReference(adapter)
        internal val adapter: Adapter<E>?
            get() {
                if (Thread.currentThread() == Looper.getMainLooper().thread) return reference.get()
                throw IllegalStateException("You must modify the ObservableList on the main thread.")
            }

        override fun onChanged(sender: ObservableList<E>) {
            adapter?.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(
            sender: ObservableList<E>,
            positionStart: Int,
            itemCount: Int
        ) {
            adapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(
            sender: ObservableList<E>,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
            adapter?.run {
                for (i in 0 until itemCount) notifyItemMoved(
                    fromPosition + i,
                    toPosition + i
                )
            }
        }

        override fun onItemRangeInserted(
            sender: ObservableList<E>,
            positionStart: Int,
            itemCount: Int
        ) {
            adapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(
            sender: ObservableList<E>,
            positionStart: Int,
            itemCount: Int
        ) {
            adapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

    }

}

fun <E : Any> observableListOf(): ObservableList<E> = ObservableList()
fun <E : Any> observableListOf(vararg elements: E): ObservableList<E> =
    ObservableList(elements)

internal fun <E : Any> Array<out E>.toObservableList(): ObservableList<E> = ObservableList(this)