package com.campoe.android.zycle.databinding.viewholder

import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.layout.Layout
import com.campoe.android.zycle.viewholder.ViewHolder

internal class BindingViewHolder<DB : ViewDataBinding>(
    internal val binding: DB,
    layout: Layout<ViewHolder>
) :
    ViewHolder(binding.root, layout) {

    override fun onCreateViewHolder(recyclerView: RecyclerView) {
        super.onCreateViewHolder(recyclerView)
        binding.addOnRebindCallback(object : OnRebindCallback<DB>() {
            override fun onCanceled(binding: DB) {
                if (recyclerView.isComputingLayout) {
                    return
                }
                val adapterPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    recyclerView.adapter?.notifyItemChanged(adapterPosition) // TODO: goes wrong with callback when headers
                }
            }

            override fun onPreBind(binding: DB): Boolean =
                recyclerView.isComputingLayout
        })
    }

    override fun onBindViewHolder() {
        super.onBindViewHolder()
        binding.executePendingBindings()
    }

}