package com.campoe.android.zycle.adapter.conditional

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.condition.Condition
import com.campoe.android.zycle.ktx.inflate
import com.campoe.android.zycle.viewholder.ViewHolder

internal class ConditionalElseAdapter(
    adapter: Adapter,
    condition: Condition,
    @LayoutRes private val layoutRes: Int
) :
    ConditionalAdapter(adapter, condition) {

    override val hasStableIds: Boolean = if (isVisible) super.hasStableIds else false

    override fun getItemCount(): Int {
        return if (isVisible) super.getItemCount()
        else 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isVisible) super.getItemViewType(position)
        else layoutRes
    }

    override fun getItemId(position: Int): Long {
        return if (isVisible) super.getItemId(position)
        else RecyclerView.NO_ID
    }

    override fun isEnabled(position: Int): Boolean {
        return if (isVisible) super.isEnabled(position)
        else position == 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isVisible) super.onCreateViewHolder(parent, viewType)
        else ViewHolder(parent.inflate(layoutInflater!!, viewType))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isVisible) super.onBindViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (isVisible) super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (isVisible) super.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (isVisible) super.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return if (isVisible) super.onFailedToRecycleView(holder)
        else false
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (isVisible) super.onViewAttachedToWindow(holder)
    }

    override fun getLayoutRes(viewType: Int): Int {
        return if (isVisible) super.getLayoutRes(viewType)
        else layoutRes
    }

    override fun onChanged(preItemCount: Int, postItemCount: Int) {
        if (isVisible) {
            notifyItemRemoved(0)
            notifyItemRangeInserted(0, postItemCount)
        } else {
            notifyItemRangeRemoved(0, preItemCount)
            notifyItemInserted(0)
        }
    }

}