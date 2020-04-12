package com.campoe.android.zycle.eventhook.drag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ktx.cast
import com.campoe.android.zycle.viewholder.ViewHolder

class DragCallback(
    private val listener: OnDragListener,
    private val dragDirs: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) :
    ItemTouchHelper.Callback() {

    var isEnabled: Boolean = true

    private var fromPosition: Int = RecyclerView.NO_POSITION
    private var toPosition: Int = RecyclerView.NO_POSITION

    override fun isLongPressDragEnabled(): Boolean = isEnabled

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return current.itemViewType == target.itemViewType
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val holder = viewHolder.cast<ViewHolder>()
        val dragDirs = if (holder?.isDraggable() == true) {
            this.dragDirs
        } else 0
        return makeMovementFlags(dragDirs, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val holder = viewHolder.cast<ViewHolder>()
        if (holder?.isDraggable() != true) return false
        if (fromPosition == RecyclerView.NO_POSITION) {
            fromPosition = viewHolder.adapterPosition
        }
        toPosition = target.adapterPosition
        listener.onDragged(viewHolder.adapterPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION && fromPosition != toPosition) {
            listener.onDropped(fromPosition, toPosition)
        }
        this.toPosition = RecyclerView.NO_POSITION
        this.fromPosition = this.toPosition
    }

}