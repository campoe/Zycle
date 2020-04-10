package com.campoe.android.zycle.filter

interface Filter<T> {

    fun apply(applyTo: T): Boolean

}