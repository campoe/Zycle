package com.campoe.android.zycle.util

internal inline fun <reified T> Any.cast(): T? {
    return if (this is T) {
        this
    } else null
}