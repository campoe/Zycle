package com.campoe.android.zycle.databinding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.databinding.extension.dataBinding
import com.campoe.android.zycle.databinding.viewholder.BindingViewHolder
import com.campoe.android.zycle.viewholder.ViewHolder

internal class BindingAdapter<E : Any, DB : ViewDataBinding, VH : ViewHolder<E>>(items: MutableList<E> = mutableListOf()) :
    Adapter<E, VH>(items) {

    constructor(items: Array<out E>) : this(items.toMutableList())

    private val payloadType = Any()

    @Suppress("UNCHECKED_CAST")
    override fun instantiateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): VH {
        val binding =
            DataBindingUtil.inflate<DB>(layoutInflater, viewType, parent, false)
        return BindingViewHolder<E, DB>(binding) as VH
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.dataBinding?.addOnRebindCallback(object : OnRebindCallback<DB>() {
            override fun onCanceled(binding: DB) {
                if (recyclerView?.isComputingLayout != false) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, payloadType)
                }
            }

            override fun onPreBind(binding: DB): Boolean =
                recyclerView?.isComputingLayout ?: false
        })
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.dataBinding?.executePendingBindings()
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads.all { it == payloadType }) {
            holder.dataBinding?.executePendingBindings()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}

internal fun <E : Any, DB : ViewDataBinding> Array<out E>.toBindingAdapter(): Adapter<E, ViewHolder<E>> =
    BindingAdapter<E, DB, ViewHolder<E>>(this)

internal fun <E : Any, DB : ViewDataBinding> MutableList<E>.toBindingAdapter(): Adapter<E, ViewHolder<E>> =
    BindingAdapter<E, DB, ViewHolder<E>>(this)