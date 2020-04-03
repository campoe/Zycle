package com.campoe.android.zycle.click.extension

import android.view.MotionEvent
import com.campoe.android.zycle.click.eventhook.ClickEvent
import com.campoe.android.zycle.click.eventhook.LongClickEvent
import com.campoe.android.zycle.click.eventhook.TouchEvent
import com.campoe.android.zycle.eventhook.EventListener
import com.campoe.android.zycle.viewholder.ViewHolder

fun <E : Any, VH : ViewHolder> EventListener<E, VH>.onClick(f: VH.(item: E, position: Int) -> Unit) =
    apply {
        onEvent(object : ClickEvent<E, VH>() {
            override fun onClick(holder: VH, item: E, position: Int) = f(holder, item, position)
        })
    }

fun <E : Any, VH : ViewHolder> EventListener<E, VH>.onLongClick(f: VH.(item: E, position: Int) -> Boolean) =
    apply {
        onEvent(object : LongClickEvent<E, VH>() {
            override fun onLongClick(holder: VH, item: E, position: Int): Boolean =
                f(holder, item, position)
        })
    }

fun <E : Any, VH : ViewHolder> EventListener<E, VH>.onTouch(f: VH.(item: E, position: Int, e: MotionEvent) -> Boolean) =
    apply {
        onEvent(object : TouchEvent<E, VH>() {
            override fun onTouch(holder: VH, item: E, position: Int, e: MotionEvent): Boolean =
                f(holder, item, position, e)
        })
    }