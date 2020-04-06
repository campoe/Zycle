package com.campoe.android.zycle.drag

import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

internal class DragCallback(private val dragListener: DragListener) :
    ItemTouchHelper.Callback() {

    private var fromPosition: Int = RecyclerView.NO_POSITION
    private var toPosition: Int = RecyclerView.NO_POSITION

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return current.adapterPosition != target.adapterPosition
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (fromPosition == RecyclerView.NO_POSITION) {
            fromPosition = viewHolder.adapterPosition
        }
        toPosition = target.adapterPosition
        val fromPosition = fromPosition
        val toPosition = toPosition
        dragListener.onDragged(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val fromPosition = fromPosition
        val toPosition = toPosition
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION && fromPosition != toPosition) {
            dragListener.onDropped(fromPosition, toPosition)
        }
        this.toPosition = RecyclerView.NO_POSITION
        this.fromPosition = this.toPosition
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

}