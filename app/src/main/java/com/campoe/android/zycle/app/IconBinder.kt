package com.campoe.android.zycle.app

import androidx.recyclerview.widget.RecyclerView
import com.campoe.android.zycle.R
import com.campoe.android.zycle.binder.Binder
import com.mikepenz.iconics.IconicsColor.Companion.colorRes
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import kotlinx.android.synthetic.main.item_icon.view.*

class IconBinder<I : IIcon> : Binder<I, RecyclerView.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_icon

    override fun onBind(holder: RecyclerView.ViewHolder, item: I) {
        val itemView = holder.itemView
        itemView.icon.icon = IconicsDrawable(itemView.icon.context, item).apply {
            colorRes(R.color.colorOnSurface)
        }
        itemView.name.text = item.name
    }

    override fun onRecycle(holder: RecyclerView.ViewHolder) {
        val itemView = holder.itemView
        itemView.icon.setImageDrawable(null)
        itemView.name.text = null
    }

}