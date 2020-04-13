package com.coenvk.android.zycle.eventhook.swipe

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.ZycleDsl
import com.coenvk.android.zycle.ktx.cast
import com.coenvk.android.zycle.viewholder.ViewHolder
import kotlin.math.abs

class SwipeCallback(
    private val actionLeft: SwipeAction? = null,
    private val actionRight: SwipeAction? = null
) :
    ItemTouchHelper.Callback() {

    private val swipeDirs: Int by lazy {
        var swipeDirs: Int = actionLeft?.let { ItemTouchHelper.LEFT } ?: 0
        actionRight?.also { swipeDirs = swipeDirs or ItemTouchHelper.RIGHT }
        swipeDirs
    }

    var isEnabled: Boolean = true

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun isLongPressDragEnabled(): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = isEnabled

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val holder = viewHolder.cast<ViewHolder>()
        val swipeDirs = if (holder?.isSwipeable() == true) {
            this.swipeDirs
        } else 0
        return makeMovementFlags(0, swipeDirs)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder.adapterPosition == RecyclerView.NO_POSITION) return
        if (viewHolder.cast<ViewHolder>()?.isSwipeable() != true) return

        val position = viewHolder.adapterPosition
        if (position == RecyclerView.NO_POSITION) return
        val itemView = viewHolder.itemView

        val backgroundPaint = Paint()
        val dpDensity = recyclerView.context.resources.displayMetrics.density

        if (abs(dX) <= abs(dY)) return
        if (dX > 0 && actionRight != null) {
            backgroundPaint.color = actionRight.background
            c.drawRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + dX,
                itemView.bottom.toFloat(),
                backgroundPaint
            )

            var textMargin = 0f

            val drawable = actionRight.drawable
            val textHolder = actionRight.textHolder
            if (drawable != null) {
                val iconMargin = (itemView.height - drawable.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + drawable.intrinsicHeight

                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + drawable.intrinsicWidth
                drawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                drawable.draw(c)

                if (!textHolder?.text.isNullOrEmpty()) {
                    textMargin = iconRight + dpDensity * 12
                }
            }

            if (!textHolder?.text.isNullOrEmpty()) {
                val textBounds = Rect()
                val textPaint = textHolder!!.paint
                val text = textHolder.text
                textPaint.getTextBounds(text, 0, text.length, textBounds)
                if (textMargin == 0f) textMargin =
                    ((itemView.height - textBounds.height()) / 2).toFloat()
                c.drawText(
                    text,
                    textMargin,
                    itemView.top + itemView.height / 2 + textBounds.height() / 2f,
                    textPaint
                )
            }
        } else if (dX < 0 && actionLeft != null) {
            backgroundPaint.color = actionLeft.background
            c.drawRect(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                backgroundPaint
            )

            var textMargin = 0f

            val drawable = actionLeft.drawable
            val textHolder = actionLeft.textHolder
            if (drawable != null) {
                val iconMargin = (itemView.height - drawable.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + drawable.intrinsicHeight

                val iconRight = itemView.right - iconMargin
                val iconLeft = iconRight - drawable.intrinsicWidth
                drawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                drawable.draw(c)

                if (!textHolder?.text.isNullOrEmpty()) {
                    textMargin = iconLeft - dpDensity * 12
                }
            }

            if (!textHolder?.text.isNullOrEmpty()) {
                val textBounds = Rect()
                val textPaint = textHolder!!.paint
                val text = textHolder.text
                textPaint.getTextBounds(text, 0, text.length, textBounds)
                if (textMargin == 0f) textMargin =
                    itemView.measuredWidth - ((itemView.height - textBounds.height()) / 2).toFloat()
                c.drawText(
                    text,
                    textMargin - textBounds.width(),
                    itemView.top + itemView.height / 2 + textBounds.height() / 2f,
                    textPaint
                )
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (position == RecyclerView.NO_POSITION) return
        if (direction == ItemTouchHelper.LEFT) {
            actionLeft?.listener?.onSwiped(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            actionRight?.listener?.onSwiped(position)
        }
    }

    @ZycleDsl
    class Builder :
        com.coenvk.android.zycle.util.Builder<SwipeCallback> {

        private var actionLeft: SwipeAction? = null
        private var actionRight: SwipeAction? = null

        fun left(block: SwipeAction.Builder.() -> Unit) =
            apply {
                actionLeft = SwipeAction.Builder().apply(block).build()
            }

        fun right(block: SwipeAction.Builder.() -> Unit) =
            apply {
                actionRight = SwipeAction.Builder().apply(block).build()
            }

        override fun build(): SwipeCallback {
            return SwipeCallback(
                actionLeft,
                actionRight
            )
        }

    }

}