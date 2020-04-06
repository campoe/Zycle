package com.campoe.android.zycle.click.ktx

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.click.eventhook.ClickEvent
import com.campoe.android.zycle.click.eventhook.LongClickEvent
import com.campoe.android.zycle.click.eventhook.TouchEvent
import com.campoe.android.zycle.eventhook.Hookable

fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.onClick(f: VH.(item: E, position: Int) -> Unit) =
    apply {
        eventHooks.add(object : ClickEvent<E, VH>() {
            override fun onClick(holder: VH, item: E, position: Int) = f(holder, item, position)
        })
    }

fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.onLongClick(f: VH.(item: E, position: Int) -> Boolean) =
    apply {
        eventHooks.add(object : LongClickEvent<E, VH>() {
            override fun onLongClick(holder: VH, item: E, position: Int): Boolean =
                f(holder, item, position)
        })
    }

fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.onTouch(f: VH.(item: E, position: Int, e: MotionEvent) -> Boolean) =
    apply {
        eventHooks.add(object : TouchEvent<E, VH>() {
            override fun onTouch(holder: VH, item: E, position: Int, e: MotionEvent): Boolean =
                f(holder, item, position, e)
        })
    }