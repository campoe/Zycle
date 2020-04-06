package com.campoe.android.zycle.emptiness.extension

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.extension.AdapterExtension

class EmptinessExtension(private val emptyView: View) : AdapterExtension() {

    private var emptinessObserver: RecyclerView.AdapterDataObserver? = null

    override fun onAttach(recyclerView: RecyclerView) {
        fun showEmptyView(visible: Boolean) {
            recyclerView.visibility = if (visible) View.GONE else View.VISIBLE
            emptyView.visibility = if (visible) View.VISIBLE else View.GONE
        }

        val adapter = recyclerView.adapter
        adapter?.also {
            emptinessObserver = object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    showEmptyView(it.itemCount == 0)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    showEmptyView(it.itemCount == 0)
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    showEmptyView(it.itemCount == 0)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    showEmptyView(it.itemCount == 0)
                }
            }
            it.registerAdapterDataObserver(emptinessObserver!!)
            showEmptyView(it.itemCount == 0)
        }
    }

    override fun onDetach(recyclerView: RecyclerView) {
        recyclerView.adapter?.unregisterAdapterDataObserver(emptinessObserver ?: return)
    }

}