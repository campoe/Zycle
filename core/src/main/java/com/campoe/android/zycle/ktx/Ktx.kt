package com.campoe.android.zycle.ktx

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.layout.RecyclerItem
import com.campoe.android.zycle.observablelist.ObservableList
import com.campoe.android.zycle.viewholder.ViewHolder

internal inline fun <reified T> Any.cast(): T? {
    return this as? T
}

internal inline fun <reified T> Any.requireCast(): T {
    return this as T
}

internal fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(callbacks: List<ObservableList.ObservableListCallback<E>>) {
    callbacks.forEach { callback ->
        callback.adapter?.let { adapter -> dispatchUpdatesTo(adapter) }
    }
}

internal fun RecyclerItem<*>.inflate(
    layoutInflater: LayoutInflater,
    parent: ViewGroup? = null
): RecyclerView.ViewHolder {
    return ViewHolder(
        layoutInflater.inflate(layoutRes, parent, false)
    )
}

internal fun RecyclerItem<*>.inflate(
    parent: ViewGroup
): RecyclerView.ViewHolder {
    return inflate(parent.context, parent)
}

internal fun RecyclerItem<*>.inflate(
    context: Context,
    parent: ViewGroup? = null
): RecyclerView.ViewHolder {
    return inflate(LayoutInflater.from(context), parent)
}