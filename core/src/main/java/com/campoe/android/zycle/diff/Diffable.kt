package com.campoe.android.zycle.diff

interface Diffable<in E> {

    fun areItemsTheSame(other: E): Boolean

    fun areContentsTheSame(other: E): Boolean

}