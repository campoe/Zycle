package com.campoe.android.zycle.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.observer.AdapterDataObserver
import com.campoe.android.zycle.adapter.observer.ForwardingRecyclerObserver

internal class RecyclerAdapter(@JvmField internal val adapter: Adapter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val forwardingObserver: AdapterDataObserver = ForwardingRecyclerObserver(this)

    init {
        super.setHasStableIds(adapter.hasStableIds)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        throw RuntimeException()
    }

    override fun getItemCount(): Int = adapter.getItemCount()
    override fun getItemId(position: Int): Long = adapter.getItemId(position)
    override fun getItemViewType(position: Int): Int = adapter.getItemViewType(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        adapter.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        adapter.onBindViewHolder(holder, position)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) = adapter.onBindViewHolder(holder, position, payloads)

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        val hasObservers = hasObservers()
        super.registerAdapterDataObserver(observer)
        if (!hasObservers) {
            notifyDataSetChanged()
            adapter.registerAdapterDataObserver(forwardingObserver)
        }
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        if (!hasObservers()) {
            adapter.unregisterAdapterDataObserver(forwardingObserver)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        adapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        adapter.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean =
        adapter.onFailedToRecycleView(holder)

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        adapter.onViewRecycled(holder)
    }

}