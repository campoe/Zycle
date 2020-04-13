package com.coenvk.android.zycle.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coenvk.android.zycle.adapter.observer.AdapterDataObserver

internal object EmptyAdapter : Adapter() {

    override val hasStableIds: Boolean = true

    override fun getItemCount(): Int = 0

    override fun getItemId(position: Int): Long = throw UnsupportedOperationException()

    override fun getItemViewType(position: Int): Int = throw UnsupportedOperationException()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        throw UnsupportedOperationException()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        throw UnsupportedOperationException()

    @SuppressLint("MissingSuperCall")
    override fun registerAdapterDataObserver(observer: AdapterDataObserver) = Unit

    @SuppressLint("MissingSuperCall")
    override fun unregisterAdapterDataObserver(observer: AdapterDataObserver) = Unit

    override fun getLayoutRes(viewType: Int): Int = throw UnsupportedOperationException()

}