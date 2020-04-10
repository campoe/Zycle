package com.campoe.android.zycle.adapter.pagination

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.ktx.inflate
import com.campoe.android.zycle.viewholder.ViewHolder

internal class EndlessScrollAdapter(
    @LayoutRes private val layoutRes: Int,
    private val onLoadNext: (currentPage: Int) -> Unit
) : Adapter() {

    private var itemCount: Int = 0

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(layoutInflater!!, viewType))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun getLayoutRes(viewType: Int): Int {
        return viewType
    }

    override fun getItemViewType(position: Int): Int = layoutRes

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager?.also { layoutManager ->
            recyclerView.addOnScrollListener(object :
                EndlessScrollListener(layoutManager) {
                override fun onLoadNext(currentPage: Int) {
                    this@EndlessScrollAdapter.onLoadNext(currentPage)
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

}