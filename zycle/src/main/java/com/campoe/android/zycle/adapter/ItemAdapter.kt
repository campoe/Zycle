package com.campoe.android.zycle.adapter

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.eventhook.drag.Draggable
import com.campoe.android.zycle.eventhook.swipe.Swipeable
import com.campoe.android.zycle.ktx.attachEvents
import com.campoe.android.zycle.ktx.cast
import com.campoe.android.zycle.ktx.inflate
import com.campoe.android.zycle.mapper.Mapper
import com.campoe.android.zycle.mapper.mapperOf
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.viewholder.ViewHolder
import java.lang.ref.WeakReference
import java.util.*

internal sealed class ItemAdapter<E : Any> : Adapter()

private class MultiItemAdapter<E : Any>(
    private val items: List<E>,
    private val mapper: Mapper<E>
) : ItemAdapter<E>() {

    @Suppress("LeakingThis")
    private var callback: ObservableList.ObservableListCallback<E> =
        ObservableList.ObservableListCallback(this)
    private val viewTypes: WeakHashMap<Int, WeakReference<RecyclerBinder<E>>> = WeakHashMap()

    constructor(items: List<E>, binder: RecyclerBinder<E>) : this(
        items,
        mapperOf(binder)
    )

    constructor(items: Array<out E>, mapper: Mapper<E>) : this(items.toList(), mapper)
    constructor(items: Array<out E>, binder: RecyclerBinder<E>) : this(
        items.toList(),
        mapperOf(binder)
    )

    override fun getItemCount(): Int {
        return items.size
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (items is ObservableList) items.addCallback(callback)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (items is ObservableList) items.removeCallback(callback)
    }

    private fun getItem(position: Int): E? {
        if (position < 0 || position >= items.size) return null
        return items[position]
    }

    override fun getLayoutRes(viewType: Int): Int {
        return viewTypes[viewType]?.get()?.layoutRes ?: throw RuntimeException()
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        val binder = mapper.getBinder(item.javaClass)
        val viewType = binder.viewType
        if (!viewTypes.containsKey(viewType)) {
            viewTypes[viewType] = WeakReference(binder)
        }
        return viewType
    }

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return mapper.getBinder(item.javaClass).getItemId(item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binder = viewTypes[viewType]?.get() ?: throw RuntimeException()
        return object : ViewHolder(binder.inflate(layoutInflater!!, parent)) {
            override fun isDraggable(): Boolean = binder.cast<Draggable>()?.isDraggable ?: false
            override fun isSwipeable(): Boolean = binder.cast<Swipeable>()?.isSwipeable ?: false
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: throw RuntimeException()
        val binder = mapper.getBinder(item.javaClass)
        binder.cast<Hookable<E, RecyclerView.ViewHolder>>()?.attachEvents(holder, item, position)
        binder.onBind(holder, item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition == RecyclerView.NO_POSITION) return
        val binder = viewTypes[holder.itemViewType]?.get()
        binder?.onRecycle(holder)
    }

}

private class SingleItemAdapter<E : Any>(
    private val item: E,
    private val binder: RecyclerBinder<E>
) : ItemAdapter<E>() {

    override fun getItemCount(): Int = 1

    override fun getLayoutRes(viewType: Int): Int = binder.layoutRes

    override fun getItemViewType(position: Int): Int = binder.viewType

    override fun getItemId(position: Int): Long = binder.getItemId(item, position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : ViewHolder(binder.inflate(layoutInflater!!, parent)) {
            override fun isDraggable(): Boolean = binder.cast<Draggable>()?.isDraggable ?: false
            override fun isSwipeable(): Boolean = binder.cast<Swipeable>()?.isSwipeable ?: false
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binder.cast<Hookable<E, RecyclerView.ViewHolder>>()?.attachEvents(holder, item, position)
        binder.onBind(holder, item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition == RecyclerView.NO_POSITION) return
        binder.onRecycle(holder)
    }

}

internal fun <E : Any> itemAdapterOf(vararg items: E, mapper: Mapper<E>): Adapter {
    return when {
        items.isEmpty() -> EmptyAdapter
        items.size == 1 -> SingleItemAdapter(items[0], mapper.getBinder(items[0].javaClass))
        else -> MultiItemAdapter(items, mapper)
    }
}

internal fun <E : Any> itemAdapterOf(vararg items: E, binder: RecyclerBinder<E>): Adapter {
    return when {
        items.isEmpty() -> EmptyAdapter
        items.size == 1 -> SingleItemAdapter(items[0], binder)
        else -> MultiItemAdapter(items, binder)
    }
}

internal fun <E : Any> itemAdapterOf(items: List<E>, mapper: Mapper<E>): Adapter {
    if (items is ObservableList) return MultiItemAdapter(items, mapper)
    return when {
        items.isEmpty() -> EmptyAdapter
        items.size == 1 -> SingleItemAdapter(items[0], mapper.getBinder(items[0].javaClass))
        else -> MultiItemAdapter(items, mapper)
    }
}

internal fun <E : Any> itemAdapterOf(items: List<E>, binder: RecyclerBinder<E>): Adapter {
    if (items is ObservableList) return MultiItemAdapter(items, binder)
    return when {
        items.isEmpty() -> EmptyAdapter
        items.size == 1 -> SingleItemAdapter(items[0], binder)
        else -> MultiItemAdapter(items, binder)
    }
}