package com.campoe.android.zycle.app.stickyheader

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderDecoration(private val listener: StickyHeaderListener) :
    RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }
        val headerPosition = listener.getHeaderPositionForItem(topChildPosition)
        val currentHeader = getHeaderViewForItem(headerPosition, parent)
        val headerHeight = fixLayoutSizeAndGetHeaderHeight(parent, currentHeader)
        val childInContact =
            getChildInContact(parent, currentHeader.bottom, headerPosition, headerHeight)
        if (childInContact != null && listener.isHeader(
                parent.getChildAdapterPosition(
                    childInContact
                )
            )
        ) {
            moveHeader(c, currentHeader, childInContact)
            return
        }
        drawHeader(c, currentHeader)
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val layoutRes = listener.getHeaderLayoutRes(headerPosition)
        val header = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        listener.bindHeaderData(header, headerPosition)
        return header
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        canvas.translate(0f, 0f)
        header.draw(canvas)
        canvas.restore()
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
        canvas.save()
        canvas.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(canvas)
        canvas.restore()
    }

    private fun getChildInContact(
        parent: RecyclerView,
        contactPoint: Int,
        currentHeaderPosition: Int,
        stickyHeaderHeight: Int
    ): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightTolerance = 0
            val child = parent.getChildAt(i)
            if (currentHeaderPosition != i) {
                if (listener.isHeader(parent.getChildAdapterPosition(child))) {
                    heightTolerance = stickyHeaderHeight - child.height
                }
            }
            val childBottomPosition = if (child.top > 0) {
                child.bottom + heightTolerance
            } else {
                child.bottom
            }
            if (childBottomPosition > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSizeAndGetHeaderHeight(parent: ViewGroup, view: View): Int {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidthSpec, childHeightSpec)
        val headerHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, headerHeight)
        return headerHeight
    }

}