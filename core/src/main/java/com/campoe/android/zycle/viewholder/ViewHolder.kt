package com.campoe.android.zycle.viewholder

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.`typealias`.OnItemClickListener
import com.campoe.android.zycle.`typealias`.OnItemLongClickListener
import com.campoe.android.zycle.`typealias`.OnItemTouchListener

@ZycleDsl
open class ViewHolder<E : Any>(itemView: View) : RecyclerView.ViewHolder(itemView),
    IViewHolder<E> {

    var item: E? = null
        protected set

    @CallSuper
    override fun bind(
        item: E,
        onClick: OnItemClickListener<ViewHolder<E>>?,
        onLongClick: OnItemLongClickListener<ViewHolder<E>>?,
        onTouch: OnItemTouchListener<ViewHolder<E>>?
    ) {
        this.item = item
        onClick?.let { onClick(it) }
        onLongClick?.let { onLongClick(it) }
        onTouch?.let { onTouch(it) }
    }

    @CallSuper
    override fun unbind() {
        itemView.setOnClickListener(null)
        itemView.setOnLongClickListener(null)
        itemView.setOnTouchListener(null)
        item = null
    }

    inline fun onClick(crossinline f: OnItemClickListener<ViewHolder<E>>) =
        apply {
            itemView.setOnClickListener { f(this) }
        }

    inline fun onLongClick(crossinline f: OnItemLongClickListener<ViewHolder<E>>) =
        apply {
            itemView.setOnLongClickListener { f(this) }
        }

    inline fun onTouch(crossinline f: OnItemTouchListener<ViewHolder<E>>) =
        apply {
            itemView.setOnTouchListener { _, e -> f(this, e) }
        }

}