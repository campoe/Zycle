package com.coenvk.android.zycle.ktx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.coenvk.android.zycle.binder.RecyclerBinder
import com.coenvk.android.zycle.diff.AdapterListUpdateCallback
import com.coenvk.android.zycle.eventhook.Hookable
import com.coenvk.android.zycle.observablelist.ObservableList

internal inline fun <reified T> Any.cast(): T? {
    return this as? T
}

internal inline fun <reified T> Any.requireCast(): T {
    return this as T
}

internal fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(callbacks: List<ObservableList.ObservableListCallback<E>>) {
    callbacks.forEach { callback ->
        callback.adapter?.let { adapter -> dispatchUpdatesTo(AdapterListUpdateCallback(adapter)) }
    }
}

internal fun ViewGroup?.inflate(
    layoutInflater: LayoutInflater,
    @LayoutRes layoutRes: Int
): View = layoutInflater.inflate(layoutRes, this, false)

internal fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int
): View = inflate(context, layoutRes)

internal fun ViewGroup?.inflate(
    context: Context,
    @LayoutRes layoutRes: Int
): View = inflate(LayoutInflater.from(context), layoutRes)

internal fun RecyclerBinder<*>.inflate(
    layoutInflater: LayoutInflater,
    parent: ViewGroup? = null
): View = layoutInflater.inflate(layoutRes, parent, false)

internal fun RecyclerBinder<*>.inflate(
    parent: ViewGroup
): View = inflate(parent.context, parent)

internal fun RecyclerBinder<*>.inflate(
    context: Context,
    parent: ViewGroup? = null
): View = inflate(LayoutInflater.from(context), parent)

internal fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.attachEvents(
    holder: VH,
    item: E,
    position: Int
) {
    eventHooks.forEach {
        it.attach(holder, item, position)
    }
}

internal fun RecyclerView.LayoutManager.findFirstVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findFirstVisibleItemPosition()
        is GridLayoutManager -> findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> findFirstVisibleItemPositions(null).min() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findFirstCompletelyVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findFirstCompletelyVisibleItemPosition()
        is GridLayoutManager -> findFirstCompletelyVisibleItemPosition()
        is StaggeredGridLayoutManager -> findFirstCompletelyVisibleItemPositions(null).min() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> findLastVisibleItemPositions(null).max() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findLastCompletelyVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastCompletelyVisibleItemPosition()
        is GridLayoutManager -> findLastCompletelyVisibleItemPosition()
        is StaggeredGridLayoutManager -> findLastCompletelyVisibleItemPositions(null).max() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}