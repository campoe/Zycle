package com.campoe.android.zycle.drag.ktx

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.drag.DragListener
import com.campoe.android.zycle.drag.eventhook.DragEvent
import com.campoe.android.zycle.drag.extension.DragDropExtension
import com.campoe.android.zycle.eventhook.Hookable

fun <E : Any> Adapter.Builder<E>.onDragDrop(f: (fromPosition: Int, toPosition: Int, isDropped: Boolean) -> Unit) =
    extendWith(
        DragDropExtension(
            object : DragListener {
                override fun onDragged(fromIndex: Int, toIndex: Int) {
                    f(fromIndex, toIndex, false)
                }

                override fun onDropped(fromIndex: Int, toIndex: Int) {
                    f(fromIndex, toIndex, true)
                }
            }
        )
    )

fun <E : Any, VH : RecyclerView.ViewHolder> Hookable<E, VH>.onDrag(f: VH.(item: E, position: Int, e: android.view.DragEvent) -> Boolean) =
    onEvent(
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