package com.campoe.android.zycle.drag.extension

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.drag.DragCallback
import com.campoe.android.zycle.drag.DragListener
import com.campoe.android.zycle.drag.ktx.onDrag
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.extension.AdapterExtension

class DragDropExtension(dragListener: DragListener) : AdapterExtension() {

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(DragCallback(dragListener))

    override fun onAttach(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
        val adapter = recyclerView.adapter
        if (adapter is Hookable<*, *>) {
            adapter.onDrag { _, _, e ->
                if (android.view.DragEvent.ACTION_DRAG_STARTED == e.action) {
                    itemTouchHelper.startDrag(this)
                    true
                } else false
            }
        }
    }

    override fun onDetach(recyclerView: RecyclerView) {
    }

}