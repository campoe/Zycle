package com.campoe.android.zycle.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.ZycleDsl
import com.campoe.android.zycle.adapter.Adapter
import com.campoe.android.zycle.extension.cast
import com.campoe.android.zycle.layout.Layout

@ZycleDsl
open class ViewHolder(
    itemView: View,
    private val layout: Layout<ViewHolder>
) :
    RecyclerView.ViewHolder(itemView),
    IViewHolder {

    val item: Any? // FIXME
        get() = layout.cast<Adapter<*, *>.ItemLayout>()?.getItem(this)

    override val itemPosition: Int // FIXME
        get() = layout.cast<Adapter<*, *>.ItemLayout>()?.getItemPosition(this)
            ?: RecyclerView.NO_POSITION

    override fun onCreateViewHolder(recyclerView: RecyclerView) {
        layout.onCreate(this)
    }

    override fun onBindViewHolder() {
        layout.onBind(this)
    }

    override fun onViewRecycled(recyclerView: RecyclerView) {
        layout.onRecycle(this)
    }

}