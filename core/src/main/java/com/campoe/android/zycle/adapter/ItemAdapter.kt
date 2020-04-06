package com.campoe.android.zycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.eventhook.EventHook
import com.campoe.android.zycle.eventhook.Hookable
import com.campoe.android.zycle.ktx.cast
import com.campoe.android.zycle.ktx.inflate
import com.campoe.android.zycle.layout.Item
import com.campoe.android.zycle.layout.RecyclerItem
import com.campoe.android.zycle.viewholder.ViewHolder

internal class ItemAdapter<E : RecyclerItem<E>>(items: MutableList<E> = mutableListOf()) :
    Adapter<E>(items), Hookable<E, RecyclerView.ViewHolder> {

    private val viewHolderFactories: MutableMap<Int, Item<*, RecyclerView.ViewHolder>> =
        mutableMapOf()

    override val eventHooks: MutableList<EventHook<E, RecyclerView.ViewHolder>> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val item = viewHolderFactories[viewType]
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        val holder = item?.inflate(layoutInflater!!, parent) ?: throw RuntimeException()
        item.onCreate(holder)
        holder.cast<ViewHolder>()?.also {
            it.onCreateViewHolder(recyclerView!!)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: throw RuntimeException()
        eventHooks.forEach { hook -> hook.attach(holder, item, position) }
        item.onBind(holder, item)
        holder.cast<ViewHolder>()?.also {
            it.onBindViewHolder()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            getItem(holder.adapterPosition)?.onRecycle(holder) // TODO: use tag to get item instead of adapterPosition
            holder.cast<ViewHolder>()?.also {
                it.onViewRecycled(recyclerView!!)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position) ?: return super.getItemId(position)
        return item.getStableId(item, position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if (!viewHolderFactories.containsKey(it.viewType)) {
                viewHolderFactories[it.viewType] = it
            }
            it.viewType
        } ?: super.getItemViewType(position)
    }

    override fun getLayoutRes(viewType: Int): Int {
        return viewHolderFactories[viewType]?.layoutRes
            ?: throw RuntimeException()
    }

    override fun getStableId(item: E, position: Int): Long {
        return item.getStableId(item, position)
    }

}