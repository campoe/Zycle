package com.campoe.android.zycle.pagination.module

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.layout.Layout
import com.campoe.android.zycle.pagination.PaginationScrollListener
import com.campoe.android.zycle.viewholder.ViewHolder

class PaginationFooter<VH : ViewHolder>(@LayoutRes private val layoutRes: Int) : Layout<VH>() {

    private var onLoadNext: ((currentPage: Int) -> Unit)? = null
    private var itemCount: Int = 0

    override fun getLayoutRes(position: Int): Int {
        return layoutRes
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    fun onLoadNext(f: (currentPage: Int) -> Unit) =
        apply { onLoadNext = f }

    override fun onAttach(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager!!) {
            override fun onLoadNext(currentPage: Int) {
                onLoadNext?.invoke(currentPage)
            }

            override fun onLoadingStateChanged(isLoading: Boolean) {
                if (isLoading) {
                    itemCount = 1
                    recyclerView.adapter?.run {
                        notifyItemInserted(itemCount - 1)
                    }
                } else {
                    itemCount = 0
                    recyclerView.adapter?.run {
                        notifyItemRemoved(itemCount - 1)
                    }
                }
            }
        })
    }

}