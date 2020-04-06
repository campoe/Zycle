package com.campoe.android.zycle.pagination.extension

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campoe.android.zycle.viewholder.ViewHolder

//fun <E : Any, VH : ViewHolder> Adapter<E>.paginate(
//    @LayoutRes layoutRes: Int,
//    onLoadNext: (currentPage: Int) -> Unit
//) =
//    apply { footer(PaginationFooter<VH>(layoutRes).onLoadNext(onLoadNext)) }

internal fun RecyclerView.LayoutManager.findFirstVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findFirstVisibleItemPosition()
        is GridLayoutManager -> findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> findFirstVisibleItemPositions(null).min() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findFirstCompletelyVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findFirstCompletelyVisibleItemPosition()
        is GridLayoutManager -> findFirstCompletelyVisibleItemPosition()
        is StaggeredGridLayoutManager -> findFirstCompletelyVisibleItemPositions(null).min() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> findLastVisibleItemPositions(null).max() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}

internal fun RecyclerView.LayoutManager.findLastCompletelyVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastCompletelyVisibleItemPosition()
        is GridLayoutManager -> findLastCompletelyVisibleItemPosition()
        is StaggeredGridLayoutManager -> findLastCompletelyVisibleItemPositions(null).max() ?: 0
        else -> throw IllegalStateException("Layout manager of type ${this::class.java} is not supported.")
    }
}