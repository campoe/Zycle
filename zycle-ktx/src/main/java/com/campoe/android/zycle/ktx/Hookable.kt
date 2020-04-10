package com.campoe.android.zycle.ktx

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.eventhook.click.ClickEvent
import com.campoe.android.zycle.eventhook.click.LongClickEvent
import com.campoe.android.zycle.eventhook.drag.DragEvent
import com.campoe.android.zycle.eventhook.touch.TouchEvent

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

fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.onDrag(f: VH.(item: E, position: Int, e: android.view.DragEvent) -> Boolean) =
    apply {
        eventHooks.add(
            object : DragEvent<E, VH>() {
                override fun onDrag(
                    holder: VH,
                    item: E,
                    position: Int,
                    e: android.view.DragEvent
                ): Boolean {
                    return onDrag(holder, item, position, e)
                }
            }
        )
    }
