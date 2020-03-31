package com.campoe.android.zycle.extension

import androidx.recyclerview.widget.DiffUtil
import com.campoe.android.zycle.observablelist.ObservableList

internal inline fun <reified T> Any.cast(): T? {
    return this as? T
}

internal fun <E : Any> DiffUtil.DiffResult.dispatchUpdatesTo(callbacks: List<ObservableList.ObservableListCallback<E, *>>) {
    callbacks.forEach { callback ->
        callback.adapter?.let { adapter -> dispatchUpdatesTo(adapter) }
    }
}