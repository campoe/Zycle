package com.coenvk.android.zycle.observablelist

import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import com.coenvk.android.zycle.adapter.Adapter
import com.coenvk.android.zycle.diff.DiffUtilCallback
import com.coenvk.android.zycle.ktx.dispatchUpdatesTo
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*
import java.util.function.Predicate
import java.util.function.UnaryOperator

class ObservableList<E : Any>(private val items: MutableList<E> = mutableListOf()) :
    IObservableList<E>,
    MutableList<E> by items {

    constructor(items: Array<out E>) : this(items.toMutableList())
    constructor(items: Iterable<E>) : this(items.toMutableList())

    private val callbacks: MutableList<ObservableListCallback<E>> =
        mutableListOf()

    override fun swap(i: Int, j: Int) {
        Collections.swap(items, i, j)
        callbacks.forEach {
            it.onItemRangeChanged(this, i, 1)
            it.onItemRangeChanged(this, j, 1)
        }
    }

    override fun move(fromIndex: Int, toIndex: Int) {
        val item = items.removeAt(fromIndex)
        items.add(toIndex, item)
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

    override fun removeAll(
        elements: Collection<E>
    ): Boolean {
        if (elements.size == 1) return remove(elements.single())
        val oldItems = this.toMutableList()
        val ret = items.removeAll(elements)
        if (ret) DiffUtil.calculateDiff(
            DiffUtilCallback(
                oldItems,
                this
            )
        )
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
        if (ret) DiffUtil.calculateDiff(
            DiffUtilCallback(
                oldItems,
                this
            )
        )
            .dispatchUpdatesTo(callbacks)
        return ret
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun removeIf(filter: Predicate<in E>): Boolean {
        var removed = false
        val each: MutableListIterator<E> = listIterator()
        while (each.hasNext()) {
            val i = each.nextIndex()
            if (filter.test(each.next())) {
                each.remove()
                removed = true
                callbacks.forEach { it.onItemRangeRemoved(this, i, 1) }
            }
        }
        return removed
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun replaceAll(operator: UnaryOperator<E>) {
        val li = listIterator()
        while (li.hasNext()) {
            li.set(operator.apply(li.next()))
        }
        callbacks.forEach { it.onChanged(this) }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        return ObservableList(items.subList(fromIndex, toIndex))
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

    class ObservableListCallback<E : Any>(adapter: Adapter) :
        IObservableList.IObservableListCallback<ObservableList<E>> {

        private val reference: Reference<Adapter> = WeakReference(adapter)
        internal val adapter: Adapter?
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