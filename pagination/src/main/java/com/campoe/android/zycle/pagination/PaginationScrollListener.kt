package com.campoe.android.zycle.pagination

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter

abstract class PaginationScrollListener(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    private var isLoading: Boolean = false
    private var currentPage: Int = 0
    private var previousTotal: Int = 0

    private var isVerticalOrientation: Boolean = true
    private var orientationHelper: OrientationHelper? = null

    private var footerAdapter: Adapter<*, *>? = null

    fun attach(recyclerView: RecyclerView) =
        apply { recyclerView.addOnScrollListener(this) }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val footerItemCount = footerAdapter?.itemCount ?: 0
        val firstVisibleItem = findFirstVisibleItemPosition(recyclerView)
        val visibleThreshold =
            findLastVisibleItemPosition(recyclerView) - firstVisibleItem - footerItemCount
        val visibleItemCount = recyclerView.childCount - footerItemCount
        val totalItemCount = layoutManager.itemCount - footerItemCount
        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }
        }
        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            isLoading = true
            onLoadNext(++currentPage)
        }
    }

    fun resetPageCount(page: Int = 0) {
        previousTotal = 0
        isLoading = true
        currentPage = page
        onLoadNext(currentPage)
    }

    protected abstract fun onLoadNext(currentPage: Int)

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(0, layoutManager.childCount)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
            child
        )
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(recyclerView.childCount - 1, -1)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
            child
        )
    }

    private fun findOneVisibleChild(
        fromIndex: Int,
        toIndex: Int
    ): View? {
        val canScrollVertically = layoutManager.canScrollVertically()
        if (canScrollVertically != isVerticalOrientation || orientationHelper == null) {
            isVerticalOrientation = canScrollVertically
            orientationHelper = if (isVerticalOrientation)
                OrientationHelper.createVerticalHelper(layoutManager)
            else OrientationHelper.createHorizontalHelper(layoutManager)
        }
        val orientationHelper = this.orientationHelper ?: return null
        val start = orientationHelper.startAfterPadding
        val end = orientationHelper.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1
        for (i in fromIndex until toIndex step next) {
            val child = layoutManager.getChildAt(i)
            if (child != null) {
                val childStart = orientationHelper.getDecoratedStart(child)
                val childEnd = orientationHelper.getDecoratedEnd(child)
                if (childStart < end && childEnd > start) {
                    return child
                }
            }
        }
        return null
    }

}