package com.campoe.android.zycle.ktx

import com.campoe.android.zycle.observablelist.ObservableList

fun <E : Any> observableListOf(): ObservableList<E> = ObservableList()
fun <E : Any> observableListOf(vararg elements: E): ObservableList<E> =
    ObservableList(elements)

fun <E : Any> Array<out E>.toObservableList(): ObservableList<E> = ObservableList(this)
fun <E : Any> Iterable<E>.toObservableList(): ObservableList<E> = ObservableList(this)