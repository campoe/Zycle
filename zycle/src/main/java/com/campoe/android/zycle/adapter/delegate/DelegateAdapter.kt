package com.campoe.android.zycle.adapter.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver

open class DelegateAdapter(private val delegateAdapter: Adapter) : Adapter() {

    private val observer: AdapterDataObserver =
        object : AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemRangeMoved(fromPosition, toPosition, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }
        }

    override val hasStableIds: Boolean = delegateAdapter.hasStableIds

    override fun getItemCount(): Int = delegateAdapter.getItemCount()
    override fun getItemViewType(position: Int): Int = delegateAdapter.getItemViewType(position)
    override fun getItemId(position: Int): Long = delegateAdapter.getItemId(position)
    override fun isEnabled(position: Int): Boolean = delegateAdapter.isEnabled(position)
    override fun onFirstObserverRegistered() {
        delegateAdapter.registerAdapterDataObserver(observer)
    }

    override fun onLastObserverUnregistered() {
        delegateAdapter.unregisterAdapterDataObserver(observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegateAdapter.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegateAdapter.onBindViewHolder(holder, position)

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) =
        delegateAdapter.onViewRecycled(holder)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = delegateAdapter.onBindViewHolder(holder, position, payloads)

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) =
        delegateAdapter.onViewDetachedFromWindow(holder)

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean =
        delegateAdapter.onFailedToRecycleView(holder)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) =
        delegateAdapter.onViewAttachedToWindow(holder)

    override fun getLayoutRes(viewType: Int): Int = delegateAdapter.getLayoutRes(viewType)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        delegateAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        delegateAdapter.onDetachedFromRecyclerView(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

}