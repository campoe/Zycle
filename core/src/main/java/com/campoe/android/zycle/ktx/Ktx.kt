package com.campoe.android.zycle.ktx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver
import com.campoe.android.zycle.binder.RecyclerBinder
import com.campoe.android.zycle.diff.AdapterListUpdateCallback
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.observablelist.ObservableList

operator fun RecyclerView.Adapter<*>.plusAssign(observer: RecyclerView.AdapterDataObserver) =
    registerAdapterDataObserver(observer)

operator fun RecyclerView.Adapter<*>.minusAssign(observer: RecyclerView.AdapterDataObserver) =
    unregisterAdapterDataObserver(observer)

operator fun RecyclerView.plusAssign(decor: RecyclerView.ItemDecoration) =
    addItemDecoration(decor)

operator fun RecyclerView.minusAssign(decor: RecyclerView.ItemDecoration) =
    removeItemDecoration(decor)

operator fun Adapter.plusAssign(observer: AdapterDataObserver) =
    registerAdapterDataObserver(observer)

operator fun Adapter.minusAssign(observer: AdapterDataObserver) =
    unregisterAdapterDataObserver(observer)

operator fun Adapter.plus(adapter: Adapter) =
    append(adapter)

internal inline fun <reified T> Any.cast(): T? {
    return this as? T
}

internal inline fun <reified T> Any.requireCast(): T {
    return this as T
}

fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(adapter: Adapter) {
    dispatchUpdatesTo(AdapterListUpdateCallback(adapter))
}

internal fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(callbacks: List<ObservableList.ObservableListCallback<E>>) {
    callbacks.forEach { callback ->
        callback.adapter?.let { adapter -> dispatchUpdatesTo<Adapter>(adapter) }
    }
}

fun ViewGroup?.inflate(
    layoutInflater: LayoutInflater,
    @LayoutRes layoutRes: Int
): View = layoutInflater.inflate(layoutRes, this, false)

fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int
): View = inflate(context, layoutRes)

fun ViewGroup?.inflate(
    context: Context,
    @LayoutRes layoutRes: Int
): View = inflate(LayoutInflater.from(context), layoutRes)

fun RecyclerBinder<*>.inflate(
    layoutInflater: LayoutInflater,
    parent: ViewGroup? = null
): View = layoutInflater.inflate(layoutRes, parent, false)

fun RecyclerBinder<*>.inflate(
    parent: ViewGroup
): View = inflate(parent.context, parent)

fun RecyclerBinder<*>.inflate(
    context: Context,
    parent: ViewGroup? = null
): View = inflate(LayoutInflater.from(context), parent)

internal fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.attachEvents(
    holder: VH,
    item: E,
    position: Int
) {
    eventHooks.forEach { it.attach(holder, item, position) }
}