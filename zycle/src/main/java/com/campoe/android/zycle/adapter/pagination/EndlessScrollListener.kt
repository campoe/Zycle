package com.campoe.android.zycle.adapter.pagination

import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(private val layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    private var isLoading: Boolean = false
    private var currentPage: Int = 0
    private var previousTotal: Int = 0

    final override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE || dy <= 0) return
        val totalItemCount = layoutManager.itemCount
        if (isLoading && totalItemCount > previousTotal + 1) {
            isLoading = false
            onLoadingStateChanged(isLoading)
        }
        if (!isLoading && !recyclerView.canScrollVertically(1)) {
            isLoading = true
            onLoadingStateChanged(isLoading)
            onLoadNext(++currentPage)
        }
        previousTotal = totalItemCount
    }

    fun resetPageCount(page: Int = 0) {
        previousTotal = 0
        isLoading = true
        currentPage = page
        onLoadNext(currentPage)
    }

    protected abstract fun onLoadNext(currentPage: Int)
    protected abstract fun onLoadingStateChanged(isLoading: Boolean)

}